package org.go.together.test.entities;

import org.go.together.interfaces.IdentifiedEntity;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class ManyJoinEntity implements IdentifiedEntity {
    @Id
    @Type(type = "uuid-char")
    private UUID id;
    private String name;
    private long number;

    ManyJoinEntity(UUID id, String name, long number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    public ManyJoinEntity() {
    }

    public static ManyJoinEntityBuilder builder() {
        return new ManyJoinEntityBuilder();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getNumber() {
        return number;
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

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ManyJoinEntity)) return false;
        final ManyJoinEntity other = (ManyJoinEntity) o;
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
        return other instanceof ManyJoinEntity;
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

    public static class ManyJoinEntityBuilder {
        private UUID id;
        private String name;
        private long number;

        ManyJoinEntityBuilder() {
        }

        public ManyJoinEntityBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public ManyJoinEntityBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ManyJoinEntityBuilder number(long number) {
            this.number = number;
            return this;
        }

        public ManyJoinEntity build() {
            return new ManyJoinEntity(id, name, number);
        }

        public String toString() {
            return "ManyJoinEntity.ManyJoinEntityBuilder(id=" + this.id + ", name=" + this.name + ", number=" + this.number + ")";
        }
    }
}
