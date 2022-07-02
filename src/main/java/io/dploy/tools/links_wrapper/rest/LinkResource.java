package io.dploy.tools.links_wrapper.rest;

import io.dploy.tools.links_wrapper.model.LinkDTO;
import io.dploy.tools.links_wrapper.service.LinkService;
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
@RequestMapping(value = "/api/links", produces = MediaType.APPLICATION_JSON_VALUE)
public class LinkResource {

    private final LinkService linkService;

    public LinkResource(final LinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping
    public ResponseEntity<List<LinkDTO>> getAllLinks() {
        return ResponseEntity.ok(linkService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LinkDTO> getLink(@PathVariable final Long id) {
        return ResponseEntity.ok(linkService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createLink(@RequestBody @Valid final LinkDTO linkDTO) {
        return new ResponseEntity<>(linkService.create(linkDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLink(@PathVariable final Long id,
            @RequestBody @Valid final LinkDTO linkDTO) {
        linkService.update(id, linkDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteLink(@PathVariable final Long id) {
        linkService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
