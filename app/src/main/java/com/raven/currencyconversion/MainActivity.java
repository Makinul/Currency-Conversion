package com.raven.currencyconversion;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.raven.currencyconversion.adapter.CountryAdapter;
import com.raven.currencyconversion.network.ApiClient;
import com.raven.currencyconversion.network.ApiService;
import com.raven.currencyconversion.network.NetworkConnection;
import com.raven.currencyconversion.network.model.Rate;
import com.raven.currencyconversion.network.model.RateObject;
import com.raven.currencyconversion.network.model.Tax;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.inputField)
    EditText inputField;

    @BindView(R.id.countries)
    Spinner countries;

    @BindView(R.id.taxTypeLay)
    LinearLayout taxTypeLay;

    @BindView(R.id.result)
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchAllRates();
            }
        });

        inputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateResultView();
            }
        });

        fetchAllRates();
    }

    private RateObject rateObject;

    private void fetchAllRates() {
        if (!NetworkConnection.getInstance().isNetworkAvailable(this)) {
//            Snackbar.make(taxTypeLay, "There has no internet connection, please check your connectivity.", Snackbar.LENGTH_LONG).show();
            result.setText("There has no internet connection, please check your connectivity.");
            return;
        }

        showProgressDialog();
        ApiService apiService = ApiClient.getClient(getApplicationContext()).create(ApiService.class);

        // Fetching all rates
        apiService.fetchAllRate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<RateObject>() {
                    @Override
                    public void onSuccess(RateObject rateObj) {
                        closeProgressDialog();
                        rateObject = rateObj;
                        loadData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        closeProgressDialog();
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private CountryAdapter countryAdapter;

    private void loadData() {
        if (rateObject == null || rateObject.getRates() == null)
            return;

        countryAdapter = new CountryAdapter(this, R.layout.simple_spinner_item, rateObject.getRates());
        countries.setAdapter(countryAdapter);
        countries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCountry = countryAdapter.getItem(position);
                updateTaxView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private Rate selectedCountry;

    private void updateTaxView() {
        if (selectedCountry == null)
            return;

        if (taxTypeLay.getChildCount() > 0)
            taxTypeLay.removeAllViews();

        RadioGroup group = new RadioGroup(this);
        group.setOrientation(RadioGroup.VERTICAL);

        ArrayList<Tax> periods = selectedCountry.getPeriods();

        if (periods == null || periods.isEmpty())
            return;

        // here we select 0 index
        // because there has no other element in the period list
        // and also we are not going to add any functionality for other index
        Tax tax = periods.get(0);
        if (tax == null)
            return;

        final HashMap<String, Double> rates = tax.getRates();
        if (rates == null)
            return;

        int count = 0;
        int defaultId = 0;
        for (final String key : rates.keySet()) {
            final RadioButton radioButton = new RadioButton(this);
            radioButton.setId(count);
            radioButton.setText(key + " (" + rates.get(key) + "%)");
            if (key.equals("standard"))
                defaultId = count;

            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        Double newTaxRate = rates.get(key);
                        taxType = key;

                        if (newTaxRate != null) {
                            taxRate = newTaxRate;
                        }
                        if (inputField.getText().toString().trim().isEmpty()) {
                            result.setText(R.string.please_insert_desire_value_what_you_want_to_convert);
                            return;
                        }

                        updateResultView();
                    }
                }
            });
            count++;
            group.addView(radioButton);
        }

        group.check(defaultId);

        taxTypeLay.addView(group);
    }

    private String taxType;
    private double taxRate;

    private void updateResultView() {
        if (taxType == null)
            return;

        double inputtedValue = 0;
        try {
            inputtedValue = Double.parseDouble(inputField.getText().toString());
        } catch (Exception ignored) {

        }

        final double finalTax;
        if (inputtedValue == 0) {
            finalTax = 0;
        } else {
            finalTax = (inputtedValue / 100) * taxRate;
        }
        result.setText(getString(R.string.calculated_tax, String.valueOf(finalTax), String.valueOf(inputtedValue), taxType.toUpperCase(), selectedCountry.getName()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ProgressDialog progressDialog;

    private void showProgressDialog() {
        showProgressDialog("", getString(R.string.loading));
    }

    private void showProgressDialog(String message) {
        showProgressDialog("", message);
    }

    private void showProgressDialog(String title, String message) {
        showProgressDialog(title, message, false);
    }

    private void showProgressDialog(String title, String message, boolean cancelable) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(this, title, message);
            progressDialog.setCancelable(cancelable);
        } else {
            progressDialog.show();
        }
    }

    private void closeProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
