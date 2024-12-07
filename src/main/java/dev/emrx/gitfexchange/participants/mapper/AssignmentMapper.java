package dev.emrx.gitfexchange.participants.mapper;

import org.springframework.stereotype.Component;

import dev.emrx.gitfexchange.participants.dto.GiftRecipientResponse;
import dev.emrx.gitfexchange.participants.model.Participant;

@Component
public class AssignmentMapper {
  
  public GiftRecipientResponse toGiftRecipientResponse(Participant giver, Participant recipient) {
    return new GiftRecipientResponse(giver.getName(), giver.getEmail(), recipient.getName());
  }

}
