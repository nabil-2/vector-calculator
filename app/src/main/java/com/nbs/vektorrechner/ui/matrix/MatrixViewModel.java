package com.nbs.vektorrechner.ui.matrix;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.MutableLiveData;

public class MatrixViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MatrixViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is matrix fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}