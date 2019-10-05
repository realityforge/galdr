package galdr;

import galdr.spy.EntityAddCompleteEvent;
import galdr.spy.EntityAddStartEvent;
import galdr.spy.EntityRemoveCompleteEvent;
import galdr.spy.EntityRemoveStartEvent;
import java.util.BitSet;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class EntityManagerTest
  extends AbstractTest
{
  private static class Armour
  {
  }

  private static class Health
  {
  }

  private static class Attack
  {
  }

  @Test
  public void isAlive()
  {
    final int initialEntityCount = 10;
    final EntityManager entityManager =
      Worlds.world().initialEntityCount( initialEntityCount ).build().getEntityManager();
    final Entity entity1 = entityManager.createEntity( set() );
    final Entity entity2 = entityManager.createEntity( set() );

    assertTrue( entityManager.isAlive( entity1.getId() ) );
    assertTrue( entityManager.isAlive( entity2.getId() ) );
    assertFalse( entityManager.isAlive( entity2.getId() + 1 ) );
    assertFalse( entityManager.isAlive( initialEntityCount ) );
    assertFalse( entityManager.isAlive( initialEntityCount + 1 ) );
  }

  @Test
  public void createEntity()
  {
    final int initialEntityCount = 5;
    final World world = Worlds.world()
      .initialEntityCount( initialEntityCount )
      .component( Armour.class, Armour::new )
      .component( Health.class, Health::new )
      .component( Attack.class, Attack::new )
      .build();

    final ComponentAPI<Armour> armour = world.getComponentByType( Armour.class );
    final ComponentAPI<Health> health = world.getComponentByType( Health.class );
    final ComponentAPI<Attack> attack = world.getComponentByType( Attack.class );

    final int armourId = armour.getId();
    final int healthId = health.getId();
    final int attackId = attack.getId();

    final EntityManager entityManager = world.getEntityManager();

    final BitSet componentIds1 = set();
    componentIds1.set( armourId );
    componentIds1.set( healthId );

    final BitSet componentIds2 = set();
    componentIds2.set( healthId );
    componentIds2.set( attackId );

    final Entity entity1 = entityManager.createEntity( componentIds1 );
    final Entity entity2 = entityManager.createEntity( componentIds2 );

    assertEquals( entityManager.getEntityById( entity1.getId() ), entity1 );
    assertEquals( entityManager.getEntityById( entity2.getId() ), entity2 );

    // entities should be marked as alive
    assertTrue( entity1.isAlive() );
    assertTrue( entity2.isAlive() );

    // We know ids are allocated sequentially
    assertEquals( entity1.getId(), 0 );
    assertEquals( entity2.getId(), 1 );

    // Component 1 components
    assertEquals( entity1.getComponentIds(), componentIds1 );
    run( world, () -> assertTrue( armour.has( entity1.getId() ) ) );
    run( world, () -> assertTrue( health.has( entity1.getId() ) ) );
    run( world, () -> assertFalse( attack.has( entity1.getId() ) ) );

    // Component 2 components
    assertEquals( entity2.getComponentIds(), componentIds2 );
    run( world, () -> assertFalse( armour.has( entity2.getId() ) ) );
    run( world, () -> assertTrue( health.has( entity2.getId() ) ) );
    run( world, () -> assertTrue( attack.has( entity2.getId() ) ) );
  }

  @Test
  public void createEntity_withSpyEventsEnabled()
  {
    final World world = Worlds.world()
      .initialEntityCount( 3 )
      .component( Armour.class, Armour::new )
      .component( Health.class, Health::new )
      .component( Attack.class, Attack::new )
      .build();

    final ComponentAPI<Armour> armour = world.getComponentByType( Armour.class );
    final ComponentAPI<Health> health = world.getComponentByType( Health.class );
    final ComponentAPI<Attack> attack = world.getComponentByType( Attack.class );

    final int armourId = armour.getId();
    final int healthId = health.getId();
    final int attackId = attack.getId();

    final EntityManager entityManager = world.getEntityManager();

    final BitSet componentIds1 = set();
    componentIds1.set( armourId );
    componentIds1.set( healthId );

    final BitSet componentIds2 = set();
    componentIds2.set( healthId );
    componentIds2.set( attackId );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( world );

    final Entity entity1 = entityManager.createEntity( componentIds1 );
    final Entity entity2 = entityManager.createEntity( componentIds2 );

    assertEquals( entityManager.getEntityById( entity1.getId() ), entity1 );
    assertEquals( entityManager.getEntityById( entity2.getId() ), entity2 );

    // entities should be marked as alive
    assertTrue( entity1.isAlive() );
    assertTrue( entity2.isAlive() );

    // We know ids are allocated sequentially
    assertEquals( entity1.getId(), 0 );
    assertEquals( entity2.getId(), 1 );

    // Component 1 components
    assertEquals( entity1.getComponentIds(), componentIds1 );
    run( world, () -> assertTrue( armour.has( entity1.getId() ) ) );
    run( world, () -> assertTrue( health.has( entity1.getId() ) ) );
    run( world, () -> assertFalse( attack.has( entity1.getId() ) ) );

    // Component 2 components
    assertEquals( entity2.getComponentIds(), componentIds2 );
    run( world, () -> assertFalse( armour.has( entity2.getId() ) ) );
    run( world, () -> assertTrue( health.has( entity2.getId() ) ) );
    run( world, () -> assertTrue( attack.has( entity2.getId() ) ) );

    handler.unsubscribe();

    handler.assertEventCount( 4 );
    handler.assertNextEvent( EntityAddStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getComponentIds(), componentIds1 );
    } );
    handler.assertNextEvent( EntityAddCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), entity1.getId() );
      assertEquals( e.getComponentIds(), componentIds1 );
    } );
    handler.assertNextEvent( EntityAddStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getComponentIds(), componentIds2 );
    } );
    handler.assertNextEvent( EntityAddCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), entity2.getId() );
      assertEquals( e.getComponentIds(), componentIds2 );
    } );
  }

  @Test
  public void disposeEntity()
  {
    final int initialEntityCount = 5;
    final World world = Worlds.world()
      .initialEntityCount( initialEntityCount )
      .component( Armour.class, Armour::new )
      .component( Health.class, Health::new )
      .build();

    final ComponentAPI<Armour> armour = world.getComponentByType( Armour.class );
    final ComponentAPI<Health> health = world.getComponentByType( Health.class );

    final int armourId = armour.getId();
    final int healthId = health.getId();

    final EntityManager entityManager = world.getEntityManager();

    final BitSet componentIds1 = set();
    componentIds1.set( armourId );
    componentIds1.set( healthId );

    final Entity entity = entityManager.createEntity( componentIds1 );
    final int entityId = entity.getId();

    assertTrue( entity.isAlive() );

    assertEquals( entity.getComponentIds(), componentIds1 );
    run( world, () -> assertTrue( armour.has( entityId ) ) );
    run( world, () -> assertTrue( health.has( entityId ) ) );

    run( world, () -> entityManager.disposeEntity( entityId ) );

    assertTrue( entity.getComponentIds().isEmpty() );
    assertFalse( entity.isAlive() );
  }

  @Test
  public void disposeEntity_WithSpyEnabled()
  {
    final int initialEntityCount = 5;
    final World world = Worlds.world()
      .initialEntityCount( initialEntityCount )
      .component( Armour.class, Armour::new )
      .component( Health.class, Health::new )
      .build();

    final ComponentAPI<Armour> armour = world.getComponentByType( Armour.class );
    final ComponentAPI<Health> health = world.getComponentByType( Health.class );

    final int armourId = armour.getId();
    final int healthId = health.getId();

    final EntityManager entityManager = world.getEntityManager();

    final BitSet componentIds1 = set();
    componentIds1.set( armourId );
    componentIds1.set( healthId );

    final Entity entity = entityManager.createEntity( componentIds1 );
    final int entityId = entity.getId();

    assertTrue( entity.isAlive() );

    assertEquals( entity.getComponentIds(), componentIds1 );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( world );

    run( world, () -> entityManager.disposeEntity( entityId ) );

    assertTrue( entity.getComponentIds().isEmpty() );
    assertFalse( entity.isAlive() );

    handler.assertEventCount( 2 );
    handler.assertNextEvent( EntityRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), entityId );
      assertEquals( e.getComponentIds(), componentIds1 );
    } );
    handler.assertNextEvent( EntityRemoveCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), entityId );
      assertEquals( e.getComponentIds(), componentIds1 );
    } );
  }

  @Test
  public void disposeEntity_disposeErrors()
  {
    final World world1 = Worlds.world().initialEntityCount( 4 ).build();
    final World world2 = Worlds.world().initialEntityCount( 4 ).build();

    final EntityManager entityManager = world1.getEntityManager();

    final int entityId = createEntity( world1, set() );

    // entity part of different world
    assertInvariantFailure( () -> run( world2, () -> entityManager.disposeEntity( entityId ) ),
                            "Galdr-0159: Attempting to dispose entity 0 which is not contained by the active world." );

    // No current world
    assertInvariantFailure( () -> entityManager.disposeEntity( entityId ),
                            "Galdr-0026: Invoked WorldHolder.world() when no world was active." );

    run( world1, () -> entityManager.disposeEntity( entityId ) );

    // In Free list
    assertInvariantFailure( () -> run( world1, () -> entityManager.disposeEntity( entityId ) ),
                            "Galdr-0009: Attempting to dispose entity 0 but entity is not allocated." );
    // Not yet allocated
    assertInvariantFailure( () -> run( world1, () -> entityManager.disposeEntity( entityId + 1 ) ),
                            "Galdr-0009: Attempting to dispose entity 1 but entity is not allocated." );
    // Past the end of the capacity
    assertInvariantFailure( () -> run( world1, () -> entityManager.disposeEntity( entityManager.capacity() + 1 ) ),
                            "Galdr-0009: Attempting to dispose entity 5 but entity is not allocated." );
  }

  @Test
  public void disposeEntity_entityNotAllocated()
  {
    final World world = Worlds.world().build();

    final EntityManager entityManager = world.getEntityManager();
    final Entity entity = entityManager.createEntity( set() );

    assertEquals( entityManager.getEntityById( entity.getId() ), entity );

    entity.clearAlive();

    assertInvariantFailure( () -> run( world, () -> entityManager.disposeEntity( entity.getId() ) ),
                            "Galdr-0059: Attempting to dispose entity 0 and entity is allocated but not alive." );
  }

  @Test
  public void verifyReuseOfFreedEntities()
  {
    final World world = Worlds.world().initialEntityCount( 4 ).build();

    final EntityManager entityManager = world.getEntityManager();

    assertEquals( entityManager.capacity(), 4 );

    final int entityId1 = createEntity( world, set() );
    final int entityId2 = createEntity( world, set() );
    final int entityId3 = createEntity( world, set() );
    final int entityId4 = createEntity( world, set() );

    assertEquals( entityManager.capacity(), 4 );

    run( world, () -> entityManager.disposeEntity( entityId1 ) );

    assertEquals( entityManager.capacity(), 4 );

    entityManager.createEntity( set() );

    assertEquals( entityManager.capacity(), 4 );

    run( world, () -> entityManager.disposeEntity( entityId2 ) );
    run( world, () -> entityManager.disposeEntity( entityId3 ) );
    run( world, () -> entityManager.disposeEntity( entityId4 ) );

    assertEquals( entityManager.capacity(), 4 );

    entityManager.createEntity( set() );
    entityManager.createEntity( set() );
    entityManager.createEntity( set() );

    assertEquals( entityManager.capacity(), 4 );
  }

  @Test
  public void capacityGrowsOnDemand()
  {
    final World world = Worlds.world().initialEntityCount( 2 ).build();

    final EntityManager entityManager = world.getEntityManager();

    assertEquals( entityManager.capacity(), 2 );

    final int entityId1 = createEntity( world, set() );
    final int entityId2 = createEntity( world, set() );

    assertEquals( entityManager.capacity(), 2 );

    final int entityId3 = createEntity( world, set() );
    final int entityId4 = createEntity( world, set() );

    assertEquals( entityManager.capacity(), 4 );

    run( world, () -> entityManager.disposeEntity( entityId1 ) );

    assertEquals( entityManager.capacity(), 4 );

    final int entityId5 = createEntity( world, set() );
    final int entityId6 = createEntity( world, set() );

    assertEquals( entityManager.capacity(), 8 );

    run( world, () -> entityManager.disposeEntity( entityId2 ) );
    run( world, () -> entityManager.disposeEntity( entityId3 ) );
    run( world, () -> entityManager.disposeEntity( entityId4 ) );

    assertEquals( entityManager.capacity(), 8 );

    final int entityId7 = createEntity( world, set() );
    final int entityId8 = createEntity( world, set() );
    final int entityId9 = createEntity( world, set() );

    assertEquals( entityManager.capacity(), 8 );

    final int entityId10 = createEntity( world, set() );
    final int entityId11 = createEntity( world, set() );
    final int entityId12 = createEntity( world, set() );

    assertEquals( entityManager.capacity(), 8 );

    final int entityId13 = createEntity( world, set() );

    assertEquals( entityManager.capacity(), 16 );

    assertEquals( entityId1, 0 );
    assertEquals( entityId2, 1 );
    assertEquals( entityId3, 2 );
    assertEquals( entityId4, 3 );
    assertEquals( entityId5, 0 );
    assertEquals( entityId6, 4 );
    assertEquals( entityId7, 1 );
    assertEquals( entityId8, 2 );
    assertEquals( entityId9, 3 );
    assertEquals( entityId10, 5 );
    assertEquals( entityId11, 6 );
    assertEquals( entityId12, 7 );
    assertEquals( entityId13, 8 );

    assertTrue( entityManager.isAlive( 0 ) );
    assertTrue( entityManager.isAlive( 1 ) );
    assertTrue( entityManager.isAlive( 2 ) );
    assertTrue( entityManager.isAlive( 3 ) );
    assertTrue( entityManager.isAlive( 4 ) );
    assertTrue( entityManager.isAlive( 5 ) );
    assertTrue( entityManager.isAlive( 6 ) );
    assertTrue( entityManager.isAlive( 7 ) );
    assertTrue( entityManager.isAlive( 8 ) );
  }

  @Test
  public void getEntityById_entityNotAlive()
  {
    final World world = Worlds.world().build();

    final EntityManager entityManager = world.getEntityManager();

    final Entity entity = entityManager.createEntity( set() );

    assertEquals( entityManager.getEntityById( entity.getId() ), entity );
    entity.clearAlive();
    assertInvariantFailure( () -> entityManager.getEntityById( entity.getId() ),
                            "Galdr-0078: Attempting to get entity 0 but entity is allocated but not alive." );
  }

  @Test
  public void getEntityById_entityNotAllocated()
  {
    final World world = Worlds.world().build();

    final EntityManager entityManager = world.getEntityManager();

    assertInvariantFailure( () -> entityManager.getEntityById( 37 ),
                            "Galdr-0079: Attempting to get entity 37 but entity is not allocated." );
  }

  @Test
  public void createEntity_badComponentId()
  {
    final World world = Worlds.world()
      .component( Armour.class, Armour::new )
      .component( Health.class, Health::new )
      .build();

    final EntityManager entityManager = world.getEntityManager();

    final BitSet componentIds1 = set();
    // invalid
    componentIds1.set( 33 );

    assertInvariantFailure( () -> entityManager.createEntity( componentIds1 ),
                            "Galdr-0006: Attempting to create entity with invalid componentId 33" );

    final BitSet componentIds2 = set();
    componentIds2.set( 0 );
    componentIds2.set( 1 );
    // 2 is invalid
    componentIds2.set( 2 );

    assertInvariantFailure( () -> entityManager.createEntity( componentIds2 ),
                            "Galdr-0006: Attempting to create entity with invalid componentId 2" );
  }
}
