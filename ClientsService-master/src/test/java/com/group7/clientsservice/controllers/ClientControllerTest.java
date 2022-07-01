package com.group7.clientsservice.controllers;

import com.group7.clientsservice.dto.ClientsRequestDto;
import com.group7.clientsservice.dto.ClientsResponseDto;
import com.group7.clientsservice.dto.ProductsResponseDto;
import com.group7.clientsservice.model.Accounts;
import com.group7.clientsservice.model.Client;
import com.group7.clientsservice.model.Credit;
import com.group7.clientsservice.repository.ClientRepository;
import com.group7.clientsservice.utils.WebClientUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ClientControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ClientRepository clientRepository;
    @MockBean
    private WebClientUtils webClientUtils;
    private Client client;
    private ClientsRequestDto clientDto;

    private ClientsRequestDto clientsRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        client = Client.builder()
                .id("123")
                .documentType("DNI")
                .documentNumber("74910877")
                .firstName("Cristian")
                .lastName("Paredes")
                .businessName("")
                .type("personal")
                .profile("vip")
                .build();

        clientDto = ClientsRequestDto.builder()
                .documentType("DNI")
                .documentNumber("74910877")
                .firstName("Cristian")
                .lastName("Paredes")
                .businessName("")
                .type("personal")
                .profile("vip")
                .build();


        clientsRequestDto = ClientsRequestDto.builder()
                .documentType("DNI")
                .documentNumber("75596874")
                .firstName("Cristian")
                .lastName("Paredes")
                .businessName("")
                .type("personal")
                .profile("vip")
                .build();
    }
    @Test
    void getClientAll() {
        when(clientRepository.findAll()).thenReturn(Flux.just(client));
        List<Client> responseClients = this.webTestClient
                .get()
                .uri("/clients")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Client.class)
                .returnResult()
                .getResponseBody();
        assertNotNull(responseClients);
    }

    @Test
    void getClientById() {
        when(clientRepository.findById(any(String.class))).thenReturn(Mono.just(client));
        Client responseClient = this.webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/clients/{id}").build("123"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Client.class)
                .returnResult()
                .getResponseBody();
        assertNotNull(responseClient);
        assertEquals("123", responseClient.getId());
    }

    @Test
    void deleteClient() {
        when(clientRepository.findById(any(String.class))).thenReturn(Mono.just(client));
        when(clientRepository.delete(any(Client.class))).thenReturn(Mono.empty());
        this.webTestClient
                .delete()
                .uri(uriBuilder -> uriBuilder.path("/clients/{id}").build("123"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Client.class);
    }

    @Test
    void saveCliente() {
        when(clientRepository.findClientByDocumentTypeAndDocumentNumber(any(String.class), any(String.class)))
                .thenReturn(Mono.empty());
        when(clientRepository.insert(any(Client.class)))
                .thenReturn(Mono.just(client));
        this.webTestClient
                .post()
                .uri(uriBuilder -> uriBuilder.path("/clients").build())
                .bodyValue(clientsRequestDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ClientsResponseDto.class)
                .returnResult()
                .getResponseBody();
    }

    @Test
    void updateCliente() {
        when(clientRepository.findById(any(String.class))).thenReturn(Mono.just(client));
        when(clientRepository.save(any(Client.class))).thenReturn(Mono.just(client));
        this.webTestClient
                .put()
                .uri(uriBuilder -> uriBuilder.path("/clients/{id}").build("123"))
                .bodyValue(clientsRequestDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClientsResponseDto.class)
                .returnResult()
                .getResponseBody();
    }

    @Test
    void getProductsByClient() {

        Accounts accounts =  new Accounts();
        accounts.setId("123");
        accounts.setClient("456");
        accounts.setType("personal");
        accounts.setClientProfile("vip");

        Credit credit = new Credit();
        credit.setId("123");
        credit.setClient("456");
        credit.setAmount(50);

        when(clientRepository.findById(any(String.class))).thenReturn(Mono.just(client));
        when(webClientUtils.getAccounts(any(String.class))).thenReturn(Flux.just(accounts));
        when(webClientUtils.getCredits(any(String.class))).thenReturn(Flux.just(credit));
        this.webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/clients/{id}/products").build("123"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductsResponseDto.class)
                .returnResult()
                .getResponseBody();
    }

}