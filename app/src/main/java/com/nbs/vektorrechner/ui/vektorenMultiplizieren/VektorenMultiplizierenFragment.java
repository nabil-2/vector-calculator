package com.nbs.vektorrechner.ui.vektorenMultiplizieren;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;

import com.nbs.vektorrechner.R;

import java.util.ArrayList;

public class VektorenMultiplizierenFragment extends Fragment {

    private VektorenMultiplizierenViewModel vektorenMultiplizierenViewModel;

    private View rot;

    private void mul() {
        TextView loesung = rot.findViewById(R.id.loesung);

        EditText e1 = rot.findViewById(R.id.eingabe_1);
        EditText e2 = rot.findViewById(R.id.eingabe_2);
        EditText e3 = rot.findViewById(R.id.eingabe_3);
        EditText e4 = rot.findViewById(R.id.eingabe_4);
        EditText e5 = rot.findViewById(R.id.eingabe_5);
        EditText e6 = rot.findViewById(R.id.eingabe_6);

        String [] liste_1 = {e2.getText().toString(), e3.getText().toString(), e1.getText().toString(), e2.getText().toString()};
        String [] liste_2 = {e5.getText().toString(), e6.getText().toString(), e4.getText().toString(), e5.getText().toString()};
        ArrayList<Double> erg = new ArrayList<>();

        for(int i = 0; i < 3; i++) {
            Double r;
            r = (Double.parseDouble(liste_1[i])*Double.parseDouble(liste_2[i+1]))-(Double.parseDouble(liste_1[i+1])*Double.parseDouble(liste_2[i]));
            erg.add(r);
        }

        loesung.setText("P( "+ erg.get(0) +" | "+ erg.get(1) +" | "+ erg.get(2) +" )");

        e1.setText("");
        e2.setText("");
        e3.setText("");
        e4.setText("");
        e5.setText("");
        e6.setText("");

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vektorenMultiplizierenViewModel = ViewModelProviders.of(this).get(VektorenMultiplizierenViewModel.class);
        View root = inflater.inflate(R.layout.fragment_vektorenmultiplizieren, container, false);
        rot = root;

        EditText e1 = rot.findViewById(R.id.eingabe_1);
        EditText e2 = rot.findViewById(R.id.eingabe_2);
        EditText e3 = rot.findViewById(R.id.eingabe_3);
        EditText e4 = rot.findViewById(R.id.eingabe_4);
        EditText e5 = rot.findViewById(R.id.eingabe_5);
        EditText e6 = rot.findViewById(R.id.eingabe_6);

        Button berechnen = rot.findViewById(R.id.berechnen);
        Button reset = rot.findViewById(R.id.reset);

        e1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        e2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        e3.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        e4.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        e5.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        e6.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);

        berechnen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!e1.getText().toString().equals("") && !e2.getText().toString().equals("") && !e3.getText().toString().equals("") && !e4.getText().toString().equals("") && !e5.getText().toString().equals("") && !e6.getText().toString().equals("")){

                    mul();
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView loesung = rot.findViewById(R.id.loesung);
                loesung.setText("");

                e1.setText("");
                e2.setText("");
                e3.setText("");
                e4.setText("");
                e5.setText("");
                e6.setText("");

            }
        });

        return root;
    }
}