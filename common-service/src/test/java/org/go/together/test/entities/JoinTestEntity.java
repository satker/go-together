package org.go.together.test.entities;

import org.go.together.interfaces.IdentifiedEntity;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class JoinTestEntity implements IdentifiedEntity {
    @Id
    @Type(type = "uuid-char")
    private UUID id;
    private String name;

    public JoinTestEntity() {
    }

    JoinTestEntity(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public static JoinTestEntityBuilder builder() {
        return new JoinTestEntityBuilder();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
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
