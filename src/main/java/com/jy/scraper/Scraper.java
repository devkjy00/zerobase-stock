package com.jy.scraper;


import com.jy.model.Company;
import com.jy.model.ScrapedResult;

public interface Scraper {
    Company scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(Company company);
}
