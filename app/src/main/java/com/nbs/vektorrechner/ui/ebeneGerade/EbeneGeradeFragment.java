package com.nbs.vektorrechner.ui.ebeneGerade;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;

import com.nbs.vektorrechner.R;

import java.util.ArrayList;

public class EbeneGeradeFragment extends Fragment {

    private View rot;

    private void bestimmen(String e1, String e2, String e3, String e4, String e5, String e6, String e7, String e8, String e9, String eA, String eB, String eC, String eD, String eE, String eF) {
        boolean con = true;
        String[] parameter_1 = {e1, e2, e3, e4, e5, e6, e7, e8, e9, eA, eB, eC, eD, eE, eF};
        ArrayList<Double> parameter_2 = new ArrayList<>();
        for(int i = 0; i<parameter_1.length; i++) {
            parameter_2.add(Double.parseDouble(parameter_1[i].toString()));
        }
        con = check(parameter_2.get(0), parameter_2.get(1), parameter_2.get(2), parameter_2.get(9), parameter_2.get(10), parameter_2.get(11));

        if(con) {
            parallel(parameter_2.get(0), parameter_2.get(1), parameter_2.get(2), parameter_2.get(9), parameter_2.get(10), parameter_2.get(11));
        } else {
            nicht_parallel(parameter_2.get(0), parameter_2.get(1), parameter_2.get(2), parameter_2.get(3), parameter_2.get(4), parameter_2.get(5),
                    parameter_2.get(6), parameter_2.get(7), parameter_2.get(8), parameter_2.get(9), parameter_2.get(10), parameter_2.get(11), parameter_2.get(12), parameter_2.get(13), parameter_2.get(14));
        }
    }

    private boolean check(double z1, double z2, double z3, double z4, double z5, double z6) {

        Double[] parameter = {z1, z2, z3, z4, z5, z6};

        for(int i = 0; i<parameter.length; i++) {
            if(parameter[i] == 0) {
                if(i<3) {
                    if(parameter[i+3] != 0) {
                        return false;
                    }
                } else {
                    if(parameter[i-3] != 0) {
                        return false;
                    }
                }
            }
        }

        double f_1 = z1/z4, f_2 = z2/z5, f_3 = z3/z6;
        if(f_1 != f_2 || f_1 != f_3) {
            return false;
        }

        return true;
    }

    private void parallel(double z1, double z2, double z3, double z4, double z5, double z6) {
        TextView antwort_1 = rot.findViewById(R.id.antwort_1);

        if(z1 == z4 && z2 == z5 && z3 == z6) {
            antwort_1.setText("Die Gerade liegt in der Ebene");
        } else {
            antwort_1.setText("E und g liegen parallel zueinander");
        }
    }

    private void nicht_parallel(double z1, double z2, double z3, double z4, double z5, double z6, double z7, double z8, double z9, double zA, double zB, double zC, double zD, double zE, double zF) {
        TextView antwort_1 = rot.findViewById(R.id.antwort_1);
        TextView antwort_2 = rot.findViewById(R.id.antwort_2);
        TextView antwort_3 = rot.findViewById(R.id.antwort_4);

        double erg_1 = zA-z1, erg_2 = zB-z2, erg_3 = zC-z3;
        double det_1 = det(z4, z5, z6, z7, z8, z9, zD*(-1), zE*(-1), zF*(-1)),
                det_2 = det(erg_1, erg_2, erg_3, z7, z8, z9, zD*(-1), zE*(-1), zF*(-1)),
                det_3 = det(z4, z5, z6, erg_1, erg_2, erg_3, zD*(-1), zE*(-1), zF*(-1)),
                det_4 = det(z4, z5, z6, z7, z8, z9, erg_1, erg_2, erg_3);

        if(det_1 == 0) {
            if(det_2 == 0 && det_3 == 0 && det_4 == 0) {
                antwort_1.setText("Es liegen unendlich viele Schnittpunkte vor");
            } else {
                antwort_1.setText("Es liegen keine Schnittpunkte vor");
            }
        } else{
            double l_f = Math.round(det_2*100000000/det_1), m_f = Math.round(det_3*100000000/det_1), a_f = Math.round(det_4*100000000/det_1);
            l_f /= 100000000;
            m_f /= 100000000;
            a_f /= 100000000;

            double x_koor = Math.round((zA + (a_f*zD))*10000), y_koor = Math.round((zB + (a_f*zE))*10000), z_koor = Math.round((zC + (a_f*zF))*10000);
            double n_x = z5*z9 - z6*z8, n_y = z6*z7-z4*z9, n_z = z4*z8-z5*z7;
            double l_1 = laenge(n_x, n_y, n_z), l_2 = laenge(zD, zE, zF);
            double ska = skalar(n_x, n_y, n_z, zD, zE, zF);
            double phi = Math.round(winkel(l_1, l_2, ska)*10000);
            x_koor /= 10000;
            y_koor /= 10000;
            z_koor /= 10000;
            l_f = Math.round(l_f*10000);
            m_f = Math.round(m_f*10000);
            a_f = Math.round(a_f*10000);
            l_f /= 10000;
            m_f /= 10000;
            a_f /= 10000;
            phi /= 10000;

            if(phi<0) {
                phi *= (-1);
            }

            antwort_1.setText("Die Gerade schneidet die Ebene im Punkt S( "+x_koor+" | "+y_koor+" | "+z_koor+" )");
            antwort_2.setText("λ ="+l_f+" μ= "+m_f+" η= "+a_f);
            antwort_3.setText("Der Schnittwinkel beträgt etwa "+phi+"°");
        }

    }

    private double det(double z1, double z2, double z3, double z4, double z5, double z6, double z7, double z8, double z9) {
        double determinante = (z1*z5*z9)+(z4*z8*z3)+(z7*z2*z6)-(z7*z5*z3)-(z8*z6*z1)-(z9*z2*z4);
        return determinante;
    }

    private double winkel(double l1, double l2, double ska) {
        double alpha = Math.asin(ska/(l1*l2));
        alpha = Math.toDegrees(alpha);
        double phi = alpha;

        return phi;
    }

    private double laenge(double z1, double z2, double z3) {
        double l = Math.sqrt(Math.pow(z1, 2)+Math.pow(z2, 3)+Math.pow(z3, 2));
        return l;
    }

    private double skalar(double z1, double z2, double z3, double z4, double z5, double z6) {
        double ska = (z1*z4)+(z2*z5)+(z3*z6);
        return ska;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_ebene_gerade, container, false);
        rot = root;

        Button berechnen = rot.findViewById(R.id.btn_berechnen);
        Button reset = rot.findViewById(R.id.btn_reset);

        int[][] entrys = {{R.id.v1_1, R.id.v1_2, R.id.v1_3},
                {R.id.v2_1, R.id.v2_2, R.id.v2_3},
                {R.id.v3_1, R.id.v3_2, R.id.v3_3},
                {R.id.v4_1, R.id.v4_2, R.id.v4_3},
                {R.id.v5_1, R.id.v5_2, R.id.v5_3}
        };

        final EditText e1= null, e2 = null, e3= null, e4= null, e5= null, e6= null, e7= null, e8= null, e9= null, eA= null, eB= null, eC= null, eD= null, eE= null, eF= null;
        final EditText [][] names = {{e1, e2, e3}, {e4, e5, e6}, {e7, e8, e9}, {eA, eB, eC}, {eD, eE, eF}};

        for(int i=0; i<entrys.length; i++) {
            for(int j = 0; j<entrys[i].length; j++) {
                names[i][j] = root.findViewById(entrys[i][j]);
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
                            con = false;
                            antwort_1.setText("Alle Felder Ausfüllen!");
                        }
                    }
                }

                if(con) {
                    bestimmen(names[0][0].getText().toString(), names[0][1].getText().toString(), names[0][2].getText().toString(),
                            names[1][0].getText().toString(), names[1][1].getText().toString(), names[1][2].getText().toString(),
                            names[2][0].getText().toString(), names[2][1].getText().toString(), names[2][2].getText().toString(),
                            names[3][0].getText().toString(), names[3][1].getText().toString(), names[3][2].getText().toString(),
                            names[4][0].getText().toString(), names[4][1].getText().toString(), names[4][2].getText().toString());
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i<names.length; i++) {
                    for(int j = 0; j<names[i].length; j++) {
                        names[i][j].setText("");
                    }
                }
            }
        });

        return root;
    }
}