package com.groupeisi.company.controller;

import com.groupeisi.company.dto.ProduitsDto;
import com.groupeisi.company.dto.VentesDto;
import com.groupeisi.company.service.ProduitsService;
import com.groupeisi.company.service.VentesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ventes")
@RequiredArgsConstructor
public class VentesWebController {

    private final VentesService ventesService;
    private final ProduitsService produitsService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("ventes", ventesService.findAll());
        return "ventes/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("vente", new VentesDto());
        model.addAttribute("produits", produitsService.findAll());
        return "ventes/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam Long productId,
                       @RequestParam Double quantity,
                       @RequestParam String dateP) {
        try {
            VentesDto vente = new VentesDto();
            vente.setQuantity(quantity);

            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            vente.setDateP(sdf.parse(dateP));

            ProduitsDto produit = new ProduitsDto();
            produit.setId(productId);
            vente.setProduct(produit);

            ventesService.save(vente);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/ventes";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        ventesService.deleteById(id);
        return "redirect:/ventes";
    }
}