package galdr;

import galdr.spy.CollectionInfo;
import galdr.spy.ComponentInfo;
import galdr.spy.Spy;
import galdr.spy.SpyEventHandler;
import galdr.spy.SubscriptionInfo;
import galdr.spy.WorldInfo;
import grim.annotations.OmitType;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Spy implementation.
 */
@OmitType( unless = "galdr.enable_spies" )
final class SpyImpl
  implements Spy
{
  /**
   * The world that this spy is associated wtih.
   */
  @Nonnull
  private final World _world;
  /**
   * Support infrastructure for interacting with spy event handlers..
   */
  @Nonnull
  private final SpyEventHandlerSupport _spyEventHandlerSupport = new SpyEventHandlerSupport();

  SpyImpl( @Nonnull final World world )
  {
    _world = Objects.requireNonNull( world );
  }

  @Override
  public void addSpyEventHandler( @Nonnull final SpyEventHandler handler )
  {
    _spyEventHandlerSupport.addSpyEventHandler( handler );
  }

  @Override
  public void removeSpyEventHandler( @Nonnull final SpyEventHandler handler )
  {
    _spyEventHandlerSupport.removeSpyEventHandler( handler );
  }

  @Override
  public void reportSpyEvent( @Nonnull final Object event )
  {
    _spyEventHandlerSupport.reportSpyEvent( event );
  }

  @Override
  public boolean willPropagateSpyEvents()
  {
    return _spyEventHandlerSupport.willPropagateSpyEvents();
  }

  @Nonnull
  @Override
  public WorldInfo asWorldInfo()
  {
    return _world.asInfo();
  }

  @Nonnull
  @Override
  public ComponentInfo getComponentByType( @Nonnull final Class<?> componentType )
  {
    return _world.getComponentManagerByType( componentType ).asInfo();
  }

  @Nonnull
  @Override
  public List<ComponentInfo> getComponents()
  {
    return Collections.unmodifiableList( _world.getComponentManagers()
                                           .stream()
                                           .map( ComponentManager::asInfo )
                                           .collect( Collectors.toList() ) );
  }

  @Nonnull
  @Override
  public Map<AreaOfInterest, CollectionInfo> getCollections()
  {
    return Collections.unmodifiableMap( _world.getEntityCollections()
                                          .values()
                                          .stream()
                                          .map( EntityCollection::asInfo )
                                          .collect( Collectors.toMap( CollectionInfo::getAreaOfInterest,
                                                                      Function.identity() ) ) );
  }

  @Nullable
  @Override
  public SubscriptionInfo findSubscriptionById( final int id )
  {
    final Subscription subscription = _world.getSubscriptions().get( id );
    return null == subscription ? null : subscription.asInfo();
  }

  @Nonnull
  @Override
  public Collection<SubscriptionInfo> getSubscriptions()
  {
    return Collections.unmodifiableCollection( _world.getSubscriptions()
                                                 .values()
                                                 .stream()
                                                 .map( Subscription::asInfo )
                                                 .collect( Collectors.toList() ) );
  }
}
