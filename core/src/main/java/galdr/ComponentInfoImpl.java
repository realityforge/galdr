package galdr;

import galdr.spy.ComponentInfo;
import grim.annotations.OmitType;
import java.util.Objects;
import javax.annotation.Nonnull;

@OmitType( unless = "galdr.enable_spies" )
final class ComponentInfoImpl
  implements ComponentInfo
{
  @Nonnull
  private final ComponentManager<?> _componentManager;

  ComponentInfoImpl( @Nonnull final ComponentManager<?> componentManager )
  {
    _componentManager = Objects.requireNonNull( componentManager );
  }

  @Override
  public int getId()
  {
    return _componentManager.getId();
  }

  @Nonnull
  @Override
  public String getName()
  {
    return _componentManager.getName();
  }

  @Override
  public int getCollectionCount()
  {
    return _componentManager.getCollections().size();
  }

  @Override
  public String toString()
  {
    return _componentManager.toString();
  }

  @Override
  public boolean equals( final Object o )
  {
    if ( o instanceof ComponentInfoImpl )
    {
      final ComponentInfoImpl that = (ComponentInfoImpl) o;
      return _componentManager.equals( that._componentManager );
    }
    else
    {
      return false;
    }
  }

  @Override
  public int hashCode()
  {
    return _componentManager.hashCode();
  }
}
