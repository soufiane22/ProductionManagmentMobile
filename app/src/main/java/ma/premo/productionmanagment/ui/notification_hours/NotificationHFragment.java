package ma.premo.productionmanagment.ui.notification_hours;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
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

import ma.premo.productionmanagment.MainActivity;
import ma.premo.productionmanagment.R;

import ma.premo.productionmanagment.Utils.API;
import ma.premo.productionmanagment.Utils.JsonConvert;
import ma.premo.productionmanagment.databinding.FragmentNotificationHoursBinding;
import ma.premo.productionmanagment.models.Line;
import ma.premo.productionmanagment.models.NotificationHAdapter;
import ma.premo.productionmanagment.models.Notification_Hours;
import ma.premo.productionmanagment.models.Of;
import ma.premo.productionmanagment.models.Produit;
import ma.premo.productionmanagment.models.User;


public  class  NotificationHFragment extends Fragment  implements NotificationHAdapter.ItemClickListener{

    private NotificationHViewModel notificationViewModel;
    private FragmentNotificationHoursBinding binding;

    private RecyclerView recyclerView;
    private RequestQueue Queue;
    private AlertDialog.Builder builder;
    private NotificationHAdapter notificationHAdapter;
    private Button addButon;
    private RelativeLayout relativeLayout;
    private ProgressDialog pDialog;
    private  FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private final String url = API.urlBackend+"notification_heures/" ;
    private String access_token;
    private User  leader = new User();

    List<Notification_Hours> notificationsList ;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        notificationViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(NotificationHViewModel.class);
         fragmentManager = getFragmentManager();
         fragmentTransaction = fragmentManager.beginTransaction();
        access_token =  ((MainActivity)getActivity()).access_token;
         leader =  ((MainActivity)getActivity()).user;
        binding = FragmentNotificationHoursBinding.inflate(inflater, container, false);
        View view =  inflater.inflate(R.layout.fragment_notification_hours, container, false);
        View root = binding.getRoot();


/*
        binding.AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_notification add_notification = new add_notification();
                fragmentTransaction.replace( R.id.fragment_notification,  add_notification);
                fragmentTransaction.commit();

            }
        });

 */

        builder = new AlertDialog.Builder(getContext());
        Queue = Volley.newRequestQueue(getContext());
        relativeLayout = view.findViewById(R.id.fragment_notification);
        addButon = view.findViewById(R.id.AddButton);
        recyclerView= view.findViewById(R.id.recycler_view);
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...PLease wait");
        getAllNotifications();

        addButon.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
               add_notification fragment = new  add_notification();
              //relativeLayout.setVisibility(View.GONE);
              //addNotifaicationLayout.setVisibility(View.VISIBLE);
              Bundle bundle = new Bundle();
              bundle.putString("mode","add");
              fragment.setArguments(bundle);
              FragmentTransaction transaction = getFragmentManager().beginTransaction();
              transaction.setReorderingAllowed(true);
              //transaction.remove(new NotificationHFragment());
              fragmentTransaction.replace( R.id.nav_host_fragment_content_main,fragment,null).addToBackStack(null);
              fragmentTransaction.commit();
            }
           });



      //  final TextView textView = binding.textGallery;
        notificationViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
               // textView.setText(s);

            }
        });

        return view;
    }


    private void getAllNotifications() {
        String urlGetN = url +"getlist/"+leader.getId();
        pDialog.show();
        JsonArrayRequest  jsonObjectRequest  = new JsonArrayRequest(com.android.volley.Request.Method.GET, urlGetN,
                null, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                   Log.e("Response", "Response " + response);
                    JSONArray data = response;
                    notificationsList = new ArrayList<>();
                    if(data.length()==0) {
                        Toast.makeText(getContext(), "No data available", Toast.LENGTH_SHORT).show();
                    }else{
                        for(int i=0 ; i< data.length() ; i++){

                            JSONObject objet = null;
                            try {
                                objet = data.getJSONObject(i);
                                Gson g = new Gson();
                                Notification_Hours notif = new Notification_Hours();
                                Of of = new Of();
                                Line line = new Line();
                                Produit product = new Produit();

                                notif = g.fromJson(String.valueOf(objet), Notification_Hours.class);

                                JSONObject jsonObject = objet.getJSONObject("of");
                                of = g.fromJson(String.valueOf(jsonObject), Of.class);
                                notif.setOF(of);

                                JSONObject jsonObjectLine = objet.getJSONObject("ligne");
                                line = g.fromJson(String.valueOf(jsonObjectLine), Line.class);
                                notif.setLine(line);

                                JSONObject jsonObjectProduct = objet.getJSONObject("produit");
                                product = g.fromJson(String.valueOf(jsonObjectProduct), Produit.class);
                                notif.setProduit(product);
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

        Queue.add(jsonObjectRequest);

    }

    private void setRecycleview() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationHAdapter = new NotificationHAdapter(getContext(),notificationsList , this);
        recyclerView.setAdapter(notificationHAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(Notification_Hours notif) {
        add_notification fragment = new add_notification();
        Bundle bundle = new Bundle();
        String notifJsonString = JsonConvert.getGsonParser().toJson(notif);
        bundle.putSerializable("objet",notifJsonString);
        bundle.putString("mode","update");
        fragment.setArguments(bundle);
        fragmentTransaction.replace( R.id.nav_host_fragment_content_main,fragment,null).addToBackStack(null);
        fragmentTransaction.commit();

    }
}