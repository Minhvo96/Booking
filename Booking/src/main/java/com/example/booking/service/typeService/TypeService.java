package com.example.booking.service.typeService;

import com.example.booking.repository.TypeRepository;
import com.example.booking.service.response.SelectOptionResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
@AllArgsConstructor
public class TypeService {
    private TypeRepository typeRepository;
    public List<SelectOptionResponse> findAll(){
        return typeRepository.findAll().stream()
                .map(type -> new SelectOptionResponse(type.getId().toString(), type.getName()))
                .collect(Collectors.toList());
    }

}
