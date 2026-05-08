package dev.gabrielsales.user_service.user;

import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserDataInitializer {

    @Bean
    CommandLineRunner seedUsers(UserRepository userRepository) {
        return args -> {
            if (userRepository.count() > 0) {
                return;
            }

            var users = List.of(
                    new User("Ana Souza", "11111111111", "ana.souza@example.com", "secret123", UserType.COMMON),
                    new User("Bruno Lima", "22222222222", "bruno.lima@example.com", "secret123", UserType.COMMON),
                    new User("Carla Mendes", "33333333333", "carla.mendes@example.com", "secret123", UserType.COMMON),
                    new User("Diego Alves", "44444444444", "diego.alves@example.com", "secret123", UserType.COMMON),
                    new User("Elisa Rocha", "55555555555", "elisa.rocha@example.com", "secret123", UserType.COMMON),
                    new User("Loja Centro", "66666666666", "loja.centro@example.com", "secret123", UserType.MERCHANT),
                    new User("Mercado Nova Era", "77777777777", "mercado.novaera@example.com", "secret123", UserType.MERCHANT),
                    new User("Varejo Prime", "88888888888", "varejo.prime@example.com", "secret123", UserType.MERCHANT),
                    new User("Outlet Popular", "99999999999", "outlet.popular@example.com", "secret123", UserType.MERCHANT),
                    new User("Emporio Brasil", "10101010101", "emporio.brasil@example.com", "secret123", UserType.MERCHANT)
            );

            userRepository.saveAll(users);
        };
    }
}
