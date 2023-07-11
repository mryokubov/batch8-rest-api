package com.academy.techcenture.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderResponsePayload {

    private String id;
    private int bookId;
    private String customerName;
    private String createdBy;
    private int quantity;
    private long timestamp;

}
