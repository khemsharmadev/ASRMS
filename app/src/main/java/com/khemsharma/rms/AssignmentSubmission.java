package com.khemsharma.rms;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.khemsharma.rms.Models.Assignment;
import com.khemsharma.rms.Models.Courses;
import com.khemsharma.rms.Models.Subjects;
import com.khemsharma.rms.Models.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AssignmentSubmission extends AppCompatActivity {

    private static final String TAG ="RMSAssignment" ;
    View view;

    Spinner assignCode;

    Button assignFileGraber;

    TextView assignStatus;

    private String selectedImagePath;

    private String saq;
    private String asd;
    private String sdd;
    private String uid;
    private String sss;
    private String fff;
    private String nnn;
    private String subject_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_submission);

        assignFileGraber =findViewById(R.id.assignFileGraber);
        assignCode = findViewById(R.id.assignCode);
        assignStatus = findViewById(R.id.assignStatus);

        assignFileGraber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //File upload
                Intent intent;
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, 7);









            }
        });



        fillSubject(this,assignCode);



        assignCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0){
                    subject_id = parent.getSelectedItem().toString();
                    subject_id= subject_id.substring(0,subject_id.indexOf('-')).trim();
                    getAssignmentStatus(AppUtils.getCurrentUser(getApplicationContext()).getEnrolmentNumber(),subject_id);
                    Toast.makeText(getApplicationContext(), "Selcected"+subject_id, Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

                            assignStatus.setVisibility(View.VISIBLE);
                            assignFileGraber.setVisibility(View.GONE);



                        }else {
                            assignStatus.setVisibility(View.GONE);
                            assignFileGraber.setVisibility(View.VISIBLE);
                        }
                    }


                }
            }

            @Override
            public void onFailure(Call<Assignment> call, Throwable t) {
                Toast.makeText(AssignmentSubmission.this, t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG + "getMessage", t.getMessage()+call.request()+call.clone());
            }
        });

    }


    private void fillSubject(final Activity activity, final Spinner spinner) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RMSApi.BaseApiUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setLenient()
                        .create()))
                .build();

        RMSApi service = retrofit.create(RMSApi.class);



        Call<Subjects> call = service.getSubjectList(AppUtils.getCurrentUser(getApplicationContext()).getCourseId());

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
                Log.d(TAG + "getMessage", t.getMessage()+call.request()+call.clone());
            }
        });





    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch(requestCode){

            case 7:

                if(resultCode==RESULT_OK) {

                    Uri selectedImageUri = data.getData();

                    //Getting image path
                    //selectedImagePath = getPath(selectedImageUri);

                    Toast.makeText(AssignmentSubmission.this, selectedImagePath, Toast.LENGTH_LONG).show();


                    String filePath = getRealPathFromUri(data.getData());
                    if (filePath != null && !filePath.isEmpty()) {
                        File file = new File(filePath);
                        if (file.exists()) {
                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(RMSApi.BaseApiUrl)
                                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                                            .setLenient()
                                            .create()))
                                    .build();

                            RMSApi service = retrofit.create(RMSApi.class);

                            RequestBody fbody = RequestBody.create(MediaType.parse("image/*"), file);

                            User user = AppUtils.getCurrentUser(this);

                            //Change
                            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);


                            RequestBody enr = RequestBody.create(MediaType.parse("text/plain"), user.getEnrolmentNumber());
                            RequestBody id = RequestBody.create(MediaType.parse("text/plain"), subject_id);
                            Call<Assignment> call = service.uploadAssignment(body, enr, id);

                            call.enqueue(new Callback<Assignment>() {
                                @Override
                                public void onResponse(Call<Assignment> call, Response<Assignment> response) {
                                    if (response.isSuccessful()) {

                                        if (response.body() != null) {
                                            if (response.body().getSuccess() == 1) {


                                                assignStatus.setVisibility(View.VISIBLE);
                                                assignFileGraber.setVisibility(View.GONE);


                                                Assignment assignment = response.body();
                                                Log.d(TAG + "PathFile", assignment.getMessage());
                                                Toast.makeText(AssignmentSubmission.this, assignment.getMessage(), Toast.LENGTH_SHORT).show();


                                            } else {
                                                Toast.makeText(AssignmentSubmission.this, "Error Occurred. Please Try again later", Toast.LENGTH_SHORT).show();

                                            }
                                        }

                                    }
                                }

                                @Override
                                public void onFailure(Call<Assignment> call, Throwable t) {
                                    Log.i(TAG+"S", "Fail");
                                    Toast.makeText(AssignmentSubmission.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                    Log.d(TAG + "getMessage", t.getMessage() + call.request() + call.clone());
                                }
                            });


        }}}}}


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AssignmentSubmission.this,MainActivity.class);
        startActivity(intent);
        finish();

    }


    public String getRealPathFromUri(final Uri uri) {
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(getApplicationContext(), uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(getApplicationContext(), contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(getApplicationContext(), contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(getApplicationContext(), uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}

