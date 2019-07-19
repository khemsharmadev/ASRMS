package com.khemsharma.rms;


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
import com.khemsharma.rms.Models.Exam;
import com.khemsharma.rms.Models.Exams;
import com.khemsharma.rms.Models.Result;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class StuPercentageReport extends Fragment {


    View view;

    private static final String TAG ="RMSPercentage" ;


    RecyclerView stuPercentageRecycleView;

    PercentageReportAdapter percentageReportAdapter;

    Button perReportPdf,perReportCSV;

    Exams exams;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_stu_percentage_report, container, false);

        stuPercentageRecycleView=view.findViewById(R.id.stuPercentageRecycleView);

        perReportPdf=view.findViewById(R.id.perReportPdf);
        perReportCSV=view.findViewById(R.id.perReportCSV);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RMSApi.BaseApiUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setLenient()
                        .create()))
                .build();

        RMSApi service = retrofit.create(RMSApi.class);

        Call<Exams> call = service.getExamByStudent(AppUtils.getCurrentUser(getActivity()).getEnrolmentNumber());

        call.enqueue(new Callback<Exams>() {
            @Override
            public void onResponse(Call<Exams> call, Response<Exams> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null)
                    {
                        if (response.body().getSuccess()==1) {

                            percentageReportAdapter = new PercentageReportAdapter(response.body().getExams());

                            exams=response.body();

                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

                            stuPercentageRecycleView.setLayoutManager(layoutManager);

                            stuPercentageRecycleView.setItemAnimator(new DefaultItemAnimator());

                            stuPercentageRecycleView.setAdapter(percentageReportAdapter);

                        }
                    }


                }
            }

            @Override
            public void onFailure(Call<Exams> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG + "getMessage", t.getMessage()+call.request()+call.clone());
            }
        });

        perReportPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create a new document
                PdfDocument document = new PdfDocument();

                // crate a page description
                View content = stuPercentageRecycleView;
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
                String pdfName = "StuPercentageReport"
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


        perReportCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeToCSV(exams.getExams());
                Toast.makeText(getActivity(), "File Created Successfully", Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }




    private static void writeToCSV(List<Exam> examsList)
    {
        try
        {   File outputFile = new File(Environment.getExternalStorageDirectory().getPath()+"/RMS/", "StuPercentageReport.csv");
            outputFile.createNewFile();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
            for (Exam exams : examsList)
            {
                StringBuffer oneLine = new StringBuffer();
                oneLine.append(exams.getExamId().trim().length() == 0? "" : exams.getExamId());
                oneLine.append(AppUtils.CSV_SEPARATOR);
                oneLine.append(exams.getExamStudentEnrol().trim().length() == 0? "" : exams.getExamStudentEnrol());
                oneLine.append(AppUtils.CSV_SEPARATOR);
                oneLine.append(exams.getSubject().get(0).getSubjectCode().trim().length() == 0? "" : exams.getSubject().get(0).getSubjectCode());
                oneLine.append(AppUtils.CSV_SEPARATOR);
                oneLine.append(exams.getAssignmentMarks().trim().length() == 0? "" : exams.getAssignmentMarks());
                oneLine.append(AppUtils.CSV_SEPARATOR);
                oneLine.append(exams.getExamMarks().trim().length() == 0? "" : exams.getExamMarks());
                oneLine.append(AppUtils.CSV_SEPARATOR);
                oneLine.append(exams.getExamStatus().trim().length() == 0? "" : exams.getExamStatus());
                oneLine.append(AppUtils.CSV_SEPARATOR);
                oneLine.append(exams.getExamDate().trim().length() == 0? "" : exams.getExamDate());



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
