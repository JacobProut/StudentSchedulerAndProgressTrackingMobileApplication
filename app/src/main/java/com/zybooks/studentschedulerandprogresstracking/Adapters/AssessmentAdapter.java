package com.zybooks.studentschedulerandprogresstracking.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zybooks.studentschedulerandprogresstracking.Entities.Assessment;
import com.zybooks.studentschedulerandprogresstracking.R;
import com.zybooks.studentschedulerandprogresstracking.UI.AssessmentUI.AssessmentAddPage;

import java.util.List;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentViewHolder> {
    private List<Assessment> mAssessments;
    private final Context context;
    private final LayoutInflater mInflater;

    class AssessmentViewHolder extends RecyclerView.ViewHolder {
        private final TextView assessmentItemView;
        private final TextView assessmentItemView2;

        private AssessmentViewHolder(View itemView) {
            super(itemView);
            assessmentItemView = itemView.findViewById(R.id.assessment_list_textView);
            assessmentItemView2 = itemView.findViewById(R.id.assessment_list_textView2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Assessment current = mAssessments.get(position);
                    Intent intent = new Intent(context, AssessmentAddPage.class);
                    intent.putExtra("id", current.getAssessmentId());
                    intent.putExtra("title", current.getAssessmentTitle());
                    intent.putExtra("startDate", current.getStartDate());
                    intent.putExtra("endDate", current.getEndDate());
                    intent.putExtra("status", current.getSpinnerOAorPA());
                    intent.putExtra("courseId", current.getCourseId());
                    context.startActivity(intent);
                }
            });
        }
    }

    public AssessmentAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= mInflater.inflate(R.layout.assessment_list_item,parent,false);
        return new AssessmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentViewHolder holder, int position) {
        if (mAssessments != null) {
            Assessment current = mAssessments.get(position);
            String title = current.getAssessmentTitle();
            int assessmentId = current.getAssessmentId();
            holder.assessmentItemView.setText(title);
            holder.assessmentItemView2.setText(Integer.toString(assessmentId) + ".");
        }
        else {
            holder.assessmentItemView.setText("No Assessment Title");
            holder.assessmentItemView2.setText("No course id");
        }
    }

    @Override
    public int getItemCount() {
        if (mAssessments != null) {
            return mAssessments.size();
        }
        else {
            return 0;
        }
    }

    public void setAssessments(List<Assessment> assessments) {
        mAssessments = assessments;
        notifyDataSetChanged();
    }
}
