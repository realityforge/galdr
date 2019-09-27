package galdr.spy;

import galdr.World;
import java.util.BitSet;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Notification emitted after an entity is added to the world.
 */
public final class EntityAddCompleteEvent
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
  /**
   * The component ids.
   */
  @Nonnull
  private final BitSet _componentIds;

  public EntityAddCompleteEvent( @Nonnull final World world, final int entityId, @Nonnull final BitSet componentIds )
  {
    _world = Objects.requireNonNull( world );
    _entityId = entityId;
    _componentIds = Objects.requireNonNull( componentIds );
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

  @Nonnull
  public BitSet getComponentIds()
  {
    return _componentIds;
  }

  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", "EntityAddComplete" );
    map.put( "world", _world.getName() );
    map.put( "entityId", _entityId );
    map.put( "componentIds", _componentIds );
  }
}
