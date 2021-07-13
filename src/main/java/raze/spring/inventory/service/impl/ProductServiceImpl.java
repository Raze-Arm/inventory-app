package raze.spring.inventory.service.impl;

import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import raze.spring.inventory.converter.ProductDtoToProduct;
import raze.spring.inventory.converter.ProductViewToProductDto;
import raze.spring.inventory.service.ProductService;
import raze.spring.inventory.domain.Product;
import raze.spring.inventory.domain.dto.ProductDto;
import raze.spring.inventory.repository.ProductRepository;
import raze.spring.inventory.repository.ProductViewRepository;
import raze.spring.inventory.utility.FileUploadUtil;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final static String IMAGE_DIR = "files/images/product/";

    private final ProductRepository productRepository;
    private final ProductViewRepository productViewRepository;
    private final ProductDtoToProduct productDtoToProduct;
    private final ProductViewToProductDto productViewToProductDto;

      public ProductServiceImpl(
          ProductRepository productRepository,
          ProductViewRepository productViewRepository,
          ProductDtoToProduct productDtoToProduct,
          ProductViewToProductDto productViewToProductDto) {
        this.productRepository = productRepository;
        this.productViewRepository = productViewRepository;
        this.productDtoToProduct = productDtoToProduct;
        this.productViewToProductDto = productViewToProductDto;
      }

    @Override
    public Page<ProductDto> getProductDtoPage(int page, int size, String sort, String search) {
        final Pageable pageable = PageRequest.of(page, size, Sort.by(sort != null ? sort : "id"));
        if(search == null || search.length() == 0) {
            return  this.productViewRepository.findAll(pageable).map(this.productViewToProductDto::convert);
        } else {
            return  this.productViewRepository.findAll(pageable, search).map(this.productViewToProductDto::convert);
        }
    }

    @Override
    public List<ProductDto> getProductList() {

        return this.productViewRepository.findAll().stream()
            .map(this.productViewToProductDto::convert)
            .collect(Collectors.toList());
    }
    @Override
    public ProductDto getProduct(UUID id) {

        return this.productViewToProductDto.convert(
            this.productViewRepository.findById(id).orElseThrow());
    }

    @Override
    public Resource getProductImage(UUID id)  {
//        final Product product =this.productRepository.findById(id).orElse(null);
        try{
            List<Path> result = findByFileName(Path.of(IMAGE_DIR + id  ), ".original.");
//            List<Path> result = findByFileName(Path.of(IMAGE_DIR + product.getId()  ), product.getId().toString()+ ".original.");
            if(result.size() > 0) {

                Resource resource =new  UrlResource(result.get(0).toAbsolutePath().toUri());
                if(resource.exists())return  resource;
                else throw new MalformedURLException();
            }
        }catch (IOException e) {
            return null;
        }

        return null;
    }

    @Override
    public Resource getProductSmallImage(UUID id) {
//        final Product product =this.productRepository.findById(id).orElse(null);
        try{
//            List<Path> result = findByFileName(Path.of(IMAGE_DIR + product.getId()  ), product.getId().toString() + ".small.");
            List<Path> result = findByFileName(Path.of(IMAGE_DIR + id  ), ".small.");
            if(result.size() > 0) {

                Resource resource =new  UrlResource(result.get(0).toAbsolutePath().toUri());
                if(resource.exists())return  resource;
                else throw new MalformedURLException();
            }
        }catch (IOException e) {
            return null;
        }

        return null;
    }

    public static List<Path> findByFileName(Path path, String fileName)
            throws IOException {

        List<Path> result;
        try (Stream<Path> walk = java.nio.file.Files.walk(path)) {
            result = walk.filter(java.nio.file.Files::isRegularFile)
                    .filter(p -> p.toString().contains(fileName))
                    .collect(Collectors.toList());
        }
        return result;

    }


    @Override
    public UUID saveProduct(ProductDto productDto) throws IOException {
        final Product productToSave = Objects.requireNonNull(this.productDtoToProduct.convert(productDto));
        final Product product =
                this.productRepository.save(
                        productToSave);
        saveImageFile(productDto, product, IMAGE_DIR);
        return product.getId();
    }
    static void saveImageFile(ProductDto productDto, Product product, String imageDir) throws IOException {
        MultipartFile file = productDto.getImage();
        if(file != null){
            product.setImageAvailable(true);
//            final String existingImage =  product.getId()+"/"+ product.getName() + ".original.";
            final boolean isDir = java.nio.file.Files.isDirectory(Path.of(IMAGE_DIR + product.getId()));
            if(isDir) FileUtils.deleteDirectory(new File(IMAGE_DIR + product.getId()));
//            String fileName =  product.getId() + ".original."+ Date.from(Instant.now()).toString() + "." + Files.getFileExtension(file.getResource().getFilename());
            String fileName =   Date.from(Instant.now()).toString() + ".original." + Files.getFileExtension(file.getResource().getFilename());
            FileUploadUtil.saveFile(imageDir + product.getId() + "/", fileName, file);
        }
    }

    @Override
    public void updateProduct(ProductDto productDto) throws IOException {
        final Product productToEdit = updateProductProps(productDto);

        saveImageFile(productDto, productToEdit, IMAGE_DIR);

    }

    @Transactional
    @NotNull Product updateProductProps(ProductDto productDto) {
        if(productDto.getId() == null) throw new NoSuchElementException();
        final Product productToEdit= this.productRepository.findById(productDto.getId()).orElseThrow();
        productToEdit.setName(productDto.getName());
        productToEdit.setPrice(productDto.getPrice());
        productToEdit.setSalePrice(productDto.getSalePrice());
        productToEdit.setDescription(productDto.getDescription());

        this.productRepository.save(productToEdit);
        return productToEdit;
    }

    @Override
    public void deleteProduct(UUID id) {
        this.productRepository.deleteById(id);
    }
}
