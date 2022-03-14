package com.nbs.vektorrechner.ui.vektorenMultiplizieren;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.MutableLiveData;

public class VektorenMultiplizierenViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public VektorenMultiplizierenViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is VektorenMultiplizieren fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}