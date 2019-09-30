package galdr;

import galdr.spy.Spy;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

// TODO: Remove WeakerAccess suppression by adding integration tests that call al the APIs
@SuppressWarnings( "WeakerAccess" )
public final class World
{
  /**
   * A human consumable name for node. It must be non-null if {@link Galdr#areNamesEnabled()} returns
   * true and <tt>null</tt> otherwise.
   */
  @Nullable
  private final String _name;

  /**
   * Interface used to define actions that can be run in the context of a world.
   */
  @FunctionalInterface
  interface WorldAction
  {
    void call();
  }

  /**
   * Interface used to define functions that can be run in the context of a world.
   */
  @FunctionalInterface
  interface WorldFunction<T>
  {
    T call();
  }

  /**
   * A synthetic id used to construct te worlds name if not explicitly supplied.
   */
  private static int c_nextId = 1;
  @Nullable
  private EntityManager _entityManager;
  /**
   * The ComponentManagers available in the world ordered by id.
   */
  @Nullable
  private ComponentManager<?>[] _components;
  /**
   * The ComponentManagers available in the world accessible by class.
   */
  @Nullable
  private Map<Class<?>, ComponentManager<?>> _componentByClass;
  /**
   * The container of stages available in the world.
   */
  @Nullable
  private Map<String, ProcessorStage> _stages;
  /**
   * The collection of subscriptions in the world.
   */
  @Nullable
  private Map<AreaOfInterest, Subscription> _subscriptions;
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
    _name = Galdr.areNamesEnabled() ? null == name ? "World@" + c_nextId++ : name : null;
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
    assertWorldConstructed( "World.getEntityManager()" );
    assert null != _entityManager;
    return _entityManager;
  }

  @Nonnull
  public <T> ComponentAPI<T> getComponentByType( @Nonnull final Class<T> type )
  {
    return getComponentManagerByType( type ).getApi();
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
  Map<String, ProcessorStage> getStages()
  {
    assertWorldConstructed( "World.getStages()" );
    assert null != _stages;
    return _stages;
  }

  void completeConstruction( final int initialEntityCount,
                             @Nonnull final ComponentManager<?>[] components,
                             @Nonnull final Map<String, ProcessorStage> stages )
  {
    _entityManager = new EntityManager( this, initialEntityCount );
    _stages = Collections.unmodifiableMap( new HashMap<>( Objects.requireNonNull( stages ) ) );
    _subscriptions = new HashMap<>();
    _components = components;
    _componentByClass = buildComponentMap( components );
  }

  @Nonnull
  private Map<Class<?>, ComponentManager<?>> buildComponentMap( @Nonnull final ComponentManager<?>[] components )
  {
    final Map<Class<?>, ComponentManager<?>> map = new HashMap<>();
    for ( int i = 0; i < components.length; i++ )
    {
      final ComponentManager component = components[ i ];
      if ( Galdr.shouldCheckInvariants() )
      {
        final int index = i;
        final int id = component.getId();
        invariant( () -> index == id,
                   () -> "Galdr-0003: Component named '" + component.getName() + "' has id " + id +
                         " but was passed as index " + index + "." );
      }
      map.put( component.getType(), component );
    }
    return Collections.unmodifiableMap( map );
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

  /**
   * Return the human readable name of the World.
   * This method should NOT be invoked unless {@link Galdr#areNamesEnabled()} returns <code>true</code>.
   *
   * @return the human readable name of the World.
   */
  @Nonnull
  public final String getName()
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( Galdr::areNamesEnabled,
                    () -> "Galdr-0004: World.getName() invoked when Galdr.areNamesEnabled() returns false" );
    }
    assert null != _name;
    return _name;
  }

  @Override
  public String toString()
  {
    if ( Galdr.areDebugToStringMethodsEnabled() )
    {
      return "World[" + getName() + "]";
    }
    else
    {
      return super.toString();
    }
  }

  @Nonnull
  Map<AreaOfInterest, Subscription> getSubscriptions()
  {
    assertWorldConstructed( "World.getSubscriptions()" );
    assert null != _subscriptions;
    return _subscriptions;
  }

  @Nonnull
  Subscription findOrCreateSubscription( @Nonnull final AreaOfInterest areaOfInterest )
  {
    final Map<AreaOfInterest, Subscription> subscriptions = getSubscriptions();
    final Subscription subscription = subscriptions.get( areaOfInterest );
    if ( null != subscription )
    {
      return subscription;
    }
    else
    {
      final Subscription newSubscription = new Subscription( areaOfInterest, getEntityManager().capacity() );
      subscriptions.put( areaOfInterest, newSubscription );
      linkSubscription( newSubscription, areaOfInterest.getAll() );
      linkSubscription( newSubscription, areaOfInterest.getOne() );
      linkSubscription( newSubscription, areaOfInterest.getExclude() );
      return newSubscription;
    }
  }

  private void linkSubscription( @Nonnull final Subscription subscription, @Nonnull final BitSet componentIds )
  {
    int current = -1;
    while ( -1 != ( current = componentIds.nextSetBit( current + 1 ) ) )
    {
      getComponentManagerById( current ).addSubscription( subscription );
    }
  }

  void run( @Nonnull final WorldAction action )
  {
    // TODO: Should we detect that WorldHolder.world() has not been accessed and generate an error?
    //  We could probably pass a flags to this method so that the check is optional but in most cases
    //  this check is a reasonable default.
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

  <T> T run( @Nonnull final WorldFunction<T> action )
  {
    // TODO: Should we detect that WorldHolder.world() has not been accessed and generate an error?
    //  We could probably pass a flags to this method so that the check is optional but in most cases
    //  this check is a reasonable default.
    WorldHolder.activateWorld( this );
    try
    {
      return action.call();
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

  @Nonnull
  ComponentManager<?> getComponentManagerById( final int id )
  {
    assertWorldConstructed( "World.getComponentManagerById()" );
    if ( Galdr.shouldCheckApiInvariants() )
    {
      invariant( () -> isComponentIdValid( id ),
                 () -> "Galdr-0002: World.getComponentManagerByIndex() attempted to access Component " +
                       "with id " + id + " but no such component exists." );
    }
    assert null != _components;
    return _components[ id ];
  }

  boolean isComponentIdValid( final int id )
  {
    assert null != _components;
    return id >= 0 && id < _components.length;
  }

  int getComponentCount()
  {
    assertWorldConstructed( "World.getComponentCount()" );
    assert null != _components;
    return _components.length;
  }

  @SuppressWarnings( "unchecked" )
  @Nonnull
  <T> ComponentManager<T> getComponentManagerByType( @Nonnull final Class<T> type )
  {
    assertWorldConstructed( "World.getComponentManagerByType()" );
    assert null != _componentByClass;
    final ComponentManager<T> componentManager = (ComponentManager<T>) _componentByClass.get( type );
    if ( Galdr.shouldCheckApiInvariants() )
    {
      invariant( () -> null != componentManager,
                 () -> "Galdr-0001: World.getComponentManagerByType() attempted to access Component " +
                       "for type " + type + " but no such component exists." );
    }
    return componentManager;
  }

  @Nonnull
  Set<Class<?>> getComponentTypes()
  {
    assertWorldConstructed( "World.getComponentTypes()" );
    assert null != _componentByClass;
    return _componentByClass.keySet();
  }

  /**
   * Reset id used when constructing names for anonymous worlds.
   * This is only invoked from tests.
   */
  static void resetId()
  {
    c_nextId = 1;
  }

  private void assertWorldConstructed( @Nonnull final String methodName )
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> null != _stages,
                    () -> "Galdr-0045: Attempted to invoke " + methodName + " on World named '" +
                          getName() + "' prior to World completing construction" );
    }
  }
}
