package ma.premo.productionmanagment.ui.Notification_Hours;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.premo.productionmanagment.R;
import ma.premo.productionmanagment.databinding.FragmentNotificationHoursBinding;
import ma.premo.productionmanagment.models.NotificationHAdapter;
import ma.premo.productionmanagment.models.Notification_Hours;


public class add_notification extends Fragment {
    private DatePickerDialog datePickerDialog;
    private Button DateButton;
    private Button saveButton ;
    private NotificationHViewModel notificationViewModel;
   // FragmentActivitydd_notificationBinding binding;
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
    ProgressDialog pDialog;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private final String url ="http://192.168.137.131:8090/notification_heures/" ;

    List<Notification_Hours> notificationsList ;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.add_notification1, container, false);

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
      //  binding = FragmentNotificationHoursBinding.inflate(inflater, container, false);
        DateButton = (Button) view.findViewById(R.id.Datebutton);
        saveButton = (Button) view.findViewById(R.id.button2) ;
        lineSpinner = (Spinner) view.findViewById(R.id.Linespinner) ;
        shiftSpinner  = (Spinner) view.findViewById(R.id.Shiftspinner) ;
        ofSpinner = (Spinner) view.findViewById(R.id.OFSpinner) ;
        nbr_operators = view.findViewById(R.id.nbrOperators);
        hTotal = view.findViewById(R.id.hTotal);
        hExtra = view.findViewById(R.id.hExtra);
        hDevolution = view.findViewById(R.id.hDevolution);
        hStopped = view.findViewById(R.id.hStopped);
        hNewProject = view.findViewById(R.id.hNewProject);
        remark = (TextView) view.findViewById(R.id.Remark) ;

        nbr_operators.setText("0");
        hTotal.setText("0");
        hExtra.setText("0");
        hDevolution.setText("0");
        hStopped.setText("0");
        hNewProject.setText("0");
        Queue = Volley.newRequestQueue(getContext());
        notificationsList = new ArrayList<>();
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...PLease wait");

        CreateSpinners();
        getShift();
        getOf();
        getLine();
        initDatePicker();

        String  date = getTodayDates();
        System.out.println("1---date===>"+date);
        DateButton.setText(getTodayDates());

        DateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // System.out.println("tester date");
                datePickerDialog.show();
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // nameEdt.getText().toString().isEmpty()

                save_notification();

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

    public void save_notification(){
        pDialog.show();
        String urlSaveN = url+"save/";
        Notification_Hours notif = new Notification_Hours();
        notif.setDate(DateButton.getText().toString());
        notif.setShift(getShift());
        notif.setLigne(getLine());
        notif.setOF(getOf());
        notif.setNbr_operateurs(Integer. parseInt(nbr_operators.getText().toString()));
        notif.setTotal_h(Integer. parseInt(hTotal.getText().toString()));
        notif.setH_sup(Integer. parseInt(hExtra.getText().toString()));
        notif.setH_devolution(Integer. parseInt(hDevolution.getText().toString()));
        notif.setH_arrete(Integer. parseInt(hStopped.getText().toString()));
        notif.setH_nouvau_projet(Integer. parseInt(hNewProject.getText().toString()));
        Log.e("Notification", "Notification for add====> " + notif.toString());
        Map<String, String> notification = new HashMap<>();
        notification.put("of",notif.getOF());
        notification.put("date",String.valueOf(notif.getDate()));
        notification.put("shift",notif.getShift());
        notification.put("ligne",notif.getLigne());
        notification.put("nbr_operateurs",String.valueOf(notif.getNbr_operateurs()));
        notification.put("total_h",String.valueOf(notif.getTotal_h()));
        notification.put("h_sup",String.valueOf(notif.getH_sup()));
        notification.put("h_devolution",String.valueOf(notif.getH_devolution()));
        notification.put("h_arrete",String.valueOf(notif.getH_arrete()));
        notification.put("h_nouvau_projet",String.valueOf(notif.getH_nouvau_projet()));
        notification.put("remark",remark.getText().toString());
        JSONObject json = new JSONObject(notification);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,urlSaveN,json,
                response -> {

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
                });

        Queue.add(jsonRequest);



    }




    public add_notification() {
        // Required empty public constructor
    }




    private void CreateSpinners() {
        ArrayAdapter<CharSequence> adapterShift = ArrayAdapter.createFromResource(getContext(),R.array.shift, android.R.layout.simple_spinner_item);
        adapterShift.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shiftSpinner.setAdapter(adapterShift);

        ArrayAdapter<CharSequence> adapterLine = ArrayAdapter.createFromResource(getContext(),R.array.lines, android.R.layout.simple_spinner_item);
        adapterLine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lineSpinner.setAdapter(adapterLine);

        ArrayAdapter<CharSequence> adapterOF = ArrayAdapter.createFromResource(getContext(),R.array.OF, android.R.layout.simple_spinner_item);
        adapterOF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ofSpinner.setAdapter(adapterOF);

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

    String line ;
    private String getLine() {
        lineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                line  = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(),line,Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return line;
    }

    String of ;
    private String getOf() {
        ofSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                of  = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(),of,Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return of;
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