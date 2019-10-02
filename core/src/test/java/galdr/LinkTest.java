package galdr;

import galdr.spy.EntityRemoveCompleteEvent;
import galdr.spy.EntityRemoveStartEvent;
import galdr.spy.LinkRemoveCompleteEvent;
import galdr.spy.LinkRemoveStartEvent;
import java.util.BitSet;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class LinkTest
  extends AbstractTest
{
  @Test
  public void toString_test()
  {
    final World world = Worlds.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity source = em.createEntity( new BitSet() );
    final Entity target = em.createEntity( new BitSet() );

    final Link link = run( world, () -> em.link( source, target, false, false ) );

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

    final EntityManager em = world.getEntityManager();

    final Entity source = em.createEntity( new BitSet() );
    final Entity target = em.createEntity( new BitSet() );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );

    final Link link = run( world, () -> em.link( source, target, false, false ) );

    assertLinkShape( link, source, target, false, false );
    assertLinkCount( source, 0, 1 );
    assertLinkCount( target, 1, 0 );

    run( world, () -> em.disposeEntity( source.getId() ) );

    assertFalse( link.isValid() );

    assertFalse( em.isAlive( source.getId() ) );
    assertTrue( em.isAlive( target.getId() ) );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );
  }

  @Test
  public void createLinkWithNoCascadeAndDisposeSource_withSpyEnabled()
  {
    final World world = Worlds.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity source = em.createEntity( new BitSet() );
    final Entity target = em.createEntity( new BitSet() );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );

    final Link link = run( world, () -> em.link( source, target, false, false ) );

    assertLinkShape( link, source, target, false, false );
    assertLinkCount( source, 0, 1 );
    assertLinkCount( target, 1, 0 );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( world );
    run( world, () -> em.disposeEntity( source.getId() ) );
    handler.unsubscribe();

    assertFalse( link.isValid() );

    assertFalse( em.isAlive( source.getId() ) );
    assertTrue( em.isAlive( target.getId() ) );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );

    handler.assertEventCount( 4 );
    handler.assertNextEvent( EntityRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), source.getId() );
    } );
    handler.assertNextEvent( LinkRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getSourceEntityId(), source.getId() );
      assertEquals( e.getTargetEntityId(), target.getId() );
    } );
    handler.assertNextEvent( LinkRemoveCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getSourceEntityId(), source.getId() );
      assertEquals( e.getTargetEntityId(), target.getId() );
    } );
    handler.assertNextEvent( EntityRemoveCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), source.getId() );
    } );
  }

  @Test
  public void createLinkWithNoCascadeAndDisposeTarget()
  {
    final World world = Worlds.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity source = em.createEntity( new BitSet() );
    final Entity target = em.createEntity( new BitSet() );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );

    final Link link = run( world, () -> em.link( source, target, false, false ) );

    assertLinkShape( link, source, target, false, false );

    assertLinkCount( source, 0, 1 );
    assertLinkCount( target, 1, 0 );

    run( world, () -> em.disposeEntity( target.getId() ) );

    assertFalse( link.isValid() );

    assertTrue( em.isAlive( source.getId() ) );
    assertFalse( em.isAlive( target.getId() ) );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );
  }

  @Test
  public void createLinkWithNoCascadeAndDisposeTarget_withSpyEnabled()
  {
    final World world = Worlds.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity source = em.createEntity( new BitSet() );
    final Entity target = em.createEntity( new BitSet() );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );

    final Link link = run( world, () -> em.link( source, target, false, false ) );

    assertLinkShape( link, source, target, false, false );

    assertLinkCount( source, 0, 1 );
    assertLinkCount( target, 1, 0 );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( world );
    run( world, () -> em.disposeEntity( target.getId() ) );
    handler.unsubscribe();

    assertFalse( link.isValid() );

    assertTrue( em.isAlive( source.getId() ) );
    assertFalse( em.isAlive( target.getId() ) );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );

    handler.assertEventCount( 4 );
    handler.assertNextEvent( EntityRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), target.getId() );
    } );
    handler.assertNextEvent( LinkRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getSourceEntityId(), source.getId() );
      assertEquals( e.getTargetEntityId(), target.getId() );
    } );
    handler.assertNextEvent( LinkRemoveCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getSourceEntityId(), source.getId() );
      assertEquals( e.getTargetEntityId(), target.getId() );
    } );
    handler.assertNextEvent( EntityRemoveCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), target.getId() );
    } );
  }

  @Test
  public void createLinkWithCascadeSourceRemoveAndDisposeSource()
  {
    final World world = Worlds.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity source = em.createEntity( new BitSet() );
    final Entity target = em.createEntity( new BitSet() );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );

    final Link link = run( world, () -> em.link( source, target, true, false ) );

    assertLinkShape( link, source, target, true, false );
    assertLinkCount( source, 0, 1 );
    assertLinkCount( target, 1, 0 );

    run( world, () -> em.disposeEntity( source.getId() ) );

    assertFalse( link.isValid() );

    assertFalse( em.isAlive( source.getId() ) );
    assertFalse( em.isAlive( target.getId() ) );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );
  }

  @Test
  public void createLinkWithCascadeSourceRemoveAndDisposeSource_withSpyEnabled()
  {
    final World world = Worlds.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity source = em.createEntity( new BitSet() );
    final Entity target = em.createEntity( new BitSet() );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );

    final Link link = run( world, () -> em.link( source, target, true, false ) );

    assertLinkShape( link, source, target, true, false );
    assertLinkCount( source, 0, 1 );
    assertLinkCount( target, 1, 0 );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( world );
    run( world, () -> em.disposeEntity( source.getId() ) );
    handler.unsubscribe();

    assertFalse( link.isValid() );

    assertFalse( em.isAlive( source.getId() ) );
    assertFalse( em.isAlive( target.getId() ) );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );

    handler.assertEventCount( 6 );
    handler.assertNextEvent( EntityRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), source.getId() );
    } );
    handler.assertNextEvent( LinkRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getSourceEntityId(), source.getId() );
      assertEquals( e.getTargetEntityId(), target.getId() );
    } );
    handler.assertNextEvent( EntityRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), target.getId() );
    } );
    handler.assertNextEvent( EntityRemoveCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), target.getId() );
    } );
    handler.assertNextEvent( LinkRemoveCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getSourceEntityId(), source.getId() );
      assertEquals( e.getTargetEntityId(), target.getId() );
    } );
    handler.assertNextEvent( EntityRemoveCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), source.getId() );
    } );
  }

  @Test
  public void createLinkWithCascadeSourceRemoveAndDisposeTarget()
  {
    final World world = Worlds.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity source = em.createEntity( new BitSet() );
    final Entity target = em.createEntity( new BitSet() );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );

    final Link link = run( world, () -> em.link( source, target, true, false ) );

    assertLinkShape( link, source, target, true, false );
    assertLinkCount( source, 0, 1 );
    assertLinkCount( target, 1, 0 );

    run( world, () -> em.disposeEntity( target.getId() ) );

    assertFalse( link.isValid() );

    assertTrue( em.isAlive( source.getId() ) );
    assertFalse( em.isAlive( target.getId() ) );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );
  }

  @Test
  public void createLinkWithCascadeSourceRemoveAndDisposeTarget_withSpyEnabled()
  {
    final World world = Worlds.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity source = em.createEntity( new BitSet() );
    final Entity target = em.createEntity( new BitSet() );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );

    final Link link = run( world, () -> em.link( source, target, true, false ) );

    assertLinkShape( link, source, target, true, false );
    assertLinkCount( source, 0, 1 );
    assertLinkCount( target, 1, 0 );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( world );
    run( world, () -> em.disposeEntity( target.getId() ) );
    handler.unsubscribe();

    assertFalse( link.isValid() );

    assertTrue( em.isAlive( source.getId() ) );
    assertFalse( em.isAlive( target.getId() ) );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );

    handler.assertEventCount( 4 );
    handler.assertNextEvent( EntityRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), target.getId() );
    } );
    handler.assertNextEvent( LinkRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getSourceEntityId(), source.getId() );
      assertEquals( e.getTargetEntityId(), target.getId() );
    } );
    handler.assertNextEvent( LinkRemoveCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getSourceEntityId(), source.getId() );
      assertEquals( e.getTargetEntityId(), target.getId() );
    } );
    handler.assertNextEvent( EntityRemoveCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), target.getId() );
    } );
  }

  @Test
  public void createLinkWithCascadeTargetRemoveAndDisposeSource()
  {
    final World world = Worlds.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity source = em.createEntity( new BitSet() );
    final Entity target = em.createEntity( new BitSet() );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );

    final Link link = run( world, () -> em.link( source, target, false, true ) );

    assertLinkShape( link, source, target, false, true );
    assertLinkCount( source, 0, 1 );
    assertLinkCount( target, 1, 0 );

    run( world, () -> em.disposeEntity( source.getId() ) );

    assertFalse( link.isValid() );

    assertFalse( em.isAlive( source.getId() ) );
    assertTrue( em.isAlive( target.getId() ) );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );
  }

  @Test
  public void createLinkWithCascadeTargetRemoveAndDisposeSource_withSpyEnabled()
  {
    final World world = Worlds.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity source = em.createEntity( new BitSet() );
    final Entity target = em.createEntity( new BitSet() );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );

    final Link link = run( world, () -> em.link( source, target, false, true ) );

    assertLinkShape( link, source, target, false, true );
    assertLinkCount( source, 0, 1 );
    assertLinkCount( target, 1, 0 );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( world );
    run( world, () -> em.disposeEntity( source.getId() ) );
    handler.unsubscribe();

    assertFalse( link.isValid() );

    assertFalse( em.isAlive( source.getId() ) );
    assertTrue( em.isAlive( target.getId() ) );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );

    handler.assertEventCount( 4 );
    handler.assertNextEvent( EntityRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), source.getId() );
    } );
    handler.assertNextEvent( LinkRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getSourceEntityId(), source.getId() );
      assertEquals( e.getTargetEntityId(), target.getId() );
    } );
    handler.assertNextEvent( LinkRemoveCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getSourceEntityId(), source.getId() );
      assertEquals( e.getTargetEntityId(), target.getId() );
    } );
    handler.assertNextEvent( EntityRemoveCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), source.getId() );
    } );
  }

  @Test
  public void createLinkWithCascadeTargetRemoveAndDisposeTarget()
  {
    final World world = Worlds.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity source = em.createEntity( new BitSet() );
    final Entity target = em.createEntity( new BitSet() );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );

    final Link link = run( world, () -> em.link( source, target, false, true ) );

    assertLinkShape( link, source, target, false, true );
    assertLinkCount( source, 0, 1 );
    assertLinkCount( target, 1, 0 );

    run( world, () -> em.disposeEntity( target.getId() ) );

    assertFalse( link.isValid() );

    assertFalse( em.isAlive( source.getId() ) );
    assertFalse( em.isAlive( target.getId() ) );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );
  }

  @Test
  public void createLinkWithCascadeTargetRemoveAndDisposeTarget_withSpyEnabled()
  {
    final World world = Worlds.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity source = em.createEntity( new BitSet() );
    final Entity target = em.createEntity( new BitSet() );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );

    final Link link = run( world, () -> em.link( source, target, false, true ) );

    assertLinkShape( link, source, target, false, true );
    assertLinkCount( source, 0, 1 );
    assertLinkCount( target, 1, 0 );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( world );
    run( world, () -> em.disposeEntity( target.getId() ) );
    handler.unsubscribe();

    assertFalse( link.isValid() );

    assertFalse( em.isAlive( source.getId() ) );
    assertFalse( em.isAlive( target.getId() ) );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );

    handler.assertEventCount( 6 );
    handler.assertNextEvent( EntityRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), target.getId() );
    } );
    handler.assertNextEvent( LinkRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getSourceEntityId(), source.getId() );
      assertEquals( e.getTargetEntityId(), target.getId() );
    } );
    handler.assertNextEvent( EntityRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), source.getId() );
    } );
    handler.assertNextEvent( EntityRemoveCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), source.getId() );
    } );
    handler.assertNextEvent( LinkRemoveCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getSourceEntityId(), source.getId() );
      assertEquals( e.getTargetEntityId(), target.getId() );
    } );
    handler.assertNextEvent( EntityRemoveCompleteEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getEntityId(), target.getId() );
    } );
  }

  @Test
  public void dispose()
  {
    final World world = Worlds.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity source = em.createEntity( new BitSet() );
    final Entity target = em.createEntity( new BitSet() );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );

    final Link link = run( world, () -> em.link( source, target, false, true ) );

    assertLinkShape( link, source, target, false, true );
    assertLinkCount( source, 0, 1 );
    assertLinkCount( target, 1, 0 );

    run( world, link::dispose );

    assertFalse( link.isValid() );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );

    assertTrue( em.isAlive( source.getId() ) );
    assertTrue( em.isAlive( target.getId() ) );
  }

  @Test
  public void dispose_withSpyEnabled()
  {
    final World world = Worlds.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity source = em.createEntity( new BitSet() );
    final Entity target = em.createEntity( new BitSet() );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );

    final Link link = run( world, () -> em.link( source, target, false, true ) );

    assertLinkShape( link, source, target, false, true );
    assertLinkCount( source, 0, 1 );
    assertLinkCount( target, 1, 0 );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( world );
    run( world, link::dispose );
    handler.unsubscribe();

    assertFalse( link.isValid() );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );

    assertTrue( em.isAlive( source.getId() ) );
    assertTrue( em.isAlive( target.getId() ) );

    handler.assertEventCount( 2 );
    handler.assertNextEvent( LinkRemoveStartEvent.class, e -> {
      assertEquals( e.getWorld(), world );
      assertEquals( e.getSourceEntityId(), source.getId() );
      assertEquals( e.getTargetEntityId(), target.getId() );
    } );
  }

  @Test
  public void dispose_disposedLink()
  {
    final World world = Worlds.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity source = em.createEntity( new BitSet() );
    final Entity target = em.createEntity( new BitSet() );

    final Link link = run( world, () -> em.link( source, target, false, false ) );
    run( world, link::dispose );

    assertInvariantFailure( link::dispose, "Galdr-0117: Link.dispose() method invoked on invalid link." );
  }

  @Test
  public void getSourceEntity_disposedLink()
  {
    final World world = Worlds.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity source = em.createEntity( new BitSet() );
    final Entity target = em.createEntity( new BitSet() );

    final Link link = run( world, () -> em.link( source, target, false, false ) );
    run( world, link::dispose );

    assertInvariantFailure( link::getSourceEntity,
                            "Galdr-0007: The Link.getSourceEntity() method invoked on invalid link." );
  }

  @Test
  public void getTargetEntity_disposedLink()
  {
    final World world = Worlds.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity source = em.createEntity( new BitSet() );
    final Entity target = em.createEntity( new BitSet() );

    final Link link = run( world, () -> em.link( source, target, false, false ) );
    run( world, link::dispose );

    assertInvariantFailure( link::getTargetEntity,
                            "Galdr-0016: The Link.getTargetEntity() method invoked on invalid link." );
  }

  private void assertLinkShape( @Nonnull final Link link,
                                @Nonnull final Entity source,
                                @Nonnull final Entity target,
                                final boolean cascadeSourceRemoveToTarget,
                                final boolean cascadeTargetRemoveToSource )
  {
    assertEquals( link.getId(), target.getId() );
    assertEquals( link.getSourceEntity(), source );
    assertEquals( link.getTargetEntity(), target );
    assertEquals( link.shouldCascadeSourceRemoveToTarget(), cascadeSourceRemoveToTarget );
    assertEquals( link.shouldCascadeTargetRemoveToSource(), cascadeTargetRemoveToSource );
    assertTrue( link.isValid() );
  }

  private void assertLinkCount( @Nonnull final Entity entity, final int in, final int out )
  {
    assertEquals( entity.getInwardLinks().size(), in );
    assertEquals( entity.getOutwardLinks().size(), out );
  }
}
