package galdr;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

public final class Subscription
{
  /**
   * A human consumable name for Processor. It must be non-null if {@link Galdr#areNamesEnabled()} returns
   * true and <tt>null</tt> otherwise.
   */
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

  Subscription( @Nullable final String name, @Nonnull final EntityCollection collection )
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> Galdr.areNamesEnabled() || null == name,
                    () -> "Galdr-0052: Subscription passed a name '" + name +
                          "' but Galdr.areNamesEnabled() is false" );
    }
    _name = Galdr.areNamesEnabled() ? Objects.requireNonNull( name ) : null;
    _collection = Objects.requireNonNull( collection );
    _collection.incRef();
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
    _disposed = true;
    _collection.decRef();
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
