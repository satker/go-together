package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = RepositoryContext.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EventPhotoServiceTest {

    @Test
    void setPhotoService() {
    }

    @Test
    void savePhotosForEvent() {
    }

    @Test
    void createOrUpdatePhotosByEventPhotoDto() {
    }

    @Test
    void enrichEntity() {
    }

    @Test
    void getEventPhotosById() {
    }
}