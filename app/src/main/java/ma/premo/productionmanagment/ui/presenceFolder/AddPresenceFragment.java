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
import android.support.v7.widget.AlertDialogLayout;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ma.premo.productionmanagment.MainActivity;
import ma.premo.productionmanagment.R;
import ma.premo.productionmanagment.Utils.API;
import ma.premo.productionmanagment.Utils.JsonConvert;
import ma.premo.productionmanagment.databinding.FragmentAddPresenceBinding;
import ma.premo.productionmanagment.models.Groupe;
import ma.premo.productionmanagment.models.Presence;
import ma.premo.productionmanagment.models.User;


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



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        addPresenceViewModel = new ViewModelProvider(this,new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(AddPresenceViewModel.class);
        binding = FragmentAddPresenceBinding.inflate(inflater, container, false);
        binding.setViewModel(addPresenceViewModel);
        binding.setLifecycleOwner(this);

        access_token =  ((MainActivity)getActivity()).access_token;
        leader =  ((MainActivity)getActivity()).user;
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...PLease wait");
        Queue = Volley.newRequestQueue(getContext());
        initDatePicker();
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        binding.Datebutton.setText(getTodayDates());
        binding.Datebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        pDialog.show();
        View view  = binding.getRoot();



        userRecyclerView= view.findViewById(R.id.userRecyclerView);
        String function = "Chef%20Equipe";
       addPresenceViewModel.getUsersByFunction(access_token , function);
       addPresenceViewModel.getAllUsers(access_token);

        userRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        addPresenceViewModel.groupeMutableLiveData.observe(this, new Observer<Groupe>() {
            @Override
            public void onChanged(@Nullable Groupe groupeM) {
             if(groupeM == null){
                 Toast.makeText(getContext(), "No data available", Toast.LENGTH_SHORT).show();
                 pDialog.dismiss();
             }else{
                 groupe = groupeM;
                 listUsers = (List<User>) groupe.getListOperateurs();
                 //   listLineSpinner = new ArrayList(groupe.getListLine());
                 listLineSpinner =  (ArrayList<String>) groupe.getListLine();

                // binding.Leader.setText(getLeader());
                 adapter = new PresenceAdapter(getContext(),listUsers,listLineSpinner);
                 userRecyclerView.setAdapter(adapter);
                 pDialog.dismiss();

             }


            }
        });
        addPresenceViewModel.usersMutableLiveData.observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                listLeaders = users;
                setLeaderSpinner();
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
                    savePresence();
                    PresenceFragment presenceFragment = new PresenceFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("mode","add");

                    presenceFragment.setArguments(bundle);
                    //fragmentTransaction.setReorderingAllowed(true);
                    fragmentTransaction.replace( R.id.nav_host_fragment_content_main,presenceFragment,null).addToBackStack(null);
                    fragmentTransaction.commit();

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

  public String getLeader(){
      String name = "";
        for(User u : listUsers){
            if(u.getFonction().equals("chef equipe")){
                 name = u.getNom()+" "+ u.getPrenom();
            }
        }
        return name;
    }

    public void setLeaderSpinner(){

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
        return day+"-"+month+"-"+year;
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
        String url = API.urlBackend+"presence/saveAll";
       pDialog.show();

       org.json.simple.JSONArray listJson = new JSONArray();
       org.json.JSONArray listJSON = new org.json.JSONArray();
       for (int i = 0; i < listUsers.size(); i++) {
           Presence presence = new Presence();
           User user = listUsers.get(i);
           View view = userRecyclerView.getChildAt(i);
           EditText nbrHours = view.findViewById(R.id.NbrHours);
           String hours = nbrHours.getText().toString();
           if (hours.equals("")) {
             hours ="0";
           }
           presence.setNbrHeurs(Integer.parseInt(hours));
           Spinner stateSpinner = view.findViewById(R.id.stateSpinner);
           String state = stateSpinner.getSelectedItem().toString();
           presence.setEtat(state);
           presence.setOperateur(user);
           presence.setDate(binding.Datebutton.getText().toString());
           presence.setChefEquipe(idLeader);
           presence.setGroupe(groupe.getDesignation());
           presence.setIngenieur(groupe.getIngenieur().getNom().toString());
           Spinner lineSpiner = view.findViewById(R.id.lineSpinner);
           presence.setLine(lineSpiner.getSelectedItem().toString());
           Spinner shiftSpinner = view.findViewById(R.id.shifSpinner);
           presence.setShift(shiftSpinner.getSelectedItem().toString());
           String presenceStringJson = JsonConvert.getGsonParser().toJson(presence);

           try {
               JSONParser parser = new JSONParser();
               org.json.simple.JSONObject json = (JSONObject) parser.parse(presenceStringJson);
               //System.out.println("json====>"+json.toString());
               //listJson.add(json);
               org.json.JSONObject json1 = (org.json.JSONObject) new org.json.JSONObject(json);
               listJSON.put(json1);


           }catch (Exception e){
               System.out.println(e);
           }
       }
      // Log.d("listJSON",listJSON.toString());
       JsonArrayRequest  jsonArrayRequest  = new JsonArrayRequest(Request.Method.POST, url, listJSON,
               response ->  {
               pDialog.dismiss();
               Toast.makeText(getContext(), "save with success", Toast.LENGTH_SHORT).show();



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



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }


}