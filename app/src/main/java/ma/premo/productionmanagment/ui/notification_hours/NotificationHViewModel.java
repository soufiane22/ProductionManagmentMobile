package ma.premo.productionmanagment.ui.notification_hours;

import android.app.Application;
import android.app.ProgressDialog;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.premo.productionmanagment.Utils.API;
import ma.premo.productionmanagment.Utils.JsonConvert;
import ma.premo.productionmanagment.models.Line;
import ma.premo.productionmanagment.models.Notification_Hours;

public class NotificationHViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<Notification_Hours> notifLiveData = new MutableLiveData<>();
    private Application application;
    private RequestQueue queue;
    private ProgressDialog pDialog;

    public NotificationHViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        queue = Volley.newRequestQueue(application);
        pDialog = new ProgressDialog(getApplication());
        pDialog.setMessage("Loading...PLease wait");
    }
 /*
    public void getNotificationByStatus(String token) {
        List<Line> listLines = new ArrayList<>();
        String url = API.urlBackend + "/notification_heures/get/state/No%Validate";
        List<Notification_Hours> notificationsList =
        JsonArrayRequest   jsonObjectRequest  = new JsonArrayRequest(com.android.volley.Request.Method.GET, url,
                null, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray data = response;
                notificationsList = new ArrayList<>();
                if(data.length()==0) {
                    if(getActivity().getApplicationContext() != null){
                        Toast.makeText(getActivity().getApplicationContext(), "No data available", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    for(int i=0 ; i< data.length() ; i++){

                        JSONObject objet = null;
                        try {
                            objet = data.getJSONObject(i);
                            Gson g = new Gson();
                            Notification_Hours notif = new Notification_Hours();

                            notif = g.fromJson(String.valueOf(objet), Notification_Hours.class);

                            notificationsList.add(notif);

                            pDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            pDialog.dismiss();
                        }

                    }
                }

                setRecycleview();

                pDialog.dismiss();


            }
        }, new com.android.volley.Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("*******error connection******");
                error.printStackTrace();
                Log.e("NetworkError", "Response " + error.networkResponse);
                pDialog.dismiss();
                //TODO
                if(getContext() != null )
                    Toast.makeText(getContext(),"server error",Toast.LENGTH_SHORT).show();


            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", access_token);
                //params.put("Accept-Language", "fr");
                return params;
            };


        };
        queue.add(jsonObjectRequest);
    }


  */
    public LiveData<String> getText() {
        return mText;
    }
}