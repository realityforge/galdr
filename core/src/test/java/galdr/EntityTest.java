package galdr;

import java.util.BitSet;
import org.testng.annotations.Test;

public class EntityTest
  extends AbstractTest
{
  @Test
  public void linkOutgoing_entityNotSource()
  {
    final World world = Galdr.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity entity1 = em.createEntity( new BitSet() );
    final Entity entity2 = em.createEntity( new BitSet() );
    final Entity entity3 = em.createEntity( new BitSet() );

    final Link link = world.run( () -> em.link( entity1, entity2, false, false ) );

    assertInvariantFailure( () -> world.run( () -> entity3.linkOutgoing( link ) ),
                            "Galdr-0809: Entity.linkOutgoing() on entity 2 but entity is not the source of the link." );
  }

  @Test
  public void linkIncoming_entityNotTarget()
  {
    final World world = Galdr.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity entity1 = em.createEntity( new BitSet() );
    final Entity entity2 = em.createEntity( new BitSet() );
    final Entity entity3 = em.createEntity( new BitSet() );

    final Link link = world.run( () -> em.link( entity1, entity2, false, false ) );

    assertInvariantFailure( () -> world.run( () -> entity3.linkIncoming( link ) ),
                            "Galdr-0808: Entity.linkIncoming() on entity 2 but entity is not the target of the link." );
  }

  @Test
  public void unlinkIncoming_noLinks()
  {
    final World world = Galdr.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity entity1 = em.createEntity( new BitSet() );
    final Entity entity2 = em.createEntity( new BitSet() );
    final Entity entity3 = em.createEntity( new BitSet() );

    final Link link = world.run( () -> em.link( entity1, entity2, false, false ) );

    assertInvariantFailure( () -> world.run( () -> entity3.unlinkIncoming( link ) ),
                            "Galdr-0008: Attempted to unlink incoming link Link[0->1] but entity 2 has no incoming links." );
  }

  @Test
  public void unlinkIncoming_noMatchingLink()
  {
    final World world = Galdr.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity entity1 = em.createEntity( new BitSet() );
    final Entity entity2 = em.createEntity( new BitSet() );
    final Entity entity3 = em.createEntity( new BitSet() );

    final Link link1 = world.run( () -> em.link( entity1, entity2, false, false ) );
    world.run( () -> em.link( entity1, entity3, false, false ) );

    assertInvariantFailure( () -> world.run( () -> entity3.unlinkIncoming( link1 ) ),
                            "Galdr-0012: Invoked Entity.unlinkIncoming with link Link[0->1] but entity 2 has no such incoming link." );
  }

  @Test
  public void unlinkOutgoing_noLinks()
  {
    final World world = Galdr.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity entity1 = em.createEntity( new BitSet() );
    final Entity entity2 = em.createEntity( new BitSet() );
    final Entity entity3 = em.createEntity( new BitSet() );

    final Link link = world.run( () -> em.link( entity1, entity2, false, false ) );

    assertInvariantFailure( () -> world.run( () -> entity3.unlinkOutgoing( link ) ),
                            "Galdr-0038: Attempted to unlink outgoing link Link[0->1] but entity 2 has no outgoing links." );
  }

  @Test
  public void unlinkOutgoing_noMatchingLink()
  {
    final World world = Galdr.world().build();

    final EntityManager em = world.getEntityManager();

    final Entity entity1 = em.createEntity( new BitSet() );
    final Entity entity2 = em.createEntity( new BitSet() );
    final Entity entity3 = em.createEntity( new BitSet() );

    final Link link1 = world.run( () -> em.link( entity1, entity2, false, false ) );
    world.run( () -> em.link( entity3, entity2, false, false ) );

    assertInvariantFailure( () -> world.run( () -> entity3.unlinkOutgoing( link1 ) ),
                            "Galdr-0039: Invoked Entity.unlinkOutgoing with link Link[0->1] but entity 2 has no such outgoing link." );
  }
}
