package dev.emrx.gitfexchange.participants.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import dev.emrx.gitfexchange.participants.model.Participant;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    
    Logger log = LoggerFactory.getLogger(this.getClass()); 

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine engine;
    @Value("${spring.mail.username}")
    private String from;

    public EmailService(JavaMailSender mailSender, SpringTemplateEngine engine) {
        this.mailSender = mailSender;
        this.engine = engine;
    }

    public void sendEmailFromTemplate(String email, String templateName, String subject) {
        Context context = new Context();
        
        String content = engine.process(templateName, context);
        sendEmail(email, subject, content, false, true);
    }

    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            var helper = new MimeMessageHelper(message, isMultipart, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(from);
            helper.setText(content, isHtml);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendEmailFromRegistry(Participant participant) {
        Context context = new Context();
        context.setVariable("name", participant.getName());
        log.info(participant.getEmail());
        String content = engine.process("mail/registry-participant", context);
        sendEmail(participant.getEmail(), "¡Gracias por registrarte al intercambio de regalos!", content, false, true);
    }
  
    public void sendEmailFromNotifyDraw(List<Participant> participants) {
        String subject = "Your Gift Exchange Assignment";
        for (Participant giver : participants) {
            Participant recipient = giver.getGiftRecipient();
            _sendEmailFromNotifyDraw(giver.getEmail(), subject, recipient.getName());
        }
    }
    
    private void _sendEmailFromNotifyDraw(String to, String subject, String sender) {
        Context context = new Context();
        context.setVariable("sender", sender);
        log.info(String.format("%s %s %s", to, subject, sender));
        String content = engine.process("mail/gift-participant", context);
        sendEmail(to, subject, content, false, true);
    }
}
