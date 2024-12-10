package dev.emrx.gitfexchange.participants.service;

import java.util.List;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import dev.emrx.gitfexchange.participants.model.Participant;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Aspect
@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String from;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine engine;

    public EmailService(JavaMailSender javaMailSender, SpringTemplateEngine engine) {
        this.mailSender = javaMailSender;
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

    @AfterReturning(pointcut = "execution(* dev.emrx.gitfexchange.participants.model.ParticipantService.addParticipant(..))", returning = "participant")
    public void sendEmailFromRegistry(Participant participant) {
        Context context = new Context();
        context.setVariable("name", participant.getName());

        String content = engine.process("mail/registry-participant", context);
        sendEmail(participant.getEmail(), "¡Gracias por registrarte al intercambio de regalos!", content, false, true);
    }
  
    @AfterReturning(pointcut = "execution(* dev.emrx.gitfexchange.participants.model.ParticipantService.assignRandomGiftRecipients(..))", returning = "participants")
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

        String content = engine.process("mail/gift-participant", context);
        sendEmail(to, subject, content, false, true);
    }
}
