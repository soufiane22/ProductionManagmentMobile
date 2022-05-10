package ma.premo.productionmanagment.ui.notification_hours;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.premo.productionmanagment.MainActivity;
import ma.premo.productionmanagment.R;
import ma.premo.productionmanagment.Utils.API;
import ma.premo.productionmanagment.Utils.JsonConvert;
import ma.premo.productionmanagment.models.Line;
import ma.premo.productionmanagment.models.NotificationHAdapter;
import ma.premo.productionmanagment.models.Notification_Hours;
import ma.premo.productionmanagment.models.Of;
import ma.premo.productionmanagment.models.Produit;
import ma.premo.productionmanagment.models.User;


public class add_notification extends Fragment {
    private DatePickerDialog datePickerDialog;
    private Button DateButton;
    private Button saveButton ;
    private NotificationHViewModel notificationViewModel;
    private Spinner productSpinner ;
    private Spinner lineSpinner;
    private Spinner shiftSpinner;
    private Spinner ofSpinner;
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
    private NotificationHAdapter notificationHAdapter;
    ProgressDialog pDialog;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private Map<String, String> mParams;
    private final String url = API.urlBackend;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.add_notification1, container, false);
         access_token =  ((MainActivity)getActivity()).access_token;
         leader =  ((MainActivity)getActivity()).user;
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
      //  binding = FragmentNotificationHoursBinding.inflate(inflater, container, false);
        DateButton = (Button) view.findViewById(R.id.Datebutton);
        saveButton = (Button) view.findViewById(R.id.button2) ;
        lineSpinner = (Spinner) view.findViewById(R.id.Linespinner) ;
        shiftSpinner  = (Spinner) view.findViewById(R.id.Shiftspinner) ;
        productSpinner =(Spinner) view.findViewById(R.id.Productspinner);
        ofSpinner = (Spinner) view.findViewById(R.id.OFSpinner) ;
        nbr_operators = view.findViewById(R.id.nbrOperators);
        hTotal = view.findViewById(R.id.hTotal);
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
        hTotal.setText("0");
        hExtra.setText("0");
        hDevolution.setText("0");
        hStopped.setText("0");
        hNewProject.setText("0");
        hNormal.setText("0");
        Queue = Volley.newRequestQueue(getContext());
        notificationsList = new ArrayList<>();
        mParams  = new HashMap<>();
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...PLease wait");
        DateButton.setText(getTodayDates());
        CreateSpinners();
        getShift();
        getOfSelected();
        initDatePicker();
        getLines();
        getLineSelected();
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
        DateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // System.out.println("tester date");
                datePickerDialog.show();
            }
        });


        Bundle args = getArguments();
        mode  = args.getString("mode");
        //Log.e("mode====>",mode);

        if(mode.equals("update")){
            String notifJsonString = args.getString("objet");
            notifItem = JsonConvert.getGsonParser().fromJson(notifJsonString, Notification_Hours.class);
            setForms(notifItem);
            Log.e("2--notif from adapter",notifItem.toString());
        }

        //Notification_Hours notif= Utils.getGsonParser().fromJson(personJsonString, Person.class);

      //  hNormal.setEnabled(false);








        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nbr_operators.getText().toString().isEmpty() || hTotal.getText().toString().isEmpty() || hExtra.getText().toString().isEmpty()
                || hDevolution.getText().toString().isEmpty() || hStopped.getText().toString().isEmpty() || hNewProject.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Please fill in the empty fields !", Toast.LENGTH_LONG).show();
                }else{

                       if(Integer.parseInt(nbr_operators.getText().toString()) ==0 ||Integer.parseInt(hTotal.getText().toString()) ==0 ){
                           Toast.makeText(getContext(), "The number of operators and total hours must be greater than 0 !", Toast.LENGTH_LONG).show();
                       }else{
                           int totalHours = Integer.parseInt(hTotal.getText().toString());
                           int extraHours= Integer.parseInt(hExtra.getText().toString());
                           int devolutionHours= Integer.parseInt(hDevolution.getText().toString());
                           int StoppedHours= Integer.parseInt(hStopped.getText().toString());
                           int newProjectHours= Integer.parseInt(hNewProject.getText().toString());
                           int normalHours =  Integer.parseInt(hNormal.getText().toString());
                           if(totalHours != extraHours+devolutionHours+StoppedHours+newProjectHours+normalHours){
                               Toast.makeText(getContext(), "The total hours is wrong !", Toast.LENGTH_LONG).show();
                           }else{
                               if(mode == "add"){
                                   save_notification();
                               }
                               if(mode == "update"){
                                   update_notification();
                               }

                           }

                       }
                }
                // nameEdt.getText().toString().isEmpty()



                /*
                Date date1 = new Date();
                try {
                     date1= new SimpleDateFormat("dd-MM-yyyy"). parse(DateButton.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                 */

            }

        });

        return view;
    }

    private void setForms(Notification_Hours notification_hours) {
        DateButton.setText(notification_hours.getDate());
        shiftSpinner.setSelection(getIndex(shiftSpinner, notification_hours.getShift()));
       // lineSpinner.setSelection(getIndex(lineSpinner, notification_hours.getLigne().getDesignation()));
        productSpinner.setTag(notification_hours.getProduit().getReference());
        ofSpinner.setTag(notification_hours.getOF().getReference());
        nbr_operators.setText(String.valueOf(notification_hours.getNbr_operateurs()));
        hTotal.setText(String.valueOf(notification_hours.getTotal_h()));
        hNormal.setText(String.valueOf(notification_hours.getH_normal()));
        hExtra.setText(String.valueOf(notification_hours.getH_sup()));
        hStopped.setText(String.valueOf(notification_hours.getH_arrete()));
        hDevolution.setText(String.valueOf(notification_hours.getH_devolution()));
        hNewProject.setText(String.valueOf(notification_hours.getH_nouvau_projet()));
        remark.setText(String.valueOf(notification_hours.getRemark()));
    }

    //private method of your class
    private int getIndex(Spinner spinner, String myString){


            for (int i=0;i<spinner.getCount();i++){
                System.out.println("item "+i+":"+spinner.getItemAtPosition(i).toString());
                if (spinner.getItemAtPosition(i).toString().equals(myString)){
                    return i;
                }
            }



        return 0;
    }

    private void getProducts() {
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
                    for (Produit p : productList) {
                        productListSpinner.add(productList.indexOf(p), p.getReference());
                    }
                    setSpinner( productListSpinner, productSpinner );
                    getProductSelected();
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
                    for (Line l : lineList) {
                        lineListSpinner.add(lineList.indexOf(l), l.getDesignation());
                    }
                    setSpinner( lineListSpinner, lineSpinner );
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

                    setSpinner( OfListSpinner, ofSpinner );
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


/*
    @Override
    public void onBackPressed() {}

*/



    private void setSpinner(ArrayList list, Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext() , android.R.layout.simple_spinner_dropdown_item, list);
        spinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if(spinner == lineSpinner && mode =="update" ){
            int spinnerPosition = adapter.getPosition(notifItem.getLigne().getDesignation());
            spinner.setSelection(spinnerPosition);
        }
        if(spinner == productSpinner && mode =="update" ){
            int spinnerPosition = adapter.getPosition(notifItem.getProduit().getReference());
            spinner.setSelection(spinnerPosition);
        }
        if(spinner == ofSpinner && mode =="update" ){
            int spinnerPosition = adapter.getPosition(notifItem.getOF().getReference());
            spinner.setSelection(spinnerPosition);
        }


    }

    public void save_notification(){
        pDialog.show();
        String urlSaveN = url+"notification_heures/save/"+lineSelected.getId()+"/"+produiSelected.getId()+"/"+ofSelected.getId();
        Notification_Hours notif = new Notification_Hours();
        notif.setDate(DateButton.getText().toString());
        notif.setShift(getShift());
        notif.setIdLeader(leader.getId());
       // notif.setLigne(getElementSelected());
        notif.setOF(ofSelected);
        notif.setNbr_operateurs(Integer. parseInt(nbr_operators.getText().toString()));
        notif.setTotal_h(Integer. parseInt(hTotal.getText().toString()));
        notif.setH_sup(Integer. parseInt(hExtra.getText().toString()));
        notif.setH_devolution(Integer. parseInt(hDevolution.getText().toString()));
        notif.setH_arrete(Integer. parseInt(hStopped.getText().toString()));
        notif.setH_nouvau_projet(Integer. parseInt(hNewProject.getText().toString()));
        notif.setH_normal(Integer. parseInt(hNormal.getText().toString()));
       // Log.e("Notification", "Notification for add====> " + notif.toString());
        Map<String, String> notification = new HashMap<>();
       //notification.put("of",String.valueOf(notif.getOF()));
        notification.put("date",String.valueOf(notif.getDate()));
        notification.put("shift",notif.getShift());
        notification.put("idLeader",notif.getIdLeader());
        //notification.put("ligne",notif.getLigne());
        notification.put("nbr_operateurs",String.valueOf(notif.getNbr_operateurs()));
        notification.put("total_h",String.valueOf(notif.getTotal_h()));
        notification.put("h_sup",String.valueOf(notif.getH_sup()));
        notification.put("h_devolution",String.valueOf(notif.getH_devolution()));
        notification.put("h_arrete",String.valueOf(notif.getH_arrete()));
        notification.put("h_nouvau_projet",String.valueOf(notif.getH_nouvau_projet()));
        notification.put("h_normal",String.valueOf(notif.getH_normal()));
        notification.put("remark",remark.getText().toString());
        JSONObject json = new JSONObject(notification);
        mParams.put("idOf",ofSelected.getId());
        //mParams.put("idOf",ofSelected.getId());

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,urlSaveN,json,
                response -> {
                    Log.e("notif to add",json.toString());
                    Toast.makeText(getContext(),"success",Toast.LENGTH_LONG).show();
                    pDialog.dismiss();
                    fragmentTransaction.replace( R.id.nav_host_fragment_content_main,new NotificationHFragment(),null);
                    fragmentTransaction.commit();

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
                       System.out.println("idOf=======>"+ofSelected.getId());
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

        Queue.add(jsonRequest);



    }

    private void update_notification() {
        pDialog.show();
        System.out.println("*********update notification***********");
        String urlSaveN = url+"notification_heures/update/"+notifItem.getId()+"/"+lineSelected.getId()+"/"+produiSelected.getId()+"/"+ofSelected.getId();
        Notification_Hours notif = new Notification_Hours();
        notif.setDate(DateButton.getText().toString());
        notif.setIdLeader(leader.getId());
        notif.setShift(getShift());
        notif.setNbr_operateurs(Integer. parseInt(nbr_operators.getText().toString()));
        notif.setTotal_h(Integer. parseInt(hTotal.getText().toString()));
        notif.setH_sup(Integer. parseInt(hExtra.getText().toString()));
        notif.setH_devolution(Integer. parseInt(hDevolution.getText().toString()));
        notif.setH_arrete(Integer. parseInt(hStopped.getText().toString()));
        notif.setH_nouvau_projet(Integer. parseInt(hNewProject.getText().toString()));
        notif.setH_normal(Integer. parseInt(hNormal.getText().toString()));
        Map<String, String> notification = new HashMap<>();
        notification.put("date",String.valueOf(notif.getDate()));
        notification.put("shift",notif.getShift());
        //notification.put("ligne",notif.getLigne());
        notification.put("nbr_operateurs",String.valueOf(notif.getNbr_operateurs()));
        notification.put("total_h",String.valueOf(notif.getTotal_h()));
        notification.put("h_sup",String.valueOf(notif.getH_sup()));
        notification.put("h_devolution",String.valueOf(notif.getH_devolution()));
        notification.put("h_arrete",String.valueOf(notif.getH_arrete()));
        notification.put("h_nouvau_projet",String.valueOf(notif.getH_nouvau_projet()));
        notification.put("h_normal",String.valueOf(notif.getH_normal()));
        notification.put("remark",remark.getText().toString());
        JSONObject json = new JSONObject(notification);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PUT,urlSaveN,json,
                response -> {
                    Log.e("notif to update",json.toString());
                    Toast.makeText(getContext(),"success",Toast.LENGTH_LONG).show();
                    pDialog.dismiss();
                    fragmentTransaction.replace( R.id.nav_host_fragment_content_main,new NotificationHFragment(),null);
                    fragmentTransaction.commit();

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

        Queue.add(jsonRequest);
    }




    public add_notification() {
        // Required empty public constructor
    }




    private void CreateSpinners() {
        ArrayAdapter<CharSequence> adapterShift = ArrayAdapter.createFromResource(getContext(),R.array.shift, android.R.layout.simple_spinner_item);
       adapterShift.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       shiftSpinner.setAdapter(adapterShift);

    }

    String shift;
    private String getShift() {
        shiftSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shift  = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(),shift,Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return shift;
    }

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
                        getProducts();
                    }

                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    String productRef;
    private void getProductSelected() {
        productSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                productRef  = parent.getItemAtPosition(position).toString();
                for(Produit p:productList){

                    if(p.getReference().equals(productRef)){
                        produiSelected = p;
                        getOFs();
                       // System.out.println("id produit selected  ===> "+produiSelected.getId().toString());
                    }

                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    String ofRefe ;
    private  void getOfSelected() {
        ofSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ofRefe  = parent.getItemAtPosition(position).toString();
                for(Of c:OfList){

                    if(c.getReference().equals(ofRefe)){
                        ofSelected = c;
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
        System.out.println("date=====>"+day+month+year);
        return makeDateString(day,month ,year);
    };

    private String makeDateString(int day, int month, int year) {
        return day+"-"+month+"-"+year;
    };

    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month+1;
                String date = makeDateString(day,month,year);
                DateButton.setText(date);
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