package galdr;

import galdr.spy.CollectionAttachEvent;
import galdr.spy.CollectionDetachEvent;
import galdr.spy.SubscriptionDisposeCompleteEvent;
import galdr.spy.SubscriptionDisposeStartEvent;
import grim.annotations.OmitSymbol;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

public final class Subscription
{
  @Nonnull
  private final World _world;
  /**
   * A unique identifier for the subscription.
   */
  private final int _id;
  /**
   * A human consumable name for Processor. It must be non-null if {@link Galdr#areNamesEnabled()} returns
   * true and <tt>null</tt> otherwise.
   */
  @OmitSymbol( unless = "galdr.enable_names" )
  @Nullable
  private final String _name;
  /**
   * The underlying collection that contains the entities.
   */
  @Nonnull
  private final EntityCollection _collection;
  /**
   * A flag set when subscription is disposed.
   */
  private boolean _disposed;

  Subscription( @Nonnull final World world,
                final int id,
                @Nullable final String name,
                @Nonnull final AreaOfInterest areaOfInterest )
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> Galdr.areNamesEnabled() || null == name,
                    () -> "Galdr-0052: Subscription passed a name '" + name +
                          "' but Galdr.areNamesEnabled() is false" );
    }
    _world = Objects.requireNonNull( world );
    _id = id;
    _name = Galdr.areNamesEnabled() ? Objects.requireNonNull( name ) : null;
    _collection = world.findOrCreateCollection( areaOfInterest );
    if ( _world.willPropagateSpyEvents() && 0 != _collection.getRefCount() )
    {
      _world.getSpy().reportSpyEvent( new CollectionAttachEvent( _collection.asInfo() ) );
    }
    _collection.incRef();
  }

  public int getId()
  {
    return _id;
  }

  public boolean isNotDisposed()
  {
    return !isDisposed();
  }

  public boolean isDisposed()
  {
    return _disposed;
  }

  public void dispose()
  {
    ensureNotDisposed();
    AreaOfInterest areaOfInterest = null;
    if ( _world.willPropagateSpyEvents() )
    {
      assert null != _name;
      areaOfInterest = _collection.getAreaOfInterest();
      _world.getSpy().reportSpyEvent( new SubscriptionDisposeStartEvent( _id, _name, areaOfInterest ) );
    }
    _disposed = true;
    _collection.decRef();
    if ( _world.willPropagateSpyEvents() )
    {
      if ( 0 != _collection.getRefCount() )
      {
        _world.getSpy().reportSpyEvent( new CollectionDetachEvent( _collection.asInfo() ) );
      }
      assert null != _name;
      assert null != areaOfInterest;
      _world.getSpy().reportSpyEvent( new SubscriptionDisposeCompleteEvent( _id, _name, areaOfInterest ) );
    }
  }

  public void beginIteration()
  {
    ensureNotDisposed();
    _collection.beginIteration( this );
  }

  public int nextEntity()
  {
    ensureNotDisposed();
    return _collection.nextEntity( this );
  }

  public void abortIteration()
  {
    ensureNotDisposed();
    _collection.abortIteration( this );
  }

  /**
   * Return the human readable name of the Subscription.
   * This method should NOT be invoked unless {@link Galdr#areNamesEnabled()} returns <code>true</code>.
   *
   * @return the human readable name of the Subscription.
   */
  @OmitSymbol( unless = "galdr.enable_names" )
  @Nonnull
  public final String getName()
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( Galdr::areNamesEnabled,
                    () -> "Galdr-0004: Subscription.getName() invoked when Galdr.areNamesEnabled() returns false" );
    }
    assert null != _name;
    return _name;
  }

  @OmitSymbol( unless = "galdr.debug_to_string" )
  @Override
  public String toString()
  {
    if ( Galdr.areDebugToStringMethodsEnabled() )
    {
      return "Subscription[" + getName() + "]";
    }
    else
    {
      return super.toString();
    }
  }

  @Nonnull
  EntityCollection getCollection()
  {
    return _collection;
  }

  @OmitSymbol( unless = "galdr.check_api_invariants" )
  private void ensureNotDisposed()
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> !_disposed,
                    () -> "Galdr-0043: Attempted to invoke method on Subscription named '" + getName() +
                          "' but the Subscription is disposed." );
    }
  }
}
