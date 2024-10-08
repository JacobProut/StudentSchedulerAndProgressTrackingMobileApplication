package com.zybooks.studentschedulerandprogresstracking.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.zybooks.studentschedulerandprogresstracking.Entities.Course;

import java.util.List;

@Dao
public interface CourseDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Course course);

    @Update
    void update(Course course);

    @Delete
    void delete(Course course);

    @Query("SELECT * FROM courses ORDER BY courseId ASC")
    List<Course> getAllCourses();

    @Query("SELECT * FROM courses WHERE courseId = :courseId")
    Course getCourseById(int courseId);

    @Query("SELECT c.* FROM courses c INNER JOIN term_course tc ON c.courseId = tc.courseId WHERE tc.termId = :termId")
    List<Course> getCoursesInTerm(int termId);

}
