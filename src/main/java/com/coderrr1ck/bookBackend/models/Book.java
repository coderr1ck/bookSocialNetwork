package com.coderrr1ck.bookBackend.models;

import com.coderrr1ck.bookBackend.models.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Book extends BaseEntity {

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

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonBackReference("user-books")
    @NotNull
    private User owner;

    @OneToMany(mappedBy = "book" )
    @JsonManagedReference
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    @JsonManagedReference("book-transactions")
    private List<BookTransactionHistory> bookTransactions;

}
