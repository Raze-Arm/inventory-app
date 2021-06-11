package raze.spring.inventory.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import raze.spring.inventory.converter.PurchaseInvoiceDtoToPurchaseInvoice;
import raze.spring.inventory.converter.PurchaseInvoiceToPurchaseInvoiceDto;
import raze.spring.inventory.service.PurchaseInvoiceService;
import raze.spring.inventory.domain.PurchaseInvoice;
import raze.spring.inventory.domain.dto.PurchaseInvoiceDto;
import raze.spring.inventory.repository.PurchaseInvoiceRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PurchaseInvoiceServiceImpl implements PurchaseInvoiceService {
    private final PurchaseInvoiceRepository invoiceRepository;
    private final PurchaseInvoiceDtoToPurchaseInvoice invoiceDtoToInvoice;
    private final PurchaseInvoiceToPurchaseInvoiceDto invoiceToInvoiceDto;

      public PurchaseInvoiceServiceImpl(
          PurchaseInvoiceRepository invoiceRepository,
          PurchaseInvoiceDtoToPurchaseInvoice invoiceDtoToInvoice,
          PurchaseInvoiceToPurchaseInvoiceDto invoiceToInvoiceDto) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceDtoToInvoice = invoiceDtoToInvoice;
        this.invoiceToInvoiceDto = invoiceToInvoiceDto;
      }

    @Override
    public Page<PurchaseInvoiceDto> getInvoicePage(int page, int size, String sort, String search) {
        final Pageable pageable = PageRequest.of(page, size, Sort.by(sort != null ? sort : "id"));
        if(search == null || search.length() == 0) {
            return this.invoiceRepository.findAll(pageable).map(this.invoiceToInvoiceDto::convert);
        } else {
            return  this.invoiceRepository.findAll(pageable, search).map(this.invoiceToInvoiceDto::convert);
        }
    }

    @Transactional
    @Override
    public List<PurchaseInvoiceDto> getInvoiceList() {
        return this.invoiceRepository.findAll().stream()
            .map(this.invoiceToInvoiceDto::convert)
            .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public PurchaseInvoiceDto getInvoice(UUID id) {

        return this.invoiceToInvoiceDto.convert(this.invoiceRepository.findById(id).orElseThrow());
    }

    @Transactional
    @Override
    public UUID saveInvoice(PurchaseInvoiceDto invoiceDto) {
        if(invoiceDto.getTransactions() == null) throw new NullPointerException("Null Transactions");
        final PurchaseInvoice invoice =
            this.invoiceRepository.save(
                Objects.requireNonNull(this.invoiceDtoToInvoice.convert(invoiceDto)));
        return invoice.getId();
    }



    @Override
    public void deleteInvoice(UUID id) {
        this.invoiceRepository.deleteById(id);
    }
}
