package com.zybooks.studentschedulerandprogresstracking.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "assessments")
public class Assessment {
    @PrimaryKey(autoGenerate = true)
    public int assessmentId;

    //Foreign Key
    public int courseId;

    private String assessmentTitle;
    private String endDate;
    private String startDate;
    private String spinnerOAorPA;

    public Assessment(int assessmentId, String assessmentTitle, String startDate, String endDate, String spinnerOAorPA) {
        this.assessmentId = assessmentId;
        this.assessmentTitle = assessmentTitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.spinnerOAorPA = spinnerOAorPA;
    }

    public int getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getAssessmentTitle() {
        return assessmentTitle;
    }

    public void setAssessmentTitle(String assessmentTitle) {
        this.assessmentTitle = assessmentTitle;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getSpinnerOAorPA() {
        return spinnerOAorPA;
    }

    public void setSpinnerOAorPA(String spinnerOAorPA) {
        this.spinnerOAorPA = spinnerOAorPA;
    }
}
