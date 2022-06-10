package ma.premo.productionmanagment.ui.presenceFolder;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProvider;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ma.premo.productionmanagment.MainActivity;
import ma.premo.productionmanagment.R;
import ma.premo.productionmanagment.databinding.FragmentPresenseBinding;
import ma.premo.productionmanagment.models.DeclarationPresence;
import ma.premo.productionmanagment.models.PresenceGroup;
import ma.premo.productionmanagment.models.User;
import ma.premo.productionmanagment.ui.controllers.ApplicationControler;

public class PresenceFragment extends Fragment implements DeleteitemLestener {

    public PresenceViewModel presenceViewModel;
    private FragmentPresenseBinding binding;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private String mode = "";
    private DeclarationPresence declarationPresence;
    private List<PresenceGroup> PresenceGroupList = new ArrayList<>();
    private ProgressDialog pDialog;
    private String access_token;
    private User leader;
    private String nameLeader ;
    private  PresenceGroupAdapter adapter;
    private ApplicationControler presenceControler;
    private NavController navController;
    private List<User> listLeaders;
    private DatePickerDialog datePickerDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // presenceControler = new PresenceControler();
        leader = new User();
        access_token =  ((MainActivity)getActivity()).access_token;
        leader =  ((MainActivity)getActivity()).user;

        navController =  ((MainActivity)getActivity()).navController;
        // presenceViewModel.getPresenses(leader.getId() , access_token);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenceViewModel = new ViewModelProvider(this,new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(PresenceViewModel.class);
       // View view = inflater.inflate(R.layout.fragment_presense, container, false);
        nameLeader= "";
        if(leader != null){
            nameLeader = leader.getNom()+" "+leader.getPrenom();
            presenceViewModel.getPresenses(leader.getId(),access_token);
        }

        binding = FragmentPresenseBinding.inflate(inflater, container, false);
        binding.setViewModel(presenceViewModel);
        binding.setLifecycleOwner(this);
        View view  = binding.getRoot();
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...PLease wait");
        pDialog.show();
        binding.PresenceGroupRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if(leader != null && PresenceGroupList != null){
            adapter = new PresenceGroupAdapter(getContext(),PresenceGroupList , leader.getId(),PresenceFragment.this );
            binding.PresenceGroupRecyclerView.setAdapter(adapter);
        }


        String function = "Chef%20Equipe";
        presenceViewModel.getUsersByFunction(access_token ,function);

        /***** configuration of Dtae Button  *****/
        initDatePicker();
        binding.DateButton.setText(getTodayDates());
        binding.DateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        binding.AddButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("mode","add");
                /*
                fragmentTransaction.setReorderingAllowed(true);
                fragmentTransaction.replace( R.id.nav_host_fragment_content_main,addPresenceFragment,null).addToBackStack(null);
                fragmentTransaction.commit();

                 */
               // getActivity().onBackPressed();
                navController.navigate(R.id.add_presence,bundle);


            }
        });

        /***** Search Button  *****/
        binding.SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog.show();
                User leaderSelected = (User) binding.SpinnerLeader.getSelectedItem();
                String idLeader = leaderSelected.getId();
                String date = binding.DateButton.getText().toString();
                presenceViewModel.searchPresence(idLeader,date,access_token);
                //System.out.println("search "+idLeader+" "+date);

            }
        });

        /***** refresh Button  *****/
        binding.Refreshbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog.show();
                presenceViewModel.getPresenses(leader.getId(),access_token);
            }
        });


        presenceViewModel.declarationGroupPresenceMutableLiveData.observe(this, new Observer<List<PresenceGroup>>() {
            @Override
            public void onChanged(@Nullable List<PresenceGroup> presenceGroups) {
                pDialog.show();
                if(presenceGroups == null || presenceGroups.size()==0) {
                    // Toast.makeText(getContext(), "no error", Toast.LENGTH_SHORT).show();
                    binding.PresenceGroupRecyclerView.setVisibility(View.GONE);
                    binding.Nodata.setVisibility(View.VISIBLE);
                    pDialog.dismiss();

                }else{

                    binding.PresenceGroupRecyclerView.setVisibility(View.VISIBLE);
                    binding.Nodata.setVisibility(View.GONE);
                    PresenceGroupList =new ArrayList<>(presenceGroups) ;
                    adapter.setListPresenceGroupe(PresenceGroupList);
                   // binding.PresenceGroupRecyclerView.setAdapter(adapter);
                    pDialog.dismiss();
                }

            }
        });

       presenceViewModel.leadersMutableLiveData.observe(this, new Observer<List<User>>() {
           @Override
           public void onChanged(@Nullable List<User> users) {
               if(users != null && leader != null){
                   listLeaders = new ArrayList<>(users);
                   setLeaderSpinner(leader);
               }else {
                   Toast.makeText(getContext(),"Server error",Toast.LENGTH_SHORT).show();
               }

           }
       });


        return view;
    }


    public void setLeaderSpinner(User leader) {

        ArrayAdapter adapterLeaders = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, listLeaders);
        binding.SpinnerLeader.setAdapter(adapterLeaders);

        for (int i = 0; i < binding.SpinnerLeader.getCount(); i++) {

            if (leader.toString().equals(binding.SpinnerLeader.getItemAtPosition(i).toString())) {
                //System.out.println("leader found");
                binding.SpinnerLeader.setSelection(i);
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
         // System.out.println("date=====>"+makeDateString(day,month ,year));
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
                binding.DateButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(getContext(),style , dateSetListener, year,month,day);
    }

    /*
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }

     */

    @Override
    public void deleteitem(String id , int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Do you want to delete this declaration ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pDialog.show();
                        PresenceGroupList.remove(position);
                        adapter.setListPresenceGroupe(PresenceGroupList);
                        dialogInterface.cancel();
                        presenceViewModel.deletePresenceGroup(id, access_token);
                        pDialog.dismiss();
                        Toast.makeText(getContext(),"Declaration deleted",Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
         //outState.putInt();
    }


}