package galdr;

import java.util.Objects;
import java.util.function.Supplier;
import javax.annotation.Nonnull;

final class FastArrayComponentManager<T>
  extends AllocatingComponentManager<T>
{
  @Nonnull
  private Object[] _data;

  FastArrayComponentManager( @Nonnull final World world,
                             final int id,
                             @Nonnull final Class<T> type,
                             @Nonnull final Supplier<T> createFn,
                             final int initialCapacity )
  {
    super( world, id, Flags.ALLOCATE, type, createFn );
    _data = new Object[ initialCapacity ];
  }

  @Nonnull
  @Override
  ComponentStorage getStorage()
  {
    return ComponentStorage.ARRAY;
  }

  @SuppressWarnings( "unchecked" )
  @Nonnull
  @Override
  T performGet( final int entityId )
  {
    return Objects.requireNonNull( (T) _data[ entityId ] );
  }

  @Nonnull
  @Override
  T performCreate( final int entityId )
  {
    if ( entityId >= _data.length )
    {
      // Growth size is based on heuristic present in a few different ECS implementations
      // This newCapacity value may need to be re-assessed in the future
      grow( Math.max( 2 * _data.length, ( 3 * entityId ) / 2 ) );
    }
    final T component = createComponentInstance();
    _data[ entityId ] = component;
    return component;
  }

  @Override
  void performRemove( final int entityId )
  {
    _data[ entityId ] = null;
    // At some point we should probably shrink the underlying array.
    // When/how and with what knbs and controls will have to wait until
    // after better performance testing
  }

  private void grow( final int newCapacity )
  {
    final Object[] oldData = _data;
    _data = new Object[ newCapacity ];
    System.arraycopy( oldData, 0, _data, 0, oldData.length );
  }

  int capacity()
  {
    return _data.length;
  }
}
