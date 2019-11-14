package com.example.component_manager_ref;

import galdr.ComponentManager;
import galdr.Galdr;
import galdr.World;
import galdr.internal.PostConstructFn;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("galdr.processor.SubSystemProcessor")
public final class Galdr_Suppressed1PublicAccessComponentManagerRefSubSystem implements PostConstructFn {
  @Nonnull
  private final EnhancedSubSystem _subsystem = new EnhancedSubSystem( this );

  @Nonnull
  private World $galdr$_getWorld() {
    return World.current();
  }

  @Override
  public void postConstruct() {
    _subsystem.$galdr$_postConstruct();
  }

  private static final class EnhancedSubSystem extends Suppressed1PublicAccessComponentManagerRefSubSystem {
    @Nonnull
    private final Galdr_Suppressed1PublicAccessComponentManagerRefSubSystem $galdr$_outer;

    @Nullable
    private ComponentManager<Suppressed1PublicAccessComponentManagerRefSubSystem.MyComponent> $galdrc$_cm;

    private EnhancedSubSystem(
        @Nonnull final Galdr_Suppressed1PublicAccessComponentManagerRefSubSystem outer) {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    @Override
    @Nonnull
    public ComponentManager<Suppressed1PublicAccessComponentManagerRefSubSystem.MyComponent> cm() {
      assert null != $galdrc$_cm;
      return $galdrc$_cm;
    }

    @Nonnull
    private String $galdr$_getName() {
      return "Suppressed1PublicAccessComponentManagerRefSubSystem";
    }

    private void $galdr$_postConstruct() {
      $galdrc$_cm = $galdr$_outer.$galdr$_getWorld().getComponentByType( Suppressed1PublicAccessComponentManagerRefSubSystem.MyComponent.class );
    }

    @Nonnull
    @Override
    public String toString() {
      if ( Galdr.areDebugToStringMethodsEnabled() ) {
        return "SubSystem[" + $galdr$_getName() + "]";
      } else {
        return super.toString();
      }
    }
  }
}
