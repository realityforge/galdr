package galdr;

import galdr.spy.EntityPostAddEvent;
import galdr.spy.EntityPreRemoveEvent;
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
      Galdr.world().initialEntityCount( initialEntityCount ).build().getEntityManager();
    final Entity entity1 = entityManager.createEntity( new BitSet() );
    final Entity entity2 = entityManager.createEntity( new BitSet() );

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
    final World world = Galdr.world()
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

    final BitSet componentIds1 = new BitSet();
    componentIds1.set( armourId );
    componentIds1.set( healthId );

    final BitSet componentIds2 = new BitSet();
    componentIds2.set( healthId );
    componentIds2.set( attackId );

    final Entity entity1 = entityManager.createEntity( componentIds1 );
    final Entity entity2 = entityManager.createEntity( componentIds2 );

    // entities should be marked as alive
    assertTrue( entity1.isAlive() );
    assertTrue( entity2.isAlive() );

    // We know ids are allocated sequentially
    assertEquals( entity1.getId(), 0 );
    assertEquals( entity2.getId(), 1 );

    // Component 1 components
    assertEquals( entity1.getComponentIds(), componentIds1 );
    assertTrue( armour.has( entity1.getId() ) );
    assertTrue( health.has( entity1.getId() ) );
    assertFalse( attack.has( entity1.getId() ) );

    // Component 2 components
    assertEquals( entity2.getComponentIds(), componentIds2 );
    assertFalse( armour.has( entity2.getId() ) );
    assertTrue( health.has( entity2.getId() ) );
    assertTrue( attack.has( entity2.getId() ) );
  }

  @Test
  public void createEntity_withSpyEventsEnabled()
  {
    final World world = Galdr.world()
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

    final BitSet componentIds1 = new BitSet();
    componentIds1.set( armourId );
    componentIds1.set( healthId );

    final BitSet componentIds2 = new BitSet();
    componentIds2.set( healthId );
    componentIds2.set( attackId );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( world );

    final Entity entity1 = entityManager.createEntity( componentIds1 );
    final Entity entity2 = entityManager.createEntity( componentIds2 );

    // entities should be marked as alive
    assertTrue( entity1.isAlive() );
    assertTrue( entity2.isAlive() );

    // We know ids are allocated sequentially
    assertEquals( entity1.getId(), 0 );
    assertEquals( entity2.getId(), 1 );

    // Component 1 components
    assertEquals( entity1.getComponentIds(), componentIds1 );
    assertTrue( armour.has( entity1.getId() ) );
    assertTrue( health.has( entity1.getId() ) );
    assertFalse( attack.has( entity1.getId() ) );

    // Component 2 components
    assertEquals( entity2.getComponentIds(), componentIds2 );
    assertFalse( armour.has( entity2.getId() ) );
    assertTrue( health.has( entity2.getId() ) );
    assertTrue( attack.has( entity2.getId() ) );

    handler.unsubscribe();

    handler.assertEventCount( 2 );
    handler.assertNextEvent( EntityPostAddEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), entity1.getId() );
      assertTrue( entity1.isAlive() );

    } );
    handler.assertNextEvent( EntityPostAddEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), entity2.getId() );
      assertTrue( entity2.isAlive() );
    } );
  }

  @Test
  public void disposeEntity()
  {
    final int initialEntityCount = 5;
    final World world = Galdr.world()
      .initialEntityCount( initialEntityCount )
      .component( Armour.class, Armour::new )
      .component( Health.class, Health::new )
      .build();

    final ComponentAPI<Armour> armour = world.getComponentByType( Armour.class );
    final ComponentAPI<Health> health = world.getComponentByType( Health.class );

    final int armourId = armour.getId();
    final int healthId = health.getId();

    final EntityManager entityManager = world.getEntityManager();

    final BitSet componentIds1 = new BitSet();
    componentIds1.set( armourId );
    componentIds1.set( healthId );

    final Entity entity = entityManager.createEntity( componentIds1 );
    final int entityId = entity.getId();

    assertTrue( entity.isAlive() );

    assertEquals( entity.getComponentIds(), componentIds1 );
    assertTrue( armour.has( entityId ) );
    assertTrue( health.has( entityId ) );

    entityManager.disposeEntity( entityId );

    assertTrue( entity.getComponentIds().isEmpty() );
    assertFalse( entity.isAlive() );
  }

  @Test
  public void disposeEntity_WithSpyEnabled()
  {
    final int initialEntityCount = 5;
    final World world = Galdr.world()
      .initialEntityCount( initialEntityCount )
      .component( Armour.class, Armour::new )
      .component( Health.class, Health::new )
      .build();

    final ComponentAPI<Armour> armour = world.getComponentByType( Armour.class );
    final ComponentAPI<Health> health = world.getComponentByType( Health.class );

    final int armourId = armour.getId();
    final int healthId = health.getId();

    final EntityManager entityManager = world.getEntityManager();

    final BitSet componentIds1 = new BitSet();
    componentIds1.set( armourId );
    componentIds1.set( healthId );

    final Entity entity = entityManager.createEntity( componentIds1 );
    final int entityId = entity.getId();

    assertTrue( entity.isAlive() );

    assertEquals( entity.getComponentIds(), componentIds1 );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( world );

    entityManager.disposeEntity( entityId );

    assertTrue( entity.getComponentIds().isEmpty() );
    assertFalse( entity.isAlive() );

    handler.assertEventCount( 1 );
    handler.assertNextEvent( EntityPreRemoveEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), entityId );
    } );
  }

  @Test
  public void disposeEntity_disposeErrors()
  {
    final World world = Galdr.world().initialEntityCount( 4 ).build();

    final EntityManager entityManager = world.getEntityManager();

    final int entityId = entityManager.createEntity( new BitSet() ).getId();
    entityManager.disposeEntity( entityId );

    // In Free list
    assertInvariantFailure( () -> entityManager.disposeEntity( entityId ),
                            "Galdr-0009: Attempting to dispose entity 0 but entity is not allocated." );
    // Not yet allocated
    assertInvariantFailure( () -> entityManager.disposeEntity( entityId + 1 ),
                            "Galdr-0009: Attempting to dispose entity 1 but entity is not allocated." );
    // Past the end of the capacity
    assertInvariantFailure( () -> entityManager.disposeEntity( entityManager.capacity() + 1 ),
                            "Galdr-0009: Attempting to dispose entity 5 but entity is not allocated." );
  }

  @Test
  public void disposeEntity_entityNotAllocated()
  {
    final World world = Galdr.world().build();

    final EntityManager entityManager = world.getEntityManager();
    final Entity entity = entityManager.createEntity( new BitSet() );

    assertEquals( entityManager.getEntityById( entity.getId() ), entity );

    entity.clearAlive();

    assertInvariantFailure( () -> entityManager.disposeEntity( entity.getId() ),
                            "Galdr-0059: Attempting to dispose entity 0 and entity is allocated but not alive." );
  }

  @Test
  public void verifyReuseOfFreedEntities()
  {
    final World world = Galdr.world().initialEntityCount( 4 ).build();

    final EntityManager entityManager = world.getEntityManager();

    assertEquals( entityManager.capacity(), 4 );

    final int entityId1 = entityManager.createEntity( new BitSet() ).getId();
    final int entityId2 = entityManager.createEntity( new BitSet() ).getId();
    final int entityId3 = entityManager.createEntity( new BitSet() ).getId();
    final int entityId4 = entityManager.createEntity( new BitSet() ).getId();

    assertEquals( entityManager.capacity(), 4 );

    entityManager.disposeEntity( entityId1 );

    assertEquals( entityManager.capacity(), 4 );

    entityManager.createEntity( new BitSet() );

    assertEquals( entityManager.capacity(), 4 );

    entityManager.disposeEntity( entityId2 );
    entityManager.disposeEntity( entityId3 );
    entityManager.disposeEntity( entityId4 );

    assertEquals( entityManager.capacity(), 4 );

    entityManager.createEntity( new BitSet() );
    entityManager.createEntity( new BitSet() );
    entityManager.createEntity( new BitSet() );

    assertEquals( entityManager.capacity(), 4 );
  }

  @Test
  public void capacityGrowsOnDemand()
  {
    final World world = Galdr.world().initialEntityCount( 2 ).build();

    final EntityManager entityManager = world.getEntityManager();

    assertEquals( entityManager.capacity(), 2 );

    final int entityId1 = entityManager.createEntity( new BitSet() ).getId();
    final int entityId2 = entityManager.createEntity( new BitSet() ).getId();

    assertEquals( entityManager.capacity(), 2 );

    final int entityId3 = entityManager.createEntity( new BitSet() ).getId();
    final int entityId4 = entityManager.createEntity( new BitSet() ).getId();

    assertEquals( entityManager.capacity(), 4 );

    entityManager.disposeEntity( entityId1 );

    assertEquals( entityManager.capacity(), 4 );

    final int entityId5 = entityManager.createEntity( new BitSet() ).getId();
    final int entityId6 = entityManager.createEntity( new BitSet() ).getId();

    assertEquals( entityManager.capacity(), 8 );

    entityManager.disposeEntity( entityId2 );
    entityManager.disposeEntity( entityId3 );
    entityManager.disposeEntity( entityId4 );

    assertEquals( entityManager.capacity(), 8 );

    final int entityId7 = entityManager.createEntity( new BitSet() ).getId();
    final int entityId8 = entityManager.createEntity( new BitSet() ).getId();
    final int entityId9 = entityManager.createEntity( new BitSet() ).getId();

    assertEquals( entityManager.capacity(), 8 );

    final int entityId10 = entityManager.createEntity( new BitSet() ).getId();
    final int entityId11 = entityManager.createEntity( new BitSet() ).getId();
    final int entityId12 = entityManager.createEntity( new BitSet() ).getId();

    assertEquals( entityManager.capacity(), 8 );

    final int entityId13 = entityManager.createEntity( new BitSet() ).getId();

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
}
