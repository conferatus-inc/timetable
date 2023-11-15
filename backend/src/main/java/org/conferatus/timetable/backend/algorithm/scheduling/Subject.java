package org.conferatus.timetable.backend.algorithm.scheduling;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class Subject {
    public String id;
    public int seminarAmount;
    public int lectureAmount;
    public List<Teacher> seminarTeacher;
    public Teacher lectureTeacher;

    @Override
    public String toString() {
        return id;
    }
}
