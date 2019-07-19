package com.khemsharma.rms;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.khemsharma.rms.Models.Assignment;
import com.khemsharma.rms.Models.Subjects;
import com.khemsharma.rms.Models.Users;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadAssignment extends Fragment {


    private static final String TAG = "RMSDownloadAssign";
    View view;

    TextInputEditText assignDownloadEnrol;

    Button enrolSubmit,assignDownload;

    String enrolment;

    LinearLayout subject_layout;

    Spinner subCode;

    TextView assignStatus;

    String downloadUrl,downloadFileName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_download_assignment, container, false);
        assignDownloadEnrol = view.findViewById(R.id.assignDownloadEnrol);
        enrolSubmit = view.findViewById(R.id.enrolSubmit);

        subject_layout = view.findViewById(R.id.subject_layout);

        subCode = view.findViewById(R.id.subCode);

        assignStatus = view.findViewById(R.id.assignStatus);

        assignDownload = view.findViewById(R.id.assignDownload);


        assignDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadingTask().execute();
            }
        });







        enrolSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enrolment = assignDownloadEnrol.getText().toString();


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(RMSApi.BaseApiUrl)
                        .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                                .setLenient()
                                .create()))
                        .build();

                RMSApi service = retrofit.create(RMSApi.class);

                Call<Users> call = service.getStudent(enrolment);
                call.enqueue(new Callback<Users>() {
                    @Override
                    public void onResponse(Call<Users> call, Response<Users> response) {
                        if (response.isSuccessful()) {

                            if (response.body() != null)
                            {
                                if (response.body().getSuccess()==1)
                                {

                                    Toast.makeText(getActivity(), "Spinner Filled", Toast.LENGTH_SHORT).show();
                                    Users users=response.body();
                                    subject_layout.setVisibility(View.VISIBLE);
                                    enrolSubmit.setText("Change");

                                    fillSubject(getActivity(),subCode,users.getUsers().get(0).getCourseId());



                                }
                                else {
                                    Toast.makeText(getActivity(), "Student has't uploaded any assignment.", Toast.LENGTH_SHORT).show();

                                }
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<Users> call, Throwable t) {

                    }
                });


            }
        });


        subCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0){
                    String subject_id;
                    subject_id = parent.getSelectedItem().toString();
                    subject_id= subject_id.substring(0,subject_id.indexOf('-')).trim();
                    getAssignmentStatus(enrolment,subject_id);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        return view;
    }

    private void fillSubject(final Activity activity, final Spinner spinner,String course_id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RMSApi.BaseApiUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setLenient()
                        .create()))
                .build();

        RMSApi service = retrofit.create(RMSApi.class);



        Call<Subjects> call = service.getSubjectList(course_id);

        call.enqueue(new Callback<Subjects>() {
            @Override
            public void onResponse(Call<Subjects> call, Response<Subjects> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null)
                    {
                        if (response.body().getSuccess()==1)
                        {

                            Toast.makeText(activity, "Spinner Filled", Toast.LENGTH_SHORT).show();
                            Subjects courses=response.body();



                            String[] items = new String[courses.getSubjects().size()+1];

                            items[0] = "--Select Course--";
                            for (int i= 0;i<courses.getSubjects().size();i++)
                            {
                                items[i+1] = courses.getSubjects().get(i).getSubjectId() + "-" + courses.getSubjects().get(i).getSubjectCode();

                            }

                            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity,android.R.layout.simple_spinner_item,items);

                            spinner.setAdapter(arrayAdapter);
                        }
                        else {
                            Toast.makeText(activity, "Error Occurred. Please Try again later", Toast.LENGTH_SHORT).show();

                        }
                    }

                }

            }

            @Override
            public void onFailure(Call<Subjects> call, Throwable t) {
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG + "Msg", t.getMessage()+call.request()+call.clone());
            }
        });









    }


    private void getAssignmentStatus(String enrolmentNumber, String subject_id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RMSApi.BaseApiUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setLenient()
                        .create()))
                .build();

        RMSApi service = retrofit.create(RMSApi.class);

        Call<Assignment> call = service.getAssignment(enrolmentNumber,subject_id);

        call.enqueue(new Callback<Assignment>() {
            @Override
            public void onResponse(Call<Assignment> call, Response<Assignment> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null)
                    {
                        if (response.body().getSuccess()==1)
                        {

                            Assignment assignment = response.body();


                            if (assignment.getAssignStatus().equals("1"))
                            {
                                assignStatus.setVisibility(View.GONE);
                                assignDownload.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(), "Approved", Toast.LENGTH_SHORT).show();
                                downloadUrl=assignment.getAssignFilePath();
                                downloadUrl=RMSApi.BaseApiUrl+downloadUrl;
                                downloadFileName=downloadUrl.substring(downloadUrl.lastIndexOf('/')+1, downloadUrl.length());
                            }
                            else {
                                assignStatus.setVisibility(View.VISIBLE);
                                assignDownload.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "Not Approved", Toast.LENGTH_SHORT).show();

                            }


                        }else {
                            Toast.makeText(getActivity(), "Not Uploaded by student", Toast.LENGTH_SHORT).show();
                        }
                    }


                }
            }

            @Override
            public void onFailure(Call<Assignment> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG + "msg", t.getMessage()+call.request()+call.clone());
            }
        });

    }





    private class DownloadingTask extends AsyncTask<Void, Void, Void> {

        File apkStorage = null;
        File outputFile = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getActivity(), "Downloading Start", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (outputFile != null) {
                    Toast.makeText(getActivity(), "Downloaded Successfully", Toast.LENGTH_SHORT).show();
                } else {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 3000);

                    Log.e(TAG, "Download Failed"+downloadUrl+ " "+ downloadFileName );

                }
            } catch (Exception e) {
                e.printStackTrace();

                //Change button text if exception occurs

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 3000);
                Log.e(TAG, "Download Failed with Exception - " + e.getLocalizedMessage());

            }


            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL url = new URL(downloadUrl);//Create Download URl
                HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                c.setRequestMethod("GET");//Set Request Method to "GET" since we are getting data
                c.connect();//connect the URL Connection

                //If Connection response is not OK then show Logs
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
                            + " " + c.getResponseMessage());

                }


                //Get File if SD card is present
                if (new CheckForSDCard().isSDCardPresent()) {

                    apkStorage = new File(
                            Environment.getExternalStorageDirectory() + "/"
                                    + "RMS");
                } else
                    Toast.makeText(getActivity(), "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

                //If File is not present create directory
                if (!apkStorage.exists()) {
                    apkStorage.mkdir();
                    Log.e(TAG, "Directory Created.");
                }

                outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File

                //Create New File if not present
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                    Log.e(TAG, "File Created");
                }

                FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                InputStream is = c.getInputStream();//Get InputStream for connection

                byte[] buffer = new byte[1024];//Set buffer type
                int len1 = 0;//init length
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);//Write new file
                }

                //Close all connection after doing task
                fos.close();
                is.close();

            } catch (Exception e) {

                //Read exception if something went wrong
                e.printStackTrace();
                outputFile = null;
                Log.e(TAG, "Download Error Exception " + e.getMessage());
            }



            return null;
        }
    }



}
