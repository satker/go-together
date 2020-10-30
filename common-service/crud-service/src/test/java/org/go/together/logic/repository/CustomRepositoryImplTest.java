package org.go.together.logic.repository;

import org.go.together.context.RepositoryContext;
import org.go.together.repository.builder.Sql;
import org.go.together.repository.entities.Direction;
import org.go.together.repository.sql.SqlOperator;
import org.go.together.test.entities.JoinTestEntity;
import org.go.together.test.entities.ManyJoinEntity;
import org.go.together.test.entities.TestEntity;
import org.go.together.test.repository.impl.TestRepositoryImpl;
import org.go.together.test.repository.interfaces.ManyJoinRepository;
import org.go.together.test.repository.interfaces.TestRepository;
import org.go.together.utils.ReflectionUtils;
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
class CustomRepositoryImplTest {
    TestEntity testEntity;
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ManyJoinRepository manyJoinRepository;

    @BeforeEach
    public void init() {
        for (int i = 0; i < 10; i++) {
            getTestEntity(UUID.randomUUID(),
                    "many entity" + i,
                    "join entity" + i,
                    "name" + i,
                    i);
            testRepository.save(testEntity);
        }
    }

    private void getTestEntity(UUID id, String manyEntityName, String joinEntityName, String testName, int testNumber) {
        Set<ManyJoinEntity> manyJoinEntities = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            ManyJoinEntity manyEntity = new ManyJoinEntity();
            manyEntity.setId(UUID.randomUUID());
            manyEntity.setNumber(i);
            manyEntity.setName(manyEntityName + i);
            manyJoinRepository.save(manyEntity);
            manyJoinEntities.add(manyEntity);
        }
        Set<JoinTestEntity> joinTestEntities = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            JoinTestEntity joinTestEntity = new JoinTestEntity();
            joinTestEntity.setId(UUID.randomUUID());
            joinTestEntity.setName(joinEntityName + i);
            entityManager.merge(joinTestEntity);
            joinTestEntities.add(joinTestEntity);
        }
        Set<UUID> elements = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            elements.add(UUID.randomUUID());
        }
        this.testEntity = new TestEntity();
        this.testEntity.setId(id);
        this.testEntity.setNumber(testNumber);
        this.testEntity.setName(testName);
        this.testEntity.setDate(new Date());
        this.testEntity.setElements(elements);
        this.testEntity.setManyJoinEntities(manyJoinEntities);
        this.testEntity.setJoinTestEntities(joinTestEntities);
    }

    @AfterEach
    public void clean() {
        entityManager.clear();
    }

    @Test
    void delete() {
        testRepository.delete(testEntity);

        Optional<TestEntity> foundEntity = testRepository.findById(testEntity.getId());

        assertFalse(foundEntity.isEmpty());
    }

    @Test
    void findAll() {
        Collection<TestEntity> testEntities = testRepository.findAll();

        assertEquals(10, testEntities.size());
    }

    @Test
    void createQueryWhereEqualsConditionsString() {
        String expectedSql = " FROM TestEntity te WHERE te.name = '" + testEntity.getName() + "'";
        Sql<TestEntity> sql = testRepository.createQuery()
                .where(testRepository.createWhere().condition("name", SqlOperator.EQUAL, testEntity.getName())).build();

        Optional<TestEntity> entity = sql.fetchOne();

        assertEquals(expectedSql, sql.getQuery());
        assertTrue(entity.isPresent());
        assertEquals(testEntity.getName(), entity.get().getName());
    }

    @Test
    void createQueryWhereEqualsConditionsElementCollection() {
        UUID findElement = testEntity.getElements().iterator().next();

        String expectedSql = "select distinct te FROM TestEntity te left join te.elements te_elements WHERE te_elements = '"
                + findElement.toString() + "'";
        Sql<TestEntity> sql = testRepository.createQuery()
                .where(testRepository.createWhere().condition("elements", SqlOperator.EQUAL, findElement)).build();

        Optional<TestEntity> entity = sql.fetchOne();

        assertEquals(expectedSql, sql.getQuery());
        assertTrue(entity.isPresent());
        assertTrue(testEntity.getElements().stream().anyMatch(element -> element.equals(findElement)));
    }

    @Test
    void createQueryWhereEqualsConditionsOneToManyJoin() {
        String joinNameCondition = testEntity.getJoinTestEntities().iterator().next().getName();

        String expectedSql = "select distinct te FROM TestEntity te left join te.joinTestEntities te_joinTestEntities " +
                "WHERE te_joinTestEntities.name = '" + joinNameCondition + "'";

        Sql<TestEntity> sql = testRepository.createQuery()
                .where(testRepository.createWhere().condition("joinTestEntities.name", SqlOperator.EQUAL, joinNameCondition))
                .build();

        Optional<TestEntity> entity = sql.fetchOne();

        assertEquals(expectedSql, sql.getQuery());
        assertTrue(entity.isPresent());
        assertTrue(entity.get().getJoinTestEntities().stream()
                .anyMatch(joinTestEntity -> joinTestEntity.getName().equals(joinNameCondition)));
    }

    @Test
    void createQueryWhereEqualsConditionsOneToManyJoinAndSort() {
        String expectedSql = " FROM ManyJoinEntity mje ORDER BY mje.number ASC";

        Sql<ManyJoinEntity> sql = manyJoinRepository.createQuery()
                .sort("number", Direction.DESC)
                .sort("number", Direction.ASC)
                .build();

        Collection<ManyJoinEntity> entities = sql.fetchAll();

        assertEquals(expectedSql, sql.getQuery());
        assertFalse(entities.isEmpty());
        assertEquals(100, entities.size());
    }

    @Test
    void createQueryMultpleResultsAndSort() {
        String expectedSql = "select distinct te FROM TestEntity te left join fetch te.joinTestEntities te_joinTestEntities left join te.manyJoinEntities te_manyJoinEntities " +
                "WHERE te_joinTestEntities.name like '%join%' and te_manyJoinEntities.name like '%many%' ORDER BY te.name DESC, te.date ASC, te_joinTestEntities.name DESC";

        Sql<TestEntity> sql = testRepository.createQuery()
                .where(testRepository.createWhere().condition("joinTestEntities.name", SqlOperator.LIKE, "join"))
                .where(testRepository.createWhere().condition("manyJoinEntities.name", SqlOperator.LIKE, "many"))
                .sort("date", Direction.ASC)
                .sort("name", Direction.DESC)
                .sort("joinTestEntities.name", Direction.DESC)
                .build();

        Collection<TestEntity> entities = sql.fetchAll();

        assertEquals(expectedSql, sql.getQuery());
        assertFalse(entities.isEmpty());
        assertEquals(10, entities.size());
    }

    @Test
    void createQueryWhereEqualsConditionsManyToManyJoin() {
        String manyJoinNameCondition = testEntity.getManyJoinEntities().iterator().next().getName();

        String expectedSql = "select distinct te FROM TestEntity te left join te.manyJoinEntities te_manyJoinEntities " +
                "WHERE te_manyJoinEntities.name = '" + manyJoinNameCondition + "'";

        Sql<TestEntity> sql = testRepository.createQuery()
                .where(testRepository.createWhere()
                        .condition("manyJoinEntities.name", SqlOperator.EQUAL, manyJoinNameCondition))
                .build();

        Optional<TestEntity> entity = sql.fetchOne();

        assertEquals(expectedSql, sql.getQuery());
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

        Sql<TestEntity> sql = testRepository.createQuery()
                .where(testRepository.createWhere()
                        .condition("manyJoinEntities.name", SqlOperator.EQUAL, manyJoinNameCondition)
                        .and()
                        .condition("joinTestEntities.name", SqlOperator.EQUAL, joinNameCondition)
                        .and()
                        .condition("elements", SqlOperator.EQUAL, findElement))
                .build();

        Optional<TestEntity> entity = sql.fetchOne();

        assertEquals(expectedSql, sql.getQuery());
        assertTrue(entity.isPresent());
        assertTrue(entity.get().getManyJoinEntities().stream()
                .anyMatch(manyJoinEntity -> manyJoinEntity.getName().equals(manyJoinNameCondition)));
        assertTrue(entity.get().getJoinTestEntities().stream()
                .anyMatch(joinTestEntity -> joinTestEntity.getName().equals(joinNameCondition)));
        assertTrue(testEntity.getElements().stream().anyMatch(element -> element.equals(findElement)));
    }

    @Test
    void getEntityClass() {
        Class<TestEntity> entityClass = ReflectionUtils.getParametrizedClass(TestRepositoryImpl.class, 0);

        assertEquals(TestEntity.class, entityClass);
    }
}