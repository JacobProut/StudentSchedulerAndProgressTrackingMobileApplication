package com.zybooks.studentschedulerandprogresstracking.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.zybooks.studentschedulerandprogresstracking.Entities.Term;
import com.zybooks.studentschedulerandprogresstracking.Entities.TermCourse;

import java.util.List;

@Dao
public interface TermDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Term term);

    @Update
    void update(Term term);

    @Delete
    void delete(Term term);

    @Query("SELECT * FROM terms")
    List<Term> getAllTerms();

    @Query("SELECT * FROM terms WHERE termId = :termId")
    Term getTermById(int termId);

    @Query("SELECT * FROM term_course WHERE termId = :termId")
    List<TermCourse> getTermCourses(int termId);
}
