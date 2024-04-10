package it.lovacino.betburger.bot.repository;

import it.lovacino.betburger.bot.domain.Bookmaker;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Bookmaker entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookmakerRepository extends JpaRepository<Bookmaker, Long> {}
