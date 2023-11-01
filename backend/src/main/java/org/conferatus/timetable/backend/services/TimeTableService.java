package org.conferatus.timetable.backend.services;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.exception.ServerException;
import org.conferatus.timetable.backend.model.entity.Auditory;
import org.conferatus.timetable.backend.model.entity.Lesson;
import org.conferatus.timetable.backend.model.entity.StudyGroup;
import org.conferatus.timetable.backend.model.repos.AuditoryRepository;
import org.conferatus.timetable.backend.model.repos.LessonRepository;
import org.conferatus.timetable.backend.model.repos.StudyGroupRepository;
import org.conferatus.timetable.backend.model.repos.TeacherRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TimeTableService {

    private final LessonRepository lessonRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final AuditoryRepository auditoryRepository;
    private final TeacherRepository teacherRepository;

    private StudyGroup getGroupByNameOrThrow(String name) {
        return studyGroupRepository.findStudyGroupByName(name)
                .orElseThrow(() -> new ServerException(HttpStatus.NOT_FOUND,
                        "Group with name " + name + " does not exist"));
    }

    private Auditory getAuditoryByNameOrThrow(String name) {
        return auditoryRepository.findByName(name)
                .orElseThrow(() -> new ServerException(HttpStatus.NOT_FOUND,
                        "Group with name " + name + " does not exist"));
    }


    public List<Lesson> getLessonsByGroupName(String groupName) {
        StudyGroup group = getGroupByNameOrThrow(groupName);
        return group.getLessons();
    }

    public List<Lesson> getLessonsByAuditory(String auditoryName) {
        Auditory auditory = getAuditoryByNameOrThrow(auditoryName);
        return List.of();
    }

    public Map<String, Map<Integer, Lesson>> getTable(List<Lesson> lessons) {
        Map<String, Map<Integer, Lesson>> table = new HashMap<>();
        var days = List.of("Monday", "Tuesday");
        days.forEach((s -> {
            table.put(s, new HashMap<>());
        }));
        lessons.forEach((lesson -> {
            var day = days.get(lesson.getWeekDay());
            table.get(day).put(lesson.getNumberOfTime(), lesson);
        }));
        return table;
    }

    public Map<String, Map<Integer, Lesson>> getTableByGroupName(String groupName) {
        return getTable(getLessonsByGroupName(groupName));
    }
}
