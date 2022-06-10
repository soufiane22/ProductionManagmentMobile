package ma.premo.productionmanagment.ui.presenceFolder;

import android.app.Application;
import android.app.ProgressDialog;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

import ma.premo.productionmanagment.MainActivity;
import ma.premo.productionmanagment.Utils.API;
import ma.premo.productionmanagment.Utils.JsonConvert;
import ma.premo.productionmanagment.models.DeclarationPresence;
import ma.premo.productionmanagment.models.Groupe;
import ma.premo.productionmanagment.models.Notification_Hours;
import ma.premo.productionmanagment.models.Presence;
import ma.premo.productionmanagment.models.PresenceGroup;
import ma.premo.productionmanagment.models.User;

public class PresenceViewModel extends AndroidViewModel  {
    private Application application;
    private RequestQueue queue;
    private ProgressDialog pDialog;

    public MutableLiveData<List<PresenceGroup>> declarationGroupPresenceMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<User>> leadersMutableLiveData = new MutableLiveData<>();

    public PresenceViewModel(Application application) {
        super(application);
        this.application = application;
        queue = Volley.newRequestQueue(application);

    }

    public void getPresenses(String idLeader , String token){
        String url = API.urlBackend + "presencegroup/getlist/"+idLeader+"/"+7;
        List<PresenceGroup> listpresenceGroups = new ArrayList<>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArray = new JSONArray();

                        try {
                            jsonArray = response.getJSONArray("data1");
                            if (jsonArray.length() == 0 || jsonArray == null)
                                declarationGroupPresenceMutableLiveData.setValue(null);
                            else {
                                  System.out.println("list presences group "+jsonArray.toString());
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject objet = null;
                                    Gson g = new Gson();
                                    objet = jsonArray.getJSONObject(i);
                                    System.out.println("list presences group "+objet.toString());
                                    PresenceGroup presenceGroup = new PresenceGroup();
                                    presenceGroup = g.fromJson(String.valueOf(objet), PresenceGroup.class);
                                    // presenceGroup = JsonConvert.getGsonParser().fromJson(String.valueOf(jsonArray.getJSONObject(i)), PresenceGroup.class);
                                    listpresenceGroups.add(presenceGroup);

                                }

                                declarationGroupPresenceMutableLiveData.setValue(listpresenceGroups);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("NetworkErro", "Response " + error.networkResponse);


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

    public void searchPresence(String idLeader, String date , String token){
        String url = API.urlBackend + "presencegroup/search/"+idLeader+"/"+date;
        List<PresenceGroup> listpresenceGroups = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArray = new JSONArray();
                        try {
                            jsonArray = response.getJSONArray("data1");
                            if (jsonArray.length() == 0 || jsonArray == null)
                                declarationGroupPresenceMutableLiveData.setValue(null);
                            else {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    PresenceGroup presenceGroup = JsonConvert.getGsonParser().fromJson(String.valueOf(jsonArray.getJSONObject(i)), PresenceGroup.class);
                                    listpresenceGroups.add(presenceGroup);


                                }
                                Log.d("list presences ",listpresenceGroups.toString());
                                 declarationGroupPresenceMutableLiveData.setValue(listpresenceGroups);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }



                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        declarationGroupPresenceMutableLiveData.setValue(null);
                        Log.e("NetworkErro", "Response " + error.networkResponse);

                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", token);
                return params;
            };

        };

        queue.add(jsonObjectRequest);
    }

    public  void  deletePresenceGroup(String id , String token){
        String url = API.urlBackend + "presencegroup/delete/"+id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               Log.d("succuss",response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error",error.toString());
            }
        }){
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
                            leadersMutableLiveData.setValue(listLeaders);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("NetworkError", "Response " + error.networkResponse);
                leadersMutableLiveData.setValue(null);

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