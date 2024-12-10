package dev.emrx.gitfexchange.participants.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.emrx.gitfexchange.participants.model.Participant;
import dev.emrx.gitfexchange.participants.model.ParticipantRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssignmentService {

    private ParticipantRepository participantRepository;

    public AssignmentService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @Transactional
    public List<Participant> assignRandomGiftRecipients() {
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

        var savedParticipants = participantRepository.saveAll(participants);
        List<Participant> participantList = new ArrayList<>();
        savedParticipants.forEach(participantList::add);
        return participantList;
    }

    @Transactional(readOnly = true)
    public List<Participant> listGiftRecipients() {
        List<Participant> participants = new ArrayList<>();
        participantRepository.findAll().forEach(participants::add);
 
        return participants;
    }
}