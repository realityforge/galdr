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
    final AreaOfInterest areaOfInterest = new AreaOfInterest( set( 0 ), set(), set() );

    assertEquals( world.getSubscriptions().size(), 0 );
    final Subscription subscription = world.createSubscription( areaOfInterest );

    assertEquals( world.getSubscriptions().size(), 1 );
    assertEquals( world.getSubscriptions().get( areaOfInterest ), subscription );

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

    final Subscription subscription = world.createSubscription( new AreaOfInterest( set(), set(), set() ) );

    subscription.ensureNotDisposed();

    subscription.markAsDisposed();

    assertInvariantFailure( subscription::ensureNotDisposed,
                            "Galdr-0015: Invoked method on Subscription when subscription is disposed." );
  }

  @Test
  public void createAndDisposeEntityThatMatchesSubscription()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final AreaOfInterest areaOfInterest = new AreaOfInterest( set( 0 ), set(), set() );

    final Subscription subscription = world.createSubscription( areaOfInterest );

    assertEquals( subscription.getEntities().cardinality(), 0 );

    final int entityId = world.createEntity( set( 0 ) );

    assertEquals( subscription.getEntities().cardinality(), 1 );
    assertTrue( subscription.getEntities().get( entityId ) );

    world.run( () -> world.disposeEntity( entityId ) );

    assertEquals( subscription.getEntities().cardinality(), 0 );
  }

  @Test
  public void entityAdd_badEntity()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final AreaOfInterest areaOfInterest = new AreaOfInterest( set( 0 ), set(), set() );

    final Subscription subscription = world.createSubscription( areaOfInterest );

    final int entityId = world.createEntity( set() );
    final Entity entity = world.getEntityManager().getEntityById( entityId );
    world.run( () -> world.disposeEntity( entityId ) );

    assertInvariantFailure( () -> subscription.entityAdd( entity ),
                            "Galdr-0018: Invoked Subscription.entityAdd with invalid Entity." );
  }

  @Test
  public void entityRemove_badEntity()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final AreaOfInterest areaOfInterest = new AreaOfInterest( set( 0 ), set(), set() );

    final Subscription subscription = world.createSubscription( areaOfInterest );

    final int entityId = world.createEntity( set() );
    final Entity entity = world.getEntityManager().getEntityById( entityId );
    world.run( () -> world.disposeEntity( entityId ) );

    assertInvariantFailure( () -> subscription.entityRemove( entity ),
                            "Galdr-0018: Invoked Subscription.entityRemove with invalid Entity." );
  }

  @Test
  public void componentChangeThatMatchesSubscription()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final AreaOfInterest areaOfInterest = new AreaOfInterest( set( 0 ), set(), set() );

    final Subscription subscription = world.createSubscription( areaOfInterest );

    assertEquals( subscription.getEntities().cardinality(), 0 );

    final int entityId = world.createEntity( set() );

    assertEquals( subscription.getEntities().cardinality(), 0 );

    world.getComponentByType( Component1.class ).allocate( entityId );

    assertEquals( subscription.getEntities().cardinality(), 1 );
    assertTrue( subscription.getEntities().get( entityId ) );

    world.getComponentByType( Component1.class ).remove( entityId );

    assertEquals( subscription.getEntities().cardinality(), 0 );
  }

  @Test
  public void componentChange_badEntity()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final AreaOfInterest areaOfInterest = new AreaOfInterest( set( 0 ), set(), set() );

    final Subscription subscription = world.createSubscription( areaOfInterest );

    final int entityId = world.createEntity( set() );
    final Entity entity = world.getEntityManager().getEntityById( entityId );
    world.run( () -> world.disposeEntity( entityId ) );

    assertInvariantFailure( () -> subscription.componentChange( entity ),
                            "Galdr-0018: Invoked Subscription.componentChange with invalid Entity." );
  }

  @Test
  public void iterateOverEmptySubscription()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription = world.createSubscription( new AreaOfInterest( set( 0 ), set(), set() ) );

    final Object owner = new Object();

    assertNull( subscription.getOwner() );

    subscription.startIteration( owner );

    assertTrue( subscription.isIterationInProgress() );
    assertEquals( subscription.getCurrentEntityId(), -1 );
    assertEquals( subscription.getOwner(), owner );

    // There is no entities so should immediately complete iteration
    assertEquals( subscription.nextEntity( owner ), -1 );

    // Verify we are complete
    assertSubscriptionComplete( subscription );
  }

  @SuppressWarnings( "unused" )
  @Test
  public void iterateOverSubscriptionContainingSubsetOfEntitiesNoModifications()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription = world.createSubscription( new AreaOfInterest( set( 0 ), set(), set() ) );

    final int entityId0 = world.createEntity( set( 0 ) );
    final int entityId1 = world.createEntity( set( 0 ) );
    final int entityId2 = world.createEntity( set() );
    final int entityId3 = world.createEntity( set() );
    final int entityId4 = world.createEntity( set( 0 ) );
    final int entityId5 = world.createEntity( set() );
    final int entityId6 = world.createEntity( set() );
    final int entityId7 = world.createEntity( set( 0 ) );
    final int entityId8 = world.createEntity( set() );
    final int entityId9 = world.createEntity( set( 0 ) );

    assertEquals( entityId0, 0 );
    assertEquals( entityId9, 9 );

    final Object owner = new Object();

    assertNull( subscription.getOwner() );

    subscription.startIteration( owner );

    assertTrue( subscription.isIterationInProgress() );
    assertEquals( subscription.getCurrentEntityId(), -1 );
    assertEquals( subscription.getOwner(), owner );

    assertEquals( subscription.nextEntity( owner ), entityId0 );
    assertEquals( subscription.nextEntity( owner ), entityId1 );
    assertEquals( subscription.nextEntity( owner ), entityId4 );
    assertEquals( subscription.nextEntity( owner ), entityId7 );
    assertEquals( subscription.nextEntity( owner ), entityId9 );
    assertEquals( subscription.nextEntity( owner ), -1 );

    // Verify we are complete
    assertSubscriptionComplete( subscription );
  }

  @SuppressWarnings( "unused" )
  @Test
  public void iterateOverSubscriptionContainingSubsetOfEntitiesRemoveEntityLaterInIteration()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription = world.createSubscription( new AreaOfInterest( set( 0 ), set(), set() ) );

    final int entityId0 = world.createEntity( set( 0 ) );
    final int entityId1 = world.createEntity( set( 0 ) );
    final int entityId2 = world.createEntity( set() );
    final int entityId3 = world.createEntity( set() );
    final int entityId4 = world.createEntity( set( 0 ) );
    final int entityId5 = world.createEntity( set() );
    final int entityId6 = world.createEntity( set() );
    final int entityId7 = world.createEntity( set( 0 ) );
    final int entityId8 = world.createEntity( set() );
    final int entityId9 = world.createEntity( set( 0 ) );

    assertEquals( entityId0, 0 );
    assertEquals( entityId9, 9 );

    final Object owner = new Object();

    assertNull( subscription.getOwner() );

    subscription.startIteration( owner );

    assertTrue( subscription.isIterationInProgress() );
    assertEquals( subscription.getCurrentEntityId(), -1 );
    assertEquals( subscription.getOwner(), owner );

    assertEquals( subscription.nextEntity( owner ), entityId0 );

    // Not part of the subscription
    world.run( () -> world.disposeEntity( entityId3 ) );

    // Part of the subscription
    world.run( () -> world.disposeEntity( entityId4 ) );

    // Not part of the subscription
    world.run( () -> world.disposeEntity( entityId8 ) );

    world.getComponentByType( Component1.class ).remove( entityId7 );

    assertEquals( subscription.nextEntity( owner ), entityId1 );
    assertEquals( subscription.nextEntity( owner ), entityId9 );
    assertEquals( subscription.nextEntity( owner ), -1 );

    // Verify we are complete
    assertSubscriptionComplete( subscription );
  }

  @SuppressWarnings( "unused" )
  @Test
  public void iterateOverSubscriptionContainingSubsetOfEntitiesRemoveEntityEarlierInIteration()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription = world.createSubscription( new AreaOfInterest( set( 0 ), set(), set() ) );

    final int entityId0 = world.createEntity( set( 0 ) );
    final int entityId1 = world.createEntity( set( 0 ) );
    final int entityId2 = world.createEntity( set() );
    final int entityId3 = world.createEntity( set() );
    final int entityId4 = world.createEntity( set( 0 ) );
    final int entityId5 = world.createEntity( set() );
    final int entityId6 = world.createEntity( set() );
    final int entityId7 = world.createEntity( set( 0 ) );
    final int entityId8 = world.createEntity( set() );
    final int entityId9 = world.createEntity( set( 0 ) );

    assertEquals( entityId0, 0 );
    assertEquals( entityId9, 9 );

    final Object owner = new Object();

    assertNull( subscription.getOwner() );

    subscription.startIteration( owner );

    assertTrue( subscription.isIterationInProgress() );
    assertEquals( subscription.getCurrentEntityId(), -1 );
    assertEquals( subscription.getOwner(), owner );

    assertEquals( subscription.nextEntity( owner ), entityId0 );
    assertEquals( subscription.nextEntity( owner ), entityId1 );
    assertEquals( subscription.nextEntity( owner ), entityId4 );
    assertEquals( subscription.nextEntity( owner ), entityId7 );

    // Not part of the subscription
    world.run( () -> world.disposeEntity( 3 ) );

    // Part of the subscription
    world.run( () -> world.disposeEntity( 4 ) );

    // Not part of the subscription
    world.run( () -> world.disposeEntity( 8 ) );

    assertEquals( subscription.nextEntity( owner ), entityId9 );
    assertEquals( subscription.nextEntity( owner ), -1 );

    // Verify we are complete
    assertSubscriptionComplete( subscription );
  }

  @SuppressWarnings( "unused" )
  @Test
  public void iterateOverSubscriptionContainingSubsetOfEntitiesAddEntityLaterInIteration()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription = world.createSubscription( new AreaOfInterest( set( 0 ), set(), set() ) );

    final int entityId0 = world.createEntity( set( 0 ) );
    final int entityId1 = world.createEntity( set( 0 ) );
    final int entityId2 = world.createEntity( set() );
    final int entityId3 = world.createEntity( set() );
    final int entityId4 = world.createEntity( set( 0 ) );
    final int entityId5 = world.createEntity( set() );
    final int entityId6 = world.createEntity( set() );
    final int entityId7 = world.createEntity( set( 0 ) );
    final int entityId8 = world.createEntity( set() );
    final int entityId9 = world.createEntity( set( 0 ) );

    final Object owner = new Object();

    assertNull( subscription.getOwner() );

    subscription.startIteration( owner );

    assertTrue( subscription.isIterationInProgress() );
    assertEquals( subscription.getCurrentEntityId(), -1 );
    assertEquals( subscription.getOwner(), owner );

    assertEquals( subscription.nextEntity( owner ), entityId0 );

    assertEquals( subscription.getNewEntities().cardinality(), 0 );
    assertFalse( subscription.hasNewEntities() );

    // Not part of the subscription before allocate
    world.getComponentByType( Component1.class ).allocate( entityId3 );

    // Not part of the subscription before allocate
    world.getComponentByType( Component1.class ).allocate( entityId8 );

    assertEquals( subscription.getNewEntities().cardinality(), 0 );
    assertFalse( subscription.hasNewEntities() );

    assertEquals( subscription.nextEntity( owner ), entityId1 );
    assertEquals( subscription.nextEntity( owner ), entityId3 );
    assertEquals( subscription.nextEntity( owner ), entityId4 );
    assertEquals( subscription.nextEntity( owner ), entityId7 );
    assertEquals( subscription.nextEntity( owner ), entityId8 );
    assertEquals( subscription.nextEntity( owner ), entityId9 );
    assertEquals( subscription.nextEntity( owner ), -1 );

    // Verify we are complete
    assertSubscriptionComplete( subscription );
  }

  @SuppressWarnings( "unused" )
  @Test
  public void iterateOverSubscriptionContainingSubsetOfEntitiesAddEntityEarlierInIteration()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription = world.createSubscription( new AreaOfInterest( set( 0 ), set(), set() ) );

    final int entityId0 = world.createEntity( set( 0 ) );
    final int entityId1 = world.createEntity( set( 0 ) );
    final int entityId2 = world.createEntity( set() );
    final int entityId3 = world.createEntity( set() );
    final int entityId4 = world.createEntity( set( 0 ) );
    final int entityId5 = world.createEntity( set() );
    final int entityId6 = world.createEntity( set() );
    final int entityId7 = world.createEntity( set( 0 ) );
    final int entityId8 = world.createEntity( set() );
    final int entityId9 = world.createEntity( set( 0 ) );

    final Object owner = new Object();

    assertNull( subscription.getOwner() );

    subscription.startIteration( owner );

    assertTrue( subscription.isIterationInProgress() );
    assertEquals( subscription.getCurrentEntityId(), -1 );
    assertEquals( subscription.getOwner(), owner );

    assertEquals( subscription.nextEntity( owner ), entityId0 );
    assertEquals( subscription.getCurrentEntityId(), entityId0 );

    assertEquals( subscription.nextEntity( owner ), entityId1 );
    assertEquals( subscription.nextEntity( owner ), entityId4 );
    assertEquals( subscription.nextEntity( owner ), entityId7 );

    assertEquals( subscription.getNewEntities().cardinality(), 0 );
    assertFalse( subscription.hasNewEntities() );

    // Not part of the subscription before allocate
    world.getComponentByType( Component1.class ).allocate( entityId5 );

    // Not part of the subscription before allocate
    world.getComponentByType( Component1.class ).allocate( entityId3 );

    assertEquals( subscription.getNewEntities().cardinality(), 2 );
    assertTrue( subscription.hasNewEntities() );

    assertEquals( subscription.nextEntity( owner ), entityId9 );

    // This wraps around to the NewEntities list
    assertEquals( subscription.nextEntity( owner ), entityId3 );

    assertEquals( subscription.getNewEntities().cardinality(), 1 );

    assertEquals( subscription.nextEntity( owner ), entityId5 );

    assertEquals( subscription.getNewEntities().cardinality(), 0 );

    assertEquals( subscription.nextEntity( owner ), -1 );

    // Verify we are complete
    assertSubscriptionComplete( subscription );
  }

  @SuppressWarnings( "unused" )
  @Test
  public void iterateOverSubscriptionWithMultiplePassesOverNewEntities()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription = world.createSubscription( new AreaOfInterest( set( 0 ), set(), set() ) );

    final int entityId0 = world.createEntity( set( 0 ) );
    final int entityId1 = world.createEntity( set() );
    final int entityId2 = world.createEntity( set() );
    final int entityId3 = world.createEntity( set( 0 ) );

    final Object owner = new Object();

    assertNull( subscription.getOwner() );

    subscription.startIteration( owner );

    assertTrue( subscription.isIterationInProgress() );
    assertEquals( subscription.getCurrentEntityId(), -1 );
    assertEquals( subscription.getOwner(), owner );

    assertEquals( subscription.nextEntity( owner ), entityId0 );
    assertEquals( subscription.getCurrentEntityId(), entityId0 );

    assertEquals( subscription.nextEntity( owner ), entityId3 );

    assertEquals( subscription.getNewEntities().cardinality(), 0 );
    assertFalse( subscription.hasNewEntities() );

    // Not part of the subscription before allocate
    world.getComponentByType( Component1.class ).allocate( entityId2 );

    assertEquals( subscription.getNewEntities().cardinality(), 1 );
    assertTrue( subscription.hasNewEntities() );

    // This wraps around to the NewEntities list
    assertEquals( subscription.nextEntity( owner ), entityId2 );

    assertEquals( subscription.getNewEntities().cardinality(), 0 );
    assertTrue( subscription.hasNewEntities() );

    // Not part of the subscription before allocate
    // This will cause another wrap around
    world.getComponentByType( Component1.class ).allocate( entityId1 );

    // This wraps around again
    assertEquals( subscription.nextEntity( owner ), entityId1 );

    assertEquals( subscription.getNewEntities().cardinality(), 0 );

    assertEquals( subscription.nextEntity( owner ), -1 );

    // Verify we are complete
    assertSubscriptionComplete( subscription );
  }

  @SuppressWarnings( "unused" )
  @Test
  public void iterateWhereEntityAddedAndRemovedFromNewEntitiesPriorToVisiting()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription = world.createSubscription( new AreaOfInterest( set( 0 ), set(), set() ) );

    final int entityId0 = world.createEntity( set( 0 ) );
    final int entityId1 = world.createEntity( set() );
    final int entityId2 = world.createEntity( set() );
    final int entityId3 = world.createEntity( set() );
    final int entityId4 = world.createEntity( set( 0 ) );

    final Object owner = new Object();

    assertNull( subscription.getOwner() );

    subscription.startIteration( owner );

    assertTrue( subscription.isIterationInProgress() );
    assertEquals( subscription.getCurrentEntityId(), -1 );
    assertEquals( subscription.getOwner(), owner );

    assertEquals( subscription.nextEntity( owner ), entityId0 );
    assertEquals( subscription.getCurrentEntityId(), entityId0 );

    assertEquals( subscription.nextEntity( owner ), entityId4 );

    assertEquals( subscription.getNewEntities().cardinality(), 0 );
    assertFalse( subscription.hasNewEntities() );

    // Add to New Entities
    world.getComponentByType( Component1.class ).allocate( entityId1 );
    world.getComponentByType( Component1.class ).allocate( entityId2 );

    assertEquals( subscription.getNewEntities().cardinality(), 2 );
    assertTrue( subscription.hasNewEntities() );

    // Remove from New Entities
    world.getComponentByType( Component1.class ).remove( entityId1 );

    assertEquals( subscription.getNewEntities().cardinality(), 1 );
    assertTrue( subscription.hasNewEntities() );

    // This wraps around to the NewEntities list
    assertEquals( subscription.nextEntity( owner ), entityId2 );
    assertEquals( subscription.getNewEntities().cardinality(), 0 );

    assertEquals( subscription.nextEntity( owner ), -1 );

    // Verify we are complete
    assertSubscriptionComplete( subscription );
  }

  @SuppressWarnings( "unused" )
  @Test
  public void explicitCompleteOfIterationBeforeAllEntitiesProcessed()
  {
    final World world = Worlds.world().component( Component1.class ).build();
    final Subscription subscription = world.createSubscription( new AreaOfInterest( set( 0 ), set(), set() ) );

    final int entityId0 = world.createEntity( set( 0 ) );
    final int entityId1 = world.createEntity( set( 0 ) );

    final Object owner = new Object();

    assertNull( subscription.getOwner() );

    subscription.startIteration( owner );

    assertTrue( subscription.isIterationInProgress() );
    assertEquals( subscription.getCurrentEntityId(), -1 );
    assertEquals( subscription.getOwner(), owner );

    assertEquals( subscription.nextEntity( owner ), entityId0 );
    assertEquals( subscription.getCurrentEntityId(), entityId0 );

    // Explicit complete of iteration before all entities processed
    subscription.completeIteration( owner );

    // Verify we are complete
    assertSubscriptionComplete( subscription );
  }

  private void assertSubscriptionComplete( @Nonnull final Subscription subscription )
  {
    assertFalse( subscription.isIterationInProgress() );
    assertEquals( subscription.getCurrentEntityId(), -1 );
    assertNull( subscription.getOwner() );
    assertFalse( subscription.hasNewEntities() );
    assertFalse( subscription.isProcessingNewEntities() );
  }
}
