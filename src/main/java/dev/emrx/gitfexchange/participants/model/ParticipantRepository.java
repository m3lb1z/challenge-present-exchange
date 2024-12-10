package dev.emrx.gitfexchange.participants.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import dev.emrx.gitfexchange.users.model.User;

import java.util.Optional;

@Repository
public interface ParticipantRepository extends CrudRepository<Participant, Long> {
    Optional<Participant> findByEmail(String email);
    Optional<Participant> findByUser(User user);

    boolean existsByEmail(String email);
    boolean existsByUser(User user);
}
