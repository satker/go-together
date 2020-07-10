/*
 * This file is generated by jOOQ.
 */
package org.go.together.model;

import lombok.Data;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Data
@Table(name = "interest", schema = "user_service")
public class Interest implements IdentifiedEntity {
    @Id
    private UUID id;
    private String name;
}
