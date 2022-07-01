package com.group7.clientsservice.dto;

import com.group7.clientsservice.model.Accounts;
import com.group7.clientsservice.model.Client;
import com.group7.clientsservice.model.Credit;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductsResponseDto {
    private Client client;
    private List<Accounts> accounts;
    private List<Credit> credit;

    public ProductsResponseDto(Client client) {
        this.client = client;
    }
}
