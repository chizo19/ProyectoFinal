package com.group7.yanki.dto;

import com.group7.yanki.model.Yanki;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class YankiRequest {
    private String id;
    private Long from;
    private LocalDate date;
    private Long to;
    private Double amount;

    private Double unitPrice;
    private String paymentType;
    private Double quantity;
    private String status;
    private Double sellRate;
    public Yanki toModel() {
        return Yanki.builder()
                .id(this.id)
                .from(this.from)
                .to(this.to)
                .amount(this.amount)
                .date(this.date)
                .paymentType(this.paymentType)
                .status(this.status)
                .quantity(this.quantity)
                .sellRate(this.sellRate)
                .unitPrice(this.unitPrice)
                .build();
    }
}
