package galdr.spy;

import galdr.World;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Notification emitted after component is added to an entity.
 */
public final class ComponentAddCompleteEvent
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
   * The component id.
   */
  private final int _componentId;

  public ComponentAddCompleteEvent( @Nonnull final World world, final int entityId, final int componentId )
  {
    _world = Objects.requireNonNull( world );
    _entityId = entityId;
    _componentId = componentId;
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

  public int getComponentId()
  {
    return _componentId;
  }

  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", "ComponentAddComplete" );
    map.put( "world", _world.getName() );
    map.put( "entityId", _entityId );
    map.put( "componentId", _componentId );
  }
}
