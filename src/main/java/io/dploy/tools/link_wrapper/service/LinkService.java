package io.dploy.tools.link_wrapper.service;

import io.dploy.tools.link_wrapper.domain.Category;
import io.dploy.tools.link_wrapper.domain.Importance;
import io.dploy.tools.link_wrapper.domain.Link;
import io.dploy.tools.link_wrapper.model.LinkDTO;
import io.dploy.tools.link_wrapper.repos.CategoryRepository;
import io.dploy.tools.link_wrapper.repos.ImportanceRepository;
import io.dploy.tools.link_wrapper.repos.LinkRepository;

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
public class LinkService {

    private final LinkRepository linkRepository;
    private final CategoryRepository categoryRepository;
    private final ImportanceRepository importanceRepository;

    public LinkService(final LinkRepository linkRepository,
            final CategoryRepository categoryRepository,
            final ImportanceRepository importanceRepository) {
        this.linkRepository = linkRepository;
        this.categoryRepository = categoryRepository;
        this.importanceRepository = importanceRepository;
    }

    public List<LinkDTO> findAll() {
        return linkRepository.findAll(Sort.by("id"))
                .stream()
                .map(link -> mapToDTO(link, new LinkDTO()))
                .collect(Collectors.toList());
    }

    public LinkDTO get(final Long id) {
        return linkRepository.findById(id)
                .map(link -> mapToDTO(link, new LinkDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long create(final LinkDTO linkDTO) {
        final Link link = new Link();
        mapToEntity(linkDTO, link);
        return linkRepository.save(link).getId();
    }

    public void update(final Long id, final LinkDTO linkDTO) {
        final Link link = linkRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(linkDTO, link);
        linkRepository.save(link);
    }

    public void delete(final Long id) {
        linkRepository.deleteById(id);
    }

    private LinkDTO mapToDTO(final Link link, final LinkDTO linkDTO) {
        linkDTO.setId(link.getId());
        linkDTO.setUrl(link.getUrl());
        linkDTO.setComment(link.getComment());
        linkDTO.setStatus(link.getStatus());
        linkDTO.setLinkImportance(link.getLinkImportance() == null ? null : link.getLinkImportance().getId());
        linkDTO.setLinkCategorys(link.getLinkCategoryCategorys() == null ? null : link.getLinkCategoryCategorys().stream()
            .map(category -> category.getId())
            .collect(Collectors.toList()));
        return linkDTO;
    }

    private Link mapToEntity(final LinkDTO linkDTO, final Link link) {
        link.setUrl(linkDTO.getUrl());
        link.setComment(linkDTO.getComment());
        link.setStatus(linkDTO.getStatus());
        final List<Category> linkCategorys = categoryRepository.findAllById(
                linkDTO.getLinkCategorys() == null ? Collections.emptyList() : linkDTO.getLinkCategorys());
        if (linkCategorys.size() != (linkDTO.getLinkCategorys() == null ? 0 : linkDTO.getLinkCategorys().size())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "one of linkCategorys not found");
        }
        final Importance linkImportance = linkDTO.getLinkImportance() == null ? null : importanceRepository.findById(linkDTO.getLinkImportance())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "linkImportance not found"));
        link.setLinkImportance(linkImportance);
        return link;
    }

}
