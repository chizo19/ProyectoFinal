package com.group7.clientsservice.dto;

import com.group7.clientsservice.exception.client.ClientsCreationException;
import com.group7.clientsservice.model.Client;
import lombok.*;

import java.util.Objects;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientsRequestDto {

    private String documentType;
    private String documentNumber;
    private String firstName;
    private String lastName;
    private String businessName;
    private String type;
    private String profile;

    public Client toModel() {
        if (Objects.isNull(documentType) || Objects.isNull(documentNumber) ||
                documentType.isEmpty() || documentNumber.isEmpty())
            throw new ClientsCreationException("DocumentType and DocumentNumber are required");

        if (documentType.equalsIgnoreCase("DNI") &&
                (Objects.isNull(firstName) || Objects.isNull(lastName) ||
                        firstName.isEmpty() || lastName.isEmpty()))
            throw new ClientsCreationException("FirstName and LastName are required");

        if (documentType.equalsIgnoreCase("RUC") &&
                (Objects.isNull(businessName) || businessName.isEmpty()))
            throw new ClientsCreationException("BusinessName is required");

        if (Objects.isNull(type) || type.isEmpty())
            throw new ClientsCreationException("Type is required");

        if (type.equalsIgnoreCase("personal") &&
                (!Objects.isNull(profile) && !profile.isEmpty() &&
                        !profile.equalsIgnoreCase("vip")))
            throw new ClientsCreationException("Profile "+profile+" is not valid");

        if (type.equalsIgnoreCase("business") &&
                (!Objects.isNull(profile) && !profile.isEmpty() &&
                        !profile.equalsIgnoreCase("pyme")))
            throw new ClientsCreationException("Profile "+profile+" is not valid");

        return Client.builder()
                .documentType(this.documentType)
                .documentNumber(this.documentNumber)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .businessName(this.businessName)
                .type(this.type)
                .profile(this.profile)
                .build();
    }

}
