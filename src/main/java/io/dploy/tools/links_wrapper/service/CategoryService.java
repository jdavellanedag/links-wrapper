package io.dploy.tools.links_wrapper.service;

import io.dploy.tools.links_wrapper.domain.Category;
import io.dploy.tools.links_wrapper.domain.Link;
import io.dploy.tools.links_wrapper.model.CategoryDTO;
import io.dploy.tools.links_wrapper.repos.CategoryRepository;
import io.dploy.tools.links_wrapper.repos.LinkRepository;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Transactional
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final LinkRepository linkRepository;

    public CategoryService(final CategoryRepository categoryRepository,
            final LinkRepository linkRepository) {
        this.categoryRepository = categoryRepository;
        this.linkRepository = linkRepository;
    }

    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll(Sort.by("id"))
                .stream()
                .map(category -> mapToDTO(category, new CategoryDTO()))
                .collect(Collectors.toList());
    }

    public CategoryDTO get(final Long id) {
        return categoryRepository.findById(id)
                .map(category -> mapToDTO(category, new CategoryDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long create(final CategoryDTO categoryDTO) {
        final Category category = new Category();
        mapToEntity(categoryDTO, category);
        return categoryRepository.save(category).getId();
    }

    public void update(final Long id, final CategoryDTO categoryDTO) {
        final Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(categoryDTO, category);
        categoryRepository.save(category);
    }

    public void delete(final Long id) {
        categoryRepository.deleteById(id);
    }

    private CategoryDTO mapToDTO(final Category category, final CategoryDTO categoryDTO) {
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setLinkCategorys(category.getLinkCategoryLinks() == null ? null : category.getLinkCategoryLinks().stream()
                .map(link -> link.getId())
                .collect(Collectors.toList()));
        return categoryDTO;
    }

    private Category mapToEntity(final CategoryDTO categoryDTO, final Category category) {
        category.setName(categoryDTO.getName());
        final List<Link> linkCategorys = linkRepository.findAllById(
                categoryDTO.getLinkCategorys() == null ? Collections.emptyList() : categoryDTO.getLinkCategorys());
        if (linkCategorys.size() != (categoryDTO.getLinkCategorys() == null ? 0 : categoryDTO.getLinkCategorys().size())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "one of linkCategorys not found");
        }
        category.setLinkCategoryLinks(linkCategorys.stream().collect(Collectors.toSet()));
        return category;
    }

}
