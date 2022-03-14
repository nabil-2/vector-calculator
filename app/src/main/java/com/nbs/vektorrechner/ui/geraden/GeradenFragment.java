package com.nbs.vektorrechner.ui.geraden;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

import java.util.ArrayList;

public class GeradenFragment extends Fragment {

    private GeradenViewModel geradenViewModel;

    private View rot;

    private void gefüllt() {}

    private boolean check(double z1, double z2, double z3, double z4, double z5, double z6) {
        Double[] ueberprüfen = {z1, z2, z3, z4, z5, z6};
        boolean condition = true;
        double f_1, f_2, f_3;

        for(int i = 0; i<ueberprüfen.length; i++) {
            if(ueberprüfen[i] == 0 && i<3) {
                if(ueberprüfen[i+3] != 0) {
                    condition = false;
                    return condition;
                }
            } else if(ueberprüfen[i] == 0 && i>=3) {
                if(ueberprüfen[i-3] != 0) {
                    condition = false;
                    return condition;
                }
            }
        }

        if(z1 == 0 || z4 == 0) {
            f_1 = 0;
        }  else {
            f_1 = Math.round(z1*10000/z4);
            f_1 /= 10000;
        }

        if(z2 == 0 || z5 == 0) {
            f_2 = 0;
        } else {
            f_2 = Math.round(z2*10000/z5);
            f_2 /= 10000;
        }
        if(z3 == 0 || z6 == 0) {
            f_3 = 0;
        } else {
            f_3 = Math.round(z3*10000/z6);
            f_3 /= 10000;
        }

        if(f_1 != f_2 || f_1 != f_3) {
            condition = false;
        }

        return condition;
    }

    private void parallel(String z1, String z2, String z3, String z4, String z5, String z6) {
        TextView antwort_1 = rot.findViewById(R.id.antwort_1);

        if(z1.equals(z4) && z2.equals(z5) && z3.equals(z6)) {
            antwort_1.setText("Die Geraden sind identisch");
        } else {
            antwort_1.setText("Die Geraden liegen parallel zueinander");
        }
    }

    private void nicht_parallel(String z1, String z2, String z3, String z4, String z5, String z6, String z7, String z8, String z9, String zA, String zB, String zC) {
        TextView antwort_1 = rot.findViewById(R.id.antwort_1);
        TextView antwort_2 = rot.findViewById(R.id.antwort_2);
        boolean condition;
        double m_f = 0, l_f = 0;

        String [] parameter_1 = {z1, z2, z3, z4, z5, z6, z7, z8, z9, zA, zB, zC};
        ArrayList<Double> parameter_2 = new ArrayList<>();
        for(int i = 0; i<parameter_1.length; i++) {
            parameter_2.add(Double.parseDouble(parameter_1[i]));
        }

        Double erg_1 = parameter_2.get(0) - parameter_2.get(6), erg_2 = parameter_2.get(1) - parameter_2.get(7), erg_3 = parameter_2.get(2) - parameter_2.get(8);

        double d_1 = (parameter_2.get(9)*(-1)*parameter_2.get(4) - parameter_2.get(10)*(-1)*parameter_2.get(3)),
                d_2 = (erg_1*(-1)*parameter_2.get(4) - erg_2*(-1)*parameter_2.get(3)),
                d_3 =(parameter_2.get(9)*erg_2 - parameter_2.get(10)*erg_1);

        if(d_1 == 0) {
            d_1 = (parameter_2.get(9)*(-1)*parameter_2.get(5) - parameter_2.get(11)*(-1)*parameter_2.get(3));
            d_2 = (erg_1*(-1)*parameter_2.get(5) - erg_2*(-1)*parameter_2.get(3));
            d_3 = (parameter_2.get(9)*erg_2 - parameter_2.get(11)*erg_1);

            if(d_1 == 0) {
                d_1 = (parameter_2.get(10)*(-1)*parameter_2.get(5) - parameter_2.get(11)*(-1)*parameter_2.get(4));
                d_2 = (erg_1*(-1)*parameter_2.get(5) - erg_2*(-1)*parameter_2.get(4));
                d_3 = (parameter_2.get(10)*erg_2 - parameter_2.get(11)*erg_1);

                if(d_1 == 0) {
                    antwort_1.setText("Die Geraden liegen Windschief zueinander");
                    condition = false;
                }
            }
        }

        m_f = Math.round(d_2*10000/d_1);
        m_f /= 10000;
        l_f = Math.round(d_3*10000/d_1);
        l_f /= 10000;
        condition = probe(erg_1, erg_2, erg_3, parameter_2.get(9), parameter_2.get(10), parameter_2.get(11), parameter_2.get(3), parameter_2.get(4), parameter_2.get(5), m_f, l_f);
        if(condition) {
            Double x_koor = Double.parseDouble(z1)+l_f*Double.parseDouble(z4), y_koor = Double.parseDouble(z2)+l_f*Double.parseDouble(z5), z_koor = Double.parseDouble(z3)+l_f*Double.parseDouble(z6);

            antwort_1.setText("Die Geraden haben einen gemeinsamen Schnittpunkt: S( "+x_koor+" | "+y_koor+" | "+z_koor+" )");
            antwort_2.setText("λ = "+l_f+", μ= "+m_f);
            winkel(z4, z5, z6, zA, zB, zC);
        } else {
            antwort_1.setText("Die Geraden liegen Windschief zueinander");
        }
    }

    private boolean probe(Double e1, Double e2, Double e3, Double a, Double b, Double c, Double z4, Double z5, Double z6, double f_1, double f_2) {
        boolean condition = true;

        if(a*f_1-z4*f_2 != e1 || b*f_1-z5*f_2 != e2 || c*f_1-z6*f_2 != e3) {
            condition = false;
        }

        return condition;
    }

    private void winkel(String z1, String z2, String z3, String z4, String z5, String z6) {
        TextView antwort_4 = rot.findViewById(R.id.antwort_4);

        String [] parameter_1 = {z1, z2, z3, z4, z5, z6};
        ArrayList<Double> parameter_2 = new ArrayList<>();
        for(int i = 0; i<parameter_1.length; i++) {
            parameter_2.add(Double.parseDouble(parameter_1[i]));
        }

        double s_produkt = skalar(parameter_2.get(0), parameter_2.get(1), parameter_2.get(2), parameter_2.get(3), parameter_2.get(4), parameter_2.get(5));
        double l_1 = laenge(parameter_2.get(0), parameter_2.get(1), parameter_2.get(2)), l_2 = laenge(parameter_2.get(3), parameter_2.get(4), parameter_2.get(5));
        double phi = s_produkt/(l_1*l_2);
        phi = Math.acos(phi);
        phi = Math.toDegrees(phi);
        phi = Math.round(phi*10000);
        phi /= 10000;
        antwort_4.setText("Der Schnittwinkel beträgt etwa "+phi+"°");
    }

    private double skalar(Double z1, Double z2, Double z3, Double z4, Double z5, Double z6) {
        double z = 0;
        Double [] werte = {z1, z2, z3, z4, z5, z6};
        for(int i = 0; i<3; i++) {
            z += werte[i]*werte[i+3];
        }
        return z;
    }

    private Double laenge(Double z1, Double z2, Double z3) {
        Double z = Math.pow(z1, 2) + Math.pow(z2, 2) + Math.pow(z3, 2);
        z = Math.sqrt(z);
        return z;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        geradenViewModel = ViewModelProviders.of(this).get(GeradenViewModel.class);
        View root = inflater.inflate(R.layout.fragment_geraden, container, false);

        rot = root;

        Button berechnen = rot.findViewById(R.id.btn_berechnen);
        Button clear = rot.findViewById(R.id.btn_reset);

        int[][] entrys = {{R.id.v1_1, R.id.v1_2, R.id.v1_3},
                {R.id.v2_1, R.id.v2_2, R.id.v2_3},
                {R.id.v3_1, R.id.v3_2, R.id.v3_3},
                {R.id.v4_1, R.id.v4_2, R.id.v4_3}
        };

        final View e1= null, e2 = null, e3= null, e4= null, e5= null, e6= null, e7= null, e8= null, e9= null, eA= null, eB= null, eC= null;
        final EditText [][] names = {{(EditText) e1, (EditText) e2, (EditText) e3}, {(EditText) e4, (EditText) e5, (EditText) e6}, {(EditText) e7, (EditText) e8, (EditText) e9}, {(EditText) eA, (EditText) eB, (EditText) eC}};

        for(int i=0; i<entrys.length; i++) {
            for(int j = 0; j<entrys[i].length; j++) {
                names[i][j] = root.findViewById(entrys[i][j]);
                ((EditText) names[i][j]).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
            }
        }

        berechnen.setOnClickListener(new View.OnClickListener() {
            TextView antwort_1 = rot.findViewById(R.id.antwort_1);
            TextView antwort_2 = rot.findViewById(R.id.antwort_2);
            TextView antwort_4 = rot.findViewById(R.id.antwort_4);
            @Override
            public void onClick(View v) {
                antwort_1.setText("");
                antwort_2.setText("");
                antwort_4.setText("");
                boolean con_1 = true, con_2 = false;

                for(int i = 0; i<names.length; i++) {
                    for(int j = 0; j<names[i].length; j++) {
                        if(names[i][j].getText().toString().equals("")) {
                            con_1 = false;
                        }
                    }
                }

                if(con_1) {
                    con_2 = check(Double.parseDouble(names[1][0].getText().toString()), Double.parseDouble(names[1][1].getText().toString()), Double.parseDouble(names[1][2].getText().toString()),
                            Double.parseDouble(names[3][0].getText().toString()), Double.parseDouble(names[3][1].getText().toString()), Double.parseDouble(names[3][2].getText().toString()));

                    if(con_2) {
                        parallel(names[0][0].getText().toString(), names[0][1].getText().toString(), names[0][2].getText().toString(),
                                names[2][0].getText().toString(), names[2][1].getText().toString(), names[2][2].getText().toString());
                    } else {
                        nicht_parallel(names[0][0].getText().toString(), names[0][1].getText().toString(), names[0][2].getText().toString(),
                                names[1][0].getText().toString(), names[1][1].getText().toString(), names[1][2].getText().toString(),
                                names[2][0].getText().toString(), names[2][1].getText().toString(), names[2][2].getText().toString(),
                                names[3][0].getText().toString(), names[3][1].getText().toString(), names[3][2].getText().toString());
                    }
                } else {
                    antwort_1.setText("Alle Felder ausfüllen!");
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i< names.length; i++) {
                    for(int j = 0; j< names[i].length; j++) {
                        names[i][j].setText("");
                    }
                }
            }
        });

        names[3][2].setOnEditorActionListener((v, actionId, event) -> {
            berechnen.performClick();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            View vw = getActivity().getCurrentFocus();
            if(vw == null) {
                vw = new View(getActivity());
            }
            imm.hideSoftInputFromWindow(vw.getWindowToken(), 0);
            vw.clearFocus();
            return true;
        });

        return root;
    }
}