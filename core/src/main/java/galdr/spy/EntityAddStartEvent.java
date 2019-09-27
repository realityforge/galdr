package galdr.spy;

import galdr.World;
import java.util.BitSet;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Notification emitted before an entity is added to the world.
 */
public final class EntityAddStartEvent
  implements SerializableEvent
{
  /**
   * The associated world.
   */
  @Nonnull
  private final World _world;
  /**
   * The component ids.
   */
  @Nonnull
  private final BitSet _componentIds;

  public EntityAddStartEvent( @Nonnull final World world, @Nonnull final BitSet componentIds )
  {
    _world = Objects.requireNonNull( world );
    _componentIds = Objects.requireNonNull( componentIds );
  }

  @Nonnull
  public World getWorld()
  {
    return _world;
  }

  @Nonnull
  public BitSet getComponentIds()
  {
    return _componentIds;
  }

  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", "EntityAddStart" );
    map.put( "world", _world.getName() );
    map.put( "componentIds", _componentIds );
  }
}
