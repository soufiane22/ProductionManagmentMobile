package ma.premo.productionmanagment.ui.presenceFolder;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.premo.productionmanagment.Utils.API;
import ma.premo.productionmanagment.Utils.JsonConvert;
import ma.premo.productionmanagment.models.Groupe;
import ma.premo.productionmanagment.models.Notification_Hours;
import ma.premo.productionmanagment.models.Presence;
import ma.premo.productionmanagment.models.User;


public class AddPresenceViewModel extends AndroidViewModel {
    private Application application;
    private RequestQueue queue;
    private ProgressDialog pDialog;



    public Presence presence;
    public MutableLiveData<Groupe> groupeMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<User>> allUsersMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<User>> usersMutableLiveData = new MutableLiveData<>();


    public AddPresenceViewModel(Application application) {

        super(application);
        // super(application);
        this.application = application;
        queue = Volley.newRequestQueue(application);
        //getAllUsers();
       // getLeaders();

    }


    public void getGroup(String idLeader, String token) {
      //  String leader = leaderMutableLiveData.getValue();
        Groupe groupe = new Groupe();
        String url = API.urlBackend + "groupe/get/leader/"+idLeader;
        //pDialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Groupe groupe = new Groupe();
                        JSONObject data = null;

                        try {
                            data = response.getJSONObject("object");
                            groupe = JsonConvert.getGsonParser().fromJson(String.valueOf(data), Groupe.class);
                            groupeMutableLiveData.setValue(groupe);

                        } catch (JSONException e) {
                            System.out.println("error convert to json");
                            e.printStackTrace();
                            groupeMutableLiveData.setValue(null);
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("*******error connection******");
                error.printStackTrace();
                Log.e("NetworkError", "Response " + error.networkResponse);
                //pDialog.dismiss();
                 groupeMutableLiveData.setValue(null);


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

    public void getAllUsers(String token) {
        List<User> listUsers = new ArrayList<>();
        String url = API.urlBackend + "user/getAll";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArray = new JSONArray();
                        try {
                            jsonArray = response.getJSONArray("data1");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                User user = JsonConvert.getGsonParser().fromJson(String.valueOf(jsonArray.getJSONObject(i)), User.class);
                                listUsers.add(user);
                            }

                            allUsersMutableLiveData.setValue(listUsers);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("NetworkError", "Response " + error.networkResponse);
                allUsersMutableLiveData.setValue(null);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization",token);
                //params.put("Accept-Language", "fr");
                return params;
            };
        };

        queue.add(jsonObjectRequest);

    }

    public void getUsersByFunction(String access_token ,String function){
        List<User> listLeaders = new ArrayList<>();

        String url = API.urlBackend + "user/get/function/"+function;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = new JSONArray();

                try {
                    jsonArray = response.getJSONArray("data1");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        User user = JsonConvert.getGsonParser().fromJson(String.valueOf(jsonArray.getJSONObject(i)), User.class);
                        listLeaders.add(user);
                    }
                    usersMutableLiveData.setValue(listLeaders);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("NetworkError", "Response " + error.networkResponse);
                usersMutableLiveData.setValue(null);

            }
        }
        ){
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






}
