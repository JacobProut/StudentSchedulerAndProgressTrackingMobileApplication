package com.zybooks.studentschedulerandprogresstracking.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.zybooks.studentschedulerandprogresstracking.Entities.Assessment;

import java.util.List;

@Dao
public interface AssessmentDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Assessment assessment);

    @Update
    void update(Assessment assessment);

    @Delete
    void delete(Assessment assessment);

    @Query("SELECT * FROM assessments")
    List<Assessment> getAllAssessments();

    @Query("SELECT * FROM assessments WHERE courseId = :courseId")
    List<Assessment> getAllAssessmentsRelatedToCourseId(int courseId);

    @Query("SELECT * FROM assessments WHERE assessmentId = :assessmentId")
    Assessment getAssessmentById(int assessmentId);

    @Query("SELECT a.* FROM assessments a INNER JOIN course_assessment ca ON a.assessmentId = ca.assessmentId WHERE ca.courseId = :courseId")
    List<Assessment> getAssessmentsInCourse(int courseId);
}
