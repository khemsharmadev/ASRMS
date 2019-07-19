package com.khemsharma.rms;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.khemsharma.rms.Models.Exam;
import com.khemsharma.rms.Models.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class StuScholarshipStatus extends Fragment {


    private static final String TAG = "RMSScholarship";
    View view;
    TextView scholarshipStatus;
    CardView scholarshipCardbg;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stu_scholarship_status, container, false);

        scholarshipStatus=view.findViewById(R.id.scholarshipStatus);
        scholarshipCardbg=view.findViewById(R.id.scholarshipCardbg);

        resultGenrate(AppUtils.getCurrentUser(getActivity()).getEnrolmentNumber());



        return view;
    }

    private void resultGenrate(String enrolment) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RMSApi.BaseApiUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setLenient()
                        .create()))
                .build();
        RMSApi service = retrofit.create(RMSApi.class);

        Call<Result> call = service.getResultByStudent(enrolment);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body()!=null)
                    if (response.body().getSuccess()==1){
                        if (response.body().getScholarshipStatus().equals("Approved"))
                        {
                            scholarshipStatus.setText("Scholarship Approved");

                        }
                        else
                        {
                            scholarshipStatus.setText("Scholarship Not Approved");
                            scholarshipCardbg.setCardBackgroundColor(Color.RED);
                        }

                    }

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG + "Msg", t.getMessage()+call.request()+call.clone());
            }
        });
    }



}
