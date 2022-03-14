package com.nbs.vektorrechner.ui.fehlendeKoor;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.MutableLiveData;

public class FehlendeKoorViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FehlendeKoorViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is fehlendeKoor fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}