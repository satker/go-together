package org.go.together.mapper;

import lombok.RequiredArgsConstructor;
import org.go.together.dto.CountryDto;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.model.Country;
import org.go.together.repository.interfaces.CountryRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CountryMapper implements Mapper<CountryDto, Country> {
    private final CountryRepository countryRepository;

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
