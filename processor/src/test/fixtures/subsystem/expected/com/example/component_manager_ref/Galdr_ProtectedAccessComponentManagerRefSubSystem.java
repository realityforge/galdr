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
public final class Galdr_ProtectedAccessComponentManagerRefSubSystem implements PostConstructFn {
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

  private static final class EnhancedSubSystem extends ProtectedAccessComponentManagerRefSubSystem {
    @Nonnull
    private final Galdr_ProtectedAccessComponentManagerRefSubSystem $galdr$_outer;

    @Nullable
    private ComponentManager<ProtectedAccessComponentManagerRefSubSystem.MyComponent> $galdrc$_cm;

    private EnhancedSubSystem(
        @Nonnull final Galdr_ProtectedAccessComponentManagerRefSubSystem outer) {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    @Override
    @Nonnull
    protected ComponentManager<ProtectedAccessComponentManagerRefSubSystem.MyComponent> cm() {
      assert null != $galdrc$_cm;
      return $galdrc$_cm;
    }

    private void $galdr$_postConstruct() {
      $galdrc$_cm = $galdr$_outer.$galdr$_getWorld().getComponentByType( ProtectedAccessComponentManagerRefSubSystem.MyComponent.class );
    }

    @Nonnull
    private String $galdr$_getName() {
      return "ProtectedAccessComponentManagerRefSubSystem";
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
