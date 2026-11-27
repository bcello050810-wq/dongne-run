package com.dongnerun.backend.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "running_course")
@Getter
public class RunningCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private CourseType type;

    @Column(name = "start_latitude")
    private double startLatitude;

    @Column(name = "start_longitude")
    private double startLongitude;

    @Column(name = "distance_meters")
    private int distanceMeters;

    private boolean flat;

    @Column(name = "flat_evidence")
    private String flatEvidence;

    private String directions;

    protected RunningCourse() {
    }

    public RunningCourse(String name, CourseType type, double startLatitude, double startLongitude,
                         int distanceMeters, boolean flat, String flatEvidence, String directions) {
        this.name = name;
        this.type = type;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.distanceMeters = distanceMeters;
        this.flat = flat;
        this.flatEvidence = flatEvidence;
        this.directions = directions;
    }
}
