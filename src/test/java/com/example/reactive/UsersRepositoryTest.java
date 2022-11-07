package com.example.reactive;

import com.example.reactive.entity.Users;
import com.example.reactive.repository.UsersRepository;
import io.r2dbc.spi.ConnectionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.connectionfactory.init.CompositeDatabasePopulator;
import org.springframework.data.r2dbc.connectionfactory.init.ConnectionFactoryInitializer;
import org.springframework.data.r2dbc.connectionfactory.init.ResourceDatabasePopulator;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataR2dbcTest
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsersRepositoryTest {
    @Container
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer();

    static {
        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    static void resgiterDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", () -> "r2dbc:postgresql://"
                + postgreSQLContainer.getHost() + ":" + postgreSQLContainer.getFirstMappedPort()
                + "/" + postgreSQLContainer.getDatabaseName());
        registry.add("spring.r2dbc.username", () -> postgreSQLContainer.getUsername());
        registry.add("spring.r2dbc.password", () -> postgreSQLContainer.getPassword());
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

            ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
            initializer.setConnectionFactory(connectionFactory);

            CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
            populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
            populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("data.sql")));
            initializer.setDatabasePopulator(populator);

            return initializer;
        }
    }

    @Autowired
    UsersRepository usersRepository;

    @Test
    public void isUsersRepositoryExists() {
        assertNotNull(usersRepository);
    }

    @Test
    public void shouldGetAllUser() {
        Flux<Users> users = usersRepository.findAll();
        Users user = new Users("ab32cf8c-88ea-4c75-8a3b-e5969a25625e", "DOM", "12312312", "dom@inc.co", false);
        StepVerifier.create(users)
                .expectSubscription()
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    public void shouldGetOneUser() {
        Mono<Users> users = usersRepository.findUserByUuid("ab32cf8c-88ea-4c75-8a3b-e5969a25625e");
        Users user = new Users("ab32cf8c-88ea-4c75-8a3b-e5969a25625e", "DOM", "12312312", "dom@inc.co", false);
        StepVerifier.create(users)
                .expectSubscription()
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    public void shouldDeleteOneUser() {
        Mono<Users> users = usersRepository.deleteUserByUuid("ab32cf8c-88ea-4c75-8a3b-e5969a25625e");
        StepVerifier.create(users)
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    public void shouldInsertOneUser() {
        Users user = new Users("ab32cf8c-88ea-4c75-8a3b-e5969a25625q", "JACK", "1111111", "jack@jack.co", true);
        Mono<Users> users = usersRepository.save(user);
        StepVerifier.create(users)
                .expectSubscription()
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    public void shouldUpdateOneUser() {
        Users user = new Users("ab32cf8c-88ea-4c75-8a3b-e5969a25625e", "JACK", "1111111", "jack@jack.co", false);
        Mono<Users> users = usersRepository.save(user);
        StepVerifier.create(users)
                .expectSubscription()
                .expectNext(user)
                .verifyComplete();
    }
}
