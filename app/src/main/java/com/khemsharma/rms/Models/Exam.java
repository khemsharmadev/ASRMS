package com.khemsharma.rms.Models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Exam {

    @SerializedName("exam_id")
    @Expose
    private String examId;
    @SerializedName("exam_student_enrol")
    @Expose
    private String examStudentEnrol;
    @SerializedName("exam_subject_id")
    @Expose
    private String examSubjectId;
    @SerializedName("exam_marks")
    @Expose
    private String examMarks;
    @SerializedName("exam_status")
    @Expose
    private String examStatus;
    @SerializedName("assignment_marks")
    @Expose
    private String assignmentMarks;
    @SerializedName("exam_date")
    @Expose
    private String examDate;
    @SerializedName("subject")
    @Expose
    private List<Subject> subject = null;

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getExamStudentEnrol() {
        return examStudentEnrol;
    }

    public void setExamStudentEnrol(String examStudentEnrol) {
        this.examStudentEnrol = examStudentEnrol;
    }

    public String getExamSubjectId() {
        return examSubjectId;
    }

    public void setExamSubjectId(String examSubjectId) {
        this.examSubjectId = examSubjectId;
    }

    public String getExamMarks() {
        return examMarks;
    }

    public void setExamMarks(String examMarks) {
        this.examMarks = examMarks;
    }

    public String getExamStatus() {
        return examStatus;
    }

    public void setExamStatus(String examStatus) {
        this.examStatus = examStatus;
    }

    public String getAssignmentMarks() {
        return assignmentMarks;
    }

    public void setAssignmentMarks(String assignmentMarks) {
        this.assignmentMarks = assignmentMarks;
    }

    public String getExamDate() {
        return examDate;
    }

    public void setExamDate(String examDate) {
        this.examDate = examDate;
    }

    public List<Subject> getSubject() {
        return subject;
    }

    public void setSubject(List<Subject> subject) {
        this.subject = subject;
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