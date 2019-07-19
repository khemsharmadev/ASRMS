package com.khemsharma.rms;

import com.khemsharma.rms.Models.Assignment;
import com.khemsharma.rms.Models.Courses;
import com.khemsharma.rms.Models.Exam;
import com.khemsharma.rms.Models.Exams;
import com.khemsharma.rms.Models.Result;
import com.khemsharma.rms.Models.Results;
import com.khemsharma.rms.Models.Subjects;
import com.khemsharma.rms.Models.Users;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RMSApi {

    public static String BaseApiUrl= "http://192.168.0.105/rms/";
    //public static String BaseApiUrl= "https://api.learn2crack.com/android/";


    @GET("get_all_users.php")
    Call<Users> getUsersList();

    //@HTTP(method = "DELETE", path = RMSApi.BaseApiUrl + "/login", hasBody = true)

    @FormUrlEncoded
    @POST("login_user.php")
    Call<Users> userLogin(
            @Field("email_ID") String email_ID,
            @Field("password") String password

    );

    @FormUrlEncoded
        @POST("get_user_by_email.php")
    Call<Users> getUserByMail(
            @Field("email_ID") String email_ID

    );

    @FormUrlEncoded
    @POST("register_user.php")
    Call<Users> registerUser(
            @Field("name") String name,
            @Field("email_ID") String email_ID,
            @Field("role_ID") String role_ID,
            @Field("password") String password,
            @Field("enrolment_number") String enrolment_number,
            @Field("course_id") String course_id,
            @Field("mob") String mob,
            @Field("address") String address

    );

    @FormUrlEncoded
    @POST("update_user.php")
    Call<Users> updateUser(
            @Field("name") String name,
            @Field("email_ID") String email_ID,
            @Field("role_ID") String role_ID,
            @Field("password") String password,
            @Field("enrolment_number") String enrolment_number,
            @Field("course_id") String course_id,
            @Field("mob") String mob,
            @Field("address") String address

    );

    @GET("get_all_courses.php")
    Call<Courses> getCourseList();

    @FormUrlEncoded
    @POST("get_subjects.php")
    Call<Subjects> getSubjectList(@Field("course_id") String course_id);

    @FormUrlEncoded
    @POST("get_assignment.php")
    Call<Assignment> getAssignment(@Field("enrolment_number") String enrolment_number, @Field("subject_id") String subject_id);


    @Multipart
    @POST("file_upload.php")
    Call<Assignment> uploadAssignment(@Part MultipartBody.Part file, @Part("assign_stu_enrol") RequestBody assign_stu_enrol, @Part("subject_id") RequestBody subject_id);



    @FormUrlEncoded
    @POST("get_student.php")
    Call<Users> getStudent(
                    @Field("enrolment_number") String enrolment_number
            );


    @FormUrlEncoded
    @POST("get_exam.php")
    Call<Exam> getExam(@Field("enrolment_number") String enrolment_number, @Field("subject_id") String subject_id);


    @FormUrlEncoded
    @POST("add_exam_marks.php")
    Call<Exam> addExam(
                    @Field("exam_student_enrol") String exam_student_enrol,
                    @Field("exam_subject_id") String exam_subject_id,
                    @Field("exam_marks") String exam_marks,
                    @Field("exam_status") String exam_status,
                    @Field("assignment_marks") String assignment_marks,
                    @Field("exam_date") String exam_date

            );

    @FormUrlEncoded
    @POST("get_all_exam_by_student.php")
    Call<Exams> getExamByStudent(
            @Field("enrolment_number") String enrolment_number
    );

    @FormUrlEncoded
    @POST("get_result_by_student.php")
    Call<Result> getResultByStudent(
                    @Field("enrolment_number") String enrolment_number
            );

    @FormUrlEncoded
    @POST("genrate_result.php")
    Call<Result> genrateResult(
                    @Field("enrolment_number") String enrolment_number,
                    @Field("course_id") String course_id,
                    @Field("date") String date
            );

    @FormUrlEncoded
    @POST("update_exam_marks.php")
    Call<Exam> updateExam(
            @Field("exam_student_enrol") String exam_student_enrol,
            @Field("exam_subject_id") String exam_subject_id,
            @Field("exam_marks") String exam_marks,
            @Field("exam_status") String exam_status,
            @Field("assignment_marks") String assignment_marks,
            @Field("exam_date") String exam_date

    );

    @GET("get_all_result.php")
    Call<Results> getAllResult();







}
