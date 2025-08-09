package com.coderrr1ck.bookBackend.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    private String title;

    @NotNull
    private String authorName;

    @NotNull
    @Column(unique = true)
    private String isbn;

    private String synopsis;

    @NotNull
    private boolean sharable;

    @NotNull
    private boolean archived;

    private String bookCover;

    private boolean isActive = true;

    @OneToMany(mappedBy = "book" )
    @JsonManagedReference
    private List<Feedback> feedbacks;

}
