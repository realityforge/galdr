package galdr.annotations.test;

import galdr.ComponentManager;
import galdr.Galdr;
import galdr.World;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated( "galdr" )
final class Galdr_BootstrapProcessorImpl
  extends BootstrapProcessor
{
  @Nonnull
  private final Galdr_BootstrapProcessor $galdr$_processor;
  @Nullable
  private ComponentManager<Health> $galdrc$_health;

  Galdr_BootstrapProcessorImpl( @Nonnull final Galdr_BootstrapProcessor processor )
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
  ComponentManager<Health> health()
  {
    assert null != $galdrc$_health;
    return $galdrc$_health;
  }

  void $galdr$_process( final int delta )
  {
    processGlobalActions();
  }

  void $galdr$_postConstruct()
  {
    $galdrc$_health = world().getComponentByType( Health.class );
  }

  @Nonnull
  @Override
  final String getName()
  {
    return "BootstrapProcessor";
  }

  @Override
  public String toString()
  {
    if ( Galdr.areDebugToStringMethodsEnabled() )
    {
      return "SubSystem[" + getName() + "]";
    }
    else
    {
      return super.toString();
    }
  }
}
