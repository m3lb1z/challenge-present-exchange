package dev.emrx.gitfexchange.participants.mapper;

import org.springframework.stereotype.Component;

import dev.emrx.gitfexchange.participants.dto.GiftAssignmentResponse;
import dev.emrx.gitfexchange.participants.model.Participant;

@Component
public class AssignmentMapper {
  
  public GiftAssignmentResponse toGiftAssignmentResponse(Participant giver, Participant recipient) {
    return new GiftAssignmentResponse(giver.getName(), giver.getEmail(), recipient.getName());
  }

}
