package it.lovacino.betburger.bot.repository;

import it.lovacino.betburger.bot.domain.AccountBet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AccountBet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountBetRepository extends JpaRepository<AccountBet, Long> {}
