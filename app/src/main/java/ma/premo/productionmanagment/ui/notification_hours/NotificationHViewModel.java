package ma.premo.productionmanagment.ui.notification_hours;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

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