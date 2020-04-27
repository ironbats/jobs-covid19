package com.covid.jobs;

import com.covid.dto.Country;
import com.covid.service.CountryService;
import com.covid.service.Covid19Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JobGetAllCountries {

    @Autowired
    private Covid19Service covid19Service;
    @Autowired
    private CountryService countryService;

    @Scheduled(fixedRate = 50000)
    public void saveAllCountriesInDatabase() {

        log.info("############# START RUNNING JOB GET ALL COUNTRIES ################");

        try {
            Country[] countries = covid19Service.getAllCountries();
            countryService.saveSampleCountry(countries);
        } catch (Exception cause) {
            log.warn("problem to save informatiions !" + cause.getMessage());
        }

        log.info("informtions saved !");

    }

}
