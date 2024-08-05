package com.zybooks.studentschedulerandprogresstracking.UI.CourseUI;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zybooks.studentschedulerandprogresstracking.Adapters.CoursePageAdapters.CoursePageAssessmentMultiSelectAdapter;
import com.zybooks.studentschedulerandprogresstracking.Adapters.CoursePageAdapters.CoursePageAssessmentRecyclerViewAdapter;
import com.zybooks.studentschedulerandprogresstracking.Database.Repository;
import com.zybooks.studentschedulerandprogresstracking.Entities.Assessment;
import com.zybooks.studentschedulerandprogresstracking.Entities.Course;
import com.zybooks.studentschedulerandprogresstracking.R;
import com.zybooks.studentschedulerandprogresstracking.Receiver.MyReceiver;
import com.zybooks.studentschedulerandprogresstracking.UI.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CourseAddPage extends AppCompatActivity {
    Repository repository;
    int courseId;
    int termId;
    String title;
    EditText editTitle;
    String sstartDate;
    TextView editStartDate;
    DatePickerDialog.OnDateSetListener startDate;
    final Calendar myCalenderStartDate = Calendar.getInstance();
    String eendDate;
    TextView editEndDate;
    DatePickerDialog.OnDateSetListener endDate;
    final Calendar myCalenderEndDate = Calendar.getInstance();
    String status;
    Spinner editStatus;
    String instructName;
    EditText editInstructName;
    int instructNumber;
    EditText editInstructNumber;
    String instructEmail;
    EditText editInstructEmail;

    String note;
    EditText optionalNote;

    Course currentCourse;


    private List<Assessment> allAssessments;
    private List<Assessment> associatedAssessments;
    private List<Assessment> assessmentsNotInCourse;
    private List<Assessment> assessmentList;
    private List<Assessment> selectedAssessments;


    private List<Assessment> assessmentsToBeAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_add_page);

        TextView txt2 = findViewById(R.id.associated_assessments_textView);
        txt2.setPaintFlags(txt2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        courseId = getIntent().getIntExtra("id", -1);
        termId = getIntent().getIntExtra("termId", -1);

        editTitle = findViewById(R.id.course_add_page_course_title);
        title = getIntent().getStringExtra("title");
        editTitle.setText(title);

        editStartDate = findViewById(R.id.course_add_page_course_startDate);
        editStartDate.setOnClickListener(v -> {
            Date date;

            String info = editStartDate.getText().toString();
            if (info.equals("")) info = "09/00/24";
            try {
                myCalenderStartDate.setTime(sdf.parse(info));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            new DatePickerDialog(CourseAddPage.this, startDate, myCalenderStartDate.get(Calendar.YEAR), myCalenderStartDate.get(Calendar.MONTH), myCalenderStartDate.get(Calendar.DAY_OF_MONTH)).show();
        });
        startDate = (view, year, month, dayOfMonth) -> {
            myCalenderStartDate.set(Calendar.YEAR, year);
            myCalenderStartDate.set(Calendar.MONTH, month);
            myCalenderStartDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabelStart();
        };
        sstartDate = getIntent().getStringExtra("startDate");
        editStartDate.setText(sstartDate);

        editEndDate = findViewById(R.id.course_add_page_course_endDate);
        editEndDate.setOnClickListener(v -> {
            Date date;

            String info = editEndDate.getText().toString();
            if (info.equals("")) info = "09/00/24";
            try {
                myCalenderEndDate.setTime(sdf.parse(info));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            new DatePickerDialog(CourseAddPage.this, endDate, myCalenderEndDate.get(Calendar.YEAR), myCalenderEndDate.get(Calendar.MONTH), myCalenderEndDate.get(Calendar.DAY_OF_MONTH)).show();
        });
        endDate = (view, year, month, dayOfMonth) -> {
            myCalenderEndDate.set(Calendar.YEAR, year);
            myCalenderEndDate.set(Calendar.MONTH, month);
            myCalenderEndDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabelEnd();
        };
        eendDate = getIntent().getStringExtra("endDate");
        editEndDate.setText(eendDate);

        editStatus = findViewById(R.id.course_add_page_course_status);
        status = getIntent().getStringExtra("status");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.status_ComboBox_aka_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editStatus.setAdapter(adapter);
        int position = adapter.getPosition(status);
        if (position < 0) {
            position = 0;
        }
        editStatus.setSelection(position);

        editInstructName = findViewById(R.id.course_add_page_course_instructor_name);
        instructName = getIntent().getStringExtra("instructor name");
        editInstructName.setText(instructName);

        editInstructNumber = findViewById(R.id.course_add_page_course_instructor_number);
        instructNumber = getIntent().getIntExtra("instructor number", 0);
        editInstructNumber.setText(Integer.toString(instructNumber));

        editInstructEmail = findViewById(R.id.course_add_page_course_instructor_email);
        instructEmail = getIntent().getStringExtra("instructor email");
        editInstructEmail.setText(instructEmail);

        repository = new Repository(getApplication());

        associatedAssessments = repository.getmAssociatedAssessmentsForCourse(courseId);
        allAssessments = repository.getmAllAssessments();

        CoursePageAssessmentRecyclerViewAdapter displayAdapter = new CoursePageAssessmentRecyclerViewAdapter(this, associatedAssessments);
        RecyclerView associatedRecyclerView = findViewById(R.id.course_add_page_associated_assessments_recyclerView);
        associatedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        associatedRecyclerView.setAdapter(displayAdapter);

        assessmentsToBeAdded = new ArrayList<>();
        CoursePageAssessmentRecyclerViewAdapter coursesToBeAddedAdapter = new CoursePageAssessmentRecyclerViewAdapter(this, assessmentsToBeAdded);
        RecyclerView coursesToBeAddedListView = findViewById(R.id.course_add_page_add_assessmentsToBeAdded_recyclerView);
        coursesToBeAddedListView.setLayoutManager(new LinearLayoutManager(this));
        coursesToBeAddedListView.setAdapter(coursesToBeAddedAdapter);

        fetchAssessments();
        fetchAssociatedAssessments();
        //getAssessmentsNotInCourse();
        Button showDialogButton = findViewById(R.id.button_add_assessments);
        showDialogButton.setOnClickListener(v -> showMultiSelectDialog());

        optionalNote = findViewById(R.id.course_add_page_optional_note);
        note = getIntent().getStringExtra("note");
        optionalNote.setText(note);
    }


    private void showMultiSelectDialog() {
        if (assessmentList == null) {
            Toast.makeText(CourseAddPage.this, "Assessment List is Null", Toast.LENGTH_LONG).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(CourseAddPage.this);
        builder.setTitle("Select Assessments");

        CoursePageAssessmentMultiSelectAdapter adapter = new CoursePageAssessmentMultiSelectAdapter(this, assessmentList, true);
        ListView listView = new ListView(this);
        listView.setAdapter(adapter);
        builder.setView(listView);
        fetchAssociatedAssessments();

        CoursePageAssessmentMultiSelectAdapter assessmentsToBeAddedAdapter = new CoursePageAssessmentMultiSelectAdapter(this, assessmentsToBeAdded, false);


        builder.setPositiveButton("Add Assessments", (dialog, which) -> {
            selectedAssessments = adapter.getSelectedAssessments();
            assessmentsToBeAdded.addAll(selectedAssessments);

            CoursePageAssessmentRecyclerViewAdapter toBeAddedAdapter = new CoursePageAssessmentRecyclerViewAdapter(this, assessmentsToBeAdded);
            RecyclerView recyclerView = findViewById(R.id.course_add_page_add_assessmentsToBeAdded_recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(toBeAddedAdapter);
            assessmentsToBeAddedAdapter.notifyDataSetChanged();

            fetchAssociatedAssessments();
            dialog.dismiss();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss(); // Dismiss dialog if cancel is pressed
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void fetchAssessments() {
        new Thread(() -> {
            assessmentList = repository.getmAllAssessments();
            runOnUiThread(() -> {
                if (assessmentList == null) {
                    assessmentList = new ArrayList<>();
                }
            });
        }).start();
    }

    private void fetchAssociatedAssessments() {
        new Thread(() -> {
            associatedAssessments = repository.getAssessmentForCourse(courseId);
            runOnUiThread(() -> {
                CoursePageAssessmentRecyclerViewAdapter displayAdapter = new CoursePageAssessmentRecyclerViewAdapter(CourseAddPage.this, associatedAssessments);
                RecyclerView associatedRecyclerView = findViewById(R.id.course_add_page_associated_assessments_recyclerView);
                associatedRecyclerView.setLayoutManager(new LinearLayoutManager(CourseAddPage.this));
                associatedRecyclerView.setAdapter(displayAdapter);
            });
        }).start();
    }

    public void updateLabelStart() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editStartDate.setText(sdf.format(myCalenderStartDate.getTime()));
    }

    public void updateLabelEnd() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editEndDate.setText(sdf.format(myCalenderEndDate.getTime()));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course_add_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent returnToCoursePage = new Intent(CourseAddPage.this, CourseOverviewPage.class);
            startActivity(returnToCoursePage);
            Toast.makeText(this, "Returning to Course Overview", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (item.getItemId() == R.id.share) {
            Intent sentIntent = new Intent();
            sentIntent.setAction(Intent.ACTION_SEND);
            sentIntent.putExtra(Intent.EXTRA_TEXT, optionalNote.getText().toString());
            sentIntent.putExtra(Intent.EXTRA_TITLE, "Student Scheduler Course Note");
            sentIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sentIntent, null);
            startActivity(shareIntent);
            return true;

        }
        if (item.getItemId() == R.id.notifyEndDate) {
            String dateFromScreen = editEndDate.getText().toString();
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long trigger = myDate.getTime();
            Intent intent = new Intent(CourseAddPage.this, MyReceiver.class);
            intent.putExtra("key", "Course Ends Today!");
            PendingIntent sender = PendingIntent.getBroadcast(CourseAddPage.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            return true;
        }
        if (item.getItemId() == R.id.notifyStartDate) {
            String dateFromScreen = editStartDate.getText().toString();
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long trigger = myDate.getTime();
            Intent intent = new Intent(CourseAddPage.this, MyReceiver.class);
            intent.putExtra("key", "Course Starts Today!");
            PendingIntent sender = PendingIntent.getBroadcast(CourseAddPage.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            return true;
        }
        if (item.getItemId() == R.id.courseDelete) {
            for (Course course : repository.getmAllCourses()) {
                if (course.getCourseId() == courseId) {
                    currentCourse = course;
                }
                if (currentCourse != null) {
                    Toast.makeText(CourseAddPage.this, currentCourse.getTitle() + " was deleted", Toast.LENGTH_LONG).show();
                    repository.delete(currentCourse);
                    CourseAddPage.this.finish();
                }
            }
        }
        if (item.getItemId() == R.id.courseSave) {
            String startDateStr = editStartDate.getText().toString();
            String endDateStr = editEndDate.getText().toString();
            String titleStr = editTitle.getText().toString();
            String instructNameStr = editInstructName.getText().toString();
            String instructNumberStr = editInstructNumber.getText().toString();
            String instructEmailStr = editInstructEmail.getText().toString();
            String status = editStatus.getSelectedItem().toString();
            String optNote = optionalNote.getText().toString();

            if (titleStr.isEmpty() || instructNameStr.isEmpty() || instructNumberStr.isEmpty() || instructEmailStr.isEmpty()) {
                Toast.makeText(CourseAddPage.this, "Please fill in all required fields.", Toast.LENGTH_LONG).show();
                return true;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
            Date startDate, endDate;
            try {
                startDate = sdf.parse(startDateStr);
                endDate = sdf.parse(endDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(CourseAddPage.this, "Invalid date format", Toast.LENGTH_LONG).show();
                return true;
            }

            if (startDate != null && endDate != null) {
                Calendar startCalendar = Calendar.getInstance();
                startCalendar.setTime(startDate);
                Calendar endCalendar = Calendar.getInstance();
                endCalendar.setTime(endDate);
                startCalendar.add(Calendar.DAY_OF_MONTH, 1);

                if (endCalendar.before(startCalendar)) {
                    Toast.makeText(CourseAddPage.this, "End date must be at least 1 day after the start date.", Toast.LENGTH_LONG).show();
                    return true;
                }

                Course course;
                if (courseId == -1) {
                    if (repository.getmAllCourses().size() == 0) courseId = 1;
                    else
                        courseId = repository.getmAllCourses().get(repository.getmAllCourses().size() - 1).getCourseId() + 1;
                    course = new Course(courseId, titleStr, startDateStr, endDateStr, status, instructNameStr, Integer.parseInt(instructNumberStr), instructEmailStr, optNote);
                    repository.insert(course);
                    Toast.makeText(CourseAddPage.this, "Course has been added!", Toast.LENGTH_LONG).show();

                } else {
                    course = new Course(courseId, titleStr, startDateStr, endDateStr, status, instructNameStr, Integer.parseInt(instructNumberStr), instructEmailStr, optNote);
                    repository.update(course);
                    Toast.makeText(CourseAddPage.this, "Course has been updated!", Toast.LENGTH_LONG).show();
                }
                if (selectedAssessments != null && !selectedAssessments.isEmpty()) {
                    for (Assessment assessment : selectedAssessments) {
                        if (!associatedAssessments.contains(assessment)) {
                            associatedAssessments.add(assessment);
                            repository.addAssessmentsToCourse(courseId, assessment.getAssessmentId());
                        }
                        fetchAssociatedAssessments();
                    }
                }
                this.finish();
            } else {
                Toast.makeText(CourseAddPage.this, "Start date or end date is null.", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
