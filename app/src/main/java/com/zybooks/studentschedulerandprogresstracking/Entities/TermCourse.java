package com.zybooks.studentschedulerandprogresstracking.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "term_course")
public class TermCourse {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int termId;
    private int courseId;

    public TermCourse(int termId, int courseId) {
        this.termId = termId;
        this.courseId = courseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
