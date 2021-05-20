package raze.spring.inventory.converter.service.impl;

import org.springframework.stereotype.Service;
import raze.spring.inventory.converter.SaleInvoiceDtoToSaleInvoice;
import raze.spring.inventory.converter.SaleInvoiceToSaleInvoiceDto;
import raze.spring.inventory.converter.service.SaleInvoiceService;
import raze.spring.inventory.domain.SaleInvoice;
import raze.spring.inventory.domain.dto.SaleInvoiceDto;
import raze.spring.inventory.repository.SaleInvoiceRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SaleInvoiceServiceImpl implements SaleInvoiceService {
    private final SaleInvoiceRepository invoiceRepository;
    private final SaleInvoiceDtoToSaleInvoice invoiceDtoToInvoice;
    private final SaleInvoiceToSaleInvoiceDto invoiceToInvoiceDto;

      public SaleInvoiceServiceImpl(
          SaleInvoiceRepository invoiceRepository,
          SaleInvoiceDtoToSaleInvoice invoiceDtoToInvoice,
          SaleInvoiceToSaleInvoiceDto invoiceToInvoiceDto) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceDtoToInvoice = invoiceDtoToInvoice;
        this.invoiceToInvoiceDto = invoiceToInvoiceDto;
      }
    @Transactional
    @Override
    public List<SaleInvoiceDto> getInvoiceList() {

        return this.invoiceRepository.findAll().stream()
            .map(this.invoiceToInvoiceDto::convert)
            .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public SaleInvoiceDto getInvoice(UUID id) {
        return this.invoiceToInvoiceDto.convert(this.invoiceRepository.findById(id).orElseThrow());
    }

    @Transactional
    @Override
    public UUID saveInvoice(SaleInvoiceDto saleInvoiceDto) {
        final SaleInvoice invoice =
            this.invoiceRepository.save(
                Objects.requireNonNull(this.invoiceDtoToInvoice.convert(saleInvoiceDto)));
        return invoice.getId();
    }

    @Override
    public void deleteInvoice(UUID id) {
        this.invoiceRepository.deleteById(id);
    }
}
