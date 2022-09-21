package ma.premo.productionmanagment.ui.statistics;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
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
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.premo.productionmanagment.Utils.API;
import ma.premo.productionmanagment.models.Notification_Hours;
import ma.premo.productionmanagment.models.PresenceGroup;
import ma.premo.productionmanagment.models.Statistic;

public class StatisticViewModel extends AndroidViewModel {

    private Application application;
    private RequestQueue queue;
    public MutableLiveData<List<Notification_Hours>> NotificationsMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<Statistic>> StatisticsMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<Notification_Hours>> NotificationsBetweenDatesLiveData = new MutableLiveData<>();
    public  MutableLiveData<Boolean> tokenExpired = new MutableLiveData<>();

    public StatisticViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        queue = Volley.newRequestQueue(application);
    }

    public void getListNotifications(String idLeader , String token) {
        String url = API.urlBackend + "notification_heures/list/"+idLeader;
        List<Notification_Hours> listNotifications = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArray = new JSONArray();
                        try {
                            jsonArray = response.getJSONArray("data1");
                            if (jsonArray.length() == 0 || jsonArray == null){
                                NotificationsMutableLiveData.setValue(null);
                            }else{
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject objet = null;
                                    Gson g = new Gson();
                                    objet = jsonArray.getJSONObject(i);
                                    Notification_Hours notificationHours = new Notification_Hours();
                                    notificationHours = g.fromJson(String.valueOf(objet), Notification_Hours.class);
                                    // presenceGroup = JsonConvert.getGsonParser().fromJson(String.valueOf(jsonArray.getJSONObject(i)), PresenceGroup.class);
                                    listNotifications.add(notificationHours);

                                }
                                NotificationsMutableLiveData.setValue(listNotifications);
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(application.getApplicationContext() ,"cast error",Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {
                       Log.e("NetworkErro", "Response " + error.networkResponse);
                       if ( error instanceof AuthFailureError) {
                           Toast.makeText(application, "token expired",Toast.LENGTH_LONG).show();
                           tokenExpired.setValue(true);
                       }else{
                           Toast.makeText(application.getApplicationContext() ,"Server error",Toast.LENGTH_SHORT).show();
                       }

            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", token);
                //params.put("Accept-Language", "fr");
                return params;
            };

        };
        queue.add(jsonObjectRequest);
    }

    public void getNotificationsBetweenDates(String token,String startDate , String endDate , String idLeader) {
        String url = API.urlBackend + "notification_heures/getbetween/leader/"+startDate+"/"+endDate+"/"+idLeader;
        List<Notification_Hours> listNotifications = new ArrayList<>();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(com.android.volley.Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONArray jsonArray = new JSONArray();
                        try {
                            jsonArray = response;
                            System.out.println("response  "+response.toString());
                            if (jsonArray.length() == 0 || jsonArray == null){
                                NotificationsMutableLiveData.setValue(null);
                            }else{
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject objet = null;
                                    Gson g = new Gson();
                                    objet = jsonArray.getJSONObject(i);
                                    Notification_Hours notificationHours = new Notification_Hours();
                                    notificationHours = g.fromJson(String.valueOf(objet), Notification_Hours.class);
                                    // presenceGroup = JsonConvert.getGsonParser().fromJson(String.valueOf(jsonArray.getJSONObject(i)), PresenceGroup.class);
                                    listNotifications.add(notificationHours);

                                }
                                NotificationsMutableLiveData.setValue(listNotifications);
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(application.getApplicationContext() ,"cast error",Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("NetworkErro", "Response " + error.networkResponse);
                Toast.makeText(application.getApplicationContext() ,"Server error",Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", token);
                //params.put("Accept-Language", "fr");
                return params;
            };

        };
        queue.add(jsonObjectRequest);
    }

    public void getStatistics(String token,String startDate , String endDate , String idLine) {
        String url = API.urlBackend + "notification_heures/getstatistic/"+startDate+"/"+endDate+"/"+idLine;
        List<Statistic> listStatistic = new ArrayList<>();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(com.android.volley.Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONArray jsonArray = new JSONArray();
                        try {
                            jsonArray = response;
                            if (jsonArray.length() == 0 || jsonArray == null){
                                NotificationsMutableLiveData.setValue(null);
                            }else{
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject objet = null;
                                    Gson g = new Gson();
                                    objet = jsonArray.getJSONObject(i);
                                    Statistic statistic = new Statistic();
                                    statistic = g.fromJson(String.valueOf(objet), Statistic.class);
                                    // presenceGroup = JsonConvert.getGsonParser().fromJson(String.valueOf(jsonArray.getJSONObject(i)), PresenceGroup.class);
                                    listStatistic.add(statistic);

                                }

                                StatisticsMutableLiveData.setValue(listStatistic);
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(application.getApplicationContext() ,"cast error",Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("NetworkErro", "Response " + error.networkResponse);
                if ( error instanceof AuthFailureError) {
                    Toast.makeText(application, "token expired", Toast.LENGTH_LONG).show();
                    tokenExpired.setValue(true);
                }else{
                    Toast.makeText(application.getApplicationContext() ,"Server error",Toast.LENGTH_SHORT).show();
                }

            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", token);
                //params.put("Accept-Language", "fr");
                return params;
            };

        };
        queue.add(jsonObjectRequest);
    }
    public void getNotificationsByOF(String token,String idLeader,int of) {
        String url = API.urlBackend + "notification_heures/getbyof/leader/"+idLeader+"/"+of;
        List<Notification_Hours> listNotifications = new ArrayList<>();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(com.android.volley.Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONArray jsonArray = new JSONArray();
                        try {
                            jsonArray = response;
                            System.out.println("response  "+response.toString());
                            if (jsonArray.length() == 0 || jsonArray == null){
                                NotificationsMutableLiveData.setValue(null);
                            }else{
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject objet = null;
                                    Gson g = new Gson();
                                    objet = jsonArray.getJSONObject(i);
                                    Notification_Hours notificationHours = new Notification_Hours();
                                    notificationHours = g.fromJson(String.valueOf(objet), Notification_Hours.class);
                                    // presenceGroup = JsonConvert.getGsonParser().fromJson(String.valueOf(jsonArray.getJSONObject(i)), PresenceGroup.class);
                                    listNotifications.add(notificationHours);

                                }
                                NotificationsMutableLiveData.setValue(listNotifications);
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(application.getApplicationContext() ,"cast error",Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("NetworkErro", "Response " + error.networkResponse);
                Toast.makeText(application.getApplicationContext() ,"Server error",Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", token);
                //params.put("Accept-Language", "fr");
                return params;
            };

        };
        queue.add(jsonObjectRequest);
    }

}
