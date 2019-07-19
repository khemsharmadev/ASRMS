package com.khemsharma.rms;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.khemsharma.rms.Models.Courses;
import com.khemsharma.rms.Models.Users;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterUser extends AppCompatActivity {

    private static final String TAG = "RMSRegister";
    TextInputEditText regName,regEmail,regPassword,regMob,regAddr,regEnrol;
    String name,email,password,enrol,mob,addr,roleID,course_id;

    Spinner regCourse;

    Button registerBtn;
    RadioGroup roleRadioGroup;
    RadioButton roleRadio;

    TextInputLayout enrolLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        regName = findViewById(R.id.regName);
        regEmail = findViewById(R.id.regEmail);
        regPassword = findViewById(R.id.regPassword);
        regMob = findViewById(R.id.regMob);
        regEnrol = findViewById(R.id.regEnrol);
        regAddr = findViewById(R.id.regAddr);
        enrolLayout = findViewById(R.id.enrolLayout);

        regCourse = findViewById(R.id.regCourse);

        registerBtn = findViewById(R.id.registerBtn);

        roleRadioGroup = findViewById(R.id.radioGroup);


        registerBtn = findViewById(R.id.registerBtn);

        roleRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                roleRadio = findViewById(checkedId);
                if (roleRadio.getText().equals("Teacher"))
                {
                    enrolLayout.setVisibility(View.GONE);
                    regCourse.setVisibility(View.GONE);
                    roleID="2";
                }
                else
                {
                    enrolLayout.setVisibility(View.VISIBLE);
                    regCourse.setVisibility(View.VISIBLE);
                    roleID="1";

                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = regName.getText().toString();
                email = regEmail.getText().toString();
                password = regPassword.getText().toString();
                mob = regMob.getText().toString();
                addr = regAddr.getText().toString();
                enrol = regEnrol.getText().toString();
                course_id=regCourse.getItemAtPosition(regCourse.getSelectedItemPosition()).toString();
                course_id=course_id.substring(0,course_id.indexOf('-')).trim();
                registerUser(name,email,password,mob,addr,enrol,course_id,roleID);



            }
        });


        fillCourseList(regCourse);


    }

    private void fillCourseList(final Spinner spinner) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RMSApi.BaseApiUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setLenient()
                        .create()))
                .build();

        RMSApi service = retrofit.create(RMSApi.class);




        Call<Courses> call = service.getCourseList();

        call.enqueue(new Callback<Courses>() {
            @Override
            public void onResponse(Call<Courses> call, Response<Courses> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null)
                    {
                        if (response.body().getSuccess()==1)
                        {
                            Toast.makeText(RegisterUser.this, "Spinner Filled", Toast.LENGTH_SHORT).show();
                            Courses courses=response.body();



                            String[] items = new String[courses.getCourses().size()+1];

                            items[0] = "--Select Course--";
                            for (int i= 0;i<courses.getCourses().size();i++)
                            {
                                items[i+1] = courses.getCourses().get(i).getCourseId() + "-" + courses.getCourses().get(i).getCourseCode();

                            }

                            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,items);

                            spinner.setAdapter(arrayAdapter);
                        }
                        else {
                            Toast.makeText(RegisterUser.this, "Error Occurred. Please Try again later", Toast.LENGTH_SHORT).show();

                        }
                    }

                }
                else {

                }
            }

            @Override
            public void onFailure(Call<Courses> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG + "getMessage", t.getMessage()+call.request()+call.clone());
            }
        });





    }

    private void registerUser(String name, String email, String password, String mob, String addr, String enrol,String course_id, String roleID) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RMSApi.BaseApiUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setLenient()
                        .create()))
                .build();

        RMSApi service = retrofit.create(RMSApi.class);


        Call<Users> call = service.registerUser(name,email,roleID,password,enrol,course_id,mob,addr);

        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null)
                    {
                        if (response.body().getSuccess()==1)
                        {
                            Toast.makeText(RegisterUser.this, "Register Successfully ! You can login now", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(RegisterUser.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(RegisterUser.this, "Error Occurred. Please Try again later", Toast.LENGTH_SHORT).show();

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
    }
}
