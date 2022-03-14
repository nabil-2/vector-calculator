package com.nbs.vektorrechner;

import java.util.ArrayList;
import java.util.Arrays;

public class LEScalculator {
    private float[][] matrix;
    private float[] vector;
    private int dimension;

    public LEScalculator(float[][] matrix, float[] vector) {
        this.matrix = matrix;
        this.vector = vector;
        this.dimension = vector.length;
    }
    public void setMatrix(float[][] matrix) {
        this.matrix = matrix;
    }
    public void setVector(float[] vector) {
        this.vector = vector;
    }
    public float[] getVector() {
        return vector;
    }
    public float[][] getMatrix() {
        return matrix;
    }
    public float[] calculate() {
        return start();
    }

    private float[] ArrListToArr1D(ArrayList<Float> arrayList) {
        float[] arr = new float[arrayList.size()];
        for(int i=0; i<arrayList.size(); i++) {
            arr[i] = arrayList.get(i);
        }
        return arr;
    }

    private static float[][] ArrListToArr2D(ArrayList<ArrayList<Float>> arrayList) {
        float[][] arr = new float[arrayList.size()][arrayList.size()];
        for(int i=0; i<arrayList.size(); i++) {
            for(int j=0; j<arrayList.get(i).size(); j++) {
                arr[i][j] = arrayList.get(i).get(j);
            }
        }
        return arr;
    }

    private static float[][] rdu(float[][] m, int ix) {
        if(m.length == 2) return m;
        //float[][] smallMatrix = new float[m.length-1][m.length-1];
        ArrayList<ArrayList<Float>> smallMatrix = new ArrayList<>();
        for(int i2=1; i2<m.length; i2++) {
            smallMatrix.add(new ArrayList<Float>());
            for(int j=0; j<m[i2].length; j++) {
                if(ix != j) {
                    smallMatrix.get(i2-1).add(m[i2][j]);
                }
            }
        }
        return ArrListToArr2D(smallMatrix);
    }

    private static float calc(float[][] arr) {
        if(arr.length == 2) {
            return det(arr);
        }
        boolean operator = true;
        float dtInt = 0;
        for(int i3=0; i3<arr[0].length; i3++) {
            float eq = arr[0][i3] * calc(rdu(arr, i3));
            if(operator) { //plus
                dtInt += eq;
            } else { //minus
                dtInt -= eq;
            }
            operator = !operator;
        }
        return dtInt;
    }

    public static float det(float[][] arr) {
        if(arr[0].length == 2) {
            float quer1 = arr[0][0] * arr[1][1],
                  quer2 = arr[1][0] * arr[0][1];
            return quer1 - quer2;
        }
        return calc(arr);
    }

    private float[] start() {
        float[] determinanten = new float[dimension + 1];
        boolean operator = true;
        int dtInt = 0;
        for(int i=0; i<matrix.length; i++) {
            float eq = matrix[0][i] * det(rdu(matrix, i));
            if(operator) {
                dtInt += eq;
            } else {
                dtInt -= eq;
            }
            operator = !operator;
        }
        determinanten[0] = dtInt;
        for(int i=0; i<matrix.length; i++) {
            float[][] newMtr = new float[matrix.length][matrix[0].length];
            for(int j=0; j<matrix.length; j++) {
                for(int k=0; k<matrix[j].length; k++) {
                    newMtr[j][k] = matrix[j][k];
                }
            }
            //float[][] newMtr = Arrays.copyOf(matrix, matrix.length);
            for(int j=0; j<matrix.length; j++) {
                newMtr[j][i] = vector[j];
            }
            dtInt = 0;
            operator = true;
            for(int j2=0; j2<newMtr[0].length; j2++) {
                float eq = newMtr[0][j2] * det(rdu(newMtr, j2));
                if(operator) {
                    dtInt += eq;
                } else {
                    dtInt -= eq;
                }
                operator = !operator;
            }
            determinanten[i+1] = dtInt;
            if(matrix.length == 2) {
                determinanten[i+1] = det(newMtr);
            }
        }
        if(matrix.length == 2) {
            determinanten[0] = det(matrix);
        }
        float[] ret = new float[dimension];
        if(determinanten[0] == 0) {
            ret = new float[1];
            ret[0] = -1;
        } else {
            for(int i=0; i<matrix.length; i++) {
                ret[i] = (determinanten[i+1] / determinanten[0]);
            }
        }
        return ret;
    }
}