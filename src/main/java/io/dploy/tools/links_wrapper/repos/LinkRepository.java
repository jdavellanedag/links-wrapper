package io.dploy.tools.links_wrapper.repos;

import io.dploy.tools.links_wrapper.domain.Link;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LinkRepository extends JpaRepository<Link, Long> {
}
