package galdr.annotations.test;

import galdr.ComponentManager;
import galdr.Galdr;
import galdr.SubSystem;
import galdr.World;
import galdr.internal.PostConstructFn;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated( "galdr" )
public final class Galdr_BootstrapProcessor
  implements SubSystem, PostConstructFn
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

  private static final class EnhancedSubSystem
    extends BootstrapProcessor
  {
    @Nonnull
    private final Galdr_BootstrapProcessor $galdr$_outer;
    @Nullable
    private ComponentManager<Health> $galdrc$_health;

    private EnhancedSubSystem( @Nonnull final Galdr_BootstrapProcessor outer )
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
      processGlobalActions();
    }

    private void $galdr$_postConstruct()
    {
      $galdrc$_health = $galdr$_outer.$galdr$_world().getComponentByType( Health.class );
    }

    @Nonnull
    @Override
    String getName()
    {
      return $galdr$_getName();
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
