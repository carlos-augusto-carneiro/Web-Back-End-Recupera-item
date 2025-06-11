package com.recupera.item.back.recupera.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.recupera.item.back.recupera.domain.dto.usuario.DTOCreatedUsuario;
import com.recupera.item.back.recupera.domain.enums.Perfis;
import com.recupera.item.back.recupera.service.EmailConfirmacaoTokenService;
import com.recupera.item.back.recupera.service.UsuarioService;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EmailConfirmacaoTokenService emailConfirmacaoTokenService;

    @Override
    public void run(String... args) throws Exception {
        if (usuarioService.listarUsuarios().isEmpty()) {
            // Assuming DTOCreatedUsuario has similar fields and a suitable constructor or setters
            DTOCreatedUsuario adminDto = new DTOCreatedUsuario(
                "admin",
                "carlosaugustocarneiro@alu.ufc.br",
                "Admin1234!",
                Perfis.Administrador
            );

            var usuario = usuarioService.createUsuario(adminDto);
            emailConfirmacaoTokenService.enviarConfirmacaoEmail(usuario);

            System.out.println("Administrador iniciado com sucesso!");
        } else {
            System.out.println("Administrador já existe.");
        }
    }
}
