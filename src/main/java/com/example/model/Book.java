package com.example.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class Book {
    private Long bookId;
    private String author;
    private String title;
    private String ISBN;
    private Integer publishedYear;
    private Double rank;
}
