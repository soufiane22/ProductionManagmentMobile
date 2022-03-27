package ma.premo.productionmanagment.ui.home;

import android.os.Bundle;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ma.premo.productionmanagment.R;
import ma.premo.productionmanagment.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private TextView text;
    private RequestQueue Queue;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        text = view.findViewById(R.id.Date);
        text.setText("TEST TEXT");
        //OkHttpClient client = new OkHttpClient();

        //String url = "https://reqres.in/api/users?page=2";


        Queue = Volley.newRequestQueue(getContext());
        //jsonparse();




        //final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
              //  textView.setText(s);
            }
        });
        return view;
    }

    private void jsonparse() {
        String url = "http://192.168.137.145:8090/notification_heures/getAll";

        JsonObjectRequest jsonObjectRequest  = new JsonObjectRequest(com.android.volley.Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.e("Network", "Response " + response);
                    JSONArray data = response.getJSONArray("data1");
                    System.out.println("*******connection******");

                    text.setText(String.valueOf(data));

                    for(int i=0 ; i< data.length() ; i++){
                        JSONObject person = data.getJSONObject(i);
                        int h_devolution = person.getInt("h_devolution");
                        String chefEquipe = person.getString("chefEquipe");
                        String of = person.getString("of");
                        String id = person.getString("id");
                        String Sperson = String.valueOf(h_devolution)+", "+id+", "+chefEquipe+", "+of+"\n\n";
                        System.out.println(Sperson);
                        text.append(Sperson);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("*******error connection******");
                error.printStackTrace();
                Log.e("NetworkError", "Response " + error.networkResponse);


            }
        });

        Queue.add(jsonObjectRequest);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}