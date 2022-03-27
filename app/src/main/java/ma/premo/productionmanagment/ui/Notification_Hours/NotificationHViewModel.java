package ma.premo.productionmanagment.ui.Notification_Hours;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import ma.premo.productionmanagment.R;

public class NotificationHViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NotificationHViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}