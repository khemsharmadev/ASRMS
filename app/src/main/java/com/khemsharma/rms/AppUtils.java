package com.khemsharma.rms;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.khemsharma.rms.Models.Course;
import com.khemsharma.rms.Models.User;

import java.util.ArrayList;
import java.util.List;

public class AppUtils {

    private static String NULL = "NA";
    public static final String CSV_SEPARATOR = ",";
    private static SharedPreferences sharedPreferences;

    public static void saveUserInfo(Context context,User user)
    {
        if (sharedPreferences==null)
            sharedPreferences = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();

        editor.putString("UserID",user.getUserID());
        editor.putString("name",user.getName());
        editor.putString("EmailID",user.getEmailID());
        editor.putString("RoleID",user.getRoleID());
        editor.putString("password",user.getPassword());
        editor.putString("EnrolNum",user.getEnrolmentNumber());
        editor.putString("MobNum",user.getMob());
        editor.putString("Address",user.getAddress());
        if (user.getCourse()!=null)
        {
            editor.putString("Course",user.getCourse().get(0).getCourseCode());
            editor.putString("CourseID",user.getCourseId());
        }

        editor.putBoolean("UserLogin",true);
        editor.commit();
    }

    public static User getCurrentUser(Context context){
        if (sharedPreferences==null)
            sharedPreferences = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        User user = new User();
        user.setUserID(sharedPreferences.getString("UserID",NULL));
        user.setName(sharedPreferences.getString("name",NULL));
        user.setEmailID(sharedPreferences.getString("EmailID",NULL));
        user.setRoleID(sharedPreferences.getString("RoleID",NULL));
        user.setPassword(sharedPreferences.getString("password",NULL));
        user.setEnrolmentNumber(sharedPreferences.getString("EnrolNum",NULL));
        user.setMob(sharedPreferences.getString("MobNum",NULL));
        user.setCourseId(sharedPreferences.getString("CourseID",NULL));
        user.setAddress(sharedPreferences.getString("Address",NULL));


        List<Course> course = new ArrayList<>();
        course.add(new Course(sharedPreferences.getString("CourseID",NULL)
                ,sharedPreferences.getString("Course",NULL)
        ));
        user.setCourse(course);


        return user;
    }

    public static Boolean isUserLogin(Context context){
        if (sharedPreferences==null)
            sharedPreferences = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("UserLogin",false);
    }

    public static void logOut(Context context){
        if (sharedPreferences==null)
            sharedPreferences = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.clear();
        editor.commit();

    }
}
