package com.recupera.item.back.recupera.service;

import org.springframework.stereotype.Service;

@Service
public class CorpoEmailService {

    public String gerarCorpoEmailConfirmacao(String link){
        return """
                <html>
                    <body style="background-color: #f4f4f4; padding: 20px; font-family: Arial, sans-serif;">
                        <div style="max-width: 600px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1);">
                            <img src="https://i.pinimg.com/originals/f0/ae/8b/f0ae8bc86ab9b5459880ea9e8894774d.gif" alt="Logo" style="display: block; margin: 0 auto 20px; width: 150px;">
                            <h2 style="text-align: center; color: #333;">Confirmação de E-mail</h2>
                            <p style="text-align: center; color: #555;">Clique no botão abaixo para confirmar seu e-mail:</p>
                            <div style="text-align: center; margin-top: 30px;">
                                <a href="%s" style="background-color: #007bff; color: white; text-decoration: none; padding: 12px 25px; border-radius: 5px; display: inline-block;">Confirmar E-mail</a>
                            </div>
                            <p style="text-align: center; margin-top: 20px; color: #aaa;">Esse link expira em 24 horas.</p>
                        </div>
                    </body>
                </html>
                """.formatted(link);
    }

    public String gerarCorpoEmailRecuperacao(String link){
        return """
                <html>
                    <body style="background-color: #f4f4f4; padding: 20px; font-family: Arial, sans-serif;">
                        <div style="max-width: 600px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1);">
                            <img src="https://i.pinimg.com/originals/f0/ae/8b/f0ae8bc86ab9b5459880ea9e8894774d.gif" alt="Logo" style="display: block; margin: 0 auto 20px; width: 150px;">
                            <h2 style="text-align: center; color: #333;">Recuperação de Senha</h2>
                            <p style="text-align: center; color: #555;">Clique no botão abaixo para redefinir sua senha:</p>
                            <div style="text-align: center; margin-top: 30px;">
                                <a href="%s" style="background-color: #007bff; color: white; text-decoration: none; padding: 12px 25px; border-radius: 5px; display: inline-block;">Redefinir Senha</a>
                            </div>
                            <p style="text-align: center; margin-top: 20px; color: #aaa;">Esse link expira em 1 hora.</p>
                        </div>
                    </body>
                </html>
                """.formatted(link);
    }
}
