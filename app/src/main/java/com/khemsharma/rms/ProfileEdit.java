package com.khemsharma.rms;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.khemsharma.rms.Models.User;
import com.khemsharma.rms.Models.Users;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileEdit extends Fragment {


    private static final String TAG ="RMSProfileEdit" ;
    View view;


    EditText nameEt,emailEt,enrolEt,mobEt,addrEt,passEt;

    Spinner roleSp;

    TextView username,userEmail;

    String emailID;

    Button profileSubmit;

    User selectedUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_edit, container, false);

        nameEt =view.findViewById(R.id.nameEt);
        emailEt =view.findViewById(R.id.emailEt);
        enrolEt =view.findViewById(R.id.enrolEt);
        roleSp =view.findViewById(R.id.roleSp);
        mobEt =view.findViewById(R.id.mobEt);
        addrEt =view.findViewById(R.id.addrEt);
        passEt =view.findViewById(R.id.passEt);

        username =view.findViewById(R.id.username);
        userEmail =view.findViewById(R.id.userEmail);

        profileSubmit =view.findViewById(R.id.profileSubmit);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            emailID = bundle.getString("email", "sahilhry1@gmail.com");
        }

        List<String> roleArray =  new ArrayList<String>();
        roleArray.add("1-Student");
        roleArray.add("2-Teacher");
        roleArray.add("3-Admin");


        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, roleArray);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        roleSp.setAdapter(spinnerAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RMSApi.BaseApiUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setLenient()
                        .create()))
                .build();

        RMSApi service = retrofit.create(RMSApi.class);

        Call<Users> usersCall = service.getUserByMail(emailID);

        usersCall.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (response.body().getSuccess() == 1) {

                            selectedUser = response.body().getUsers().get(0);

                            //set profile details
                            emailEt.setText(selectedUser.getEmailID());
                            passEt.setText("******");
                            enrolEt.setText(selectedUser.getEnrolmentNumber());
                            nameEt.setText(selectedUser.getName());
                            mobEt.setText(selectedUser.getMob());
                            addrEt.setText(selectedUser.getAddress());

                            //Nav header
                            username.setText(selectedUser.getName());
                            userEmail.setText(selectedUser.getEmailID());

                            roleSp.setSelection(Integer.parseInt(selectedUser.getRoleID())-1);



                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG + "getMsg", t.getMessage()+call.request()+call.clone());

            }
        });


        profileSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nameEt.isEnabled())
                {
                    profileSubmit.setText("Save");

                    enrolEt.setEnabled(true);
                    roleSp.setEnabled(true);
                    roleSp.setClickable(true);
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

                    roleSp.setEnabled(true);
                    roleSp.setClickable(true);

                    mobEt.setEnabled(false);
                    addrEt.setEnabled(false);

                    updateProfile(nameEt.getText().toString(),
                            emailEt.getText().toString(),
                            enrolEt.getText().toString(),
                            passEt.getText().toString(),
                            String.valueOf(roleSp.getSelectedItemPosition()+1),
                            mobEt.getText().toString(),
                            addrEt.getText().toString()
                    );

                }

            }
        });




        return view;
    }

    private void updateProfile(String name, String email, String enrol, String pass, String role, String mob, String add) {

        //Update Profile
        if (pass.equals("******"))
        {
            pass=selectedUser.getPassword();
        }

        User user = selectedUser;
        user.setName(name);
        user.setEmailID(email);
        user.setEnrolmentNumber(enrol);
        user.setPassword(pass);
        user.setMob(mob);
        user.setAddress(add);
        user.setAddress(role);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RMSApi.BaseApiUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setLenient()
                        .create()))
                .build();

        RMSApi service = retrofit.create(RMSApi.class);

        Call<Users> call = service.updateUser(name,email,role,pass,enrol,
                user.getCourseId(),mob,add);

        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (response.body().getSuccess() == 1) {
                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {

                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG + "getMsg", t.getMessage()+call.request()+call.clone());

            }
        });


        passEt.setText("******");


    }


}
