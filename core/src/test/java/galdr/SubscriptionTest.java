package galdr;

import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SubscriptionTest
  extends AbstractTest
{
  private static class Component1
  {
  }

  @Test
  public void create()
  {
    final World world = Worlds.world().component( Component1.class ).build();

    assertEquals( world.getSubscriptions().size(), 0 );
    final Subscription subscription = world.createSubscription( set( 0 ), set(), set() );

    assertEquals( world.getSubscriptions().size(), 1 );

    final AreaOfInterest areaOfInterest = new AreaOfInterest( set( 0 ), set(), set() );
    assertEquals( world.getSubscriptions().get( areaOfInterest ), subscription );

    assertEquals( subscription.getWorld(), world );
    assertEquals( subscription.getAreaOfInterest(), areaOfInterest );
    assertEquals( subscription.getEntities().cardinality(), 0 );
    assertEquals( subscription.getNewEntities().cardinality(), 0 );
    assertEquals( subscription.getCurrentEntityId(), -1 );
    assertTrue( subscription.isNotDisposed() );
    assertFalse( subscription.isDisposed() );
    assertFalse( subscription.isProcessingNewEntities() );
    assertFalse( subscription.hasNewEntities() );
  }

  @Test
  public void ensureNotDisposed()
  {
    final World world = Worlds.world().build();

    final Subscription subscription = world.createSubscription( set(), set(), set() );

    subscription.ensureNotDisposed();

    subscription.markAsDisposed();

    assertInvariantFailure( subscription::ensureNotDisposed,
                            "Galdr-0015: Invoked method on Subscription when subscription is disposed." );
  }

  @Test
  public void ensureCurrentWorldMatches()
  {
    final World world1 = Worlds.world().build();
    final World world2 = Worlds.world().build();

    final Subscription subscription = world1.createSubscription( set(), set(), set() );

    run( world1, subscription::ensureCurrentWorldMatches );

    assertInvariantFailure( () -> run( world2, subscription::ensureCurrentWorldMatches ),
                            "Galdr-0036: Subscription method invoked in the context of the world 'World@2' but the subscription belongs to the world 'World@1'" );
  }

  @Test
  public void createAndDisposeEntityThatMatchesSubscription()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription = world.createSubscription( set( 0 ), set(), set() );

    assertEquals( subscription.getEntities().cardinality(), 0 );

    final int entityId = run( world, () -> world.createEntity( set( 0 ) ) );

    assertEquals( subscription.getEntities().cardinality(), 1 );
    assertTrue( subscription.getEntities().get( entityId ) );

    run( world, () -> world.disposeEntity( entityId ) );

    assertEquals( subscription.getEntities().cardinality(), 0 );
  }

  @Test
  public void entityAdd_badEntity()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription = world.createSubscription( set( 0 ), set(), set() );

    final int entityId = run( world, () -> world.createEntity( set() ) );
    final Entity entity = world.getEntityManager().getEntityById( entityId );
    run( world, () -> world.disposeEntity( entityId ) );

    assertInvariantFailure( () -> run( world, () -> subscription.entityAdd( entity ) ),
                            "Galdr-0018: Invoked Subscription.entityAdd with invalid Entity." );
  }

  @Test
  public void entityRemove_badEntity()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription = world.createSubscription( set( 0 ), set(), set() );

    final int entityId = run( world, () -> world.createEntity( set() ) );
    final Entity entity = world.getEntityManager().getEntityById( entityId );
    run( world, () -> world.disposeEntity( entityId ) );

    assertInvariantFailure( () -> run( world, () -> subscription.entityRemove( entity ) ),
                            "Galdr-0018: Invoked Subscription.entityRemove with invalid Entity." );
  }

  @Test
  public void componentChangeThatMatchesSubscription()
  {
    final World world = Worlds.world().component( Component1.class ).build();

    final Subscription subscription = world.createSubscription( set( 0 ), set(), set() );

    assertEquals( subscription.getEntities().cardinality(), 0 );

    final int entityId = run( world, () -> world.createEntity( set() ) );

    assertEquals( subscription.getEntities().cardinality(), 0 );

    run( world, () -> world.getComponentByType( Component1.class ).allocate( entityId ) );

    assertEquals( subscription.getEntities().cardinality(), 1 );
    assertTrue( subscription.getEntities().get( entityId ) );

    run( world, () -> world.getComponentByType( Component1.class ).remove( entityId ) );

    assertEquals( subscription.getEntities().cardinality(), 0 );
  }

  @Test
  public void componentChange_badEntity()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription = world.createSubscription( set( 0 ), set(), set() );

    final int entityId = run( world, () -> world.createEntity( set() ) );
    final Entity entity = world.getEntityManager().getEntityById( entityId );
    run( world, () -> world.disposeEntity( entityId ) );

    assertInvariantFailure( () -> run( world, () -> subscription.componentChange( entity ) ),
                            "Galdr-0018: Invoked Subscription.componentChange with invalid Entity." );
  }

  @Test
  public void iterateOverEmptySubscription()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription = world.createSubscription( set( 0 ), set(), set() );

    final Object owner = new Object();

    assertNull( subscription.getOwner() );

    run( world, () -> subscription.startIteration( owner ) );

    assertSubscriptionIterationStart( subscription, owner );

    // There is no entities so should immediately complete iteration
    assertNextEntity( world, subscription, owner, -1 );

    // Verify we are complete
    assertSubscriptionComplete( subscription );
  }

  @SuppressWarnings( "unused" )
  @Test
  public void iterateOverSubscriptionContainingSubsetOfEntitiesNoModifications()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription = world.createSubscription( set( 0 ), set(), set() );

    final int entityId0 = run( world, () -> world.createEntity( set( 0 ) ) );
    final int entityId1 = run( world, () -> world.createEntity( set( 0 ) ) );
    final int entityId2 = run( world, () -> world.createEntity( set() ) );
    final int entityId3 = run( world, () -> world.createEntity( set() ) );
    final int entityId4 = run( world, () -> world.createEntity( set( 0 ) ) );
    final int entityId5 = run( world, () -> world.createEntity( set() ) );
    final int entityId6 = run( world, () -> world.createEntity( set() ) );
    final int entityId7 = run( world, () -> world.createEntity( set( 0 ) ) );
    final int entityId8 = run( world, () -> world.createEntity( set() ) );
    final int entityId9 = run( world, () -> world.createEntity( set( 0 ) ) );

    assertEquals( entityId0, 0 );
    assertEquals( entityId9, 9 );

    final Object owner = new Object();

    assertNull( subscription.getOwner() );

    run( world, () -> subscription.startIteration( owner ) );

    assertSubscriptionIterationStart( subscription, owner );

    assertNextEntity( world, subscription, owner, entityId0 );
    assertNextEntity( world, subscription, owner, entityId1 );
    assertNextEntity( world, subscription, owner, entityId4 );
    assertNextEntity( world, subscription, owner, entityId7 );
    assertNextEntity( world, subscription, owner, entityId9 );
    assertNextEntity( world, subscription, owner, -1 );

    // Verify we are complete
    assertSubscriptionComplete( subscription );
  }

  @SuppressWarnings( "unused" )
  @Test
  public void iterateOverSubscriptionContainingSubsetOfEntitiesRemoveEntityLaterInIteration()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final ComponentAPI<Component1> componentApi = world.getComponentByType( Component1.class );
    final Subscription subscription = world.createSubscription( set( 0 ), set(), set() );

    final int entityId0 = run( world, () -> world.createEntity( set( 0 ) ) );
    final int entityId1 = run( world, () -> world.createEntity( set( 0 ) ) );
    final int entityId2 = run( world, () -> world.createEntity( set() ) );
    final int entityId3 = run( world, () -> world.createEntity( set() ) );
    final int entityId4 = run( world, () -> world.createEntity( set( 0 ) ) );
    final int entityId5 = run( world, () -> world.createEntity( set() ) );
    final int entityId6 = run( world, () -> world.createEntity( set() ) );
    final int entityId7 = run( world, () -> world.createEntity( set( 0 ) ) );
    final int entityId8 = run( world, () -> world.createEntity( set() ) );
    final int entityId9 = run( world, () -> world.createEntity( set( 0 ) ) );

    assertEquals( entityId0, 0 );
    assertEquals( entityId9, 9 );

    final Object owner = new Object();

    assertNull( subscription.getOwner() );

    run( world, () -> subscription.startIteration( owner ) );

    assertSubscriptionIterationStart( subscription, owner );

    assertNextEntity( world, subscription, owner, entityId0 );

    // Not part of the subscription
    run( world, () -> world.disposeEntity( entityId3 ) );

    // Part of the subscription
    run( world, () -> world.disposeEntity( entityId4 ) );

    // Not part of the subscription
    run( world, () -> world.disposeEntity( entityId8 ) );

    run( world, () -> componentApi.remove( entityId7 ) );

    assertNextEntity( world, subscription, owner, entityId1 );
    assertNextEntity( world, subscription, owner, entityId9 );
    assertNextEntity( world, subscription, owner, -1 );

    // Verify we are complete
    assertSubscriptionComplete( subscription );
  }

  @SuppressWarnings( "unused" )
  @Test
  public void iterateOverSubscriptionContainingSubsetOfEntitiesRemoveEntityEarlierInIteration()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription = world.createSubscription( set( 0 ), set(), set() );

    final int entityId0 = run( world, () -> world.createEntity( set( 0 ) ) );
    final int entityId1 = run( world, () -> world.createEntity( set( 0 ) ) );
    final int entityId2 = run( world, () -> world.createEntity( set() ) );
    final int entityId3 = run( world, () -> world.createEntity( set() ) );
    final int entityId4 = run( world, () -> world.createEntity( set( 0 ) ) );
    final int entityId5 = run( world, () -> world.createEntity( set() ) );
    final int entityId6 = run( world, () -> world.createEntity( set() ) );
    final int entityId7 = run( world, () -> world.createEntity( set( 0 ) ) );
    final int entityId8 = run( world, () -> world.createEntity( set() ) );
    final int entityId9 = run( world, () -> world.createEntity( set( 0 ) ) );

    assertEquals( entityId0, 0 );
    assertEquals( entityId9, 9 );

    final Object owner = new Object();

    assertNull( subscription.getOwner() );

    run( world, () -> subscription.startIteration( owner ) );

    assertSubscriptionIterationStart( subscription, owner );

    assertNextEntity( world, subscription, owner, entityId0 );
    assertNextEntity( world, subscription, owner, entityId1 );
    assertNextEntity( world, subscription, owner, entityId4 );
    assertNextEntity( world, subscription, owner, entityId7 );

    // Not part of the subscription
    run( world, () -> world.disposeEntity( 3 ) );

    // Part of the subscription
    run( world, () -> world.disposeEntity( 4 ) );

    // Not part of the subscription
    run( world, () -> world.disposeEntity( 8 ) );

    assertNextEntity( world, subscription, owner, entityId9 );
    assertNextEntity( world, subscription, owner, -1 );

    // Verify we are complete
    assertSubscriptionComplete( subscription );
  }

  @SuppressWarnings( "unused" )
  @Test
  public void iterateOverSubscriptionContainingSubsetOfEntitiesAddEntityLaterInIteration()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final ComponentAPI<Component1> componentApi = world.getComponentByType( Component1.class );
    final Subscription subscription = world.createSubscription( set( 0 ), set(), set() );

    final int entityId0 = run( world, () -> world.createEntity( set( 0 ) ) );
    final int entityId1 = run( world, () -> world.createEntity( set( 0 ) ) );
    final int entityId2 = run( world, () -> world.createEntity( set() ) );
    final int entityId3 = run( world, () -> world.createEntity( set() ) );
    final int entityId4 = run( world, () -> world.createEntity( set( 0 ) ) );
    final int entityId5 = run( world, () -> world.createEntity( set() ) );
    final int entityId6 = run( world, () -> world.createEntity( set() ) );
    final int entityId7 = run( world, () -> world.createEntity( set( 0 ) ) );
    final int entityId8 = run( world, () -> world.createEntity( set() ) );
    final int entityId9 = run( world, () -> world.createEntity( set( 0 ) ) );

    final Object owner = new Object();

    assertNull( subscription.getOwner() );

    run( world, () -> subscription.startIteration( owner ) );

    assertSubscriptionIterationStart( subscription, owner );

    assertNextEntity( world, subscription, owner, entityId0 );

    assertEquals( subscription.getNewEntities().cardinality(), 0 );
    assertFalse( subscription.hasNewEntities() );

    // Not part of the subscription before allocate
    run( world, () -> componentApi.allocate( entityId3 ) );

    // Not part of the subscription before allocate
    run( world, () -> componentApi.allocate( entityId8 ) );

    assertEquals( subscription.getNewEntities().cardinality(), 0 );
    assertFalse( subscription.hasNewEntities() );

    assertNextEntity( world, subscription, owner, entityId1 );
    assertNextEntity( world, subscription, owner, entityId3 );
    assertNextEntity( world, subscription, owner, entityId4 );
    assertNextEntity( world, subscription, owner, entityId7 );
    assertNextEntity( world, subscription, owner, entityId8 );
    assertNextEntity( world, subscription, owner, entityId9 );
    assertNextEntity( world, subscription, owner, -1 );

    // Verify we are complete
    assertSubscriptionComplete( subscription );
  }

  @SuppressWarnings( "unused" )
  @Test
  public void iterateOverSubscriptionContainingSubsetOfEntitiesAddEntityEarlierInIteration()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final ComponentAPI<Component1> componentApi = world.getComponentByType( Component1.class );
    final Subscription subscription = world.createSubscription( set( 0 ), set(), set() );

    final int entityId0 = run( world, () -> world.createEntity( set( 0 ) ) );
    final int entityId1 = run( world, () -> world.createEntity( set( 0 ) ) );
    final int entityId2 = run( world, () -> world.createEntity( set() ) );
    final int entityId3 = run( world, () -> world.createEntity( set() ) );
    final int entityId4 = run( world, () -> world.createEntity( set( 0 ) ) );
    final int entityId5 = run( world, () -> world.createEntity( set() ) );
    final int entityId6 = run( world, () -> world.createEntity( set() ) );
    final int entityId7 = run( world, () -> world.createEntity( set( 0 ) ) );
    final int entityId8 = run( world, () -> world.createEntity( set() ) );
    final int entityId9 = run( world, () -> world.createEntity( set( 0 ) ) );

    final Object owner = new Object();

    assertNull( subscription.getOwner() );

    run( world, () -> subscription.startIteration( owner ) );

    assertSubscriptionIterationStart( subscription, owner );

    assertNextEntity( world, subscription, owner, entityId0 );
    assertNextEntity( world, subscription, owner, entityId1 );
    assertNextEntity( world, subscription, owner, entityId4 );
    assertNextEntity( world, subscription, owner, entityId7 );

    assertEquals( subscription.getNewEntities().cardinality(), 0 );
    assertFalse( subscription.hasNewEntities() );

    // Not part of the subscription before allocate
    run( world, () -> componentApi.allocate( entityId5 ) );

    // Not part of the subscription before allocate
    run( world, () -> componentApi.allocate( entityId3 ) );

    assertEquals( subscription.getNewEntities().cardinality(), 2 );
    assertTrue( subscription.hasNewEntities() );

    assertNextEntity( world, subscription, owner, entityId9 );

    // This wraps around to the NewEntities list
    assertNextEntity( world, subscription, owner, entityId3 );

    assertEquals( subscription.getNewEntities().cardinality(), 1 );

    assertNextEntity( world, subscription, owner, entityId5 );

    assertEquals( subscription.getNewEntities().cardinality(), 0 );

    assertNextEntity( world, subscription, owner, -1 );

    // Verify we are complete
    assertSubscriptionComplete( subscription );
  }

  @SuppressWarnings( "unused" )
  @Test
  public void iterateOverSubscriptionWithMultiplePassesOverNewEntities()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final ComponentAPI<Component1> componentApi = world.getComponentByType( Component1.class );
    final Subscription subscription = world.createSubscription( set( 0 ), set(), set() );

    final int entityId0 = run( world, () -> world.createEntity( set( 0 ) ) );
    final int entityId1 = run( world, () -> world.createEntity( set() ) );
    final int entityId2 = run( world, () -> world.createEntity( set() ) );
    final int entityId3 = run( world, () -> world.createEntity( set( 0 ) ) );

    final Object owner = new Object();

    assertNull( subscription.getOwner() );

    run( world, () -> subscription.startIteration( owner ) );

    assertSubscriptionIterationStart( subscription, owner );

    assertNextEntity( world, subscription, owner, entityId0 );
    assertNextEntity( world, subscription, owner, entityId3 );

    assertEquals( subscription.getNewEntities().cardinality(), 0 );
    assertFalse( subscription.hasNewEntities() );

    // Not part of the subscription before allocate
    run( world, () -> componentApi.allocate( entityId2 ) );

    assertEquals( subscription.getNewEntities().cardinality(), 1 );
    assertTrue( subscription.hasNewEntities() );

    // This wraps around to the NewEntities list
    assertNextEntity( world, subscription, owner, entityId2 );

    assertEquals( subscription.getNewEntities().cardinality(), 0 );
    assertTrue( subscription.hasNewEntities() );

    // Not part of the subscription before allocate
    // This will cause another wrap around
    run( world, () -> componentApi.allocate( entityId1 ) );

    // This wraps around again
    assertNextEntity( world, subscription, owner, entityId1 );

    assertEquals( subscription.getNewEntities().cardinality(), 0 );

    assertNextEntity( world, subscription, owner, -1 );

    // Verify we are complete
    assertSubscriptionComplete( subscription );
  }

  @SuppressWarnings( "unused" )
  @Test
  public void iterateWhereEntityAddedAndRemovedFromNewEntitiesPriorToVisiting()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final ComponentAPI<Component1> componentApi = world.getComponentByType( Component1.class );
    final Subscription subscription = world.createSubscription( set( 0 ), set(), set() );

    final int entityId0 = run( world, () -> world.createEntity( set( 0 ) ) );
    final int entityId1 = run( world, () -> world.createEntity( set() ) );
    final int entityId2 = run( world, () -> world.createEntity( set() ) );
    final int entityId3 = run( world, () -> world.createEntity( set() ) );
    final int entityId4 = run( world, () -> world.createEntity( set( 0 ) ) );

    final Object owner = new Object();

    assertNull( subscription.getOwner() );

    run( world, () -> subscription.startIteration( owner ) );

    assertSubscriptionIterationStart( subscription, owner );

    assertNextEntity( world, subscription, owner, entityId0 );
    assertNextEntity( world, subscription, owner, entityId4 );

    assertEquals( subscription.getNewEntities().cardinality(), 0 );
    assertFalse( subscription.hasNewEntities() );

    // Add to New Entities
    run( world, () -> componentApi.allocate( entityId1 ) );
    run( world, () -> componentApi.allocate( entityId2 ) );

    assertEquals( subscription.getNewEntities().cardinality(), 2 );
    assertTrue( subscription.hasNewEntities() );

    // Remove from New Entities
    run( world, () -> componentApi.remove( entityId1 ) );

    assertEquals( subscription.getNewEntities().cardinality(), 1 );
    assertTrue( subscription.hasNewEntities() );

    // This wraps around to the NewEntities list
    assertNextEntity( world, subscription, owner, entityId2 );
    assertEquals( subscription.getNewEntities().cardinality(), 0 );

    assertNextEntity( world, subscription, owner, -1 );

    // Verify we are complete
    assertSubscriptionComplete( subscription );
  }

  @SuppressWarnings( "unused" )
  @Test
  public void explicitCompleteOfIterationBeforeAllEntitiesProcessed()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription = world.createSubscription( set( 0 ), set(), set() );

    final int entityId0 = run( world, () -> world.createEntity( set( 0 ) ) );
    final int entityId1 = run( world, () -> world.createEntity( set( 0 ) ) );

    final Object owner = new Object();

    assertNull( subscription.getOwner() );

    run( world, () -> subscription.startIteration( owner ) );

    assertSubscriptionIterationStart( subscription, owner );

    assertNextEntity( world, subscription, owner, entityId0 );

    // Explicit complete of iteration before all entities processed
    run( world, () -> subscription.completeIteration( owner ) );

    // Verify we are complete
    assertSubscriptionComplete( subscription );
  }

  @SuppressWarnings( "unused" )
  @Test
  public void explicitCompleteWhenProcessingNewEntitiesList()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final ComponentAPI<Component1> componentApi = world.getComponentByType( Component1.class );
    final Subscription subscription = world.createSubscription( set( 0 ), set(), set() );

    final int entityId0 = run( world, () -> world.createEntity( set() ) );
    final int entityId1 = run( world, () -> world.createEntity( set() ) );
    final int entityId2 = run( world, () -> world.createEntity( set( 0 ) ) );

    final Object owner = new Object();

    assertNull( subscription.getOwner() );

    run( world, () -> subscription.startIteration( owner ) );

    assertSubscriptionIterationStart( subscription, owner );

    assertNextEntity( world, subscription, owner, entityId2 );

    // Add to New Entities to subscription
    run( world, () -> componentApi.allocate( entityId1 ) );
    run( world, () -> componentApi.allocate( entityId0 ) );

    assertEquals( subscription.getNewEntities().cardinality(), 2 );
    assertTrue( subscription.hasNewEntities() );

    assertNextEntity( world, subscription, owner, entityId0 );

    assertEquals( subscription.getNewEntities().cardinality(), 1 );
    assertTrue( subscription.hasNewEntities() );

    // Explicit complete of iteration before all entities processed
    run( world, () -> subscription.completeIteration( owner ) );

    // Verify we are complete
    assertSubscriptionComplete( subscription );
  }

  @SuppressWarnings( "unused" )
  @Test
  public void startIteration_badOwner()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription = world.createSubscription( set( 0 ), set(), set() );

    final Object owner1 = new Object();
    final Object owner2 = new Object();

    run( world, () -> subscription.startIteration( owner1 ) );

    assertInvariantFailure( () -> run( world, () -> subscription.startIteration( owner2 ) ),
                            "Galdr-0022: Subscription.startIteration() invoked with owner '" +
                            owner2 + "' but an existing iteration is in progress with owner '" + owner1 + "'." );
  }

  @Test
  public void startIteration_whenIterationInProgress()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription = world.createSubscription( set( 0 ), set(), set() );

    final int entityId0 = run( world, () -> world.createEntity( set( 0 ) ) );

    final Object owner = new Object();

    run( world, () -> subscription.startIteration( owner ) );

    assertNextEntity( world, subscription, owner, entityId0 );

    assertInvariantFailure( () -> run( world, () -> subscription.startIteration( owner ) ),
                            "Galdr-0032: Subscription.startIteration() invoked when _currentEntityId has not been reset. Current value 0" );
  }

  private void assertSubscriptionIterationStart( @Nonnull final Subscription subscription, @Nonnull final Object owner )
  {
    assertTrue( subscription.isIterationInProgress() );
    assertEquals( subscription.getCurrentEntityId(), -1 );
    assertEquals( subscription.getOwner(), owner );
  }

  private void assertSubscriptionComplete( @Nonnull final Subscription subscription )
  {
    assertFalse( subscription.isIterationInProgress() );
    assertEquals( subscription.getCurrentEntityId(), -1 );
    assertNull( subscription.getOwner() );
    assertEquals( subscription.getNewEntities().cardinality(), 0 );
    assertFalse( subscription.hasNewEntities() );
    assertFalse( subscription.isProcessingNewEntities() );
  }

  private void assertNextEntity( @Nonnull final World world,
                                 @Nonnull final Subscription subscription,
                                 @Nonnull final Object owner,
                                 final int entityId )
  {
    run( world, () -> assertEquals( subscription.nextEntity( owner ), entityId ) );
    assertEquals( subscription.getCurrentEntityId(), entityId );
  }
}
