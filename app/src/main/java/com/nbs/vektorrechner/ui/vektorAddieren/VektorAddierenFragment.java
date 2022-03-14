package com.nbs.vektorrechner.ui.vektorAddieren;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;

import com.nbs.vektorrechner.R;

public class VektorAddierenFragment extends Fragment {

    private VektorAddierenViewModel vektorAddierenViewModel;

    private View rot;

    private void update(double x, double y, double z) {
        TextView antwort = rot.findViewById(R.id.antwort);
        antwort.setText("P( "+Math.round(x*10000)/10000.0+" | "+Math.round(y*10000)/10000.0+" | "+Math.round(z*10000)/10000.0+" )");
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vektorAddierenViewModel = ViewModelProviders.of(this).get(VektorAddierenViewModel.class);
        View root = inflater.inflate(R.layout.fragment_vektorenaddieren, container, false);

        rot = root;

        TextView antwort = rot.findViewById(R.id.antwort);

        EditText e1 = rot.findViewById(R.id.eingabe_1);
        EditText e2 = rot.findViewById(R.id.eingabe_2);
        EditText e3 = rot.findViewById(R.id.eingabe_3);
        EditText e4 = rot.findViewById(R.id.eingabe_4);

        e1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        e2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        e3.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        e4.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);

        Button knopf = rot.findViewById(R.id.knopf);

        Spinner operator = rot.findViewById(R.id.spinner);

        update(0, 0, 0);

        operator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            Spinner operator = rot.findViewById(R.id.spinner);
            EditText e1 = rot.findViewById(R.id.eingabe_1);
            EditText e2 = rot.findViewById(R.id.eingabe_2);
            EditText e3 = rot.findViewById(R.id.eingabe_3);
            EditText e4 = rot.findViewById(R.id.eingabe_4);

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String zeichen = operator.getSelectedItem().toString();
                if(zeichen.equals("*") || zeichen.equals("/")) {
                    e1.setText("");
                    e2.setText("");
                    e3.setText("");

                    e1.setEnabled(false);
                    e2.setEnabled(false);
                    e3.setEnabled(false);
                } else {
                    e4.setText("");

                    e1.setEnabled(true);
                    e2.setEnabled(true);
                    e3.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        knopf.setOnClickListener(new View.OnClickListener() {
            double x = 0;
            double y = 0;
            double z = 0;

            @Override
            public void onClick(View v) {
                Spinner operator = rot.findViewById(R.id.spinner);
                EditText e1 = rot.findViewById(R.id.eingabe_1);
                EditText e2 = rot.findViewById(R.id.eingabe_2);
                EditText e3 = rot.findViewById(R.id.eingabe_3);
                EditText e4 = rot.findViewById(R.id.eingabe_4);

                String zeichen = operator.getSelectedItem().toString();
                if(zeichen.equals("+") || zeichen.equals("-")) {
                    if(!e1.getText().toString().equals("") && !e2.getText().toString().equals("") && !e3.getText().toString().equals("")) {

                        if(e4.getText().toString().equals("")){
                            e4.setText("1");
                        }

                        if(zeichen.equals("+")){
                            x += (Double.parseDouble(e1.getText().toString()))*Double.parseDouble(e4.getText().toString());
                            y += (Double.parseDouble(e2.getText().toString()))*Double.parseDouble(e4.getText().toString());
                            z += (Double.parseDouble(e3.getText().toString()))*Double.parseDouble(e4.getText().toString());
                        }
                        if(zeichen.equals("-")) {
                            x -= (Double.parseDouble(e1.getText().toString()))*Double.parseDouble(e4.getText().toString());
                            y -= (Double.parseDouble(e2.getText().toString()))*Double.parseDouble(e4.getText().toString());
                            z -= (Double.parseDouble(e3.getText().toString()))*Double.parseDouble(e4.getText().toString());
                        }

                        e1.setText("");
                        e2.setText("");
                        e3.setText("");
                        e4.setText("");

                        update(x, y, z);
                    }
                }
                if(zeichen.equals("*") || zeichen.equals("/")) {
                    if(e4.getText().toString().equals("")) {
                        e4.setText("1");
                    }

                    if(zeichen.equals("*")){
                        x *= Double.parseDouble(e4.getText().toString());
                        y *= Double.parseDouble(e4.getText().toString());
                        z *= Double.parseDouble(e4.getText().toString());
                    } else {
                        if(e4.getText().toString().equals("0")) e4.setText("1");
                        x /= Double.parseDouble(e4.getText().toString());
                        y /= Double.parseDouble(e4.getText().toString());
                        z /= Double.parseDouble(e4.getText().toString());
                    }

                    e4.setText("");
                    update(x, y, z);

                }
                if(zeichen.equals("Reset")){
                    x = 0;
                    y = 0;
                    z = 0;

                    update(x, y, z);

                }
            }

        });

        return root;
    }
}

/*interface myconst {

    double x = 0;
    double y = 0;
    double z = 0;

}*/