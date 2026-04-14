package com.groupeisi.company.controller;

import com.groupeisi.company.service.AchatsService;
import com.groupeisi.company.service.ProduitsService;
import com.groupeisi.company.service.VentesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProduitsService produitsService;
    private final AchatsService achatsService;
    private final VentesService ventesService;

    @GetMapping("/")
    public String home(Model model) {
        var produits = produitsService.findAll();
        var achats = achatsService.findAll();
        var ventes = ventesService.findAll();

        // Stats
        model.addAttribute("totalProduits", produits.size());
        model.addAttribute("totalAchats", achats.size());
        model.addAttribute("totalVentes", ventes.size());

        // Stock total
        double stockTotal = produits.stream()
                .mapToDouble(p -> p.getStock())
                .sum();
        model.addAttribute("stockTotal", (int) stockTotal);

        // Données
        model.addAttribute("produits", produits);
        model.addAttribute("derniersAchats", achats);
        model.addAttribute("dernieresVentes", ventes);

        return "dashboard";
    }
}