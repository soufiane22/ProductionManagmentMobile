package ma.premo.productionmanagment.ui.group;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
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
import ma.premo.productionmanagment.databinding.FragmentGroupBinding;
import ma.premo.productionmanagment.models.Groupe;
import ma.premo.productionmanagment.models.Line;
import ma.premo.productionmanagment.models.NotificationHAdapter;
import ma.premo.productionmanagment.models.User;
import ma.premo.productionmanagment.ui.presenceFolder.AddPresenceFragment;
import ma.premo.productionmanagment.ui.presenceFolder.AddPresenceViewModel;
import ma.premo.productionmanagment.ui.presenceFolder.PresenceAdapter;
import ma.premo.productionmanagment.ui.presenceFolder.UserAdapter;

public class GroupFragment extends Fragment implements UserAdapter.Userselected  , EditlineInterface{

    private AddPresenceViewModel addPresenceViewModel;
    private  GroupeViewModel groupeViewModel;
    public FragmentGroupBinding binding;
    private User leader;
    private String access_token;
    private AlertDialog pDialog;
    private Groupe groupe;
    private List<User> listUsers;
    private List<User> listEngineer;
    private List<String> listGroupLines;
    private List<String> listAllLines;
    private PersonAdapter personAdapter;
    private LineGroupeAdapter lineGroupeAdapter;
    private  LineGroupeAdapter allLineadapter;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private AlertDialog dialogEditLine;
    private SearchView searchView;
    private UserAdapter userAdapter;
    private RequestQueue Queue;
    private LineClickListenner lineClickListenner;
    private RecyclerView allLineRecyclerView;
    private NavController navController;
    private  View editLineView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        access_token =  ((MainActivity)getActivity()).access_token;
        leader =  ((MainActivity)getActivity()).user;
        navController =  ((MainActivity)getActivity()).navController;
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...PLease wait");
        addPresenceViewModel = new ViewModelProvider(this,new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(AddPresenceViewModel.class);
        groupeViewModel = new ViewModelProvider(this,new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(GroupeViewModel.class);
        groupeViewModel.getAllLines(access_token);
        if(leader != null){
            addPresenceViewModel.getGroup(leader.getId(),access_token);
        }

        addPresenceViewModel.getUsersByFunction(access_token , "Ingenieur");
        addPresenceViewModel.getAllUsers(access_token);
        Queue = Volley.newRequestQueue(getContext());



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentGroupBinding.inflate(inflater, container, false);
        View view  = binding.getRoot();
        binding.personsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.lineRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        editLineView =  inflater.inflate(R.layout.popup_person, container, false);


        addPresenceViewModel.groupeMutableLiveData.observe(this, new Observer<Groupe>() {

            @Override
            public void onChanged(@Nullable Groupe groupeM) {
                if(groupeM == null){
                    Toast.makeText(getContext(), "No data available", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }else{
                    groupe = groupeM;
                    listUsers = (List<User>) groupe.getListOperateurs();

                    binding.groupe.setText(groupe.getDesignation());
                    binding.Leader.setText(leader.getNom()+" "+leader.getPrenom());
                    listUsers = new ArrayList<>(groupe.getListOperateurs());
                    listGroupLines = new ArrayList<>(groupe.getListLine());
                    lineGroupeAdapter = new LineGroupeAdapter(getContext(),listGroupLines,"group",lineClickListenner);
                    binding.lineRecyclerView.setAdapter(lineGroupeAdapter);
                    //listUsersUpdated = groupe.getListOperateurs();

                    Comparator<User> compareByFirstName = (User o1, User o2) -> o1.getPrenom().compareTo( o2.getPrenom() );
                    Collections.sort(listUsers , compareByFirstName);
                    listUsers.remove(leader);
                    for(int i = 0; i < listUsers.size(); i++){
                        if(leader.getId().equals(listUsers.get(i).getId()))
                            listUsers.remove(i);
                    }
                    System.out.println("list of person without leader: "+listUsers.toString());
                    personAdapter = new PersonAdapter(getContext(),listUsers,GroupFragment.this);
                    binding.personsRecyclerView.setAdapter(personAdapter);
                    setShiftSpinners();
                    try {
                        getListEngineer();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    pDialog.dismiss();

                }


            }
        });

        binding.AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewDialog(inflater , container);

            }
        });

        binding.AddButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createLineDialog(inflater , container);

            }
        });

        binding.Updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    updategroupe();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

        });

        setAllLineRecyclerView();

        refresh();


        return view;
    }

    private void setAllLineRecyclerView() {
        groupeViewModel.listLineMutableLiveData.observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> lines) {
                listAllLines = new ArrayList<>(lines);
                Collections.sort(listAllLines);

            }
        });
    }


/****** Edit line of Person *********/
    @Override
    public void editLine(String id, int position) {
        User user = listUsers.get(position);
        if(editLineView.getParent() != null) {
            ((ViewGroup)editLineView.getParent()).removeView(editLineView); // <- fix
        }

        TextView lastname = editLineView.findViewById(R.id.LastName);
        TextView firstname = editLineView.findViewById(R.id.FirstName);
        TextView function = editLineView.findViewById(R.id.Function);
        TextView registerNumber = editLineView.findViewById(R.id.RegisterNumber);
        Spinner lineSpinner = editLineView.findViewById(R.id.SpinnerLine);
        lastname.setText(user.getNom());
        firstname.setText(user.getPrenom());
        function.setText(user.getFonction());
        registerNumber.setText(String.valueOf(user.getMatricule()));

        ArrayAdapter<String> adapterLine = new ArrayAdapter<String> (getContext(), android.R.layout.simple_spinner_dropdown_item, listGroupLines);
        lineSpinner.setAdapter(adapterLine);

        AlertDialog.Builder Builder = new AlertDialog.Builder(getContext());
        Builder .setView(editLineView)
                .setTitle("Edit line of "+user.getNom())
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pDialog.show();
                      String lineSelected =(String) lineSpinner.getSelectedItem();
                      Toast.makeText(getContext(),lineSelected,Toast.LENGTH_SHORT).show();
                      user.setLine(lineSelected);
                        try {
                            groupeViewModel.updatePerson(user.getId(),user,access_token ,pDialog);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),"Error !",Toast.LENGTH_SHORT).show();
                            pDialog.dismiss();
                        }
                        personAdapter.notifyDataSetChanged();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        dialogEditLine = Builder.create();
        //   dialogEditLine.setContentView(R.layout.popup_person);
        if (dialogEditLine.getWindow() != null){
            dialogEditLine.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        dialogEditLine.show();
        dialogEditLine.getWindow().setLayout(600,580);

    }


    View linesView;
    private void createLineDialog(LayoutInflater inflater, ViewGroup container) {

        linesView =  inflater.inflate(R.layout.popup_line, container, false);
        dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setView(linesView);
        dialogBuilder.setTitle("Add Lines");
        dialog = dialogBuilder.create();
        if (dialog.getWindow() != null){
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        allLineRecyclerView = linesView.findViewById(R.id.AllLineRecyclerView);

        lineClickListenner = new LineClickListenner() {
            @Override
            public void onClick(String s) {
                allLineRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        allLineadapter.notifyDataSetChanged();
                    }
                });
                //Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                lineAdd= s;
            }
        };

        allLineRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        allLineadapter = new LineGroupeAdapter(getContext(),listAllLines,"All",lineClickListenner);
        allLineRecyclerView.setAdapter(allLineadapter);




        dialog.show();

        dialog.getWindow().setLayout(600,710);
        Button cancelButton = (Button) linesView.findViewById(R.id.CancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        addLine();
    }

    String lineAdd;
    private void addLine(){


        Button addLineButton = linesView.findViewById(R.id.AddLineButton);
        addLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listGroupLines.add(lineAdd);
                lineGroupeAdapter.setLineGroupeList(listGroupLines);
                lineGroupeAdapter.notifyDataSetChanged();
                dialog.cancel();
            }
        });
    }

    public void getListEngineer() throws InterruptedException {
        Thread.currentThread().setPriority(1);
         Thread.sleep(500);
      //  System.out.println("1----Engineers retrieved ");
        addPresenceViewModel.usersMutableLiveData.observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                listEngineer = users;
                setEngineerSpinners(groupe.getIngenieur().toString());

            }
        });
    }

    private void setShiftSpinners() {
        ArrayAdapter<CharSequence> adapterShift = ArrayAdapter.createFromResource(getContext(),R.array.shift, android.R.layout.simple_spinner_item);
        adapterShift.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.ShiftSpinner.setAdapter(adapterShift);
        binding.ShiftSpinner.setSelection( adapterShift.getPosition(groupe.getShift()));


    }

    private void setEngineerSpinners(String engineerName){
      //  System.out.println("2----Set engineer spinner");
       // System.out.println("listEngineer===>"+listEngineer.toString());
        ArrayAdapter adapterEngineer = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, listEngineer);

        binding.EngineerSpinner.setAdapter(adapterEngineer);

        for(int i = 0; i < binding.EngineerSpinner.getCount(); i++)
        {
            if (engineerName.equals(binding.EngineerSpinner.getItemAtPosition(i).toString()) )
            {
                binding.EngineerSpinner.setSelection(i); //(false is optional)
                break;
            }
        }
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

    private RecyclerView allUserRecyclerView;
    private List<User> allUsersList;
    public void setUserRecyclerView(){
        allUserRecyclerView  = popupView.findViewById(R.id.userRecyclerView);
        allUserRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        addPresenceViewModel.allUsersMutableLiveData.observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                allUsersList  = users;

                userAdapter = new UserAdapter(getContext(),allUsersList, GroupFragment.this);
                allUserRecyclerView.setAdapter(userAdapter);
            }
        });
    }
    public void addUsers() {
        Button addusersbutton =(Button) popupView.findViewById(R.id.AddUserButton);
        addusersbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("users selected"+listToAdd.toString());
                listUsers.addAll(listToAdd);
                personAdapter.setPersonList(listUsers);
                dialog.cancel();
                for (User user : listToAdd){
                    user.setSelected(false);
                }
                //setUserRecyclerView();

            }
        });
    }
    public void refresh(){
        binding.RefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  System.out.println("refresh table");
                listUsers = new ArrayList<>(groupe.getListOperateurs());
                listUsers.removeAll(listToAdd);
                personAdapter.setPersonList(listUsers);
            }
        });
    }

    private void updategroupe() throws ParseException {
        pDialog.show();
        String url = API.urlBackend+"groupe/update/"+groupe.getId();
        //System.out.println("list persons====>"+personAdapter.getPersonsList().toString());
        listUsers = personAdapter.getPersonsList();
        listUsers.add(leader);
        groupe.setListOperateurs(listUsers);
        groupe.setShift(binding.ShiftSpinner.getSelectedItem().toString());
        User engineer = (User) binding.EngineerSpinner.getSelectedItem();
        groupe.setIngenieur(engineer);
        groupe.setListLine(lineGroupeAdapter.getLineGroupeList());
       // System.out.println("groupe to update :"+groupe.toString());
        String groupStringJson = JsonConvert.getGsonParser().toJson(groupe);
        JSONParser parser = new JSONParser();
        org.json.simple.JSONObject json = (JSONObject) parser.parse(groupStringJson);
        org.json.JSONObject groupJson = (org.json.JSONObject) new org.json.JSONObject(json);

        JsonObjectRequest jsonObjectRequest  = new JsonObjectRequest(Request.Method.PUT, url, groupJson,
                new Response.Listener<org.json.JSONObject>() {
                    @Override
                    public void onResponse(org.json.JSONObject response) {
                        pDialog.dismiss();
                        Toast.makeText(getContext(), "save with success", Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.nav_home);
                    }

                }, new Response.ErrorListener() {
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
                //params.put("Accept-Language", "fr");
                return params;
            };
        };
        Queue.add(jsonObjectRequest);
    }

    public GroupFragment() {
        // Required empty public constructor
    }
    List<User> listToAdd = new ArrayList<>();
    @Override
    public void getUserCheked(List<User> list) {
        listToAdd = list;

    }


}