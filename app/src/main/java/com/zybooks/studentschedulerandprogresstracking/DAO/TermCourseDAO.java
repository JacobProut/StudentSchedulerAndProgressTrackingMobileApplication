package com.zybooks.studentschedulerandprogresstracking.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.zybooks.studentschedulerandprogresstracking.Entities.TermCourse;

import java.util.List;

@Dao
public interface TermCourseDAO {
    @Insert
    void insert(TermCourse termCourse);

    @Delete
    void delete(TermCourse termCourse);

    @Query("DELETE FROM term_course WHERE termId = :termId")
    void deleteCoursesForTerm(int termId);

    @Query("SELECT * FROM term_course WHERE termId = :termId")
    List<TermCourse> getCoursesForTerm(int termId);

    @Query("SELECT * FROM term_course WHERE courseId = :courseId")
    List<TermCourse> getTermsForCourse(int courseId);

    @Query("SELECT * FROM term_course WHERE termId = :termId AND courseId = :courseId")
    TermCourse getTermCourse(int termId, int courseId);
}