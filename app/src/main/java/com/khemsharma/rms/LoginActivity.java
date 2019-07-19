package com.khemsharma.rms;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;




import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.khemsharma.rms.Models.User;
import com.khemsharma.rms.Models.Users;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText username,password;

    Button loginBtn,registerBtn;

    String TAG="RMSLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username=findViewById(R.id.username);
        password=findViewById(R.id.password);

        loginBtn=findViewById(R.id.loginBtn);
        registerBtn=findViewById(R.id.registerNowBtn);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getAllUsers();

                String user = username.getText().toString().trim();
                String pass = password.getText().toString().trim();
                if (user.isEmpty()&&pass.isEmpty())
                {
                    userLogin("sahilhry1@gmail.com","password");
                }
                else userLogin(user,pass);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterUser.class);
                startActivity(intent);

            }
        });



        

    }

    private void userLogin(String username, String password) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RMSApi.BaseApiUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setLenient()
                        .create()))
                .build();

        RMSApi service = retrofit.create(RMSApi.class);


        Call<Users> call = service.userLogin(username, password);

        call.enqueue(new Callback<Users>() {

            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {








                if (response.isSuccessful()) {


                    if (response.body() != null)
                    {

                        if (response.body().getSuccess()!=0)
                        {
                            User user = response.body().getUsers().get(0);
                            Log.d(TAG+"Email",user.getEmailID());
                            Log.d(TAG+"Name",user.getName());
                            AppUtils.saveUserInfo(getApplicationContext(),user);
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();

                        }
                        else Toast.makeText(LoginActivity.this, "User Not Found", Toast.LENGTH_SHORT).show();

                    }
                    else Toast.makeText(LoginActivity.this, "User Not Found", Toast.LENGTH_SHORT).show();
                    //finish();
                    //SharedPrefManager.getInstance(getApplicationContext()).userLogin(response.body().getUser());
                    //startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_LONG).show();
                    Log.d(TAG + "err", response.errorBody().toString());
                    Log.d(TAG + "code", response.message());

                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG + "getMessage", t.getMessage()+call.request()+call.clone());


            }
        });


    }


    public void getAllUsers(){


        Retrofit loginuser = new Retrofit.Builder()
                .baseUrl(RMSApi.BaseApiUrl)
                //.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RMSApi service = loginuser.create(RMSApi.class);

        Call<Users> call = service.getUsersList();

        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                //List<Users> users = (List<Users>) response.body();


                Log.d(TAG + "isSuccessful", response.body().toString());
                if (response.body()!=null)
                    Log.d(TAG + "res", response.body().toString());

                Users users = response.body();
                // Log.d(TAG + "Message", response.raw().toString());
                // Log.d(TAG + "Message2", response.body().toString());

                if (users.getUsers()!=null)
                {
                    Log.d(TAG + "getUsers ID", users.getUsers().toString());
                }


               /* assert users != null;
                //for (int i=0;i<users.size();i++)
                for(Users u: users)
                    Log.d(TAG + "Email ID", u.getEmailID());*/

                //Log.d(TAG + "Email ID", users.getEmailID());
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG +"onfail",t.getMessage());
                //Log.d(TAG)
            }
        });
    }
}
