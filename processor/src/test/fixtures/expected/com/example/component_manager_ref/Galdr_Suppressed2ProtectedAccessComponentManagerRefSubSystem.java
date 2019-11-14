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
public final class Galdr_Suppressed2ProtectedAccessComponentManagerRefSubSystem implements PostConstructFn {
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

  private static final class EnhancedSubSystem extends Suppressed2ProtectedAccessComponentManagerRefSubSystem {
    @Nonnull
    private final Galdr_Suppressed2ProtectedAccessComponentManagerRefSubSystem $galdr$_outer;

    @Nullable
    private ComponentManager<Suppressed2ProtectedAccessComponentManagerRefSubSystem.MyComponent> $galdrc$_cm;

    private EnhancedSubSystem(
        @Nonnull final Galdr_Suppressed2ProtectedAccessComponentManagerRefSubSystem outer) {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    @Override
    @Nonnull
    protected ComponentManager<Suppressed2ProtectedAccessComponentManagerRefSubSystem.MyComponent> cm(
        ) {
      assert null != $galdrc$_cm;
      return $galdrc$_cm;
    }

    @Nonnull
    private String $galdr$_getName() {
      return "Suppressed2ProtectedAccessComponentManagerRefSubSystem";
    }

    private void $galdr$_postConstruct() {
      $galdrc$_cm = $galdr$_outer.$galdr$_getWorld().getComponentByType( Suppressed2ProtectedAccessComponentManagerRefSubSystem.MyComponent.class );
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
