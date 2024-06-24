package org.conferatus.timetable.backend.algorithm.scheduling;

import java.util.List;
import java.util.Map;

public record SubjectEvolve(Long id,
                            Map<TeacherEvolve, List<GroupEvolve>> teacherToGroups,
                            String subjectType,
                            int subId) {

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
