package raze.spring.inventory.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raze.spring.inventory.service.SupplierService;
import raze.spring.inventory.domain.dto.SupplierDto;

import java.util.List;
import java.util.UUID;

@RestController
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }


    @GetMapping(path = {"/supplier", "/supplier/"})
    public ResponseEntity<List<SupplierDto>> getAllSuppliers() {
        return  ResponseEntity.ok(this.supplierService.getSupplierList());
    }

    @GetMapping(path = {"/supplier/{id}", "/supplier/{id}/"})
    public ResponseEntity<SupplierDto> getSupplier(@PathVariable("id")UUID id) {
        return  ResponseEntity.ok(this.supplierService.getSupplier(id));
    }

    @PostMapping(path = {"/supplier"})
    public ResponseEntity<UUID> saveSupplier(@RequestBody SupplierDto supplierDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(this.supplierService.saveSupplier(supplierDto));
    }
    @PutMapping(path = {"/supplier"})
    public ResponseEntity<Void> updateSupplier(@RequestBody SupplierDto supplierDto) {
        this.supplierService.updateSupplier(supplierDto);
        return  ResponseEntity.ok().build();
    }

    @DeleteMapping(path = {"/supplier/{id}", "/supplier/{id}/"})
    public ResponseEntity<Void> deleteSupplier(@PathVariable("id") UUID id) {
        this.supplierService.deleteSupplier(id);
        return ResponseEntity.ok().build();
    }




}
