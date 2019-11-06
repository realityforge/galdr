package galdr.annotations.test;

import galdr.ComponentManager;
import galdr.Galdr;
import galdr.ProcessorFn;
import galdr.World;
import galdr.internal.PostConstructFn;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated( "galdr" )
final class Galdr_BootstrapProcessor
  implements ProcessorFn, PostConstructFn
{
  @Nonnull
  private final ProcessorImpl _processor = new ProcessorImpl( this );

  @Nonnull
  World $galdr$_world()
  {
    return World.current();
  }

  @Override
  public void process( final int delta )
  {
    _processor.$galdr$_process( delta );
  }

  @Override
  public void postConstruct()
  {
    _processor.$galdr$_postConstruct();
  }

  private static final class ProcessorImpl
    extends BootstrapProcessor
  {
    @Nonnull
    private final Galdr_BootstrapProcessor $galdr$_wrapper;
    @Nullable
    private ComponentManager<Health> $galdrc$_health;

    private ProcessorImpl( @Nonnull final Galdr_BootstrapProcessor processor )
    {
      $galdr$_wrapper = Objects.requireNonNull( processor );
    }

    @Nonnull
    @Override
    World world()
    {
      return $galdr$_wrapper.$galdr$_world();
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
      processGlobalActions();
    }

    private void $galdr$_postConstruct()
    {
      $galdrc$_health = $galdr$_wrapper.$galdr$_world().getComponentByType( Health.class );
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
}