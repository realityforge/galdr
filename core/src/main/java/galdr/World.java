package galdr;

import galdr.spy.Spy;
import java.util.BitSet;
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
   * Interface used to define actions that can be run in the context of a world.
   */
  @FunctionalInterface
  interface WorldAction
  {
    void call();
  }

  /**
   * A synthetic id used to construct te worlds name if not explicitly supplied.
   */
  private static int c_nextId = 1;
  @Nullable
  private EntityManager _entityManager;
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
  /**
   * Support infrastructure for propagating processor errors.
   */
  @Nullable
  private final ErrorHandlerSupport _errorHandlerSupport =
    Galdr.areErrorHandlersEnabled() ? new ErrorHandlerSupport() : null;
  /**
   * Support infrastructure for spy events.
   */
  @Nullable
  private final SpyImpl _spy = Galdr.areSpiesEnabled() ? new SpyImpl() : null;

  World( @Nullable final String name )
  {
    super( Galdr.areNamesEnabled() && null == name ? "World@" + c_nextId++ : name );
  }

  /**
   * Create a new entity with the specified components.
   *
   * @param initialComponentIds the ids of the components to create.
   * @return the id of the entity.
   */
  public int createEntity( @Nonnull final BitSet initialComponentIds )
  {
    return getEntityManager().createEntity( initialComponentIds ).getId();
  }

  /**
   * Dispose the entity with the specified id.
   *
   * @param entityId the entity id.
   */
  public void disposeEntity( final int entityId )
  {
    getEntityManager().disposeEntity( entityId );
  }

  /**
   * Return true if the specified id designates a valid entity.
   *
   * @param entityId the entity id.
   * @return true if the entityId represents a valid entity.
   */
  public boolean isEntity( final int entityId )
  {
    return getEntityManager().isAlive( entityId );
  }

  @Nonnull
  EntityManager getEntityManager()
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> null != _entityManager,
                    () -> "Galdr-0005: Attempted to invoke World.getEntityManager() on World named '" +
                          getName() + "' prior to World completing construction" );
    }
    assert null != _entityManager;
    return _entityManager;
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

  void completeConstruction( final int initialEntityCount,
                             @Nonnull final ComponentRegistry componentRegistry,
                             @Nonnull final Map<String, ProcessorStage> stages )
  {
    _entityManager = new EntityManager( this, initialEntityCount );
    _componentRegistry = Objects.requireNonNull( componentRegistry );
    _stages = Collections.unmodifiableMap( new HashMap<>( Objects.requireNonNull( stages ) ) );
  }

  /**
   * Add error handler to the list of error handlers called.
   * The handler should not already be in the list. This method should NOT be called if
   * {@link Galdr#areErrorHandlersEnabled()} returns false.
   *
   * @param handler the error handler.
   */
  public void addErrorHandler( @Nonnull final ErrorHandler handler )
  {
    if ( Galdr.shouldCheckInvariants() )
    {
      invariant( Galdr::areErrorHandlersEnabled,
                 () -> "Galdr-0182: World.addErrorHandler() invoked when Galdr.areErrorHandlersEnabled() returns false." );
    }
    getErrorHandlerSupport().addErrorHandler( handler );
  }

  /**
   * Remove error handler from list of existing error handlers.
   * The handler should already be in the list. This method should NOT be called if
   * {@link Galdr#areErrorHandlersEnabled()} returns false.
   *
   * @param handler the error handler.
   */
  public void removeErrorHandler( @Nonnull final ErrorHandler handler )
  {
    if ( Galdr.shouldCheckInvariants() )
    {
      invariant( Galdr::areErrorHandlersEnabled,
                 () -> "Galdr-0181: World.removeErrorHandler() invoked when Galdr.areErrorHandlersEnabled() returns false." );
    }
    getErrorHandlerSupport().removeErrorHandler( handler );
  }

  /**
   * Return true if spy events will be propagated.
   * This means spies are enabled and there is at least one spy event handler present.
   *
   * @return true if spy events will be propagated, false otherwise.
   */
  boolean willPropagateSpyEvents()
  {
    return Galdr.areSpiesEnabled() && getSpy().willPropagateSpyEvents();
  }

  /**
   * Return the spy associated with context.
   * This method should not be invoked unless {@link Galdr#areSpiesEnabled()} returns true.
   *
   * @return the spy associated with context.
   */
  @Nonnull
  public Spy getSpy()
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( Galdr::areSpiesEnabled, () -> "Galdr-0021: Attempting to get Spy but spies are not enabled." );
    }
    assert null != _spy;
    return _spy;
  }

  void run( @Nonnull final WorldAction action )
  {
    WorldHolder.activateWorld( this );
    try
    {
      action.call();
    }
    finally
    {
      WorldHolder.deactivateWorld( this );
    }
  }

  /**
   * Report an error in processor.
   *
   * @param stage     the stage that contained the processor that generated the error.
   * @param processor the processor that generated error.
   * @param throwable the exception that caused error if any.
   */
  void reportError( @Nonnull final ProcessorStage stage,
                    @Nonnull final Processor processor,
                    @Nonnull final Throwable throwable )
  {
    if ( Galdr.areErrorHandlersEnabled() )
    {
      getErrorHandlerSupport().onError( stage, processor, throwable );
    }
  }

  @Nonnull
  ErrorHandlerSupport getErrorHandlerSupport()
  {
    assert null != _errorHandlerSupport;
    return _errorHandlerSupport;
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
