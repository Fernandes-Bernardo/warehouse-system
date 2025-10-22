package com.almoxarifado.almoxarifado_backend.config;

import com.almoxarifado.almoxarifado_backend.model.Usuario;
import com.almoxarifado.almoxarifado_backend.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.count() == 0) {
                // Maciel como ADMIN
                Usuario maciel = new Usuario();
                maciel.setUsername("maciel");
                maciel.setPassword(passwordEncoder.encode("maciel123"));
                maciel.setRole("ADMIN");
                usuarioRepository.save(maciel);

                // Admin padrão
                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole("ADMIN");
                usuarioRepository.save(admin);

                // Usuário comum João
                Usuario joao = new Usuario();
                joao.setUsername("joao");
                joao.setPassword(passwordEncoder.encode("joao123"));
                joao.setRole("USER");
                usuarioRepository.save(joao);

                // Usuário comum Maria
                Usuario maria = new Usuario();
                maria.setUsername("maria");
                maria.setPassword(passwordEncoder.encode("maria123"));
                maria.setRole("USER");
                usuarioRepository.save(maria);

                // Usuário comum Pedro
                Usuario pedro = new Usuario();
                pedro.setUsername("pedro");
                pedro.setPassword(passwordEncoder.encode("pedro123"));
                pedro.setRole("USER");
                usuarioRepository.save(pedro);

                System.out.println("✅ Usuários de teste criados com sucesso!");
            }
        };
    }
}