package com.zybooks.studentschedulerandprogresstracking.Database;

import android.app.Application;

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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private CourseDAO mCourseDAO;
    private TermDAO mTermDAO;
    private AssessmentDAO mAssessmentDAO;
    private TermCourseDAO mTermCourseDAO;

    private CourseAssessmentDAO mCourseAssessmentDAO;


    private List<Course> mAllCourses;
    private List<Term> mAllTerms;
    private List<Assessment> mAllAssessments;


    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application) {
        CourseDatabaseBuilder db = CourseDatabaseBuilder.getDatabase(application);
        mCourseDAO = db.courseDAO();
        mTermDAO = db.termDAO();
        mAssessmentDAO = db.assessmentDAO();
        mTermCourseDAO = db.termCourseDAO();
        mCourseAssessmentDAO = db.courseAssessmentDAO();
    }

    public List<Course> getmAllCourses() {
        databaseExecutor.execute(() -> {
            mAllCourses = mCourseDAO.getAllCourses();
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllCourses;
    }

    public List<Course> getmAllAssociatedCourses(int termId) {
        databaseExecutor.execute(() -> {
            mAllCourses = mCourseDAO.getCoursesInTerm(termId);
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllCourses;
    }

    public void insert(Course course) {
        databaseExecutor.execute(() -> {
            mCourseDAO.insert(course);
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Course course) {
        databaseExecutor.execute(() -> {
            mCourseDAO.update(course);
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Course course) {
        databaseExecutor.execute(() -> {
            mCourseDAO.delete(course);
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Term> getmAllTerms() {
        databaseExecutor.execute(() -> {
            mAllTerms = mTermDAO.getAllTerms();
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllTerms;
    }


    public void insert(Term term) {
        databaseExecutor.execute(() -> {
            mTermDAO.insert(term);
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Term term) {
        databaseExecutor.execute(() -> {
            mTermDAO.update(term);
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Term term) {
        databaseExecutor.execute(() -> {
            mTermDAO.delete(term);
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Assessment> getmAllAssessments() {
        databaseExecutor.execute(() -> {
            mAllAssessments = mAssessmentDAO.getAllAssessments();
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllAssessments;
    }

    public List<Assessment> getmAssociatedAssessmentsForCourse(int courseId) {
        databaseExecutor.execute(() -> {
            mAllAssessments = mAssessmentDAO.getAllAssessmentsRelatedToCourseId(courseId);
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllAssessments;
    }

    public void insert(Assessment assessment) {
        databaseExecutor.execute(() -> {
            mAssessmentDAO.insert(assessment);
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Assessment assessment) {
        databaseExecutor.execute(() -> {
            mAssessmentDAO.update(assessment);
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Assessment assessment) {
        databaseExecutor.execute(() -> {
            mAssessmentDAO.delete(assessment);
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



    //TermCourse Stuff
    public void removeCourseFromTerm(int termId, int courseId) {
        TermCourse termCourse = new TermCourse(termId, courseId);
        new Thread(() -> mTermCourseDAO.delete(termCourse)).start();
    }

    public void removeAllCoursesFromTerm(int termId) {
        new Thread(() -> mTermCourseDAO.deleteCoursesForTerm(termId)).start();
    }

    public List<Term> getTermsForCourse(int courseId) {
        List<Term> terms = new ArrayList<>();
        new Thread(() -> {
            List<TermCourse> termCourses = mTermCourseDAO.getTermsForCourse(courseId);

            for (TermCourse termCourse : termCourses) {
                Term term = mTermDAO.getTermById(termCourse.getTermId());
                if (term != null) {
                    terms.add(term);
                }
            }
        }).start();
        return terms;
    }

    public List<Course> getCoursesInTerm(int termId) {
        return mCourseDAO.getCoursesInTerm(termId);
    }

    public void addCourseToTerm(int termId, int courseId) {
        new Thread(() -> {
            TermCourse existingTermCourse = mTermCourseDAO.getTermCourse(termId, courseId);
            if (existingTermCourse == null) {
                TermCourse termCourse = new TermCourse(termId, courseId);
                mTermCourseDAO.insert(termCourse);
            }
        }).start();
    }

    public List<Course> getCoursesForTerm(int termId) {
        return mCourseDAO.getCoursesInTerm(termId);
    }




    //CourseAssessment Stuff
    public void removeAssessmentFromCourse(int courseId, int assessmentId) {
        CourseAssessment courseAssessment = new CourseAssessment(courseId, assessmentId);
        new Thread(() -> mCourseAssessmentDAO.delete(courseAssessment)).start();
    }

    public void removeAllAssessmentsFromCourse(int courseId) {
        new Thread(() -> mCourseAssessmentDAO.deleteAssessmentsForCourse(courseId)).start();
    }

    public List<Course> getCourseForAssessments(int assessmentId) {
        List<Course> courses = new ArrayList<>();
        new Thread(() -> {
            List<CourseAssessment> courseAssessments = mCourseAssessmentDAO.getAssessmentsForCourse(assessmentId);
            for (CourseAssessment courseAssessment : courseAssessments) {
                Course course = mCourseDAO.getCourseById(courseAssessment.getCourseId());
                if (course != null) {
                    courses.add(course);
                }
            }
        }).start();
        return courses;
    }

    public List<Assessment> getAssessmentsInCourse(int courseId) {
        return mAssessmentDAO.getAllAssessmentsRelatedToCourseId(courseId);
    }

    public void addAssessmentsToCourse(int courseId, int assessmentId) {
        new Thread(() -> {
            // Check if the TermCourse relationship already exists
            CourseAssessment existingCourseAssessment = mCourseAssessmentDAO.getCourseAssessment(courseId, assessmentId);
            if (existingCourseAssessment == null) {
                CourseAssessment courseAssessment = new CourseAssessment(courseId, assessmentId);
                mCourseAssessmentDAO.insert(courseAssessment);
            }
        }).start();
    }

    public List<Assessment> getAssessmentForCourse(int courseId) {
        return mAssessmentDAO.getAssessmentsInCourse(courseId);
    }
}
