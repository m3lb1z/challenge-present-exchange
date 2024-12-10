package dev.emrx.gitfexchange.participants.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.emrx.gitfexchange.participants.model.Participant;
import dev.emrx.gitfexchange.participants.model.ParticipantRepository;
import dev.emrx.gitfexchange.users.model.User;
import dev.emrx.gitfexchange.users.model.UserRepository;

@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;

    public ParticipantService(ParticipantRepository participantRepository, UserRepository userRepository) {
        this.participantRepository = participantRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Participant> getParticipantByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return Optional.empty();
        }

        return participantRepository.findByUser(user.get());
    }

    @Transactional
    public Participant createParticipant(String username, Participant participant) {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            return null;
        }

        participant.setUser(user.get());
        return participantRepository.save(participant);
    }

    @Transactional(readOnly = true)
    public List<Participant> listParticipants() {
        List<Participant> participants = new ArrayList<>();
        participantRepository.findAll().forEach(participants::add);
        return participants;
    }
}
