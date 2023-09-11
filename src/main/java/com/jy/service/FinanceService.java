package com.jy.service;

import com.jy.model.Company;
import com.jy.model.Dividend;
import com.jy.model.ScrapedResult;
import com.jy.model.constants.CacheKey;
import com.jy.persist.CompanyRepository;
import com.jy.persist.DividendRepository;
import com.jy.persist.entity.CompanyEntity;
import com.jy.persist.entity.DividendEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class FinanceService {
    
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    @Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE)
    public ScrapedResult getDividendByCompanyName(String companyName) {
        // 1. 회사명을 기준으로 회사 정보를 조회
        CompanyEntity company = this.companyRepository.findByName(companyName)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 회사명입니다 "));

        // 2. 조회된 회사 ID 로 배당금 정보 조회
        List<DividendEntity> dividendEntities = this.dividendRepository.findAllByCompanyId(company.getId());

        // 3. 결과 조합 후 반환
        List<Dividend> dividends = new ArrayList<>();

        for (var entity : dividendEntities) {
            dividends.add(Dividend.builder()
                .date(entity.getDate())
                .dividend(entity.getDividend())
                .build());
        }

         return new ScrapedResult(Company.builder()
            .name(company.getName())
            .ticker(company.getTicker())
            .build(), dividends);
    }
}
