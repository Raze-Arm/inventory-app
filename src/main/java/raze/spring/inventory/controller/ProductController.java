package raze.spring.inventory.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raze.spring.inventory.service.ProductService;
import raze.spring.inventory.domain.dto.ProductDto;

import java.util.List;
import java.util.UUID;

@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping(path = {"/product", "/product/"})
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(this.productService.getProductList());
    }

    @GetMapping(path = {"/product/{id}", "/product/{id}/"})
    public ResponseEntity<ProductDto> getProduct(@PathVariable("id")UUID id){
        return ResponseEntity.ok(this.productService.getProduct(id));
    }


    @PostMapping(path = {"/product", "/product/"})
    public ResponseEntity<UUID> postProduct(@RequestBody ProductDto productDto) {
        return ResponseEntity.ok(this.productService.saveProduct(productDto));
    }


    @PutMapping(path = {"/product", "/product/"})
    public ResponseEntity<Void>  updateProduct(@RequestBody ProductDto productDto) {
        this.productService.updateProduct(productDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = {"/product/{id}", "/product/{id}/"})
    private ResponseEntity<Void> deleteProduct(@PathVariable("id") UUID id) {
        this.productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}
