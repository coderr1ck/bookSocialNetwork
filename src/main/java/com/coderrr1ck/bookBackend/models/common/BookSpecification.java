package com.coderrr1ck.bookBackend.models.common;

import com.coderrr1ck.bookBackend.models.Book;
import com.coderrr1ck.bookBackend.models.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class BookSpecification {

    public static Specification<Book> withOwnerId(User owner){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("owner"),owner);
    }
}
