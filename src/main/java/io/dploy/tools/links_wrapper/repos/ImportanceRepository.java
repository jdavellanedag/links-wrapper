package io.dploy.tools.links_wrapper.repos;

import io.dploy.tools.links_wrapper.domain.Importance;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ImportanceRepository extends JpaRepository<Importance, Long> {
}
