package com.zybooks.studentschedulerandprogresstracking.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.zybooks.studentschedulerandprogresstracking.Entities.CourseAssessment;

import java.util.List;

@Dao
public interface CourseAssessmentDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(CourseAssessment courseAssessment);

    @Delete
    void delete(CourseAssessment courseAssessment);

    @Query("DELETE FROM course_assessment WHERE courseId = :courseId")
    void deleteAssessmentsForCourse(int courseId);

    @Query("SELECT * FROM course_assessment WHERE courseId = :courseId")
    List<CourseAssessment> getCoursesForAssessment(int courseId);

    @Query("SELECT * FROM course_assessment WHERE assessmentId = :assessmentId")
    List<CourseAssessment> getAssessmentsForCourse(int assessmentId);

    @Query("SELECT * FROM course_assessment WHERE courseId = :courseId AND assessmentId = :assessmentId")
    CourseAssessment getCourseAssessment(int courseId, int assessmentId);
}