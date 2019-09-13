package galdr;

import javax.annotation.Nonnull;

final class FastArrayComponentStore
  extends ComponentStore
{
  @Nonnull
  private Object[] _data;

  FastArrayComponentStore( @Nonnull final ComponentType componentType )
  {
    this( componentType, 120 );
  }

  FastArrayComponentStore( @Nonnull final ComponentType componentType, final int initialCapacity )
  {
    super( componentType );
    _data = new Object[ initialCapacity ];
  }

  @Override
  boolean performHas( final int entityId )
  {
    return entityId < _data.length && null != _data[ entityId ];
  }

  @Override
  Object performFind( final int entityId )
  {
    return entityId < _data.length ? _data[ entityId ] : null;
  }

  @Override
  Object performCreate( final int entityId )
  {
    if ( entityId >= _data.length )
    {
      grow( Math.max( 2 * _data.length, ( 3 * entityId ) / 2 ) );
    }
    final Object component = createComponentInstance();
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
