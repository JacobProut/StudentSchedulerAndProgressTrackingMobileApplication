package com.zybooks.studentschedulerandprogresstracking.UI.AssessmentUI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zybooks.studentschedulerandprogresstracking.Database.Repository;
import com.zybooks.studentschedulerandprogresstracking.Entities.Assessment;
import com.zybooks.studentschedulerandprogresstracking.R;
import com.zybooks.studentschedulerandprogresstracking.Receiver.MyReceiver;
import com.zybooks.studentschedulerandprogresstracking.UI.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AssessmentAddPage extends AppCompatActivity {
    Repository repository;
    int assessmentId;
    int courseId;
    String title;
    EditText editTitle;
    String eendDate;
    TextView editAssessEndDate;
    DatePickerDialog.OnDateSetListener endDate;
    final Calendar myCalenderEndDate = Calendar.getInstance();
    String sstartDate;
    TextView editAssessStartDate;
    DatePickerDialog.OnDateSetListener startDate;
    final Calendar myCalenderStartDate = Calendar.getInstance();
    String assessmentType;
    Spinner editTypeOfAssessment;

    Assessment currentAssessment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessment_add_page);

        assessmentId = getIntent().getIntExtra("id", -1);
        courseId = getIntent().getIntExtra("courseId", 0);

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editTitle = findViewById(R.id.assessment_add_page_assessment_title);
        title = getIntent().getStringExtra("title");
        editTitle.setText(title);

        editAssessStartDate = findViewById(R.id.assessment_add_page_assessment_startDate);
        editAssessStartDate.setOnClickListener(v -> {
            Date date;

            String info = editAssessStartDate.getText().toString();
            if (info.equals("")) info = "09/00/24";
            try {
                myCalenderStartDate.setTime(sdf.parse(info));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            new DatePickerDialog(AssessmentAddPage.this, startDate, myCalenderStartDate.get(Calendar.YEAR), myCalenderStartDate.get(Calendar.MONTH), myCalenderStartDate.get(Calendar.DAY_OF_MONTH)).show();
        });
        startDate = (view, year, month, dayOfMonth) -> {
            myCalenderStartDate.set(Calendar.YEAR, year);
            myCalenderStartDate.set(Calendar.MONTH, month);
            myCalenderStartDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabelStart();
        };
        sstartDate = getIntent().getStringExtra("startDate");
        editAssessStartDate.setText(sstartDate);

        editAssessEndDate = findViewById(R.id.assessment_add_page_assessment_endDate);
        editAssessEndDate.setOnClickListener(v -> {
            Date date;

            String info = editAssessEndDate.getText().toString();
            if (info.equals("")) info = "09/00/24";
            try {
                myCalenderEndDate.setTime(sdf.parse(info));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            new DatePickerDialog(AssessmentAddPage.this, endDate, myCalenderEndDate.get(Calendar.YEAR), myCalenderEndDate.get(Calendar.MONTH), myCalenderEndDate.get(Calendar.DAY_OF_MONTH)).show();
        });
        endDate = (view, year, month, dayOfMonth) -> {
            myCalenderEndDate.set(Calendar.YEAR, year);
            myCalenderEndDate.set(Calendar.MONTH, month);
            myCalenderEndDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabelEnd();
        };
        eendDate = getIntent().getStringExtra("endDate");
        editAssessEndDate.setText(eendDate);

        editTypeOfAssessment = findViewById(R.id.assessment_add_page_assessment_type);
        assessmentType = getIntent().getStringExtra("status");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_OA_or_PA, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editTypeOfAssessment.setAdapter(adapter);
        int position = adapter.getPosition(assessmentType);
        if (position < 0) {
            position = 0;
        }
        editTypeOfAssessment.setSelection(position);

        repository = new Repository(getApplication());
    }

    public void updateLabelStart() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editAssessStartDate.setText(sdf.format(myCalenderStartDate.getTime()));
    }

    public void updateLabelEnd() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editAssessEndDate.setText(sdf.format(myCalenderEndDate.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_assessment_add_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent returnToCoursePage = new Intent(AssessmentAddPage.this, AssessmentOverviewPage.class);
            startActivity(returnToCoursePage);
            Toast.makeText(this, "Returning to Assessment Overview", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (item.getItemId() == R.id.notifyAssessmentStartDate) {
            String dateFromScreen = editAssessStartDate.getText().toString();
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long trigger = myDate.getTime();
            Intent intent = new Intent(AssessmentAddPage.this, MyReceiver.class);
            intent.putExtra("key", "Assessment Starts Today!");
            PendingIntent sender = PendingIntent.getBroadcast(AssessmentAddPage.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            return true;
        }
        if (item.getItemId() == R.id.notifyAssessmentEndDate) {
            String dateFromScreen = editAssessEndDate.getText().toString();
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long trigger = myDate.getTime();
            Intent intent = new Intent(AssessmentAddPage.this, MyReceiver.class);
            intent.putExtra("key", "Assessment Ends Today!");
            PendingIntent sender = PendingIntent.getBroadcast(AssessmentAddPage.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            return true;
        }
        if (item.getItemId() == R.id.assessmentDelete) {
            for (Assessment assessment : repository.getmAllAssessments()) {
                if (assessment.getAssessmentId() == assessmentId) {
                    currentAssessment = assessment;
                }
                if (currentAssessment != null) {
                    Toast.makeText(AssessmentAddPage.this, currentAssessment.getAssessmentTitle() + " was deleted", Toast.LENGTH_LONG).show();
                    repository.delete(currentAssessment);
                    AssessmentAddPage.this.finish();
                }
            }
        }
        if (item.getItemId() == R.id.assessmentSave) {
            String startDateStr = editAssessStartDate.getText().toString();
            String endDateStr = editAssessEndDate.getText().toString();
            String titleStr = editTitle.getText().toString();

            if (titleStr.isEmpty()) {
                Toast.makeText(AssessmentAddPage.this, "Please fill in all required fields.", Toast.LENGTH_LONG).show();
                return true;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
            Date startDate, endDate;
            try {
                startDate = sdf.parse(startDateStr);
                endDate = sdf.parse(endDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(AssessmentAddPage.this, "Invalid date format", Toast.LENGTH_LONG).show();
                return true;
            }

            if (startDate != null && endDate != null) {
                Calendar startCalendar = Calendar.getInstance();
                startCalendar.setTime(startDate);
                Calendar endCalendar = Calendar.getInstance();
                endCalendar.setTime(endDate);
                startCalendar.add(Calendar.DAY_OF_MONTH, 1);

                if (endCalendar.before(startCalendar)) {
                    Toast.makeText(AssessmentAddPage.this, "End date must be at least 1 day after the start date.", Toast.LENGTH_LONG).show();
                    return true;
                }

                Assessment assessment;
                if (assessmentId == -1) {
                    if (repository.getmAllAssessments().size() == 0) assessmentId = 1;
                    else
                        assessmentId = repository.getmAllAssessments().get(repository.getmAllAssessments().size() - 1).getAssessmentId() + 1;
                    assessment = new Assessment(assessmentId, titleStr, startDateStr, endDateStr, editTypeOfAssessment.getSelectedItem().toString());
                    repository.insert(assessment);
                    Toast.makeText(AssessmentAddPage.this, "Assessment has been added!", Toast.LENGTH_LONG).show();
                    this.finish();
                } else {
                    assessment = new Assessment(assessmentId, titleStr, startDateStr, endDateStr, editTypeOfAssessment.getSelectedItem().toString());
                    repository.update(assessment);
                    Toast.makeText(AssessmentAddPage.this, "Assessment has been updated!", Toast.LENGTH_LONG).show();
                    this.finish();
                }
            } else {
                Toast.makeText(AssessmentAddPage.this, "Start date or end date is null.", Toast.LENGTH_LONG).show();
            }
        }
        return true;
    }
}

