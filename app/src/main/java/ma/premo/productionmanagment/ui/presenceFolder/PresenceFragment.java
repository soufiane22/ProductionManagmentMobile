package ma.premo.productionmanagment.ui.presenceFolder;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ma.premo.productionmanagment.MainActivity;
import ma.premo.productionmanagment.R;
import ma.premo.productionmanagment.databinding.FragmentPresenseBinding;
import ma.premo.productionmanagment.models.DeclarationPresence;
import ma.premo.productionmanagment.models.User;

public class PresenceFragment extends Fragment {

    private PresenceViewModel presenceViewModel;
    private FragmentPresenseBinding binding;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private String mode = "";
    private DeclarationPresence declarationPresence;
    private ArrayList<DeclarationPresence> listDeclarationPresence = new ArrayList<>();
    private ProgressDialog pDialog;
    private String access_token;
    private User leader;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenceViewModel = new ViewModelProvider(this,new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(PresenceViewModel.class);;
       // View view = inflater.inflate(R.layout.fragment_presense, container, false);
        access_token =  ((MainActivity)getActivity()).access_token;
        leader =  ((MainActivity)getActivity()).user;

        binding = FragmentPresenseBinding.inflate(inflater, container, false);
        binding.setViewModel(presenceViewModel);
        binding.setLifecycleOwner(this);
        View view  = binding.getRoot();
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...PLease wait");


        //binding.PresencesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPresenceFragment addPresenceFragment = new AddPresenceFragment();
                Bundle bundle = new Bundle();
                bundle.putString("mode","add");
                addPresenceFragment.setArguments(bundle);
                fragmentTransaction.setReorderingAllowed(true);
                fragmentTransaction.replace( R.id.nav_host_fragment_content_main,addPresenceFragment,null).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        Bundle args = getArguments();
        if (args != null){
            Log.e("args",args.toString());
            mode  = args.getString("mode");

        }

        presenceViewModel.getPresenses(leader.getId() , access_token, getTodayDates());

        presenceViewModel.declarationPresenceMutableLiveData.observe(this, new Observer<DeclarationPresence>() {
                @Override
                public void onChanged(@Nullable DeclarationPresence MdeclarationPresence) {
                    pDialog.show();
                    if(MdeclarationPresence == null) {
                       // Toast.makeText(getContext(), "no error", Toast.LENGTH_SHORT).show();
                        binding.Cardview.setVisibility(View.GONE);
                        binding.Nodata.setVisibility(View.VISIBLE);
                        pDialog.dismiss();
                    }
                    else{
                        binding.Cardview.setVisibility(View.VISIBLE);
                        binding.Nodata.setVisibility(View.GONE);
                        declarationPresence = MdeclarationPresence;
                        listDeclarationPresence.add(declarationPresence);
                        System.out.println("2----declarationPresence======>"+declarationPresence.toString());
                        //AllPresenceAdapter adapter = new AllPresenceAdapter(getContext(),listDeclarationPresence);
                        //binding.PresencesRecyclerView.setAdapter(adapter);
                        pDialog.dismiss();
                    }
                }

            });


        return view;
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
        return day+"-"+month+"-"+year;
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }
}