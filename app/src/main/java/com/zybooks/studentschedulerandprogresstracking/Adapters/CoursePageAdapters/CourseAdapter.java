package com.zybooks.studentschedulerandprogresstracking.Adapters.CoursePageAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zybooks.studentschedulerandprogresstracking.Entities.Course;
import com.zybooks.studentschedulerandprogresstracking.R;
import com.zybooks.studentschedulerandprogresstracking.UI.CourseUI.CourseAddPage;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private List<Course> mCourses;
    private final Context context;
    private final LayoutInflater mInflater;
    public CourseAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context=context;
    }
    public class CourseViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseItemView;
        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseItemView = itemView.findViewById(R.id.course_list_textView);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                final Course current = mCourses.get(position);
                Intent intent = new Intent(context, CourseAddPage.class);
                intent.putExtra("id", current.getCourseId());
                intent.putExtra("title", current.getTitle());
                intent.putExtra("startDate", current.getStartDate());
                intent.putExtra("endDate", current.getEndDate());
                intent.putExtra("status", current.getStatus());
                intent.putExtra("instructor name", current.getInstructName());
                intent.putExtra("instructor number", current.getInstructNumber());
                intent.putExtra("instructor email", current.getInstructEmail());
                intent.putExtra("termId", current.getTermId());
                intent.putExtra("note", current.getNote());
                context.startActivity(intent);
            });
        }
    }

    @NonNull
    @Override
    public CourseAdapter.CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.course_list_item,parent,false);
        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.CourseViewHolder holder, int position) {
        if (mCourses != null) {
            Course current = mCourses.get(position);
            String title = current.getTitle();
            holder.courseItemView.setText(title);
        }
        else {
            holder.courseItemView.setText("No Course Title");
        }
    }

    @Override
    public int getItemCount() {
        if (mCourses != null) {
            return mCourses.size();
        } else {
            return 0;
        }
    }

    public void setCourse(List<Course> courses) {
        mCourses = courses;
        notifyDataSetChanged();
    }

}
