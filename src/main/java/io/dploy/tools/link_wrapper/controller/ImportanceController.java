package io.dploy.tools.link_wrapper.controller;

import io.dploy.tools.link_wrapper.model.ImportanceDTO;
import io.dploy.tools.link_wrapper.service.ImportanceService;
import io.dploy.tools.link_wrapper.util.WebUtils;
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
@RequestMapping("/importances")
public class ImportanceController {

    private final ImportanceService importanceService;

    public ImportanceController(final ImportanceService importanceService) {
        this.importanceService = importanceService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("importances", importanceService.findAll());
        return "importance/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("importance") final ImportanceDTO importanceDTO) {
        return "importance/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("importance") @Valid final ImportanceDTO importanceDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "importance/add";
        }
        importanceService.create(importanceDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("importance.create.success"));
        return "redirect:/importances";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("importance", importanceService.get(id));
        return "importance/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("importance") @Valid final ImportanceDTO importanceDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "importance/edit";
        }
        importanceService.update(id, importanceDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("importance.update.success"));
        return "redirect:/importances";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = importanceService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            importanceService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("importance.delete.success"));
        }
        return "redirect:/importances";
    }

}
