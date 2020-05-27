package org.go.together.test.entities;

import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class JoinTestEntity implements IdentifiedEntity {
    @Id
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
