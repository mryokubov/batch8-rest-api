package com.academy.techcenture.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class BookFullDetailsResponse {

    private int id;
    private String name;
    private String author;
    private String isbn;
    private String type;
    private double price;
    @JsonProperty("current-stock")
    private int currentStock;
    private boolean available;

}
