package ma.premo.productionmanagment.ui.notification_hours;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.premo.productionmanagment.MainActivity;
import ma.premo.productionmanagment.R;
import ma.premo.productionmanagment.Utils.API;
import ma.premo.productionmanagment.Utils.JsonConvert;
import ma.premo.productionmanagment.databinding.AddNotification1Binding;
import ma.premo.productionmanagment.models.Groupe;
import ma.premo.productionmanagment.models.Line;
import ma.premo.productionmanagment.models.Notification_Hours;
import ma.premo.productionmanagment.models.Of;
import ma.premo.productionmanagment.models.PresenceGroup;
import ma.premo.productionmanagment.models.Produit;
import ma.premo.productionmanagment.models.Statistic;
import ma.premo.productionmanagment.models.User;
import ma.premo.productionmanagment.ui.presenceFolder.AddPresenceViewModel;
import ma.premo.productionmanagment.ui.statistics.StatisticAdapter;
import ma.premo.productionmanagment.ui.statistics.StatisticViewModel;


public class AddNotificationHours extends Fragment {
    private AddPresenceViewModel addPresenceViewModel;
    private AddNotification1Binding binding;
    private DatePickerDialog datePickerDialog;
    private Button DateButton;
    private Button saveButton ;
    private NotificationHViewModel notificationViewModel;
    private Spinner productSpinner ;
    private Spinner lineSpinner;
    private Spinner shiftSpinner;
    private TextView nbr_operators;
    private TextView hTotal;
    private TextView hExtra;
    private TextView hDevolution;
    private TextView hStopped;
    private TextView hNormal;
    private TextView hNewProject;
    private TextView texterror , remark;
    private RecyclerView recyclerView;
    private RequestQueue Queue;
    private AlertDialog.Builder builder;
    ProgressDialog pDialog;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private Map<String, String> mParams;
    private final String url = API.urlBackend;
    private AddNotificationViewModel addNotificationViewModel;
    private List<Notification_Hours> notificationsList ;
    private ArrayList OfListSpinner;
    private ArrayList<Of> OfList;
    private Of ofSelected;
    private ArrayList lineListSpinner;
    private ArrayList<Line> lineList;
    private Line lineSelected;
    private ArrayList productListSpinner;
    private ArrayList<Produit> productList;
    private Produit produiSelected;
    private Notification_Hours notifItem;
    private String mode;
    private String access_token;
    private  User leader;
    private Groupe myGroupe = new Groupe();
    int totalHours = 0;
    public StatisticViewModel statisticViewModel;
    private List<Statistic> listStatistic = new ArrayList<>();;
    private StatisticAdapter adapter;
    private String startDateStr;
    private String endDateStr;
    private NavController navController;
    private  ArrayAdapter adapterProducts;
    private boolean correctOF = true;
    private Groupe groupSelected;
    private Boolean forOtherGroup = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        addNotificationViewModel = new ViewModelProvider(this,new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(AddNotificationViewModel.class);
        addPresenceViewModel = new ViewModelProvider(this,new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(AddPresenceViewModel.class);
        statisticViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(StatisticViewModel.class);

        binding = AddNotification1Binding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        View view  = binding.getRoot();
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...PLease wait");
         access_token =  ((MainActivity)getActivity()).access_token;
         leader =  ((MainActivity)getActivity()).user;
        Queue = Volley.newRequestQueue(getContext());
        navController =  ((MainActivity)getActivity()).navController;
        //navController.popBackStack(R.id.nav_home,true);
        getLines();
        if(leader != null){
            addPresenceViewModel.getGroup(leader.getId(), access_token);
        }

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        DateButton = (Button) view.findViewById(R.id.Datebutton);
        saveButton = (Button) view.findViewById(R.id.button2) ;
        lineSpinner = (Spinner) view.findViewById(R.id.Linespinner) ;

        productSpinner =(Spinner) view.findViewById(R.id.Productspinner);
        nbr_operators = view.findViewById(R.id.nbrOperators);
        //hTotal = view.findViewById(R.id.hTotal);
        hExtra = view.findViewById(R.id.hExtra);
        hDevolution = view.findViewById(R.id.hDevolution);
        hStopped = view.findViewById(R.id.hStopped);
        hNewProject = view.findViewById(R.id.hNewProject);
        hNormal = view.findViewById(R.id.hNormal);
        remark = (TextView) view.findViewById(R.id.Remark) ;
        ofSelected = new Of();
        lineSelected = new Line();
        produiSelected = new Produit();
        notifItem = new Notification_Hours();
        nbr_operators.setText("0");
        //hTotal.setText("0");
        hExtra.setText("0");
        hDevolution.setText("0");
        hStopped.setText("0");
        hNewProject.setText("0");
        hNormal.setText("0");
        binding.TotalOutput.setText("0");
        binding.TotalScrap.setText("0");
        notificationsList = new ArrayList<>();
        mParams  = new HashMap<>();
        binding.statisticRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.Datebutton.setText(getTodayDates());

        Bundle args = getArguments();
        String groupJson = "";
        mode  = args.getString("mode");
        groupJson = args.getString("groupJsonString");
        if(groupJson != null){
            groupSelected = JsonConvert.getGsonParser().fromJson(groupJson, Groupe.class);
            forOtherGroup = true;

             TextView shift = view.findViewById(R.id.ShiftGroup);
             binding.ShiftGroup.setText(groupSelected.getShift());
             binding.textViewLeaderName.setText(groupSelected.getLeaderName());
        }else{
            forOtherGroup = false;
            view.findViewById(R.id.leaderNameLyout).setVisibility(View.GONE);
        }


        if(mode.equals("update")){
            String notifJsonString = args.getString("objet");
            notifItem = JsonConvert.getGsonParser().fromJson(notifJsonString, Notification_Hours.class);
            setForms(notifItem);

        }

        /******* get start and end date in the current month ****************/
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date monthFirstDay = calendar.getTime();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date monthLastDay = calendar.getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
         startDateStr = df.format(monthFirstDay);
         endDateStr = df.format(monthLastDay);
         binding.startDate.setText(startDateStr);
        binding.endDate.setText(endDateStr);
        Log.e("DateFirstLast",startDateStr+" "+endDateStr);
        /************************************************************************************/
        addPresenceViewModel.groupeMutableLiveData.observe(this, new Observer<Groupe>() {
            @Override
            public void onChanged(@Nullable Groupe groupe) {
                if(groupe != null){
                    myGroupe = groupe;
                    if(!forOtherGroup) {
                        binding.ShiftGroup.setText(myGroupe.getShift());
                        ArrayAdapter<Line> adapter = new ArrayAdapter<Line>(getContext(), android.R.layout.simple_spinner_dropdown_item, myGroupe.getListLine());
                        binding.Linespinner.setAdapter(adapter);
                        statisticViewModel.getStatistics(access_token, startDateStr, endDateStr, myGroupe.getListLine().get(0).getId());
                      }
                    if(mode.equals("update")){
                        lineSpinner.setSelection(getIndex(lineSpinner, notifItem.getLigne().getDesignation()));
                        productSpinner.setSelection(getIndex(productSpinner,notifItem.getProduit().getDesignation()));
                        lineSelected = notifItem.getLigne();
                    }
                    if(forOtherGroup){
                        ArrayAdapter<Line> adapterLine = new ArrayAdapter<Line>(getContext(),android.R.layout.simple_spinner_dropdown_item,groupSelected.getListLine());
                        binding.Linespinner.setAdapter(adapterLine);
                        statisticViewModel.getStatistics(access_token, startDateStr, endDateStr, groupSelected.getListLine().get(0).getId());
                    }
                }else{
                    Toast.makeText(getContext(),"Server error",Toast.LENGTH_SHORT).show();
                }

            }
        });
       // CreateSpinners();
       // getShift();
       // getOfSelected();
        initDatePicker();


        nbr_operators.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")){
                    int normalHours ;
                    Integer nbrOparators = Integer.parseInt(nbr_operators.getText().toString());
                    normalHours = nbrOparators*8;
                    hNormal.setText(String.valueOf(normalHours));
                }else{
                    hNormal.setText("0");
                }

            }
        });

       binding.Datebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datePickerDialog.show();
            }
        });




        //Notification_Hours notif= Utils.getGsonParser().fromJson(personJsonString, Person.class);

      //  hNormal.setEnabled(false);



       binding.OF.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void afterTextChanged(Editable editable) {
               int size = editable.length();
               if(size < 7){
                   correctOF = false;
               }else{
                   correctOF = true;
               }


           }
       });




        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nbr_operators.getText().toString().isEmpty() || hExtra.getText().toString().isEmpty()
                || hDevolution.getText().toString().isEmpty() || hStopped.getText().toString().isEmpty() || hNewProject.getText().toString().isEmpty()
                || binding.OF.getText().toString().isEmpty() || binding.TotalScrap.getText().toString().isEmpty()
                        || binding.TotalOutput.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Please fill in the empty fields please !", Toast.LENGTH_LONG).show();
                }else{

                       if(Integer.parseInt(nbr_operators.getText().toString()) ==0 ){
                           Toast.makeText(getContext(), "The number of operators !", Toast.LENGTH_LONG).show();
                       }else{

                           int extraHours= Integer.parseInt(hExtra.getText().toString());
                           int devolutionHours= Integer.parseInt(hDevolution.getText().toString());
                           int StoppedHours= Integer.parseInt(hStopped.getText().toString());
                           int newProjectHours= Integer.parseInt(hNewProject.getText().toString());
                           int normalHours =  Integer.parseInt(hNormal.getText().toString());
                           totalHours = normalHours + newProjectHours + StoppedHours + devolutionHours + extraHours;

                           if(!correctOF){
                               Toast.makeText(getActivity().getApplicationContext(),"The OF must be greater or equal 7",Toast.LENGTH_LONG).show();
                           }else {
                               if(mode == "add"){
                                   try {

                                       save_notification();
                                   } catch (ParseException | java.text.ParseException e) {
                                       e.printStackTrace();
                                   }
                               }
                               if(mode == "update"){
                                   try {
                                       update_notification();
                                   } catch (ParseException e) {
                                       e.printStackTrace();
                                   }
                               }

                           }



                       }
                }

            }

        });

        statisticViewModel.StatisticsMutableLiveData.observe(this, new Observer<List<Statistic>>() {
            @Override
            public void onChanged(@Nullable List<Statistic> statistics) {
                listStatistic.clear();
                listStatistic = statistics;
                if (listStatistic == null || listStatistic.size() == 0) {
                    Toast.makeText(getActivity().getApplicationContext(), "No Data found", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } else {
                    //onResum();

                    adapter = new StatisticAdapter(getContext(), listStatistic);
                    binding.statisticRecyclerView.setAdapter(adapter);
                    pDialog.dismiss();
                }
            }
        });

        return view;
    }

    private void setForms(Notification_Hours notification_hours) {
        if(notification_hours != null){
            binding.Datebutton.setText(notification_hours.getDate());
            // shiftSpinner.setSelection(getIndex(shiftSpinner, notification_hours.getShift()))
           // binding.Productspinner.setTag(notification_hours.getProduit().getReference());
            binding.OF.setText(String.valueOf(notification_hours.getOF()));
            nbr_operators.setText(String.valueOf(notification_hours.getNbr_operateurs()));
          //  hTotal.setText(String.valueOf(notification_hours.getTotal_h()));
            hNormal.setText(String.valueOf(notification_hours.getH_normal()));
            hExtra.setText(String.valueOf(notification_hours.getH_sup()));
            hStopped.setText(String.valueOf(notification_hours.getH_arrete()));
            hDevolution.setText(String.valueOf(notification_hours.getH_devolution()));
            hNewProject.setText(String.valueOf(notification_hours.getH_nouvau_projet()));
            remark.setText(String.valueOf(notification_hours.getRemark()));
            binding.TotalScrap.setText(String.valueOf( notification_hours.getTotalScrap()));
            binding.TotalOutput.setText(String.valueOf(notification_hours.getTotalOutput()));
        }

    }

    //get the position of line
    private int getIndex(Spinner spinner, String myString){
            for (int i=0;i<spinner.getCount();i++){
                if (spinner.getItemAtPosition(i).toString().equals(myString)){
                    return i;
                }
            }



        return 0;
    }

    private void getProducts() {
        pDialog.show();
        String urlLine = url+"produit/get/line/"+lineSelected.getId();
        JsonObjectRequest jsonObjectRequest  = new JsonObjectRequest(com.android.volley.Request.Method.GET, urlLine,
                null, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("data1");
                    productListSpinner = new ArrayList();
                    productList = new ArrayList<Produit>();
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject objet = data.getJSONObject(i);
                        Gson g = new Gson();
                        Produit produit = new Produit();
                        produit = g.fromJson(String.valueOf(objet), Produit.class);
                        productList.add(produit);
                    }


                    if(productList != null)
                     adapterProducts = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, productList);
                    binding.Productspinner.setAdapter(adapterProducts);
                    if (mode.equals("update")){

                        productSpinner.setSelection(getIndex(binding.Productspinner, notifItem.getProduit().getReference()));
                    }
                    //setSpinner( productListSpinner, productSpinner );

                    pDialog.dismiss();


                } catch (JSONException e) {
                    e.printStackTrace();
                    pDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("NetworkError", "Response " + error.networkResponse);
                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();

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
        Queue.add(jsonObjectRequest);
    }

    private void getLines() {

        String urlLine = url+"line/getAll/";
        pDialog.show();

        JsonObjectRequest jsonObjectRequest  = new JsonObjectRequest(com.android.volley.Request.Method.GET, urlLine,
                null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray data = response.getJSONArray("data1");
                    lineListSpinner  = new ArrayList();
                    lineList = new ArrayList<Line>();
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject objet = data.getJSONObject(i);
                        Gson g = new Gson();
                        Line line = new Line();
                        line = g.fromJson(String.valueOf(objet), Line.class);
                        lineList.add(line);

                    }


                    getLineSelected();
                    /*
                    for (Line l : lineList) {
                        lineListSpinner.add(lineList.indexOf(l), l.getDesignation());
                    }
                     */
                    //setSpinner( lineListSpinner, lineSpinner );
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("*******error connection******");
                error.printStackTrace();
                Log.e("NetworkError", "Response " + error.networkResponse);
                pDialog.dismiss();

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
        Queue.add(jsonObjectRequest);
    }

    private void getOFs() {
        String urlOF = url+"of/get/produit/"+produiSelected.getId();
        pDialog.show();
        JsonObjectRequest jsonObjectRequest  = new JsonObjectRequest(com.android.volley.Request.Method.GET, urlOF,
                null, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Log.e("OFs=====>", "Response " + response);
                    JSONArray data = response.getJSONArray("data1");
                    OfListSpinner = new ArrayList();
                    OfList = new ArrayList<Of>();
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject objet = data.getJSONObject(i);
                        Gson g = new Gson();
                        Of of = new Of();
                        of = g.fromJson(String.valueOf(objet), Of.class);
                        OfList.add(of);

                    }
                    for (Of o : OfList) {

                        OfListSpinner.add(OfList.indexOf(o), o.getReference());

                    }

                   // setSpinner( OfListSpinner, ofSpinner );
                    pDialog.dismiss();


                } catch (JSONException e) {
                    e.printStackTrace();
                    pDialog.dismiss();
                }
            }
        },
        new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("*******error connection******");
                error.printStackTrace();
                Log.e("NetworkError", "Response " + error.networkResponse);
                pDialog.dismiss();

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
        Queue.add(jsonObjectRequest);

    }



    public void save_notification() throws ParseException, java.text.ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        date = sdf.parse(binding.Datebutton.getText().toString());

        if(lineSelected == null || binding.Productspinner.getSelectedItem()==null){
            Toast.makeText(getContext(),"please select the line and the product",Toast.LENGTH_SHORT).show();
        }else{

            pDialog.show();
            String urlSaveN = url+"notification_heures/save/";
            Notification_Hours notif = new Notification_Hours();
            notif.setDate(binding.Datebutton.getText().toString());
           // notif.setCreatedAt(date);
            if(!forOtherGroup){
                notif.setShift(myGroupe.getShift());
                notif.setIdLeader(leader.getId());
                String name = leader.getPrenom()+" "+leader.getNom();
                notif.setLeaderName(name);
            }
            if(forOtherGroup){
                notif.setShift(groupSelected.getShift());
                notif.setIdLeader(groupSelected.getChefEquipe());
                notif.setLeaderName(groupSelected.getLeaderName());
            }

            notif.setLine(lineSelected);
            notif.setProduit((Produit) binding.Productspinner.getSelectedItem());
            notif.setOF(Integer.parseInt(binding.OF.getText().toString()));
            notif.setNbr_operateurs(Integer. parseInt(nbr_operators.getText().toString()));
            notif.setTotal_h(totalHours);
            notif.setH_sup(Integer. parseInt(hExtra.getText().toString()));
            notif.setH_devolution(Integer. parseInt(hDevolution.getText().toString()));
            notif.setH_arrete(Integer. parseInt(hStopped.getText().toString()));
            notif.setH_nouvau_projet(Integer. parseInt(hNewProject.getText().toString()));
            notif.setH_normal(Integer. parseInt(hNormal.getText().toString()));
            notif.setRemark(binding.Remark.getText().toString());
            notif.setTotalOutput(Integer.parseInt(binding.TotalOutput.getText().toString()));
            notif.setTotalScrap(Integer.parseInt(binding.TotalScrap.getText().toString()));
            String notifJsonString = JsonConvert.getGsonParser().toJson(notif);
            JSONParser parser = new JSONParser();
            org.json.simple.JSONObject json = (org.json.simple.JSONObject) parser.parse(notifJsonString);
            org.json.JSONObject notifJson = (org.json.JSONObject) new org.json.JSONObject(json);

            mParams.put("idOf",ofSelected.getId());

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,urlSaveN,notifJson,
                    response -> {
                        Log.e("notif to add",json.toString());
                        Toast.makeText(getContext(),"success",Toast.LENGTH_LONG).show();
                        pDialog.dismiss();
                        //fragmentTransaction.replace( R.id.nav_host_fragment_content_main,new NotificationHFragment(),null);
                       // fragmentTransaction.commit();

                        navController.navigate(R.id.notification_fragment);

                        //getAllNotifications();
                    },
                    error ->
                    {
                        pDialog.dismiss();
                        Toast.makeText(getContext(),"error",Toast.LENGTH_LONG).show();
                        Log.w("error in response", "Error: " + error.getMessage());
                    }
            ) {
                @ Override
                public Map<String, String> getParams() {
                    return mParams;
                };

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", access_token);
                    //params.put("Accept-Language", "fr");
                    return params;
                };

            };

            jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            Queue.add(jsonRequest);
        }




    }

    private void update_notification() throws ParseException {
        if(lineSelected == null || binding.Productspinner.getSelectedItem()==null){
            Toast.makeText(getContext(),"please select the line and the product",Toast.LENGTH_SHORT).show();
        }else{
            pDialog.show();

            String urlSaveN = url+"notification_heures/update/"+notifItem.getId();
            Notification_Hours notif = new Notification_Hours();
            notif.setDate(binding.Datebutton.getText().toString());
            notif.setIdLeader(leader.getId());
            String name = leader.getPrenom()+" "+leader.getNom();
            notif.setLeaderName(name);
            notif.setShift(myGroupe.getShift());
            notif.setNbr_operateurs(Integer. parseInt(nbr_operators.getText().toString()));
            notif.setTotal_h(totalHours);
            notif.setH_sup(Integer. parseInt(hExtra.getText().toString()));
            notif.setH_devolution(Integer. parseInt(hDevolution.getText().toString()));
            notif.setH_arrete(Integer. parseInt(hStopped.getText().toString()));
            notif.setH_nouvau_projet(Integer. parseInt(hNewProject.getText().toString()));
            notif.setH_normal(Integer. parseInt(hNormal.getText().toString()));
            notif.setLine(lineSelected);
            notif.setProduit((Produit) binding.Productspinner.getSelectedItem());
            notif.setOF(Integer.parseInt(binding.OF.getText().toString()));
            notif.setTotalOutput(Integer.parseInt(binding.TotalOutput.getText().toString()));
            notif.setTotalScrap(Integer.parseInt(binding.TotalScrap.getText().toString()));
            if(binding.Remark.getText().toString()!= null){
                notif.setRemark(binding.Remark.getText().toString());
            }

            String notifJsonString = JsonConvert.getGsonParser().toJson(notif);

            JSONParser parser = new JSONParser();
            org.json.simple.JSONObject json = (org.json.simple.JSONObject) parser.parse(notifJsonString);
            org.json.JSONObject notifJson = (org.json.JSONObject) new org.json.JSONObject(json);

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PUT,urlSaveN,notifJson,
                    response -> {
                        //Log.d("notif to update",json.toString());
                        Toast.makeText(getContext(),"Notification updated",Toast.LENGTH_LONG).show();
                        pDialog.dismiss();
                     //   fragmentTransaction.replace( R.id.nav_host_fragment_content_main,new NotificationHFragment(),null);
                       // fragmentTransaction.commit();
                        fragmentTransaction.addToBackStack(null);
                        navController.navigate(R.id.action_add_notification_to_notification_fragment2);

                    },
                    error ->{
                        pDialog.dismiss();
                        Toast.makeText(getContext(),"server error",Toast.LENGTH_LONG).show();
                        Log.w("error in response", "Error: " + error.getMessage());

                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", access_token);
                    //params.put("Accept-Language", "fr");
                    return params;
                }


            };
            jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            Queue.add(jsonRequest);

        }

    }





/*
    private void CreateSpinners() {
        ArrayAdapter<CharSequence> adapterShift = ArrayAdapter.createFromResource(getContext(),R.array.shift, android.R.layout.simple_spinner_item);
       adapterShift.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       shiftSpinner.setAdapter(adapterShift);

    }

 */


    String lineDesignation ;
    private void getLineSelected() {
        lineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lineDesignation  = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(),lineDesignation,Toast.LENGTH_SHORT).show();
                for(Line l:lineList){

                    if(l.getDesignation().equals(lineDesignation)){
                        lineSelected = l;
                        statisticViewModel.getStatistics(access_token, startDateStr, endDateStr, lineSelected.getId());

                        getProducts();
                        break;

                    }

                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }




    public void openDatePicker(View view){
        datePickerDialog.show();
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

    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month+1;
                String date = makeDateString(day,month,year);
                binding.Datebutton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(getContext(),style , dateSetListener, year,month,day);
    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }







}