package com.zybooks.studentschedulerandprogresstracking.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.zybooks.studentschedulerandprogresstracking.DAO.AssessmentDAO;
import com.zybooks.studentschedulerandprogresstracking.DAO.CourseAssessmentDAO;
import com.zybooks.studentschedulerandprogresstracking.DAO.CourseDAO;
import com.zybooks.studentschedulerandprogresstracking.DAO.TermCourseDAO;
import com.zybooks.studentschedulerandprogresstracking.DAO.TermDAO;
import com.zybooks.studentschedulerandprogresstracking.Entities.Assessment;
import com.zybooks.studentschedulerandprogresstracking.Entities.Course;
import com.zybooks.studentschedulerandprogresstracking.Entities.CourseAssessment;
import com.zybooks.studentschedulerandprogresstracking.Entities.Term;
import com.zybooks.studentschedulerandprogresstracking.Entities.TermCourse;

@Database(entities = {Course.class, Term.class, Assessment.class, TermCourse.class, CourseAssessment.class}, version = 43, exportSchema = false)
public abstract class CourseDatabaseBuilder extends RoomDatabase {
    public abstract CourseDAO courseDAO();

    public abstract TermDAO termDAO();

    public abstract AssessmentDAO assessmentDAO();

    public abstract TermCourseDAO termCourseDAO();

    public abstract CourseAssessmentDAO courseAssessmentDAO();

    private static volatile CourseDatabaseBuilder INSTANCE;

    static CourseDatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CourseDatabaseBuilder.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CourseDatabaseBuilder.class, "CourseDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
