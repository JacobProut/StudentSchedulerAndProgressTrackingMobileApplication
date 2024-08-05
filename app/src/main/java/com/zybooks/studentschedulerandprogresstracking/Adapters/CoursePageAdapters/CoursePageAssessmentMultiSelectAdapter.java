package com.zybooks.studentschedulerandprogresstracking.Adapters.CoursePageAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zybooks.studentschedulerandprogresstracking.Entities.Assessment;
import com.zybooks.studentschedulerandprogresstracking.R;

import java.util.ArrayList;
import java.util.List;

public class CoursePageAssessmentMultiSelectAdapter extends BaseAdapter {
    private Context context;
    private List<Assessment> assessmentList;
    private List<Assessment> selectedAssessments;
    private boolean checkBoxVisibility;

    public CoursePageAssessmentMultiSelectAdapter(Context context, List<Assessment> assessmentList, boolean checkBoxVisibility) {
        this.context = context;
        this.assessmentList = assessmentList != null ? assessmentList : new ArrayList<>();
        this.selectedAssessments = new ArrayList<>();
        this.checkBoxVisibility = checkBoxVisibility;
    }

    @Override
    public int getCount() {
        return assessmentList.size();
    }

    @Override
    public Assessment getItem(int position) {
        return assessmentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return assessmentList.get(position).getAssessmentId();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    checkBoxVisibility ? R.layout.course_assessment_add_checkboxlayout : R.layout.course_assessment_add_display_unassociatedassessments_layout, parent, false);
            holder = new CoursePageAssessmentMultiSelectAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (CoursePageAssessmentMultiSelectAdapter.ViewHolder) convertView.getTag();
        }

        Assessment assessment = assessmentList.get(position);
        holder.assessmentTitle.setText(assessment.getAssessmentTitle());

        if (checkBoxVisibility) {
            if (holder.assessmentCheckbox != null) {
                holder.assessmentCheckbox.setVisibility(View.VISIBLE);
                // Handle checkbox selection state
                holder.assessmentCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        if (!selectedAssessments.contains(assessment)) {
                            selectedAssessments.add(assessment);
                        }
                    } else {
                        selectedAssessments.remove(assessment);
                    }
                });
                holder.assessmentCheckbox.setChecked(selectedAssessments.contains(assessment));
            }
        } else {
            if (holder.assessmentCheckbox != null) {
                holder.assessmentCheckbox.setVisibility(View.GONE);
            }
        }

        return convertView;
    }
    private static class ViewHolder {
        TextView assessmentTitle;
        CheckBox assessmentCheckbox;
        ViewHolder(View view) {
            assessmentTitle = view.findViewById(R.id.assessment_title1);
            assessmentCheckbox = view.findViewById(R.id.assessmentCheckbox);
        }
    }

    public List<Assessment> getSelectedAssessments() {
        return selectedAssessments;
    }
}
