package it.lovacino.betburger.bot.repository;

import it.lovacino.betburger.bot.domain.EventSource;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventSource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventSourceRepository extends JpaRepository<EventSource, Long> {}
