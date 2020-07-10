package org.go.together.service;

import org.apache.commons.io.FileUtils;
import org.go.together.context.RepositoryContext;
import org.go.together.dto.*;
import org.go.together.mapper.GroupPhotoMapper;
import org.go.together.model.GroupPhoto;
import org.go.together.model.Photo;
import org.go.together.repository.GroupPhotoRepository;
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
class GroupPhotoServiceTest {
    @Value("${photo.store.path}")
    private String storePath;

    @Autowired
    private GroupPhotoRepository groupPhotoRepository;

    @Autowired
    private GroupPhotoService groupPhotoService;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private GroupPhotoMapper groupPhotoMapper;

    private GroupPhoto groupPhoto;

    @BeforeEach
    public void init() {
        GroupPhotoDto groupPhotoDto = createGroupPhoto(Set.of("photos/1.jpg", "photos/2.jpg"));
        IdDto idGroupPhotoDto = groupPhotoService.create(groupPhotoDto);
        Optional<GroupPhoto> groupPhotoOptional = groupPhotoRepository.findById(idGroupPhotoDto.getId());
        if (groupPhotoOptional.isEmpty()) {
            throw new RuntimeException("Cannot find saved group");
        }
        groupPhoto = groupPhotoOptional.get();
    }

    @AfterEach
    public void clean() throws IOException {
        FileUtils.cleanDirectory(new File(storePath));
        groupPhotoRepository.findAll().forEach(groupPhotoRepository::delete);
        photoRepository.findAll().forEach(photoRepository::delete);
    }

    @Test
    void createGroupPhotos() {
        GroupPhotoDto groupPhotoDto = createGroupPhoto(Set.of("photos/3.jpg", "photos/4.jpg"));
        IdDto idGroupPhotoDto = groupPhotoService.create(groupPhotoDto);
        Optional<GroupPhoto> groupPhoto = groupPhotoRepository.findById(idGroupPhotoDto.getId());

        assertTrue(groupPhoto.isPresent());

        Set<Photo> photos = groupPhoto.get().getPhotos();
        List<String> savedFiles = checkSavedPhotosToDirectory(photos);

        assertEquals(2, photos.size());
        assertEquals(4, savedFiles.size());
    }

    @Test
    void updateGroupPhotos() {
        GroupPhotoDto groupPhotoDto = groupPhotoMapper.entityToDto(groupPhoto);
        groupPhotoDto.setPhotos(Set.of("photos/3.jpg", "photos/4.jpg").stream()
                .map(this::getPhotoDto)
                .collect(Collectors.toSet()));
        IdDto idGroupPhotoDto = groupPhotoService.update(groupPhotoDto);
        Optional<GroupPhoto> groupPhoto = groupPhotoRepository.findById(idGroupPhotoDto.getId());

        assertTrue(groupPhoto.isPresent());

        Set<Photo> photos = groupPhoto.get().getPhotos();
        List<String> savedFiles = checkSavedPhotosToDirectory(photos);

        assertEquals(2, photos.size());
        assertEquals(2, savedFiles.size());
    }

    @Test
    void getGroupPhotosById() {
        GroupPhotoDto groupPhotosById = groupPhotoService.read(groupPhoto.getId());

        Set<UUID> foundPhotosId = groupPhotosById.getPhotos().stream()
                .map(PhotoDto::getId)
                .collect(Collectors.toSet());

        assertEquals(2, groupPhotosById.getPhotos().size());
        assertTrue(groupPhoto.getPhotos().stream()
                .map(Photo::getId)
                .allMatch(foundPhotosId::contains));
        assertEquals(PhotoCategory.EVENT, groupPhotosById.getCategory());
    }

    private GroupPhotoDto createGroupPhoto(Set<String> photos) {
        GroupPhotoDto groupPhotoDto = new GroupPhotoDto();
        groupPhotoDto.setPhotos(photos.stream().map(this::getPhotoDto).collect(Collectors.toSet()));
        groupPhotoDto.setGroupId(UUID.randomUUID());
        groupPhotoDto.setCategory(PhotoCategory.EVENT);

        return groupPhotoDto;
    }

    private File getFileFromResource(String resourceFilePath) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(Objects.requireNonNull(classLoader.getResource(resourceFilePath)).getFile());
    }

    private PhotoDto getPhotoDto(String filePath) {
        ContentDto contentDto = new ContentDto();
        contentDto.setType("data:image/jpeg;base64,");
        byte[] fileArray;
        try {
            fileArray = Files.readAllBytes(getFileFromResource(filePath).toPath());
        } catch (IOException e) {
            throw new RuntimeException("Cannot read photo by path" + filePath);
        }
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
}