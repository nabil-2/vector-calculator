package com.nbs.vektorrechner.ui.spiegelpunkte;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
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

import org.apache.commons.lang3.ArrayUtils;

import java.math.BigDecimal;

import static android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL;

public class SpiegelpunkteFragment extends Fragment {

    private SpiegelpunkteViewModel spiegelpunkteViewModel;

    private EditText et_p1;

    private View global_root;

    private void setVektorString(boolean pkt) {
        TextView tv = global_root.findViewById(R.id.vektor_b);
        tv.setText("M(");
        if(pkt) {
            //change to P'
            tv.setText("P'(");
        }

    }

    private void berechnen(float[] vkt_1,float[] vkt_2) {
        TextView tv = global_root.findViewById(R.id.vektor_b);
        float[] vkt_rslt = new float[vkt_1.length];
        String point = "";
        if(tv.getText().toString().charAt(0) == 'M') { //Spiegelpunkt berechnen
            point = "P'";
            for(int i=0; i<vkt_rslt.length; i++) {
                vkt_rslt[i] = 2*vkt_1[i] - vkt_2[i];
                BigDecimal bd = new BigDecimal(Float.toString(vkt_rslt[i]));
                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                vkt_rslt[i] = bd.floatValue();
            }
        } else { //char = P' || Mittelpunkt berechen
            point = "M";
            for(int i=0; i<vkt_rslt.length; i++) {
                vkt_rslt[i] = (vkt_1[i] + vkt_2[i])/2;
                BigDecimal bd = new BigDecimal(Float.toString(vkt_rslt[i]));
                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                vkt_rslt[i] = bd.floatValue();
            }
        }
        TextView tv_rslt = global_root.findViewById(R.id.ergebnis);
        tv_rslt.setText(point + "(" + vkt_rslt[0] + "/" + vkt_rslt[1] + "/" + vkt_rslt[2] + ")");
        tv_rslt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
        tv_rslt.setTextColor(tv.getTextColors());
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        spiegelpunkteViewModel = ViewModelProviders.of(this).get(SpiegelpunkteViewModel.class);
        View root = inflater.inflate(R.layout.fragment_spiegelpunkte, container, false);
        global_root = root;

        EditText[] et_p = {root.findViewById(R.id.spkt_p1), root.findViewById(R.id.spkt_p2), root.findViewById(R.id.spkt_p3)};
        for(int i=0; i< et_p.length; i++) {
            et_p[i].setInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        }
        EditText[] et_m = {root.findViewById(R.id.spkt_m1), root.findViewById(R.id.spkt_m2), root.findViewById(R.id.spkt_m3)};
        for(int i=0; i< et_m.length; i++) {
            et_m[i].setInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        }

        Spinner pkt_auswahl = root.findViewById(R.id.spkt_gesucht);

        pkt_auswahl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                String selected = item.toString();
                TextView tv_r = root.findViewById(R.id.ergebnis);
                tv_r.setText("");
                switch (selected.substring(selected.length() - 1)) {
                    case "'":
                        setVektorString(false);
                        //System.out.println(selected.substring(selected.length() - 1));
                        break;
                    case "M":
                        setVektorString(true);
                        //System.out.println(selected.substring(selected.length() - 1));
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        Button btn_berechnen = root.findViewById(R.id.btn_berechnen);
        btn_berechnen.setOnClickListener(x -> {
            float[] vkt_a = new float[3];
            float[] vkt_b =  new float[3];
            boolean complete = true;
            for(int i=0; i<et_p.length; i++) {
                if(et_p[i].getText().toString().matches("")) {
                    complete = false;
                    break;
                }
                vkt_a[i] = Float.parseFloat(et_p[i].getText().toString());
            }
            for(int i=0; i<et_m.length; i++) {
                if(et_m[i].getText().toString().matches("")) {
                    complete = false;
                    break;
                }
                vkt_b[i] = Float.parseFloat(et_m[i].getText().toString());
            }
            if(complete) {
                berechnen(vkt_a, vkt_b);
            } else {
                TextView tv_r = root.findViewById(R.id.ergebnis);
                tv_r.setText("Bitte alle Felder ausfüllen!");
                tv_r.setTextColor(Color.parseColor("#2196F3"));
                tv_r.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            }
        });

        Button btn_reset = root.findViewById(R.id.btn_zrcksetzen);
        btn_reset.setOnClickListener(x -> {
            EditText[] ET_all = ArrayUtils.addAll(et_p, et_m);
            for(int i=0; i<ET_all.length; i++) {
                ET_all[i].getText().clear();
            }
            TextView tv_r = root.findViewById(R.id.ergebnis);
            tv_r.setText("");
        });

        et_m[2].addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                btn_berechnen.setEnabled(true);
            }
        });

        et_m[2].setOnEditorActionListener((v, actionId, event) -> {
            btn_berechnen.performClick();
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