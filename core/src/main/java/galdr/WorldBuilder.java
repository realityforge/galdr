package galdr;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

public final class WorldBuilder
{
  /**
   * The world that is being setup by the builder.
   */
  @Nonnull
  private final World _world;
  /**
   * The set of components that will be defined in the world.
   */
  @Nonnull
  private final List<ComponentManager<?>> _components = new ArrayList<>();
  /**
   * A flag set to true after build is invoked. After the world is constructed it is invalid to invoke construction methods.
   */
  private boolean _worldConstructed;

  WorldBuilder( @Nullable final String name )
  {
    _world = new World( name );
    WorldHolder.activateWorld( _world );
  }

  @Nonnull
  public <T> WorldBuilder component( @Nonnull final Class<T> type, @Nonnull final Supplier<T> createFn )
  {
    ensureWorldNotConstructed();
    _components.add( new FastArrayComponentManager<>( _components.size(), type, createFn ) );
    return this;
  }

  @Nonnull
  public World build()
  {
    ensureWorldNotConstructed();
    _worldConstructed = true;
    WorldHolder.deactivateWorld( _world );
    final ComponentRegistry registry = new ComponentRegistry( _components.toArray( new ComponentManager[ 0 ] ) );
    _world.completeConstruction( registry );
    return _world;
  }

  private void ensureWorldNotConstructed()
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      invariant( () -> !_worldConstructed,
                 () -> "Galdr-0019: Attempted to invoke method on WorldBuilder but world has already been constructed" );
    }
  }
}
