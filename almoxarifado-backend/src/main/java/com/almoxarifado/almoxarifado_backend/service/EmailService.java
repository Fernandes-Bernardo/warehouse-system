package com.almoxarifado.almoxarifado_backend.service;

import com.almoxarifado.almoxarifado_backend.model.Produto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${alerta.email.destino}")
    private String emailDestino;

    @Value("${spring.mail.username}")
    private String remetente;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarAlertaEstoqueBaixo(Produto produto) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setFrom(remetente);
        mensagem.setTo(emailDestino);
        mensagem.setSubject("Estoque Baixo: " + produto.getNome());
        mensagem.setText(
            "Olá,\n\n" +
            "O produto \"" + produto.getNome() + "\" está com estoque abaixo do mínimo.\n\n" +
            "Quantidade atual: " + produto.getQuantidade() + "\n" +
            "Estoque mínimo: " + produto.getEstoqueMinimo() + "\n\n" +
            "Verifique se é necessário reabastecer.\n\n" +
            "Atenciosamente,\n" +
            "Sistema Almoxarifado"
        );

        mailSender.send(mensagem);
    }
}