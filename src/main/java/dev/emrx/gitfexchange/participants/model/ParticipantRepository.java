package dev.emrx.gitfexchange.participants.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipantRepository extends CrudRepository<Participant, Long> {
    Optional<Participant> findByEmail(String email);
}
