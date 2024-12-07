package dev.emrx.gitfexchange.participants.model;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssignmentService {

    private ParticipantRepository participantRepository;
    private EmailService emailService;

    public AssignmentService(ParticipantRepository participantRepository, EmailService emailService) {
        this.participantRepository = participantRepository;
        this.emailService = emailService;
    }

    @Transactional
    public void assignGiftRecipients() {
        List<Participant> participants = new ArrayList<>();
        participantRepository.findAll().forEach(participants::add);

        if (participants.size() <= 2) {
            throw new IllegalStateException("El número de participantes debe ser mayor a 2 para realizar asignaciones.");
        }

        List<Long> participantIds = participants.stream()
                                                .map(Participant::getId)
                                                .collect(Collectors.toList());

        Collections.shuffle(participantIds);
        for (int i = 0; i < participants.size(); i++) {
            Participant giver = participants.get(i);
            Long recipientId = null;

            for (Long id : participantIds) {
                if (!id.equals(giver.getId())) {
                    recipientId = id;
                    break;
                }
            }

            if (recipientId == null) {
                throw new IllegalStateException("No se encontró un destinatario válido para el participante con ID: " + giver.getId());
            }

            participantIds.remove(recipientId);
            Participant recipient = participantRepository.findById(recipientId).orElse(null);
            giver.setGiftRecipient(recipient);
        }

        participantRepository.saveAll(participants);
    }

    private void _sendAssignmentEmail(Participant giver, Participant recipient) {
        String subject = "Your Gift Exchange Assignment";
        // String body = "Hello " + giver.getName() + ", you have been assigned to buy a gift for " + recipient.getName() + ".";
        emailService.sendGiftEmail(giver.getEmail(), subject, recipient.getName());
    }

    @Transactional(readOnly = true)
    public List<Participant> getParticipants() {
        List<Participant> participants = new ArrayList<>();
        participantRepository.findAll().forEach(participants::add);

        if(!participants.isEmpty()) {
            for(Participant participant : participants) {
                if(participant.getGiftRecipient() != null) {
                    _sendAssignmentEmail(participant, participant.getGiftRecipient());
                }
            }
        }

        return participants;
    }
}
