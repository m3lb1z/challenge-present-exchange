package dev.emrx.gitfexchange.participants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/participants")
public class ParticipantController {
  
    @Autowired
    private ParticipantService participantService;

    @PostMapping
    public ResponseEntity<ParticipantResponse> createParticipant(@RequestBody @Valid CreateParticipantRequest request, UriComponentsBuilder uriBuilder) {
        // Create a new Participant object using the builder pattern
        Participant participant = Participant.builder()
            .name(request.name())
            .email(request.email())
            .build();

        // Save the participant to the database
        Participant savedParticipant = participantService.addParticipant(participant);

        // Create a ParticipantResponse from the saved participant
        ParticipantResponse response = new ParticipantResponse(savedParticipant.getId(), savedParticipant.getName(), savedParticipant.getEmail());

        // Build the URI for the newly created resource
        return ResponseEntity.created(uriBuilder.path("/participants/{id}").buildAndExpand(savedParticipant.getId()).toUri())
                             .body(response);
    }
}
