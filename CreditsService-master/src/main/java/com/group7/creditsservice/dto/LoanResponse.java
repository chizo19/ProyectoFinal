package com.group7.creditsservice.dto;

import com.group7.creditsservice.model.Loan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanResponse {
    private String id;
    private String client;
    private double amount;
    private double balance;
    private int paymentDay;
    private double fullPayments;
    private int numberInstallments;

    public static LoanResponse fromModel(Loan loan) {
        LoanResponseBuilder response = LoanResponse.builder()
                .id(loan.getId())
                .client(loan.getClient())
                .amount(loan.getAmount())
                .balance(loan.getBalance())
                .paymentDay(loan.getPaymentDay())
                .fullPayments(loan.getFullPayment())
                .numberInstallments(loan.getNumberInstallments());

        return response.build();
    }
}
