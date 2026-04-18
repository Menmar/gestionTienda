package com.menmar.gestionTienda.controller;

import com.menmar.gestionTienda.model.PageResponse;
import com.menmar.gestionTienda.model.cliente.ClienteRequest;
import com.menmar.gestionTienda.model.cliente.ClienteResponse;
import com.menmar.gestionTienda.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Gestión del fichero de clientes de la tienda")
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    @Operation(summary = "Crear cliente", description = "Registra un nuevo cliente. El teléfono debe ser único.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente creado"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "El teléfono ya existe")
    })
    public ResponseEntity<ClienteResponse> crear(@Valid @RequestBody ClienteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.crear(request));
    }

    @GetMapping
    @Operation(summary = "Listar o buscar clientes",
               description = "Sin ?nombre devuelve todos paginados. Con ?nombre filtra por nombre/apellidos (parcial, sin distinción de mayúsculas).")
    @ApiResponse(responseCode = "200", description = "Listado de clientes")
    public ResponseEntity<PageResponse<ClienteResponse>> listar(
            @Parameter(description = "Texto a buscar en nombre o apellidos (opcional)") @RequestParam(required = false) String nombre,
            @Parameter(description = "Número de página (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "20") int size) {
        var pageable = PageRequest.of(page, size, Sort.by("apellidos").ascending());
        if (nombre != null && !nombre.isBlank()) {
            return ResponseEntity.ok(clienteService.buscarPorNombre(nombre, pageable));
        }
        return ResponseEntity.ok(clienteService.listar(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<ClienteResponse> buscar(
            @Parameter(description = "ID del cliente") @PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @GetMapping("/telefono/{telefono}")
    @Operation(summary = "Buscar cliente por teléfono", description = "Útil para localizar rápidamente a un cliente al abrir un nuevo ticket.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe cliente con ese teléfono")
    })
    public ResponseEntity<ClienteResponse> buscarPorTelefono(
            @Parameter(description = "Número de teléfono del cliente") @PathVariable String telefono) {
        return ResponseEntity.ok(clienteService.buscarPorTelefono(telefono));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente actualizado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<ClienteResponse> actualizar(
            @Parameter(description = "ID del cliente") @PathVariable Long id,
            @Valid @RequestBody ClienteRequest request) {
        return ResponseEntity.ok(clienteService.actualizar(id, request));
    }
}
