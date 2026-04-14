package com.groupeisi.company.controller;

import com.groupeisi.company.dto.AchatsDto;
import com.groupeisi.company.service.AchatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/achats")
@RequiredArgsConstructor
public class AchatsController {

    private final AchatsService achatsService;

    @GetMapping
    public ResponseEntity<List<AchatsDto>> getAll() {
        return ResponseEntity.ok(achatsService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AchatsDto> getById(@PathVariable Long id) {
        return achatsService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AchatsDto> create(@RequestBody AchatsDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(achatsService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AchatsDto> update(@PathVariable Long id,
                                            @RequestBody AchatsDto dto) {
        return ResponseEntity.ok(achatsService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        achatsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}