package com.khemsharma.rms.Models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {

    @SerializedName("user_ID")
    @Expose
    private String userID;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email_ID")
    @Expose
    private String emailID;
    @SerializedName("course_id")
    @Expose
    private String courseId;
    @SerializedName("role_ID")
    @Expose
    private String roleID;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("enrolment_number")
    @Expose
    private String enrolmentNumber;
    @SerializedName("mob")
    @Expose
    private String mob;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("course")
    @Expose
    private List<Course> course = null;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEnrolmentNumber() {
        return enrolmentNumber;
    }

    public void setEnrolmentNumber(String enrolmentNumber) {
        this.enrolmentNumber = enrolmentNumber;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Course> getCourse() {
        return course;
    }

    public void setCourse(List<Course> course) {
        this.course = course;
    }

}