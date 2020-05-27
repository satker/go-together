package org.go.together.test.entities;

import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class ManyJoinEntity implements IdentifiedEntity {
    @Id
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
