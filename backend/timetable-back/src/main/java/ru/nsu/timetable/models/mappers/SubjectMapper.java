package ru.nsu.timetable.models.mappers;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.SubjectDTO;
import ru.nsu.timetable.models.entities.Subject;
import ru.nsu.timetable.models.entities.TimeSlot;
import ru.nsu.timetable.repositories.TimeSlotRepository;

@Component
public class SubjectMapper {
    private final TimeSlotRepository timeSlotRepository;

    public SubjectMapper(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }
    public SubjectDTO toSubjectDTO(Subject subject) {
        return new SubjectDTO(subject.getId(), subject.getName(), subject.getType().name(), subject.getHours(),
                subject.getQualification(),
                subject.getOccupiedTimeSlots().stream()
                        .map(TimeSlot::getId)
                        .collect(Collectors.toSet()));
    }

    public Subject toSubject(SubjectDTO subjectDTO) {
        Subject subject = new Subject();
        subject.setName(subjectDTO.name());
        subject.setType(Subject.SubjectType.valueOf(subjectDTO.type()));
        subject.setHours(subjectDTO.hours());
        subject.setQualification(subjectDTO.qualification());
        subjectDTO.occupiedTimeSlotsIds().forEach(timeSlotId ->
                timeSlotRepository.findById(timeSlotId).ifPresent(subject::addOccupiedTimeSlot)
        );
        if (subjectDTO.id() != null) {
            subject.setId(subjectDTO.id());
        }
        return subject;
    }
}
