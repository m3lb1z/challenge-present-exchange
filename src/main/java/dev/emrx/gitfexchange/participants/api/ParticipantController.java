package dev.emrx.gitfexchange.participants.api;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import dev.emrx.gitfexchange.participants.dto.CreateParticipantRequest;
import dev.emrx.gitfexchange.participants.dto.GiftRecipientResponse;
import dev.emrx.gitfexchange.participants.dto.ParticipantResponse;
import dev.emrx.gitfexchange.participants.model.Participant;
import dev.emrx.gitfexchange.participants.service.ParticipantService;
import dev.emrx.gitfexchange.participants.mapper.ParticipantMapper;
import dev.emrx.gitfexchange.participants.validation.UniqueEmailValidator;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/participants")
@SecurityRequirement(name = "bearer-key")
public class ParticipantController {
  
    private ParticipantService participantService;
    private UniqueEmailValidator uniqueEmailValidator;
    private ParticipantMapper participantMapper;

    public ParticipantController(ParticipantService participantService, UniqueEmailValidator uniqueEmailValidator, ParticipantMapper participantMapper) {
        this.participantService = participantService;
        this.uniqueEmailValidator = uniqueEmailValidator;
        this.participantMapper = participantMapper;
    }

    @PostMapping
    public ResponseEntity<ParticipantResponse> createParticipant(@RequestBody @Valid CreateParticipantRequest request, UriComponentsBuilder uriBuilder) {
        uniqueEmailValidator.validate(request);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Participant participant = participantMapper.toParticipant(request);
        Participant savedParticipant = participantService.createParticipant(username, participant);
        ParticipantResponse response = participantMapper.toParticipantResponse(savedParticipant);

        return ResponseEntity.created(uriBuilder.path("/participants/{id}").buildAndExpand(savedParticipant.getId()).toUri())
                             .body(response);
    }


    @GetMapping("/assigned")
    public ResponseEntity<GiftRecipientResponse> infoParticipant() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Participant> participantOptional = participantService.getParticipantByUsername(username);

        if (participantOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Participant participant = participantOptional.get();

        return ResponseEntity.ok(new GiftRecipientResponse(participant.getName(), participant.getEmail(), participant.getGiftRecipient().getName()));
    }
}
