package com.zybooks.studentschedulerandprogresstracking.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses")

public class Course {
    @PrimaryKey(autoGenerate = true)
    public int courseId;

    //Foreign Key
    public int termId;

    public String note;

    private String title;
    private String startDate;
    private String endDate;
    private String status;
    private String instructName;
    private int instructNumber;
    private String instructEmail;

    public Course(int courseId, String title, String startDate, String endDate, String status, String instructName, int instructNumber, String instructEmail, String note) {
        this.courseId = courseId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.instructName = instructName;
        this.instructNumber = instructNumber;
        this.instructEmail = instructEmail;
        this.note = note;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInstructName() {
        return instructName;
    }

    public void setInstructName(String instructName) {
        this.instructName = instructName;
    }

    public int getInstructNumber() {
        return instructNumber;
    }

    public void setInstructNumber(int instructNumber) {
        this.instructNumber = instructNumber;
    }

    public String getInstructEmail() {
        return instructEmail;
    }

    public void setInstructEmail(String instructEmail) {
        this.instructEmail = instructEmail;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
