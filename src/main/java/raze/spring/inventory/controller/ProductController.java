package raze.spring.inventory.controller;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raze.spring.inventory.service.ProductService;
import raze.spring.inventory.domain.dto.ProductDto;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping("/v1")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(path = {"/product", "/product/"})
    public ResponseEntity<Page<ProductDto>> getProductPage(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam(value = "sort",required = false) String sort,
            @RequestParam(value = "search", required = false) String search) {
        return ResponseEntity.ok(this.productService.getProductDtoPage(page,size,sort, search));
    }
    @GetMapping(path = {"/product", "/product/"}, params = {"search-type=list"})
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(this.productService.getProductList());
    }

    @GetMapping(path = {"/product/{id}", "/product/{id}/"})
    public ResponseEntity<ProductDto> getProduct(@PathVariable("id")UUID id){
        return ResponseEntity.ok(this.productService.getProduct(id));
    }
    @GetMapping(path = {"/download/product/{id}"})
    public ResponseEntity<Resource> downloadProductImage(@PathVariable UUID id) {
        return ResponseEntity.ok(this.productService.getProductImage(id));
    }

    @GetMapping(path = {"/download/small/product/{id}"})
    public ResponseEntity<Resource> downloadSmallProductImage(@PathVariable UUID id) {
        return ResponseEntity.ok(this.productService.getProductSmallImage(id));
    }

    @PostMapping(path = {"/product", "/product/"})
    public ResponseEntity<UUID> postProduct(@Valid @ModelAttribute ProductDto productDto) throws IOException {
        return ResponseEntity.ok(this.productService.saveProduct(productDto));
    }


    @PutMapping(path = {"/product", "/product/"})
    public ResponseEntity<Void>  updateProduct(@Valid@ModelAttribute ProductDto productDto) throws IOException {
        this.productService.updateProduct(productDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = {"/product/{id}", "/product/{id}/"})
    private ResponseEntity<Void> deleteProduct(@PathVariable("id") UUID id) {
        this.productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}
