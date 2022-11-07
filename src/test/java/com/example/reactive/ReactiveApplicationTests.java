package com.example.reactive;

import com.example.reactive.handlers.UsersHandler;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.example.reactive.entity.Users;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReactiveApplicationTests {

    public static WireMockServer wireMockRule = new WireMockServer(options().dynamicPort());

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("localhost", wireMockRule::baseUrl);
    }

    @BeforeAll
    public static void beforeAll() {
        wireMockRule.start();
    }

    @AfterAll
    public static void afterAll() {
        wireMockRule.stop();
    }

    @AfterEach
    public void afterEach() {
        wireMockRule.resetAll();
    }

    @Autowired
    private UsersHandler usersHandler;

    @Test
    void itShouldReturnOkWhenGetAllUser() {
        List<Users> mockUsers = new ArrayList<>();
        Users user = new Users("ab32cf8c-88ea-4c75-8a3b-e5969a25625q", "JACK", "1111111", "jack@jack.co", true);
        mockUsers.add(user);

        wireMockRule.stubFor(get(urlEqualTo("/api/users"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(String.valueOf(mockUsers))
                )
        );

        MockServerHttpRequest request = MockServerHttpRequest.get("/api/users")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        ServerRequest serverRequest =
                ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
        Mono<ServerResponse> usersFlux = usersHandler.getAllUsers(serverRequest);

        StepVerifier.create(usersFlux)
                .expectNextMatches(response -> HttpStatus.OK.equals(response.statusCode()))
                .expectComplete()
                .verify();
    }

    @Test
    void itShouldReturnOkWhenGetOneUser() {
        List<Users> mockUsers = new ArrayList<>();
        Users user = new Users("ab32cf8c-88ea-4c75-8a3b-e5969a25625q", "JACK", "1111111", "jack@jack.co", true);
        mockUsers.add(user);

        wireMockRule.stubFor(get(urlEqualTo("/api/users/ab32cf8c-88ea-4c75-8a3b-e5969a25625q"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(String.valueOf(mockUsers))
                )
        );

        MockServerHttpRequest request = MockServerHttpRequest.get("/api/users/{uuid}")
                .queryParam( "ab32cf8c-88ea-4c75-8a3b-e5969a25625q")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        ServerRequest serverRequest =
                ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
        serverRequest.pathVariable("uuid");
        Mono<ServerResponse> usersFlux = usersHandler.getUser(serverRequest);

        StepVerifier.create(usersFlux)
                .expectNextMatches(response -> HttpStatus.OK.equals(response.statusCode()))
                .expectComplete()
                .verify();
    }

    @Test
    void itShouldReturnOkWhenAddUser() {
        List<Users> mockUsers = new ArrayList<>();
        Users user = new Users("ab32cf8c-88ea-4c75-8a3b-e5969a25625q", "JACK", "1111111", "jack@jack.co", true);
        mockUsers.add(user);

        wireMockRule.stubFor(post(urlEqualTo("/api/users"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(String.valueOf(mockUsers))
                )
        );

        MockServerHttpRequest request = MockServerHttpRequest.post("http://example.com")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        ServerRequest serverRequest =
                ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
        Mono<ServerResponse> usersFlux = usersHandler.addUser(serverRequest);

        StepVerifier.create(usersFlux)
                .expectComplete()
                .verify();
    }

    @Test
    void itShouldReturnOkWhenUpdateUser() {
        List<Users> mockUsers = new ArrayList<>();
        Users user = new Users("ab32cf8c-88ea-4c75-8a3b-e5969a25625q", "JACK", "1111111", "jack@jack.co", true);
        mockUsers.add(user);

        wireMockRule.stubFor(put(urlEqualTo("/api/users/ab32cf8c-88ea-4c75-8a3b-e5969a25625q"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(String.valueOf(mockUsers))
                )
        );

        MockServerHttpRequest request = MockServerHttpRequest.put("/api/users")
                .queryParam("uuid", "ab32cf8c-88ea-4c75-8a3b-e5969a25625q")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        ServerRequest serverRequest =
                ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
        Mono<ServerResponse> usersFlux = usersHandler.updateUser(serverRequest);

        StepVerifier.create(usersFlux)
                .expectNextMatches(response -> HttpStatus.OK.equals(response.statusCode()))
                .expectComplete()
                .verify();
    }

    @Test
    void itShouldReturnOkWhenDeleteUser() {
        wireMockRule.stubFor(put(urlEqualTo("/api/users/ab32cf8c-88ea-4c75-8a3b-e5969a25625q"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody("User ab32cf8c-88ea-4c75-8a3b-e5969a25625q has been deleted!")
                )
        );

        MockServerHttpRequest request = MockServerHttpRequest.delete("/api/users")
                .queryParam("uuid", "ab32cf8c-88ea-4c75-8a3b-e5969a25625q")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        ServerRequest serverRequest =
                ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
        Mono<ServerResponse> usersFlux = usersHandler.deleteUser(serverRequest);

        StepVerifier.create(usersFlux)
                .expectNextMatches(response -> HttpStatus.OK.equals(response.statusCode()))
                .expectComplete()
                .verify();
    }
}
