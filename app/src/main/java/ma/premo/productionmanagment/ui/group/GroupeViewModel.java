package ma.premo.productionmanagment.ui.group;

import android.app.Application;
import android.app.ProgressDialog;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

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
import ma.premo.productionmanagment.models.Line;
import ma.premo.productionmanagment.models.User;

public class GroupeViewModel  extends AndroidViewModel {
    private Application application;
    private RequestQueue queue;
    private ProgressDialog pDialog;

    public MutableLiveData<List<String>> listLineMutableLiveData = new MutableLiveData<>();

    public GroupeViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        queue = Volley.newRequestQueue(application);
    }

    public void getAllLines(String token) {
        List<String> listLines = new ArrayList<>();
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
                                listLines.add(line.getDesignation());
                            }
                            System.out.println("list of all lines"+listLines.toString());
                            listLineMutableLiveData.setValue(listLines);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
}
