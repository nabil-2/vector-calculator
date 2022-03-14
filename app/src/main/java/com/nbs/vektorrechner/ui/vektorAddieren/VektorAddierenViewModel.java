package com.nbs.vektorrechner.ui.vektorAddieren;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.MutableLiveData;;

public class VektorAddierenViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public VektorAddierenViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is VektorAddieren fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}