package com.group7.yanki.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Slf4j
@Document(collection = "yankis")
public class Yanki {
    @Id
    private String id;
    private Long from;
    private Long to;
    private String paymentType;
    private Double amount;
    private Double quantity;
    private Double sellRate;
    private Double unitPrice;
    private LocalDate date;
    private String status;
}
