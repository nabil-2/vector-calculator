package com.nbs.vektorrechner.ui.geradeGerade;


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

public class GeradeGeradeFragment extends Fragment {

    private View rot;

    private boolean linearDependent(float[] v1, float[] v2) {
        float k = v1[0]/v2[0];
        if(v2[1]*k == v1[1] && v2[2]*k == v1[2]) {
            return true;
        }
        return false;
    }

    private void calculate(float[][] g1, float[][] g2) {
        TextView result = rot.findViewById(R.id.result);
        float supportVec[] = g2[0];
        for(int i=0; i<supportVec.length; i++) {
            supportVec[i] -= g1[0][i];
        }
        if(linearDependent(g1[1], g2[1])) {
            if(linearDependent(supportVec, g2[1])) {
                result.setText("Geraden liegen ineinander");
            } else {
                result.setText("Geraden sind parallel");
            }
        } else {
            float[][] matrix = {
                    {g1[1][0], g2[1][0]},
                    {g1[1][1], g2[1][1]}
            };
            LEScalculator LES = new LEScalculator(matrix, supportVec);
            float[] ret = LES.calculate();
            boolean unsolveable = (ret.length == 1);
            if(!unsolveable) {
                if((g1[1][2]*ret[0] + g2[1][2]*ret[1]) == supportVec[2]) {
                    float[] point = g1[0];
                    for(int i=0; i<point.length; i++) {
                        point[i] += ret[0]*g1[1][1];
                    }
                    result.setText("Der Schnittpunkt der Geraden ist:\n P(" + point[0] + "/"+point[1]+"/" + point[2] + ")");
                } else unsolveable = true;
            }
            if(unsolveable) {
                result.setText("Geraden sind windschief");
            }
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragement_gerade_gerade, container, false);
        rot = root;

        Button berechnen = rot.findViewById(R.id.btn_berechnen);
        Button reset = rot.findViewById(R.id.btn_reset);

        int[][] entrys = {
                {R.id.v1_1, R.id.v1_2, R.id.v1_3},
                {R.id.v2_1, R.id.v2_2, R.id.v2_3},
                {R.id.v3_1, R.id.v3_2, R.id.v3_3},
                {R.id.v4_1, R.id.v4_2, R.id.v4_3},
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
                        vals[i][j] = Float.parseFloat(ets[i][j].getText().toString());
                    }
                }
                calculate(new float[][]{vals[0], vals[1]}, new float[][]{vals[2], vals[3]});
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


