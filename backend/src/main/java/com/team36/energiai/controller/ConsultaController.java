package com.team36.energiai.controller;

import com.team36.energiai.dto.AnalisisResponse;
import com.team36.energiai.service.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analisis")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @GetMapping("/{id}")
    public ResponseEntity<AnalisisResponse> obtenerPorId(@PathVariable Long id){
        return ResponseEntity.ok(consultaService.obtenerPorId(id));
    }

    @GetMapping("")
    public ResponseEntity<Page<AnalisisResponse>> obtenerTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            ){
        Pageable pageable = PageRequest.of(page, size, Sort.by("creadoEn").descending());
        Page<AnalisisResponse> resultado = consultaService.obtenerTodos(pageable);
        return ResponseEntity.ok(resultado);
        }
}
