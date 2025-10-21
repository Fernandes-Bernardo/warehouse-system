package com.almoxarifado.almoxarifado_backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.almoxarifado.almoxarifado_backend.model.Usuario;
import com.almoxarifado.almoxarifado_backend.repository.UsuarioRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, PasswordEncoder encoder) {
        return args -> {

            // Usuário ADMIN
            if (usuarioRepository.findByUsername("admin").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("admin123"));
                admin.setRole("ADMIN");
                usuarioRepository.save(admin);
                System.out.println("Usuário ADMIN criado: admin/admin123");
            }

            // Usuário USER
            if (usuarioRepository.findByUsername("user").isEmpty()) {
                Usuario user = new Usuario();
                user.setUsername("user");
                user.setPassword(encoder.encode("user123"));
                user.setRole("USER");
                usuarioRepository.save(user);
                System.out.println("Usuário USER criado: user/user123");
            }

            // Usuário ESTOQUISTA
            if (usuarioRepository.findByUsername("estoquista").isEmpty()) {
                Usuario estoquista = new Usuario();
                estoquista.setUsername("estoquista");
                estoquista.setPassword(encoder.encode("estoque456"));
                estoquista.setRole("USER");
                usuarioRepository.save(estoquista);
                System.out.println("Usuário ESTOQUISTA criado: estoquista/estoque456");
            }

            // Usuário VISITANTE
            if (usuarioRepository.findByUsername("visitante").isEmpty()) {
                Usuario visitante = new Usuario();
                visitante.setUsername("visitante");
                visitante.setPassword(encoder.encode("visita789"));
                visitante.setRole("USER");
                usuarioRepository.save(visitante);
                System.out.println("Usuário VISITANTE criado: visitante/visita789");
            }

            // Usuário MACIEL (ADMIN)
            if (usuarioRepository.findByUsername("maciel").isEmpty()) {
                Usuario maciel = new Usuario();
                maciel.setUsername("maciel");
                maciel.setPassword(encoder.encode("maciel123"));
                maciel.setRole("ADMIN");
                usuarioRepository.save(maciel);
                System.out.println("Usuário MACIEL criado: maciel/maciel123");
            }
        };
    }
}