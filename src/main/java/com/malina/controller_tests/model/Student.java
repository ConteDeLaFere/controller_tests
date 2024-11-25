package com.malina.controller_tests.model;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Student {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(255)")
    private String name;
    private int age;

    @OneToOne(mappedBy = "student")
    @JoinColumn(name = "student_id")
    private Avatar avatar;

    @ManyToOne
    @JoinColumn(name = "faculty_id", referencedColumnName = "id")
    private Faculty facultyStudent;

}
