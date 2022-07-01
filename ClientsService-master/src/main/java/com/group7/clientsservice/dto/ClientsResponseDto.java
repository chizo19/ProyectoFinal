package com.group7.clientsservice.dto;

import com.group7.clientsservice.model.Client;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientsResponseDto {

    private String id;
    private String documentType;
    private String documentNumber;
    private String firstName;
    private String lastName;
    private String businessName;
    private String type;
    private String profile;

    public static ClientsResponseDto fromModel(Client client) {
        return ClientsResponseDto.builder()
                .id(client.getId())
                .documentType(client.getDocumentType())
                .documentNumber(client.getDocumentNumber())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .businessName(client.getBusinessName())
                .type(client.getType())
                .profile(client.getProfile())
                .build();
    }
}
