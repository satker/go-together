package org.go.together.service;

import org.apache.commons.io.FileUtils;
import org.go.together.context.RepositoryContext;
import org.go.together.dto.ContentDto;
import org.go.together.dto.PhotoDto;
import org.go.together.model.Photo;
import org.go.together.notification.streams.NotificationSource;
import org.go.together.service.interfaces.PhotoService;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = RepositoryContext.class)
class PhotoServiceTest extends CrudServiceCommonTest<Photo, PhotoDto> {
    @Value("${photo.store.path}")
    private String storePath;

    @Autowired
    private NotificationSource source;

    @Override
    @BeforeEach
    public void init() {
        super.init();
        MessageChannel messageChannel = Mockito.mock(MessageChannel.class);
        when(source.output()).thenReturn(messageChannel);
        when(messageChannel.send(any())).thenReturn(true);
        PhotoDto photoDto;
        try {
            photoDto = getPhotoDto("photos/1.jpg");
        } catch (IOException e) {
            throw new RuntimeException("Cannot get file in content-service");
        }
        crudService.create(photoDto);
    }

    @AfterEach
    public void clean() {
        try {
            FileUtils.deleteDirectory(new File(storePath));
        } catch (IOException e) {
            throw new RuntimeException("Cannot get file in content-service");
        }
        repository.findAll().forEach(repository::delete);
    }

    @Test
    void saveOrUpdatePhotosWithOldPhotos() throws IOException {
        PhotoDto photoDto2 = getPhotoDto("photos/2.jpg");
        PhotoDto photoDto3 = getPhotoDto("photos/3.jpg");
        Set<Photo> photoRepositoryAll = Set.copyOf(repository.findAll());
        Set<Photo> result = ((PhotoService) crudService).savePhotos(Set.of(photoDto2, photoDto3), photoRepositoryAll);

        List<String> files = checkSavedPhotosToDirectory(result);
        assertEquals(2, result.size());
        assertEquals(2, files.size());
    }

    @Test
    void saveOrUpdatePhotosWithoutOldPhotos() throws IOException {
        PhotoDto photoDto2 = getPhotoDto("photos/2.jpg");
        PhotoDto photoDto3 = getPhotoDto("photos/3.jpg");
        Set<Photo> result = ((PhotoService) crudService).savePhotos(Set.of(photoDto2, photoDto3), Collections.emptySet());

        List<String> files = checkSavedPhotosToDirectory(result);
        assertEquals(2, result.size());
        assertEquals(3, files.size());
    }

    @Test
    void validationPhoto() throws IOException {
        PhotoDto photoDto = getPhotoDto("photos/2.jpg");
        String result = validator.validate(photoDto, null);

        assertTrue(StringUtils.isBlank(result));
    }

    @Test
    void validationPhotoWithEmptyContent() throws IOException {
        PhotoDto photoDto = getPhotoDto("photos/2.jpg");
        photoDto.setContent(new ContentDto());

        String result = validator.validate(photoDto, null);

        assertTrue(StringUtils.isNotBlank(result));
    }

    @Test
    void deletePhotos() throws IOException {
        PhotoDto photoDto = getPhotoDto("photos/2.jpg");
        crudService.create(photoDto);

        List<String> files = Arrays.stream(Objects.requireNonNull(new File(storePath).list()))
                .map(fileName -> fileName.split("\\.")[0])
                .collect(Collectors.toList());
        assertEquals(2, files.size());

        Set<Photo> repositoryAll = Set.copyOf(repository.findAll());

        repositoryAll.stream().map(Photo::getId).forEach(crudService::delete);

        List<String> deletedPhotos = Arrays.stream(Objects.requireNonNull(new File(storePath).list()))
                .map(fileName -> fileName.split("\\.")[0])
                .collect(Collectors.toList());
        assertEquals(0, deletedPhotos.size());
    }

    private File getFileFromResource(String resourceFilePath) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(Objects.requireNonNull(classLoader.getResource(resourceFilePath)).getFile());
    }

    private PhotoDto getPhotoDto(String filePath) throws IOException {
        ContentDto contentDto = new ContentDto();
        contentDto.setType("data:image/jpeg;base64,");
        byte[] fileArray = Files.readAllBytes(getFileFromResource(filePath).toPath());
        contentDto.setPhotoContent(fileArray);
        PhotoDto photoDto = new PhotoDto();
        photoDto.setContent(contentDto);
        return photoDto;
    }

    private List<String> checkSavedPhotosToDirectory(Set<Photo> result) {
        List<String> files = Arrays.stream(Objects.requireNonNull(new File(storePath).list()))
                .map(fileName -> fileName.split("\\.")[0])
                .collect(Collectors.toList());
        boolean correctPhotoNames = result.stream()
                .map(Photo::getId)
                .map(UUID::toString)
                .allMatch(files::contains);

        assertTrue(correctPhotoNames);
        return files;
    }

    @Override
    protected PhotoDto createDto() {
        PhotoDto photoDto = factory.manufacturePojo(PhotoDto.class);
        photoDto.setPhotoUrl(null);
        photoDto.getContent().setType("data:image/jpeg;base64,");
        return photoDto;
    }
}