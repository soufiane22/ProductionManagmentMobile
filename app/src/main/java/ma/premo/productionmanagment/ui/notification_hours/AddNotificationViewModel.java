package ma.premo.productionmanagment.ui.notification_hours;

import android.app.Application;
import android.app.ProgressDialog;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AddNotificationViewModel extends AndroidViewModel {

    private Application application;
    private RequestQueue queue;

    public AddNotificationViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        queue = Volley.newRequestQueue(application);
    }


}
