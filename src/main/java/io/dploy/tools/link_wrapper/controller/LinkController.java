package io.dploy.tools.link_wrapper.controller;

import io.dploy.tools.link_wrapper.domain.Category;
import io.dploy.tools.link_wrapper.domain.Importance;
import io.dploy.tools.link_wrapper.model.LinkDTO;
import io.dploy.tools.link_wrapper.repos.CategoryRepository;
import io.dploy.tools.link_wrapper.repos.ImportanceRepository;
import io.dploy.tools.link_wrapper.service.LinkService;
import io.dploy.tools.link_wrapper.util.WebUtils;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class LinkController {

    private final LinkService linkService;
    private final ImportanceRepository importanceRepository;
    private final CategoryRepository categoryRepository;

    public LinkController(final LinkService linkService,
        final CategoryRepository categoryRepository,
        final ImportanceRepository importanceRepository) {
        this.linkService = linkService;
        this.categoryRepository = categoryRepository;
        this.importanceRepository = importanceRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("linkCategorysValues", categoryRepository.findAll().stream().collect(
                Collectors.toMap(Category::getId, Category::getName)));
        model.addAttribute("linkImportanceValues", importanceRepository.findAll().stream().collect(
                Collectors.toMap(Importance::getId, Importance::getName)));
    }

    @GetMapping("/")
    public String index(final Model model) {
        model.addAttribute("links", linkService.findAll());
        return "home/index";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("link") final LinkDTO linkDTO) {
        return "link/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("link") @Valid final LinkDTO linkDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "link/add";
        }
        linkService.create(linkDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("link.create.success"));
        return "redirect:/links";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("link", linkService.get(id));
        return "link/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("link") @Valid final LinkDTO linkDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "link/edit";
        }
        linkService.update(id, linkDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("link.update.success"));
        return "redirect:/links";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        linkService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("link.delete.success"));
        return "redirect:/links";
    }

}
