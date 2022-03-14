package com.nbs.vektorrechner.ui.laenge;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.MutableLiveData;

public class LaengeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LaengeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is laenge fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}