package com.khemsharma.rms;


import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.khemsharma.rms.Models.Result;
import com.khemsharma.rms.Models.Results;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class StudentsReport extends Fragment {

    private static final String TAG = "RMSStudentReport";
    View view;

    Button finalReportPdf,finalReportCSV;

    RecyclerView stuReportView;

    StudentReportAdapter studentReportAdapter;

    Results results;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_students_report, container, false);

        stuReportView=view.findViewById(R.id.stuReportView);

        finalReportPdf=view.findViewById(R.id.finalReportPdf);
        finalReportCSV=view.findViewById(R.id.finalReportCSV);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RMSApi.BaseApiUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setLenient()
                        .create()))
                .build();

        RMSApi service = retrofit.create(RMSApi.class);

        Call<Results> call = service.getAllResult();

        call.enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null)
                    {
                        if (response.body().getSuccess()==1)
                        {

                            results = response.body();

                            studentReportAdapter = new StudentReportAdapter(response.body().getResults());

                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

                            stuReportView.setLayoutManager(layoutManager);

                            stuReportView.setItemAnimator(new DefaultItemAnimator());

                            stuReportView.setAdapter(studentReportAdapter);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Results> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG + "Msg", t.getMessage()+call.request()+call.clone());
            }
        });

        finalReportPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create a new document
                PdfDocument document = new PdfDocument();

                // crate a page description
                View content = stuReportView;
                int pageNumber = 1;
                //PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(new Rect(0, 0, 100, 100), 1).create();
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(content.getWidth(),
                        content.getHeight() - 20, pageNumber).create();
                // start a page
                PdfDocument.Page page = document.startPage(pageInfo);

                // draw something on the page
                content.draw(page.getCanvas());

                // finish the page
                document.finishPage(page);

                // add more pages

                // write the document contentgetOutputStream
                // saving pdf document to sdcard
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss");
                String pdfName = "AllStudentsReport"
                        + sdf.format(Calendar.getInstance().getTime()) + ".pdf";

// all created files will be saved at path /sdcard/PDFDemo_AndroidSRC/
                File outputFile = new File(Environment.getExternalStorageDirectory().getPath()+"/RMS/", pdfName);

                try {
                    outputFile.createNewFile();
                    OutputStream out = new FileOutputStream(outputFile);
                    document.writeTo(out);
                    document.close();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                // close the document
                document.close();
                Toast.makeText(getActivity(), "File Created Successfully", Toast.LENGTH_SHORT).show();

            }
        });


        finalReportCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeToCSV(results.getResults());
                Toast.makeText(getActivity(), "File Created Successfully", Toast.LENGTH_SHORT).show();

            }
        });



        return view;
    }


    private static void writeToCSV(List<Result> resultsList)
    {
        try
        {   File outputFile = new File(Environment.getExternalStorageDirectory().getPath()+"/RMS/", "AllStudentsReport.csv");
            outputFile.createNewFile();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
            for (Result results : resultsList)
            {
                StringBuffer oneLine = new StringBuffer();
                oneLine.append(results.getResultId().trim().length() == 0? "" : results.getResultId());
                oneLine.append(AppUtils.CSV_SEPARATOR);
                oneLine.append(results.getResultStudentEnrol().trim().length() == 0? "" : results.getResultStudentEnrol());
                oneLine.append(AppUtils.CSV_SEPARATOR);
                oneLine.append(results.getGradeScore().trim().length() == 0? "" : results.getGradeScore());
                oneLine.append(AppUtils.CSV_SEPARATOR);
                oneLine.append(results.getTotalPercentage().trim().length() == 0? "" : results.getTotalPercentage());
                oneLine.append(AppUtils.CSV_SEPARATOR);
                oneLine.append(results.getResultStatus().trim().length() == 0? "" : results.getResultStatus());
                oneLine.append(AppUtils.CSV_SEPARATOR);
                oneLine.append(results.getScholarshipStatus().trim().length() == 0? "" : results.getScholarshipStatus());
                oneLine.append(AppUtils.CSV_SEPARATOR);
                oneLine.append(results.getResultDate().trim().length() == 0? "" : results.getResultDate());
                bw.write(oneLine.toString());
                bw.newLine();
            }
            bw.flush();
            bw.close();
        }
        catch (UnsupportedEncodingException e) {Log.d("Error:",e.getMessage());}
        catch (FileNotFoundException e){Log.d("Error:",e.getMessage());}
        catch (IOException e){Log.d("Error:",e.getMessage());}
    }

}
