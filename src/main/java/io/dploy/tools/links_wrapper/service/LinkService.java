package io.dploy.tools.links_wrapper.service;

import io.dploy.tools.links_wrapper.domain.Importance;
import io.dploy.tools.links_wrapper.domain.Link;
import io.dploy.tools.links_wrapper.model.LinkDTO;
import io.dploy.tools.links_wrapper.repos.ImportanceRepository;
import io.dploy.tools.links_wrapper.repos.LinkRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class LinkService {

    private final LinkRepository linkRepository;
    private final ImportanceRepository importanceRepository;

    public LinkService(final LinkRepository linkRepository,
            final ImportanceRepository importanceRepository) {
        this.linkRepository = linkRepository;
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
        linkDTO.setStatus(link.getStatus());
        linkDTO.setComment(link.getComment());
        linkDTO.setImportance(link.getImportance() == null ? null : link.getImportance().getId());
        return linkDTO;
    }

    private Link mapToEntity(final LinkDTO linkDTO, final Link link) {
        link.setUrl(linkDTO.getUrl());
        link.setStatus(linkDTO.getStatus());
        link.setComment(linkDTO.getComment());
        final Importance importance = linkDTO.getImportance() == null ? null : importanceRepository.findById(linkDTO.getImportance())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "importance not found"));
        link.setImportance(importance);
        return link;
    }

}
