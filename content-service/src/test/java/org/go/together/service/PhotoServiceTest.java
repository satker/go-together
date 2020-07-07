package org.go.together.service;

import org.apache.commons.io.FileUtils;
import org.go.together.context.RepositoryContext;
import org.go.together.dto.ContentDto;
import org.go.together.dto.IdDto;
import org.go.together.dto.PhotoCategory;
import org.go.together.dto.PhotoDto;
import org.go.together.mapper.PhotoMapper;
import org.go.together.model.Photo;
import org.go.together.repository.PhotoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = RepositoryContext.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PhotoServiceTest {
    @Autowired
    private PhotoRepository photoRepository;

    @Value("${photo.store.path}")
    private String storePath;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private PhotoMapper photoMapper;

    @BeforeEach
    public void init() throws IOException {
        PhotoDto photoDto = getPhotoDto("photos/1.jpg", PhotoCategory.EVENT);
        Photo photo = photoMapper.dtoToEntity(photoDto);
        photoRepository.save(photo);
    }

    @AfterEach
    public void clean() throws IOException {
        FileUtils.cleanDirectory(new File(storePath));
    }

    @Test
    void savePhotos() throws IOException {
        PhotoDto photoDto = getPhotoDto("photos/2.jpg", PhotoCategory.EVENT);
        Collection<IdDto> idDtos = photoService.savePhotos(Collections.singleton(photoDto));

        List<String> files = Arrays.stream(Objects.requireNonNull(new File(storePath).list()))
                .map(fileName -> fileName.split("\\.")[0])
                .collect(Collectors.toList());
        boolean correctPhotoNames = idDtos.stream()
                .map(IdDto::getId)
                .map(UUID::toString)
                .allMatch(files::contains);

        assertTrue(correctPhotoNames);
        assertEquals(1, idDtos.size());
        assertEquals(2, files.size());
    }

    @Test
    void saveOrUpdatePhotosWithOldPhotos() throws IOException {
        PhotoDto photoDto2 = getPhotoDto("photos/2.jpg", PhotoCategory.EVENT);
        PhotoDto photoDto3 = getPhotoDto("photos/3.jpg", PhotoCategory.EVENT);
        Set<Photo> photoRepositoryAll = Set.copyOf(photoRepository.findAll());
        Set<Photo> result = photoService.saveOrUpdatePhotos(Set.of(photoDto2, photoDto3), photoRepositoryAll);

        List<String> files = Arrays.stream(Objects.requireNonNull(new File(storePath).list()))
                .map(fileName -> fileName.split("\\.")[0])
                .collect(Collectors.toList());
        boolean correctPhotoNames = result.stream()
                .map(Photo::getId)
                .map(UUID::toString)
                .allMatch(files::contains);

        assertTrue(correctPhotoNames);
        assertEquals(2, result.size());
        assertEquals(2, files.size());
    }

    @Test
    void saveOrUpdatePhotosWithoutOldPhotos() throws IOException {
        PhotoDto photoDto2 = getPhotoDto("photos/2.jpg", PhotoCategory.EVENT);
        PhotoDto photoDto3 = getPhotoDto("photos/3.jpg", PhotoCategory.EVENT);
        Set<Photo> result = photoService.saveOrUpdatePhotos(Set.of(photoDto2, photoDto3), Collections.emptySet());

        List<String> files = Arrays.stream(Objects.requireNonNull(new File(storePath).list()))
                .map(fileName -> fileName.split("\\.")[0])
                .collect(Collectors.toList());
        boolean correctPhotoNames = result.stream()
                .map(Photo::getId)
                .map(UUID::toString)
                .allMatch(files::contains);

        assertTrue(correctPhotoNames);
        assertEquals(2, result.size());
        assertEquals(3, files.size());
    }

    @Test
    void deletePhotos() throws IOException {
        PhotoDto photoDto = getPhotoDto("photos/2.jpg", PhotoCategory.EVENT);
        Photo photo = photoMapper.dtoToEntity(photoDto);
        photoRepository.save(photo);

        List<String> files = Arrays.stream(Objects.requireNonNull(new File(storePath).list()))
                .map(fileName -> fileName.split("\\.")[0])
                .collect(Collectors.toList());
        assertEquals(2, files.size());

        Set<Photo> repositoryAll = Set.copyOf(photoRepository.findAll());

        photoService.deletePhotos(repositoryAll);

        List<String> deletedPhotos = Arrays.stream(Objects.requireNonNull(new File(storePath).list()))
                .map(fileName -> fileName.split("\\.")[0])
                .collect(Collectors.toList());
        assertEquals(0, deletedPhotos.size());
    }

    private File getFileFromResource(String resourceFilePath) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(Objects.requireNonNull(classLoader.getResource(resourceFilePath)).getFile());
    }

    private PhotoDto getPhotoDto(String filePath, PhotoCategory photoCategory) throws IOException {
        ContentDto contentDto = new ContentDto();
        contentDto.setType("data:image/jpeg;base64,");
        byte[] fileArray = Files.readAllBytes(getFileFromResource(filePath).toPath());
        contentDto.setPhotoContent(fileArray);
        PhotoDto photoDto = new PhotoDto();
        photoDto.setPhotoCategory(photoCategory);
        photoDto.setContent(contentDto);
        return photoDto;
    }
}