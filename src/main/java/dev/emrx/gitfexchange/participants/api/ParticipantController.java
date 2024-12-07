package dev.emrx.gitfexchange.participants.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import dev.emrx.gitfexchange.participants.dto.CreateParticipantRequest;
import dev.emrx.gitfexchange.participants.dto.ParticipantResponse;
import dev.emrx.gitfexchange.participants.model.Participant;
import dev.emrx.gitfexchange.participants.model.ParticipantService;
import dev.emrx.gitfexchange.participants.mapper.ParticipantMapper;
import dev.emrx.gitfexchange.participants.validation.UniqueEmailValidator;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/participants")
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
        // Validate email uniqueness
        uniqueEmailValidator.validate(request);

        Participant participant = participantMapper.toParticipant(request);
        Participant savedParticipant = participantService.addParticipant(participant);
        ParticipantResponse response = participantMapper.toParticipantResponse(savedParticipant);

        // Build the URI for the newly created resource
        return ResponseEntity.created(uriBuilder.path("/participants/{id}").buildAndExpand(savedParticipant.getId()).toUri())
                             .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ParticipantResponse>> listParticipants() {
        List<Participant> participants = participantService.getAllParticipants();
        List<ParticipantResponse> responses = participants.stream()
                                                          .map(participantMapper::toParticipantResponse)
                                                          .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}
