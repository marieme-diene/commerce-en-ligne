package com.groupeisi.company.controller;

import com.groupeisi.company.dto.ProduitsDto;
import com.groupeisi.company.service.ProduitsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/produits")
@RequiredArgsConstructor
public class ProduitsWebController {

    private final ProduitsService produitsService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("produits", produitsService.findAll());
        return "produits/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("produit", new ProduitsDto());
        return "produits/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        produitsService.findById(id).ifPresent(p -> model.addAttribute("produit", p));
        return "produits/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute ProduitsDto produit) {
        if (produit.getId() != null) {
            produitsService.update(produit.getId(), produit);
        } else {
            produitsService.save(produit);
        }
        return "redirect:/produits";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        produitsService.deleteById(id);
        return "redirect:/produits";
    }
}