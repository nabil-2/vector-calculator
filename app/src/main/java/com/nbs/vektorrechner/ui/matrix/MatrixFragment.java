package com.nbs.vektorrechner.ui.matrix;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintSet;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.text.style.SubscriptSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.constraint.ConstraintLayout;
import android.arch.lifecycle.ViewModelProviders;

import com.nbs.vektorrechner.R;
import com.nbs.vektorrechner.LEScalculator;
import org.w3c.dom.Text;
import java.lang.invoke.ConstantCallSite;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Math.round;

public class MatrixFragment extends Fragment {

    private MatrixViewModel matrixViewModel;

    private ArrayList<TextView> klammern = new ArrayList<>();
    private ArrayList<TextView> rechenzeichen = new ArrayList<>();
    private ArrayList<TextView> variablen = new ArrayList<>();
    private ArrayList<ArrayList<EditText>> ets = new ArrayList<>();
    private LinearLayout globalLayout;
    private View rot;

    private Spinner dimensionen;
    private EditText[] dmsion = new EditText[2];
    private ArrayList<ConstraintLayout> layouts = new ArrayList<>();
    private ArrayList<ConstraintLayout> layouts_v = new ArrayList<>();

    private int currentWidth = 3;

    private void setKlammern(TextView klammer, int height) {
        int marginTop = 0;
        int referenz = 0;
        double scaleX = 0.4;
        if(height > 4) {
            marginTop +=50;
            scaleX -= 0.1;
            referenz +=1;
        }
        if(height > 5) {
            marginTop +=50;
            referenz +=1;
            scaleX -= 0.1;
        }if(height > 5) marginTop +=50;
        klammer.setTextSize(TypedValue.COMPLEX_UNIT_SP, (height + referenz) * 50);
        klammer.setScaleX((float) scaleX);
        final float scale = getContext().getResources().getDisplayMetrics().density;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, (int) (marginTop * scale + 0.5f)*(-1), 0, 0);
        klammer.setLayoutParams(params);
    }

    private void setHeight(int height, boolean configVis) {
        heightG = height;
        TextView outTV = rot.findViewById(R.id.mtrOutput);
        outTV.setText("");
        HorizontalScrollView scroll_hz = rot.findViewById(R.id.scroll_hz);
        scroll_hz.fullScroll(ScrollView.FOCUS_LEFT);
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int scroll_height = (int) ((height * 60) * scale + 0.5f);
        LinearLayout.LayoutParams scroll_param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, scroll_height);
        scroll_hz.setLayoutParams(scroll_param);
        //System.out.println("set height to: " + height);
        for(int i=0; i<klammern.size(); i++) {
            setKlammern(klammern.get(i), height);
        }
        int widthdp = (int) (45 * scale + 0.5f);
        int heightdp = (int) (60 * scale + 0.5f);
        for(int i=0; i<layouts.size(); i++) {
            if(height > ets.get(i).size()) {
                for(int j=0; j<ets.get(i).size(); j++) {
                    ets.get(i).get(j).setVisibility(View.VISIBLE);
                }
                for(int j=ets.get(i).size(); j<height; j++) {
                    ConstraintLayout.LayoutParams p = new ConstraintLayout.LayoutParams(widthdp, heightdp);
                    EditText et = new EditText(getActivity());
                    ets.get(i).add(j, et);
                    et.setLayoutParams(p);
                    int id = View.generateViewId();
                    et.setId(id);
                    et.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                    et.setLines(1);
                    et.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                    et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                    layouts.get(i).addView(et);
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(layouts.get(i));
                    if(j==0) {
                        constraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
                    } else {
                        constraintSet.connect(id, ConstraintSet.TOP, ets.get(i).get(j-1).getId(), ConstraintSet.BOTTOM,0);
                    }
                    constraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START,0);
                    constraintSet.applyTo(layouts.get(i));
                }
            } else if(height < ets.get(i).size()) {
                for(int j=0; j<ets.get(i).size(); j++) {
                    ets.get(i).get(j).setVisibility(View.VISIBLE);
                }
                for(int j=ets.get(i).size()-1; j>=height; j--) {
                    ets.get(i).get(j).setVisibility(View.INVISIBLE);
                }
            } else { //height == ets.get(i).size()
                for(int j=0; j<ets.get(i).size(); j++) {
                    ets.get(i).get(j).setVisibility(View.VISIBLE);
                }
            }
        }

        TextView tv = new TextView(getActivity());
        Rect bounds = new Rect();
        Paint textPaint = tv.getPaint();
        textPaint.getTextBounds("+",0,"+".length(),bounds);
        int h = bounds.height();

        TextView tv2 = klammern.get(0);
        Rect bounds2 = new Rect();
        Paint textPaint2 = tv2.getPaint();
        textPaint2.getTextBounds("(",0,"(".length(),bounds2);
        int h2 = bounds2.height();


        GradientDrawable border0 = new GradientDrawable();
        border0.setColor(0xFF339999);
        border0.setStroke(1, 0xFF000000);

        klammern.get(0).measure(0, 0);
        int bracketHeight = klammern.get(0).getMeasuredHeight();
        rechenzeichen.get(1).measure(0, 0);
        int varHeight = rechenzeichen.get(1).getMeasuredHeight();
        for(int i=0; i<layouts_v.size(); i++) {
            ViewGroup.LayoutParams params = layouts_v.get(i).getLayoutParams();
            params.height = (int) (((height + 1) * 55) * scale + 0.5f);
            layouts_v.get(i).setLayoutParams(params);
            //if(i>3) layouts_v.get(i).setPadding(0, (int) Math.round((0.5f*(h2 - h) + 0.5f)), 0, 0);
            //layouts_v.get(i).setBackground(border0);
        }


        if(configVis) configVisibility(widthG, height);
    }

    private int pxToDp(int px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return round(dp);
    }

    private int widthG = 3;
    private int heightG = 3;

    private void setWidth(int width) {
        TextView outTV = rot.findViewById(R.id.mtrOutput);
        outTV.setText("");
        widthG = width;
        //System.out.println("set width to: " + width);
        final float scale = getContext().getResources().getDisplayMetrics().density;
        LinearLayout parent_layout = rot.findViewById(R.id.input_layout);
        //LinearLayout parent_layout = rot.findViewById(R.id.test2);
        //System.out.println(width > layouts.size() - 1);

        int visibleEtsIx = 0;
        for(int j=0; j<ets.get(0).size(); j++) {
            if(ets.get(0).get(j).getVisibility() == View.VISIBLE) {
                visibleEtsIx = j;
            }
        }
        int height = visibleEtsIx + 1;

        if(width > layouts.size() - 1) {
            for(int i=layouts.size() - 1; i<width; i++) {
                //System.out.println("creating: " + i);

                ConstraintLayout cl_v = new ConstraintLayout(getActivity());
                ConstraintLayout.LayoutParams params_v = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, (int) Math.round((200 * scale + 0.5f)));
                cl_v.setLayoutParams(params_v);
                //cl_v.setPadding(0, (int) Math.round(((140 + 30 * (height - 3))  * scale + 0.5f)), 0, 0);
                cl_v.setPadding(0, 0, 0, 0);
                int cl_v_id = View.generateViewId();
                cl_v.setId(cl_v_id);
                ConstraintSet cl_v_set = new ConstraintSet();
                cl_v_set.clone(cl_v);
                cl_v_set.connect(cl_v_id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP,200);
                cl_v_set.connect(cl_v_id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START,0);
                cl_v_set.applyTo(cl_v);
                layouts_v.add(cl_v);
                GradientDrawable border0 = new GradientDrawable();
                border0.setColor(0xFF339999);
                border0.setStroke(1, 0xFF000000);

                //cl_v.setBackground(border0);
                parent_layout.addView(cl_v);

                //System.out.println("cl_v height:" + layouts_v.get(0).getWidth());

                TextView add = new TextView(getActivity());
                int add_id = View.generateViewId();
                add.setId(add_id);
                add.setTextColor(Color.parseColor("#000000"));
                add.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                ConstraintSet add_set = new ConstraintSet();
                add_set.clone(cl_v);
                add_set.connect(add_id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP,0);
                add_set.connect(add_id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM,0);
                add_set.connect(add_id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START,0);
                add_set.applyTo(cl_v);
                add.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
                //ViewGroup.LayoutParams params_add = cl_v.getLayoutParams();

                //params_add.setMargins(0, (int) (800 * scale + 0.5f)*(-1), 0, 0);

                //params_add.setMarginStart((int) (50 * scale + 0.5f)*(-1));
                /*params_add.topMargin = 100;
                params_add.leftMargin = 0;
                params_add.rightMargin = 0;
                params_add.bottomMargin = 0;*/

                //add.setLayoutParams(params_add);

                //cl_v.addView(add, new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                //add.setPadding(0, 30, 0, 0);
                cl_v.addView(add);
                rechenzeichen.add(add);

                TextView var = new TextView(getActivity());
                int var_id = View.generateViewId();
                var.setId(var_id);
                ConstraintLayout.LayoutParams params_var = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) (50 * scale + 0.5f));
                params_var.setMargins((int) (30 * scale + 0.5f), (int) (5 * scale + 0.5f), 0, 0);
                var.setLayoutParams(params_var);
                var.setTextColor(Color.parseColor("#000000"));
                var.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                ConstraintSet var_set = new ConstraintSet();
                var_set.clone(cl_v);
                var_set.connect(var_id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP,0);
                var_set.connect(var_id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM,0);
                var_set.connect(var_id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START,0);
                var_set.applyTo(cl_v);
                var.setPadding((int) Math.round((30 * scale + 0.5f)), 0, 0, 0);
                variablen.add(var);
                //cl_v.addView(var, new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                cl_v.addView(var);

                TextView klammerAuf = new TextView(getActivity());
                int klammerAuf_id = View.generateViewId();
                klammerAuf.setId(klammerAuf_id);
                klammerAuf.setText("(");
                ConstraintLayout.LayoutParams params_klammerAuf = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                klammerAuf.setLayoutParams(params_klammerAuf);
                klammerAuf.setTextColor(Color.parseColor("#000000"));
                klammerAuf.setTextSize(TypedValue.COMPLEX_UNIT_SP, 150);
                klammerAuf.setTextScaleX(.4f);
                klammerAuf.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                klammern.add(klammerAuf);
                globalLayout.addView(klammerAuf);

                ConstraintLayout cl_et = new ConstraintLayout(getActivity());
                ConstraintLayout.LayoutParams params_cl_et = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                cl_et.setLayoutParams(params_cl_et);
                int cl_et_id = View.generateViewId();
                cl_et.setId(cl_et_id);
                ConstraintSet cl_et_set = new ConstraintSet();
                cl_et_set.clone(cl_et);
                cl_et_set.connect(cl_et_id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
                cl_et_set.connect(cl_et_id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
                cl_et_set.applyTo(cl_et);

                cl_et.setPadding(0, 65, 0, 0);

                GradientDrawable border = new GradientDrawable();
                border.setColor(0xFF339999);
                border.setStroke(1, 0xFF000000);

                //cl_et.setBackground(border);
                layouts.add(cl_et);
                globalLayout.addView(cl_et);


                ets.add(new ArrayList<EditText>());
                /*int widthdp = (int) (45 * scale + 0.5f);
                int heightdp = (int) (60 * scale + 0.5f);
                for(int j=0; j<=visibleEtsIx; j++) {
                    EditText et = new EditText(getActivity());
                    ConstraintLayout.LayoutParams params_et = new ConstraintLayout.LayoutParams(widthdp, heightdp);
                    int et_id = View.generateViewId();
                    et.setId(et_id);
                    et.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                    et.setLines(1);
                    et.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                    et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                    ConstraintSet et_set = new ConstraintSet();
                    et_set.clone(cl_et);
                    et_set.connect(et_id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
                    if(j==0) {
                        et_set.connect(et_id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
                        params_et.setMargins(0, 25, 0, 0); pxToDp(25, getActivity())
                    } else {
                        et_set.connect(et_id, ConstraintSet.TOP, ets.get(ets.size()-1).get(j-1).getId(), ConstraintSet.BOTTOM, 0);
                    }
                    et.setLayoutParams(params_et);
                    et_set.applyTo(cl_et);
                    ets.get(ets.size()-1).add(et);
                    cl_et.addView(et);
                }*/
                /*ConstraintLayout.LayoutParams p = new ConstraintLayout.LayoutParams(widthdp, heightdp);
                    EditText et = new EditText(getActivity());
                    ets.get(i).add(j, et);
                    et.setLayoutParams(p);
                    int id = View.generateViewId();
                    et.setId(id);
                    et.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                    et.setLines(1);
                    et.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                    et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                    layouts.get(i).addView(et);
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(layouts.get(i));
                    constraintSet.connect(id, ConstraintSet.TOP, ets.get(i).get(j-1).getId(), ConstraintSet.BOTTOM,0);
                    constraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START,0);
                    constraintSet.applyTo(layouts.get(i));
                * */

                /*TextView test = new TextView(getActivity());
                int test_id = View.generateViewId();
                test.setId(test_id);
                test.setText("test");
                ConstraintLayout.LayoutParams params_test = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                test.setLayoutParams(params_test);
                test.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                test.setTextColor(Color.parseColor("#FF0000"));
                cl_et.addView(test);*/

                TextView klammerZu = new TextView(getActivity());
                int klammerZu_id = View.generateViewId();
                klammerZu.setId(klammerZu_id);
                klammerZu.setText(")");
                ConstraintLayout.LayoutParams params_klammerZu = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                klammerZu.setLayoutParams(params_klammerZu);
                klammerZu.setTextColor(Color.parseColor("#000000"));
                klammerZu.setTextSize(TypedValue.COMPLEX_UNIT_SP, 150);
                klammerZu.setTextScaleX(0.4f);
                klammerZu.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                klammern.add(klammerZu);
                globalLayout.addView(klammerZu);

                setHeight(height, false);
            }
        }
        configVisibility(widthG, height);
    }

    private void configVisibility(int width, int height) {
        final float scale = getContext().getResources().getDisplayMetrics().density;

        for(int i=4; i<layouts_v.size(); i++) {
            layouts_v.get(i).setPadding(0, (int) Math.round(((110 + 30 * (height - 3))  * scale + 0.5f)), 0, 0);
            //layouts_v.get(i).setPadding(0, (int) Math.round((140  * scale + 0.5f)), 0, 0);
        }
        for(ConstraintLayout layout : layouts) layout.setVisibility(View.INVISIBLE);
        for(TextView zeichen : rechenzeichen) zeichen.setVisibility(View.INVISIBLE);
        for(TextView variable : variablen) variable.setVisibility(View.INVISIBLE);
        for(TextView klammer : klammern) klammer.setVisibility(View.INVISIBLE);
        for(int i=0; i<=width; i++) {
            rechenzeichen.get(i).setText(" +     ");
            if(i!=0) rechenzeichen.get(i).setVisibility(View.VISIBLE);
            if(i==width) rechenzeichen.get(i).setText("=");
            variablen.get(i).setText("x" + (i+1));
            if(i != width) {
                variablen.get(i).setVisibility(View.VISIBLE);
            } else variablen.get(i).setText("");
            layouts.get(i).setVisibility(View.VISIBLE);
            klammern.get((2*i)+1).setVisibility(View.VISIBLE);
            klammern.get((2*i)+1).setText(")");
            klammern.get(i*2).setVisibility(View.VISIBLE);
            klammern.get(i*2).setText("(");
        }
        rechenzeichen.get(0).setText("");
        setSubIndex();
        if(modusState == 1 || modusState == 2) {
            for(TextView klammer : klammern) {
                klammer.setText("");
                klammer.setVisibility(View.INVISIBLE);
            }
            klammern.get(0).setVisibility(View.VISIBLE);
            klammern.get(0).setText("(");
            for(int j=width*2 - 1; j<width*2 + 2; j++) {
                klammern.get(j).setVisibility(View.VISIBLE);
                if(j % 2 == 0) {
                    klammern.get(j).setText("(");
                } else klammern.get(j).setText(")");
            }
            for(TextView zeichen : rechenzeichen) {
                zeichen.setText("");
                zeichen.setVisibility(View.INVISIBLE);
            }
            for(TextView variable : variablen) {
                variable.setText("");
                variable.setVisibility(View.INVISIBLE);
            }
        }
        if(modusState == 2) {
            klammern.get(width * 2).setVisibility(View.INVISIBLE);
            klammern.get(width * 2 +1).setVisibility(View.INVISIBLE);
            layouts.get(width).setVisibility(View.INVISIBLE);
        }
    }

    private void setSubIndex() {
        for(int i=0; i<variablen.size(); i++) {
            String text = variablen.get(i).getText().toString();
            if(text.equals("") || text.equals("=")) continue;
            SpannableStringBuilder cs = new SpannableStringBuilder(text);
            cs.setSpan(new SubscriptSpan(), 1, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            cs.setSpan(new RelativeSizeSpan(0.75f), 1, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            variablen.get(i).setText(cs);
        }
    }

    public void toast(String input) {
        Context context = getContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, input, duration);
        toast.show();
    }

    private int modusState = 0;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        matrixViewModel = ViewModelProviders.of(this).get(MatrixViewModel.class);
        View root = inflater.inflate(R.layout.fragment_matrix, container, false);
        rot = root;

        globalLayout = rot.findViewById(R.id.input_layout);

        int[] layouts_v2 = {R.id.layout_v1, R.id.layout_v2, R.id.layout_v3, R.id.layout_v4};
        for(int i=0; i<layouts_v2.length; i++) {
            layouts_v.add(i, rot.findViewById(layouts_v2[i]));
        }

        int[] layouts2 = {R.id.layout_1, R.id.layout_2, R.id.layout_3, R.id.layout_4};
        for(int i=0; i<layouts2.length; i++) {
            layouts.add(i, rot.findViewById(layouts2[i]));
        }

        int[] klammern2 = {R.id.tv0, R.id.tv1, R.id.tv2,R.id.tv3,R.id.tv4,R.id.tv5,R.id.tv6,R.id.tv7};
        for(int i=0; i<klammern2.length; i++) {
            klammern.add(i, root.findViewById(klammern2[i]));
        }

        int[] rechenzeichen2 = {R.id.add0, R.id.add1, R.id.add2, R.id.add3};
        for(int i=0; i<rechenzeichen2.length; i++) {
            rechenzeichen.add(root.findViewById(rechenzeichen2[i]));
        }

        int[] var2 = {R.id.v1, R.id.v2, R.id.v3, R.id.v4};
        for(int i=0; i<var2.length; i++) {
            variablen.add(i, root.findViewById(var2[i]));
            /*if(i == var2.length - 1) continue;
            SpannableStringBuilder cs = new SpannableStringBuilder(variablen.get(i).getText().toString());
            cs.setSpan(new SubscriptSpan(), 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            cs.setSpan(new RelativeSizeSpan(0.75f), 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            variablen.get(i).setText(cs);*/
        }
        setSubIndex();

        int [][] ets2 = {
                {R.id.vk1_1, R.id.vk1_2, R.id.vk1_3},
                {R.id.vk2_1, R.id.vk2_2, R.id.vk2_3},
                {R.id.vk3_1, R.id.vk3_2, R.id.vk3_3},
                {R.id.vk4_1, R.id.vk4_2, R.id.vk4_3},
        };
        for(int i=0; i<ets2.length; i++) {
            ets.add(i, new ArrayList<>());
            for(int j=0; j<ets2[i].length; j++) {
                ets.get(i).add(j, root.findViewById(ets2[i][j]));
                ets.get(i).get(j).setInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
            }
        }

        dimensionen = root.findViewById(R.id.dimensionen);
        dimensionen.setSelection(1);
        dimensionen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                String selected = item.toString();
                if(selected.equals("n")) return;
                ((EditText) root.findViewById(R.id.et_breite)).setText(selected);
                ((EditText) root.findViewById(R.id.et_hoehe)).setText(selected);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        dmsion[0] = root.findViewById(R.id.et_breite);
        dmsion[1] = root.findViewById(R.id.et_hoehe);
        dmsion[0].setInputType(InputType.TYPE_CLASS_NUMBER);
        dmsion[1].setInputType(InputType.TYPE_CLASS_NUMBER);

        Button btn_berechnen = root.findViewById(R.id.btn_berechnen);
        Button btn_zuruecksetzen = root.findViewById(R.id.btn_zuruecksetzen);

        btn_zuruecksetzen.setOnClickListener(x -> {
            for(ArrayList<EditText> ArrList : ets) {
                for(EditText et : ArrList) {
                    et.setText("");
                }
            }
        });
        ArrayList<ArrayList<Float>> matrix = new ArrayList<>();
        ArrayList<Float> vector = new ArrayList<>();
        btn_berechnen.setOnClickListener(x-> {
            matrix.clear();
            vector.clear();
            int widthDmsion = Integer.parseInt(dmsion[0].getText().toString());
            int heightDmsion = Integer.parseInt(dmsion[1].getText().toString());
            if(widthDmsion <= 1) {
                toast("Breite muss größer als 1 sein!");
            } else if(heightDmsion <= 1) {
                toast("Höhe muss größer als 1 sein!");
            } else if(widthDmsion != heightDmsion) {
                if(modusState != 2) {
                    toast("Breite (Anzahl Vaiablen) muss Höhe (Anzahl Gleichungen) entsprechen!");
                } else toast("Breite muss Höhe entsprechen");
            } else {
                int LESdimensions = 0;
                for(int i=0; i<layouts.size(); i++) {
                    boolean visible = (layouts.get(i).getVisibility() == View.VISIBLE);
                    if(layouts.get(i).getVisibility() == View.VISIBLE) {
                        LESdimensions = i;
                    }
                }
                if(modusState == 2) LESdimensions++;
                boolean givenErr = false;
                outerloop1:
                for(int i=0; i<LESdimensions; i++) {
                    ArrayList<Float> column = new ArrayList<>();
                    for(int j=0; j<LESdimensions; j++) {
                        String input = ets.get(i).get(j).getText().toString();
                        if(input.equals("")) {
                            givenErr = true;
                            break outerloop1;
                        }
                        column.add(Float.parseFloat(input));
                    }
                    matrix.add(column);
                }
                if(givenErr) {
                    toast("Bitte alle Felder ausfüllen!");
                } else {
                    if(modusState != 2) {
                        for(int i=0; i<LESdimensions; i++) {
                            String input = ets.get(LESdimensions).get(i).getText().toString();
                            if(input.equals("")) {
                                givenErr = true;
                                break;
                            }
                            vector.add(Float.parseFloat(input));
                        }
                    }
                    if(givenErr) {
                        toast("Bitte alle Felder ausfüllen!");
                    } else {
                        float[][] matrixArr = new float[matrix.size()][matrix.get(0).size()];
                        float[] vectorArr = new float[vector.size()];
                        for(int i=0; i<matrix.size(); i++) {
                            for(int j=0; j<matrix.get(i).size(); j++) {
                                matrixArr[j][i] = matrix.get(i).get(j); //Transpose Matrix
                            }
                        }
                        if(modusState != 2) {
                            for(int i=0; i<vector.size(); i++) {
                                vectorArr[i] = vector.get(i);
                            }
                            LEScalculator LEScalc = new LEScalculator(matrixArr, vectorArr);
                            float[] ret = LEScalc.calculate();
                            TextView output = rot.findViewById(R.id.mtrOutput);
                            if(ret.length == 1) { // determinante  = 0
                                output.setText("Gleichungssystem nicht lösbar");
                            } else {
                                output.setText("");
                                for(int i=0; i<ret.length; i++) {
                                    String text = "x" + String.valueOf(i+1);
                                    if(text.equals("") || text.equals("=")) continue;
                                    SpannableStringBuilder cs = new SpannableStringBuilder(text);
                                    cs.setSpan(new SubscriptSpan(), 1, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    cs.setSpan(new RelativeSizeSpan(0.75f), 1, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    output.append(cs);

                                    output.append(" = " + ret[i] + "\n");
                                }
                            }
                        } else {
                            float det = LEScalculator.det(matrixArr);
                            TextView output = rot.findViewById(R.id.mtrOutput);
                            output.setText("Determinante d = " + det);
                        }
                    }
                }
            }
        });

        dmsion[0].addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                HorizontalScrollView scroll_hz = rot.findViewById(R.id.scroll_hz);
                scroll_hz.fullScroll(ScrollView.FOCUS_LEFT);
                dmsion[0].requestFocus();
                String input = dmsion[0].getText().toString();
                if(!input.equals("") && !input.equals("1") && !input.equals("0")) setWidth(Integer.parseInt(input));
            }
        });
        dmsion[1].addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                ScrollView scroll_ver = rot.findViewById(R.id.scroll_ver);
                scroll_ver.fullScroll(ScrollView.FOCUS_UP);
                dmsion[1].requestFocus();
                String input = dmsion[1].getText().toString();
                if(!input.equals("") && !input.equals("1") && !input.equals("0")) setHeight(Integer.parseInt(input), true);
            }
        });

        Spinner modus = (Spinner) root.findViewById(R.id.modus);
        modus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == modusState) return;
                modusState = i;
                configVisibility(widthG, heightG);
                modusState = i;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        modus.setSelection(1);

        return root;
    }
}