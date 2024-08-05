package com.zybooks.studentschedulerandprogresstracking.UI.CourseUI;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zybooks.studentschedulerandprogresstracking.Adapters.CoursePageAdapters.CourseAdapter;
import com.zybooks.studentschedulerandprogresstracking.Database.Repository;
import com.zybooks.studentschedulerandprogresstracking.Entities.Course;
import com.zybooks.studentschedulerandprogresstracking.R;
import com.zybooks.studentschedulerandprogresstracking.UI.MainActivity;

import java.util.List;

public class CourseOverviewPage extends AppCompatActivity {
    Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_overview_page);

        FloatingActionButton addButton = findViewById(R.id.CoursesFloatingActionAddButton);
        addButton.setOnClickListener(v -> {
            Intent add = new Intent(CourseOverviewPage.this, CourseAddPage.class);
            startActivity(add);
        });

        TextView txt = (TextView) findViewById(R.id.course_overview_list_text);
        txt.setPaintFlags(txt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        RecyclerView recyclerView = findViewById(R.id.course_overview_recyclerView);
        repository= new Repository(getApplication());
        List<Course> allCourses = repository.getmAllCourses();
        final CourseAdapter courseAdapter = new CourseAdapter(this);
        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter.setCourse(allCourses);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent returnToCoursePage = new Intent(CourseOverviewPage.this, MainActivity.class);
            startActivity(returnToCoursePage);
            Toast.makeText(this, "Returning to Main Menu", Toast.LENGTH_SHORT).show();
            return true;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Course> allCourses = repository.getmAllCourses();
        RecyclerView recyclerView = findViewById(R.id.course_overview_recyclerView);
        final CourseAdapter courseAdapter = new CourseAdapter(this);
        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter.setCourse(allCourses);
    }


}
