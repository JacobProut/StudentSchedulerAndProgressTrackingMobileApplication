package com.zybooks.studentschedulerandprogresstracking.Adapters.CoursePageAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zybooks.studentschedulerandprogresstracking.Entities.Assessment;
import com.zybooks.studentschedulerandprogresstracking.R;

import java.util.List;

public class CoursePageAssessmentRecyclerViewAdapter extends RecyclerView.Adapter<CoursePageAssessmentRecyclerViewAdapter.ViewHolder> {
    private final Context context;
    private final List<Assessment> assessments;

    public CoursePageAssessmentRecyclerViewAdapter(Context context, List<Assessment> assessments) {
        this.context = context;
        this.assessments = assessments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_assessment_add_display_unassociatedassessments_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Assessment assessment = assessments.get(position);
        holder.assessmentTitle.setText(assessment.getAssessmentTitle());

    }

    @Override
    public int getItemCount() {
        return assessments.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView assessmentTitle;
        ViewHolder(View itemView) {
            super(itemView);
            assessmentTitle = itemView.findViewById(R.id.assessment_title1); // Updated ID
        }
    }
}
