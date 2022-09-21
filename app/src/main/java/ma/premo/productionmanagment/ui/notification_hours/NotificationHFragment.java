package ma.premo.productionmanagment.ui.notification_hours;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.navigation.NavController;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.premo.productionmanagment.MainActivity;
import ma.premo.productionmanagment.R;

import ma.premo.productionmanagment.SignInActivity;
import ma.premo.productionmanagment.Utils.API;
import ma.premo.productionmanagment.Utils.JsonConvert;
import ma.premo.productionmanagment.databinding.FragmentNotificationHoursBinding;
import ma.premo.productionmanagment.models.Groupe;
import ma.premo.productionmanagment.models.Notification_Hours;
import ma.premo.productionmanagment.models.User;
import ma.premo.productionmanagment.ui.presenceFolder.AddPresenceViewModel;
import ma.premo.productionmanagment.ui.presenceFolder.PresenceViewModel;


public  class  NotificationHFragment extends Fragment  implements NotificationHAdapter.ItemClickListener{

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
    private NavController navController;
    List<Notification_Hours> notificationsList ;
    private ImageButton  refreshButton ;
    private View connectionView ;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button groupButton ;
    private Spinner leaderSpinner;
    public PresenceViewModel presenceViewModel;
    public AddPresenceViewModel addPresenceViewModel;
    private List<User> listLeaders;
    private ArrayAdapter<User> adapterLeaders ;
    private Groupe groupSelected;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenceViewModel = new ViewModelProvider(this,new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(PresenceViewModel.class);
        addPresenceViewModel = new ViewModelProvider(this,new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(AddPresenceViewModel.class);
        access_token =  ((MainActivity)getActivity()).access_token;
        leader =  ((MainActivity)getActivity()).user;


    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String function = "Chef%20Equipe";

       // notificationViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(NotificationHViewModel.class);
         fragmentManager = getFragmentManager();
         fragmentTransaction = fragmentManager.beginTransaction();
        connectionView =  inflater.inflate(R.layout.popup_connection, container, false);
        binding = FragmentNotificationHoursBinding.inflate(inflater, container, false);
        View view =  inflater.inflate(R.layout.fragment_notification_hours, container, false);
        View root = binding.getRoot();
        navController =  ((MainActivity)getActivity()).navController;
        groupButton = view.findViewById(R.id.groupButton);

        presenceViewModel.getUsersByFunction(access_token ,function);

        /********* popup connection to other group ******/

        groupButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                createNewDialog(inflater , container);

            }
        });

        /************** get all leaders **************/
        presenceViewModel.leadersMutableLiveData.observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                if(users != null){
                    listLeaders = new ArrayList<>(users);

                }else {
                    Toast.makeText(getContext(),"Server error",Toast.LENGTH_SHORT).show();
                }

            }
        });

        /************** get group of leader selected **************/
        addPresenceViewModel.groupeMutableLiveData.observe(this, new Observer<Groupe>() {
            @Override
            public void onChanged(@Nullable Groupe groupe) {
                if(groupe == null){
                    Toast.makeText(getContext(),"Group not exist",Toast.LENGTH_SHORT).show();
                    System.out.println("group null");
                }
                else{
                    groupSelected = groupe;
                }


            }
        });
        /***********************************************/


        refreshButton = view.findViewById(R.id.refreshbutton1);

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
        if(leader != null){
            getAllNotifications();
        }

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(leader != null){
                    pDialog.show();
                    getAllNotifications();

                }
            }
        });


        addButon.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
               AddNotificationHours fragment = new AddNotificationHours();

              Bundle bundle = new Bundle();
              bundle.putString("mode","add");

              navController.navigate(R.id.add_notification,bundle);
            }
           });





        return view;
    }
    String passwordgroup ;
    public void createNewDialog(LayoutInflater inflater , ViewGroup container){
        leaderSpinner = connectionView.findViewById(R.id.SpinnerChefEquipe);
        if(connectionView.getParent() != null) {
            ((ViewGroup)connectionView.getParent()).removeView(connectionView); // <- fix
        }
        EditText passwordGroup = connectionView.findViewById(R.id.groupPassword);

        dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setView(connectionView);
        dialogBuilder.setTitle("Connect to other group");
        adapterLeaders = new ArrayAdapter<User>(getContext(), android.R.layout.simple_spinner_dropdown_item, listLeaders);
        leaderSpinner.setAdapter(adapterLeaders);
        dialogBuilder.setCancelable(true)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        passwordgroup = passwordGroup.getText().toString();
                        if(!groupSelected.equals(null)){
                            if(passwordgroup.equals(groupSelected.getPasswordGroup())){
                                Toast.makeText(getContext(),"Connection successful",Toast.LENGTH_SHORT).show();
                                String presenceJsonString = JsonConvert.getGsonParser().toJson(groupSelected);
                                Bundle bundle = new Bundle();
                                bundle.putString("mode","add");
                                bundle.putString("groupJsonString",presenceJsonString);
                                bundle.putBoolean("forOtherGroup",true);
                                navController.navigate(R.id.add_notification,bundle);
                            }

                            else
                                Toast.makeText(getContext(),"Incorrect password",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(),"This leader not have group",Toast.LENGTH_SHORT).show();
                        }


                        passwordGroup.setText("");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        dialog = dialogBuilder.create();
        dialog.show();
        dialog.getWindow().setLayout(500,500);

        leaderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                User leader = (User) adapterView.getSelectedItem();
                addPresenceViewModel.getGroup(leader.getId(),access_token);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void getAllNotifications() {
        String state ="No%20Validate";
        String urlGetN = url +"get/state/"+leader.getId()+"/"+state;
        pDialog.show();
        JsonArrayRequest  jsonObjectRequest  = new JsonArrayRequest(com.android.volley.Request.Method.GET, urlGetN,
                null, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                    JSONArray data = response;
                    notificationsList = new ArrayList<>();
                    if(data.length()==0) {
                        if(getActivity().getApplicationContext() != null){
                            Toast.makeText(getActivity().getApplicationContext(), "No data available", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        for(int i=0 ; i< data.length() ; i++){

                            JSONObject objet = null;
                            try {
                                objet = data.getJSONObject(i);
                                Gson g = new Gson();
                                Notification_Hours notif = new Notification_Hours();

                                notif = g.fromJson(String.valueOf(objet), Notification_Hours.class);

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
                if ( error instanceof AuthFailureError) {
                    Toast.makeText(getActivity().getApplicationContext(), "token expired",Toast.LENGTH_LONG).show();
                    Intent loginIntent = new Intent(getContext() , SignInActivity.class);
                    startActivity(loginIntent);
                    getActivity().finish();
                }else{
                    //TODO
                    if(getContext() != null )
                        Toast.makeText(getContext(),"server error",Toast.LENGTH_SHORT).show();
                }



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

    private String getTodayDates() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day,month ,year);
    };


    private String makeDateString(int day, int month, int year) {
        return year+"-"+month+"-"+day;
    };
    private void setRecycleview() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationHAdapter = new NotificationHAdapter(getContext(),notificationsList , this);
        recyclerView.setAdapter(notificationHAdapter);
        notificationHAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(Notification_Hours notif) {
      //  AddNotificationHours fragment = new AddNotificationHours();
        Bundle bundle = new Bundle();
        String notifJsonString = JsonConvert.getGsonParser().toJson(notif);
        bundle.putSerializable("objet",notifJsonString);
        bundle.putString("mode","update");
        //fragment.setArguments(bundle);
      //  fragmentTransaction.replace( R.id.nav_host_fragment_content_main,fragment,null).addToBackStack(null);
      //  fragmentTransaction.commit();

        navController.navigate(R.id.action_notification_fragment_to_add_notification,bundle);

    }


}