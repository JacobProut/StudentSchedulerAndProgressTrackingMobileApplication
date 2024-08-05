package com.zybooks.studentschedulerandprogresstracking.Adapters.TermPageAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zybooks.studentschedulerandprogresstracking.Entities.Course;
import com.zybooks.studentschedulerandprogresstracking.R;

import java.util.ArrayList;
import java.util.List;

public class TermPageCourseMultiSelectAdapter extends BaseAdapter {
    private Context context;
    private List<Course> courseList;
    private List<Course> selectedCourses;
    private boolean checkBoxVisibility;

    public TermPageCourseMultiSelectAdapter(Context context, List<Course> courseList, boolean checkBoxVisibility) {
        this.context = context;
        this.courseList = courseList != null ? courseList : new ArrayList<>();
        this.selectedCourses = new ArrayList<>();
        this.checkBoxVisibility = checkBoxVisibility;
    }

    @Override
    public int getCount() {
        return courseList.size();
    }

    @Override
    public Course getItem(int position) {
        return courseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return courseList.get(position).getCourseId();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            // Inflate the correct layout based on showCheckbox flag
            convertView = LayoutInflater.from(context).inflate(
                    checkBoxVisibility ? R.layout.terms_course_add_checkboxlayout : R.layout.terms_course_add_display_unassociatedcourses_layout, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Bind data
        Course course = courseList.get(position);
        holder.courseTitle.setText(course.getTitle());

        // Control checkbox visibility based on showCheckbox flag
        if (checkBoxVisibility) {
            if (holder.courseCheckbox != null) {
                holder.courseCheckbox.setVisibility(View.VISIBLE);
                // Handle checkbox selection state
                holder.courseCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        if (!selectedCourses.contains(course)) {
                            selectedCourses.add(course);
                        }
                    } else {
                        selectedCourses.remove(course);
                    }
                });
                holder.courseCheckbox.setChecked(selectedCourses.contains(course));
            }
        } else {
            if (holder.courseCheckbox != null) {
                holder.courseCheckbox.setVisibility(View.GONE);
            }
        }

        return convertView;
    }
    private static class ViewHolder {
        TextView courseTitle;
        CheckBox courseCheckbox;
        ViewHolder(View view) {
            courseTitle = view.findViewById(R.id.course_title);
            courseCheckbox = view.findViewById(R.id.courseCheckbox);
        }
    }

    public List<Course> getSelectedCourses() {
        return selectedCourses;
    }
}