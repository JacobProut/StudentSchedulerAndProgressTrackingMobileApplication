package com.zybooks.studentschedulerandprogresstracking.UI.AssessmentUI;

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
import com.zybooks.studentschedulerandprogresstracking.Adapters.AssessmentAdapter;
import com.zybooks.studentschedulerandprogresstracking.Database.Repository;
import com.zybooks.studentschedulerandprogresstracking.Entities.Assessment;
import com.zybooks.studentschedulerandprogresstracking.R;
import com.zybooks.studentschedulerandprogresstracking.UI.MainActivity;

import java.util.List;

public class AssessmentOverviewPage extends AppCompatActivity {
    Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessment_overview_page);

        FloatingActionButton addButton = findViewById(R.id.AssessmentFloatingActionAddButton);
        addButton.setOnClickListener(v -> {
            Intent add = new Intent(AssessmentOverviewPage.this, AssessmentAddPage.class);
            startActivity(add);
        });

        TextView txt = (TextView) findViewById(R.id.assessment_overview_list_text);
        txt.setPaintFlags(txt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        RecyclerView recyclerView = findViewById(R.id.assessment_overview_recyclerView);
        repository = new Repository(getApplication());
        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this);
        recyclerView.setAdapter(assessmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        assessmentAdapter.setAssessments(repository.getmAllAssessments());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent returnToCoursePage = new Intent(AssessmentOverviewPage.this, MainActivity.class);
            startActivity(returnToCoursePage);
            Toast.makeText(this, "Returning to Main Menu", Toast.LENGTH_SHORT).show();
            return true;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Assessment> allAssessments = repository.getmAllAssessments();
        RecyclerView recyclerView = findViewById(R.id.assessment_overview_recyclerView);
        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this);
        recyclerView.setAdapter(assessmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        assessmentAdapter.setAssessments(allAssessments);
    }
}
