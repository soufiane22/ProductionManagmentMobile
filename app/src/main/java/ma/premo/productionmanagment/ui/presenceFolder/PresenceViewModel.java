package ma.premo.productionmanagment.ui.presenceFolder;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class PresenceViewModel extends ViewModel {

    public MutableLiveData<String> mText;

    public PresenceViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is presence fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }


    private void getPresenses(){

    }
}