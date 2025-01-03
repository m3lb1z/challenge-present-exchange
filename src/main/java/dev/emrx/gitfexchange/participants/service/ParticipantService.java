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
    private final EmailService emailService;

    public ParticipantService(ParticipantRepository participantRepository, UserRepository userRepository, EmailService emailService) {
        this.participantRepository = participantRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
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
        emailService.sendEmailFromRegistry(participant);
        
        return participantRepository.save(participant);
    }

    @Transactional(readOnly = true)
    public List<Participant> listParticipants() {
        List<Participant> participants = new ArrayList<>();
        participantRepository.findAll().forEach(participants::add);
        return participants;
    }

    @Transactional(readOnly = true)
    public boolean existsParticipantByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return false;
        }
        return participantRepository.existsByUser(user);
    }
}
