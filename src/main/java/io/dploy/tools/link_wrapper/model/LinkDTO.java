package io.dploy.tools.link_wrapper.model;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LinkDTO {

    private Long id;

    @NotNull
    private String url;

    private String comment;

    @NotNull
    private Boolean status;

    @NotNull
    private Long linkImportance;

}
