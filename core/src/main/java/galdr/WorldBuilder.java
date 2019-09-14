package galdr;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import static org.realityforge.braincheck.Guards.*;

public final class WorldBuilder
{
  @Nonnull
  private final List<ComponentManager<?>> _components = new ArrayList<>();
  private boolean _worldConstructed;

  @Nonnull
  public static WorldBuilder create()
  {
    return new WorldBuilder();
  }

  private WorldBuilder()
  {
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
    return new World( _components.toArray( new ComponentManager[ 0 ] ) );
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
