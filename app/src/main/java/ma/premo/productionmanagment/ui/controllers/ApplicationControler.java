package ma.premo.productionmanagment.ui.controllers;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelStoreOwner;
import android.util.Log;

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
import ma.premo.productionmanagment.models.PresenceGroup;
import ma.premo.productionmanagment.ui.presenceFolder.PresenceViewModel;

public class ApplicationControler extends Application {

    private static ApplicationControler sInstance;
    private RequestQueue queue  =Volley.newRequestQueue(getApplicationContext());;
    private PresenceViewModel presenceViewModel ;


    @Override
    public void onCreate() {
        super.onCreate();

        // initialize the singleton
        sInstance = this;
        queue = Volley.newRequestQueue(getApplicationContext());
        presenceViewModel = new ViewModelProvider((ViewModelStoreOwner) this,new ViewModelProvider.AndroidViewModelFactory(getInstance())).get(PresenceViewModel.class);;
    }

    public static synchronized ApplicationControler getInstance() {
        return sInstance;
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
                                presenceViewModel.declarationGroupPresenceMutableLiveData.setValue(null);
                            else {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    PresenceGroup presenceGroup = JsonConvert.getGsonParser().fromJson(String.valueOf(jsonArray.getJSONObject(i)), PresenceGroup.class);
                                    listpresenceGroups.add(presenceGroup);

                                }
                                // System.out.println("list presence group"+listpresenceGroups.toString());
                                presenceViewModel.declarationGroupPresenceMutableLiveData.setValue(listpresenceGroups);

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
}
