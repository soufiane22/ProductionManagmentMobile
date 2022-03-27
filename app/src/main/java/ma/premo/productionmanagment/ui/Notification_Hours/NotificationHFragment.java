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

import ma.premo.productionmanagment.databinding.FragmentNotificationHoursBinding;
import ma.premo.productionmanagment.models.NotificationHAdapter;
import ma.premo.productionmanagment.models.Notification_Hours;
import retrofit2.Call;
import ma.premo.productionmanagment.network.GetDataService;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public  class  NotificationHFragment extends Fragment {
    private DatePickerDialog datePickerDialog;
    private Button DateButton;
    private Button saveButton ;
    private NotificationHViewModel notificationViewModel;
    private @NonNull  FragmentNotificationHoursBinding binding;
    private Spinner lineSpinner;
    private Spinner shiftSpinner;
    private Spinner ofSpinner;
    private TextView nbr_operators;
    private TextView hTotal;
    private TextView hExtra;
    private TextView hDevolution;
    private TextView hStopped;
    private TextView hNewProject;
    private TextView texterror , remark;
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
    private final String url ="http://192.168.137.131:8090/notification_heures/" ;


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

              //relativeLayout.setVisibility(View.GONE);
              //addNotifaicationLayout.setVisibility(View.VISIBLE);

              FragmentTransaction transaction = getFragmentManager().beginTransaction();
              transaction.setReorderingAllowed(true);
              //transaction.remove(new NotificationHFragment());
              fragmentTransaction.replace( R.id.nav_host_fragment_content_main,new  add_notification(),null);
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

        JsonObjectRequest jsonObjectRequest  = new JsonObjectRequest(com.android.volley.Request.Method.GET, urlGetN,
                null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.e("Network", "Response " + response);
                    JSONArray data = response.getJSONArray("data1");
                    notificationsList = new ArrayList<>();
                    for(int i=0 ; i< data.length() ; i++){
                        JSONObject objet = data.getJSONObject(i);
                        Gson g = new Gson();
                        Notification_Hours notif = new Notification_Hours();
                         notif = g.fromJson(String.valueOf(objet), Notification_Hours.class);
                         notificationsList.add(notif);

                        int h_devolution = objet.getInt("h_devolution");
                        String chefEquipe = objet.getString("chefEquipe");
                        String of = objet.getString("of");
                        String id = objet.getString("id");
                        String Sperson = String.valueOf(h_devolution)+", "+id+", "+chefEquipe+", "+of+"\n\n";
                        //System.out.println("******NOTIFICATION*******");
                         //System.out.println(notif.toString());

                    }
                    setRecycleview();





                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pDialog.dismiss();

            }
        }, new com.android.volley.Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("*******error connection******");
                error.printStackTrace();
                Log.e("NetworkError", "Response " + error.networkResponse);


            }
        });

        Queue.add(jsonObjectRequest);

    }

    private void setRecycleview() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationHAdapter = new NotificationHAdapter(getContext(),notificationsList);
        recyclerView.setAdapter(notificationHAdapter);
    }











    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}