package com.leonardo.almacen.controllers;

import com.leonardo.almacen.dto.sucursales.SucursalRequest;
import com.leonardo.almacen.dto.sucursales.SucursalResponse;
import com.leonardo.almacen.entities.Sucursal;
import com.leonardo.almacen.services.sucursales.SucursalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.LifecycleState;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
@RequiredArgsConstructor
@Tag(name = "API Sucursales",description = "Géstion de Sucursales")
public class SucursalControler {
    private final SucursalService sucursalService;

    @GetMapping
    @Operation(summary = "Listar sucursales", description = "Obtiene todas las sucursales registradas.")
    @ApiResponse(responseCode = "200", description = "Sucursal encontrada")
    public ResponseEntity<List<SucursalResponse>> Listar(){
        return ResponseEntity.ok(sucursalService.listar());
    }
    @GetMapping("/{id}")
    @Operation(summary = "Obtener sucursal por id")
    @ApiResponse(responseCode = "200", description = "Sucursal encontrada")
    public ResponseEntity<SucursalResponse>obtenerPorId(
            @Parameter(description = "Id de la sucursal", example = "1")
            @PathVariable @Positive(message = "El id debe ser positivo")
            Long id
    ){
        return ResponseEntity.ok(sucursalService.obtenerPorId(id));
    }
    @PostMapping
    @Operation(summary = "Registrar una nueva sucursal")
    @ApiResponse(responseCode = "201",description = "Sucursal creada.")
    public ResponseEntity<SucursalResponse> registrar(
            @Valid @RequestBody SucursalRequest request
            ){
        return ResponseEntity.status(HttpStatus.CREATED).body(sucursalService.registrar(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una sucursal existente")
    @ApiResponse(responseCode = "200",description = "Sucursal Actualizada.")
    public ResponseEntity<SucursalResponse> actualizar(
            @Valid @RequestBody SucursalRequest request,
            @Parameter(description = "Id de la sucursal", example = "1")
            @PathVariable @Positive(message = "El id debe ser positivo.") Long id

    ){
        return ResponseEntity.ok(sucursalService.actualizar(request,id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una Sucursal")
    @ApiResponse(responseCode = "204", description = "Sucursal eliminada")
    public ResponseEntity eliminar(@PathVariable Long id){
        sucursalService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
