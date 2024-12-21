package ru.nsu.timetable.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "operations")
@NoArgsConstructor
@Data
public class Operations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(timezone = "Asia/Novosibirsk")
    @Column(name = "creation_time")
    private Date dateOfCreation;

    @Column(name = "description")
    private String description;

    @Column(name = "username")
    private String userAccount;
}
