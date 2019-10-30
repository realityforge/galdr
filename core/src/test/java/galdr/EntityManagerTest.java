package galdr;

import galdr.spy.EntityAddCompleteEvent;
import galdr.spy.EntityAddStartEvent;
import galdr.spy.EntityRemoveCompleteEvent;
import galdr.spy.EntityRemoveStartEvent;
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

  private static class Resistance
  {
  }

  @Test
  public void isAlive()
  {
    final int initialEntityCount = 10;
    final World world = Worlds.world().initialEntityCount( initialEntityCount ).build();
    final int entityId1 = createEntity( world );
    final int entityId2 = createEntity( world );

    run( world, () -> assertTrue( world.isAlive( entityId1 ) ) );
    run( world, () -> assertTrue( world.isAlive( entityId2 ) ) );
    run( world, () -> assertFalse( world.isAlive( entityId2 + 1 ) ) );
    run( world, () -> assertFalse( world.isAlive( initialEntityCount ) ) );
    run( world, () -> assertFalse( world.isAlive( initialEntityCount + 1 ) ) );
  }

  @Test
  public void createEntity()
  {
    final int initialEntityCount = 5;
    final World world = Worlds.world()
      .initialEntityCount( initialEntityCount )
      .component( Armour.class )
      .component( Health.class )
      .component( Attack.class )
      .build();

    final ComponentManager<Armour> armour = world.getComponentByType( Armour.class );
    final ComponentManager<Health> health = world.getComponentByType( Health.class );
    final ComponentManager<Attack> attack = world.getComponentByType( Attack.class );

    final int entityId1 = createEntity( world, Armour.class, Health.class );
    final int entityId2 = createEntity( world, Health.class, Attack.class );

    final EntityManager entityManager = world.getEntityManager();
    final Entity entity1 = entityManager.getEntityById( entityId1 );
    final Entity entity2 = entityManager.getEntityById( entityId2 );
    assertEquals( entity1.getId(), entityId1 );
    assertEquals( entity2.getId(), entityId2 );

    // entities should be marked as alive
    assertTrue( entity1.isAlive() );
    assertTrue( entity2.isAlive() );

    // We know ids are allocated sequentially
    assertEquals( entityId1, 0 );
    assertEquals( entityId2, 1 );

    // Component 1 components
    assertEquals( entity1.getComponentIds(), set( 0, 1 ) );
    run( world, () -> assertTrue( armour.has( entityId1 ) ) );
    run( world, () -> assertTrue( health.has( entityId1 ) ) );
    run( world, () -> assertFalse( attack.has( entityId1 ) ) );

    // Component 2 components
    assertEquals( entity2.getComponentIds(), set( 1, 2 ) );
    run( world, () -> assertFalse( armour.has( entityId2 ) ) );
    run( world, () -> assertTrue( health.has( entityId2 ) ) );
    run( world, () -> assertTrue( attack.has( entityId2 ) ) );
  }

  @Test
  public void createEntity_withSpyEventsEnabled()
  {
    final World world = Worlds.world()
      .initialEntityCount( 3 )
      .component( Armour.class )
      .component( Health.class )
      .component( Attack.class )
      .build();

    final ComponentManager<Armour> armour = world.getComponentByType( Armour.class );
    final ComponentManager<Health> health = world.getComponentByType( Health.class );
    final ComponentManager<Attack> attack = world.getComponentByType( Attack.class );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( world );

    final int entityId1 = createEntity( world, Armour.class, Health.class );
    final int entityId2 = createEntity( world, Health.class, Attack.class );

    final EntityManager entityManager = world.getEntityManager();
    final Entity entity1 = entityManager.getEntityById( entityId1 );
    final Entity entity2 = entityManager.getEntityById( entityId2 );
    assertEquals( entity1.getId(), entityId1 );
    assertEquals( entity2.getId(), entityId2 );

    // entities should be marked as alive
    assertTrue( entity1.isAlive() );
    assertTrue( entity2.isAlive() );

    // We know ids are allocated sequentially
    assertEquals( entityId1, 0 );
    assertEquals( entityId2, 1 );

    // Component 1 components
    assertEquals( entity1.getComponentIds(), set( 0, 1 ) );
    run( world, () -> assertTrue( armour.has( entityId1 ) ) );
    run( world, () -> assertTrue( health.has( entityId1 ) ) );
    run( world, () -> assertFalse( attack.has( entityId1 ) ) );

    // Component 2 components
    assertEquals( entity2.getComponentIds(), set( 1, 2 ) );
    run( world, () -> assertFalse( armour.has( entityId2 ) ) );
    run( world, () -> assertTrue( health.has( entityId2 ) ) );
    run( world, () -> assertTrue( attack.has( entityId2 ) ) );

    handler.unsubscribe();

    handler.assertEventCount( 4 );
    handler.assertNextEvent( EntityAddStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getComponentIds(), set( 0, 1 ) );
    } );
    handler.assertNextEvent( EntityAddCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), entityId1 );
      assertEquals( e.getComponentIds(), set( 0, 1 ) );
    } );
    handler.assertNextEvent( EntityAddStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getComponentIds(), set( 1, 2 ) );
    } );
    handler.assertNextEvent( EntityAddCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), entityId2 );
      assertEquals( e.getComponentIds(), set( 1, 2 ) );
    } );
  }

  @Test
  public void disposeEntity()
  {
    final World world = Worlds.world()
      .component( Armour.class )
      .component( Health.class )
      .component( Attack.class )
      .build();

    final ComponentManager<Armour> armour = world.getComponentByType( Armour.class );
    final ComponentManager<Health> health = world.getComponentByType( Health.class );

    final int entityId = createEntity( world, Armour.class, Health.class );

    final Entity entity = world.getEntityManager().getEntityById( entityId );
    assertTrue( entity.isAlive() );

    assertEquals( entity.getComponentIds(), set( 0, 1 ) );
    run( world, () -> assertTrue( armour.has( entityId ) ) );
    run( world, () -> assertTrue( health.has( entityId ) ) );

    run( world, () -> world.disposeEntity( entityId ) );

    assertTrue( entity.getComponentIds().isEmpty() );
    assertFalse( entity.isAlive() );
  }

  @Test
  public void disposeEntity_WithSpyEnabled()
  {
    final World world = Worlds.world()
      .component( Armour.class )
      .component( Health.class )
      .component( Attack.class )
      .build();

    final int entityId = createEntity( world, Armour.class, Health.class );

    final Entity entity = world.getEntityManager().getEntityById( entityId );
    assertTrue( entity.isAlive() );

    assertEquals( entity.getComponentIds(), set( 0, 1 ) );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( world );

    run( world, () -> world.disposeEntity( entityId ) );

    assertTrue( entity.getComponentIds().isEmpty() );
    assertFalse( entity.isAlive() );

    handler.assertEventCount( 2 );
    handler.assertNextEvent( EntityRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), entityId );
      assertEquals( e.getComponentIds(), set( 0, 1 ) );
    } );
    handler.assertNextEvent( EntityRemoveCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), entityId );
      assertEquals( e.getComponentIds(), set( 0, 1 ) );
    } );
  }

  @Test
  public void disposeEntity_disposeErrors()
  {
    final World world1 = Worlds.world().initialEntityCount( 4 ).build();
    final World world2 = Worlds.world().initialEntityCount( 4 ).build();

    final EntityManager entityManager = world1.getEntityManager();

    final int entityId = createEntity( world1 );

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

    final int entityId = createEntity( world );

    final Entity entity = world.getEntityManager().getEntityById( entityId );

    entity.clearAlive();

    assertInvariantFailure( () -> run( world, () -> world.disposeEntity( entityId ) ),
                            "Galdr-0059: Attempting to dispose entity 0 and entity is allocated but not alive." );
  }

  @Test
  public void verifyReuseOfFreedEntities()
  {
    final World world = Worlds.world().initialEntityCount( 4 ).build();

    final EntityManager entityManager = world.getEntityManager();

    assertEquals( entityManager.capacity(), 4 );

    final int entityId1 = createEntity( world );
    final int entityId2 = createEntity( world );
    final int entityId3 = createEntity( world );
    final int entityId4 = createEntity( world );

    assertEquals( entityManager.capacity(), 4 );

    run( world, () -> world.disposeEntity( entityId1 ) );

    assertEquals( entityManager.capacity(), 4 );

    createEntity( world );

    assertEquals( entityManager.capacity(), 4 );

    run( world, () -> world.disposeEntity( entityId2 ) );
    run( world, () -> world.disposeEntity( entityId3 ) );
    run( world, () -> world.disposeEntity( entityId4 ) );

    assertEquals( entityManager.capacity(), 4 );

    createEntity( world );
    createEntity( world );
    createEntity( world );

    assertEquals( entityManager.capacity(), 4 );
  }

  @Test
  public void capacityGrowsOnDemand()
  {
    final World world = Worlds.world().initialEntityCount( 2 ).build();

    final EntityManager entityManager = world.getEntityManager();

    assertEquals( entityManager.capacity(), 2 );

    final int entityId1 = createEntity( world );
    final int entityId2 = createEntity( world );

    assertEquals( entityManager.capacity(), 2 );

    final int entityId3 = createEntity( world );
    final int entityId4 = createEntity( world );

    assertEquals( entityManager.capacity(), 4 );

    run( world, () -> entityManager.disposeEntity( entityId1 ) );

    assertEquals( entityManager.capacity(), 4 );

    final int entityId5 = createEntity( world );
    final int entityId6 = createEntity( world );

    assertEquals( entityManager.capacity(), 8 );

    run( world, () -> entityManager.disposeEntity( entityId2 ) );
    run( world, () -> entityManager.disposeEntity( entityId3 ) );
    run( world, () -> entityManager.disposeEntity( entityId4 ) );

    assertEquals( entityManager.capacity(), 8 );

    final int entityId7 = createEntity( world );
    final int entityId8 = createEntity( world );
    final int entityId9 = createEntity( world );

    assertEquals( entityManager.capacity(), 8 );

    final int entityId10 = createEntity( world );
    final int entityId11 = createEntity( world );
    final int entityId12 = createEntity( world );

    assertEquals( entityManager.capacity(), 8 );

    final int entityId13 = createEntity( world );

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

    final int entityId = createEntity( world );
    final Entity entity = entityManager.getEntityById( entityId );

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
    final World world1 = Worlds.world()
      .component( Armour.class )
      .component( Health.class )
      .build();
    final World world2 = Worlds.world()
      .component( Armour.class )
      .component( Health.class )
      .component( Attack.class )
      .component( Resistance.class )
      .build();

    final ComponentIdSet componentIdSet1 = world2.createComponentIdSet( Resistance.class );
    final ComponentIdSet componentIdSet2 =
      world2.createComponentIdSet( Armour.class, Health.class, Attack.class );

    // invalid
    assertInvariantFailure( () -> run( world1, () -> world1.createEntity( componentIdSet1 ) ),
                            "Galdr-0006: Attempting to create entity with invalid componentId 3" );

    // 2 is invalid
    assertInvariantFailure( () -> run( world1, () -> world1.createEntity( componentIdSet2 ) ),
                            "Galdr-0006: Attempting to create entity with invalid componentId 2" );
  }
}
