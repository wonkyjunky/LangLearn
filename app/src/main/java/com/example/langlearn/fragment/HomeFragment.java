package com.example.langlearn.fragment;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.langlearn.R;
import com.example.langlearn.Util;
import com.parse.ParseUser;

import static com.example.langlearn.Util.langCodes;

public class HomeFragment extends Fragment {

    EditText etTranslate;
    TextView tvResult;
    Button btTranslate;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner spinner = view.findViewById(R.id.spinner);


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Util.langNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        btTranslate = view.findViewById(R.id.btTranslate);
        etTranslate = view.findViewById(R.id.etTranslate);
        tvResult = view.findViewById(R.id.tvResult);

        btTranslate.setOnClickListener((View v) -> {

            String codeFrom = ParseUser.getCurrentUser().getString("nativelang");
            String codeTo = langCodes[spinner.getSelectedItemPosition()];
            String text = etTranslate.getText().toString();

            Util.translate(text, codeFrom, codeTo, (Message msg) -> {

                Bundle b = msg.getData();
                tvResult.setText(b.getString("result"));
                return true;

            });
        });
    }
}