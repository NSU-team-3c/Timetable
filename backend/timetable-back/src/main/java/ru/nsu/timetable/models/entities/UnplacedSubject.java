package ru.nsu.timetable.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnplacedSubject {
    private Group group;
    private Subject subject;
    private User teacher;
    private Event.AudienceType audienceType;
}
