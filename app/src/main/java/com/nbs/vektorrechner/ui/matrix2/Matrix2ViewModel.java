package com.nbs.vektorrechner.ui.matrix2;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.MutableLiveData;

public class Matrix2ViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public Matrix2ViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is matrix fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}