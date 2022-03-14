package com.nbs.vektorrechner.ui.skalar;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.MutableLiveData;

public class SkalarViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SkalarViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Skalar fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}