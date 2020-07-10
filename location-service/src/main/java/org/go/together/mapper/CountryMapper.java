package org.go.together.mapper;

import org.go.together.dto.CountryDto;
import org.go.together.logic.Mapper;
import org.go.together.model.Country;
import org.go.together.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CountryMapper implements Mapper<CountryDto, Country> {
    private CountryService countryService;

    @Autowired
    public void setCountryService(CountryService countryService) {
        this.countryService = countryService;
    }

    public CountryDto entityToDto(Country country) {
        CountryDto countryDto = new CountryDto();
        countryDto.setCountryCode(country.getCountryCode());
        countryDto.setId(country.getId());
        countryDto.setName(country.getName());
        return countryDto;
    }

    public Country dtoToEntity(CountryDto countryDto) {
        Optional<Country> countryById = Optional.ofNullable(countryService.findByName(countryDto.getName()));
        return countryById.orElseThrow(() -> new RuntimeException("Such country not present: " + countryDto.getName()));
    }
}
