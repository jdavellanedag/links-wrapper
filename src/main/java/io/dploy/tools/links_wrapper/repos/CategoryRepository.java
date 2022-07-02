package io.dploy.tools.links_wrapper.repos;

import io.dploy.tools.links_wrapper.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Long> {
}
