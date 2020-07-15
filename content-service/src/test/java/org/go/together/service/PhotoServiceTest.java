package org.go.together.service;

import org.apache.commons.io.FileUtils;
import org.go.together.context.RepositoryContext;
import org.go.together.dto.ContentDto;
import org.go.together.dto.PhotoDto;
import org.go.together.model.Photo;
import org.go.together.repository.PhotoRepository;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextConfiguration(classes = RepositoryContext.class)
class PhotoServiceTest extends CrudServiceCommonTest<Photo, PhotoDto> {
    @Autowired
    private PhotoRepository photoRepository;

    @Value("${photo.store.path}")
    private String storePath;

    @Autowired
    private PhotoService photoService;

    @BeforeEach
    public void init() throws IOException {
        PhotoDto photoDto = getPhotoDto("photos/1.jpg");
        photoService.create(photoDto);
    }

    @AfterEach
    public void clean() throws IOException {
        FileUtils.cleanDirectory(new File(storePath));
        photoRepository.findAll().forEach(photoRepository::delete);
    }

    @Test
    void saveOrUpdatePhotosWithOldPhotos() throws IOException {
        PhotoDto photoDto2 = getPhotoDto("photos/2.jpg");
        PhotoDto photoDto3 = getPhotoDto("photos/3.jpg");
        Set<Photo> photoRepositoryAll = Set.copyOf(photoRepository.findAll());
        Set<Photo> result = photoService.savePhotos(Set.of(photoDto2, photoDto3), photoRepositoryAll);

        List<String> files = checkSavedPhotosToDirectory(result);
        assertEquals(2, result.size());
        assertEquals(2, files.size());
    }

    @Test
    void saveOrUpdatePhotosWithoutOldPhotos() throws IOException {
        PhotoDto photoDto2 = getPhotoDto("photos/2.jpg");
        PhotoDto photoDto3 = getPhotoDto("photos/3.jpg");
        Set<Photo> result = photoService.savePhotos(Set.of(photoDto2, photoDto3), Collections.emptySet());

        List<String> files = checkSavedPhotosToDirectory(result);
        assertEquals(2, result.size());
        assertEquals(3, files.size());
    }

    @Test
    void validationPhoto() throws IOException {
        PhotoDto photoDto = getPhotoDto("photos/2.jpg");
        String result = photoService.validate(photoDto);

        assertTrue(StringUtils.isBlank(result));
    }

    @Test
    void validationPhotoWithEmptyContent() throws IOException {
        PhotoDto photoDto = getPhotoDto("photos/2.jpg");
        photoDto.setContent(new ContentDto());

        String result = photoService.validate(photoDto);

        assertTrue(StringUtils.isNotBlank(result));
    }

    @Test
    void deletePhotos() throws IOException {
        PhotoDto photoDto = getPhotoDto("photos/2.jpg");
        photoService.create(photoDto);

        List<String> files = Arrays.stream(Objects.requireNonNull(new File(storePath).list()))
                .map(fileName -> fileName.split("\\.")[0])
                .collect(Collectors.toList());
        assertEquals(2, files.size());

        Set<Photo> repositoryAll = Set.copyOf(photoRepository.findAll());

        repositoryAll.stream().map(Photo::getId).forEach(photoService::delete);

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