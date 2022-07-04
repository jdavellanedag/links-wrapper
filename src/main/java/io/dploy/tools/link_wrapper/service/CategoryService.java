package io.dploy.tools.link_wrapper.service;

import io.dploy.tools.link_wrapper.domain.Category;
import io.dploy.tools.link_wrapper.domain.Link;
import io.dploy.tools.link_wrapper.model.CategoryDTO;
import io.dploy.tools.link_wrapper.repos.CategoryRepository;
import io.dploy.tools.link_wrapper.repos.LinkRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


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
        categoryDTO.setLinkCategory(category.getLinkCategory() == null ? null : category.getLinkCategory().getId());
        return categoryDTO;
    }

    private Category mapToEntity(final CategoryDTO categoryDTO, final Category category) {
        category.setName(categoryDTO.getName());
        final Link linkCategory = categoryDTO.getLinkCategory() == null ? null : linkRepository.findById(categoryDTO.getLinkCategory())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "linkCategory not found"));
        category.setLinkCategory(linkCategory);
        return category;
    }

}
