package com.zybooks.studentschedulerandprogresstracking.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.zybooks.studentschedulerandprogresstracking.Database.Repository;
import com.zybooks.studentschedulerandprogresstracking.Entities.Assessment;
import com.zybooks.studentschedulerandprogresstracking.Entities.Course;
import com.zybooks.studentschedulerandprogresstracking.Entities.Term;
import com.zybooks.studentschedulerandprogresstracking.R;
import com.zybooks.studentschedulerandprogresstracking.UI.AssessmentUI.AssessmentOverviewPage;
import com.zybooks.studentschedulerandprogresstracking.UI.CourseUI.CourseOverviewPage;
import com.zybooks.studentschedulerandprogresstracking.UI.TermUI.TermsOverviewPage;

public class MainActivity extends AppCompatActivity {
    Repository repository;
    public static int numAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button button = findViewById(R.id.buttonTerms);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TermsOverviewPage.class);
            startActivity(intent);
        });

        Button courses = findViewById(R.id.buttonCourses);
        courses.setOnClickListener(v -> {
            Intent courseIntent = new Intent(MainActivity.this, CourseOverviewPage.class);
            startActivity(courseIntent);
        });

        Button assessment = findViewById(R.id.buttonAssessments);
        assessment.setOnClickListener(v -> {
            Intent assessmentIntent = new Intent(MainActivity.this, AssessmentOverviewPage.class);
            startActivity(assessmentIntent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addSample) {
            repository = new Repository(getApplication());
            Course course = new Course(1, "Ethics", "08/01/24", "09/01/24", "Plan to Take", "Stacy Sue", 1234567890, "StacySue@mail.com", "Ethics is Fun... Said no one!");
            repository.insert(course);
            course = new Course(2, "Physics", "08/01/24", "10/01/24", "Dropped", "Albert Newman", 1987654321, "AlbertTheGuy@mail.com", "Science Rules");
            repository.insert(course);
            Term term = new Term(1, "Winter Term", "08/01/24", "12/01/24");
            repository.insert(term);
            term = new Term(2, "Fall Term", "12/01/24", "03/01/25");
            repository.insert(term);
            Assessment assessment = new Assessment(1, "Ways of the earth OA", "09/20/24", "09/20/24", "Performance Assessment");
            repository.insert(assessment);
            Assessment assessment2 = new Assessment(2, "Newtons Law PA", "09/20/24", "09/20/24", "Objective Assessment");
            repository.insert(assessment2);

            //REMOVE BEFORE SUBMITTING PROJECT
            Term term3 = new Term(3, "TEST", "08/01/24", "12/01/24");
            repository.insert(term3);

            return true;
        }
        return true;
    }
}