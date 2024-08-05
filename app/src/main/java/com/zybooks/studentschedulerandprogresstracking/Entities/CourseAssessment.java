package com.zybooks.studentschedulerandprogresstracking.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
    @Entity(tableName = "course_assessment")
    public class CourseAssessment {
        @PrimaryKey(autoGenerate = true)
        private int id;
        private int courseId;
        private int assessmentId;

        public CourseAssessment(int courseId, int assessmentId) {
            this.courseId = courseId;
            this.assessmentId = assessmentId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCourseId() {
            return courseId;
        }

        public void setCourseId(int courseId) {
            this.courseId = courseId;
        }

        public int getAssessmentId() {
            return assessmentId;
        }

        public void setAssessmentId(int assessmentId) {
            this.assessmentId = assessmentId;
        }

    }
