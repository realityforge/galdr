package galdr.spy;

import galdr.World;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Notification emitted before entity is removed from the world.
 */
public final class EntityRemoveStartEvent
  implements SerializableEvent
{
  /**
   * The associated world.
   */
  @Nonnull
  private final World _world;
  /**
   * The entity id.
   */
  private final int _entityId;

  public EntityRemoveStartEvent( @Nonnull final World world, final int entityId )
  {
    _world = Objects.requireNonNull( world );
    _entityId = entityId;
  }

  @Nonnull
  public World getWorld()
  {
    return _world;
  }

  public int getEntityId()
  {
    return _entityId;
  }

  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", "EntityRemoveStart" );
    map.put( "world", _world.getName() );
    map.put( "entityId", _entityId );
  }
}
