package org.go.together.test.dto;

import org.go.together.interfaces.Dto;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class TestDto implements Dto {
    private UUID id;
    private String name;
    private long number;
    private Date date;
    private Set<String> elements;
    private Set<JoinTestDto> joinTestEntities;
    private Set<ManyJoinDto> manyJoinEntities;

    public TestDto() {
    }

    public UUID getId() {
        return this.id;
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

    public Set<String> getElements() {
        return this.elements;
    }

    public void setElements(Set<String> elements) {
        this.elements = elements;
    }

    public Set<JoinTestDto> getJoinTestEntities() {
        return this.joinTestEntities;
    }

    public void setJoinTestEntities(Set<JoinTestDto> joinTestEntities) {
        this.joinTestEntities = joinTestEntities;
    }

    public Set<ManyJoinDto> getManyJoinEntities() {
        return this.manyJoinEntities;
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
        final Object $elements = this.getElements();
        result = result * PRIME + ($elements == null ? 43 : $elements.hashCode());
        final Object $joinTestEntities = this.getJoinTestEntities();
        result = result * PRIME + ($joinTestEntities == null ? 43 : $joinTestEntities.hashCode());
        final Object $manyJoinEntities = this.getManyJoinEntities();
        result = result * PRIME + ($manyJoinEntities == null ? 43 : $manyJoinEntities.hashCode());
        return result;
    }

    public String toString() {
        return "TestDto(id=" + this.getId() + ", name=" + this.getName() + ", number=" + this.getNumber() + ", date=" + this.getDate() + ", elements=" + this.getElements() + ", joinTestEntities=" + this.getJoinTestEntities() + ", manyJoinEntities=" + this.getManyJoinEntities() + ")";
    }
}
