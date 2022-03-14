package com.nbs.vektorrechner.ui.ebenenUmwandler;


import android.app.Activity;
import android.graphics.Color;
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

abstract class Ebenen {
    protected float x=0, y=0, z=0, d=0;

    private float[] getPoint(int rand) {
        float[] point = new float[3];
        point[0] = 0;
        if(this.x == 0 && this.y == 0) {
            point[1] = (float) rand;
            point[2] = (-1) * this.d / this.z;
        }
        if(this.x == 0 && this.y == 0 || this.x == 0 && this.z == 0 || this.x == 0 && this.y == 0 ||this.y == 0 && this.z == 0) {

        } else {
            if(this.z != 0) {
                //pV[2] = (-1) * this.d / this.z;
            } else if(this.y != 0) {
                //pV[1] = (-1) * this.d / this.y;
            } else if(this.x != 0) {
                //pV[1] = (-1) * this.d / this.x;
            }
        }
        return new float[]{0, 0, 0};
    }

    public Parameterform toParameterform() {
        //K --> P
        float[] nV = {this.x, this.y, this.z}, pV = {0, 0, 0};
        if(this.z != 0) {
            pV[2] = (-1) * this.d / this.z;
        } else if(this.y != 0) {
            pV[1] = (-1) * this.d / this.y;
        } else if(this.x != 0) {
            pV[1] = (-1) * this.d / this.x;
        }

        float[] r1 = {0, (-1)*nV[2], nV[1]};
        float[] r2 = {nV[2], 0, (-1)*nV[0]};

        return new Parameterform(pV, r1, r2);
    }
    public Normalform toNormalform() {
        //K --> N
        float[] nV = {this.x, this.y, this.z}, pV = {0, 0, 0};
        if(this.z != 0) {
            pV[2] = (-1) * this.d / this.z;
        } else if(this.y != 0) {
            pV[1] = (-1) * this.d / this.y;
        } else if(this.x != 0) {
            pV[1] = (-1) * this.d / this.x;
        }
        return new Normalform(pV, nV);
    }
    public HessescheNormalform toHessescheNormalform() {
        //K --> HN
        float[] nV = {this.x, this.y, this.z}, pV = {0, 0, 0};
        if(this.z != 0) {
            pV[2] = (-1) * this.d / this.z;
        } else if(this.y != 0) {
            pV[1] = (-1) * this.d / this.y;
        } else if(this.x != 0) {
            pV[1] = (-1) * this.d / this.x;
        }
        float k = 1;
        float sum = 0;
        for(int i=0; i<nV.length; i++) {
            sum += Math.pow(nV[i], 2);
        }
        k /= Math.sqrt(sum);
        return new HessescheNormalform(pV, nV, k);
    }
    public Koordinatenform toKoordinatenform() {
        //K --> K
        return new Koordinatenform(x, y, z, d);
    }
}
class Parameterform extends Ebenen {
    public float[] sVector;
    public float[] r1Vector, r2Vector;

    private void internToKoordinatenform() {
        //P --> K
        float[] nV = {
                r1Vector[1]*r2Vector[2] - r1Vector[2]*r2Vector[1],
                r1Vector[2]*r2Vector[0] - r1Vector[0]*r2Vector[2],
                r1Vector[0]*r2Vector[1] - r1Vector[1]*r2Vector[0]
        };
        this.x = nV[0];
        this.y = nV[1];
        this.z = nV[2];
        this.d = (-1)*(nV[0]*this.sVector[0] + nV[1]*this.sVector[1] + nV[2]*this.sVector[2]);
    }

    public Parameterform(float[] sV, float[] r1, float[] r2) {
        if(sV.length != 3 || r1.length != 3 || r2.length != 3) return;
        this.sVector = sV;
        this.r1Vector = r1;
        this.r2Vector = r2;
        internToKoordinatenform();
    }
}
class Normalform extends Ebenen {
    public float[] pVector;
    public float[] nVector;

    private void internToKoordinatenform() {
        //N --> K
        this.x = this.nVector[0];
        this.y = this.nVector[1];
        this.z = this.nVector[2];
        this.d = (-1)*(this.nVector[0]*this.pVector[0] + this.nVector[1]*this.pVector[1] + this.nVector[2]*this.pVector[2]);
    }

    public Normalform(float[] pV, float[] n) {
        if(pV.length != 3 || n.length != 3) return;
        this.pVector = pV;
        this.nVector = n;
        internToKoordinatenform();
    }
}
class HessescheNormalform extends Ebenen {
    public float[] pVector;
    public float[] nVector;
    public float n0;

    private void internToKoordinatenform() {
        //HN --> K
        this.x = this.nVector[0];
        this.y = this.nVector[1];
        this.z = this.nVector[2];
        this.d = (-1)*(this.nVector[0]*this.pVector[0] + this.nVector[1]*this.pVector[1] + this.nVector[2]*this.pVector[2]);
    }

    public HessescheNormalform(float[] pV, float[] n, float k) {
        if(pV.length != 3 || n.length != 3) return;
        this.pVector = pV;
        this.nVector = n;
        this.n0 = k;
        internToKoordinatenform();
    }
}
class Koordinatenform extends Ebenen {
    public Koordinatenform(float x, float y, float z, float d) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.d = d;
    }
}

public class ebenenUmwandlerFragment extends Fragment {

    private View rot;
    //private ArrayList<ArrayList<ArrayList<EditText>>> ets = new ArrayList<>();
    private EditText[][][] ets;

    private String[] listToArray(ArrayList<String> list) {
        String[] arr = new String[list.size()];
        for(int i=0; i<arr.length; i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    /*public Parameterform toParameterform(Normalform ebene) {
        return null;
    }
    public Parameterform toParameterform(HessescheNormalform ebene) {
        return null;
    }
    public Parameterform toParameterform(Koordinatenform ebene) {
        return null;
    }
    public<c> Parameterform toParameterform(Object ebene, Class c) {
        if(c == Normalform.class) return toParameterform((Normalform) ebene);
        if(c == HessescheNormalform.class) return toParameterform((HessescheNormalform) ebene);
        if(c == Koordinatenform.class) return toParameterform((Koordinatenform) ebene);
        return null;
    }

    public Normalform toNormalform(Parameterform ebene) {
        return null;
    }
    public Normalform toNormalform(HessescheNormalform ebene) {
        return null;
    }
    public Normalform toNormalform(Koordinatenform ebene) {
        return null;
    }
    public<c> Normalform toNormalform(Object ebene, Class c) {
        if(c == Parameterform.class) return toNormalform((Parameterform) ebene);
        if(c == HessescheNormalform.class) return toNormalform((HessescheNormalform) ebene);
        if(c == Koordinatenform.class) return toNormalform((Koordinatenform) ebene);
        return null;
    }

    public HessescheNormalform toHessescheNormalform(Parameterform ebene) {
        return null;
    }
    public HessescheNormalform toHessescheNormalform(Normalform ebene) {
        return null;
    }
    public HessescheNormalform toHessescheNormalform(Koordinatenform ebene) {
        return null;
    }
    public<c> HessescheNormalform toHessescheNormalform(Object ebene, Class c) {
        if(c == Parameterform.class) return toHessescheNormalform((Parameterform) ebene);
        if(c == Normalform.class) return toHessescheNormalform((Normalform) ebene);
        if(c == Koordinatenform.class) return toHessescheNormalform((Koordinatenform) ebene);
        return null;
    }

    public Koordinatenform toKoordinatenform(Parameterform ebene) {
        return null;
    }
    public Koordinatenform toKoordinatenform(Normalform ebene) {
        return null;
    }
    public Koordinatenform toKoordinatenform(HessescheNormalform ebene) {
        return null;
    }
    public<c> Koordinatenform toKoordinatenform(Object ebene, Class c) {
        if(c == Parameterform.class) return toKoordinatenform((Parameterform) ebene);
        if(c == Normalform.class) return toKoordinatenform((Normalform) ebene);
        if(c == HessescheNormalform.class) return toKoordinatenform((HessescheNormalform) ebene);
        return null;
    }*/

    private void showResult() {

    }

    private Class getC(String cName) {
        Class eC = null;
        try {
            eC = Class.forName(cName);
        } catch(Throwable e) {
            e.printStackTrace();
        }
        return eC;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_ebenen_umwandler, container, false);
        rot = root;

        final String[] formen = {
                "Parameterform",
                "Normalform",
                "Hessesche Normalform",
                "Koordinatenform"
        };

        Spinner spinnerFrom = (Spinner) rot.findViewById(R.id.eingabeform);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.ebenen_formen, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(adapter);



        /*
        Spinner spinnerTo = (Spinner) rot.findViewById(R.id.ausgabeform);



        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = formen[i];
                ArrayList<String> formenReduced = new ArrayList<>();
                for(int j=0; j<formen.length; j++) {
                    if(formen[j] != selected) {
                        formenReduced.add(formen[j]);
                    }
                }
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listToArray(formenReduced));
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerTo.setAdapter(adapter2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });*/

        Button berechnen = rot.findViewById(R.id.btn_berechnen);
        Button reset = rot.findViewById(R.id.btn_reset);

        int[][] entrysP = {
                {R.id.pv1_1, R.id.pv1_2, R.id.pv1_3},
                {R.id.pv2_1, R.id.pv2_2, R.id.pv2_3},
                {R.id.pv3_1, R.id.pv3_2, R.id.pv3_3}
        };
        int[][] entrysN = {
                {R.id.nv1_1, R.id.nv1_2, R.id.nv1_3},
                {R.id.nv2_1, R.id.nv2_2, R.id.nv2_3},
        };
        int[][] entrysH = {
                {R.id.hv1_1, R.id.hv1_2, R.id.hv1_3},
                {R.id.hv2_1, R.id.hv2_2, R.id.hv2_3},
                {R.id.hv3}
        };
        int [][] entrysK = {
                {R.id.kv1_1},
                {R.id.kv1_2},
                {R.id.kv1_3},
                {R.id.kv1_4},
        };
        int[][][] entrys = {entrysP, entrysN, entrysH, entrysK};
        EditText[][][] tmp = new EditText[entrys.length][][];
        for(int i=0; i<entrys.length; i++) {
            tmp[i] = new EditText[entrys[i].length][];
            for(int j=0; j<entrys[i].length; j++) {
                tmp[i][j] = new EditText[entrys[i][j].length];
                for(int k=0; k<entrys[i][j].length; k++) {
                    tmp[i][j][k] = rot.findViewById(entrys[i][j][k]);
                    (tmp[i][j][k]).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                }
            }
        }
        ets = tmp;

        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("test");
                for(int i=0; i<entrys.length; i++) {
                    if(i != position) {
                        for(int j=0; j<entrys[i].length; j++) {
                            for(int k=0; k<entrys[i][j].length; k++) {
                                EditText tmp = rot.findViewById(entrys[i][j][k]);
                                tmp.setHint("");
                            }
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        berechnen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = spinnerFrom.getSelectedItemPosition();
                //entrys[i] contains selected type variables
                boolean isFilled = true;
                for(int j=0; j<entrys[i].length; j++) {
                    for(int k=0; k<entrys[i][j].length; k++) {
                        EditText tmp = rot.findViewById(entrys[i][j][k]);
                        if(tmp.getText().toString().equals("")) {
                            isFilled = false;
                            tmp.setHint("?");
                        }
                    }
                }

                if(!isFilled) {
                    Toast toast = Toast.makeText(getContext(), "Bitte alle Felder ausfüllen!", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    float[][] input = new float[ets[i].length][];
                    for(int j=0; j<ets[i].length; j++) {
                        input[j] = new float[ets[i][j].length];
                        for(int k=0; k<ets[i][j].length; k++) {
                            input[j][k] = Float.parseFloat(ets[i][j][k].getText().toString());
                        }
                    }
                    Ebenen vEbene = null;
                    switch(i) {
                        case 0:
                            vEbene = new Parameterform(input[0], input[1], input[2]);
                            break;
                        case 1:
                            vEbene = new Normalform(input[0], input[1]);
                            break;
                        case 2:
                            vEbene = new HessescheNormalform(input[0], input[1], input[2][0]);
                            break;
                        case 3:
                            vEbene = new Koordinatenform(input[0][0], input[1][0], input[2][0], input[3][0]);
                            break;
                        default:
                            break;
                    }
                    for(int h=0; h<entrys.length; h++) {
                        if(h != i) {
                            switch (h) {
                                case 0:
                                    Parameterform p =  vEbene.toParameterform();
                                    float[][] retValP = {p.sVector, p.r1Vector, p.r2Vector};
                                    for(int j=0; j<entrys[h].length; j++) {
                                        for(int k=0; k<entrys[h][j].length; k++) {
                                            EditText tmp = rot.findViewById(entrys[h][j][k]);
                                            tmp.setText(String.valueOf(retValP[j][k]));
                                        }
                                    }
                                    break;
                                case 1:
                                    Normalform n =  vEbene.toNormalform();
                                    float[][] retValN = {n.pVector, n.nVector};
                                    for(int j=0; j<entrys[h].length; j++) {
                                        for(int k=0; k<entrys[h][j].length; k++) {
                                            EditText tmp = rot.findViewById(entrys[h][j][k]);
                                            tmp.setText(String.valueOf(retValN[j][k]));
                                        }
                                    }
                                    break;
                                case 2:
                                    HessescheNormalform hn =  vEbene.toHessescheNormalform();
                                    float[][] retValH = {hn.pVector, hn.nVector, {hn.n0}};
                                    for(int j=0; j<entrys[h].length; j++) {
                                        for(int k=0; k<entrys[h][j].length; k++) {
                                            EditText tmp = rot.findViewById(entrys[h][j][k]);
                                            tmp.setText(String.valueOf(retValH[j][k]));
                                        }
                                    }
                                    break;
                                case 3:
                                    Koordinatenform kf =  vEbene.toKoordinatenform();
                                    float[][] retValK = {{kf.x}, {kf.y}, {kf.z}, {kf.d}};
                                    for(int j=0; j<entrys[h].length; j++) {
                                        for(int k=0; k<entrys[h][j].length; k++) {
                                            EditText tmp = rot.findViewById(entrys[h][j][k]);
                                            tmp.setText(String.valueOf(retValK[j][k]));
                                        }
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0; i<ets.length; i++) {
                    for(int j=0; j<ets[i].length; j++) {
                        for(int k=0; k<ets[i][j].length; k++) {
                            ets[i][j][k].setText("");
                        }
                    }
                }
            }
        });
        return root;
    }
}