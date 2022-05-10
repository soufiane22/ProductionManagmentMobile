package ma.premo.productionmanagment.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;

import androidx.navigation.NavController;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ma.premo.productionmanagment.MainActivity;
import ma.premo.productionmanagment.R;
import ma.premo.productionmanagment.SignInActivity;
import ma.premo.productionmanagment.databinding.FragmentHomeBinding;
import ma.premo.productionmanagment.ui.notification_hours.NotificationHFragment;
import ma.premo.productionmanagment.ui.presenceFolder.PresenceFragment;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private TextView text;
    private RequestQueue Queue;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private NavigationView navigationView;
    private  NavController navController;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        navigationView =  ((MainActivity)getActivity()).navigationView;
        navController =  ((MainActivity)getActivity()).navController;
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        Queue = Volley.newRequestQueue(getContext());

        binding.cardNotificationHours.setOnClickListener(this);
        binding.cardPresence.setOnClickListener(this);
        binding.cardScrap.setOnClickListener(this);
        binding.cardGroup.setOnClickListener(this);
        binding.cardStatistic.setOnClickListener(this);
        binding.cardExit.setOnClickListener(this);
        return view;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.cardNotificationHours:
                navController.navigate(R.id.notification_fragment);

                break;
            case R.id.cardPresence:
                navController.navigate(R.id.presence_fragment);
                break;
            case R.id.cardScrap:
                navController.navigate(R.id.scarp_fragment);
                break;
            case R.id.cardGroup:
                navController.navigate(R.id.group_fragment);
                break;
            case R.id.cardStatistic:
                navController.navigate(R.id.statistics_fragment);
                break;
            case R.id.cardExit:
                Intent intent = new Intent(getContext() , SignInActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
        }
    }
}