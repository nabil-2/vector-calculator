package com.nbs.vektorrechner.ui.spatprodukt;


import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;

import com.nbs.vektorrechner.LEScalculator;
import com.nbs.vektorrechner.R;

import java.util.ArrayList;

public class SpatproduktFragment extends Fragment {

    private View rot;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragement_spatprodukt, container, false);
        rot = root;

        Button berechnen = rot.findViewById(R.id.btn_berechnen);
        Button reset = rot.findViewById(R.id.btn_reset);

        int[][] entrys = {
                {R.id.v1_1, R.id.v1_2, R.id.v1_3},
                {R.id.v2_1, R.id.v2_2, R.id.v2_3},
                {R.id.v3_1, R.id.v3_2, R.id.v3_3},
        };
        final EditText ets[][] = new EditText[entrys.length][entrys[0].length];

        for(int i=0; i<entrys.length; i++) {
            for(int j = 0; j<entrys[i].length; j++) {
                ets[i][j] = rot.findViewById(entrys[i][j]);
                (ets[i][j]).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
            }
        }

        berechnen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i<ets.length; i++) {
                    for(int j = 0; j<ets[i].length; j++) {
                        if(ets[i][j].getText().toString().equals("")) {
                            TextView result = rot.findViewById(R.id.result);
                            result.setText("Alle Felder Ausfüllen!");
                            return;
                        }
                    }
                }
                float[][] vals = new float[entrys.length][entrys[0].length];
                for(int i=0; i<vals.length; i++) {
                    for(int j=0; j<vals[i].length; j++) {
                        vals[j][i] = Float.parseFloat(ets[i][j].getText().toString());
                    }
                }
                LEScalculator LES = new LEScalculator(null, new float[]{});
                float ret = LES.det(vals);
                if(ret<0) ret *= -1f;
                TextView result = rot.findViewById(R.id.result);
                result.setText("Das Spatprodukt ist: " + ret);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i< ets.length; i++) {
                    for(int j = 0; j<ets[i].length; j++) {
                        ets[i][j].setText("");
                    }
                }
            }
        });

        return root;
    }

}


