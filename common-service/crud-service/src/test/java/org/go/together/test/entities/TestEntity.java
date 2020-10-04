package org.go.together.test.entities;

import org.go.together.repository.entities.NamedIdentifiedEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Entity
public class TestEntity extends NamedIdentifiedEntity {
    private long number;
    private Date date;
    private Date startDate;
    private Date endDate;
    private long startNumber;
    private long endNumber;
    private double latitude;
    private double longitude;
    private String simpleDto;

    @ElementCollection
    @CollectionTable(name = "test_elements", joinColumns = @JoinColumn(name = "test_id"))
    @Column(name = "element_id", columnDefinition = "uuid")
    private Set<UUID> elements;

    @OneToMany(cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "test_id")
    private Set<JoinTestEntity> joinTestEntities;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "test_many_join",
            joinColumns = @JoinColumn(name = "test_id"),
            inverseJoinColumns = @JoinColumn(name = "many_join_id"))
    private Set<ManyJoinEntity> manyJoinEntities;

    public TestEntity() {

    }

    TestEntity(UUID id, String name, long number, Date date, Date startDate, Date endDate, long startNumber, long endNumber, double latitude, double longitude, String simpleDto, Set<UUID> elements, Set<JoinTestEntity> joinTestEntities, Set<ManyJoinEntity> manyJoinEntities) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.date = date;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startNumber = startNumber;
        this.endNumber = endNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.simpleDto = simpleDto;
        this.elements = elements;
        this.joinTestEntities = joinTestEntities;
        this.manyJoinEntities = manyJoinEntities;
    }

    public static TestEntityBuilder builder() {
        return new TestEntityBuilder();
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

    public void setNumber(long number) {
        this.number = number;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getSimpleDto() {
        return this.simpleDto;
    }

    public void setSimpleDto(String simpleDto) {
        this.simpleDto = simpleDto;
    }

    public Set<UUID> getElements() {
        return this.elements;
    }

    public Set<JoinTestEntity> getJoinTestEntities() {
        return this.joinTestEntities;
    }

    public Set<ManyJoinEntity> getManyJoinEntities() {
        return this.manyJoinEntities;
    }

    public void setElements(Set<UUID> elements) {
        this.elements = elements;
    }

    public void setJoinTestEntities(Set<JoinTestEntity> joinTestEntities) {
        this.joinTestEntities = joinTestEntities;
    }

    public void setManyJoinEntities(Set<ManyJoinEntity> manyJoinEntities) {
        this.manyJoinEntities = manyJoinEntities;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof TestEntity)) return false;
        final TestEntity other = (TestEntity) o;
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
        return other instanceof TestEntity;
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
        return "TestEntity(id=" + this.getId() + ", name=" + this.getName() + ", number=" + this.getNumber() + ", date=" + this.getDate() + ", startDate=" + this.getStartDate() + ", endDate=" + this.getEndDate() + ", startNumber=" + this.getStartNumber() + ", endNumber=" + this.getEndNumber() + ", latitude=" + this.getLatitude() + ", longitude=" + this.getLongitude() + ", simpleDto=" + this.getSimpleDto() + ", elements=" + this.getElements() + ", joinTestEntities=" + this.getJoinTestEntities() + ", manyJoinEntities=" + this.getManyJoinEntities() + ")";
    }

    public static class TestEntityBuilder {
        private UUID id;
        private String name;
        private long number;
        private Date date;
        private Date startDate;
        private Date endDate;
        private long startNumber;
        private long endNumber;
        private double latitude;
        private double longitude;
        private String simpleDto;
        private Set<UUID> elements;
        private Set<JoinTestEntity> joinTestEntities;
        private Set<ManyJoinEntity> manyJoinEntities;

        TestEntityBuilder() {
        }

        public TestEntityBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public TestEntityBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TestEntityBuilder number(long number) {
            this.number = number;
            return this;
        }

        public TestEntityBuilder date(Date date) {
            this.date = date;
            return this;
        }

        public TestEntityBuilder startDate(Date startDate) {
            this.startDate = startDate;
            return this;
        }

        public TestEntityBuilder endDate(Date endDate) {
            this.endDate = endDate;
            return this;
        }

        public TestEntityBuilder startNumber(long startNumber) {
            this.startNumber = startNumber;
            return this;
        }

        public TestEntityBuilder endNumber(long endNumber) {
            this.endNumber = endNumber;
            return this;
        }

        public TestEntityBuilder latitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public TestEntityBuilder longitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        public TestEntityBuilder simpleDto(String simpleDto) {
            this.simpleDto = simpleDto;
            return this;
        }

        public TestEntityBuilder elements(Set<UUID> elements) {
            this.elements = elements;
            return this;
        }

        public TestEntityBuilder joinTestEntities(Set<JoinTestEntity> joinTestEntities) {
            this.joinTestEntities = joinTestEntities;
            return this;
        }

        public TestEntityBuilder manyJoinEntities(Set<ManyJoinEntity> manyJoinEntities) {
            this.manyJoinEntities = manyJoinEntities;
            return this;
        }

        public TestEntity build() {
            return new TestEntity(id, name, number, date, startDate, endDate, startNumber, endNumber, latitude, longitude, simpleDto, elements, joinTestEntities, manyJoinEntities);
        }

        public String toString() {
            return "TestEntity.TestEntityBuilder(id=" + this.id + ", name=" + this.name + ", number=" + this.number + ", date=" + this.date + ", startDate=" + this.startDate + ", endDate=" + this.endDate + ", startNumber=" + this.startNumber + ", endNumber=" + this.endNumber + ", latitude=" + this.latitude + ", longitude=" + this.longitude + ", simpleDto=" + this.simpleDto + ", elements=" + this.elements + ", joinTestEntities=" + this.joinTestEntities + ", manyJoinEntities=" + this.manyJoinEntities + ")";
        }
    }
}
