package com.khemsharma.rms;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khemsharma.rms.Models.Exam;

import java.util.List;

public class GradeCardAdapter extends RecyclerView.Adapter<GradeCardAdapter.ViewHolder> {

    private List<Exam> exams;

    public GradeCardAdapter(List<Exam> exams) {
        this.exams = exams;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grade_card_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Exam exam = exams.get(position);

        holder.code.setText(exam.getSubject().get(0).getSubjectCode());
        holder.assignment_marks.setText("Assignment:"+exam.getAssignmentMarks());
        holder.exam_marks.setText("Exam:"+exam.getExamMarks());
    }

    @Override
    public int getItemCount() {
        return exams.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView code,assignment_marks,exam_marks;

        public ViewHolder(View itemView) {
            super(itemView);
            code=itemView.findViewById(R.id.a1);
            assignment_marks=itemView.findViewById(R.id.a2);
            exam_marks=itemView.findViewById(R.id.a3);

        }
    }


}
