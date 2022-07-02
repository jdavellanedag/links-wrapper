package io.dploy.tools.links_wrapper.model;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LinkDTO {

    private Long id;

    @NotNull
    private String url;

    private Boolean status;

    private String comment;

    @NotNull
    private Long importance;

}
