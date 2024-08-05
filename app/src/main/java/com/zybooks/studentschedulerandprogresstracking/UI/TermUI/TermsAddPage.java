package com.zybooks.studentschedulerandprogresstracking.UI.TermUI;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zybooks.studentschedulerandprogresstracking.Adapters.TermPageAdapters.TermPageCourseMultiSelectAdapter;
import com.zybooks.studentschedulerandprogresstracking.Adapters.TermPageAdapters.TermPageCourseRecyclerViewAdapter;
import com.zybooks.studentschedulerandprogresstracking.Database.Repository;
import com.zybooks.studentschedulerandprogresstracking.Entities.Course;
import com.zybooks.studentschedulerandprogresstracking.Entities.Term;
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

public class TermsAddPage extends AppCompatActivity {
    Repository repository;
    int termId;
    int courseId;
    String title;
    EditText editTitle;
    String sstartDate;
    TextView editTermStartDate;
    DatePickerDialog.OnDateSetListener startDate;
    final Calendar myCalenderStartDate = Calendar.getInstance();
    String eendDate;
    TextView editTermEndDate;
    DatePickerDialog.OnDateSetListener endDate;
    final Calendar myCalenderEndDate = Calendar.getInstance();

    Term currentTerm;


    private List<Course> allCourses;
    private List<Course> associatedCourses;
    private List<Course> coursesNotInTerm;
    private List<Course> courseList;
    private List<Course> selectedCourses;


    private List<Course> coursesToBeAdded;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_add_page);

        TextView txt = findViewById(R.id.add_courses_textView);
        txt.setPaintFlags(txt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        TextView txt2 = findViewById(R.id.associated_courses_textView);
        txt2.setPaintFlags(txt2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        TextView txt3 = findViewById(R.id.courses_to_be_added_textView);
        txt3.setPaintFlags(txt3.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        termId = getIntent().getIntExtra("id", -1);
        courseId = getIntent().getIntExtra("courseId", -1);

        editTitle = findViewById(R.id.terms_add_page_title);
        title = getIntent().getStringExtra("title");
        editTitle.setText(title);

        editTermStartDate = findViewById(R.id.terms_add_page_startDate);
        editTermStartDate.setOnClickListener(v -> {
            Date date;

            String info = editTermStartDate.getText().toString();
            if (info.equals(""))info="09/00/24";
            try {
                myCalenderStartDate.setTime(sdf.parse(info));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            new DatePickerDialog(TermsAddPage.this, startDate, myCalenderStartDate.get(Calendar.YEAR), myCalenderStartDate.get(Calendar.MONTH), myCalenderStartDate.get(Calendar.DAY_OF_MONTH)).show();
        });
        startDate= (view, year, month, dayOfMonth) -> {
            myCalenderStartDate.set(Calendar.YEAR, year);
            myCalenderStartDate.set(Calendar.MONTH, month);
            myCalenderStartDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabelStart();
        };
        sstartDate = getIntent().getStringExtra("startDate");
        editTermStartDate.setText(sstartDate);

        editTermEndDate = findViewById(R.id.terms_add_page_endDate);
        editTermEndDate.setOnClickListener(v -> {
            Date date;

            String info = editTermEndDate.getText().toString();
            if (info.equals(""))info="09/00/24";
            try {
                myCalenderEndDate.setTime(sdf.parse(info));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            new DatePickerDialog(TermsAddPage.this, endDate, myCalenderEndDate.get(Calendar.YEAR), myCalenderEndDate.get(Calendar.MONTH), myCalenderEndDate.get(Calendar.DAY_OF_MONTH)).show();
        });
        endDate= (view, year, month, dayOfMonth) -> {
            myCalenderEndDate.set(Calendar.YEAR, year);
            myCalenderEndDate.set(Calendar.MONTH, month);
            myCalenderEndDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabelEnd();
        };
        eendDate = getIntent().getStringExtra("endDate");
        editTermEndDate.setText(eendDate);

        repository = new Repository(getApplication());

        associatedCourses = repository.getmAllAssociatedCourses(termId);
        allCourses = repository.getmAllCourses();
        coursesNotInTerm = getCoursesNotInTerm();

        TermPageCourseRecyclerViewAdapter selection = new TermPageCourseRecyclerViewAdapter(this, coursesNotInTerm);
        RecyclerView listView = findViewById(R.id.term_add_page_add_courses_listView);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(selection);

        TermPageCourseRecyclerViewAdapter displayAdapter = new TermPageCourseRecyclerViewAdapter(this, associatedCourses);
        RecyclerView associatedRecyclerView = findViewById(R.id.term_add_page_associated_courses_recyclerView);
        associatedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        associatedRecyclerView.setAdapter(displayAdapter);

        coursesToBeAdded = new ArrayList<>();
        TermPageCourseRecyclerViewAdapter coursesToBeAddedAdapter = new TermPageCourseRecyclerViewAdapter(this, coursesToBeAdded);
        RecyclerView coursesToBeAddedListView = findViewById(R.id.term_add_page_add_coursesToBeAdded_recyclerView);
        coursesToBeAddedListView.setLayoutManager(new LinearLayoutManager(this));
        coursesToBeAddedListView.setAdapter(coursesToBeAddedAdapter);

        fetchCourses();
        fetchAssociatedCourses();
        Button showDialogButton = findViewById(R.id.button_ok);
        showDialogButton.setOnClickListener(v -> showMultiSelectDialog());
    }

    private void showMultiSelectDialog() {
        if (courseList == null) {
            Toast.makeText(TermsAddPage.this, "Course List is Null", Toast.LENGTH_LONG).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(TermsAddPage.this);
        builder.setTitle("Select Courses");

        TermPageCourseMultiSelectAdapter adapter = new TermPageCourseMultiSelectAdapter(this, courseList, true);
        ListView listView = new ListView(this);
        listView.setAdapter(adapter);
        builder.setView(listView);
        fetchAssociatedCourses();

        TermPageCourseMultiSelectAdapter coursesToBeAddedAdapter = new TermPageCourseMultiSelectAdapter(this, coursesToBeAdded, false);


        builder.setPositiveButton("Add Courses", (dialog, which) -> {
            selectedCourses = adapter.getSelectedCourses();
            coursesToBeAdded.addAll(selectedCourses);

            TermPageCourseRecyclerViewAdapter toBeAddedAdapter = new TermPageCourseRecyclerViewAdapter(this, coursesToBeAdded);
            RecyclerView recyclerView = findViewById(R.id.term_add_page_add_coursesToBeAdded_recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(toBeAddedAdapter);
            coursesToBeAddedAdapter.notifyDataSetChanged();

            fetchAssociatedCourses();
            dialog.dismiss();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss(); // Dismiss dialog if cancel is pressed
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void fetchCourses() {
        new Thread(() -> {
            courseList = repository.getmAllCourses();
            runOnUiThread(() -> {
                if (courseList == null) {
                    courseList = new ArrayList<>();
                }
            });
        }).start();
    }

    private void fetchAssociatedCourses() {
        new Thread(() -> {
            associatedCourses = repository.getCoursesForTerm(termId); // Fetch courses for the current term
            runOnUiThread(() -> {
                // Update the RecyclerView with the new list
                TermPageCourseRecyclerViewAdapter displayAdapter = new TermPageCourseRecyclerViewAdapter(this, associatedCourses);
                RecyclerView associatedRecyclerView = findViewById(R.id.term_add_page_associated_courses_recyclerView);
                associatedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                associatedRecyclerView.setAdapter(displayAdapter);
            });
        }).start();
    }

    private List<Course> getCoursesNotInTerm() {
        List<Course> notInTerm = new ArrayList<>();
        for (Course course : allCourses) {
            boolean isAssociated = false;
            for (Course associatedCourse : associatedCourses) {
                if (course.getCourseId() == associatedCourse.getCourseId()) {
                    isAssociated = true;
                    break;
                }
            }
            if (!isAssociated) {
                notInTerm.add(course);
            }
        }
        return notInTerm;
    }

    public void updateLabelStart() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editTermStartDate.setText(sdf.format(myCalenderStartDate.getTime()));
    }

    public void updateLabelEnd() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editTermEndDate.setText(sdf.format(myCalenderEndDate.getTime()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent returnToCoursePage = new Intent(TermsAddPage.this, TermsOverviewPage.class);
            startActivity(returnToCoursePage);
            Toast.makeText(this, "Returning to Terms Overview", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (item.getItemId() == R.id.notifyTermEndDate) {
            String dateFromScreen = editTermEndDate.getText().toString();
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long trigger = myDate.getTime();
            Intent intent = new Intent(TermsAddPage.this, MyReceiver.class);
            intent.putExtra("key", "Term Ends Today!");
            PendingIntent sender = PendingIntent.getBroadcast(TermsAddPage.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            return true;
        }
        if (item.getItemId() == R.id.notifyTermStartDate) {
            String dateFromScreen = editTermStartDate.getText().toString();
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long trigger = myDate.getTime();
            Intent intent = new Intent(TermsAddPage.this, MyReceiver.class);
            intent.putExtra("key", "Term Starts Today!");
            PendingIntent sender = PendingIntent.getBroadcast(TermsAddPage.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            return true;
        }
        if (item.getItemId() == R.id.termDelete) {
            for (Term term : repository.getmAllTerms()) {
                if (term.getTermId() == termId) {
                    currentTerm = term;
                    break;
                }
                if (currentTerm != null) {
                    if (repository.getmAllAssociatedCourses(termId).isEmpty()) {
                        Toast.makeText(TermsAddPage.this, currentTerm.getTermTitle() + " was deleted", Toast.LENGTH_LONG).show();
                        repository.delete(currentTerm);
                        TermsAddPage.this.finish();
                    } else {
                        Toast.makeText(TermsAddPage.this, "Term cannot be deleted while having courses assigned to it", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
        if (item.getItemId() == R.id.termSave) {
                String startDateStr = editTermStartDate.getText().toString();
                String endDateStr = editTermEndDate.getText().toString();
                String titleStr = editTitle.getText().toString();

                if (titleStr.isEmpty()) {
                    Toast.makeText(TermsAddPage.this, "Please fill in all required fields.", Toast.LENGTH_LONG).show();
                    return true;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
                Date startDate, endDate;
                try {
                    startDate = sdf.parse(startDateStr);
                    endDate = sdf.parse(endDateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(TermsAddPage.this, "Invalid date format", Toast.LENGTH_LONG).show();
                    return true;
                }

                if (startDate != null && endDate != null) {
                    Calendar startCalendar = Calendar.getInstance();
                    startCalendar.setTime(startDate);
                    Calendar endCalendar = Calendar.getInstance();
                    endCalendar.setTime(endDate);
                    startCalendar.add(Calendar.DAY_OF_MONTH, 1);

                    if (endCalendar.before(startCalendar)) {
                        Toast.makeText(TermsAddPage.this, "End date must be at least 1 day after the start date.", Toast.LENGTH_LONG).show();
                        return true;
                    }
                    Term term;
                    if (termId == -1) {
                        if (repository.getmAllTerms().size() == 0) termId = 1;
                        else
                            termId = repository.getmAllTerms().get(repository.getmAllTerms().size() - 1).getTermId() + 1;
                        term = new Term(termId, titleStr, startDateStr, endDateStr);
                        repository.insert(term);
                        Toast.makeText(TermsAddPage.this, "Term has been added!", Toast.LENGTH_LONG).show();
                    } else {
                        term = new Term(termId, titleStr, startDateStr, endDateStr);
                        repository.update(term);
                        Toast.makeText(TermsAddPage.this, "Term has been updated!", Toast.LENGTH_LONG).show();
                    }

                    // Update course associations
                    if (selectedCourses != null && !selectedCourses.isEmpty()) {
                        for (Course course : selectedCourses) {
                            if (!associatedCourses.contains(course)) {
                                associatedCourses.add(course);
                                repository.addCourseToTerm(termId, course.getCourseId());
                            }
                            fetchAssociatedCourses();
                        }
                    }
                    this.finish();
                } else {
                    Toast.makeText(TermsAddPage.this, "Start date or end date is null.", Toast.LENGTH_LONG).show();
                }
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_term_add_page, menu);
        return true;
    }
}
