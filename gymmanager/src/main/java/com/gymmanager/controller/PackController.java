package com.gymmanager.controller;


import com.gymmanager.dto.PackDto;
import com.gymmanager.model.Pack;
import com.gymmanager.services.PackService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packs")
@RequiredArgsConstructor
public class PackController {

    private final PackService packService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pack createPack(@RequestBody PackDto dto) {
        return packService.createPack(dto);
    }

    @GetMapping
    public List<Pack> getAllPacks() {
        return packService.getAllPacks();
    }

    @GetMapping("/{id}")
    public PackDto.Response getPackById(@PathVariable Long id) {
        Pack pack = packService.getById(id);
        return PackDto.Response.fromEntity(pack);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Pack updatePack(@PathVariable Long id, @RequestBody PackDto dto) {
        return packService.updatePack(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePack(@PathVariable Long id) {
        packService.deletePack(id);
    }
}
