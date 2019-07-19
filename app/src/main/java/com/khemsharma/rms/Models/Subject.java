package com.khemsharma.rms.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Subject {

    @SerializedName("subject_id")
    @Expose
    private String subjectId;
    @SerializedName("subject_code")
    @Expose
    private String subjectCode;
    @SerializedName("subject_des")
    @Expose
    private String subjectDes;
    @SerializedName("course_id")
    @Expose
    private String courseId;

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectDes() {
        return subjectDes;
    }

    public void setSubjectDes(String subjectDes) {
        this.subjectDes = subjectDes;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

}
