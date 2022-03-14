package com.nbs.vektorrechner.ui.geraden;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.MutableLiveData;

public class GeradenViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public GeradenViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Geradem fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}