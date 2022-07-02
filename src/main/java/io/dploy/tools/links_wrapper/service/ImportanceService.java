package io.dploy.tools.links_wrapper.service;

import io.dploy.tools.links_wrapper.domain.Importance;
import io.dploy.tools.links_wrapper.model.ImportanceDTO;
import io.dploy.tools.links_wrapper.repos.ImportanceRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class ImportanceService {

    private final ImportanceRepository importanceRepository;

    public ImportanceService(final ImportanceRepository importanceRepository) {
        this.importanceRepository = importanceRepository;
    }

    public List<ImportanceDTO> findAll() {
        return importanceRepository.findAll(Sort.by("id"))
                .stream()
                .map(importance -> mapToDTO(importance, new ImportanceDTO()))
                .collect(Collectors.toList());
    }

    public ImportanceDTO get(final Long id) {
        return importanceRepository.findById(id)
                .map(importance -> mapToDTO(importance, new ImportanceDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long create(final ImportanceDTO importanceDTO) {
        final Importance importance = new Importance();
        mapToEntity(importanceDTO, importance);
        return importanceRepository.save(importance).getId();
    }

    public void update(final Long id, final ImportanceDTO importanceDTO) {
        final Importance importance = importanceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(importanceDTO, importance);
        importanceRepository.save(importance);
    }

    public void delete(final Long id) {
        importanceRepository.deleteById(id);
    }

    private ImportanceDTO mapToDTO(final Importance importance, final ImportanceDTO importanceDTO) {
        importanceDTO.setId(importance.getId());
        importanceDTO.setName(importance.getName());
        return importanceDTO;
    }

    private Importance mapToEntity(final ImportanceDTO importanceDTO, final Importance importance) {
        importance.setName(importanceDTO.getName());
        return importance;
    }

}
