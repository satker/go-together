package org.go.together.mapper;

import lombok.RequiredArgsConstructor;
import org.go.together.base.CommonMapper;
import org.go.together.dto.CountryDto;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.model.Country;
import org.go.together.repository.interfaces.CountryRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CountryMapper extends CommonMapper<CountryDto, Country> {
    private final CountryRepository countryRepository;

    public CountryDto toDto(Country country) {
        CountryDto countryDto = new CountryDto();
        countryDto.setCountryCode(country.getCountryCode());
        countryDto.setId(country.getId());
        countryDto.setName(country.getName());
        return countryDto;
    }

    public Country toEntity(CountryDto countryDto) {
        return countryRepository.findByName(countryDto.getName()).orElseThrow(() ->
                new CannotFindEntityException("Such country not present: " + countryDto.getName()));
    }
}
