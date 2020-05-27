package org.go.together.test.entities;

import org.go.together.interfaces.IdentifiedEntity;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
public class TestEntity implements IdentifiedEntity {
    @Id
    private UUID id;
    private String name;
    private long number;
    private Date date;

    @ElementCollection
    @CollectionTable(name = "test_elements", joinColumns = @JoinColumn(name = "test_id"))
    @Column(name = "element_id")
    @Type(type = "uuid-char")
    private Set<UUID> elements;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "test_id")
    private Set<JoinTestEntity> joinTestEntities;

    @ManyToMany
    @JoinTable(name = "test_many_join",
            joinColumns = @JoinColumn(name = "test_id"),
            inverseJoinColumns = @JoinColumn(name = "many_join_id"))
    private Set<ManyJoinEntity> manyJoinEntities = new HashSet<>();

    public TestEntity() {
    }

    TestEntity(UUID id, String name, long number, Date date, Set<UUID> elements, Set<JoinTestEntity> joinTestEntities, Set<ManyJoinEntity> manyJoinEntities) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.date = date;
        this.elements = elements;
        this.joinTestEntities = joinTestEntities;
        this.manyJoinEntities = manyJoinEntities;
    }

    public static TestEntityBuilder builder() {
        return new TestEntityBuilder();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getNumber() {
        return this.number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Set<UUID> getElements() {
        return this.elements;
    }

    public void setElements(Set<UUID> elements) {
        this.elements = elements;
    }

    public Set<JoinTestEntity> getJoinTestEntities() {
        return this.joinTestEntities;
    }

    public void setJoinTestEntities(Set<JoinTestEntity> joinTestEntities) {
        this.joinTestEntities = joinTestEntities;
    }

    public Set<ManyJoinEntity> getManyJoinEntities() {
        return this.manyJoinEntities;
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
        final Object $elements = this.getElements();
        result = result * PRIME + ($elements == null ? 43 : $elements.hashCode());
        final Object $joinTestEntities = this.getJoinTestEntities();
        result = result * PRIME + ($joinTestEntities == null ? 43 : $joinTestEntities.hashCode());
        final Object $manyJoinEntities = this.getManyJoinEntities();
        result = result * PRIME + ($manyJoinEntities == null ? 43 : $manyJoinEntities.hashCode());
        return result;
    }

    public String toString() {
        return "TestEntity(id=" + this.getId() + ", name=" + this.getName() + ", number=" + this.getNumber() + ", date=" + this.getDate() + ", elements=" + this.getElements() + ", joinTestEntities=" + this.getJoinTestEntities() + ", manyJoinEntities=" + this.getManyJoinEntities() + ")";
    }

    public static class TestEntityBuilder {
        private UUID id;
        private String name;
        private long number;
        private Date date;
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
            return new TestEntity(id, name, number, date, elements, joinTestEntities, manyJoinEntities);
        }

        public String toString() {
            return "TestEntity.TestEntityBuilder(id=" + this.id + ", name=" + this.name + ", number=" + this.number + ", date=" + this.date + ", elements=" + this.elements + ", joinTestEntities=" + this.joinTestEntities + ", manyJoinEntities=" + this.manyJoinEntities + ")";
        }
    }
}
