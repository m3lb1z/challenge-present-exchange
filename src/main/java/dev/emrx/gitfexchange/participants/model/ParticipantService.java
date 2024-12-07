package dev.emrx.gitfexchange.participants.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    @Transactional
    public Participant addParticipant(Participant participant) {
        return participantRepository.save(participant);
    }

    @Transactional(readOnly = true)
    public Iterable<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }
}
