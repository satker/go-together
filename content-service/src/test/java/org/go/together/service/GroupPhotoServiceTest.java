package org.go.together.service;

import org.apache.commons.io.FileUtils;
import org.go.together.context.RepositoryContext;
import org.go.together.dto.ContentDto;
import org.go.together.dto.GroupPhotoDto;
import org.go.together.dto.PhotoCategory;
import org.go.together.dto.PhotoDto;
import org.go.together.mapper.GroupPhotoMapper;
import org.go.together.model.GroupPhoto;
import org.go.together.repository.GroupPhotoRepository;
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
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

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
    private GroupPhotoMapper groupPhotoMapper;

    private GroupPhoto groupPhoto;

    @BeforeEach
    public void init() throws IOException {
        PhotoDto photoDto1 = getPhotoDto("photos/1.jpg");
        PhotoDto photoDto2 = getPhotoDto("photos/2.jpg");

        GroupPhotoDto groupPhotoDto = new GroupPhotoDto();
        groupPhotoDto.setPhotos(Set.of(photoDto1, photoDto2));
        groupPhotoDto.setGroupId(UUID.randomUUID());
        groupPhotoDto.setCategory(PhotoCategory.EVENT);

        GroupPhoto groupPhoto = groupPhotoMapper.dtoToEntity(groupPhotoDto);

        groupPhoto = groupPhotoRepository.save(groupPhoto);
    }

    @AfterEach
    public void clean() throws IOException {
        FileUtils.cleanDirectory(new File(storePath));
        groupPhotoRepository.findAll().forEach(groupPhotoRepository::delete);
    }

    @Test
    void savePhotosForEvent() {

    }

    @Test
    void savePhotosByEventPhotoDto() {
    }

    @Test
    void getEventPhotosById() {
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
}