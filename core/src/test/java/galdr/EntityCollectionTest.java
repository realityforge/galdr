package galdr;

import java.util.Collections;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class EntityCollectionTest
  extends AbstractTest
{
  private static class Component1
  {
  }

  @Test
  public void create()
  {
    final World world = Worlds.world().component( Component1.class ).build();

    final ComponentManager<Component1> component = world.getComponentManagerByType( Component1.class );
    assertEquals( world.getEntityCollections().size(), 0 );
    //TODO: Change component to componentAPI and use spy to get collections count
    assertEquals( component.getCollections().size(), 0 );
    final EntityCollection collection =
      createSubscription( world, Collections.singletonList( Component1.class ) ).getCollection();

    assertEquals( world.getEntityCollections().size(), 1 );
    assertEquals( component.getCollections().size(), 1 );

    final AreaOfInterest areaOfInterest = collection.getAreaOfInterest();
    assertEquals( world.findCollection( areaOfInterest ), collection );

    assertEquals( areaOfInterest.getAll().getBitSet(), set( 0 ) );
    assertEquals( areaOfInterest.getOne().getBitSet(), set() );
    assertEquals( areaOfInterest.getExclude().getBitSet(), set() );

    assertEquals( collection.getWorld(), world );
    assertEquals( collection.getAreaOfInterest(), areaOfInterest );
    assertEquals( collection.getEntities().cardinality(), 0 );
    assertEquals( collection.getNewEntities().cardinality(), 0 );
    assertEquals( collection.getCurrentEntityId(), -1 );
    assertTrue( collection.isNotDisposed() );
    assertFalse( collection.isDisposed() );
    assertFalse( collection.isProcessingNewEntities() );
    assertFalse( collection.hasNewEntities() );
  }

  @Test
  public void createCollection_whenMatchingCollectionExists()
  {
    final World world = Worlds.world().component( Component1.class ).build();

    final AreaOfInterest areaOfInterest = world.createAreaOfInterest( Collections.singletonList( Component1.class ) );

    // Ensure a matching collection exists
    run( world, () -> world.createSubscription( areaOfInterest ) );

    assertInvariantFailure( () -> run( world, () -> world.createCollection( areaOfInterest ) ),
                            "Galdr-0034: World.createCollection() invoked but collection with matching AreaOfInterest already exists." );
  }

  @Test
  public void removeCollection_collectionsMisaligned()
  {
    final World world = Worlds.world().component( Component1.class ).build();

    final AreaOfInterest areaOfInterest = world.createAreaOfInterest( Collections.singletonList( Component1.class ) );

    // Create a collection with known AreaOfInterest
    final EntityCollection collection1 = run( world, () -> world.createSubscription( areaOfInterest ) ).getCollection();
    run( world, () -> world.removeCollection( collection1 ) );

    // Create a different collection with the same AreaOfInterest
    run( world, () -> world.createSubscription( areaOfInterest ) );

    assertInvariantFailure( () -> run( world, () -> world.removeCollection( collection1 ) ),
                            "Galdr-0041: World.removeCollection() invoked existing collection does not match supplied collection." );
  }

  @Test
  public void ComponentMnaager_addCollection_CollectionAlreadyRegistered()
  {
    final World world = Worlds.world().component( Component1.class ).build();

    final Subscription subscription = createSubscription( world, Collections.singletonList( Component1.class ) );
    final EntityCollection collection = subscription.getCollection();

    final ComponentManager<Component1> componentManager = world.getComponentManagerByType( Component1.class );
    assertInvariantFailure( () -> componentManager.addCollection( collection ),
                            "Galdr-0029: The ComponentManager.addCollection() method for the component named 'Component1' was invoked but collection is already registered with ComponentManager." );
  }

  @Test
  public void ComponentMnaager_removeCollection_CollectionAlreadyRegistered()
  {
    final World world = Worlds.world().component( Component1.class ).build();

    final Subscription subscription = createSubscription( world, Collections.singletonList( Component1.class ) );
    final EntityCollection collection = subscription.getCollection();

    final ComponentManager<Component1> componentManager = world.getComponentManagerByType( Component1.class );
    componentManager.removeCollection( collection );
    assertInvariantFailure( () -> componentManager.removeCollection( collection ),
                            "Galdr-0042: The ComponentManager.removeCollection() method for the component named 'Component1' was invoked but collection is not registered with ComponentManager." );
  }

  @Test
  public void removeCollection_collectionsMissing()
  {
    final World world = Worlds.world().component( Component1.class ).build();

    // Create a collection with known AreaOfInterest
    final EntityCollection collection =
      createSubscription( world, Collections.singletonList( Component1.class ) ).getCollection();
    // Remove it so there is no match
    run( world, () -> world.removeCollection( collection ) );

    assertInvariantFailure( () -> run( world, () -> world.removeCollection( collection ) ),
                            "Galdr-0020: World.removeCollection() invoked but no such collection." );
  }

  @Test
  public void refCount()
  {
    final World world = Worlds.world().component( Component1.class ).build();

    assertEquals( world.getEntityCollections().size(), 0 );

    final EntityCollection collection =
      createSubscription( world, Collections.singletonList( Component1.class ) ).getCollection();

    assertEquals( world.getEntityCollections().size(), 1 );
    assertEquals( collection.getRefCount(), 1 );
    assertTrue( collection.isNotDisposed() );

    run( world, collection::incRef );

    assertEquals( world.getEntityCollections().size(), 1 );
    assertEquals( collection.getRefCount(), 2 );
    assertTrue( collection.isNotDisposed() );

    run( world, collection::decRef );

    assertEquals( world.getEntityCollections().size(), 1 );
    assertEquals( collection.getRefCount(), 1 );
    assertTrue( collection.isNotDisposed() );

    run( world, collection::decRef );

    assertEquals( world.getEntityCollections().size(), 0 );
    assertEquals( collection.getRefCount(), 0 );
    assertTrue( collection.isDisposed() );
  }

  @Test
  public void ensureNotDisposed()
  {
    final World world = Worlds.world().component( Component1.class ).build();

    final EntityCollection collection =
      createSubscription( world, Collections.singletonList( Component1.class ) ).getCollection();

    collection.ensureNotDisposed();

    collection.markAsDisposed();

    assertInvariantFailure( collection::ensureNotDisposed,
                            "Galdr-0015: Invoked method on a disposed EntityCollection." );
  }

  @Test
  public void createCollection_whenInContextOfDifferentWorld()
  {
    final World world1 = Worlds.world().component( Component1.class ).build();
    final World world2 = Worlds.world().component( Component1.class ).build();

    final AreaOfInterest areaOfInterest = world2.createAreaOfInterest( Collections.singletonList( Component1.class ) );

    assertInvariantFailure( () -> run( world1, () -> world2.createCollection( areaOfInterest ) ),
                            "Galdr-0037: World.createCollection() invoked on world named 'World@2' when a world named 'World@1' is active." );
  }

  @Test
  public void ensureCurrentWorldMatches()
  {
    final World world1 = Worlds.world().component( Component1.class ).build();
    final World world2 = Worlds.world().component( Component1.class ).build();

    final EntityCollection collection =
      createSubscription( world1, Collections.singletonList( Component1.class ) ).getCollection();

    run( world1, collection::ensureCurrentWorldMatches );

    assertInvariantFailure( () -> run( world2, collection::ensureCurrentWorldMatches ),
                            "Galdr-0036: EntityCollection method invoked in the context of the world 'World@2' but the collection belongs to the world 'World@1'" );
  }

  @Test
  public void createAndDisposeEntityThatMatchesCollection()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final EntityCollection collection =
      createSubscription( world, Collections.singletonList( Component1.class ) ).getCollection();

    assertEquals( collection.getEntities().cardinality(), 0 );

    final int entityId = createEntity( world, Component1.class );

    assertEquals( collection.getEntities().cardinality(), 1 );
    assertTrue( collection.getEntities().get( entityId ) );

    run( world, () -> world.disposeEntity( entityId ) );

    assertEquals( collection.getEntities().cardinality(), 0 );
  }

  @Test
  public void entityAdd_badEntity()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final EntityCollection collection =
      createSubscription( world, Collections.singletonList( Component1.class ) ).getCollection();

    final int entityId = createEntity( world );
    final Entity entity = world.getEntityManager().unsafeGetEntityById( entityId );
    run( world, () -> world.disposeEntity( entityId ) );

    assertInvariantFailure( () -> run( world, () -> collection.entityAdd( entity ) ),
                            "Galdr-0018: Invoked EntityCollection.entityAdd with invalid Entity." );
  }

  @Test
  public void entityRemove_badEntity()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final EntityCollection collection =
      createSubscription( world, Collections.singletonList( Component1.class ) ).getCollection();

    final int entityId = createEntity( world );
    final Entity entity = world.getEntityManager().unsafeGetEntityById( entityId );
    run( world, () -> world.disposeEntity( entityId ) );

    assertInvariantFailure( () -> run( world, () -> collection.entityRemove( entity ) ),
                            "Galdr-0018: Invoked EntityCollection.entityRemove with invalid Entity." );
  }

  @Test
  public void componentChangeThatMatchesCollection()
  {
    final World world = Worlds.world().component( Component1.class ).build();

    final EntityCollection collection =
      createSubscription( world, Collections.singletonList( Component1.class ) ).getCollection();

    assertEquals( collection.getEntities().cardinality(), 0 );

    final int entityId = createEntity( world );

    assertEquals( collection.getEntities().cardinality(), 0 );

    run( world, () -> world.getComponentByType( Component1.class ).allocate( entityId ) );

    assertEquals( collection.getEntities().cardinality(), 1 );
    assertTrue( collection.getEntities().get( entityId ) );

    run( world, () -> world.getComponentByType( Component1.class ).remove( entityId ) );

    assertEquals( collection.getEntities().cardinality(), 0 );
  }

  @Test
  public void componentChange_badEntity()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final EntityCollection collection =
      createSubscription( world, Collections.singletonList( Component1.class ) ).getCollection();

    final int entityId = createEntity( world );
    final Entity entity = world.getEntityManager().unsafeGetEntityById( entityId );
    run( world, () -> world.disposeEntity( entityId ) );

    assertInvariantFailure( () -> run( world, () -> collection.componentChange( entity ) ),
                            "Galdr-0018: Invoked EntityCollection.componentChange with invalid Entity." );
  }

  @Test
  public void iterateOverEmptyCollection()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription = createSubscription( world, Collections.singletonList( Component1.class ) );
    final EntityCollection collection = subscription.getCollection();

    assertNull( collection.getIteratingSubscription() );

    beginIteration( world, collection, subscription );

    // There is no entities so should immediately complete iteration
    assertNextEntity( world, collection, subscription, -1 );

    // Verify we are complete
    assertCollectionComplete( collection );
  }

  @SuppressWarnings( "unused" )
  @Test
  public void iterateOverCollectionContainingSubsetOfEntitiesNoModifications()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription = createSubscription( world, Collections.singletonList( Component1.class ) );
    final EntityCollection collection = subscription.getCollection();

    final int entityId0 = createEntity( world, Component1.class );
    final int entityId1 = createEntity( world, Component1.class );
    final int entityId2 = createEntity( world );
    final int entityId3 = createEntity( world );
    final int entityId4 = createEntity( world, Component1.class );
    final int entityId5 = createEntity( world );
    final int entityId6 = createEntity( world );
    final int entityId7 = createEntity( world, Component1.class );
    final int entityId8 = createEntity( world );
    final int entityId9 = createEntity( world, Component1.class );

    assertEquals( entityId0, 0 );
    assertEquals( entityId9, 9 );

    assertNull( collection.getIteratingSubscription() );

    beginIteration( world, collection, subscription );

    assertNextEntity( world, collection, subscription, entityId0 );
    assertNextEntity( world, collection, subscription, entityId1 );
    assertNextEntity( world, collection, subscription, entityId4 );
    assertNextEntity( world, collection, subscription, entityId7 );
    assertNextEntity( world, collection, subscription, entityId9 );
    assertNextEntity( world, collection, subscription, -1 );

    // Verify we are complete
    assertCollectionComplete( collection );
  }

  @SuppressWarnings( "unused" )
  @Test
  public void iterateOverCollectionContainingSubsetOfEntitiesRemoveEntityLaterInIteration()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final ComponentManager<Component1> componentApi = world.getComponentByType( Component1.class );
    final Subscription subscription = createSubscription( world, Collections.singletonList( Component1.class ) );
    final EntityCollection collection = subscription.getCollection();

    final int entityId0 = createEntity( world, Component1.class );
    final int entityId1 = createEntity( world, Component1.class );
    final int entityId2 = createEntity( world );
    final int entityId3 = createEntity( world );
    final int entityId4 = createEntity( world, Component1.class );
    final int entityId5 = createEntity( world );
    final int entityId6 = createEntity( world );
    final int entityId7 = createEntity( world, Component1.class );
    final int entityId8 = createEntity( world );
    final int entityId9 = createEntity( world, Component1.class );

    assertEquals( entityId0, 0 );
    assertEquals( entityId9, 9 );

    assertNull( collection.getIteratingSubscription() );

    beginIteration( world, collection, subscription );

    assertNextEntity( world, collection, subscription, entityId0 );

    // Not part of the collection
    run( world, () -> world.disposeEntity( entityId3 ) );

    // Part of the collection
    run( world, () -> world.disposeEntity( entityId4 ) );

    // Not part of the collection
    run( world, () -> world.disposeEntity( entityId8 ) );

    run( world, () -> componentApi.remove( entityId7 ) );

    assertNextEntity( world, collection, subscription, entityId1 );
    assertNextEntity( world, collection, subscription, entityId9 );
    assertNextEntity( world, collection, subscription, -1 );

    // Verify we are complete
    assertCollectionComplete( collection );
  }

  @SuppressWarnings( "unused" )
  @Test
  public void iterateOverCollectionContainingSubsetOfEntitiesRemoveEntityEarlierInIteration()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription = createSubscription( world, Collections.singletonList( Component1.class ) );
    final EntityCollection collection = subscription.getCollection();

    final int entityId0 = createEntity( world, Component1.class );
    final int entityId1 = createEntity( world, Component1.class );
    final int entityId2 = createEntity( world );
    final int entityId3 = createEntity( world );
    final int entityId4 = createEntity( world, Component1.class );
    final int entityId5 = createEntity( world );
    final int entityId6 = createEntity( world );
    final int entityId7 = createEntity( world, Component1.class );
    final int entityId8 = createEntity( world );
    final int entityId9 = createEntity( world, Component1.class );

    assertEquals( entityId0, 0 );
    assertEquals( entityId9, 9 );

    assertNull( collection.getIteratingSubscription() );

    beginIteration( world, collection, subscription );

    assertNextEntity( world, collection, subscription, entityId0 );
    assertNextEntity( world, collection, subscription, entityId1 );
    assertNextEntity( world, collection, subscription, entityId4 );
    assertNextEntity( world, collection, subscription, entityId7 );

    // Not part of the collection
    run( world, () -> world.disposeEntity( 3 ) );

    // Part of the collection
    run( world, () -> world.disposeEntity( 4 ) );

    // Not part of the collection
    run( world, () -> world.disposeEntity( 8 ) );

    assertNextEntity( world, collection, subscription, entityId9 );
    assertNextEntity( world, collection, subscription, -1 );

    // Verify we are complete
    assertCollectionComplete( collection );
  }

  @SuppressWarnings( "unused" )
  @Test
  public void iterateOverCollectionContainingSubsetOfEntitiesAddEntityLaterInIteration()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final ComponentManager<Component1> componentApi = world.getComponentByType( Component1.class );
    final Subscription subscription = createSubscription( world, Collections.singletonList( Component1.class ) );
    final EntityCollection collection = subscription.getCollection();

    final int entityId0 = createEntity( world, Component1.class );
    final int entityId1 = createEntity( world, Component1.class );
    final int entityId2 = createEntity( world );
    final int entityId3 = createEntity( world );
    final int entityId4 = createEntity( world, Component1.class );
    final int entityId5 = createEntity( world );
    final int entityId6 = createEntity( world );
    final int entityId7 = createEntity( world, Component1.class );
    final int entityId8 = createEntity( world );
    final int entityId9 = createEntity( world, Component1.class );

    assertNull( collection.getIteratingSubscription() );

    beginIteration( world, collection, subscription );

    assertNextEntity( world, collection, subscription, entityId0 );

    assertEquals( collection.getNewEntities().cardinality(), 0 );
    assertFalse( collection.hasNewEntities() );

    // Not part of the collection before allocate
    run( world, () -> componentApi.allocate( entityId3 ) );

    // Not part of the collection before allocate
    run( world, () -> componentApi.allocate( entityId8 ) );

    assertEquals( collection.getNewEntities().cardinality(), 0 );
    assertFalse( collection.hasNewEntities() );

    assertNextEntity( world, collection, subscription, entityId1 );
    assertNextEntity( world, collection, subscription, entityId3 );
    assertNextEntity( world, collection, subscription, entityId4 );
    assertNextEntity( world, collection, subscription, entityId7 );
    assertNextEntity( world, collection, subscription, entityId8 );
    assertNextEntity( world, collection, subscription, entityId9 );
    assertNextEntity( world, collection, subscription, -1 );

    // Verify we are complete
    assertCollectionComplete( collection );
  }

  @SuppressWarnings( "unused" )
  @Test
  public void iterateOverCollectionContainingSubsetOfEntitiesAddEntityEarlierInIteration()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final ComponentManager<Component1> componentApi = world.getComponentByType( Component1.class );
    final Subscription subscription = createSubscription( world, Collections.singletonList( Component1.class ) );
    final EntityCollection collection = subscription.getCollection();

    final int entityId0 = createEntity( world, Component1.class );
    final int entityId1 = createEntity( world, Component1.class );
    final int entityId2 = createEntity( world );
    final int entityId3 = createEntity( world );
    final int entityId4 = createEntity( world, Component1.class );
    final int entityId5 = createEntity( world );
    final int entityId6 = createEntity( world );
    final int entityId7 = createEntity( world, Component1.class );
    final int entityId8 = createEntity( world );
    final int entityId9 = createEntity( world, Component1.class );

    assertNull( collection.getIteratingSubscription() );

    beginIteration( world, collection, subscription );

    assertNextEntity( world, collection, subscription, entityId0 );
    assertNextEntity( world, collection, subscription, entityId1 );
    assertNextEntity( world, collection, subscription, entityId4 );
    assertNextEntity( world, collection, subscription, entityId7 );

    assertEquals( collection.getNewEntities().cardinality(), 0 );
    assertFalse( collection.hasNewEntities() );

    // Not part of the collection before allocate
    run( world, () -> componentApi.allocate( entityId5 ) );

    // Not part of the collection before allocate
    run( world, () -> componentApi.allocate( entityId3 ) );

    assertEquals( collection.getNewEntities().cardinality(), 2 );
    assertTrue( collection.hasNewEntities() );

    assertNextEntity( world, collection, subscription, entityId9 );

    // This wraps around to the NewEntities list
    assertNextEntity( world, collection, subscription, entityId3 );

    assertEquals( collection.getNewEntities().cardinality(), 1 );

    assertNextEntity( world, collection, subscription, entityId5 );

    assertEquals( collection.getNewEntities().cardinality(), 0 );

    assertNextEntity( world, collection, subscription, -1 );

    // Verify we are complete
    assertCollectionComplete( collection );
  }

  @SuppressWarnings( "unused" )
  @Test
  public void iterateOverCollectionWithMultiplePassesOverNewEntities()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final ComponentManager<Component1> componentApi = world.getComponentByType( Component1.class );
    final Subscription subscription = createSubscription( world, Collections.singletonList( Component1.class ) );
    final EntityCollection collection = subscription.getCollection();

    final int entityId0 = createEntity( world, Component1.class );
    final int entityId1 = createEntity( world );
    final int entityId2 = createEntity( world );
    final int entityId3 = createEntity( world, Component1.class );

    assertNull( collection.getIteratingSubscription() );

    beginIteration( world, collection, subscription );

    assertNextEntity( world, collection, subscription, entityId0 );
    assertNextEntity( world, collection, subscription, entityId3 );

    assertEquals( collection.getNewEntities().cardinality(), 0 );
    assertFalse( collection.hasNewEntities() );

    // Not part of the collection before allocate
    run( world, () -> componentApi.allocate( entityId2 ) );

    assertEquals( collection.getNewEntities().cardinality(), 1 );
    assertTrue( collection.hasNewEntities() );

    // This wraps around to the NewEntities list
    assertNextEntity( world, collection, subscription, entityId2 );

    assertEquals( collection.getNewEntities().cardinality(), 0 );
    assertTrue( collection.hasNewEntities() );

    // Not part of the collection before allocate
    // This will cause another wrap around
    run( world, () -> componentApi.allocate( entityId1 ) );

    // This wraps around again
    assertNextEntity( world, collection, subscription, entityId1 );

    assertEquals( collection.getNewEntities().cardinality(), 0 );

    assertNextEntity( world, collection, subscription, -1 );

    // Verify we are complete
    assertCollectionComplete( collection );
  }

  @SuppressWarnings( "unused" )
  @Test
  public void iterateWhereEntityAddedAndRemovedFromNewEntitiesPriorToVisiting()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final ComponentManager<Component1> componentApi = world.getComponentByType( Component1.class );
    final Subscription subscription = createSubscription( world, Collections.singletonList( Component1.class ) );
    final EntityCollection collection = subscription.getCollection();

    final int entityId0 = createEntity( world, Component1.class );
    final int entityId1 = createEntity( world );
    final int entityId2 = createEntity( world );
    final int entityId3 = createEntity( world );
    final int entityId4 = createEntity( world, Component1.class );

    assertNull( collection.getIteratingSubscription() );

    beginIteration( world, collection, subscription );

    assertNextEntity( world, collection, subscription, entityId0 );
    assertNextEntity( world, collection, subscription, entityId4 );

    assertEquals( collection.getNewEntities().cardinality(), 0 );
    assertFalse( collection.hasNewEntities() );

    // Add to New Entities
    run( world, () -> componentApi.allocate( entityId1 ) );
    run( world, () -> componentApi.allocate( entityId2 ) );

    assertEquals( collection.getNewEntities().cardinality(), 2 );
    assertTrue( collection.hasNewEntities() );

    // Remove from New Entities
    run( world, () -> componentApi.remove( entityId1 ) );

    assertEquals( collection.getNewEntities().cardinality(), 1 );
    assertTrue( collection.hasNewEntities() );

    // This wraps around to the NewEntities list
    assertNextEntity( world, collection, subscription, entityId2 );
    assertEquals( collection.getNewEntities().cardinality(), 0 );

    assertNextEntity( world, collection, subscription, -1 );

    // Verify we are complete
    assertCollectionComplete( collection );
  }

  @Test
  public void abortIterationBeforeAllEntitiesProcessed()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription = createSubscription( world, Collections.singletonList( Component1.class ) );
    final EntityCollection collection = subscription.getCollection();

    final int entityId0 = createEntity( world, Component1.class );
    createEntity( world, Component1.class );

    assertNull( collection.getIteratingSubscription() );

    beginIteration( world, collection, subscription );

    assertNextEntity( world, collection, subscription, entityId0 );

    // Explicit complete of iteration before all entities processed
    run( world, subscription::abortIteration );

    // Verify we are complete
    assertCollectionComplete( collection );
  }

  @Test
  public void abortIterationWhenProcessingNewEntitiesList()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final ComponentManager<Component1> componentApi = world.getComponentByType( Component1.class );
    final Subscription subscription = createSubscription( world, Collections.singletonList( Component1.class ) );
    final EntityCollection collection = subscription.getCollection();

    final int entityId0 = createEntity( world );
    final int entityId1 = createEntity( world );
    final int entityId2 = createEntity( world, Component1.class );

    assertNull( collection.getIteratingSubscription() );

    beginIteration( world, collection, subscription );

    assertNextEntity( world, collection, subscription, entityId2 );

    // Add to New Entities to collection
    run( world, () -> componentApi.allocate( entityId1 ) );
    run( world, () -> componentApi.allocate( entityId0 ) );

    assertEquals( collection.getNewEntities().cardinality(), 2 );
    assertTrue( collection.hasNewEntities() );

    assertNextEntity( world, collection, subscription, entityId0 );

    assertEquals( collection.getNewEntities().cardinality(), 1 );
    assertTrue( collection.hasNewEntities() );

    // Explicit complete of iteration before all entities processed
    run( world, subscription::abortIteration );

    // Verify we are complete
    assertCollectionComplete( collection );
  }

  @Test
  public void abortIteration_noIterationActive()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription = createSubscription( world, Collections.singletonList( Component1.class ) );

    assertInvariantFailure( () -> run( world, subscription::abortIteration ),
                            "Galdr-0047: EntityCollection.abortIteration() invoked with subscription named '" +
                            subscription.getName() + "' but no iteration was active." );
  }

  @Test
  public void abortIteration_nonMatchingSubscription()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription1 = createSubscription( world, Collections.singletonList( Component1.class ) );
    final Subscription subscription2 = createSubscription( world, Collections.singletonList( Component1.class ) );

    run( world, subscription1::beginIteration );

    assertInvariantFailure( () -> run( world, subscription2::abortIteration ),
                            "Galdr-0027: EntityCollection.abortIteration() invoked with subscription named 'Subscription@2' but this does not match the existing subscription named 'Subscription@1'." );
  }

  @Test
  public void nextEntity_noActiveSubscription()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription = createSubscription( world, Collections.singletonList( Component1.class ) );

    assertInvariantFailure( () -> run( world, subscription::nextEntity ),
                            "Galdr-0047: EntityCollection.nextEntity() invoked with subscription named 'Subscription@1' but no iteration was active." );
  }

  @Test
  public void nextEntity_nonMatchingSubscription()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription1 = createSubscription( world, Collections.singletonList( Component1.class ) );
    final Subscription subscription2 = createSubscription( world, Collections.singletonList( Component1.class ) );

    run( world, subscription1::beginIteration );

    assertInvariantFailure( () -> run( world, subscription2::nextEntity ),
                            "Galdr-0025: EntityCollection.nextEntity() invoked with subscription named 'Subscription@2' but an existing iteration is in progress with subscription named 'Subscription@1'." );
  }

  @Test
  public void beginIteration_subscriptionAlreadyIterating()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription1 = createSubscription( world, Collections.singletonList( Component1.class ) );
    final Subscription subscription2 = createSubscription( world, Collections.singletonList( Component1.class ) );
    final EntityCollection collection = subscription1.getCollection();

    beginIteration( world, collection, subscription1 );

    assertInvariantFailure( () -> run( world, () -> collection.beginIteration( subscription2 ) ),
                            "Galdr-0022: EntityCollection.beginIteration() invoked with subscription named '" +
                            subscription2.getName() + "' but an existing iteration is in progress with subscription " +
                            "named '" + subscription1.getName() + "'." );
  }

  @Test
  public void beginIteration_whenIterationInProgress()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription = createSubscription( world, Collections.singletonList( Component1.class ) );
    final EntityCollection collection = subscription.getCollection();

    final int entityId0 = createEntity( world, Component1.class );

    beginIteration( world, collection, subscription );

    assertNextEntity( world, collection, subscription, entityId0 );

    assertInvariantFailure( () -> run( world, subscription::beginIteration ),
                            "Galdr-0032: EntityCollection.beginIteration() invoked when _currentEntityId has not been reset. Current value 0" );
  }

  private void beginIteration( @Nonnull final World world,
                               @Nonnull final EntityCollection collection,
                               @Nonnull final Subscription subscription )
  {
    run( world, subscription::beginIteration );

    assertCollectionIterationStart( collection, subscription );
  }

  private void assertCollectionIterationStart( @Nonnull final EntityCollection collection,
                                               @Nonnull final Subscription subscription )
  {
    assertTrue( collection.isIterationInProgress() );
    assertEquals( collection.getCurrentEntityId(), -1 );
    assertEquals( collection.getIteratingSubscription(), subscription );
  }

  private void assertCollectionComplete( @Nonnull final EntityCollection collection )
  {
    assertFalse( collection.isIterationInProgress() );
    assertEquals( collection.getCurrentEntityId(), -1 );
    assertNull( collection.getIteratingSubscription() );
    assertEquals( collection.getNewEntities().cardinality(), 0 );
    assertFalse( collection.hasNewEntities() );
    assertFalse( collection.isProcessingNewEntities() );
  }

  private void assertNextEntity( @Nonnull final World world,
                                 @Nonnull final EntityCollection collection,
                                 @Nonnull final Subscription subscription,
                                 final int entityId )
  {
    run( world, () -> assertEquals( subscription.nextEntity(), entityId ) );
    assertEquals( collection.getCurrentEntityId(), entityId );
  }

}
