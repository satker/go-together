package org.go.together.service;

import org.apache.commons.io.FileUtils;
import org.go.together.context.RepositoryContext;
import org.go.together.dto.*;
import org.go.together.enums.CrudOperation;
import org.go.together.model.GroupPhoto;
import org.go.together.model.Photo;
import org.go.together.repository.interfaces.PhotoRepository;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextConfiguration(classes = RepositoryContext.class)
@EmbeddedKafka
class GroupPhotoServiceTest extends CrudServiceCommonTest<GroupPhoto, GroupPhotoDto> {
    @Value("${photo.store.path}")
    private String storePath;

    @Autowired
    private PhotoRepository photoRepository;

    private GroupPhoto groupPhoto;

    @Override
    @BeforeEach
    public void init() {
        super.init();
        GroupPhotoDto groupPhotoDto = createGroupPhoto(Set.of("photos/1.jpg", "photos/2.jpg"));
        IdDto idGroupPhotoDto = crudService.create(groupPhotoDto);
        Optional<GroupPhoto> groupPhotoOptional = repository.findById(idGroupPhotoDto.getId());
        if (groupPhotoOptional.isEmpty()) {
            throw new RuntimeException("Cannot find saved group");
        }
        groupPhoto = groupPhotoOptional.get();
    }

    @AfterEach
    public void clean() {
        try {
            FileUtils.deleteDirectory(new File(storePath));
        } catch (IOException e) {
            throw new RuntimeException("Cannot get file in content-service");
        }
        repository.findAll().forEach(repository::delete);
        photoRepository.findAll().forEach(photoRepository::delete);
    }

    @Test
    void createGroupPhotos() {
        GroupPhotoDto groupPhotoDto = createGroupPhoto(Set.of("photos/3.jpg", "photos/4.jpg"));
        IdDto idGroupPhotoDto = crudService.create(groupPhotoDto);
        Optional<GroupPhoto> groupPhoto = repository.findById(idGroupPhotoDto.getId());

        assertTrue(groupPhoto.isPresent());

        Set<Photo> photos = groupPhoto.get().getPhotos();
        List<String> savedFiles = checkSavedPhotosToDirectory(photos);

        assertEquals(2, photos.size());
        assertEquals(4, savedFiles.size());
    }

    @Test
    void updateGroupPhotos() {
        GroupPhotoDto groupPhotoDto = mapper.entityToDto(groupPhoto);
        groupPhotoDto.setPhotos(Set.of("photos/3.jpg", "photos/4.jpg").stream()
                .map(this::getPhotoDto)
                .collect(Collectors.toSet()));
        IdDto idGroupPhotoDto = crudService.update(groupPhotoDto);
        Optional<GroupPhoto> groupPhoto = repository.findById(idGroupPhotoDto.getId());

        assertTrue(groupPhoto.isPresent());

        Set<Photo> photos = groupPhoto.get().getPhotos();
        List<String> savedFiles = checkSavedPhotosToDirectory(photos);

        assertEquals(2, photos.size());
        assertEquals(2, savedFiles.size());
    }

    @Test
    void getGroupPhotosById() {
        GroupPhotoDto groupPhotosById = crudService.read(groupPhoto.getId());

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

    @Override
    protected GroupPhotoDto createDto() {
        GroupPhotoDto groupPhotoDto = factory.manufacturePojo(GroupPhotoDto.class);
        Set<PhotoDto> photoDtos = groupPhotoDto.getPhotos().stream()
                .peek(photoDto -> {
                    ContentDto content = photoDto.getContent();
                    content.setType("data:image/jpeg;base64,");
                    photoDto.setContent(content);

                })
                .peek(photoDto -> photoDto.setPhotoUrl(null))
                .peek(photoDto -> photoDto.setId(null))
                .collect(Collectors.toSet());
        groupPhotoDto.setPhotos(photoDtos);
        return groupPhotoDto;
    }

    protected void checkDtos(GroupPhotoDto dto, GroupPhotoDto savedObject, CrudOperation operation) {
        if (operation == CrudOperation.CREATE) {
            assertEquals(dto.getCategory(), savedObject.getCategory());
            assertEquals(dto.getGroupId(), savedObject.getGroupId());
            assertEquals(dto.getPhotos().size(), dto.getPhotos().size());
        } else if (operation == CrudOperation.UPDATE) {
            assertEquals(dto.getPhotos().size(), dto.getPhotos().size());
        }
    }
}