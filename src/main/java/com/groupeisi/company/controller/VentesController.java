package com.groupeisi.company.controller;

import com.groupeisi.company.dto.VentesDto;
import com.groupeisi.company.service.VentesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventes")
@RequiredArgsConstructor
public class VentesController {

    private final VentesService ventesService;

    @GetMapping
    public ResponseEntity<List<VentesDto>> getAll() {
        return ResponseEntity.ok(ventesService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentesDto> getById(@PathVariable Long id) {
        return ventesService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<VentesDto> create(@RequestBody VentesDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ventesService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VentesDto> update(@PathVariable Long id,
                                            @RequestBody VentesDto dto) {
        return ResponseEntity.ok(ventesService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ventesService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}