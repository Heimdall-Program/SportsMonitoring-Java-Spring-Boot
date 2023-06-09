package com.example.SportMonitoring.Models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "stats")
public class Stat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "value", nullable = false)
    private Double value;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "type_id", nullable = false)
    private StatType type;

    @Column(name = "assigned_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date assignedDate;


    @PrePersist
    public void prePersist() {
        assignedDate = new Date();
    }

    public StatType getType() {
        return type;
    }

    public void setType(StatType type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Stat() {
    }

    public Stat(String name, String description, Double value) {
        this.name = name;
        this.description = description;
        this.value = value;
    }


    public Date getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(Date assignedDate) {
        this.assignedDate = assignedDate;
    }
}
