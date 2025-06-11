package com.recupera.item.back.recupera.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.recupera.item.back.recupera.domain.enums.Perfis;
import com.recupera.item.back.recupera.service.UsuarioService;
import com.recupera.item.back.recupera.domain.dto.usuario.DTOCreatedUsuario;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public void run(String... args) throws Exception {
        if (usuarioService.listarUsuarios().isEmpty()) {
            // Assuming DTOCreatedUsuario has similar fields and a suitable constructor or setters
            DTOCreatedUsuario adminDto = new DTOCreatedUsuario(
                "admin",
                "admin@hotmail.com",
                "Admin1234!",
                Perfis.Administrador
            );

            usuarioService.createUsuario(adminDto);

            System.out.println("Administrador iniciado com sucesso!");
        } else {
            System.out.println("Administrador já existe.");
        }
    }
}
