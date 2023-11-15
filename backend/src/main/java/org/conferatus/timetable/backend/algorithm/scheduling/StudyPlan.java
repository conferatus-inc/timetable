package org.conferatus.timetable.backend.algorithm.scheduling;

import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@ToString
public class StudyPlan {
    public List<Subject> subjects;
    public List<Group> groups;
}
