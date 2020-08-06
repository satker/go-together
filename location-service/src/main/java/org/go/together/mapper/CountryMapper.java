package org.go.together.mapper;

import org.go.together.model.Country;
import org.go.together.notification.dto.CountryDto;
import org.go.together.notification.repository.CountryRepository;
import org.go.together.repository.exceptions.CannotFindEntityException;
import org.springframework.stereotype.Component;

@Component
public class CountryMapper implements Mapper<CountryDto, Country> {
    private final CountryRepository countryRepository;

    public CountryMapper(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public CountryDto entityToDto(Country country) {
        CountryDto countryDto = new CountryDto();
        countryDto.setCountryCode(country.getCountryCode());
        countryDto.setId(country.getId());
        countryDto.setName(country.getName());
        return countryDto;
    }

    public Country dtoToEntity(CountryDto countryDto) {
        return countryRepository.findByName(countryDto.getName()).orElseThrow(() ->
                new CannotFindEntityException("Such country not present: " + countryDto.getName()));
    }
}
