package dev.emrx.gitfexchange.participants;

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
}
