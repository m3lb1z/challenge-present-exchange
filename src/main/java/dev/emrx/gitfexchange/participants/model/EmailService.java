package dev.emrx.gitfexchange.participants.model;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;

@Aspect
@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("classpath:templates/email-gift-template.st")
    private Resource giftEmailTemplate;

    @Value("classpath:templates/email-registry-template.st")
    private Resource registryEmailTemplate;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @AfterReturning(pointcut = "execution(* dev.emrx.gitfexchange.participants.model.ParticipantService.addParticipant(..))", returning = "participant")
    public void sendRegistryEmail(Participant participant) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("notify@beluhome.pe");
            helper.setTo(participant.getEmail());
            helper.setSubject("¡Gracias por registrarte al intercambio de regalos!");

            // Cargar y procesar el template
            String htmlContent = loadAndProcessRegistryEmailTemplate(participant.getName());
            helper.setText(htmlContent, true); // El segundo parámetro indica que el contenido es HTML

            javaMailSender.send(message);
        } catch (MessagingException | IOException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private String loadAndProcessRegistryEmailTemplate(String name) throws IOException {
        // Cargar el template
        String templateContent = new String(registryEmailTemplate.getInputStream().readAllBytes());

        // Reemplazar el placeholder con el valor real
        templateContent = templateContent.replace("{name}", name);

        return templateContent;
    }


    public void sendGiftEmail(String to, String subject, String sender) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("notify@beluhome.pe");
            helper.setTo(to);
            helper.setSubject(subject);

            // Cargar y procesar el template
            String htmlContent = loadAndProcessGiftEmailTemplate(sender);
            helper.setText(htmlContent, true); // El segundo parámetro indica que el contenido es HTML

            javaMailSender.send(message);
        } catch (MessagingException | IOException e) {
            // Manejar la excepción, por ejemplo, registrando el error o lanzando una excepción personalizada
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private String loadAndProcessGiftEmailTemplate(String sender) throws IOException {
        // Cargar el template
        String templateContent = new String(giftEmailTemplate.getInputStream().readAllBytes());

        // Reemplazar el placeholder con el valor real
        templateContent = templateContent.replace("{sender}", sender);

        return templateContent;
    }
}
