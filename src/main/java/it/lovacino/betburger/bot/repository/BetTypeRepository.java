package it.lovacino.betburger.bot.repository;

import it.lovacino.betburger.bot.domain.BetType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BetType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BetTypeRepository extends JpaRepository<BetType, Long> {}
