package com.leonardo.almacen.controllers;

import com.leonardo.almacen.docs.ProblemaDoc;
import com.leonardo.almacen.dto.productos.ProductoRequest;
import com.leonardo.almacen.dto.productos.ProductoResponse;
import com.leonardo.almacen.entities.Producto;
import com.leonardo.almacen.services.productos.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Tag(name = "API productos", description = "Gestión de inventario de productos")
// Errores que pueden ocurrir en cualquier endpoint:
@ApiResponse(responseCode = "400", description = "Datos o parámetros inválidos",
        content = @Content(mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)))
@ApiResponse(responseCode = "500", description = "Error interno del servidor",
        content = @Content(mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)))
public class ProductoController {
    private final ProductoService productoService;
    @GetMapping
    @Operation(summary = "Listar productos", description = "Todos los filtros son opcionales")
    @ApiResponse(responseCode = "200",description = "Listado obtenido")
    public ResponseEntity<List<ProductoResponse>> listar(
           @RequestParam(required = false) String nombre,
           @RequestParam(required = false) String categoria,
           @RequestParam(required = false) BigDecimal precioMin,
           @RequestParam(required = false) BigDecimal precioMax
    ){
        return ResponseEntity.ok(productoService.listar(nombre,categoria,precioMin,precioMax));
    }
    @GetMapping("/{id}")
    @Operation(summary = "Traer un producto", description = "Trarer un producto por su id")
    @ApiResponse(responseCode = "200",description = "Producto encontrado")
    @ApiResponse(responseCode = "404", description = "El producto no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<ProductoResponse> obtenerPorId(
            @Parameter(description = "Id del producto", example = "1")
            @PathVariable @Positive Long id){
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }
    @PostMapping
    @Operation(summary = "Registrar", description = "Registrar un producto")
    @ApiResponse(responseCode = "201",description = "Producto Registrado")
    @ApiResponse(responseCode = "409", description = "Conflicto con datos existentes",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<ProductoResponse> registrar(@Valid @RequestBody ProductoRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.registrar(request));
    }
    @PutMapping
    @Operation(summary = "Actualizar", description = "Actualizar un producto")
    @ApiResponse(responseCode = "200", description = "Producto actualizado")
    @ApiResponse(responseCode = "404", description = "El producto no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    @ApiResponse(responseCode = "409", description = "Conflicto con datos existentes",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<ProductoResponse> actualizar (
            @Valid @RequestBody ProductoRequest request,
            @Parameter(description = "ID del producto",example = "1")
            @PathVariable @Positive Long id){
        return ResponseEntity.ok(productoService.actualizar(request,id));
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto")
    @ApiResponse(responseCode = "204", description = "Producto eliminado")
    @ApiResponse(responseCode = "404", description = "El producto no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    @ApiResponse(responseCode = "409", description = "El producto esta en uso y no pudo eliminarse.",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity eliminar(@PathVariable Long id){
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
