package com.group7.creditsservice.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "movementsLoans")
public class MovementLoan extends Movement {
    @NonNull
    private String loan;
}
