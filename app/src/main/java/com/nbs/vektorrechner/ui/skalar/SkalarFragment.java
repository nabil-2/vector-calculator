package com.nbs.vektorrechner.ui.skalar;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;

import com.nbs.vektorrechner.R;

public class SkalarFragment extends Fragment {

    private SkalarViewModel skalarViewModel;

    private View rot;

    private void rechnen(double z1, double z2, double z3, double z4, double z5, double z6) {
        EditText faktor_1 = rot.findViewById(R.id.faktor_1);
        EditText faktor_2 = rot.findViewById(R.id.faktor_2);

        TextView antwort = rot.findViewById(R.id.antwort_2);

        double erg = 0;
        double f_1;
        double f_2;

        Double liste [] = {z1, z2, z3, z4, z5, z6};

        if(faktor_1.getText().toString().equals("")) {
            f_1 = 1;
        } else {
            f_1 = Double.parseDouble(faktor_1.getText().toString());
        }

        if(faktor_2.getText().toString().equals("")) {
            f_2 = 1;
        } else {
            f_2 = Double.parseDouble(faktor_2.getText().toString());
        }

        for(int i=0; i<3; i++) {
            erg += (liste[i]*f_1)*(liste[i+3]*f_2);
        }

        antwort.setText("Das Produkt ist: "+erg);

    }

    private void clear() {
        EditText faktor_1 = rot.findViewById(R.id.faktor_1);
        EditText faktor_2 = rot.findViewById(R.id.faktor_2);
        EditText e1 = rot.findViewById(R.id.v_1);
        EditText e2 = rot.findViewById(R.id.v_2);
        EditText e3 = rot.findViewById(R.id.v_3);
        EditText e4 = rot.findViewById(R.id.v__1);
        EditText e5 = rot.findViewById(R.id.v__2);
        EditText e6 = rot.findViewById(R.id.v__3);

        faktor_1.setText("");
        faktor_2.setText("");
        e1.setText("");
        e2.setText("");
        e3.setText("");
        e4.setText("");
        e5.setText("");
        e6.setText("");

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        skalarViewModel = ViewModelProviders.of(this).get(SkalarViewModel.class);
        View root = inflater.inflate(R.layout.fragment_skalar, container, false);

        rot = root;

        EditText faktor_1 = rot.findViewById(R.id.faktor_1);
        EditText faktor_2 = rot.findViewById(R.id.faktor_2);
        EditText e1 = rot.findViewById(R.id.v_1);
        EditText e2 = rot.findViewById(R.id.v_2);
        EditText e3 = rot.findViewById(R.id.v_3);
        EditText e4 = rot.findViewById(R.id.v__1);
        EditText e5 = rot.findViewById(R.id.v__2);
        EditText e6 = rot.findViewById(R.id.v__3);

        Button berechnen = rot.findViewById(R.id.btnn_berechnen);
        Button cl = rot.findViewById(R.id.btnn_clear);

        faktor_1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        faktor_2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        e1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        e2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        e3.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        e4.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        e5.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        e6.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);

        berechnen.setOnClickListener(new View.OnClickListener() {
            EditText e1 = rot.findViewById(R.id.v_1);
            EditText e2 = rot.findViewById(R.id.v_2);
            EditText e3 = rot.findViewById(R.id.v_3);
            EditText e4 = rot.findViewById(R.id.v__1);
            EditText e5 = rot.findViewById(R.id.v__2);
            EditText e6 = rot.findViewById(R.id.v__3);

            @Override
            public void onClick(View v) {
                if(!e1.getText().toString().equals("") && !e2.getText().toString().equals("") && !e3.getText().toString().equals("") && !e4.getText().toString().equals("") && !e5.getText().toString().equals("") && !e6.getText().toString().equals("")) {
                    rechnen(Double.parseDouble(e1.getText().toString()), Double.parseDouble(e2.getText().toString()), Double.parseDouble(e3.getText().toString()), Double.parseDouble(e4.getText().toString()), Double.parseDouble(e5.getText().toString()), Double.parseDouble(e6.getText().toString()));
                }
            }
        });

        cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });

        return root;
    }
}