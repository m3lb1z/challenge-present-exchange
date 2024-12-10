package dev.emrx.gitfexchange.participants.validation;

import org.springframework.stereotype.Component;

import dev.emrx.gitfexchange.participants.model.ParticipantRepository;
import jakarta.validation.ValidationException;
import dev.emrx.gitfexchange.participants.dto.RegisterParticipantRequest;


@Component
public class UniqueEmailValidator {

    private final ParticipantRepository participantRepository;

    public UniqueEmailValidator(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public void validate(RegisterParticipantRequest request) {
        boolean existingParticipant = participantRepository.existsByEmail(request.email());
        if (existingParticipant) {
            throw new ValidationException("El participante con el correo " + request.email() + " ya existe.");
        }
    }
}
