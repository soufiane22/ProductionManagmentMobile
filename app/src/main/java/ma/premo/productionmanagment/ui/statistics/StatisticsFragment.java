package ma.premo.productionmanagment.ui.statistics;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.navigation.NavController;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ma.premo.productionmanagment.MainActivity;
import ma.premo.productionmanagment.R;
import ma.premo.productionmanagment.SignInActivity;
import ma.premo.productionmanagment.databinding.FragmentPresenseBinding;
import ma.premo.productionmanagment.databinding.FragmentStatisticsBinding;
import ma.premo.productionmanagment.databinding.StatisticItemBinding;
import ma.premo.productionmanagment.models.Groupe;
import ma.premo.productionmanagment.models.Line;
import ma.premo.productionmanagment.models.Notification_Hours;
import ma.premo.productionmanagment.models.Statistic;
import ma.premo.productionmanagment.models.User;
import ma.premo.productionmanagment.ui.presenceFolder.AddPresenceViewModel;
import ma.premo.productionmanagment.ui.presenceFolder.PresenceViewModel;
import org.achartengine.chart.BarChart.Type;

public class StatisticsFragment extends Fragment {

    public StatisticViewModel statisticViewModel;
    private String access_token;
    private User leader;
    private FragmentStatisticsBinding binding;
    private ProgressDialog pDialog;
    private List<Notification_Hours> listNotifications = new ArrayList<>();
    private NavController navController;
    private StatisticAdapter adapter;
    private ArrayAdapter<CharSequence> adapterFilter;
    private DatePickerDialog datePickerStart;
    private DatePickerDialog datePickerEnd;
    private AddPresenceViewModel addPresenceViewModel;
    private Groupe groupe;
    private List<Line> listLine = new ArrayList<Line>();
    private List<Statistic> listStatistic = new ArrayList<>();
    private GraphicalView mchart;
    private XYMultipleSeriesDataset mDataSet = new XYMultipleSeriesDataset();
    private XYMultipleSeriesRenderer mDataRender = new XYMultipleSeriesRenderer();
    private XYSeries mCurentSerries;
    private XYSeriesRenderer mCurentRender;
    private LinearLayout layout;
    private XYSeries productivitySeries = new XYSeries("Productivity");
    private XYSeries scrapSeries = new XYSeries("Scrap ratio");
    private XYSeriesRenderer productivityRenderer = new XYSeriesRenderer();
    private XYSeriesRenderer scrapRenderer = new XYSeriesRenderer();
    private XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
    private XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        leader = new User();
        access_token = ((MainActivity) getActivity()).access_token;
        leader = ((MainActivity) getActivity()).user;
        navController = ((MainActivity) getActivity()).navController;
        iniEndDatePicker();
        iniStartDatePicker();
        addPresenceViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(AddPresenceViewModel.class);
        if (leader != null) {
            addPresenceViewModel.getGroup(leader.getId(), access_token);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        statisticViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(StatisticViewModel.class);

        if (leader != null) {
            statisticViewModel.getListNotifications(leader.getId(), access_token);
        } else {
            Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
        }
        binding = FragmentStatisticsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...PLease wait");
         binding.statisticRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapterFilter = ArrayAdapter.createFromResource(getContext(), R.array.filter, android.R.layout.simple_spinner_item);
        adapterFilter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        /******* get start and end date in the current month ****************/
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date monthFirstDay = calendar.getTime();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date monthLastDay = calendar.getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String startDateStr = df.format(monthFirstDay);
        String endDateStr = df.format(monthLastDay);
        Log.e("DateFirstLast",startDateStr+" "+endDateStr);
        binding.StartDate.setText(startDateStr);
        binding.EndDate.setText(endDateStr);
      /*****************************************************************************/
        //binding.FilterSpinner.setAdapter(adapterFilter);

        /*
        binding.FilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if( binding.FilterSpinner.getSelectedItem().equals("Between two dates")){
                    binding.StartDate.setVisibility(View.VISIBLE);
                    binding.EndDate.setVisibility(View.VISIBLE);
                    binding.OfSelected.setVisibility(View.GONE);
                }else{
                    binding.StartDate.setVisibility(View.GONE);
                    binding.EndDate.setVisibility(View.GONE);
                    binding.OfSelected.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

         */

        addPresenceViewModel.groupeMutableLiveData.observe(this, new Observer<Groupe>() {
            @Override
            public void onChanged(@Nullable Groupe groupeM) {
                if (groupeM != null) {
                    groupe = groupeM;
                    listLine = groupeM.getListLine();
                    //System.out.println("list lines ====>"+listLine);
                    ArrayAdapter<Line> adapterLine = new ArrayAdapter<Line>(getContext(), android.R.layout.simple_spinner_dropdown_item, listLine);
                    binding.linespinner.setAdapter(adapterLine);
                    statisticViewModel.getStatistics(access_token, startDateStr, endDateStr, listLine.get(0).getId());
                } else {
                    Toast.makeText(getContext(), "No lines available", Toast.LENGTH_SHORT).show();
                }


            }
        });


       // binding.StartDate.setText(getDate("today"));
        //binding.EndDate.setText(getDate("tomorrow"));
        binding.StartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerStart.show();
            }
        });

        binding.EndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerEnd.show();
            }
        });



        binding.SearchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                pDialog.show();
                Line lineSelected = (Line) binding.linespinner.getSelectedItem();
               // System.out.println("start date : "+binding.StartDate.getText().toString()+" end date"+binding.EndDate.getText().toString());
                statisticViewModel.getStatistics(access_token, binding.StartDate.getText().toString(), binding.EndDate.getText().toString(), lineSelected.getId());

                /*
                if(binding.FilterSpinner.getSelectedItem().equals("Between two dates")){
                    System.out.println("start date "+binding.StartDate.getText()+"\n"+"end date "+binding.EndDate.getText());
                    statisticViewModel.getNotificationsBetweenDates(access_token ,
                                                                    binding.StartDate.getText().toString(),
                                                                    binding.EndDate.getText().toString(),leader.getId());

                }
                else{
                    pDialog.show();
                    if(binding.OfSelected.getText().toString().isEmpty()){
                        Toast.makeText(getContext(),"Enter the OF please!",Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }

                    else{
                        System.out.println("OF selected "+Integer.parseInt(binding.OfSelected.getText().toString()));
                        statisticViewModel.getNotificationsByOF(access_token ,
                                                                leader.getId(),
                                                                Integer.parseInt(binding.OfSelected.getText().toString()));

                    }

                }

                 */

            }
        });

        /*******  Create bar chart graph ******/

        layout = view.findViewById(R.id.linearLayout);

        //initChart();
        statisticViewModel.StatisticsMutableLiveData.observe(this, new Observer<List<Statistic>>() {
            @Override
            public void onChanged(@Nullable List<Statistic> statistics) {
                listStatistic.clear();
                listStatistic = statistics;
                productivitySeries.clear();
                scrapSeries.clear();
                dataset.clear();
               multiRenderer.clearXTextLabels();
                System.out.println("1-----list of statistics : " + listStatistic);
                if (listStatistic == null || listStatistic.size() == 0) {
                    Toast.makeText(getActivity().getApplicationContext(), "No Data found", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } else {

                      //onResum();
                     initChart();
                    adapter = new StatisticAdapter(getContext(), listStatistic);
                    binding.statisticRecyclerView.setAdapter(adapter);
                    pDialog.dismiss();
                }
            }
        });

        statisticViewModel.tokenExpired.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean){
                    Intent loginIntent = new Intent(getContext() , SignInActivity.class);
                    startActivity(loginIntent);
                    getActivity().finish();
                }
            }
        });

        return view;
    }


    private void initChart() {
        multiRenderer.clearXTextLabels();
        multiRenderer.removeAllRenderers();
        // Creating XYSeriesRenderer to customize productivitySerie
        productivityRenderer.setColor(Color.rgb(1,192,10));
        productivityRenderer.setFillPoints(true);
        productivityRenderer.setLineWidth(2);
        productivityRenderer.setDisplayChartValues(true);
        productivityRenderer.setChartValuesTextSize(15);
        // Creating XYSeriesRenderer to customize scrapSeries
        scrapRenderer.setColor(Color.rgb(251,77,35));
        scrapRenderer.setFillPoints(true);
        scrapRenderer.setLineWidth(2);
        scrapRenderer.setDisplayChartValues(true);
        scrapRenderer.setAnnotationsTextSize(16);
        scrapRenderer.setChartValuesTextSize(15);
        scrapRenderer.setChartValuesTextAlign(Paint.Align.CENTER);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart

        multiRenderer.setXLabels(0);
        multiRenderer.setChartTitle("Productivity and Scrap ration Chart");
        multiRenderer.setXTitle("Leaders");
        multiRenderer.setYTitle("percentage %");
        multiRenderer.setAxisTitleTextSize(16);
        multiRenderer.setLabelsTextSize(15);
        multiRenderer.setLegendTextSize(15);
        //multiRenderer.setZoomButtonsVisible(true);


        multiRenderer.setXAxisMin(-1);
        multiRenderer.setXAxisMax(4);
        multiRenderer.setYAxisMin(0);
        multiRenderer.setYAxisMax(140);
        multiRenderer.setBarSpacing(0.4);

        for (Statistic st : listStatistic) {
            productivitySeries.add(listStatistic.indexOf(st),st.getProductivity() );
            //System.out.println("st.getTauxScrap() "+st.getTauxScrap());
           // Double sc = Double.parseDouble( new DecimalFormat("##.##").format(st.getTauxScrap()));
            scrapSeries.add(listStatistic.indexOf(st), st.getTauxScrap());
            multiRenderer.addXTextLabel(listStatistic.indexOf(st), st.getLeaderName());
        }

        dataset.addSeries(productivitySeries);
        dataset.addSeries(scrapSeries);
        multiRenderer.addSeriesRenderer(productivityRenderer);
        multiRenderer.addSeriesRenderer(scrapRenderer);
        //getDataset();
       // System.out.println("2---dataset : "+dataset.getSeries().length);
        //System.out.println("3---multiRender : "+multiRenderer.getSeriesRendererCount());
        mchart = ChartFactory.getBarChartView(getContext(), dataset, multiRenderer, Type.DEFAULT);
        mchart.setBackgroundColor(Color.GRAY);
        if (mchart != null){
            mchart.repaint();
            layout.addView(mchart);
        }else{
            layout.addView(mchart);
        }


    }

    public  void getDataset(){
        //System.out.println("1.1---dataset : "+listStatistic.toString());
        for (Statistic st : listStatistic) {
            productivitySeries.add(listStatistic.indexOf(st),Double.parseDouble(String.format("%.2f",st.getProductivity())) );
            scrapSeries.add(listStatistic.indexOf(st),Double.parseDouble(String.format("%.2f",st.getTauxScrap())) );
        }

        dataset.addSeries(productivitySeries);
        dataset.addSeries(scrapSeries);
       // System.out.println("2---dataset : "+dataset.toString());
       // System.out.println("3---multiRenderer : "+multiRenderer.toString());


    }


    protected void onResum(){
        super.onResume();
        if(mchart == null){
            initChart();
            mchart = ChartFactory.getBarChartView(getContext(),dataset,multiRenderer,Type.DEFAULT);
            if (mchart != null)
            layout.addView(mchart);
        }else{
            mchart.repaint();
        }
    }

    private String getDate(String param) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        if(param.equals("tomorrow"))
           day += 1;
        return makeDateString(day,month ,year);

    }


    private void iniStartDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month+1;
                String date = makeDateString(day,month,year);
                binding.StartDate.setText(date);
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
                binding.EndDate.setText(date);
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

    public StatisticsFragment() {
        // Required empty public constructor
    }
}