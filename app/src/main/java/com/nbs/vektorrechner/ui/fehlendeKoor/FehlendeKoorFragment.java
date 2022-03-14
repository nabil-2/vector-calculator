package com.nbs.vektorrechner.ui.fehlendeKoor;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;

import com.nbs.vektorrechner.R;

public class FehlendeKoorFragment extends Fragment {

    private FehlendeKoorViewModel fehlendeKoorViewModel;

    private EditText[][] ets = new EditText[4][3];

    private int lastCheckedBtn = 1;

    private View rot;

    private void vektor_ausschließen() {
        RadioGroup radioBtns = rot.findViewById(R.id.radioGrp);
        int slectedID = radioBtns.getCheckedRadioButtonId();
        int unknownVkt;
        switch (slectedID) {
            case R.id.radioBtn2:
                unknownVkt = 1;
                break;
            case R.id.radioBtn3:
                unknownVkt = 2;
                break;
            case R.id.radioBtn4:
                unknownVkt = 3;
                break;
            default: //radioBtn1
                unknownVkt = 0;
                break;
        }
        for(int i=0; i<ets.length; i++) {
            for(int j=0; j<ets[i].length; j++) {
                if(i == unknownVkt) {
                    ets[i][j].setText("x");
                    ets[i][j].setEnabled(false);
                } else if(i == lastCheckedBtn) {
                    ets[i][j].setText("");
                    ets[i][j].setEnabled(true);
                }
            }
        }
        lastCheckedBtn = unknownVkt;
    }

    private void ermitteln(String[] in) {
        TextView antwort = rot.findViewById(R.id.antwort);
        double x_koor=0;
        double y_koor=0;
        double z_koor=0;
        double z1 = Double.parseDouble(in[0]);
        double z2 = Double.parseDouble(in[1]);
        double z3 = Double.parseDouble(in[2]);
        double z4 = Double.parseDouble(in[3]);
        double z5 = Double.parseDouble(in[4]);
        double z6 = Double.parseDouble(in[5]);
        double z7 = Double.parseDouble(in[6]);
        double z8 = Double.parseDouble(in[7]);
        double z9 = Double.parseDouble(in[8]);

        RadioGroup radioBtns = rot.findViewById(R.id.radioGrp);
        int selectedID = radioBtns.getCheckedRadioButtonId();
        switch (selectedID) {
            case R.id.radioBtn2:
                x_koor = -1*(z4-z7-z1);
                y_koor = -1*(z5-z8-z2);
                z_koor = -1*(6-z9-z3);
                break;
            case R.id.radioBtn3:
                int faktor = 1;
                if(selectedID == R.id.radioBtn3) faktor = -1;
                x_koor = z7-z4+z1;
                y_koor = z8-z5+z2;
                z_koor = z9-z6+z3;
                break;
            case R.id.radioBtn4:
                x_koor = -1*(z1-z4+z7);
                y_koor = -1*(z2-z5+z8);
                z_koor = -1*(z3-z6+z9);
                break;
            default: //radioBtn1
                x_koor = z1+z4-z7;
                y_koor = z2+z5-z8;
                z_koor = z3+z6-z9;
                break;
        }
        antwort.setText("Der Vektor hat die Koordinaten P( "+x_koor+" | "+y_koor+" | "+z_koor+" )");

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fehlendeKoorViewModel = ViewModelProviders.of(this).get(FehlendeKoorViewModel.class);
        View root = inflater.inflate(R.layout.fragment_fehlendekoor, container, false);
        rot = root;

        int[][] etIDs = {
                {R.id.vk1_1, R.id.vk1_2, R.id.vk1_3},
                {R.id.vk2_1, R.id.vk2_2, R.id.vk2_3},
                {R.id.vk3_1, R.id.vk3_2, R.id.vk3_3},
                {R.id.vk4_1, R.id.vk4_2, R.id.vk4_3}
        };
        for(int i=0; i<etIDs.length; i++) {
            for(int j=0; j<etIDs[i].length; j++) {
                ets[i][j] = rot.findViewById(etIDs[i][j]);
            }
        }

        vektor_ausschließen();
        Button berechnen = rot.findViewById(R.id.btn_berechnen);
        Button clear = rot.findViewById(R.id.btn_zrcksetzen);
        for(int i=0; i<ets.length; i++) {
            for(int j=0; j<ets[i].length; j++) {
                ets[i][j].setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
            }
        }
        RadioGroup radioBtns = rot.findViewById(R.id.radioGrp);
        radioBtns.setOnCheckedChangeListener((group, checkedId) -> {
            vektor_ausschließen();
        });
        berechnen.setOnClickListener(x-> {
            int slectedID = radioBtns.getCheckedRadioButtonId();
            boolean complete = true;
            int unknownVkt;
            switch (slectedID) {
                case R.id.radioBtn2:
                    unknownVkt = 1;
                    break;
                case R.id.radioBtn3:
                    unknownVkt = 2;
                    break;
                case R.id.radioBtn4:
                    unknownVkt = 3;
                    break;
                default: //radioBtn1
                    unknownVkt = 0;
                    break;
            }
            String[] input = new String[9];
            int arrIx = 0;
            for(int i=0; i<ets.length; i++) {
                for(int j=0; j<ets[i].length; j++) {
                    if(i != unknownVkt) {
                        if(complete && ets[i][j].getText().toString().equals("")) {
                            complete = false;
                        } else if(complete){
                            input[arrIx] = ets[i][j].getText().toString();
                            arrIx++;
                        }
                    }
                }
            }
            if(complete) ermitteln(input);
        });
        clear.setOnClickListener(v -> {
            for(int i=0; i<ets.length; i++) {
                for(int j=0; j<ets[i].length; j++) {
                    if(i != lastCheckedBtn) {
                        ets[i][j].setText("");
                    }
                }
            }
        });
        ets[3][2].setOnEditorActionListener((v, actionId, event) -> {
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