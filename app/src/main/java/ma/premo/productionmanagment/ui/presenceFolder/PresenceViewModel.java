package ma.premo.productionmanagment.ui.presenceFolder;

import android.app.Application;
import android.app.ProgressDialog;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.premo.productionmanagment.MainActivity;
import ma.premo.productionmanagment.Utils.API;
import ma.premo.productionmanagment.Utils.JsonConvert;
import ma.premo.productionmanagment.models.DeclarationPresence;
import ma.premo.productionmanagment.models.Groupe;
import ma.premo.productionmanagment.models.Presence;
import ma.premo.productionmanagment.models.User;

public class PresenceViewModel extends AndroidViewModel {
    private Application application;
    private RequestQueue queue;
    private ProgressDialog pDialog;
    private DeclarationPresence declarationPresence = new DeclarationPresence();




    public MutableLiveData<DeclarationPresence> declarationPresenceMutableLiveData = new MutableLiveData<>();

    public PresenceViewModel(Application application) {
        super(application);
        this.application = application;
        queue = Volley.newRequestQueue(application);


    }


    public void getPresenses(String idLeader , String token, String date){
        String url = API.urlBackend + "presence/getgroup/"+idLeader+"/"+date;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArray = new JSONArray();
                        int sumOperators =0, totalHours=0;
                        String leader=null , engineer = null, groupe=null;
                        try {
                            jsonArray = response.getJSONArray("data1");
                          if(jsonArray.length()==0)
                              declarationPresenceMutableLiveData.setValue(null);
                          else{
                              for (int i = 0; i < jsonArray.length(); i++)
                              {
                                  Presence presence = JsonConvert.getGsonParser().fromJson(String.valueOf(jsonArray.getJSONObject(i)), Presence.class);

                                  if(presence.getOperateur().getFonction().equals("Operateur") && (presence.getEtat().equals("Present") || presence.getEtat().equals("Retard")))
                                      totalHours += presence.getNbrHeurs();
                                  if (presence.getOperateur().getFonction().equals("Operateur")  && (presence.getEtat().equals("Present") || presence.getEtat().equals("Retard"))){
                                      sumOperators++;
                                  }

                                  if(presence.getOperateur().getFonction().equals("Chef Equipe"))
                                      leader = presence.getOperateur().getNom()+" "+presence.getOperateur().getPrenom();
                                  engineer = presence.getIngenieur();
                                  groupe = presence.getGroupe();
                              }
                              declarationPresence.setDate(date);
                              declarationPresence.setSumOperators(sumOperators);
                              declarationPresence.setTotalHours(totalHours);
                              declarationPresence.setLeader(leader);
                              declarationPresence.setEngineer(engineer);
                              declarationPresence.setGroup(groupe);
                              System.out.println("1-----declarationPresence"+declarationPresence.toString());
                              declarationPresenceMutableLiveData.setValue(declarationPresence);
                          }

                        } catch (JSONException e) {
                            System.out.println("error convert to json object");
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("NetworkErro", "Response " + error.networkResponse);
                //declarationPresenceMutableLiveData.setValue(null);

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