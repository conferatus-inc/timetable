package org.conferatus.timetable.backend.services;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.exception.ServerException;
import org.conferatus.timetable.backend.model.SubjectType;
import org.conferatus.timetable.backend.model.entity.Subject;
import org.conferatus.timetable.backend.model.repos.SubjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public Subject getSubjectByIdOrThrow(Long id) {
        return subjectRepository.findSubjectById(id)
                .orElseThrow(() -> new ServerException(HttpStatus.NOT_FOUND,
                        "Subject with id " + id + " does not exist"));
    }

    private Subject getSubjectByNameOrThrow(String name) {
        return subjectRepository.findSubjectByName(name)
                .orElseThrow(() -> new ServerException(HttpStatus.NOT_FOUND,
                        "Subject with name " + name + " does not exist"));
    }

    private void notExistsByNameOrThrow(String name) {
        subjectRepository.findSubjectByName(name).ifPresent(subject -> {
            throw new ServerException(HttpStatus.BAD_REQUEST,
                    "Subject with name " + name + " already exists");
        });
    }

    public Subject getSubject(Long id) {
        return getSubjectByIdOrThrow(id);
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Subject addSubject(String subjectName, SubjectType subjectType) {
        notExistsByNameOrThrow(subjectName);
        return subjectRepository.save(Subject.builder().name(subjectName).subjectType(subjectType).build());
    }


    public Subject updateSubject(String previousSubjectName, String newSubjectName) {
        Subject subject = getSubjectByNameOrThrow(previousSubjectName);
        if (newSubjectName.equals(previousSubjectName)) {
            return subject;
        }
        notExistsByNameOrThrow(newSubjectName);
        subject.setName(newSubjectName);
        return subjectRepository.save(subject);
    }


    public Subject deleteSubjectOrThrow(String subjectName) {
        Subject subject = getSubjectByNameOrThrow(subjectName);
        subjectRepository.delete(subject);
        return subject;
    }
}
