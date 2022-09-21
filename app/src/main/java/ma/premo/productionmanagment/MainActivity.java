package ma.premo.productionmanagment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.support.design.widget.Snackbar;
import android.support.design.widget.NavigationView;


import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingDeque;

import ma.premo.productionmanagment.Utils.API;
import ma.premo.productionmanagment.Utils.JsonConvert;
import ma.premo.productionmanagment.databinding.ActivityMainBinding;
import ma.premo.productionmanagment.models.User;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    public   String access_token;
    public String idLeader;
    public User user;
    private RequestQueue Queue;
    private   TextView  name;
    private   TextView function;
    public NavigationView navigationView;
    public NavController navController;
    public  Long timeOfLastLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Queue = Volley.newRequestQueue(getApplicationContext());
        Bundle extras = getIntent().getExtras();
         timeOfLastLogin = (Long) extras.getLong("last_login");
        System.out.println("timeOfLastLogin "+timeOfLastLogin);
        System.out.println("System.currentTimeMillis() "+System.currentTimeMillis());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.notification_fragment,R.id.presence_fragment,R.id.scarp_fragment,R.id.group_fragment,R.id.statistics_fragment )
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = navigationView.getHeaderView(0);
        name = (TextView) headerView.findViewById(R.id.name);
        function = (TextView) headerView.findViewById(R.id.function);
        if(extras != null) {
            String username = extras.getString("username");
            String tokens = extras.getString("tokens");
            JSONParser parser = new JSONParser();
            JSONObject json = null;
            try {
                json = (JSONObject) parser.parse(tokens);
                org.json.JSONObject json1 = (org.json.JSONObject) new org.json.JSONObject(json);
                access_token = json1.getString("access_token");
                String userConnected = json1.getString("userjsonstring");
                //System.out.println("1----- userjsonstring "+userConnected);
                user = JsonConvert.getGsonParser().fromJson(String.valueOf(userConnected), User.class);
                String nameUser = user.getNom()+" "+user.getPrenom();
                name.setText(nameUser);
                function.setText(user.getFonction());
              //  System.out.println("2----- user object "+user.toString2());
                Log.println(Log.ASSERT,"access_token",access_token);

            } catch (ParseException | JSONException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.SignOut:
                Intent intent = new Intent(this ,SignInActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /*
    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        }
        else {
            getFragmentManager().popBackStackImmediate();
           // getFragmentManager().popBackStack();
        }
    }

     */

 
}