package com.groupeisi.company.controller;

import com.groupeisi.company.dto.ProduitsDto;
import com.groupeisi.company.service.ProduitsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
public class ProduitsController {

    private final ProduitsService produitsService;

    @GetMapping
    public ResponseEntity<List<ProduitsDto>> getAll() {
        return ResponseEntity.ok(produitsService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProduitsDto> getById(@PathVariable Long id) {
        return produitsService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProduitsDto> create(@RequestBody ProduitsDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(produitsService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProduitsDto> update(@PathVariable Long id,
                                              @RequestBody ProduitsDto dto) {
        return ResponseEntity.ok(produitsService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        produitsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}