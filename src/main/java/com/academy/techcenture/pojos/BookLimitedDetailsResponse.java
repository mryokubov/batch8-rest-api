package com.academy.techcenture.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookLimitedDetailsResponse {
    private Integer id;
    private String name;
    private String type;
    private Boolean available;
}
