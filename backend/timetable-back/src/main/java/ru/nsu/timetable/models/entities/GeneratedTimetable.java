package ru.nsu.timetable.models.entities;

import java.util.List;

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
public class GeneratedTimetable {
    private boolean isGeneratedSuccessfully;
    private Timetable timetable;
    private List<UnplacedSubject> unplacedSubject;

}
