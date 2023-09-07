package com.example.booking.controller.rest;

import com.example.booking.repository.TypeRepository;
import com.example.booking.service.response.SelectOptionResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/types")
@AllArgsConstructor
public class TypeRestController {
    private final TypeRepository typeRepository;

    @GetMapping
    public List<SelectOptionResponse> getSelectOption(){
        return typeRepository.findAll().stream().map(type -> new SelectOptionResponse(type.getId().toString(), type.getName())).collect(Collectors.toList());
    }
}
