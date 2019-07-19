package com.khemsharma.rms;


import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.khemsharma.rms.Models.Exam;
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


public class StuFinalResult extends Fragment {


    private static final String TAG = "RMSFResult";

    View view;

    TextView resEnrol,resScore,resStatus,resDate,resPer,resScholarStatus;

    CardView resultNotDeclared,resDeclaredLayout;

    Button stuFinalReportPdf,stuFinalReportCSV;

    Result result;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.fragment_stu_final_result, container, false);

        stuFinalReportPdf=view.findViewById(R.id.stuFinalReportPdf);
        stuFinalReportCSV=view.findViewById(R.id.stuFinalReportCSV);


        resEnrol=view.findViewById(R.id.resEnrol);
        resScore=view.findViewById(R.id.resScore);
        resStatus=view.findViewById(R.id.resStatus);
        resDate=view.findViewById(R.id.resDate);
        resPer=view.findViewById(R.id.resPer);
        resScholarStatus=view.findViewById(R.id.resScholarStatus);

        resultNotDeclared=view.findViewById(R.id.resultNotDeclared);
        resDeclaredLayout=view.findViewById(R.id.resDeclaredLayout);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RMSApi.BaseApiUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setLenient()
                        .create()))
                .build();

        RMSApi service = retrofit.create(RMSApi.class);

        Call<Result> call=service.getResultByStudent(AppUtils.getCurrentUser(getActivity()).getEnrolmentNumber());

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (response.body().getSuccess() == 1) {

                            resultNotDeclared.setVisibility(View.GONE);

                            resDeclaredLayout.setVisibility(View.VISIBLE);


                            result = response.body();

                            resEnrol.setText("Enrolment:"+result.getResultStudentEnrol());
                            resScore.setText("Grade Score:"+result.getGradeScore());
                            resStatus.setText("Result Status:"+result.getResultStatus());
                            resDate.setText("Date:"+result.getResultDate());
                            resPer.setText("Total Percentage:"+result.getTotalPercentage());
                            resScholarStatus.setText("Scholarship Status:"+result.getScholarshipStatus());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG + "getMessage", t.getMessage()+call.request()+call.clone());
            }
        });

        stuFinalReportPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create a new document
                PdfDocument document = new PdfDocument();

                // crate a page description
                View content = view;
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
                String pdfName = "StudentFinalReport"
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

        stuFinalReportCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                writeToCSV(result);
                Toast.makeText(getActivity(), "File Created Successfully", Toast.LENGTH_SHORT).show();

            }
        });


        return view;
    }


    private static void writeToCSV(Result result)
    {
        try
        {   File outputFile = new File(Environment.getExternalStorageDirectory().getPath()+"/RMS/", "StudentFinalReport.csv");
            outputFile.createNewFile();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));

                StringBuffer oneLine = new StringBuffer();
                oneLine.append(result.getResultId().trim().length() == 0? "" : result.getResultId());
                oneLine.append(AppUtils.CSV_SEPARATOR);
                oneLine.append(result.getResultStudentEnrol().trim().length() == 0? "" : result.getResultStudentEnrol());
                oneLine.append(AppUtils.CSV_SEPARATOR);
                oneLine.append(result.getGradeScore().trim().length() == 0? "" : result.getGradeScore());
                oneLine.append(AppUtils.CSV_SEPARATOR);
                oneLine.append(result.getTotalPercentage().trim().length() == 0? "" : result.getTotalPercentage());
                oneLine.append(AppUtils.CSV_SEPARATOR);
                oneLine.append(result.getResultStatus().trim().length() == 0? "" : result.getResultStatus());
                oneLine.append(AppUtils.CSV_SEPARATOR);
                oneLine.append(result.getScholarshipStatus().trim().length() == 0? "" : result.getScholarshipStatus());
                oneLine.append(AppUtils.CSV_SEPARATOR);
                oneLine.append(result.getResultDate().trim().length() == 0? "" : result.getResultDate());
                bw.write(oneLine.toString());
                bw.newLine();

            bw.flush();
            bw.close();
        }
        catch (UnsupportedEncodingException e) {Log.d("Error:",e.getMessage());}
        catch (FileNotFoundException e){Log.d("Error:",e.getMessage());}
        catch (IOException e){Log.d("Error:",e.getMessage());}
    }


}
