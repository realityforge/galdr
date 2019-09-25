package galdr;

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
    final World world = Galdr.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity source = em.createEntity( new BitSet() );
    final Entity target = em.createEntity( new BitSet() );

    final Link link = world.run( () -> em.link( source, target, false, false ) );

    assertEquals( link.toString(), "Link[0->1]" );

    world.run( link::dispose );

    assertEquals( link.toString(), "Link[(disposed)]" );

    GaldrTestUtil.disableDebugToString();

    assertDefaultToString( link );
  }

  @Test
  public void createLinkWithNoCascadeAndDisposeSource()
  {
    final World world = Galdr.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity source = em.createEntity( new BitSet() );
    final Entity target = em.createEntity( new BitSet() );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );

    final Link link = world.run( () -> em.link( source, target, false, false ) );

    assertLinkShape( link, source, target, false, false );
    assertLinkCount( source, 0, 1 );
    assertLinkCount( target, 1, 0 );

    world.run( () -> em.disposeEntity( source.getId() ) );

    assertFalse( link.isValid() );

    assertFalse( em.isAlive( source.getId() ) );
    assertTrue( em.isAlive( target.getId() ) );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );
  }

  @Test
  public void createLinkWithNoCascadeAndDisposeTarget()
  {
    final World world = Galdr.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity source = em.createEntity( new BitSet() );
    final Entity target = em.createEntity( new BitSet() );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );

    final Link link = world.run( () -> em.link( source, target, false, false ) );

    assertLinkShape( link, source, target, false, false );

    assertLinkCount( source, 0, 1 );
    assertLinkCount( target, 1, 0 );

    world.run( () -> em.disposeEntity( target.getId() ) );

    assertFalse( link.isValid() );

    assertTrue( em.isAlive( source.getId() ) );
    assertFalse( em.isAlive( target.getId() ) );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );
  }

  @Test
  public void createLinkWithCascadeSourceRemoveAndDisposeSource()
  {
    final World world = Galdr.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity source = em.createEntity( new BitSet() );
    final Entity target = em.createEntity( new BitSet() );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );

    final Link link = world.run( () -> em.link( source, target, true, false ) );

    assertLinkShape( link, source, target, true, false );
    assertLinkCount( source, 0, 1 );
    assertLinkCount( target, 1, 0 );

    world.run( () -> em.disposeEntity( source.getId() ) );

    assertFalse( link.isValid() );

    assertFalse( em.isAlive( source.getId() ) );
    assertFalse( em.isAlive( target.getId() ) );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );
  }

  @Test
  public void createLinkWithCascadeSourceRemoveAndDisposeTarget()
  {
    final World world = Galdr.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity source = em.createEntity( new BitSet() );
    final Entity target = em.createEntity( new BitSet() );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );

    final Link link = world.run( () -> em.link( source, target, true, false ) );

    assertLinkShape( link, source, target, true, false );
    assertLinkCount( source, 0, 1 );
    assertLinkCount( target, 1, 0 );

    world.run( () -> em.disposeEntity( source.getId() ) );

    assertFalse( link.isValid() );

    assertFalse( em.isAlive( source.getId() ) );
    assertFalse( em.isAlive( target.getId() ) );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );
  }

  @Test
  public void createLinkWithCascadeTargetRemoveAndDisposeSource()
  {
    final World world = Galdr.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity source = em.createEntity( new BitSet() );
    final Entity target = em.createEntity( new BitSet() );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );

    final Link link = world.run( () -> em.link( source, target, false, true ) );

    assertLinkShape( link, source, target, false, true );
    assertLinkCount( source, 0, 1 );
    assertLinkCount( target, 1, 0 );

    world.run( () -> em.disposeEntity( source.getId() ) );

    assertFalse( link.isValid() );

    assertFalse( em.isAlive( source.getId() ) );
    assertTrue( em.isAlive( target.getId() ) );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );
  }

  @Test
  public void createLinkWithCascadeTargetRemoveAndDisposeTarget()
  {
    final World world = Galdr.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity source = em.createEntity( new BitSet() );
    final Entity target = em.createEntity( new BitSet() );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );

    final Link link = world.run( () -> em.link( source, target, false, true ) );

    assertLinkShape( link, source, target, false, true );
    assertLinkCount( source, 0, 1 );
    assertLinkCount( target, 1, 0 );

    world.run( () -> em.disposeEntity( target.getId() ) );

    assertFalse( link.isValid() );

    assertFalse( em.isAlive( source.getId() ) );
    assertFalse( em.isAlive( target.getId() ) );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );
  }

  @Test
  public void dispose()
  {
    final World world = Galdr.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity source = em.createEntity( new BitSet() );
    final Entity target = em.createEntity( new BitSet() );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );

    final Link link = world.run( () -> em.link( source, target, false, true ) );

    assertLinkShape( link, source, target, false, true );
    assertLinkCount( source, 0, 1 );
    assertLinkCount( target, 1, 0 );

    world.run( link::dispose );

    assertFalse( link.isValid() );

    assertLinkCount( source, 0, 0 );
    assertLinkCount( target, 0, 0 );

    assertTrue( em.isAlive( source.getId() ) );
    assertTrue( em.isAlive( target.getId() ) );
  }

  @Test
  public void dispose_disposedLink()
  {
    final World world = Galdr.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity source = em.createEntity( new BitSet() );
    final Entity target = em.createEntity( new BitSet() );

    final Link link = world.run( () -> em.link( source, target, false, false ) );
    world.run( link::dispose );

    assertInvariantFailure( link::dispose, "Galdr-0117: Link.dispose() method invoked on invalid link." );
  }

  @Test
  public void getSourceEntity_disposedLink()
  {
    final World world = Galdr.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity source = em.createEntity( new BitSet() );
    final Entity target = em.createEntity( new BitSet() );

    final Link link = world.run( () -> em.link( source, target, false, false ) );
    world.run( link::dispose );

    assertInvariantFailure( link::getSourceEntity,
                            "Galdr-0007: The Link.getSourceEntity() method invoked on invalid link." );
  }

  @Test
  public void getTargetEntity_disposedLink()
  {
    final World world = Galdr.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity source = em.createEntity( new BitSet() );
    final Entity target = em.createEntity( new BitSet() );

    final Link link = world.run( () -> em.link( source, target, false, false ) );
    world.run( link::dispose );

    assertInvariantFailure( link::getTargetEntity,
                            "Galdr-0016: The Link.getTargetEntity() method invoked on invalid link." );
  }

  private void assertLinkShape( @Nonnull final Link link,
                                @Nonnull final Entity source,
                                @Nonnull final Entity target,
                                final boolean cascadeSourceRemoveToTarget,
                                final boolean cascadeTargetRemoveToSource )
  {
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
