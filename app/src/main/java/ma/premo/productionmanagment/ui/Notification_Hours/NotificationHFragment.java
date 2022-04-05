package ma.premo.productionmanagment.ui.Notification_Hours;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.premo.productionmanagment.R;

import ma.premo.productionmanagment.Utils.JsonConvert;
import ma.premo.productionmanagment.databinding.FragmentNotificationHoursBinding;
import ma.premo.productionmanagment.models.Line;
import ma.premo.productionmanagment.models.NotificationHAdapter;
import ma.premo.productionmanagment.models.Notification_Hours;
import ma.premo.productionmanagment.models.Of;
import ma.premo.productionmanagment.models.Produit;
import retrofit2.Call;
import ma.premo.productionmanagment.network.GetDataService;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public  class  NotificationHFragment extends Fragment  implements NotificationHAdapter.ItemClickListener{

    private NotificationHViewModel notificationViewModel;
    private @NonNull  FragmentNotificationHoursBinding binding;

    private RecyclerView recyclerView;
    private RequestQueue Queue;
    private AlertDialog.Builder builder;
    private NotificationHAdapter notificationHAdapter;
    private Button addButon;
    private RelativeLayout relativeLayout;
    private RelativeLayout addNotifaicationLayout;
    private ProgressDialog pDialog;
    private  FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private final String url ="http://192.168.137.48:8090/notification_heures/" ;


    List<Notification_Hours> notificationsList ;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        notificationViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(NotificationHViewModel.class);
         fragmentManager = getFragmentManager();
         fragmentTransaction = fragmentManager.beginTransaction();

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

        addNotifaicationLayout = view.findViewById(R.id.add_notification1);
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
        String urlGetN = url +"getAll/";
        pDialog.show();
        JsonObjectRequest jsonObjectRequest  = new JsonObjectRequest(com.android.volley.Request.Method.GET, urlGetN,
                null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                   // Log.e("Network", "Response " + response);
                    JSONArray data = response.getJSONArray("data1");
                    notificationsList = new ArrayList<>();
                    for(int i=0 ; i< data.length() ; i++){
                        try {
                            JSONObject objet = data.getJSONObject(i);
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
                            //System.out.println("******NOTIFICATION*******");
                            //System.out.println(notif.toString()+"\n");
                            pDialog.dismiss();
                            //Toast.makeText(getContext(),"success",Toast.LENGTH_SHORT).show();

                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                            pDialog.dismiss();

                    }
                }
                setRecycleview();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(),"server error",Toast.LENGTH_SHORT).show();
                }
                pDialog.dismiss();


            }
        }, new com.android.volley.Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("*******error connection******");
                error.printStackTrace();
                Log.e("NetworkError", "Response " + error.networkResponse);
                pDialog.dismiss();
                //Toast.makeText(getContext(),"server error",Toast.LENGTH_SHORT).show();


            }
        });

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