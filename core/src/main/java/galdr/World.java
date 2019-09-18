package galdr;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
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
  /**
   * The container of ComponentManager available in the world.
   */
  @Nullable
  private ComponentRegistry _componentRegistry;
  /**
   * The container of stages available in the world.
   */
  @Nullable
  private Map<String, ProcessorStage> _stages;

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
  public ProcessorStage getStageByName( @Nonnull final String name )
  {
    final Map<String, ProcessorStage> stages = getStages();
    final ProcessorStage stage = stages.get( name );
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> null != stage,
                    () -> "Galdr-0046: Invoked World.getStageByName() on World named '" + getName() + "' with " +
                          "stage name '" + name + "' but no such stage exists. Known stages include: " +
                          stages.keySet().stream().sorted().collect( Collectors.toList() ) );
    }
    assert null != stage;
    return stage;
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
  Map<String, ProcessorStage> getStages()
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> null != _stages,
                    () -> "Galdr-0045: Attempted to invoke World.getStages() on World named '" +
                          getName() + "' prior to World completing construction" );
    }
    assert null != _stages;
    return _stages;
  }

  @Nonnull
  @Override
  protected final String getBaseTypeName()
  {
    return "World";
  }

  void completeConstruction( @Nonnull final ComponentRegistry componentRegistry,
                             @Nonnull final Map<String, ProcessorStage> stages )
  {
    _componentRegistry = Objects.requireNonNull( componentRegistry );
    _stages = Collections.unmodifiableMap( new HashMap<>( Objects.requireNonNull( stages ) ) );
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
