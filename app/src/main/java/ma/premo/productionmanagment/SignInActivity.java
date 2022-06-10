package ma.premo.productionmanagment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ma.premo.productionmanagment.Utils.API;
import ma.premo.productionmanagment.databinding.ActivitySignInBinding;

public class SignInActivity extends AppCompatActivity {

private ActivitySignInBinding binding;
private ProgressDialog pDialog;
private RequestQueue Queue;
private  Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_sign_in);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pDialog = new ProgressDialog(SignInActivity.this);
        pDialog.setMessage("Loading...PLease wait");
        Queue = Volley.newRequestQueue(getApplicationContext());


        intent = new Intent(this, MainActivity.class);

        binding.SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog.show();
                String username = binding.UserName.getText().toString();
                String password = binding.Password.getText().toString();
                signIn(username,password);
                binding.UserName.setText("");
                binding.Password.setText("");
            }
        });
    }

    private void signIn(String username, String password) {
      //  System.out.println("Username :"+username+"password: "+password);
        //pDialog.show();
        String url = API.urlBackend+"login"; //?username=" + username + "&password="+ password
        HashMap<String,String> login = new HashMap<>();
        login.put("username",username);
        login.put("password",password);
        JSONObject json = new JSONObject(login);
        HashMap<String, String> headers = new HashMap<String, String>();

        StringRequest jsonObjectRequest  = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
             @Override
              public void onResponse(String response) {
                 System.out.println("response====>" + response.toString());
                 Toast.makeText(getApplicationContext(), "login with success", Toast.LENGTH_SHORT).show();
                 intent. putExtra("username", username);
                 intent. putExtra("tokens",response.toString());
                 pDialog.dismiss();
                 startActivity(intent);

                 finish();
                    }
                }, new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {
                  pDialog.dismiss();
                  Log.e("error", error.toString());

                  if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                      Toast.makeText(getApplicationContext(), "Network error",Toast.LENGTH_LONG).show();
                  } else if (error instanceof AuthFailureError) {
                      //TODO
                      Toast.makeText(getApplicationContext(), "username or password is incorrect", Toast.LENGTH_LONG).show();
                  } else if (error instanceof ServerError) {
                      //TODO
                      Toast.makeText(getApplicationContext(), "Server error",Toast.LENGTH_LONG).show();

                  } else if (error instanceof NetworkError) {
                      Toast.makeText(getApplicationContext(), "Network error",Toast.LENGTH_LONG).show();
                      //TODO
                  } else if (error instanceof ParseError) {
                      //TODO
                      Toast.makeText(getApplicationContext(), "Parse error",Toast.LENGTH_LONG).show();
                  }
              }
        }){

            @Override
            public Map<String, String> getParams() throws AuthFailureError{
                Map<String,String> params = new HashMap<String, String>();
                params.put("username",username );
                params.put("password", password);
                JSONObject jsonObject = new JSONObject(params);
                return  params;
            }
  /*
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public Map<String, String> getHeaders()  {
                try {
                    headers.put("Content-Type", "application/json");

                    return headers;
                } catch (Exception e) {
                    e.printStackTrace();
                    return headers;
                }
            }

 */

        };



        Queue.add(jsonObjectRequest);

    }
}