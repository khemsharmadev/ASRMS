package com.khemsharma.rms;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.khemsharma.rms.Models.User;
import com.khemsharma.rms.Models.Users;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "RMSMain";
    Button profileSubmit;

    ImageButton backbtn,drawer_toggle,close_nav,log_out;

    User user;

    Fragment homeFragment;

    boolean doubleBackToExitPressedOnce = false;

    EditText nameEt,emailEt,enrolEt,roleEt,mobEt,addrEt,passEt;

    TextView username,userEmail;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Editext EditText nameEt,emailEt,enrolEt,roleEt,mobEt,addrEt;
        nameEt =findViewById(R.id.nameEt);
        emailEt =findViewById(R.id.emailEt);
        enrolEt =findViewById(R.id.enrolEt);
        roleEt =findViewById(R.id.roleEt);
        mobEt =findViewById(R.id.mobEt);
        addrEt =findViewById(R.id.addrEt);
        passEt =findViewById(R.id.passEt);

        username =findViewById(R.id.username);
        userEmail =findViewById(R.id.userEmail);



        close_nav=findViewById(R.id.close_nav);

        log_out=findViewById(R.id.log_out);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);

        drawer_toggle=findViewById(R.id.drawer_toggle);

        drawer_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);

            }
        });

        NavigationView navigationView = findViewById(R.id.navigation_view);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
        params.width = metrics.widthPixels;
        navigationView.setLayoutParams(params);

        profileSubmit=findViewById(R.id.profileSubmit);
        user = AppUtils.getCurrentUser(getApplicationContext());
        Toast.makeText(this, user.getEmailID() , Toast.LENGTH_SHORT).show();

        //set profile details
        emailEt.setText(user.getEmailID());
        passEt.setText("******");
        enrolEt.setText(user.getEnrolmentNumber());
        nameEt.setText(user.getName());
        mobEt.setText(user.getMob());
        addrEt.setText(user.getAddress());

        //Nav header
        username.setText(user.getName());
        userEmail.setText(user.getEmailID());

        String role;

        if (user.getRoleID().equals("1"))
        {
            role="Student";
        }else if (user.getRoleID().equals("2"))
        {
            role="Teacher";
        }
        else role="Student";

        roleEt.setText(role);




        close_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.START,true);
            }
        });

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        if (user.getRoleID().equals("1"))
        {
            homeFragment = new StudentHome();
            fragmentTransaction.replace(R.id.maincontainer,homeFragment);
            fragmentTransaction.commit();

        }else if (user.getRoleID().equals("2"))
        {
            homeFragment = new TeacherHome();
            fragmentTransaction.replace(R.id.maincontainer,homeFragment);
            fragmentTransaction.commit();
        }else
            {
                homeFragment = new AdminHome();
                fragmentTransaction.replace(R.id.maincontainer,homeFragment);
                fragmentTransaction.commit();
        }

        getSupportActionBar().setTitle("");

        profileSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nameEt.isEnabled())
                    {
                        profileSubmit.setText("Save");

                        if (user.getRoleID().equals("1"))
                        {
                            enrolEt.setEnabled(true);
                        }

                        nameEt.setEnabled(true);
                        emailEt.setEnabled(true);
                        passEt.setEnabled(true);
                        mobEt.setEnabled(true);
                        addrEt.setEnabled(true);

                    }
                else
                    {
                        profileSubmit.setText("Edit");



                        nameEt.setEnabled(false);
                        emailEt.setEnabled(false);
                        passEt.setEnabled(false);
                        enrolEt.setEnabled(false);
                        mobEt.setEnabled(false);
                        addrEt.setEnabled(false);

                        updateProfile(nameEt.getText().toString(),
                                emailEt.getText().toString(),
                                enrolEt.getText().toString(),
                                passEt.getText().toString(),
                                roleEt.getText().toString(),
                                mobEt.getText().toString(),
                                addrEt.getText().toString()
                        );

                    }

            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.logOut(getApplicationContext());
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void updateProfile(String name, String email, String enrol, String pass, String role, String mob, String add) {

        //Update Profile
        if (pass.equals("******"))
        {
            pass=AppUtils.getCurrentUser(this).getPassword();
        }

        User user = AppUtils.getCurrentUser(this);
        user.setName(name);
        user.setEmailID(email);
        user.setEnrolmentNumber(enrol);
        user.setPassword(pass);
        user.setMob(mob);
        user.setAddress(add);

        AppUtils.saveUserInfo(this,user);



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RMSApi.BaseApiUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setLenient()
                        .create()))
                .build();

        RMSApi service = retrofit.create(RMSApi.class);

        Call<Users> call = service.updateUser(name,email,user.getRoleID(),pass,enrol,
                AppUtils.getCurrentUser(this).getCourseId(),mob,add);

        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (response.body().getSuccess() == 1) {
                            Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {

                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG + "getMessage", t.getMessage()+call.request()+call.clone());

            }
        });


        passEt.setText("******");


    }


    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        DrawerLayout drawer= findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        } else if (count!=0){
            getFragmentManager().popBackStack();
        }
        else
        {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                overridePendingTransition(R.anim.in_anim, R.anim.out_anim);

                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Snackbar.make(drawer, "Please click BACK again to exit", Snackbar.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }




}
