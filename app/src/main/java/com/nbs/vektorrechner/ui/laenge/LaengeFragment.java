package com.nbs.vektorrechner.ui.laenge;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
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

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringEscapeUtils;

public class LaengeFragment extends Fragment {

    private LaengeViewModel laengeViewModel;

    private EditText[][] ets = new EditText[2][3];

    private void berechnen(float[][] vkt, View root) {
        float l = 0;
        for(int i=0; i<vkt[0].length; i++) {
            l += Math.pow((vkt[0][i] - vkt[1][i]), 2);
        }
        l = (float) Math.pow(l, 0.5);
        TextView tv_rslt = root.findViewById(R.id.ergebnis);
        tv_rslt.setText("Die Länge ist: " + l);
        tv_rslt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
        TextView tv = root.findViewById(R.id.tv1);
        tv_rslt.setTextColor(tv.getTextColors());
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        laengeViewModel = ViewModelProviders.of(this).get(LaengeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_laenge, container, false);

        /*final WebView webView = root.findViewById(R.id.webview_laenge);
        webView.getSettings().setJavaScriptEnabled(true);
        WebViewClient client = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        };
        webView.setWebViewClient(client);
        webView.loadUrl("file:///android_asset/laenge.html");
        //webView.addJavascriptInterface(new web_interface(this), "Android");*/

        EditText[][] ets_l = {
                {root.findViewById(R.id.vk1_1), root.findViewById(R.id.vk1_2), root.findViewById(R.id.vk1_3)},
                {root.findViewById(R.id.vk2_1), root.findViewById(R.id.vk2_2), root.findViewById(R.id.vk2_3)}
        };
        ets = ets_l;
        for(int i=0; i<ets.length; i++) {
            for(int j=0; j<ets[i].length; j++) {
                ets[i][j].setInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
            }
        }

        Button btn_berechnen = root.findViewById(R.id.btn_berechnen);
        btn_berechnen.setOnClickListener(x -> {
            float[][] vkt = new float[3][3];
            boolean complete = true;
            for(int i=0; i<ets.length; i++) {
                for(int j=0; j<ets[i].length; j++) {
                    if(ets[i][j].getText().toString().matches("")) {
                        complete = false;
                        break;
                    }
                    vkt[i][j] = Float.parseFloat(ets[i][j].getText().toString());
                }
            }
            if(complete) {
                berechnen(vkt, root);
            } else {
                TextView tv_r = root.findViewById(R.id.ergebnis);
                tv_r.setText("Bitte alle Felder ausfüllen!");
                tv_r.setTextColor(Color.parseColor("#2196F3"));
                tv_r.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            }
        });

        Button btn_reset = root.findViewById(R.id.btn_zrcksetzen);
        btn_reset.setOnClickListener(x -> {
            for(int i=0; i<ets.length; i++) {
                for(int j=0; j<ets[i].length; j++) {
                    ets[i][j].getText().clear();
                }
            }
            TextView tv_r = root.findViewById(R.id.ergebnis);
            tv_r.setText("");
        });

        ets[1][2].setOnEditorActionListener((v, actionId, event) -> {
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