package galdr;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

public final class World
  extends Element
{
  /**
   * A synthetic id used to construct te worlds name if not explicitly supplied.
   */
  private static int c_nextId = 1;
  @Nullable
  private ComponentRegistry _componentRegistry;
  @Nullable
  private ProcessorStage[] _stages;

  World( @Nullable final String name )
  {
    super( Galdr.areNamesEnabled() && null == name ? "World@" + c_nextId++ : name );
  }

  @Nonnull
  public <T> ComponentAPI<T> getComponentByType( @Nonnull final Class<T> type )
  {
    return getComponentRegistry().getComponentManagerByType( type ).getApi();
  }

  @Nonnull
  ComponentRegistry getComponentRegistry()
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> null != _componentRegistry,
                    () -> "Galdr-0044: Attempted to invoke World.getComponentRegistry() on World named '" +
                          getName() + "' prior to World completing construction" );
    }
    assert null != _componentRegistry;
    return _componentRegistry;
  }

  @Nonnull
  @Override
  protected final String getBaseTypeName()
  {
    return "World";
  }

  void completeConstruction( @Nonnull final ComponentRegistry componentRegistry,
                             @Nonnull final ProcessorStage[] stages )
  {
    _componentRegistry = Objects.requireNonNull( componentRegistry );
    _stages = Objects.requireNonNull( stages );
  }

  /**
   * Reset id used when constructing names for anonymous worlds.
   * This is only invoked from tests.
   */
  static void resetId()
  {
    c_nextId = 1;
  }
}
