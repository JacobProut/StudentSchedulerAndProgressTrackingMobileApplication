package com.zybooks.studentschedulerandprogresstracking.Adapters.TermPageAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zybooks.studentschedulerandprogresstracking.Entities.Course;
import com.zybooks.studentschedulerandprogresstracking.R;

import java.util.List;

public class TermPageCourseRecyclerViewAdapter extends RecyclerView.Adapter<TermPageCourseRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    private final List<Course> courses;

    public TermPageCourseRecyclerViewAdapter(Context context, List<Course> courses) {
        this.context = context;
        this.courses = courses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.terms_course_add_display_unassociatedcourses_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = courses.get(position);
        holder.courseTitle.setText(course.getTitle());
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView courseTitle;

        ViewHolder(View itemView) {
            super(itemView);
            courseTitle = itemView.findViewById(R.id.course_title);
        }
    }
}
