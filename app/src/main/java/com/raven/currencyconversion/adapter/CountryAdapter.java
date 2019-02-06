package com.raven.currencyconversion.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.raven.currencyconversion.R;
import com.raven.currencyconversion.network.model.Rate;

import java.util.ArrayList;

public class CountryAdapter extends ArrayAdapter<Rate> {

    private ArrayList<Rate> arrayList;
    private LayoutInflater inflater;

    public CountryAdapter(Context context, int textViewResourceId, ArrayList<Rate> arrayList) {
        super(context, textViewResourceId, arrayList);
        this.arrayList = arrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(ArrayList<Rate> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        View row = inflater.inflate(R.layout.simple_spinner_item, parent, false);

        Rate rate = arrayList.get(position);

        TextView itemName = row.findViewById(R.id.itemName);
        String name = rate.getName() + " (" + rate.getCode() + ")";
        itemName.setText(name);
        return row;
    }
}