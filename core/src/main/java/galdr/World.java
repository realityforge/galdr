package galdr;

import galdr.spy.LinkAddCompleteEvent;
import galdr.spy.LinkAddStartEvent;
import galdr.spy.Spy;
import galdr.spy.WorldInfo;
import java.util.BitSet;
import java.util.Collection;
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
   * Cached info object associated with element.
   * This should be null if {@link Galdr#areSpiesEnabled()} is false.
   */
  private WorldInfoImpl _info;
  /**
   * The id used to create the next unnamed subscription.
   */
  private int _nextSubscriptionId = 1;

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
   * The collection of entity collections in the world.
   */
  @Nullable
  private Map<AreaOfInterest, EntityCollection> _collections;
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
  private final SpyImpl _spy = Galdr.areSpiesEnabled() ? new SpyImpl( this ) : null;

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
    ensureCurrentWorldMatches( "createEntity()" );
    return getEntityManager().createEntity( initialComponentIds ).getId();
  }

  /**
   * Link a source entity to a target entity.
   * If either entity is disposed then the Link is marked as invalid. The link may also cascade the dispose from
   * the source entity to the target entity or vice-versa if the appropriate configuration is set.
   *
   * @param sourceEntityId              the source entity id.
   * @param targetEntityId              the target entity id.
   * @param cascadeSourceRemoveToTarget a flag that controls whether the target entity will be disposed if the source entity is disposed.
   * @param cascadeTargetRemoveToSource a flag that controls whether the source entity will be disposed if the target entity is disposed.
   * @return the newly created Link.
   */
  @Nonnull
  public Link link( final int sourceEntityId,
                    final int targetEntityId,
                    final boolean cascadeSourceRemoveToTarget,
                    final boolean cascadeTargetRemoveToSource )
  {
    ensureCurrentWorldMatches( "link()" );
    if ( Galdr.shouldCheckInvariants() )
    {
      invariant( () -> isAlive( sourceEntityId ),
                 () -> "Galdr-0011: Attempted to link from entity " + sourceEntityId + " to entity " + targetEntityId +
                       " but the source entity is not alive." );
      invariant( () -> isAlive( targetEntityId ),
                 () -> "Galdr-0010: Attempted to link from entity " + sourceEntityId + " to entity " + targetEntityId +
                       " but the target entity is not alive." );
      invariant( () -> sourceEntityId != targetEntityId,
                 () -> "Galdr-0110: Attempted to link entity " + sourceEntityId + " to itself." );
    }
    final Entity source = getEntityManager().getEntityById( sourceEntityId );
    final Entity target = getEntityManager().getEntityById( targetEntityId );
    if ( willPropagateSpyEvents() )
    {
      getSpy().reportSpyEvent( new LinkAddStartEvent( this, sourceEntityId, targetEntityId ) );
    }
    final Link link = new Link( source, target, cascadeSourceRemoveToTarget, cascadeTargetRemoveToSource );
    if ( willPropagateSpyEvents() )
    {
      getSpy().reportSpyEvent( new LinkAddCompleteEvent( this, sourceEntityId, targetEntityId ) );
    }
    return link;
  }

  /**
   * Dispose the entity with the specified id.
   *
   * @param entityId the entity id.
   */
  public void disposeEntity( final int entityId )
  {
    ensureCurrentWorldMatches( "disposeEntity()" );
    getEntityManager().disposeEntity( entityId );
  }

  /**
   * Return true if the specified id designates an entity that has completed creation and has not yet started removal.
   *
   * @param entityId the entity id.
   * @return true if the entityId represents a valid entity.
   */
  public boolean isAlive( final int entityId )
  {
    ensureCurrentWorldMatches( "isEntity()" );
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
    _collections = new HashMap<>();
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

  /**
   * Return the info associated with this class.
   *
   * @return the info associated with this class.
   */
  @SuppressWarnings( "ConstantConditions" )
  @Nonnull
  WorldInfo asInfo()
  {
    if ( Galdr.shouldCheckInvariants() )
    {
      invariant( Galdr::areSpiesEnabled,
                 () -> "Galdr-0040: World.asInfo() invoked but Galdr.areSpiesEnabled() returned false." );
    }
    if ( Galdr.areSpiesEnabled() && null == _info )
    {
      _info = new WorldInfoImpl( this );
    }
    return Galdr.areSpiesEnabled() ? _info : null;
  }

  @Nonnull
  Map<AreaOfInterest, EntityCollection> getEntityCollections()
  {
    assertWorldConstructed( "World.getEntityCollections()" );
    assert null != _collections;
    return _collections;
  }

  @Nonnull
  EntityCollection createCollection( @Nonnull final BitSet all,
                                     @Nonnull final BitSet one,
                                     @Nonnull final BitSet exclude )
  {
    return createCollection( createAreaOfInterest( all, one, exclude ) );
  }

  @Nonnull
  public AreaOfInterest createAreaOfInterest( @Nonnull final BitSet all,
                                              @Nonnull final BitSet one,
                                              @Nonnull final BitSet exclude )
  {
    verifyBitSet( "all", all );
    verifyBitSet( "one", one );
    verifyBitSet( "exclude", exclude );
    return new AreaOfInterest( all, one, exclude );
  }

  private void verifyBitSet( @Nonnull final String name, @Nonnull final BitSet set )
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      int current = -1;
      while ( -1 != ( current = set.nextSetBit( current + 1 ) ) )
      {
        final int id = current;
        apiInvariant( () -> isComponentIdValid( id ),
                      () -> "Galdr-0044: World.createAreaOfInterest() passed an invalid component id " +
                            id + " in the " + name + " component set." );

      }
    }
  }

  @Nonnull
  public Subscription createSubscription( @Nonnull final AreaOfInterest areaOfInterest )
  {
    return createSubscription( null, areaOfInterest );
  }

  @Nonnull
  public Subscription createSubscription( @Nullable final String name, @Nonnull final AreaOfInterest areaOfInterest )
  {
    final String actualName =
      Galdr.areNamesEnabled() ? null == name ? "Subscription@" + _nextSubscriptionId++ : name : null;
    return new Subscription( actualName, findOrCreateCollection( areaOfInterest ) );
  }

  @Nonnull
  public AreaOfInterest createAreaOfInterest( @Nonnull final Collection<Class<?>> all,
                                              @Nonnull final Collection<Class<?>> one,
                                              @Nonnull final Collection<Class<?>> exclude )
  {
    return new AreaOfInterest( toBitSet( "all", all ), toBitSet( "one", one ), toBitSet( "exclude", exclude ) );
  }

  @Nonnull
  private BitSet toBitSet( @Nonnull final String name, @Nonnull final Collection<Class<?>> components )
  {
    final BitSet set = new BitSet();
    for ( final Class<?> type : components )
    {
      final int id = getComponentByType( type ).getId();
      if ( Galdr.shouldCheckApiInvariants() )
      {
        apiInvariant( () -> !set.get( id ),
                      () -> "Galdr-0043: World.createAreaOfInterest() passed a duplicate component " +
                            type.getName() + " in the " + name + " component set." );
      }
      set.set( id );
    }
    return set;
  }

  @Nonnull
  private EntityCollection findOrCreateCollection( @Nonnull final AreaOfInterest areaOfInterest )
  {
    final EntityCollection collection = findCollection( areaOfInterest );
    if ( null == collection )
    {
      return createCollection( areaOfInterest );
    }
    else
    {
      return collection;
    }
  }

  @Nullable
  EntityCollection findCollection( @Nonnull final AreaOfInterest areaOfInterest )
  {
    return getEntityCollections().get( areaOfInterest );
  }

  @Nonnull
  private EntityCollection createCollection( @Nonnull final AreaOfInterest areaOfInterest )
  {
    ensureCurrentWorldMatches( "createCollection()" );
    if ( Galdr.shouldCheckInvariants() )
    {
      final EntityCollection existing = findCollection( areaOfInterest );
      invariant( () -> null == existing,
                 () -> "Galdr-0034: World.createCollection() invoked but collection with matching AreaOfInterest already exists." );
    }
    final EntityCollection collection = new EntityCollection( this, areaOfInterest, getEntityManager().capacity() );
    getEntityCollections().put( areaOfInterest, collection );
    linkCollection( collection, areaOfInterest.getAll() );
    linkCollection( collection, areaOfInterest.getOne() );
    linkCollection( collection, areaOfInterest.getExclude() );
    return collection;
  }

  private void linkCollection( @Nonnull final EntityCollection collection, @Nonnull final BitSet componentIds )
  {
    int current = -1;
    while ( -1 != ( current = componentIds.nextSetBit( current + 1 ) ) )
    {
      getComponentManagerById( current ).addCollection( collection );
    }
  }

  void removeCollection( @Nonnull final EntityCollection collection )
  {
    ensureCurrentWorldMatches( "removeCollection()" );
    final AreaOfInterest areaOfInterest = collection.getAreaOfInterest();
    final EntityCollection existing = getEntityCollections().remove( areaOfInterest );
    if ( Galdr.shouldCheckInvariants() )
    {
      invariant( () -> null != existing,
                 () -> "Galdr-0020: World.removeCollection() invoked but no such collection." );
      invariant( () -> collection == existing,
                 () -> "Galdr-0041: World.removeCollection() invoked existing collection does not match supplied collection." );
    }
    assert null != existing;
    unlinkCollection( existing, areaOfInterest.getAll() );
    unlinkCollection( existing, areaOfInterest.getOne() );
    unlinkCollection( existing, areaOfInterest.getExclude() );
    existing.markAsDisposed();
  }

  private void unlinkCollection( @Nonnull final EntityCollection collection, @Nonnull final BitSet componentIds )
  {
    int current = -1;
    while ( -1 != ( current = componentIds.nextSetBit( current + 1 ) ) )
    {
      getComponentManagerById( current ).removeCollection( collection );
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

  private void ensureCurrentWorldMatches( @Nonnull final String methodName )
  {
    if ( Galdr.shouldCheckApiInvariants() )
    {
      final World world = WorldHolder.world();
      apiInvariant( () -> this == world,
                    () -> "Galdr-0037: World." + methodName + " invoked on world named '" + getName() +
                          "' when a world named '" + world.getName() + "' is active." );
    }
  }
}
