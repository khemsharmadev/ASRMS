package com.khemsharma.rms.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Course {

    public Course(String courseId, String courseCode) {
        this.courseId = courseId;
        this.courseCode = courseCode;
    }

    public Course(String courseId, String courseCode, String courseDes) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseDes = courseDes;
    }

    @SerializedName("course_id")
    @Expose
    private String courseId;
    @SerializedName("course_code")
    @Expose
    private String courseCode;
    @SerializedName("course_des")
    @Expose
    private String courseDes;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseDes() {
        return courseDes;
    }

    public void setCourseDes(String courseDes) {
        this.courseDes = courseDes;
    }

}