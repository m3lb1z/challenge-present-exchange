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

import dev.emrx.gitfexchange.participants.dto.RegisterParticipantRequest;
import dev.emrx.gitfexchange.participants.dto.GiftRecipientResponse;
import dev.emrx.gitfexchange.participants.dto.ParticipantResponse;
import dev.emrx.gitfexchange.participants.model.Participant;
import dev.emrx.gitfexchange.participants.service.ParticipantService;
import dev.emrx.gitfexchange.participants.mapper.ParticipantMapper;
import dev.emrx.gitfexchange.participants.validation.UniqueEmailValidator;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/participants")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Participantes")
public class ParticipantController {
  
    private ParticipantService participantService;
    private UniqueEmailValidator uniqueEmailValidator;
    private ParticipantMapper participantMapper;

    public ParticipantController(ParticipantService participantService, UniqueEmailValidator uniqueEmailValidator, ParticipantMapper participantMapper) {
        this.participantService = participantService;
        this.uniqueEmailValidator = uniqueEmailValidator;
        this.participantMapper = participantMapper;
    }

    @Operation(summary = "Registrar nuevo participante", 
              description = "Permite a un usuario registrarse como participante en el intercambio de regalos")
    @PostMapping("/register")
    public ResponseEntity<ParticipantResponse> registerParticipant(@RequestBody @Valid RegisterParticipantRequest request, UriComponentsBuilder uriBuilder) {
        uniqueEmailValidator.validate(request);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (participantService.existsParticipantByUsername(username)) {
            throw new ValidationException(username + " ya se registro como participante");
        }

        Participant participant = participantMapper.toParticipant(request);
        Participant savedParticipant = participantService.createParticipant(username, participant);
        ParticipantResponse response = participantMapper.toParticipantResponse(savedParticipant);

        return ResponseEntity.created(uriBuilder.path("/participants/{id}").buildAndExpand(savedParticipant.getId()).toUri())
                             .body(response);
    }


    @Operation(summary = "Obtener participante asignado", 
              description = "Obtiene la información del participante al que se le debe entregar un regalo")
    @GetMapping("/assigned")
    public ResponseEntity<GiftRecipientResponse> assignedParticipant() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Participant> participantOptional = participantService.getParticipantByUsername(username);

        if (participantOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Participant participant = participantOptional.get();
        
        if (participant.getGiftRecipient() == null) {
            throw new ValidationException("Todavia no se ha asignado un destinatario de regalo");
        }

        return ResponseEntity.ok(new GiftRecipientResponse(participant.getName(), participant.getEmail(), participant.getGiftRecipient().getName()));
    }
}
