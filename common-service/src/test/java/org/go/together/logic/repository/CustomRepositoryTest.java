package org.go.together.logic.repository;

import org.go.together.context.RepositoryContext;
import org.go.together.logic.repository.builder.SqlBuilder;
import org.go.together.logic.repository.utils.sql.SqlOperator;
import org.go.together.test.entities.JoinTestEntity;
import org.go.together.test.entities.ManyJoinEntity;
import org.go.together.test.entities.TestEntity;
import org.go.together.test.repository.TestRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import java.util.*;

import static org.junit.Assert.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = RepositoryContext.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomRepositoryTest {
    TestEntity testEntity;
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void init() {
        UUID id = UUID.randomUUID();
        Set<ManyJoinEntity> manyJoinEntities = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            ManyJoinEntity manyEntity = ManyJoinEntity.builder()
                    .id(UUID.randomUUID())
                    .name("many entity" + i)
                    .number(i)
                    .build();
            entityManager.merge(manyEntity);
            manyJoinEntities.add(manyEntity);
        }
        Set<JoinTestEntity> joinTestEntities = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            JoinTestEntity joinTestEntity = JoinTestEntity.builder()
                    .id(UUID.randomUUID())
                    .name("join entity" + i).build();
            entityManager.merge(joinTestEntity);
            joinTestEntities.add(joinTestEntity);
        }
        Set<UUID> elements = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            elements.add(UUID.randomUUID());
        }
        testEntity = TestEntity.builder().id(id)
                .name("name")
                .number(1)
                .date(new Date())
                .elements(elements)
                .manyJoinEntities(manyJoinEntities)
                .joinTestEntities(joinTestEntities)
                .build();
        testRepository.save(testEntity);
    }

    @AfterEach
    public void clean() {
        entityManager.clear();
    }

    @Test
    void delete() {
        testRepository.delete(testEntity);

        Optional<TestEntity> foundEntity = testRepository.findById(testEntity.getId());

        assertFalse(foundEntity.isPresent());
    }

    @Test
    void findAll() {
        Collection<TestEntity> testEntities = testRepository.findAll();

        assertEquals(1, testEntities.size());
    }

    @Test
    void createQueryWhereEqualsConditionsString() {
        String expectedSql = " FROM TestEntity te WHERE te.name = '" + testEntity.getName() + "'";
        SqlBuilder<TestEntity> sqlBuilder = testRepository.createQuery()
                .where(testRepository.createWhere().condition("name", SqlOperator.EQUAL, testEntity.getName()));

        Optional<TestEntity> entity = sqlBuilder.fetchOne();

        assertEquals(expectedSql, sqlBuilder.getQuery());
        assertTrue(entity.isPresent());
        assertEquals(testEntity.getName(), entity.get().getName());
    }

    @Test
    void createQueryWhereEqualsConditionsElementCollection() {
        UUID findElement = testEntity.getElements().iterator().next();

        String expectedSql = "select distinct te FROM TestEntity te left join te.elements te_elements WHERE te_elements = '"
                + findElement.toString() + "'";
        SqlBuilder<TestEntity> sqlBuilder = testRepository.createQuery()
                .where(testRepository.createWhere().condition("elements", SqlOperator.EQUAL, findElement));

        Optional<TestEntity> entity = sqlBuilder.fetchOne();

        assertEquals(expectedSql, sqlBuilder.getQuery());
        assertTrue(entity.isPresent());
        assertTrue(testEntity.getElements().stream().anyMatch(element -> element.equals(findElement)));
    }

    @Test
    void createQueryWhereEqualsConditionsOneToManyJoin() {
        String joinNameCondition = testEntity.getJoinTestEntities().iterator().next().getName();

        String expectedSql = "select distinct te FROM TestEntity te left join te.joinTestEntities te_joinTestEntities " +
                "WHERE te_joinTestEntities.name = '" + joinNameCondition + "'";

        SqlBuilder<TestEntity> sqlBuilder = testRepository.createQuery()
                .where(testRepository.createWhere().condition("joinTestEntities.name", SqlOperator.EQUAL, joinNameCondition));

        Optional<TestEntity> entity = sqlBuilder.fetchOne();

        assertEquals(expectedSql, sqlBuilder.getQuery());
        assertTrue(entity.isPresent());
        assertTrue(entity.get().getJoinTestEntities().stream()
                .anyMatch(joinTestEntity -> joinTestEntity.getName().equals(joinNameCondition)));
    }

    @Test
    void createQueryWhereEqualsConditionsManyToManyJoin() {
        String manyJoinNameCondition = testEntity.getManyJoinEntities().iterator().next().getName();

        String expectedSql = "select distinct te FROM TestEntity te left join te.manyJoinEntities te_manyJoinEntities " +
                "WHERE te_manyJoinEntities.name = '" + manyJoinNameCondition + "'";

        SqlBuilder<TestEntity> sqlBuilder = testRepository.createQuery()
                .where(testRepository.createWhere().condition("manyJoinEntities.name", SqlOperator.EQUAL, manyJoinNameCondition));

        Optional<TestEntity> entity = sqlBuilder.fetchOne();

        assertEquals(expectedSql, sqlBuilder.getQuery());
        assertTrue(entity.isPresent());
        assertTrue(entity.get().getManyJoinEntities().stream()
                .anyMatch(manyJoinEntity -> manyJoinEntity.getName().equals(manyJoinNameCondition)));
    }

    @Test
    void createQueryWhereEqualsConditionsWithAllJoins() {
        String manyJoinNameCondition = testEntity.getManyJoinEntities().iterator().next().getName();
        String joinNameCondition = testEntity.getJoinTestEntities().iterator().next().getName();
        UUID findElement = testEntity.getElements().iterator().next();

        String expectedSql = "select distinct te FROM TestEntity te left join te.manyJoinEntities te_manyJoinEntities " +
                "left join te.joinTestEntities te_joinTestEntities " +
                "left join te.elements te_elements " +
                "WHERE te_manyJoinEntities.name = '" + manyJoinNameCondition + "' " +
                "and te_joinTestEntities.name = '" + joinNameCondition + "' " +
                "and te_elements = '" + findElement + "'";

        SqlBuilder<TestEntity> sqlBuilder = testRepository.createQuery()
                .where(testRepository.createWhere()
                        .condition("manyJoinEntities.name", SqlOperator.EQUAL, manyJoinNameCondition)
                        .and()
                        .condition("joinTestEntities.name", SqlOperator.EQUAL, joinNameCondition)
                        .and()
                        .condition("elements", SqlOperator.EQUAL, findElement));

        Optional<TestEntity> entity = sqlBuilder.fetchOne();

        assertEquals(expectedSql, sqlBuilder.getQuery());
        assertTrue(entity.isPresent());
        assertTrue(entity.get().getManyJoinEntities().stream()
                .anyMatch(manyJoinEntity -> manyJoinEntity.getName().equals(manyJoinNameCondition)));
        assertTrue(entity.get().getJoinTestEntities().stream()
                .anyMatch(joinTestEntity -> joinTestEntity.getName().equals(joinNameCondition)));
        assertTrue(testEntity.getElements().stream().anyMatch(element -> element.equals(findElement)));
    }

    @Test
    void getEntityClass() {
        Class<TestEntity> entityClass = testRepository.getEntityClass();

        assertEquals(TestEntity.class, entityClass);
    }
}