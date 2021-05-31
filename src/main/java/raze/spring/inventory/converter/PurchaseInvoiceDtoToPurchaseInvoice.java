package raze.spring.inventory.converter;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import raze.spring.inventory.domain.PurchaseInvoice;
import raze.spring.inventory.domain.Supplier;
import raze.spring.inventory.domain.dto.PurchaseInvoiceDto;
import raze.spring.inventory.repository.SupplierRepository;

import java.util.stream.Collectors;

@Component
public class PurchaseInvoiceDtoToPurchaseInvoice implements Converter<PurchaseInvoiceDto, PurchaseInvoice> {
    private final PurchaseTransactionDtoToPurchaseTransaction purchaseTransactionDtoToPurchaseTransaction;
    private final SupplierDtoToSupplier supplierDtoToSupplier;
    private final SupplierRepository supplierRepository;


    public PurchaseInvoiceDtoToPurchaseInvoice(PurchaseTransactionDtoToPurchaseTransaction purchaseTransactionDtoToPurchaseTransaction, SupplierDtoToSupplier supplierDtoToSupplier, SupplierRepository supplierRepository) {
        this.purchaseTransactionDtoToPurchaseTransaction = purchaseTransactionDtoToPurchaseTransaction;
        this.supplierDtoToSupplier = supplierDtoToSupplier;
        this.supplierRepository = supplierRepository;
    }

    @Synchronized
    @Override
    public PurchaseInvoice convert(PurchaseInvoiceDto purchaseInvoiceDto) {
        Supplier supplier = null;

        if(purchaseInvoiceDto.getSupplier() != null)
            supplier = this.supplierRepository
                .findById(purchaseInvoiceDto.getSupplier().getId())
                .orElse(this.supplierDtoToSupplier.convert(purchaseInvoiceDto.getSupplier()));
        return PurchaseInvoice.builder()
            .supplier(supplier)
            .transactions(
                purchaseInvoiceDto.getTransactions().stream()
                    .map(this.purchaseTransactionDtoToPurchaseTransaction::convert)
                    .collect(Collectors.toSet()))
            .build();
    }
}
