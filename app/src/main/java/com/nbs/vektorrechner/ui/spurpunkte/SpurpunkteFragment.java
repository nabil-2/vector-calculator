package com.nbs.vektorrechner.ui.spurpunkte;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.SubscriptSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;

import com.nbs.vektorrechner.R;

import java.util.ArrayList;

public class SpurpunkteFragment extends Fragment {

    private SpurpunkteViewModel spurpunkteViewModel;

    private View rot;

    private Spinner spinner;

    private void spurpunkte() {
        EditText e1 = rot.findViewById(R.id.v1_1);
        EditText e2 = rot.findViewById(R.id.v1_2);
        EditText e3 = rot.findViewById(R.id.v1_3);
        EditText e4 = rot.findViewById(R.id.v2_1);
        EditText e5 = rot.findViewById(R.id.v2_2);
        EditText e6 = rot.findViewById(R.id.v2_3);

        TextView yz = rot.findViewById(R.id.syz);
        TextView xz = rot.findViewById(R.id.sxz);
        TextView xy = rot.findViewById(R.id.sxy);

        ArrayList<Double> erg = new ArrayList<>();
        ArrayList<Double> erg_2 = new ArrayList<>();
        Double [] werte = {Double.parseDouble(e1.getText().toString()), Double.parseDouble(e2.getText().toString()), Double.parseDouble(e3.getText().toString()), Double.parseDouble(e4.getText().toString()), Double.parseDouble(e5.getText().toString()), Double.parseDouble(e6.getText().toString())};

        double con;
        boolean undefined = false;
        for(int t = 0; t<3; t++) {
            con = werte[t]/werte[t+3]*-1;
            erg.add(con);
        }

        for(int x = 0; x<3; x++) {
            for(int t = 0; t<3; t++) {
                con = werte[t] + erg.get(x)*werte[t+3];
                erg_2.add(con);
                System.out.println(erg_2);
            }
        }


        SpannableStringBuilder cs = new SpannableStringBuilder("Syz( "+Math.round(erg_2.get(0)*10000)/10000+" | "+Math.round(erg_2.get(1)*10000)/10000+" | "+Math.round(erg_2.get(2)*10000)/10000+" )");
        if(werte[3] != 0.0) {
            cs = new SpannableStringBuilder("Syz( "+Math.round(erg_2.get(0)*10000)/10000+" | "+Math.round(erg_2.get(1)*10000)/10000+" | "+Math.round(erg_2.get(2)*10000)/10000+" )");
            cs.setSpan(new SubscriptSpan(), 1, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            cs.setSpan(new RelativeSizeSpan(0.75f), 1, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            yz.setText(cs);
        }

        if(werte[4] != 0.0) {
            cs = new SpannableStringBuilder("Sxz( " + Math.round(erg_2.get(3) * 10000) / 10000 + " | " + Math.round(erg_2.get(4) * 10000) / 10000 + " | " + Math.round(erg_2.get(5) * 10000) / 10000 + " )");
            cs.setSpan(new SubscriptSpan(), 1, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            cs.setSpan(new RelativeSizeSpan(0.75f), 1, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            xz.setText(cs);
        }

        if(werte[5] != 0.0) {
            cs = new SpannableStringBuilder("Sxy( " + Math.round(erg_2.get(6) * 10000) / 10000 + " | " + Math.round(erg_2.get(7) * 10000) / 10000 + " | " + Math.round(erg_2.get(8) * 10000) / 10000 + " )");
            cs.setSpan(new SubscriptSpan(), 1, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            cs.setSpan(new RelativeSizeSpan(0.75f), 1, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            xy.setText(cs);
        }

    }

    private void clear() {
        EditText e1 = rot.findViewById(R.id.v1_1);
        EditText e2 = rot.findViewById(R.id.v1_2);
        EditText e3 = rot.findViewById(R.id.v1_3);
        EditText e4 = rot.findViewById(R.id.v2_1);
        EditText e5 = rot.findViewById(R.id.v2_2);
        EditText e6 = rot.findViewById(R.id.v2_3);

        e1.setText("");
        e2.setText("");
        e3.setText("");
        e4.setText("");
        e5.setText("");
        e6.setText("");
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        spurpunkteViewModel = ViewModelProviders.of(this).get(SpurpunkteViewModel.class);
        View root = inflater.inflate(R.layout.fragment_spurpunkte, container, false);
        rot = root;

        EditText e1 = rot.findViewById(R.id.v1_1);
        EditText e2 = rot.findViewById(R.id.v1_2);
        EditText e3 = rot.findViewById(R.id.v1_3);
        EditText e4 = rot.findViewById(R.id.v2_1);
        EditText e5 = rot.findViewById(R.id.v2_2);
        EditText e6 = rot.findViewById(R.id.v2_3);

        Button berechnen = rot.findViewById(R.id.btn_berechnen);
        Button reset = rot.findViewById(R.id.btn_reset);

        e1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        e2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        e3.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        e4.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        e5.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        e6.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);

        berechnen.setOnClickListener(new View.OnClickListener() {
            EditText e1 = rot.findViewById(R.id.v1_1);
            EditText e2 = rot.findViewById(R.id.v1_2);
            EditText e3 = rot.findViewById(R.id.v1_3);
            EditText e4 = rot.findViewById(R.id.v2_1);
            EditText e5 = rot.findViewById(R.id.v2_2);
            EditText e6 = rot.findViewById(R.id.v2_3);
            @Override
            public void onClick(View v) {
                if(!e1.getText().toString().equals("") && !e2.getText().toString().equals("") && !e3.getText().toString().equals("") && !e4.getText().toString().equals("") && !e5.getText().toString().equals("") && !e6.getText().toString().equals("")) {
                    spurpunkte();
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });

        e6.setOnEditorActionListener((v, actionId, event) -> {
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