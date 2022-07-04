package io.dploy.tools.link_wrapper.rest;

import io.dploy.tools.link_wrapper.model.ImportanceDTO;
import io.dploy.tools.link_wrapper.service.ImportanceService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/importances", produces = MediaType.APPLICATION_JSON_VALUE)
public class ImportanceResource {

    private final ImportanceService importanceService;

    public ImportanceResource(final ImportanceService importanceService) {
        this.importanceService = importanceService;
    }

    @GetMapping
    public ResponseEntity<List<ImportanceDTO>> getAllImportances() {
        return ResponseEntity.ok(importanceService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImportanceDTO> getImportance(@PathVariable final Long id) {
        return ResponseEntity.ok(importanceService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createImportance(
            @RequestBody @Valid final ImportanceDTO importanceDTO) {
        return new ResponseEntity<>(importanceService.create(importanceDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateImportance(@PathVariable final Long id,
            @RequestBody @Valid final ImportanceDTO importanceDTO) {
        importanceService.update(id, importanceDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteImportance(@PathVariable final Long id) {
        importanceService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
