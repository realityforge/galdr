package galdr;

import galdr.spy.EntityAddCompleteEvent;
import galdr.spy.EntityAddStartEvent;
import galdr.spy.EntityRemoveCompleteEvent;
import galdr.spy.EntityRemoveStartEvent;
import galdr.spy.LinkAddCompleteEvent;
import galdr.spy.LinkAddStartEvent;
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
    assertTrue( armour.has( entity1.getId() ) );
    assertTrue( health.has( entity1.getId() ) );
    assertFalse( attack.has( entity1.getId() ) );

    // Component 2 components
    assertEquals( entity2.getComponentIds(), componentIds2 );
    assertFalse( armour.has( entity2.getId() ) );
    assertTrue( health.has( entity2.getId() ) );
    assertTrue( attack.has( entity2.getId() ) );

    handler.unsubscribe();

    handler.assertEventCount( 4 );
    handler.assertNextEvent( EntityAddStartEvent.class, e -> assertEquals( e.getWorld(), world ) );
    handler.assertNextEvent( EntityAddCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), entity1.getId() );
      assertTrue( entity1.isAlive() );

    } );
    handler.assertNextEvent( EntityAddStartEvent.class, e -> assertEquals( e.getWorld(), world ) );
    handler.assertNextEvent( EntityAddCompleteEvent.class, e -> {
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

    world.run( () -> entityManager.disposeEntity( entityId ) );

    assertTrue( entity.getComponentIds().isEmpty() );
    assertFalse( entity.isAlive() );
  }

  @Test
  public void disposeEntity_passingEntity()
  {
    final int initialEntityCount = 5;
    final World world = Galdr.world()
      .initialEntityCount( initialEntityCount )
      .component( Armour.class, Armour::new )
      .build();

    final ComponentAPI<Armour> armour = world.getComponentByType( Armour.class );

    final EntityManager entityManager = world.getEntityManager();

    final BitSet componentIds1 = new BitSet();
    componentIds1.set( armour.getId() );

    final Entity entity = entityManager.createEntity( componentIds1 );

    assertTrue( entity.isAlive() );

    assertEquals( entity.getComponentIds(), componentIds1 );
    assertTrue( armour.has( entity.getId() ) );

    world.run( () -> entityManager.disposeEntity( entity ) );

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

    world.run( () -> entityManager.disposeEntity( entityId ) );

    assertTrue( entity.getComponentIds().isEmpty() );
    assertFalse( entity.isAlive() );

    handler.assertEventCount( 2 );
    handler.assertNextEvent( EntityRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), entityId );
    } );
    handler.assertNextEvent( EntityRemoveCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), entityId );
    } );
  }

  @Test
  public void disposeEntity_disposeErrors()
  {
    final World world1 = Galdr.world().initialEntityCount( 4 ).build();
    final World world2 = Galdr.world().initialEntityCount( 4 ).build();

    final EntityManager entityManager = world1.getEntityManager();

    final int entityId = entityManager.createEntity( new BitSet() ).getId();

    // entity part of different world
    assertInvariantFailure( () -> world2.run( () -> entityManager.disposeEntity( entityId ) ),
                            "Galdr-0159: Attempting to dispose entity 0 which is not contained by the active world." );

    // No current world
    assertInvariantFailure( () -> entityManager.disposeEntity( entityId ),
                            "Galdr-0026: Invoked WorldHolder.world() when no world was active." );

    world1.run( () -> entityManager.disposeEntity( entityId ) );

    // In Free list
    assertInvariantFailure( () -> world1.run( () -> entityManager.disposeEntity( entityId ) ),
                            "Galdr-0009: Attempting to dispose entity 0 but entity is not allocated." );
    // Not yet allocated
    assertInvariantFailure( () -> world1.run( () -> entityManager.disposeEntity( entityId + 1 ) ),
                            "Galdr-0009: Attempting to dispose entity 1 but entity is not allocated." );
    // Past the end of the capacity
    assertInvariantFailure( () -> world1.run( () -> entityManager.disposeEntity( entityManager.capacity() + 1 ) ),
                            "Galdr-0009: Attempting to dispose entity 5 but entity is not allocated." );
  }

  @Test
  public void disposeEntity_disposeErrors_whenPassingEntity()
  {
    final World world1 = Galdr.world().initialEntityCount( 4 ).build();
    final World world2 = Galdr.world().initialEntityCount( 4 ).build();

    final EntityManager entityManager1 = world1.getEntityManager();
    final EntityManager entityManager2 = world2.getEntityManager();

    final Entity entity1 = entityManager1.createEntity( new BitSet() );
    final Entity entity2 = entityManager2.createEntity( new BitSet() );

    // Entity from wrong world
    assertInvariantFailure( () -> entityManager1.disposeEntity( entity2 ),
                            "Galdr-0020: Attempting to dispose entity 0 in world 'World@1' but entity was created in a different world." );

    world1.run( () -> entityManager1.disposeEntity( entity1 ) );

    // In Free list
    assertInvariantFailure( () -> entityManager1.disposeEntity( entity1 ),
                            "Galdr-0009: Attempting to dispose entity 0 but entity is not allocated." );
  }

  @Test
  public void disposeEntity_entityNotAllocated()
  {
    final World world = Galdr.world().build();

    final EntityManager entityManager = world.getEntityManager();
    final Entity entity = entityManager.createEntity( new BitSet() );

    assertEquals( entityManager.getEntityById( entity.getId() ), entity );

    entity.clearAlive();

    assertInvariantFailure( () -> world.run( () -> entityManager.disposeEntity( entity.getId() ) ),
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

    world.run( () -> entityManager.disposeEntity( entityId1 ) );

    assertEquals( entityManager.capacity(), 4 );

    entityManager.createEntity( new BitSet() );

    assertEquals( entityManager.capacity(), 4 );

    world.run( () -> entityManager.disposeEntity( entityId2 ) );
    world.run( () -> entityManager.disposeEntity( entityId3 ) );
    world.run( () -> entityManager.disposeEntity( entityId4 ) );

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

    world.run( () -> entityManager.disposeEntity( entityId1 ) );

    assertEquals( entityManager.capacity(), 4 );

    final int entityId5 = entityManager.createEntity( new BitSet() ).getId();
    final int entityId6 = entityManager.createEntity( new BitSet() ).getId();

    assertEquals( entityManager.capacity(), 8 );

    world.run( () -> entityManager.disposeEntity( entityId2 ) );
    world.run( () -> entityManager.disposeEntity( entityId3 ) );
    world.run( () -> entityManager.disposeEntity( entityId4 ) );

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

  @Test
  public void getEntityById_entityNotAlive()
  {
    final World world = Galdr.world().build();

    final EntityManager entityManager = world.getEntityManager();

    final Entity entity = entityManager.createEntity( new BitSet() );

    assertEquals( entityManager.getEntityById( entity.getId() ), entity );
    entity.clearAlive();
    assertInvariantFailure( () -> entityManager.getEntityById( entity.getId() ),
                            "Galdr-0078: Attempting to get entity 0 but entity is allocated but not alive." );
  }

  @Test
  public void getEntityById_entityNotAllocated()
  {
    final World world = Galdr.world().build();

    final EntityManager entityManager = world.getEntityManager();

    assertInvariantFailure( () -> entityManager.getEntityById( 37 ),
                            "Galdr-0079: Attempting to get entity 37 but entity is not allocated." );
  }

  @Test
  public void createEntity_badComponentId()
  {
    final World world = Galdr.world()
      .component( Armour.class, Armour::new )
      .component( Health.class, Health::new )
      .build();

    final EntityManager entityManager = world.getEntityManager();

    final BitSet componentIds1 = new BitSet();
    // invalid
    componentIds1.set( 33 );

    assertInvariantFailure( () -> entityManager.createEntity( componentIds1 ),
                            "Galdr-0006: Attempting to create entity with invalid componentId 33" );

    final BitSet componentIds2 = new BitSet();
    componentIds2.set( 0 );
    componentIds2.set( 1 );
    // 2 is invalid
    componentIds2.set( 2 );

    assertInvariantFailure( () -> entityManager.createEntity( componentIds2 ),
                            "Galdr-0006: Attempting to create entity with invalid componentId 2" );
  }

  @Test
  public void link()
  {
    final World world = Galdr.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity entity1 = em.createEntity( new BitSet() );
    final Entity entity2 = em.createEntity( new BitSet() );

    assertEquals( entity1.getInwardLinks().size(), 0 );
    assertEquals( entity1.getOutwardLinks().size(), 0 );
    assertEquals( entity2.getInwardLinks().size(), 0 );
    assertEquals( entity2.getOutwardLinks().size(), 0 );

    final Link link = world.run( () -> em.link( entity1, entity2, false, false ) );

    assertEquals( link.getSourceEntity(), entity1 );
    assertEquals( link.getTargetEntity(), entity2 );
    assertFalse( link.shouldCascadeSourceRemoveToTarget() );
    assertFalse( link.shouldCascadeTargetRemoveToSource() );
    assertTrue( link.isValid() );
    assertEquals( link.toString(), "Link[0->1]" );

    assertEquals( entity1.getInwardLinks().size(), 0 );
    assertEquals( entity1.getOutwardLinks().size(), 1 );
    assertEquals( entity2.getInwardLinks().size(), 1 );
    assertEquals( entity2.getOutwardLinks().size(), 0 );
  }

  @Test
  public void link_withSpyEnabled()
  {
    final World world = Galdr.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity entity1 = em.createEntity( new BitSet() );
    final Entity entity2 = em.createEntity( new BitSet() );

    assertEquals( entity1.getInwardLinks().size(), 0 );
    assertEquals( entity1.getOutwardLinks().size(), 0 );
    assertEquals( entity2.getInwardLinks().size(), 0 );
    assertEquals( entity2.getOutwardLinks().size(), 0 );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( world );
    final Link link = world.run( () -> em.link( entity1, entity2, false, false ) );
    handler.unsubscribe();

    assertEquals( link.getSourceEntity(), entity1 );
    assertEquals( link.getTargetEntity(), entity2 );
    assertFalse( link.shouldCascadeSourceRemoveToTarget() );
    assertFalse( link.shouldCascadeTargetRemoveToSource() );
    assertTrue( link.isValid() );
    assertEquals( link.toString(), "Link[0->1]" );

    assertEquals( entity1.getInwardLinks().size(), 0 );
    assertEquals( entity1.getOutwardLinks().size(), 1 );
    assertEquals( entity2.getInwardLinks().size(), 1 );
    assertEquals( entity2.getOutwardLinks().size(), 0 );

    handler.assertEventCount( 2 );
    handler.assertNextEvent( LinkAddStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getSourceEntityId(), entity1.getId() );
      assertEquals( e.getTargetEntityId(), entity2.getId() );
    } );
    handler.assertNextEvent( LinkAddCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getSourceEntityId(), entity1.getId() );
      assertEquals( e.getTargetEntityId(), entity2.getId() );
    } );
  }

  @Test
  public void link_butSourceEntityNotAlive()
  {
    final World world = Galdr.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity entity1 = em.createEntity( new BitSet() );
    final Entity entity2 = em.createEntity( new BitSet() );

    world.run( () -> em.disposeEntity( entity1 ) );

    assertInvariantFailure( () -> world.run( () -> em.link( entity1, entity2, false, false ) ),
                            "Galdr-0011: Attempted to link from entity 0 to entity 1 but the source entity is not alive." );

    assertEquals( entity1.getInwardLinks().size(), 0 );
    assertEquals( entity1.getOutwardLinks().size(), 0 );
    assertEquals( entity2.getInwardLinks().size(), 0 );
    assertEquals( entity2.getOutwardLinks().size(), 0 );
  }

  @Test
  public void link_butTargetEntityNotAlive()
  {
    final World world = Galdr.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity entity1 = em.createEntity( new BitSet() );
    final Entity entity2 = em.createEntity( new BitSet() );

    world.run( () -> em.disposeEntity( entity2 ) );

    assertInvariantFailure( () -> world.run( () -> em.link( entity1, entity2, false, false ) ),
                            "Galdr-0010: Attempted to link from entity 0 to entity 1 but the target entity is not alive." );

    assertEquals( entity1.getInwardLinks().size(), 0 );
    assertEquals( entity1.getOutwardLinks().size(), 0 );
    assertEquals( entity2.getInwardLinks().size(), 0 );
    assertEquals( entity2.getOutwardLinks().size(), 0 );
  }

  @Test
  public void link_self()
  {
    final World world = Galdr.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity entity = em.createEntity( new BitSet() );

    assertInvariantFailure( () -> world.run( () -> em.link( entity, entity, false, false ) ),
                            "Galdr-0110: Attempted to link entity 0 to itself." );

    assertEquals( entity.getInwardLinks().size(), 0 );
    assertEquals( entity.getOutwardLinks().size(), 0 );
  }

  @Test
  public void link_butSourceFromDifferentWorld()
  {
    final World world1 = Galdr.world().build();
    final EntityManager em1 = world1.getEntityManager();
    final Entity entity1 = em1.createEntity( new BitSet() );

    final World world2 = Galdr.world().build();
    final EntityManager em2 = world2.getEntityManager();
    final Entity entity2 = em2.createEntity( new BitSet() );

    assertInvariantFailure( () -> world2.run( () -> em1.link( entity1, entity2, false, false ) ),
                            "Galdr-0911: Attempted to link from entity 0 to entity 0 in world named 'World@2' but world does not contain source entity." );

    assertEquals( entity1.getInwardLinks().size(), 0 );
    assertEquals( entity1.getOutwardLinks().size(), 0 );
    assertEquals( entity2.getInwardLinks().size(), 0 );
    assertEquals( entity2.getOutwardLinks().size(), 0 );
  }

  @Test
  public void link_butTargetFromDifferentWorld()
  {
    final World world1 = Galdr.world().build();
    final EntityManager em1 = world1.getEntityManager();
    final Entity entity1 = em1.createEntity( new BitSet() );

    final World world2 = Galdr.world().build();
    final EntityManager em2 = world2.getEntityManager();
    final Entity entity2 = em2.createEntity( new BitSet() );

    assertInvariantFailure( () -> world1.run( () -> em1.link( entity1, entity2, false, false ) ),
                            "Galdr-0912: Attempted to link from entity 0 to entity 0 in world named 'World@1' but world does not contain target entity." );

    assertEquals( entity1.getInwardLinks().size(), 0 );
    assertEquals( entity1.getOutwardLinks().size(), 0 );
    assertEquals( entity2.getInwardLinks().size(), 0 );
    assertEquals( entity2.getOutwardLinks().size(), 0 );
  }
}
