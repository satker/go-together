package org.go.together.test.dto;

import org.go.together.interfaces.ComparingField;
import org.go.together.interfaces.Dto;

import java.util.UUID;

public class ManyJoinDto implements Dto {
    private UUID id;

    @ComparingField(value = "name", isMain = true)
    private String name;

    @ComparingField("number")
    private long number;

    public ManyJoinDto() {
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

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ManyJoinDto)) return false;
        final ManyJoinDto other = (ManyJoinDto) o;
        if (!other.canEqual(this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        return this.getNumber() == other.getNumber();
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ManyJoinDto;
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
        return result;
    }

    public String toString() {
        return "ManyJoinDto(id=" + this.getId() + ", name=" + this.getName() + ", number=" + this.getNumber() + ")";
    }
}
