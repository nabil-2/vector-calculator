package com.nbs.vektorrechner.ui.spurpunkte;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.MutableLiveData;

public class SpurpunkteViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SpurpunkteViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is spurpunkte fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}