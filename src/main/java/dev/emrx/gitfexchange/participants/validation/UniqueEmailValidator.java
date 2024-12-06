package dev.emrx.gitfexchange.participants.validation;

import org.springframework.stereotype.Component;

import dev.emrx.gitfexchange.participants.model.Participant;
import dev.emrx.gitfexchange.participants.model.ParticipantRepository;
import jakarta.validation.ValidationException;
import dev.emrx.gitfexchange.participants.dto.CreateParticipantRequest;

import java.util.Optional;

@Component
public class UniqueEmailValidator {

    private final ParticipantRepository participantRepository;

    public UniqueEmailValidator(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public void validate(CreateParticipantRequest request) {
        Optional<Participant> existingParticipant = participantRepository.findByEmail(request.email());
        if (existingParticipant.isPresent()) {
            throw new ValidationException("El participante con el correo " + request.email() + " ya existe.");
        }
    }
}
