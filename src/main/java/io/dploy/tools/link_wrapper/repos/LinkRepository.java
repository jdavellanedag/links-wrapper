package io.dploy.tools.link_wrapper.repos;

import io.dploy.tools.link_wrapper.domain.Link;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LinkRepository extends JpaRepository<Link, Long> {
}
