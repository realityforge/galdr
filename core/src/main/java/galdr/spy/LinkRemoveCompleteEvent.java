package galdr.spy;

import galdr.World;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Notification emitted after a link is removed.
 */
public final class LinkRemoveCompleteEvent
  implements SerializableEvent
{
  /**
   * The associated world.
   */
  @Nonnull
  private final World _world;
  /**
   * The source entity id.
   */
  private final int _sourceEntityId;
  /**
   * The source entity id.
   */
  private final int _targetEntityId;

  public LinkRemoveCompleteEvent( @Nonnull final World world, final int sourceEntityId, final int targetEntityId )
  {
    _world = Objects.requireNonNull( world );
    _sourceEntityId = sourceEntityId;
    _targetEntityId = targetEntityId;
  }

  @Nonnull
  public World getWorld()
  {
    return _world;
  }

  public int getSourceEntityId()
  {
    return _sourceEntityId;
  }

  public int getTargetEntityId()
  {
    return _targetEntityId;
  }

  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", "LinkRemoveComplete" );
    map.put( "world", _world.getName() );
    map.put( "sourceEntityId", _sourceEntityId );
    map.put( "targetEntityId", _targetEntityId );
  }
}
