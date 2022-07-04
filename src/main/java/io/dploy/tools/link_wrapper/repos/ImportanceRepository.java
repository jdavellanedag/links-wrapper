package io.dploy.tools.link_wrapper.repos;

import io.dploy.tools.link_wrapper.domain.Importance;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ImportanceRepository extends JpaRepository<Importance, Long> {
}
