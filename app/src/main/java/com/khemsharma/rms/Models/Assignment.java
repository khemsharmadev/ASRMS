package com.khemsharma.rms.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Assignment {

    @SerializedName("assignment_id")
    @Expose
    private String assignmentId;
    @SerializedName("assign_stu_enrol")
    @Expose
    private String assignStuEnrol;
    @SerializedName("subject_id")
    @Expose
    private String subjectId;
    @SerializedName("assign_status")
    @Expose
    private String assignStatus;
    @SerializedName("assign_file_path")
    @Expose
    private String assignFilePath;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;

    public String getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getAssignStuEnrol() {
        return assignStuEnrol;
    }

    public void setAssignStuEnrol(String assignStuEnrol) {
        this.assignStuEnrol = assignStuEnrol;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getAssignStatus() {
        return assignStatus;
    }

    public void setAssignStatus(String assignStatus) {
        this.assignStatus = assignStatus;
    }

    public String getAssignFilePath() {
        return assignFilePath;
    }

    public void setAssignFilePath(String assignFilePath) {
        this.assignFilePath = assignFilePath;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}