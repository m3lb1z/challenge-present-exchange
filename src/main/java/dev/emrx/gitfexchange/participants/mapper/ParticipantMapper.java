package dev.emrx.gitfexchange.participants.mapper;

import dev.emrx.gitfexchange.participants.dto.CreateParticipantRequest;
import dev.emrx.gitfexchange.participants.dto.ParticipantResponse;
import dev.emrx.gitfexchange.participants.model.Participant;
import org.springframework.stereotype.Component;

@Component
public class ParticipantMapper {

    public Participant toParticipant(CreateParticipantRequest request) {
        return Participant.builder()
                .name(request.name())
                .email(request.email())
                .build();
    }

    public ParticipantResponse toParticipantResponse(Participant participant) {
        return new ParticipantResponse(participant.getId(), participant.getName(), participant.getEmail());
    }
}
