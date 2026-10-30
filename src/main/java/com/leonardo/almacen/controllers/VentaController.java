package com.leonardo.almacen.controllers;

import com.leonardo.almacen.dto.ventas.VentaRequest;
import com.leonardo.almacen.dto.ventas.VentaResponse;
import com.leonardo.almacen.services.ventas.ventaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
@Tag(name = "API Venta-sucursales",description = "Géstion de Venta de sucursales")
public class VentaController {
    private final ventaService ventaService;

    // REGISTRAR UNA VENTA
    @PostMapping
    public ResponseEntity<VentaResponse> registrar(@Valid @RequestBody VentaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaService.registrar(request));
    }

    // LISTAR VENTAS REGISTRADAS
    @GetMapping
    public ResponseEntity<List<VentaResponse>> listar() {
        return ResponseEntity.ok(ventaService.listar());
    }

    // LISTAR VENTAS CANCELADAS
    @GetMapping("/canceladas")
    public ResponseEntity<List<VentaResponse>> listarCanceladas() {
        return ResponseEntity.ok(ventaService.listarCanceladas());
    }

    // CONSULTAR VENTA ACTIVA
    @GetMapping("/{id}")
    public ResponseEntity<VentaResponse> obtener(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(ventaService.obtenerPorIdActiva(id));
    }

    // CANCELAR VENTA Y DEVOLVER STOCK
    @DeleteMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable @Positive Long id) {
        ventaService.cancel(id);
        return ResponseEntity.noContent().build();
    }
}
