package it.lovacino.betburger.bot.repository;

import it.lovacino.betburger.bot.domain.Betting;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Betting entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BettingRepository extends JpaRepository<Betting, Long> {}
