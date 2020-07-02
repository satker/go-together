package org.go.together.test.dto;

import org.go.together.dto.SimpleDto;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.ComparingField;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class TestDto implements ComparableDto {
    private UUID id;

    @ComparingField(value = "name", isMain = true)
    private String name;

    @ComparingField("number")
    private Long number;

    @ComparingField(value = "date", deepCompare = false)
    private Date date;

    @ComparingField("start date")
    private Date startDate;

    @ComparingField("end date")
    private Date endDate;

    @ComparingField("start number")
    private Long startNumber;

    @ComparingField("end number")
    private Long endNumber;

    @ComparingField("latitude")
    private Double latitude;

    @ComparingField("longitude")
    private Double longitude;

    @ComparingField("simple dto")
    private SimpleDto simpleDto;

    private Set<String> elements;

    @ComparingField("join test entities")
    private Set<JoinTestDto> joinTestEntities;

    @ComparingField("many join entities")
    private Set<ManyJoinDto> manyJoinEntities;

    public TestDto() {
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public long getNumber() {
        return this.number;
    }

    public Date getDate() {
        return this.date;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public long getStartNumber() {
        return this.startNumber;
    }

    public void setStartNumber(long startNumber) {
        this.startNumber = startNumber;
    }

    public long getEndNumber() {
        return this.endNumber;
    }

    public Set<String> getElements() {
        return this.elements;
    }

    public Set<JoinTestDto> getJoinTestEntities() {
        return this.joinTestEntities;
    }

    public Set<ManyJoinDto> getManyJoinEntities() {
        return this.manyJoinEntities;
    }

    public void setEndNumber(long endNumber) {
        this.endNumber = endNumber;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public SimpleDto getSimpleDto() {
        return this.simpleDto;
    }

    public void setSimpleDto(SimpleDto simpleDto) {
        this.simpleDto = simpleDto;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setElements(Set<String> elements) {
        this.elements = elements;
    }

    public void setJoinTestEntities(Set<JoinTestDto> joinTestEntities) {
        this.joinTestEntities = joinTestEntities;
    }

    public void setManyJoinEntities(Set<ManyJoinDto> manyJoinEntities) {
        this.manyJoinEntities = manyJoinEntities;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof TestDto)) return false;
        final TestDto other = (TestDto) o;
        if (!other.canEqual(this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        if (this.getNumber() != other.getNumber()) return false;
        final Object this$date = this.getDate();
        final Object other$date = other.getDate();
        if (this$date == null ? other$date != null : !this$date.equals(other$date)) return false;
        final Object this$startDate = this.getStartDate();
        final Object other$startDate = other.getStartDate();
        if (this$startDate == null ? other$startDate != null : !this$startDate.equals(other$startDate)) return false;
        final Object this$endDate = this.getEndDate();
        final Object other$endDate = other.getEndDate();
        if (this$endDate == null ? other$endDate != null : !this$endDate.equals(other$endDate)) return false;
        if (this.getStartNumber() != other.getStartNumber()) return false;
        if (this.getEndNumber() != other.getEndNumber()) return false;
        if (Double.compare(this.getLatitude(), other.getLatitude()) != 0) return false;
        if (Double.compare(this.getLongitude(), other.getLongitude()) != 0) return false;
        final Object this$simpleDto = this.getSimpleDto();
        final Object other$simpleDto = other.getSimpleDto();
        if (this$simpleDto == null ? other$simpleDto != null : !this$simpleDto.equals(other$simpleDto)) return false;
        final Object this$elements = this.getElements();
        final Object other$elements = other.getElements();
        if (this$elements == null ? other$elements != null : !this$elements.equals(other$elements)) return false;
        final Object this$joinTestEntities = this.getJoinTestEntities();
        final Object other$joinTestEntities = other.getJoinTestEntities();
        if (this$joinTestEntities == null ? other$joinTestEntities != null : !this$joinTestEntities.equals(other$joinTestEntities))
            return false;
        final Object this$manyJoinEntities = this.getManyJoinEntities();
        final Object other$manyJoinEntities = other.getManyJoinEntities();
        return this$manyJoinEntities == null ? other$manyJoinEntities == null : this$manyJoinEntities.equals(other$manyJoinEntities);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof TestDto;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final long $number = this.getNumber();
        result = result * PRIME + (int) ($number >>> 32 ^ $number);
        final Object $date = this.getDate();
        result = result * PRIME + ($date == null ? 43 : $date.hashCode());
        final Object $startDate = this.getStartDate();
        result = result * PRIME + ($startDate == null ? 43 : $startDate.hashCode());
        final Object $endDate = this.getEndDate();
        result = result * PRIME + ($endDate == null ? 43 : $endDate.hashCode());
        final long $startNumber = this.getStartNumber();
        result = result * PRIME + (int) ($startNumber >>> 32 ^ $startNumber);
        final long $endNumber = this.getEndNumber();
        result = result * PRIME + (int) ($endNumber >>> 32 ^ $endNumber);
        final long $latitude = Double.doubleToLongBits(this.getLatitude());
        result = result * PRIME + (int) ($latitude >>> 32 ^ $latitude);
        final long $longitude = Double.doubleToLongBits(this.getLongitude());
        result = result * PRIME + (int) ($longitude >>> 32 ^ $longitude);
        final Object $simpleDto = this.getSimpleDto();
        result = result * PRIME + ($simpleDto == null ? 43 : $simpleDto.hashCode());
        final Object $elements = this.getElements();
        result = result * PRIME + ($elements == null ? 43 : $elements.hashCode());
        final Object $joinTestEntities = this.getJoinTestEntities();
        result = result * PRIME + ($joinTestEntities == null ? 43 : $joinTestEntities.hashCode());
        final Object $manyJoinEntities = this.getManyJoinEntities();
        result = result * PRIME + ($manyJoinEntities == null ? 43 : $manyJoinEntities.hashCode());
        return result;
    }

    public String toString() {
        return "TestDto(id=" + this.getId() + ", name=" + this.getName() + ", number=" + this.getNumber() + ", date=" + this.getDate() + ", startDate=" + this.getStartDate() + ", endDate=" + this.getEndDate() + ", startNumber=" + this.getStartNumber() + ", endNumber=" + this.getEndNumber() + ", latitude=" + this.getLatitude() + ", longitude=" + this.getLongitude() + ", simpleDto=" + this.getSimpleDto() + ", elements=" + this.getElements() + ", joinTestEntities=" + this.getJoinTestEntities() + ", manyJoinEntities=" + this.getManyJoinEntities() + ")";
    }

    @Override
    public UUID getOwnerId() {
        return getId();
    }
}
