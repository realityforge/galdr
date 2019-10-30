package galdr;

import javax.annotation.Nonnull;
import static org.realityforge.braincheck.Guards.*;

final class NoStorageComponentManager<T>
  extends ComponentManager<T>
{
  NoStorageComponentManager( @Nonnull final World world,
                             final int id,
                             @Nonnull final Class<T> type )
  {
    super( world, id, 0, type );
  }

  @Nonnull
  @Override
  public ComponentStorage getStorage()
  {
    return ComponentStorage.NONE;
  }

  @Nonnull
  @Override
  T performGet( final int entityId )
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      fail( () -> "Galdr-0013: The ComponentManager.get() method has been invoked for the component named '" +
                  getName() + "' but the component was registered with ComponentStorage.NONE storage strategy " +
                  "and thus should never be accessed." );
    }
    throw new IllegalStateException();
  }

  @Nonnull
  @Override
  T performCreate( final int entityId )
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      fail( () -> "Galdr-0014: The ComponentManager.create() method has been invoked for the component named '" +
                  getName() + "' but the component was registered with ComponentStorage.NONE storage strategy " +
                  "and thus should never be accessed." );
    }
    throw new IllegalStateException();
  }

  @Override
  void performRemove( final int entityId )
  {
  }
}
