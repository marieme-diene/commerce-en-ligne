package com.groupeisi.company.controller;

import com.groupeisi.company.dto.AchatsDto;
import com.groupeisi.company.dto.ProduitsDto;
import com.groupeisi.company.service.AchatsService;
import com.groupeisi.company.service.ProduitsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/achats")
@RequiredArgsConstructor
public class AchatsWebController {

    private final AchatsService achatsService;
    private final ProduitsService produitsService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("achats", achatsService.findAll());
        return "achats/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("achat", new AchatsDto());
        model.addAttribute("produits", produitsService.findAll());
        return "achats/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam Long productId,
                       @RequestParam Double quantity,
                       @RequestParam String dateP) {
        try {
            AchatsDto achat = new AchatsDto();
            achat.setQuantity(quantity);

            // Convertir la date
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            achat.setDateP(sdf.parse(dateP));

            // Créer le produit avec juste l'ID
            ProduitsDto produit = new ProduitsDto();
            produit.setId(productId);
            achat.setProduct(produit);

            achatsService.save(achat);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/achats";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        achatsService.deleteById(id);
        return "redirect:/achats";
    }
}