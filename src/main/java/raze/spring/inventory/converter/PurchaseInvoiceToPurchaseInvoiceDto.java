package raze.spring.inventory.converter;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import raze.spring.inventory.domain.PurchaseInvoice;
import raze.spring.inventory.domain.dto.PurchaseInvoiceDto;
import raze.spring.inventory.utility.DateMapper;

import java.util.stream.Collectors;

@Component
public class PurchaseInvoiceToPurchaseInvoiceDto implements Converter<PurchaseInvoice, PurchaseInvoiceDto> {
    private final DateMapper dateMapper;
    private final PurchaseTransactionToPurchaseTransactionDto purchaseTransactionToPurchaseTransactionDto;
    private final SupplierToSupplierDto supplierToSupplierDto;
    public PurchaseInvoiceToPurchaseInvoiceDto(DateMapper dateMapper, PurchaseTransactionToPurchaseTransactionDto purchaseTransactionToPurchaseTransactionDto, SupplierToSupplierDto supplierToSupplierDto) {
        this.dateMapper = dateMapper;
        this.purchaseTransactionToPurchaseTransactionDto = purchaseTransactionToPurchaseTransactionDto;
        this.supplierToSupplierDto = supplierToSupplierDto;
    }

    @Synchronized
    @Override
    public PurchaseInvoiceDto convert(PurchaseInvoice purchaseInvoice) {
        return PurchaseInvoiceDto.builder()
            .id(purchaseInvoice.getId())
            .supplier(this.supplierToSupplierDto.convert(purchaseInvoice.getSupplier()))
            .createdDate(dateMapper.asOffsetDateTime(purchaseInvoice.getCreatedDate()))
            .transactions(
                purchaseInvoice.getTransactions().stream()
                    .map(this.purchaseTransactionToPurchaseTransactionDto::convert)
                    .collect(Collectors.toSet()))
            .build();
    }
}
