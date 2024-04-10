package it.lovacino.betburger.bot.repository;

import it.lovacino.betburger.bot.domain.ConfigParam;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ConfigParam entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigParamRepository extends JpaRepository<ConfigParam, Long> {}
