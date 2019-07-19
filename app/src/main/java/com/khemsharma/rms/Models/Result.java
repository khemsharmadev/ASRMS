package com.khemsharma.rms.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("result_id")
    @Expose
    private String resultId;
    @SerializedName("result_student_enrol")
    @Expose
    private String resultStudentEnrol;
    @SerializedName("grade_score")
    @Expose
    private String gradeScore;
    @SerializedName("result_status")
    @Expose
    private String resultStatus;
    @SerializedName("result_date")
    @Expose
    private String resultDate;
    @SerializedName("total_percentage")
    @Expose
    private String totalPercentage;
    @SerializedName("scholarship_status")
    @Expose
    private String scholarshipStatus;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    public String getResultStudentEnrol() {
        return resultStudentEnrol;
    }

    public void setResultStudentEnrol(String resultStudentEnrol) {
        this.resultStudentEnrol = resultStudentEnrol;
    }

    public String getGradeScore() {
        return gradeScore;
    }

    public void setGradeScore(String gradeScore) {
        this.gradeScore = gradeScore;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getResultDate() {
        return resultDate;
    }

    public void setResultDate(String resultDate) {
        this.resultDate = resultDate;
    }

    public String getTotalPercentage() {
        return totalPercentage;
    }

    public void setTotalPercentage(String totalPercentage) {
        this.totalPercentage = totalPercentage;
    }

    public String getScholarshipStatus() {
        return scholarshipStatus;
    }

    public void setScholarshipStatus(String scholarshipStatus) {
        this.scholarshipStatus = scholarshipStatus;
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
