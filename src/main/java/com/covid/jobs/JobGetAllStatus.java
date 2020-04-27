package com.covid.jobs;

import com.covid.dto.CountryDayOne;
import com.covid.dto.FallbackDTO;
import com.covid.service.CountryService;
import com.covid.service.Covid19Service;
import com.covid.service.FallBackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class JobGetAllStatus {

    @Autowired
    private CountryService countryService;
    @Autowired
    private Covid19Service covid19Service;
    @Autowired
    private FallBackService fallBackService;

    @Scheduled(fixedRate = 5000)
    public void saveAllStatusInDatabase() {

        log.info("############# START RUNNING JOB GET ALL STATUS ################");


        List<FallbackDTO> listOfFallbacks = new ArrayList<>();

        countryService.countryCode().forEach(code -> {
            try {
                CountryDayOne[] countriesDayOne = covid19Service.getCountryDayOneAllStatus(code);
                countryService.saveCountryDayOneAllStatus(countriesDayOne);
            } catch (Exception cause) {
                log.warn(cause.getMessage() + "    " + code);
                FallbackDTO fallbackDTO = new FallbackDTO();
                fallbackDTO.setCountryCode(code);
                fallbackDTO.setMessageError(cause.getMessage());
                listOfFallbacks.add(fallbackDTO);
            }
        });

        try {
            fallBackService.saveFallBackCountries(listOfFallbacks);
        } catch (Exception cause) {
            log.warn(cause.getMessage());
        }
        log.info("############# ALL INFORMATIONS SAVED  ################");


    }
}
