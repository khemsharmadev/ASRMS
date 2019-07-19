package com.khemsharma.rms;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khemsharma.rms.Models.Exam;

import java.util.List;

public class PercentageReportAdapter extends RecyclerView.Adapter<PercentageReportAdapter.ViewHolder> {

    private List<Exam> exams;

    public PercentageReportAdapter(List<Exam> exams) {
        this.exams = exams;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.percentage_report_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Exam exam = exams.get(position);

        holder.code.setText(exam.getSubject().get(0).getSubjectCode());

        int assignMarks =Integer.parseInt(exam.getAssignmentMarks());
        int examMarks =Integer.parseInt(exam.getExamMarks());
        String percentage = String.format("%.2f",(0.30*assignMarks)+(0.7*examMarks));
        holder.percentage_tv.setText("Percentage:"+percentage+"%");

    }

    @Override
    public int getItemCount() {
        return exams.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView code,percentage_tv;

        public ViewHolder(View itemView) {
            super(itemView);
            code=itemView.findViewById(R.id.perRowCode);
            percentage_tv=itemView.findViewById(R.id.perTv);

        }
    }


}
