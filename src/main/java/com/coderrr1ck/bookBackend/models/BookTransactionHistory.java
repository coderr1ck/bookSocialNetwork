package com.coderrr1ck.bookBackend.models;

import com.coderrr1ck.bookBackend.models.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BookTransactionHistory extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-transactions")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @JsonBackReference("book-transactions")
    private Book book;

    private boolean isReturned;
    private boolean isReturnApproved;

}
