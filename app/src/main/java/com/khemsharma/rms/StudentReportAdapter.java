package com.khemsharma.rms;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khemsharma.rms.Models.Exam;
import com.khemsharma.rms.Models.Result;

import java.util.List;

public class StudentReportAdapter extends RecyclerView.Adapter<StudentReportAdapter.ViewHolder> {

    private List<Result> results;

    public StudentReportAdapter(List<Result> results) {
        this.results = results;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_report_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Result result = results.get(position);

        holder.stuReportEnrol.setText("Enrolment:"+result.getResultStudentEnrol());
        holder.stuReportscore.setText("Grade Score:"+result.getGradeScore());
        holder.stuReportstatus.setText("Result Status:"+result.getResultStatus());
        holder.stuReportDate.setText("Result Date:"+result.getResultDate());



    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView stuReportEnrol,stuReportscore,stuReportstatus,stuReportDate;

        public ViewHolder(View itemView) {
            super(itemView);
            stuReportEnrol=itemView.findViewById(R.id.stuReportEnrol);
            stuReportscore=itemView.findViewById(R.id.stuReportscore);
            stuReportstatus=itemView.findViewById(R.id.stuReportstatus);
            stuReportDate=itemView.findViewById(R.id.stuReportDate);

        }
    }


}
