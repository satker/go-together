package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.CountryDto;
import org.go.together.enums.CrudOperation;
import org.go.together.kafka.NotificationEvent;
import org.go.together.model.Country;
import org.go.together.service.interfaces.CountryService;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@ContextConfiguration(classes = RepositoryContext.class)
class CountryServiceTest extends CrudServiceCommonTest<Country, CountryDto> {
    @Autowired
    private KafkaTemplate<UUID, NotificationEvent> kafkaTemplate;

    @Override
    @BeforeEach
    public void init() {
        super.init();
        doNothing().when(kafkaTemplate.send(any(), any(), any()));
    }

    @Test
    void findCountriesLike() {
        Collection<Country> countriesLike =
                ((CountryService) crudService).findCountriesLike(dto.getName().substring(0, 3));

        assertEquals(1, countriesLike.size());
        Country foundCountry = countriesLike.iterator().next();
        assertEquals(dto.getName(), foundCountry.getName());
        assertEquals(dto.getCountryCode(), foundCountry.getCountryCode());
    }

    @Override
    protected CountryDto createDto() {
        Country country = factory.manufacturePojo(Country.class);
        country.setCountryCode(country.getCountryCode().toUpperCase());
        country.setName(country.getName().toUpperCase());
        Country savedCountry = repository.save(country);
        return mapper.entityToDto(savedCountry);
    }

    @Override
    protected CountryDto getCreatedEntityId(CountryDto dto) {
        return dto;
    }

    @Override
    protected void checkDtos(CountryDto dto, CountryDto savedObject, CrudOperation operation) {
        assertEquals(dto.getName(), savedObject.getName());
        assertEquals(dto.getCountryCode(), savedObject.getCountryCode());
    }

    @Override
    public void updateTestWithNotPresentedId() {
    }
}