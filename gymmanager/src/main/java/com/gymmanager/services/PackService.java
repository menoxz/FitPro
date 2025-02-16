package com.gymmanager.services;


import com.gymmanager.dto.PackDto;
import com.gymmanager.model.Pack;
import com.gymmanager.repository.PackRepository;
import com.gymmanager.security.exception.GlobalExceptionHandler.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PackService {

    private final PackRepository packRepository;

    public Pack createPack(PackDto dto) {
        Pack pack = new Pack();
        pack.setName(dto.name());
        pack.setDurationInMonths(dto.durationInMonths());
        pack.setMonthlyPrice(dto.monthlyPrice());
        return packRepository.save(pack);
    }

    public List<Pack> getAllPacks() {
        return packRepository.findAll();
    }

    public Pack updatePack(Long id, PackDto dto) {
        Pack pack = packRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pack non trouvé"));
        
        pack.setName(dto.name());
        pack.setDurationInMonths(dto.durationInMonths());
        pack.setMonthlyPrice(dto.monthlyPrice());
        
        return packRepository.save(pack);
    }

    public void deletePack(Long id) {
        packRepository.deleteById(id);
    }

    public Pack getById(Long id) {
        return packRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Offre non trouvé"));
    }

}
