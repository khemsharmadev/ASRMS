package com.khemsharma.rms;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.khemsharma.rms.Models.Exam;
import com.khemsharma.rms.Models.Result;
import com.khemsharma.rms.Models.Subjects;
import com.khemsharma.rms.Models.Users;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddMarks extends Fragment {

    private static final String TAG = "RMSAddMarks";
    View view;

    TextInputEditText examEnrol;

    Button enrolSubmit,marksSubmit;

    String enrolment,assignmentMarksVal,examMarksVal;

    LinearLayout subject_layout;

    Spinner subCode;

    TextView examMarksStatus;

    EditText assignMarks,examMarks;

    String subject_id,course_id;

    SimpleDateFormat sdf;

    String date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_marks, container, false);

        examEnrol = view.findViewById(R.id.examEnrol);
        enrolSubmit = view.findViewById(R.id.enrolSubmit);
        marksSubmit = view.findViewById(R.id.marksSubmit);
        subCode = view.findViewById(R.id.subCode);
        examMarksStatus = view.findViewById(R.id.examMarksStatus);
        subject_layout = view.findViewById(R.id.subject_layout);

        assignMarks = view.findViewById(R.id.assignMarks);
        examMarks = view.findViewById(R.id.examMarks);


        enrolSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enrolment = examEnrol.getText().toString();


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
                                    course_id=users.getUsers().get(0).getCourseId();
                                    fillSubject(getActivity(),subCode,course_id);



                                }
                                else {
                                    Toast.makeText(getActivity(), "Student has't uploaded any assignment.", Toast.LENGTH_SHORT).show();

                                }
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<Users> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d(TAG + "Msg", t.getMessage()+call.request()+call.clone());
                    }
                });


            }
        });


        subCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0){

                    subject_id = parent.getSelectedItem().toString();
                    subject_id= subject_id.substring(0,subject_id.indexOf('-')).trim();
                    isExamAdded(enrolment,subject_id);





                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        marksSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignmentMarksVal=assignMarks.getText().toString();
                examMarksVal=examMarks.getText().toString();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(RMSApi.BaseApiUrl)
                        .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                                .setLenient()
                                .create()))
                        .build();

                String exam_status;
                if (Integer.parseInt(examMarksVal)<40||Integer.parseInt(assignmentMarksVal)<40)
                {
                    exam_status="Fail";
                }else exam_status="Pass";


                sdf = new SimpleDateFormat("yyyy/MM/dd");
                date = sdf.format(new Date());

                RMSApi service = retrofit.create(RMSApi.class);

                Call<Exam> call = service.addExam(enrolment,subject_id,examMarksVal,exam_status,assignmentMarksVal,date);

                call.enqueue(new Callback<Exam>() {
                    @Override
                    public void onResponse(Call<Exam> call, Response<Exam> response) {
                        if (response.body()!=null)
                            if (response.body().getSuccess()==1)
                            {
                                Toast.makeText(getActivity(), "Data added Successfully", Toast.LENGTH_SHORT).show();
                                resultGenrate(enrolment,course_id,date);

                            }else  {
                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                    }

                    @Override
                    public void onFailure(Call<Exam> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d(TAG + "Msg", t.getMessage()+call.request()+call.clone());
                    }
                });
            }
        });









        //End of oncreate



        return view;
    }

    private void resultGenrate(String enrolment, String course_id, String date) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RMSApi.BaseApiUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setLenient()
                        .create()))
                .build();
        RMSApi service = retrofit.create(RMSApi.class);

        Call<Result> call = service.genrateResult(enrolment,course_id,date);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body()!=null)
                    if (response.body().getSuccess()==1){
                        Toast.makeText(getActivity(), "Result Added", Toast.LENGTH_SHORT).show();
                    }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG + "Msg", t.getMessage()+call.request()+call.clone());
            }
        });
    }

    private void  isExamAdded(String enrolment, String subject_id) {

        final Boolean[] isExamExist = {false};

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RMSApi.BaseApiUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setLenient()
                        .create()))
                .build();

        RMSApi service = retrofit.create(RMSApi.class);

        Call<Exam> call = service.getExam(enrolment,subject_id);

        call.enqueue(new Callback<Exam>() {
            @Override
            public void onResponse(Call<Exam> call, Response<Exam> response) {
                if (response.body()!=null)
                if (response.body().getSuccess()==1)
                {
                    examMarksStatus.setVisibility(View.VISIBLE);
                    assignMarks.setVisibility(View.GONE);
                    examMarks.setVisibility(View.GONE);
                    marksSubmit.setVisibility(View.GONE);


                }
                else {
                    examMarksStatus.setVisibility(View.GONE);
                    assignMarks.setVisibility(View.VISIBLE);
                    examMarks.setVisibility(View.VISIBLE);
                    marksSubmit.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Exam> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG + "Msg", t.getMessage()+call.request()+call.clone());
            }
        });

    }


    private void fillSubject(final Activity activity, final Spinner spinner, String course_id) {

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



}
