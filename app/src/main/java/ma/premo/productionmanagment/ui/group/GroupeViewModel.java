package ma.premo.productionmanagment.ui.group;

import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.premo.productionmanagment.Utils.API;
import ma.premo.productionmanagment.Utils.JsonConvert;
import ma.premo.productionmanagment.models.Line;
import ma.premo.productionmanagment.models.User;

public class GroupeViewModel  extends AndroidViewModel {
    private Application application;
    private RequestQueue queue;
    private ProgressDialog pDialog;

    public MutableLiveData<List<Line>> listLineMutableLiveData = new MutableLiveData<>();
    public  MutableLiveData<Boolean> tokenExpired = new MutableLiveData<>();

    public GroupeViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        queue = Volley.newRequestQueue(application);
        pDialog = new ProgressDialog(getApplication());
        pDialog.setMessage("Loading...PLease wait");
    }

    public void getAllLines(String token) {
        List<Line> listLines = new ArrayList<>();
        String url = API.urlBackend + "line/getAll";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    JSONArray jsonArray = new JSONArray();
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            jsonArray = response.getJSONArray("data1");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Line line = JsonConvert.getGsonParser().fromJson(String.valueOf(jsonArray.getJSONObject(i)), Line.class);
                                listLines.add(line);
                            }

                            listLineMutableLiveData.setValue(listLines);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if ( error instanceof AuthFailureError) {
                    Toast.makeText(application, "token expired",Toast.LENGTH_LONG).show();
                    tokenExpired.setValue(true);
                }else{
                    Toast.makeText(application.getApplicationContext() ,"Server error",Toast.LENGTH_SHORT).show();
                }

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization",token);
                //params.put("Accept-Language", "fr");
                return params;
            }
        };

        queue.add(jsonObjectRequest);
    }

    public void updatePerson(String id, User user, String token , AlertDialog pDialog) throws ParseException {
        String url = API.urlBackend + "user/update/"+id;
        String groupStringJson = JsonConvert.getGsonParser().toJson(user);
        JSONParser parser = new JSONParser();
        org.json.simple.JSONObject json = (org.json.simple.JSONObject) parser.parse(groupStringJson);
        org.json.JSONObject userJson = (org.json.JSONObject) new org.json.JSONObject(json);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, userJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        Toast.makeText(getApplication(), "save with success", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Log.e("Error ",error.toString());
                        if ( error instanceof AuthFailureError) {
                            Toast.makeText(application, "token expired",Toast.LENGTH_LONG).show();
                            tokenExpired.setValue(true);
                        }else{
                            Toast.makeText(application.getApplicationContext() ,"Server error",Toast.LENGTH_SHORT).show();
                        }
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization",token);
                //params.put("Accept-Language", "fr");
                return params;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjectRequest);
    }
}
