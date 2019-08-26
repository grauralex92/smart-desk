package com.endava.smartdesk.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.endava.smartdesk.R;
import com.endava.smartdesk.model.TShirtSize;
import com.endava.smartdesk.model.TransportationType;

import java.util.ArrayList;
import java.util.List;

public class SpinnerUtils {

    private ArrayAdapter<CharSequence> getSpinnerAdapter(Context context, List<String> list, int layout) {
        return new ArrayAdapter(context, layout, list) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                }
                return true;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    SpannableString spanString = new SpannableString(list.get(position));
                    spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);
                    tv.setText(spanString);
                    tv.setTextColor(context.getResources().getColor(R.color.hint_color));
                }
                return view;
            }
        };
    }

    public void setTransportationSpinner(Context context, Spinner spinner) {
        List<String> transportationList = new ArrayList<>();

        for (TransportationType type : TransportationType.values()) {
            transportationList.add(type.getTransportationType());
        }

        ArrayAdapter<CharSequence> adapter = getSpinnerAdapter(context, transportationList, R.layout.transportation_spinner_item_view);
        adapter.setDropDownViewResource(R.layout.spinner_drop_down_view);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String transportation = transportationList.get(position);
                CheckedTextView spinnerView = ((Activity) context).findViewById(R.id.transportation_spinner_item_view);
                if (transportation.equals(TransportationType.BUS.getTransportationType())) {
                    spinnerView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_transportation_bus, 0, 0, 0);
                    spinnerView.setTextColor(context.getResources().getColor(R.color.endava_light_orange));
                } else if (transportation.equals(TransportationType.CAR.getTransportationType())) {
                    spinnerView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_transportation_car, 0, 0, 0);
                    spinnerView.setTextColor(context.getResources().getColor(R.color.endava_light_orange));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    public void setTShirtSizeSpinner(Context context, Spinner spinner) {
        List<String> tShirtSizeList = new ArrayList<>();

        for (TShirtSize size : TShirtSize.values()) {
            tShirtSizeList.add(size.getTShirtSize());
        }

        ArrayAdapter<CharSequence> adapter = getSpinnerAdapter(context, tShirtSizeList, R.layout.t_shirt_spinner_item_view);
        adapter.setDropDownViewResource(R.layout.spinner_drop_down_view);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String tShirtSize = tShirtSizeList.get(position);
                CheckedTextView spinnerView = ((Activity) context).findViewById(R.id.t_shirt_spinner_item_view);
                if(!tShirtSize.equals(TShirtSize.T_SHIRT_SIZE.getTShirtSize())){
                    spinnerView.setTextColor(context.getResources().getColor(R.color.endava_light_orange));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

}
