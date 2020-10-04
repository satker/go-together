package org.go.together.test.entities;

import org.go.together.repository.entities.NamedIdentifiedEntity;

import javax.persistence.Entity;
import java.util.UUID;

@Entity
public class JoinTestEntity extends NamedIdentifiedEntity {
    public JoinTestEntity() {
    }

    JoinTestEntity(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public static JoinTestEntityBuilder builder() {
        return new JoinTestEntityBuilder();
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof JoinTestEntity)) return false;
        final JoinTestEntity other = (JoinTestEntity) o;
        if (!other.canEqual(this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        return this$name == null ? other$name == null : this$name.equals(other$name);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof JoinTestEntity;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    public static class JoinTestEntityBuilder {
        private UUID id;
        private String name;

        JoinTestEntityBuilder() {
        }

        public JoinTestEntityBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public JoinTestEntityBuilder name(String name) {
            this.name = name;
            return this;
        }

        public JoinTestEntity build() {
            return new JoinTestEntity(id, name);
        }

        public String toString() {
            return "JoinTestEntity.JoinTestEntityBuilder(id=" + this.id + ", name=" + this.name + ")";
        }
    }
}
