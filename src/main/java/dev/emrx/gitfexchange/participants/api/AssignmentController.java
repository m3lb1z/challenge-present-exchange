package dev.emrx.gitfexchange.participants.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.emrx.gitfexchange.participants.dto.GiftRecipientResponse;
import dev.emrx.gitfexchange.participants.mapper.AssignmentMapper;
import dev.emrx.gitfexchange.participants.model.Participant;
import dev.emrx.gitfexchange.participants.service.AssignmentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/assignments")
@SecurityRequirement(name = "bearer-key")
public class AssignmentController {

    private AssignmentService assignmentService;
    private AssignmentMapper assignmentMapper;

    public AssignmentController(AssignmentService assignmentService, AssignmentMapper assignmentMapper) {
        this.assignmentService = assignmentService;
        this.assignmentMapper = assignmentMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void assignGiftRecipients() {
        assignmentService.assignRandomGiftRecipients();
    }

    @GetMapping("/list")
    public List<GiftRecipientResponse> listGiftRecipients() {
      List<Participant> participants = assignmentService.listGiftRecipients();
      participants = participants.stream().filter(participant -> participant.getGiftRecipient() != null).collect(Collectors.toList());

      return participants.stream()
                         .map(participant -> assignmentMapper.toGiftAssignmentResponse(participant, participant.getGiftRecipient()))
                         .collect(Collectors.toList());
    }
    
}
