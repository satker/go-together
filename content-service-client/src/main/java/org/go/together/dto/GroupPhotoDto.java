package org.go.together.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.ComparingField;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class GroupPhotoDto implements ComparableDto {
    private UUID id;
    private UUID groupId;
    private PhotoCategory category;
    @ComparingField(value = "photos", idCompare = true)
    private Set<PhotoDto> photos;

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getGroupId() {
        return this.groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public PhotoCategory getCategory() {
        return this.category;
    }

    public void setCategory(PhotoCategory category) {
        this.category = category;
    }

    public Set<PhotoDto> getPhotos() {
        return this.photos;
    }

    public void setPhotos(Set<PhotoDto> photos) {
        this.photos = photos;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof GroupPhotoDto)) return false;
        final GroupPhotoDto other = (GroupPhotoDto) o;
        if (!other.canEqual(this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$groupId = this.getGroupId();
        final Object other$groupId = other.getGroupId();
        if (this$groupId == null ? other$groupId != null : !this$groupId.equals(other$groupId)) return false;
        final Object this$category = this.getCategory();
        final Object other$category = other.getCategory();
        if (this$category == null ? other$category != null : !this$category.equals(other$category)) return false;
        final Object this$photos = this.getPhotos();
        final Object other$photos = other.getPhotos();
        return this$photos == null ? other$photos == null : this$photos.equals(other$photos);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof GroupPhotoDto;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $groupId = this.getGroupId();
        result = result * PRIME + ($groupId == null ? 43 : $groupId.hashCode());
        final Object $category = this.getCategory();
        result = result * PRIME + ($category == null ? 43 : $category.hashCode());
        final Object $photos = this.getPhotos();
        result = result * PRIME + ($photos == null ? 43 : $photos.hashCode());
        return result;
    }

    public String toString() {
        return "GroupPhotoDto(id=" + this.getId() + ", groupId=" + this.getGroupId() + ", category=" + this.getCategory() + ", photos=" + this.getPhotos() + ")";
    }
}
