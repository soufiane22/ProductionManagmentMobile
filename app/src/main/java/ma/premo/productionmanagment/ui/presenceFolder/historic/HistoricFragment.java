package ma.premo.productionmanagment.ui.presenceFolder.historic;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.navigation.NavController;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ma.premo.productionmanagment.MainActivity;
import ma.premo.productionmanagment.R;
import ma.premo.productionmanagment.databinding.FragmentHistoricBinding;
import ma.premo.productionmanagment.databinding.FragmentPresenseBinding;
import ma.premo.productionmanagment.models.Presence;
import ma.premo.productionmanagment.models.User;
import ma.premo.productionmanagment.ui.presenceFolder.PresenceAdapter;
import ma.premo.productionmanagment.ui.presenceFolder.PresenceViewModel;
import ma.premo.productionmanagment.ui.presenceFolder.UserAdapter;


public class HistoricFragment extends Fragment {

    public PresenceViewModel presenceViewModel;
    private String access_token;
    private User leader;
    private NavController navController;
    private FragmentHistoricBinding binding;
    private DatePickerDialog datePickerDialog;
    private List<Presence>    listPresences = new ArrayList<>();
    private DatePickerDialog datePickerStart;
    private  DatePickerDialog datePickerEnd;
    private List<User> listLeaders = new ArrayList<>();
    private Calendar c = Calendar.getInstance();
    private   String startDateStr ;
    private   String endDataStr ;
    private  User selectedLeader;
    private HistoricAdapter adapter;
    private ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenceViewModel = new ViewModelProvider(this,new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(PresenceViewModel.class);
        leader = new User();
        access_token =  ((MainActivity)getActivity()).access_token;
        leader =  ((MainActivity)getActivity()).user;
        navController =  ((MainActivity)getActivity()).navController;
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...PLease wait");
        String function = "Chef%20Equipe";
        presenceViewModel.getUsersByFunction(access_token,function);
        pDialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHistoricBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        View view  = binding.getRoot();


        /******** dates filters *******/
         startDateStr = "";
         endDataStr= "";
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 7; i++) {
            if(i==0)
                startDateStr = df.format(c.getTime());
            if(i==6)
                endDataStr = df.format(c.getTime());
            c.add(Calendar.DATE, 1);
        }
        System.out.println("startDateStr "+startDateStr+"\n"+"endDateStr "+endDataStr);
        binding.StartDateButton.setText(startDateStr);
        binding.EndDateButton.setText(endDataStr);
        iniEndDatePicker();
        iniStartDatePicker();
        binding.StartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerStart.show();
            }
        });

        binding.EndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerEnd.show();
            }
        });
        presenceViewModel.getPresensesBetweenDates(leader.getId(),startDateStr,endDataStr,access_token);

        /*******************************/

        /****** leader spinner ********/
        presenceViewModel.leadersMutableLiveData.observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                if(users != null && leader != null){
                    listLeaders = new ArrayList<>(users);
                    ArrayAdapter<User> adapterLeaders = new ArrayAdapter<User>(getContext(), android.R.layout.simple_spinner_dropdown_item, listLeaders);
                    binding.SpinnerLeader.setAdapter(adapterLeaders);
                    setLeaderSpinner(leader);
                }else {
                    Toast.makeText(getContext(),"Server error",Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.SpinnerLeader.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedLeader = (User)adapterView.getItemAtPosition(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        /******************************/

        /************ search button **********/
        binding.SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenceViewModel.getPresensesBetweenDates(selectedLeader.getId(),binding.StartDateButton.getText().toString(),
                        binding.EndDateButton.getText().toString(),access_token);
                binding.textSearch.setText("");
                pDialog.show();
            }
        });
        /************************************/

        /******** refresh button *********/
        binding.Refreshbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLeaderSpinner(leader);
                binding.StartDateButton.setText(startDateStr);
                binding.EndDateButton.setText(endDataStr);
                presenceViewModel.getPresensesBetweenDates(leader.getId(),startDateStr,endDataStr,access_token);
                binding.textSearch.setText("");
            }
        });

        /************* presence recyclerview ************/
        binding.presenceRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        presenceViewModel.presencesMutableLiveData.observe(this, new Observer<List<Presence>>() {
            @Override
            public void onChanged(@Nullable List<Presence> presences) {
                listPresences = presences;
                adapter = new HistoricAdapter(getContext(),listPresences);
                binding.presenceRecyclerView.setAdapter(adapter);
                binding.SumHours.setText(String.valueOf(new DecimalFormat("##.##").format(adapter.getSumHours())));
                pDialog.dismiss();

            }
        });
        /********************************************/

        /******************* search edit text *********************/
        binding.textSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
               filter(editable.toString());
            }
        });


        return view;
    }

    private void filter(String text) {
         List<Presence> filtredPesences = new ArrayList<>();
         if(text == null || text.equals("")){
             filtredPesences = listPresences;
         }else {
             for(Presence p : listPresences){
                 if(isNumeric(text)){
                     if(String.valueOf(p.getMatriculePerson()).toLowerCase().startsWith(String.valueOf(Integer.parseInt(text)).toLowerCase()))
                         filtredPesences.add(p);
                 }
                 if(p.getFunctionPerson().toLowerCase().startsWith(text.toLowerCase())){
                     filtredPesences.add(p);
                 }
                 if(p.getNomPerson().toLowerCase().startsWith(text.toLowerCase())
                         || p.getPrenomPerson().toLowerCase().startsWith(text.toLowerCase()) ||
                         p.getLine().getDesignation().toLowerCase().startsWith(text.toLowerCase()) ||
                         p.getEtat().toLowerCase().startsWith(text.toLowerCase()) ||
                         p.getShift().toLowerCase().startsWith(text.toLowerCase())){
                     filtredPesences.add(p);
                 }
             }
         }

         adapter.setListPresence(filtredPesences);
        binding.SumHours.setText(String.valueOf(adapter.getSumHours()));
    }

    public static boolean isNumeric(String string) {
        int intValue;

       // System.out.println(String.format("Parsing string: \"%s\"", string));

        if(string == null || string.equals("")) {
            //System.out.println("String cannot be parsed, it is null or empty.");
            return false;
        }

        try {
            intValue = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
           // System.out.println("Input String cannot be parsed to Integer.");
        }
        return false;
    }

    private void iniStartDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month+1;
                String date = makeDateString(day,month,year);
                binding.StartDateButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerStart = new DatePickerDialog(getContext(),style , dateSetListener, year,month,day);
    }

    private void iniEndDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month+1;
                String date = makeDateString(day,month,year);
                binding.EndDateButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerEnd = new DatePickerDialog(getContext(),style , dateSetListener, year,month,day);
    }

    private String makeDateString(int day, int month, int year) {
        return year+"-"+month+"-"+day;
    };

    public void setLeaderSpinner(User leader) {

        for (int i = 0; i < binding.SpinnerLeader.getCount(); i++) {

            if (leader.toString().equals(binding.SpinnerLeader.getItemAtPosition(i).toString())) {
                binding.SpinnerLeader.setSelection(i);
                break;
            }
        }
    }
}