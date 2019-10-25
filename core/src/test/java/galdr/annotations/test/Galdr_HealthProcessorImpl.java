package galdr.annotations.test;

import galdr.AreaOfInterest;
import galdr.ComponentAPI;
import galdr.Galdr;
import galdr.Subscription;
import galdr.World;
import java.util.Collections;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated( "galdr" )
final class Galdr_HealthProcessorImpl
  extends HealthProcessor
{
  @Nonnull
  private final Galdr_HealthProcessor $galdr$_processor;
  @Nullable
  private Subscription $galdr$_processHealth_subscription;
  @Nullable
  private ComponentAPI<Health> $galdrc$_health;
  private AreaOfInterest $galdr$_processHealth_areaOfInterest;

  Galdr_HealthProcessorImpl( @Nonnull final Galdr_HealthProcessor processor )
  {
    this.$galdr$_processor = Objects.requireNonNull( processor );
  }

  @Nonnull
  @Override
  World world()
  {
    return this.$galdr$_processor.$galdr$_world();
  }

  @Nonnull
  @Override
  ComponentAPI<Health> health()
  {
    assert null != $galdrc$_health;
    return $galdrc$_health;
  }

  void $galdr$_process( final int delta )
  {
    System.out.println( "process(delta=" + delta + ")" );
    assert null != $galdr$_processHealth_subscription;
    $galdr$_processHealth_subscription.beginIteration();
    int entityId;
    while ( -1 != ( entityId = $galdr$_processHealth_subscription.nextEntity() ) )
    {
      processHealth( delta, entityId );
    }
  }

  void $galdr$_postConstruct()
  {
    $galdr$_processHealth_areaOfInterest = world().createAreaOfInterest( Collections.singleton( Health.class ) );
    $galdrc$_health = world().getComponentByType( Health.class );
  }

  void $galdr$_activate()
  {
    $galdr$_processHealth_subscription =
      world().createSubscription( Galdr.areNamesEnabled() ? getName() : null, $galdr$_processHealth_areaOfInterest );
  }

  void $galdr$_deactivate()
  {
    assert null != $galdr$_processHealth_subscription;
    $galdr$_processHealth_subscription.dispose();
    $galdr$_processHealth_subscription = null;
  }

  @Nonnull
  public final String getName()
  {
    return "HealthProcessor";
  }

  @Override
  public String toString()
  {
    if ( Galdr.areDebugToStringMethodsEnabled() )
    {
      return "SubSystem[HealthProcessor]";
    }
    else
    {
      return super.toString();
    }
  }
}
