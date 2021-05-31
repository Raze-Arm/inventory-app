package raze.spring.inventory.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import raze.spring.inventory.converter.SupplierDtoToSupplier;
import raze.spring.inventory.converter.SupplierToSupplierDto;
import raze.spring.inventory.service.SupplierService;
import raze.spring.inventory.domain.Supplier;
import raze.spring.inventory.domain.dto.SupplierDto;
import raze.spring.inventory.repository.SupplierRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierDtoToSupplier supplierDtoToSupplier;
    private final SupplierToSupplierDto supplierToSupplierDto;

      public SupplierServiceImpl(
          SupplierRepository supplierRepository,
          SupplierDtoToSupplier supplierDtoToSupplier,
          SupplierToSupplierDto supplierToSupplierDto) {
        this.supplierRepository = supplierRepository;
        this.supplierDtoToSupplier = supplierDtoToSupplier;
        this.supplierToSupplierDto = supplierToSupplierDto;
      }

    @Override
    public Page<SupplierDto> getSupplierPage(int page, int size, String sort, String search) {
          final Pageable pageable = PageRequest.of(page, size, Sort.by(sort != null ? sort : "id"));
          if(search == null || search.length() == 0) {
              return  this.supplierRepository.findAll(pageable).map(this.supplierToSupplierDto::convert);
          } else {
              return this.supplierRepository.findAll(pageable, search).map(this.supplierToSupplierDto::convert);
          }
    }

    @Transactional
    @Override
    public List<SupplierDto> getSupplierList() {

        return this.supplierRepository.findAll().stream()
            .map(this.supplierToSupplierDto::convert)
            .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public SupplierDto getSupplier(UUID id) {

        return this.supplierToSupplierDto.convert(this.supplierRepository.findById(id).orElseThrow());
    }

    @Transactional
    @Override
    public UUID saveSupplier(SupplierDto supplierDto) {

        final Supplier supplier =
            this.supplierRepository.save(
                Objects.requireNonNull(this.supplierDtoToSupplier.convert(supplierDto)));
        return supplier.getId();
    }

    @Transactional
    @Override
    public void updateSupplier(SupplierDto supplierDto) {
        if(supplierDto.getId() == null) throw new NoSuchElementException();
        final Supplier supplierToEdit = this.supplierRepository.findById(supplierDto.getId()).orElseThrow();
        supplierToEdit.setFirstName(supplierDto.getFirstName());
        supplierToEdit.setLastName(supplierDto.getLastName());
        supplierToEdit.setAddress(supplierDto.getAddress());

        this.supplierRepository.save(supplierToEdit);
    }

    @Override
    public void deleteSupplier(UUID id) {
        this.supplierRepository.deleteById(id);
    }
}
