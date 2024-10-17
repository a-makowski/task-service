package com.makowski.task_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Entity
@NoArgsConstructor
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NonNull
    @NotBlank(message = "owner cannot be blank")
    @Column(name = "owner_name")
    private String owner;

    @NonNull
    @NotBlank(message = "name cannot be blank")
    @Column(name = "title")
    private String title;

    @Column(name = "status")
    private boolean status;
}
