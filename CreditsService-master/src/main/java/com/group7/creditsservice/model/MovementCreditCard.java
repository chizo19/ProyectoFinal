package com.group7.creditsservice.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@SuperBuilder
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Document(collection="movementsCreditCard")
public class MovementCreditCard extends Movement {

    @NonNull
    private String credit;
}

