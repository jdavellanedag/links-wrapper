package io.dploy.tools.link_wrapper.controller;

import io.dploy.tools.link_wrapper.domain.Link;
import io.dploy.tools.link_wrapper.model.CategoryDTO;
import io.dploy.tools.link_wrapper.repos.LinkRepository;
import io.dploy.tools.link_wrapper.service.CategoryService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/categorys")
public class CategoryController {

    private final CategoryService categoryService;
    private final LinkRepository linkRepository;

    public CategoryController(final CategoryService categoryService,
            final LinkRepository linkRepository) {
        this.categoryService = categoryService;
        this.linkRepository = linkRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("linkCategoryValues", linkRepository.findAll().stream().collect(
                Collectors.toMap(Link::getId, Link::getId)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("categorys", categoryService.findAll());
        return "category/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("category") final CategoryDTO categoryDTO) {
        return "category/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("category") @Valid final CategoryDTO categoryDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "category/add";
        }
        categoryService.create(categoryDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("category.create.success"));
        return "redirect:/categorys";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("category", categoryService.get(id));
        return "category/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("category") @Valid final CategoryDTO categoryDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "category/edit";
        }
        categoryService.update(id, categoryDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("category.update.success"));
        return "redirect:/categorys";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        categoryService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("category.delete.success"));
        return "redirect:/categorys";
    }

}
