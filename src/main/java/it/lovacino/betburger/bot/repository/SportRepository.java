package it.lovacino.betburger.bot.repository;

import it.lovacino.betburger.bot.domain.Sport;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Sport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SportRepository extends JpaRepository<Sport, Long> {}
