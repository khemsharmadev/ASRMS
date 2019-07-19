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

import com.khemsharma.rms.Models.Exam;
import com.khemsharma.rms.Models.User;
import com.khemsharma.rms.Models.Users;

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
public class UserManagement extends Fragment {


    private static final String TAG = "RMSUserManage";
    View view;

    RecyclerView userManageView;

    Button userReportPdf,userReportCSV;

    AllUserAdapter allUserAdapter;

    Users users;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_user_management, container, false);

        userManageView = view.findViewById(R.id.userManageView);

        userReportPdf = view.findViewById(R.id.userReportPdf);
        userReportCSV = view.findViewById(R.id.userReportCSV);


        getAllUsers();


        userReportPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create a new document
                PdfDocument document = new PdfDocument();

                // crate a page description
                View content = userManageView;
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
                String pdfName = "UsersReport"
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

        userReportCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeToCSV(users.getUsers());
                Toast.makeText(getActivity(), "File Created Successfully", Toast.LENGTH_SHORT).show();

            }
        });


        return view;
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


                Log.d(TAG ,"isSuccessful"+ response.body().toString());
                if (response.body()!=null)
                    Log.d(TAG + "res", response.body().toString());

                users = response.body();
                // Log.d(TAG + "Message", response.raw().toString());
                // Log.d(TAG + "Message2", response.body().toString());

                if (response.isSuccessful()) {

                    if (response.body() != null)
                    {
                        if (response.body().getSuccess()==1) {
                            allUserAdapter = new AllUserAdapter(getActivity(),response.body().getUsers());

                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

                            userManageView.setLayoutManager(layoutManager);

                            userManageView.setItemAnimator(new DefaultItemAnimator());

                            userManageView.setAdapter(allUserAdapter);
                        }
                    }
                }

               /* assert users != null;
                //for (int i=0;i<users.size();i++)
                for(Users u: users)
                    Log.d(TAG + "Email ID", u.getEmailID());*/

                //Log.d(TAG + "Email ID", users.getEmailID());
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Toast.makeText(getActivity(), "Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG +"onfail",t.getMessage());
                //Log.d(TAG)
            }
        });
    }


    private static void writeToCSV(List<User> userList)
    {
        try
        {   File outputFile = new File(Environment.getExternalStorageDirectory().getPath()+"/RMS/", "UserManagement.csv");
            outputFile.createNewFile();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
            for (User user : userList)
            {
                StringBuffer oneLine = new StringBuffer();
                oneLine.append(user.getUserID().trim().length() == 0? "" : user.getUserID());
                oneLine.append(AppUtils.CSV_SEPARATOR);
                oneLine.append(user.getEmailID().trim().length() == 0? "" : user.getEmailID());
                oneLine.append(AppUtils.CSV_SEPARATOR);
                oneLine.append(user.getName().trim().length() == 0? "" : user.getName());
                oneLine.append(AppUtils.CSV_SEPARATOR);
                oneLine.append(user.getRoleID().trim().length() == 0? "" : user.getRoleID());
                oneLine.append(AppUtils.CSV_SEPARATOR);
                oneLine.append(user.getEnrolmentNumber().trim().length() == 0? "" : user.getEnrolmentNumber());
                oneLine.append(AppUtils.CSV_SEPARATOR);
                oneLine.append(user.getCourseId().trim().length() == 0? "" : user.getCourseId());
                oneLine.append(AppUtils.CSV_SEPARATOR);
                oneLine.append(user.getCourse().get(0).getCourseCode().trim().length() == 0? "" : user.getCourse().get(0).getCourseCode());
                oneLine.append(AppUtils.CSV_SEPARATOR);
                oneLine.append(user.getMob().trim().length() == 0? "" : user.getMob());
                oneLine.append(AppUtils.CSV_SEPARATOR);
                oneLine.append(user.getAddress().trim().length() == 0? "" : user.getAddress());
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
