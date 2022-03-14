package com.nbs.vektorrechner.ui.spurpunkteEbene;

/*import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nbs.vektorrechner.R;

public class SpurpunkteEbeneFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;



    public SpurpunkteEbeneFragment() {}

    public static SpurpunkteEbeneFragment newInstance(String param1, String param2) {
        SpurpunkteEbeneFragment fragment = new SpurpunkteEbeneFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_spurpunkte_ebene, container, false);
    }
}*/


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

import com.nbs.vektorrechner.R;

import java.util.ArrayList;

public class SpurpunkteEbeneFragment extends Fragment {

    //private HomeViewModel homeViewModel;

    private View rot;

    private void spurpunkte(double z1, double z2, double z3, double z4, double z5, double z6, double z7, double z8, double z9) {
        TextView antwort_1 = rot.findViewById(R.id.antwort_1);
        TextView antwort_2 = rot.findViewById(R.id.antwort_2);
        TextView antwort_3 = rot.findViewById(R.id.antwort_4);
        String x = "x", y="y", z="z";
        double n_x=z5*z9-z6*z8, n_y=z6*z7-z4*z9, n_z=z4*z8-z5*z7;
        double ska = skalar(n_x, n_y, n_z, z1, z2, z3);
        double x_koor = Math.round(ska*10000/n_x), y_koor = Math.round(ska*10000/n_y), z_koor = Math.round(ska*10000/n_z);
        x_koor /= 10000;
        y_koor /= 10000;
        z_koor /= 10000;

        String p1 = "( "+x_koor+" | 0 | 0 )", p2 = "( 0 | "+y_koor+" | 0 )", p3 = "( 0 | 0 | "+z_koor+" )";

        bestimmen(x_koor-z1, 0-z2, 0-z3, z4, z5, z6, z7, z8, z9, antwort_1, x, p1);
        bestimmen(0-z1, y_koor-z2, 0-z3, z4, z5, z6, z7, z8, z9, antwort_2, y, p2);
        bestimmen(0-z1, 0-z2, z_koor-z3, z4, z5, z6, z7, z8, z9, antwort_3, z, p3);

    }

    private double skalar(double z1, double z2, double z3, double z4, double z5, double z6) {
        double ska = z1*z4+z2*z5+z3*z6;
        return ska;
    }

    private double det(double z1, double z2, double z3, double z4) {
        double d = z1*z4-z2*z3;
        return d;
    }

    private void bestimmen(double z1, double z2, double z3, double z4, double z5, double z6, double z7, double z8, double z9, TextView text, String achse, String p) {
        double d1 = det(z4, z5, z7, z8), d2 = det(z1, z2, z7, z8), d3 = det(z4, z5, z1, z2);
        double l_f = 0, m_f = 0;
        boolean con = true;
        if(d1 == 0) {
            d1 = det(z4, z6, z7, z9);
            d2 = det(z1, z3, z7, z9);
            d3 = det(z4, z6, z1, z3);
            if (d1 == 0) {
                d1 = det(z5, z6, z8, z9);
                d2 = det(z2, z3, z8, z9);
                d3 = det(z5, z6, z2, z3);
                if (d1 == 0) {
                    con = false;
                }
            }
        }

        if(con) {
            l_f = Math.round(d2*10000/d1);
            m_f = Math.round(d3*10000/d1);
            l_f /= 10000;
            m_f /= 10000;
            text.setText("Spurpunkt_"+achse+p+" bei "+"λ = "+l_f+", μ = "+m_f);
        } else {
            text.setText("Sonderfall");
        }

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_spurpunkte_ebene, container, false);
        rot = root;

        Button berechnen = rot.findViewById(R.id.btn_berechnen);
        Button reset = rot.findViewById(R.id.btn_reset);

        int[][] entrys = {
                {R.id.v1_1, R.id.v1_2, R.id.v1_3},
                {R.id.v2_1, R.id.v2_2, R.id.v2_3},
                {R.id.v3_1, R.id.v3_2, R.id.v3_3},
        };
        final EditText e1= null, e2 = null, e3= null, e4= null, e5= null, e6= null, e7= null, e8= null, e9= null;
        final EditText [][] names = {{e1, e2, e3}, {e4, e5, e6}, {e7, e8, e9}};

        for(int i=0; i<entrys.length; i++) {
            for(int j = 0; j<entrys[i].length; j++) {
                names[i][j] = rot.findViewById(entrys[i][j]);
                (names[i][j]).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
            }
        }

        berechnen.setOnClickListener(new View.OnClickListener() {
            TextView antwort_1 = rot.findViewById(R.id.antwort_1);
            TextView antwort_2 = rot.findViewById(R.id.antwort_2);
            TextView antwort_3 = rot.findViewById(R.id.antwort_4);
            @Override
            public void onClick(View v) {
                antwort_1.setText("");
                antwort_2.setText("");
                antwort_3.setText("");
                boolean con = true;
                for(int i = 0; i<names.length; i++) {
                    for(int j = 0; j<names[i].length; j++) {
                        if(names[i][j].getText().toString().equals("")) {
                            antwort_1.setText("Alle Felder ausfüllen");
                            con = false;
                        }
                    }
                }

                if(con) {
                    spurpunkte(Double.parseDouble(names[0][0].getText().toString()), Double.parseDouble(names[0][1].getText().toString()), Double.parseDouble(names[0][2].getText().toString()),
                            Double.parseDouble(names[1][0].getText().toString()), Double.parseDouble(names[1][1].getText().toString()), Double.parseDouble(names[1][2].getText().toString()),
                            Double.parseDouble(names[2][0].getText().toString()), Double.parseDouble(names[2][1].getText().toString()), Double.parseDouble(names[2][2].getText().toString()));
                }

            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i< names.length; i++) {
                    for(int j = 0; j<names[i].length; j++) {
                        names[i][j].setText("");
                    }
                }
            }
        });

        return root;
    }

}


