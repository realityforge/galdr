package galdr;

import galdr.spy.EntityRemoveCompleteEvent;
import galdr.spy.EntityRemoveStartEvent;
import galdr.spy.LinkAddCompleteEvent;
import galdr.spy.LinkAddStartEvent;
import galdr.spy.LinkRemoveCompleteEvent;
import galdr.spy.LinkRemoveStartEvent;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class LinkTest
  extends AbstractTest
{
  @Test
  public void link()
  {
    final World world = Worlds.world().build();

    final int sourceEntityId = createEntity( world, set() );
    final int targetEntityId = createEntity( world, set() );

    assertLinkCount( world, sourceEntityId, 0, 0 );
    assertLinkCount( world, targetEntityId, 0, 0 );

    final Link link = link( world, sourceEntityId, targetEntityId, false, false );

    assertLinkShape( link, sourceEntityId, targetEntityId, false, false );

    assertLinkCount( world, sourceEntityId, 0, 1 );
    assertLinkCount( world, targetEntityId, 1, 0 );
  }

  @Test
  public void link_withSpyEnabled()
  {
    final World world = Worlds.world().build();

    final int sourceEntityId = createEntity( world, set() );
    final int targetEntityId = createEntity( world, set() );

    assertLinkCount( world, sourceEntityId, 0, 0 );
    assertLinkCount( world, targetEntityId, 0, 0 );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( world );
    final Link link = link( world, sourceEntityId, targetEntityId, false, false );
    handler.unsubscribe();

    assertLinkShape( link, sourceEntityId, targetEntityId, false, false );

    assertLinkCount( world, sourceEntityId, 0, 1 );
    assertLinkCount( world, targetEntityId, 1, 0 );

    handler.assertEventCount( 2 );
    handler.assertNextEvent( LinkAddStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getSourceEntityId(), sourceEntityId );
      assertEquals( e.getTargetEntityId(), targetEntityId );
    } );
    handler.assertNextEvent( LinkAddCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getSourceEntityId(), sourceEntityId );
      assertEquals( e.getTargetEntityId(), targetEntityId );
    } );
  }

  @Test
  public void link_butSourceEntityNotAlive()
  {
    final World world = Worlds.world().build();

    final int sourceEntityId = createEntity( world, set() );
    final int targetEntityId = createEntity( world, set() );

    run( world, () -> world.disposeEntity( sourceEntityId ) );

    assertInvariantFailure( () -> link( world, sourceEntityId, targetEntityId, false, false ),
                            "Galdr-0011: Attempted to link from entity 0 to entity 1 but the source entity is not alive." );

    assertLinkCount( world, sourceEntityId, 0, 0 );
    assertLinkCount( world, targetEntityId, 0, 0 );
  }

  @Test
  public void link_butTargetEntityNotAlive()
  {
    final World world = Worlds.world().build();

    final int sourceEntityId = createEntity( world, set() );
    final int targetEntityId = createEntity( world, set() );

    run( world, () -> world.disposeEntity( targetEntityId ) );

    assertInvariantFailure( () -> link( world, sourceEntityId, targetEntityId, false, false ),
                            "Galdr-0010: Attempted to link from entity 0 to entity 1 but the target entity is not alive." );

    assertLinkCount( world, sourceEntityId, 0, 0 );
    assertLinkCount( world, targetEntityId, 0, 0 );
  }

  @Test
  public void link_self()
  {
    final World world = Worlds.world().build();

    final int entityId = createEntity( world, set() );

    assertInvariantFailure( () -> link( world, entityId, entityId, false, false ),
                            "Galdr-0110: Attempted to link entity 0 to itself." );

    assertLinkCount( world, entityId, 0, 0 );
  }

  @Test
  public void toString_test()
  {
    final World world = Worlds.world().build();

    final int sourceEntityId = createEntity( world, set() );
    final int targetEntityId = createEntity( world, set() );

    final Link link = link( world, sourceEntityId, targetEntityId, false, false );

    assertEquals( link.toString(), "Link[0->1]" );

    run( world, link::dispose );

    assertEquals( link.toString(), "Link[(disposed)]" );

    GaldrTestUtil.disableDebugToString();

    assertDefaultToString( link );
  }

  @Test
  public void createLinkWithNoCascadeAndDisposeSource()
  {
    final World world = Worlds.world().build();

    final int sourceEntityId = createEntity( world, set() );
    final int targetEntityId = createEntity( world, set() );

    assertLinkCount( world, sourceEntityId, 0, 0 );
    assertLinkCount( world, targetEntityId, 0, 0 );

    final Link link = link( world, sourceEntityId, targetEntityId, false, false );

    assertLinkShape( link, sourceEntityId, targetEntityId, false, false );
    assertLinkCount( world, sourceEntityId, 0, 1 );
    assertLinkCount( world, targetEntityId, 1, 0 );

    run( world, () -> world.disposeEntity( sourceEntityId ) );

    assertFalse( link.isValid() );

    assertFalse( isAlive( world, sourceEntityId ) );
    assertTrue( isAlive( world, targetEntityId ) );

    assertLinkCount( world, sourceEntityId, 0, 0 );
    assertLinkCount( world, targetEntityId, 0, 0 );
  }

  @Test
  public void createLinkWithNoCascadeAndDisposeSource_withSpyEnabled()
  {
    final World world = Worlds.world().build();
    final int sourceEntityId = createEntity( world, set() );
    final int targetEntityId = createEntity( world, set() );

    assertLinkCount( world, sourceEntityId, 0, 0 );
    assertLinkCount( world, targetEntityId, 0, 0 );

    final Link link = link( world, sourceEntityId, targetEntityId, false, false );

    assertLinkShape( link, sourceEntityId, targetEntityId, false, false );
    assertLinkCount( world, sourceEntityId, 0, 1 );
    assertLinkCount( world, targetEntityId, 1, 0 );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( world );
    run( world, () -> world.disposeEntity( sourceEntityId ) );
    handler.unsubscribe();

    assertFalse( link.isValid() );

    assertFalse( isAlive( world, sourceEntityId ) );
    assertTrue( isAlive( world, targetEntityId ) );

    assertLinkCount( world, sourceEntityId, 0, 0 );
    assertLinkCount( world, targetEntityId, 0, 0 );

    handler.assertEventCount( 4 );
    handler.assertNextEvent( EntityRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), sourceEntityId );
    } );
    handler.assertNextEvent( LinkRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getSourceEntityId(), sourceEntityId );
      assertEquals( e.getTargetEntityId(), targetEntityId );
    } );
    handler.assertNextEvent( LinkRemoveCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getSourceEntityId(), sourceEntityId );
      assertEquals( e.getTargetEntityId(), targetEntityId );
    } );
    handler.assertNextEvent( EntityRemoveCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), sourceEntityId );
    } );
  }

  @Test
  public void createLinkWithNoCascadeAndDisposeTarget()
  {
    final World world = Worlds.world().build();
    final int sourceEntityId = createEntity( world, set() );
    final int targetEntityId = createEntity( world, set() );

    assertLinkCount( world, sourceEntityId, 0, 0 );
    assertLinkCount( world, targetEntityId, 0, 0 );

    final Link link = link( world, sourceEntityId, targetEntityId, false, false );

    assertLinkShape( link, sourceEntityId, targetEntityId, false, false );

    assertLinkCount( world, sourceEntityId, 0, 1 );
    assertLinkCount( world, targetEntityId, 1, 0 );

    run( world, () -> world.disposeEntity( targetEntityId ) );

    assertFalse( link.isValid() );

    assertTrue( isAlive( world, sourceEntityId ) );
    assertFalse( isAlive( world, targetEntityId ) );

    assertLinkCount( world, sourceEntityId, 0, 0 );
    assertLinkCount( world, targetEntityId, 0, 0 );
  }

  @Test
  public void createLinkWithNoCascadeAndDisposeTarget_withSpyEnabled()
  {
    final World world = Worlds.world().build();
    final int sourceEntityId = createEntity( world, set() );
    final int targetEntityId = createEntity( world, set() );

    assertLinkCount( world, sourceEntityId, 0, 0 );
    assertLinkCount( world, targetEntityId, 0, 0 );

    final Link link = link( world, sourceEntityId, targetEntityId, false, false );

    assertLinkShape( link, sourceEntityId, targetEntityId, false, false );

    assertLinkCount( world, sourceEntityId, 0, 1 );
    assertLinkCount( world, targetEntityId, 1, 0 );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( world );
    run( world, () -> world.disposeEntity( targetEntityId ) );
    handler.unsubscribe();

    assertFalse( link.isValid() );

    assertTrue( isAlive( world, sourceEntityId ) );
    assertFalse( isAlive( world, targetEntityId ) );

    assertLinkCount( world, sourceEntityId, 0, 0 );
    assertLinkCount( world, targetEntityId, 0, 0 );

    handler.assertEventCount( 4 );
    handler.assertNextEvent( EntityRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), targetEntityId );
    } );
    handler.assertNextEvent( LinkRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getSourceEntityId(), sourceEntityId );
      assertEquals( e.getTargetEntityId(), targetEntityId );
    } );
    handler.assertNextEvent( LinkRemoveCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getSourceEntityId(), sourceEntityId );
      assertEquals( e.getTargetEntityId(), targetEntityId );
    } );
    handler.assertNextEvent( EntityRemoveCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), targetEntityId );
    } );
  }

  @Test
  public void createLinkWithCascadeSourceRemoveAndDisposeSource()
  {
    final World world = Worlds.world().build();
    final int sourceEntityId = createEntity( world, set() );
    final int targetEntityId = createEntity( world, set() );

    assertLinkCount( world, sourceEntityId, 0, 0 );
    assertLinkCount( world, targetEntityId, 0, 0 );

    final Link link = link( world, sourceEntityId, targetEntityId, true, false );

    assertLinkShape( link, sourceEntityId, targetEntityId, true, false );
    assertLinkCount( world, sourceEntityId, 0, 1 );
    assertLinkCount( world, targetEntityId, 1, 0 );

    run( world, () -> world.disposeEntity( sourceEntityId ) );

    assertFalse( link.isValid() );

    assertFalse( isAlive( world, sourceEntityId ) );
    assertFalse( isAlive( world, targetEntityId ) );

    assertLinkCount( world, sourceEntityId, 0, 0 );
    assertLinkCount( world, targetEntityId, 0, 0 );
  }

  @Test
  public void createLinkWithCascadeSourceRemoveAndDisposeSource_withSpyEnabled()
  {
    final World world = Worlds.world().build();
    final int sourceEntityId = createEntity( world, set() );
    final int targetEntityId = createEntity( world, set() );

    assertLinkCount( world, sourceEntityId, 0, 0 );
    assertLinkCount( world, targetEntityId, 0, 0 );

    final Link link = link( world, sourceEntityId, targetEntityId, true, false );

    assertLinkShape( link, sourceEntityId, targetEntityId, true, false );
    assertLinkCount( world, sourceEntityId, 0, 1 );
    assertLinkCount( world, targetEntityId, 1, 0 );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( world );
    run( world, () -> world.disposeEntity( sourceEntityId ) );
    handler.unsubscribe();

    assertFalse( link.isValid() );

    assertFalse( isAlive( world, sourceEntityId ) );
    assertFalse( isAlive( world, targetEntityId ) );

    assertLinkCount( world, sourceEntityId, 0, 0 );
    assertLinkCount( world, targetEntityId, 0, 0 );

    handler.assertEventCount( 6 );
    handler.assertNextEvent( EntityRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), sourceEntityId );
    } );
    handler.assertNextEvent( LinkRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getSourceEntityId(), sourceEntityId );
      assertEquals( e.getTargetEntityId(), targetEntityId );
    } );
    handler.assertNextEvent( EntityRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), targetEntityId );
    } );
    handler.assertNextEvent( EntityRemoveCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), targetEntityId );
    } );
    handler.assertNextEvent( LinkRemoveCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getSourceEntityId(), sourceEntityId );
      assertEquals( e.getTargetEntityId(), targetEntityId );
    } );
    handler.assertNextEvent( EntityRemoveCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), sourceEntityId );
    } );
  }

  @Test
  public void createLinkWithCascadeSourceRemoveAndDisposeTarget()
  {
    final World world = Worlds.world().build();
    final int sourceEntityId = createEntity( world, set() );
    final int targetEntityId = createEntity( world, set() );

    assertLinkCount( world, sourceEntityId, 0, 0 );
    assertLinkCount( world, targetEntityId, 0, 0 );

    final Link link = link( world, sourceEntityId, targetEntityId, true, false );

    assertLinkShape( link, sourceEntityId, targetEntityId, true, false );
    assertLinkCount( world, sourceEntityId, 0, 1 );
    assertLinkCount( world, targetEntityId, 1, 0 );

    run( world, () -> world.disposeEntity( targetEntityId ) );

    assertFalse( link.isValid() );

    assertTrue( isAlive( world, sourceEntityId ) );
    assertFalse( isAlive( world, targetEntityId ) );

    assertLinkCount( world, sourceEntityId, 0, 0 );
    assertLinkCount( world, targetEntityId, 0, 0 );
  }

  @Test
  public void createLinkWithCascadeSourceRemoveAndDisposeTarget_withSpyEnabled()
  {
    final World world = Worlds.world().build();
    final int sourceEntityId = createEntity( world, set() );
    final int targetEntityId = createEntity( world, set() );

    assertLinkCount( world, sourceEntityId, 0, 0 );
    assertLinkCount( world, targetEntityId, 0, 0 );

    final Link link = link( world, sourceEntityId, targetEntityId, true, false );

    assertLinkShape( link, sourceEntityId, targetEntityId, true, false );
    assertLinkCount( world, sourceEntityId, 0, 1 );
    assertLinkCount( world, targetEntityId, 1, 0 );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( world );
    run( world, () -> world.disposeEntity( targetEntityId ) );
    handler.unsubscribe();

    assertFalse( link.isValid() );

    assertTrue( isAlive( world, sourceEntityId ) );
    assertFalse( isAlive( world, targetEntityId ) );

    assertLinkCount( world, sourceEntityId, 0, 0 );
    assertLinkCount( world, targetEntityId, 0, 0 );

    handler.assertEventCount( 4 );
    handler.assertNextEvent( EntityRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), targetEntityId );
    } );
    handler.assertNextEvent( LinkRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getSourceEntityId(), sourceEntityId );
      assertEquals( e.getTargetEntityId(), targetEntityId );
    } );
    handler.assertNextEvent( LinkRemoveCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getSourceEntityId(), sourceEntityId );
      assertEquals( e.getTargetEntityId(), targetEntityId );
    } );
    handler.assertNextEvent( EntityRemoveCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), targetEntityId );
    } );
  }

  @Test
  public void createLinkWithCascadeTargetRemoveAndDisposeSource()
  {
    final World world = Worlds.world().build();
    final int sourceEntityId = createEntity( world, set() );
    final int targetEntityId = createEntity( world, set() );

    assertLinkCount( world, sourceEntityId, 0, 0 );
    assertLinkCount( world, targetEntityId, 0, 0 );

    final Link link = link( world, sourceEntityId, targetEntityId, false, true );

    assertLinkShape( link, sourceEntityId, targetEntityId, false, true );
    assertLinkCount( world, sourceEntityId, 0, 1 );
    assertLinkCount( world, targetEntityId, 1, 0 );

    run( world, () -> world.disposeEntity( sourceEntityId ) );

    assertFalse( link.isValid() );

    assertFalse( isAlive( world, sourceEntityId ) );
    assertTrue( isAlive( world, targetEntityId ) );

    assertLinkCount( world, sourceEntityId, 0, 0 );
    assertLinkCount( world, targetEntityId, 0, 0 );
  }

  @Test
  public void createLinkWithCascadeTargetRemoveAndDisposeSource_withSpyEnabled()
  {
    final World world = Worlds.world().build();
    final int sourceEntityId = createEntity( world, set() );
    final int targetEntityId = createEntity( world, set() );

    assertLinkCount( world, sourceEntityId, 0, 0 );
    assertLinkCount( world, targetEntityId, 0, 0 );

    final Link link = link( world, sourceEntityId, targetEntityId, false, true );

    assertLinkShape( link, sourceEntityId, targetEntityId, false, true );
    assertLinkCount( world, sourceEntityId, 0, 1 );
    assertLinkCount( world, targetEntityId, 1, 0 );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( world );
    run( world, () -> world.disposeEntity( sourceEntityId ) );
    handler.unsubscribe();

    assertFalse( link.isValid() );

    assertFalse( isAlive( world, sourceEntityId ) );
    assertTrue( isAlive( world, targetEntityId ) );

    assertLinkCount( world, sourceEntityId, 0, 0 );
    assertLinkCount( world, targetEntityId, 0, 0 );

    handler.assertEventCount( 4 );
    handler.assertNextEvent( EntityRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), sourceEntityId );
    } );
    handler.assertNextEvent( LinkRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getSourceEntityId(), sourceEntityId );
      assertEquals( e.getTargetEntityId(), targetEntityId );
    } );
    handler.assertNextEvent( LinkRemoveCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getSourceEntityId(), sourceEntityId );
      assertEquals( e.getTargetEntityId(), targetEntityId );
    } );
    handler.assertNextEvent( EntityRemoveCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), sourceEntityId );
    } );
  }

  @Test
  public void createLinkWithCascadeTargetRemoveAndDisposeTarget()
  {
    final World world = Worlds.world().build();
    final int sourceEntityId = createEntity( world, set() );
    final int targetEntityId = createEntity( world, set() );

    assertLinkCount( world, sourceEntityId, 0, 0 );
    assertLinkCount( world, targetEntityId, 0, 0 );

    final Link link = link( world, sourceEntityId, targetEntityId, false, true );

    assertLinkShape( link, sourceEntityId, targetEntityId, false, true );
    assertLinkCount( world, sourceEntityId, 0, 1 );
    assertLinkCount( world, targetEntityId, 1, 0 );

    run( world, () -> world.disposeEntity( targetEntityId ) );

    assertFalse( link.isValid() );

    assertFalse( isAlive( world, sourceEntityId ) );
    assertFalse( isAlive( world, targetEntityId ) );

    assertLinkCount( world, sourceEntityId, 0, 0 );
    assertLinkCount( world, targetEntityId, 0, 0 );
  }

  @Test
  public void createLinkWithCascadeTargetRemoveAndDisposeTarget_withSpyEnabled()
  {
    final World world = Worlds.world().build();
    final int sourceEntityId = createEntity( world, set() );
    final int targetEntityId = createEntity( world, set() );

    assertLinkCount( world, sourceEntityId, 0, 0 );
    assertLinkCount( world, targetEntityId, 0, 0 );

    final Link link = link( world, sourceEntityId, targetEntityId, false, true );

    assertLinkShape( link, sourceEntityId, targetEntityId, false, true );
    assertLinkCount( world, sourceEntityId, 0, 1 );
    assertLinkCount( world, targetEntityId, 1, 0 );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( world );
    run( world, () -> world.disposeEntity( targetEntityId ) );
    handler.unsubscribe();

    assertFalse( link.isValid() );

    assertFalse( isAlive( world, sourceEntityId ) );
    assertFalse( isAlive( world, targetEntityId ) );

    assertLinkCount( world, sourceEntityId, 0, 0 );
    assertLinkCount( world, targetEntityId, 0, 0 );

    handler.assertEventCount( 6 );
    handler.assertNextEvent( EntityRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), targetEntityId );
    } );
    handler.assertNextEvent( LinkRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getSourceEntityId(), sourceEntityId );
      assertEquals( e.getTargetEntityId(), targetEntityId );
    } );
    handler.assertNextEvent( EntityRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), sourceEntityId );
    } );
    handler.assertNextEvent( EntityRemoveCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), sourceEntityId );
    } );
    handler.assertNextEvent( LinkRemoveCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getSourceEntityId(), sourceEntityId );
      assertEquals( e.getTargetEntityId(), targetEntityId );
    } );
    handler.assertNextEvent( EntityRemoveCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), targetEntityId );
    } );
  }

  @Test
  public void dispose()
  {
    final World world = Worlds.world().build();
    final int sourceEntityId = createEntity( world, set() );
    final int targetEntityId = createEntity( world, set() );

    assertLinkCount( world, sourceEntityId, 0, 0 );
    assertLinkCount( world, targetEntityId, 0, 0 );

    final Link link = link( world, sourceEntityId, targetEntityId, false, true );

    assertLinkShape( link, sourceEntityId, targetEntityId, false, true );
    assertLinkCount( world, sourceEntityId, 0, 1 );
    assertLinkCount( world, targetEntityId, 1, 0 );

    run( world, link::dispose );

    assertFalse( link.isValid() );

    assertLinkCount( world, sourceEntityId, 0, 0 );
    assertLinkCount( world, targetEntityId, 0, 0 );

    assertTrue( isAlive( world, sourceEntityId ) );
    assertTrue( isAlive( world, targetEntityId ) );
  }

  @Test
  public void dispose_withSpyEnabled()
  {
    final World world = Worlds.world().build();
    final int sourceEntityId = createEntity( world, set() );
    final int targetEntityId = createEntity( world, set() );

    assertLinkCount( world, sourceEntityId, 0, 0 );
    assertLinkCount( world, targetEntityId, 0, 0 );

    final Link link = link( world, sourceEntityId, targetEntityId, false, true );

    assertLinkShape( link, sourceEntityId, targetEntityId, false, true );
    assertLinkCount( world, sourceEntityId, 0, 1 );
    assertLinkCount( world, targetEntityId, 1, 0 );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( world );
    run( world, link::dispose );
    handler.unsubscribe();

    assertFalse( link.isValid() );

    assertLinkCount( world, sourceEntityId, 0, 0 );
    assertLinkCount( world, targetEntityId, 0, 0 );

    assertTrue( isAlive( world, sourceEntityId ) );
    assertTrue( isAlive( world, targetEntityId ) );

    handler.assertEventCount( 2 );
    handler.assertNextEvent( LinkRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getSourceEntityId(), sourceEntityId );
      assertEquals( e.getTargetEntityId(), targetEntityId );
    } );
  }

  @Test
  public void dispose_disposedLink()
  {
    final World world = Worlds.world().build();
    final int sourceEntityId = createEntity( world, set() );
    final int targetEntityId = createEntity( world, set() );

    final Link link = link( world, sourceEntityId, targetEntityId, false, false );
    run( world, link::dispose );

    assertInvariantFailure( link::dispose, "Galdr-0117: Link.dispose() method invoked on invalid link." );
  }

  @Test
  public void getSourceEntity_disposedLink()
  {
    final World world = Worlds.world().build();
    final int sourceEntityId = createEntity( world, set() );
    final int targetEntityId = createEntity( world, set() );

    final Link link = link( world, sourceEntityId, targetEntityId, false, false );
    run( world, link::dispose );

    assertInvariantFailure( link::getSourceEntity,
                            "Galdr-0007: The Link.getSourceEntity() method invoked on invalid link." );
  }

  @Test
  public void getTargetEntity_disposedLink()
  {
    final World world = Worlds.world().build();
    final int sourceEntityId = createEntity( world, set() );
    final int targetEntityId = createEntity( world, set() );

    final Link link = link( world, sourceEntityId, targetEntityId, false, false );
    run( world, link::dispose );

    assertInvariantFailure( link::getTargetEntity,
                            "Galdr-0016: The Link.getTargetEntity() method invoked on invalid link." );
  }

  @Test
  public void Entity_linkOutgoing_entityNotSource()
  {
    final World world = Worlds.world().build();

    final int entityId1 = createEntity( world, set() );
    final int entityId2 = createEntity( world, set() );
    final int entityId3 = createEntity( world, set() );

    final Link link = link( world, entityId1, entityId2, false, false );

    final Entity entity3 = world.getEntityManager().unsafeGetEntityById( entityId3 );
    assertInvariantFailure( () -> run( world, () -> entity3.linkOutgoing( link ) ),
                            "Galdr-0809: Entity.linkOutgoing() on entity 2 but entity is not the source of the link." );
  }

  @Test
  public void Entity_linkIncoming_entityNotTarget()
  {
    final World world = Worlds.world().build();

    final int entityId1 = createEntity( world, set() );
    final int entityId2 = createEntity( world, set() );
    final int entityId3 = createEntity( world, set() );

    final Link link = link( world, entityId1, entityId2, false, false );

    final Entity entity3 = world.getEntityManager().unsafeGetEntityById( entityId3 );
    assertInvariantFailure( () -> run( world, () -> entity3.linkIncoming( link ) ),
                            "Galdr-0808: Entity.linkIncoming() on entity 2 but entity is not the target of the link." );
  }

  @Test
  public void Entity_unlinkIncoming_noLinks()
  {
    final World world = Worlds.world().build();

    final int entityId1 = createEntity( world, set() );
    final int entityId2 = createEntity( world, set() );
    final int entityId3 = createEntity( world, set() );

    final Link link = link( world, entityId1, entityId2, false, false );

    final Entity entity3 = world.getEntityManager().unsafeGetEntityById( entityId3 );
    assertInvariantFailure( () -> run( world, () -> entity3.unlinkIncoming( link ) ),
                            "Galdr-0008: Attempted to unlink incoming link Link[0->1] but entity 2 has no incoming links." );
  }

  @Test
  public void Entity_unlinkIncoming_noMatchingLink()
  {
    final World world = Worlds.world().build();

    final int entityId1 = createEntity( world, set() );
    final int entityId2 = createEntity( world, set() );
    final int entityId3 = createEntity( world, set() );

    final Link link1 = link( world, entityId1, entityId2, false, false );
    link( world, entityId1, entityId3, false, false );

    final Entity entity3 = world.getEntityManager().unsafeGetEntityById( entityId3 );
    assertInvariantFailure( () -> run( world, () -> entity3.unlinkIncoming( link1 ) ),
                            "Galdr-0012: Invoked Entity.unlinkIncoming with link Link[0->1] but entity 2 has no such incoming link." );
  }

  @Test
  public void Entity_unlinkOutgoing_noLinks()
  {
    final World world = Worlds.world().build();

    final int entityId1 = createEntity( world, set() );
    final int entityId2 = createEntity( world, set() );
    final int entityId3 = createEntity( world, set() );

    final Link link = link( world, entityId1, entityId2, false, false );

    final Entity entity3 = world.getEntityManager().unsafeGetEntityById( entityId3 );
    assertInvariantFailure( () -> run( world, () -> entity3.unlinkOutgoing( link ) ),
                            "Galdr-0038: Attempted to unlink outgoing link Link[0->1] but entity 2 has no outgoing links." );
  }

  @Test
  public void Entity_unlinkOutgoing_noMatchingLink()
  {
    final World world = Worlds.world().build();

    final int entityId1 = createEntity( world, set() );
    final int entityId2 = createEntity( world, set() );
    final int entityId3 = createEntity( world, set() );

    final Link link1 = link( world, entityId1, entityId2, false, false );
    link( world, entityId3, entityId2, false, false );

    final Entity entity3 = world.getEntityManager().unsafeGetEntityById( entityId3 );
    assertInvariantFailure( () -> run( world, () -> entity3.unlinkOutgoing( link1 ) ),
                            "Galdr-0039: Invoked Entity.unlinkOutgoing with link Link[0->1] but entity 2 has no such outgoing link." );
  }

  @Nonnull
  private Link link( @Nonnull final World world,
                     final int sourceEntityId,
                     final int targetEntityId,
                     final boolean cascadeSourceRemoveToTarget,
                     final boolean cascadeTargetRemoveToSource )
  {
    return run( world,
                () -> world.link( sourceEntityId,
                                  targetEntityId,
                                  cascadeSourceRemoveToTarget,
                                  cascadeTargetRemoveToSource ) );
  }

  private void assertLinkShape( @Nonnull final Link link,
                                final int sourceEntityId,
                                final int targetEntityId,
                                final boolean cascadeSourceRemoveToTarget,
                                final boolean cascadeTargetRemoveToSource )
  {
    assertEquals( link.getId(), targetEntityId );
    assertEquals( link.getSourceEntity().getId(), sourceEntityId );
    assertEquals( link.getTargetEntity().getId(), targetEntityId );
    assertEquals( link.shouldCascadeSourceRemoveToTarget(), cascadeSourceRemoveToTarget );
    assertEquals( link.shouldCascadeTargetRemoveToSource(), cascadeTargetRemoveToSource );
    assertTrue( link.isValid() );
  }

  private void assertLinkCount( @Nonnull final World world, final int entityId, final int in, final int out )
  {
    assertLinkCount( run( world, () -> world.getEntityManager().unsafeGetEntityById( entityId ) ), in, out );
  }

  private void assertLinkCount( @Nonnull final Entity entity, final int in, final int out )
  {
    assertEquals( entity.getInwardLinks().size(), in );
    assertEquals( entity.getOutwardLinks().size(), out );
  }
}
