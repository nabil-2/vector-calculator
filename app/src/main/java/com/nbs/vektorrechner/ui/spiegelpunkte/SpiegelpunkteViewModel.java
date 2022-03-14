package com.nbs.vektorrechner.ui.spiegelpunkte;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.MutableLiveData;

public class SpiegelpunkteViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SpiegelpunkteViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is spiegelpunkte fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}