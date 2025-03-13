package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import com.devsuperior.dsmeta.dto.SalesReportDTO;
import com.devsuperior.dsmeta.dto.SalesSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;


	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	public Page<SalesReportDTO> getReport(String minDate, String maxDate, String name, Pageable pageable) {
		LocalDate maxDateConverted = maxDateConverter(maxDate);
		LocalDate minDateConverted = minDateConverter(minDate, maxDateConverted);
		return repository.searchSalesReport(minDateConverted, maxDateConverted, name, pageable);
	}

	public List<SalesSummaryDTO> getSumary(String minDate, String maxDate) {
		LocalDate maxDateConverted = maxDateConverter(maxDate);
		LocalDate minDateConverted = minDateConverter(minDate, maxDateConverted);
		return repository.searchSalesSummary(minDateConverted, maxDateConverted);
	}

	public LocalDate maxDateConverter(String maxDate) {
		LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		LocalDate maxDateConverted;
		if(maxDate == null || maxDate.isEmpty()){
			maxDateConverted = today;
		}else{
			maxDateConverted = LocalDate.parse(maxDate);
		}
		return maxDateConverted;
	}

	public LocalDate minDateConverter(String minDate, LocalDate maxDateConverted) {
		LocalDate minDateConverted;
		if(minDate == null || minDate.isEmpty()){
			minDateConverted = maxDateConverted.minusYears(1);
		}
		else{
			minDateConverted = LocalDate.parse(minDate);
		}
		return minDateConverted;
	}
}
