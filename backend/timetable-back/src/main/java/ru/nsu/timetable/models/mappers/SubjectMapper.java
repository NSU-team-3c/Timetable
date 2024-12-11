package ru.nsu.timetable.models.mappers;

import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.SubjectDTO;
import ru.nsu.timetable.models.entities.Subject;

@Component
public class SubjectMapper {
    public SubjectDTO toSubjectDTO(Subject subject) {
        return new SubjectDTO(subject.getId(), subject.getName(), subject.getType().name(), subject.getHours(),
                subject.getQualification());
    }

    public Subject toSubject(SubjectDTO subjectDTO) {
        Subject subject = new Subject();
        subject.setName(subjectDTO.name());
        subject.setType(Subject.AudienceType.valueOf(subjectDTO.type()));
        subject.setHours(subjectDTO.hours());
        subject.setQualification(subjectDTO.qualification());
        if (subjectDTO.id() != null) {
            subject.setId(subjectDTO.id());
        }
        return subject;
    }
}
