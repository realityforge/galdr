package galdr.spy;

import galdr.World;
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

  public EntityAddStartEvent( @Nonnull final World world )
  {
    _world = Objects.requireNonNull( world );
  }

  @Nonnull
  public World getWorld()
  {
    return _world;
  }

  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", "EntityAddStart" );
    map.put( "world", _world.getName() );
  }
}
