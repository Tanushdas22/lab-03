package com.example.listycitylab3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {

    interface AddCityDialogListener {
        void addCity(City city);
        void editCity(int position, City city);
    }

    private AddCityDialogListener listener;

    private static final String ARG_CITY_NAME = "city_name";
    private static final String ARG_PROVINCE_NAME = "province_name";
    private static final String ARG_POSITION = "position";

    // Factory method for adding
    public static AddCityFragment newInstance() {
        return new AddCityFragment();
    }

    // Factory method for editing
    public static AddCityFragment newInstance(String cityName, String provinceName, int position) {
        AddCityFragment fragment = new AddCityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CITY_NAME, cityName);
        args.putString(ARG_PROVINCE_NAME, provinceName);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        // Read arguments once and make them final so lambdas can capture them
        Bundle args = getArguments();
        final String cityName = args != null ? args.getString(ARG_CITY_NAME) : null;
        final String provinceName = args != null ? args.getString(ARG_PROVINCE_NAME) : null;
        final int position = args != null ? args.getInt(ARG_POSITION, -1) : -1;

        if (cityName != null) {
            editCityName.setText(cityName);
            editProvinceName.setText(provinceName);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(cityName == null ? "Add a city" : "Edit city")
                .setNegativeButton("Cancel", null)
                .setPositiveButton(cityName == null ? "Add" : "Save", (dialog, which) -> {
                    String newCityName = editCityName.getText().toString();
                    String newProvinceName = editProvinceName.getText().toString();
                    City city = new City(newCityName, newProvinceName);
                    if (cityName == null) {
                        listener.addCity(city);
                    } else {
                        listener.editCity(position, city);
                    }
                })
                .create();
    }
}
