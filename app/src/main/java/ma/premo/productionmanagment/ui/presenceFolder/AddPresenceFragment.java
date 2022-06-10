package ma.premo.productionmanagment.ui.presenceFolder;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.navigation.NavController;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.premo.productionmanagment.MainActivity;
import ma.premo.productionmanagment.R;
import ma.premo.productionmanagment.Utils.API;
import ma.premo.productionmanagment.Utils.JsonConvert;
import ma.premo.productionmanagment.databinding.FragmentAddPresenceBinding;
import ma.premo.productionmanagment.models.Groupe;
import ma.premo.productionmanagment.models.Notification_Hours;
import ma.premo.productionmanagment.models.Presence;
import ma.premo.productionmanagment.models.PresenceGroup;
import ma.premo.productionmanagment.models.User;
import ma.premo.productionmanagment.ui.controllers.ApplicationControler;


public class AddPresenceFragment extends Fragment implements UserAdapter.Userselected {

private FragmentAddPresenceBinding binding;
private AddPresenceViewModel addPresenceViewModel;
private Presence presence;
private RecyclerView userRecyclerView ;
private ProgressDialog pDialog;
private List<User> listUsers = new ArrayList<>();
private PresenceAdapter adapter;
private UserAdapter userAdapter;
private DatePickerDialog datePickerDialog;
private Groupe groupe = new Groupe();
private RequestQueue Queue;
private AlertDialog.Builder dialogBuilder;
private AlertDialog dialog;
private SearchView searchView;
private List<User> allUsersList = new ArrayList<>();
private List<User> listLeaders = new ArrayList<>();
private  RecyclerView allUserRecyclerView;
private ArrayList<String> listLineSpinner = new ArrayList<>();
private String idLeader ;
private FragmentManager fragmentManager;
private FragmentTransaction fragmentTransaction;
private  String access_token ;
public User leader;
private PresenceGroup presenceGroup;
private NavController navController;
private String mode;
private PresenceGroup presenceGroupToupadte ;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        addPresenceViewModel = new ViewModelProvider(this,new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(AddPresenceViewModel.class);
        binding = FragmentAddPresenceBinding.inflate(inflater, container, false);
        binding.setViewModel(addPresenceViewModel);
        binding.setLifecycleOwner(this);
        access_token =  ((MainActivity)getActivity()).access_token;
        leader =  ((MainActivity)getActivity()).user;
        if(leader != null){
           idLeader = leader.getId();
        }
        navController =  ((MainActivity)getActivity()).navController;

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...PLease wait");
        Queue = Volley.newRequestQueue(getContext());
        initDatePicker();
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        binding.Datebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        pDialog.show();
        View view  = binding.getRoot();
        presenceGroup = new PresenceGroup();
        presenceGroupToupadte = new PresenceGroup();

        userRecyclerView= view.findViewById(R.id.userRecyclerView);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        String function = "Chef%20Equipe";
        addPresenceViewModel.getUsersByFunction(access_token , function);
        addPresenceViewModel.getAllUsers(access_token);

        /******** mode update ********/


        Bundle args = getArguments();

        mode = args.getString("mode");
        System.out.println("mode =====>"+mode);
        if(mode.equals("update")){
            binding.LinearAddUsers.setVisibility(View.GONE);
            presenceGroupToupadte = new PresenceGroup();
            String presenceString = "";
            presenceString = args.getString("presenceJsonString");
            presenceGroupToupadte = JsonConvert.getGsonParser().fromJson(presenceString, PresenceGroup.class);
            binding.Datebutton.setText(presenceGroupToupadte.getDate());
            if(!leader.getId().equals(presenceGroupToupadte.getLeaderId())){
                mode="view";
            }


        }else{
            binding.Datebutton.setText(getTodayDates());

        }

        /*********************************/

        addPresenceViewModel.usersMutableLiveData.observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                listLeaders = users;
                if(listLeaders != null && leader != null){
                    if(mode.equals("add")){
                        setLeaderSpinner(leader);
                    }else{
                        setLeaderSpinner(getLeader(presenceGroupToupadte.getLeaderId()));
                    }
                }else {
                    Toast.makeText(getContext(),"Server error",Toast.LENGTH_SHORT);
                }


            }
        });



        addPresenceViewModel.groupeMutableLiveData.observe(this, new Observer<Groupe>() {
            @Override
            public void onChanged(@Nullable Groupe groupeM) {
             if(groupeM == null || leader == null){
                 Toast.makeText(getContext(), "No data available", Toast.LENGTH_SHORT).show();
                 pDialog.dismiss();
             }else{
                 groupe = groupeM;
                 listUsers = (List<User>) groupe.getListOperateurs();
                 Comparator<User> compareByFirstName = (User o1, User o2) -> o1.getPrenom().compareTo( o2.getPrenom() );
                 Collections.sort(listUsers);
                // List<String> sortedList = slist.stream().sorted().collect(Collectors.toList());
                 Collections.sort(presenceGroupToupadte.getListPresence());
                 adapter = new PresenceAdapter(getContext(),listUsers,presenceGroupToupadte.getListPresence(),mode);
                 userRecyclerView.setAdapter(adapter);
                 pDialog.dismiss();

             }


            }
        });


        binding.LeaderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                User user = (User) parent.getSelectedItem();
               // System.out.println("user selected "+user.getId());
                idLeader = user.getId();
                addPresenceViewModel.getGroup(user.getId(), access_token);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



      //  userRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(mode.equals("add")){
                        savePresence();
                    }else
                        if(mode.equals("update")){
                        updatePresence();
                       }



                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

        binding.AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewDialog(inflater , container);

            }
        });


        refresh();



        return view;
    }






    private void updatePresence() {
        String url = API.urlBackend+"presencegroup/update/"+presenceGroupToupadte.getId();
        pDialog.show();
        presenceGroup.setDate(binding.Datebutton.getText().toString());
        int totalHours = 0;
        int totalOpartors = 0;
        for (int i = 0; i < presenceGroupToupadte.getListPresence().size(); i++) {
            Presence presence = new Presence();
            presence= presenceGroupToupadte.getListPresence().get(i);
            View view = userRecyclerView.getChildAt(i);
            EditText nbrHours = view.findViewById(R.id.NbrHours);
            String hours = nbrHours.getText().toString();
            if (hours.equals("")) {
                hours ="0";
            }
            presence.setNbrHeurs(Integer.parseInt(hours));
            if(presence.getFunctionPerson().equals("Operateur"))
                totalHours += Integer.parseInt(hours);

            Spinner stateSpinner = view.findViewById(R.id.stateSpinner);
            String state = stateSpinner.getSelectedItem().toString();
            if(presence.getFunctionPerson().equals("Operateur") && (state.equals("Present") || state.equals("Retard")))
                totalOpartors++;

            presence.setEtat(state);
            presence.setCreatedAt(null);

           // System.out.println("presence item "+presence.toString());
        }
        presenceGroupToupadte.setTotalOperators(totalOpartors);
        presenceGroupToupadte.setSumHours(totalHours);
        presenceGroupToupadte.setDate(binding.Datebutton.getText().toString());
        presenceGroupToupadte.setCreatedAt(null);

        System.out.println("****** group presense to update "+presenceGroupToupadte.toString());
        String presenceStringJson = JsonConvert.getGsonParser().toJson(presenceGroupToupadte);
       // System.out.println("presenceStringJson"+presenceStringJson);
        org.json.JSONObject presencejson = new org.json.JSONObject();
        try {
            JSONParser parser = new JSONParser();
            org.json.simple.JSONObject json = (JSONObject) parser.parse(presenceStringJson);
            presencejson = (org.json.JSONObject) new org.json.JSONObject(json);
        }catch (Exception e){
            System.out.println(e);
        }
      //  Log.d("presencejson",presencejson.toString());
        JsonObjectRequest jsonArrayRequest  = new JsonObjectRequest(Request.Method.PUT, url, presencejson,
                new Response.Listener<org.json.JSONObject>() {
                    @Override
                    public void onResponse(org.json.JSONObject response) {
                        pDialog.dismiss();
                        Toast.makeText(getContext(),"Presence updated with success",Toast.LENGTH_LONG).show();
                       // Log.e("response",response.toString());

                        fragmentTransaction.setReorderingAllowed(false);
                        fragmentTransaction.replace( R.id.nav_host_fragment_content_main,new PresenceFragment(),null).addToBackStack(null);
                        //fragmentManager.popBackStackImmediate();
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                },
                new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                    Toast.makeText(getContext(),"error",Toast.LENGTH_LONG).show();
                    Log.e("error",error.toString());
                 }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", access_token);
                return params;
            };
        };

        Queue.add(jsonArrayRequest);
    }

  public User getLeader(String id){
      User leader = new User();
        for(User u : listLeaders){
            if(u.getId().equals(id)){
                leader = u;
            }
        }
        return leader;
    }

    public void setLeaderSpinner(User leader){

        ArrayAdapter adapterLeaders = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, listLeaders);
        binding.LeaderSpinner.setAdapter(adapterLeaders);

        for(int i = 0; i < binding.LeaderSpinner.getCount(); i++)
        {

            if (leader.toString().equals(binding.LeaderSpinner.getItemAtPosition(i).toString()) )
            {
                System.out.println("leader found");
                binding.LeaderSpinner.setSelection(i); //(false is optional)
                break;
            }
        }


    }

    private String getTodayDates() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
      //  System.out.println("date=====>"+day+month+year);
        return makeDateString(day,month ,year);
    }
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

   private void savePresence() throws ParseException {
        String url = API.urlBackend+"presencegroup/save";
       pDialog.show();
       presenceGroup.setGroup(groupe.getDesignation());
       presenceGroup.setLeaderId(idLeader);
       User leaderSelected = (User) binding.LeaderSpinner.getSelectedItem();
       presenceGroup.setLeaderName(leaderSelected.getNom()+" "+leaderSelected.getPrenom());
       presenceGroup.setShift(groupe.getShift());
       presenceGroup.setEngineer(groupe.getIngenieur().getNom());
       presenceGroup.setDate(binding.Datebutton.getText().toString());

       int totalHours = 0;
       int totalOpartors = 0;
       for (int i = 0; i < listUsers.size(); i++) {
           Presence presence = new Presence();
           User user = listUsers.get(i);
           View view = userRecyclerView.getChildAt(i);
           EditText nbrHours = view.findViewById(R.id.NbrHours);
           String hours = nbrHours.getText().toString();

           if (hours.equals("")) {
             hours ="0";
           }
           Spinner stateSpinner = view.findViewById(R.id.stateSpinner);
           String state = stateSpinner.getSelectedItem().toString();

           if(user.getFonction().equals("Operateur"))
              totalHours += Integer.parseInt(hours);

           System.out.println("stateSpinner "+stateSpinner);

           if(user.getFonction().equals("Operateur") && (state.equals("Present") || state.equals("Retard")))
               totalOpartors++;

           presence.setNbrHeurs(Integer.parseInt(hours));

           presence.setEtat(state);
           presence.setNomPerson(user.getNom());
           presence.setPrenomPerson(user.getPrenom());
           presence.setIdPerson(user.getId());
           presence.setMatriculePerson(user.getMatricule());
           presence.setFunctionPerson(user.getFonction());
           presence.setLine(user.getLine());
           presenceGroup.addPresence(presence);


       }
       presenceGroup.setSumHours(totalHours);
       presenceGroup.setTotalOperators(totalOpartors);
       String presenceStringJson = JsonConvert.getGsonParser().toJson(presenceGroup);
       org.json.JSONObject presencejson = new org.json.JSONObject();
       try {
           JSONParser parser = new JSONParser();
           org.json.simple.JSONObject json = (JSONObject) parser.parse(presenceStringJson);
           //System.out.println("json====>"+json.toString());
           //listJson.add(json);
            presencejson = (org.json.JSONObject) new org.json.JSONObject(json);


       }catch (Exception e){
           System.out.println(e);
       }

        Log.d("presencejson",presencejson.toString());
       JsonObjectRequest jsonArrayRequest  = new JsonObjectRequest(Request.Method.POST, url, presencejson,
               response ->  {
               pDialog.dismiss();
            //   presenceFragment.presenceViewModel.getPresenses(leader.getId(),access_token);

               Toast.makeText(getContext(), "save with success", Toast.LENGTH_SHORT).show();
               System.out.println("1--- presence saved ");
                   //presenceControler.getPresenses(leader.getId(),access_token);
                   PresenceFragment presenceFragment = new PresenceFragment();
                   Bundle bundle = new Bundle();
                   bundle.putString("mode","add");
                   presenceFragment.setArguments(bundle);
                   //navController.navigate(R.id.presence_fragment);

                   fragmentManager.popBackStackImmediate();
                   fragmentTransaction.setReorderingAllowed(false);
                   fragmentTransaction.replace( R.id.nav_host_fragment_content_main,presenceFragment,null).addToBackStack(null);
                   fragmentTransaction.commit();

       }, error ->{

               pDialog.dismiss();
               Toast.makeText(getContext(),"error",Toast.LENGTH_LONG).show();
               Log.e("error",error.toString());

       }){
           @Override
           public Map<String, String> getHeaders() throws AuthFailureError {
               Map<String, String> params = new HashMap<String, String>();
               params.put("Authorization", access_token);
               //params.put("Accept-Language", "fr");
               return params;
           };
       };

       Queue.add(jsonArrayRequest);

   }

    View popupView;
    public void createNewDialog(LayoutInflater inflater , ViewGroup container){
    popupView =  inflater.inflate(R.layout.popup_operators, container, false);
        dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setView(popupView);
        dialogBuilder.setTitle("Add Persons");
        dialog = dialogBuilder.create();
       searchView = popupView.findViewById(R.id.searchView);
       if (dialog.getWindow() != null){
           dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
       }

        setUserRecyclerView();

       if(searchView != null){
           searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
               @Override
               public boolean onQueryTextSubmit(String s) {
                   return false;
               }

               @Override
               public boolean onQueryTextChange(String s) {
                    userAdapter.getFilter().filter(s);
                      //userAdapter.getfilter(s);
                      // search(s);
                       return true;



               }
           });
       }
       Button cancelButton = (Button) popupView.findViewById(R.id.CancelButton);
       cancelButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dialog.cancel();
           }
       });

       dialog.show();
       dialog.getWindow().setLayout(700,900);

       addUsers();

   }


    public void addUsers() {
     Button addusersbutton =(Button) popupView.findViewById(R.id.AddUserButton);
     addusersbutton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            // System.out.println("users selected"+listToAdd.toString());
             listUsers.addAll(listToAdd);
             adapter.setUserList(listUsers);

             dialog.cancel();
             for (User user : listToAdd){
                 user.setSelected(false);
             }
             //setUserRecyclerView();

         }
     });
    }

    public void setUserRecyclerView(){
        allUserRecyclerView  = popupView.findViewById(R.id.userRecyclerView);
        allUserRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        addPresenceViewModel.allUsersMutableLiveData.observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                allUsersList  = users;

                userAdapter = new UserAdapter(getContext(),allUsersList,AddPresenceFragment.this);
                allUserRecyclerView.setAdapter(userAdapter);
            }
        });
    }

    List<User> listToAdd = new ArrayList<>();
    @Override
    public void getUserCheked(List<User> list) {
       // System.out.println("users selected"+list.toString());

        listToAdd = list;
    }

    public void refresh(){
        binding.RefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listUsers.removeAll(listToAdd);
                adapter.setUserList(listUsers);
            }
        });
    }




    public AddPresenceFragment() {

    }






}