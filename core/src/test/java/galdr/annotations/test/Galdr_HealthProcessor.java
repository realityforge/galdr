package galdr.annotations.test;

import galdr.AreaOfInterest;
import galdr.ComponentManager;
import galdr.Galdr;
import galdr.ProcessorFn;
import galdr.Subscription;
import galdr.World;
import galdr.internal.OnActivateFn;
import galdr.internal.OnDeactivateFn;
import galdr.internal.PostConstructFn;
import java.util.Collections;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated( "galdr" )
public final class Galdr_HealthProcessor
  implements ProcessorFn, PostConstructFn, OnActivateFn, OnDeactivateFn
{
  @Nonnull
  private final EnhancedSubSystem _subsystem = new EnhancedSubSystem( this );

  @Nonnull
  private World $galdr$_world()
  {
    return World.current();
  }

  @Override
  public void process( final int delta )
  {
    _subsystem.$galdr$_process( delta );
  }

  @Override
  public void postConstruct()
  {
    _subsystem.$galdr$_postConstruct();
  }

  @Override
  public void activate()
  {
    _subsystem.$galdr$_activate();
  }

  @Override
  public void deactivate()
  {
    _subsystem.$galdr$_deactivate();
  }

  private static final class EnhancedSubSystem
    extends HealthProcessor
  {
    @Nonnull
    private final Galdr_HealthProcessor $galdr$_outer;
    @Nullable
    private Subscription $galdr$_processHealth_subscription;
    @Nullable
    private ComponentManager<Health> $galdrc$_health;
    private AreaOfInterest $galdr$_processHealth_areaOfInterest;

    private EnhancedSubSystem( @Nonnull final Galdr_HealthProcessor outer )
    {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    @Nonnull
    @Override
    World world()
    {
      return $galdr$_outer.$galdr$_world();
    }

    @Nonnull
    @Override
    ComponentManager<Health> health()
    {
      assert null != $galdrc$_health;
      return $galdrc$_health;
    }

    private void $galdr$_process( final int delta )
    {
      assert null != $galdr$_processHealth_subscription;
      $galdr$_processHealth_subscription.beginIteration();
      int entityId;
      while ( -1 != ( entityId = $galdr$_processHealth_subscription.nextEntity() ) )
      {
        processHealth( delta, entityId );
      }
    }

    private void $galdr$_postConstruct()
    {
      $galdr$_processHealth_areaOfInterest =
        $galdr$_outer.$galdr$_world().createAreaOfInterest( Collections.singleton( Health.class ) );
      $galdrc$_health = $galdr$_outer.$galdr$_world().getComponentByType( Health.class );
    }

    private void $galdr$_activate()
    {
      $galdr$_processHealth_subscription =
        $galdr$_outer.$galdr$_world().createSubscription( Galdr.areNamesEnabled() ? $galdr$_getName() : null,
                                                          $galdr$_processHealth_areaOfInterest );
    }

    private void $galdr$_deactivate()
    {
      assert null != $galdr$_processHealth_subscription;
      $galdr$_processHealth_subscription.dispose();
      $galdr$_processHealth_subscription = null;
    }

    @Nonnull
    private String $galdr$_getName()
    {
      return "HealthProcessor";
    }

    @Override
    public String toString()
    {
      if ( Galdr.areDebugToStringMethodsEnabled() )
      {
        return "SubSystem[" + $galdr$_getName() + "]";
      }
      else
      {
        return super.toString();
      }
    }
  }
}
