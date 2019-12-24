package com.example.component_manager_ref;

import galdr.ComponentManager;
import galdr.Galdr;
import galdr.SubSystem;
import galdr.World;
import galdr.internal.PostConstructFn;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("galdr.processor.SubSystemProcessor")
public final class Galdr_Suppressed1ProtectedAccessComponentManagerRefSubSystem implements PostConstructFn, SubSystem {
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

  @Override
  public void process(final int delta) {
    _subsystem.$galdr$_process( delta );
  }

  private static final class EnhancedSubSystem extends Suppressed1ProtectedAccessComponentManagerRefSubSystem {
    @Nonnull
    private final Galdr_Suppressed1ProtectedAccessComponentManagerRefSubSystem $galdr$_outer;

    @Nullable
    private ComponentManager<Suppressed1ProtectedAccessComponentManagerRefSubSystem.MyComponent> $galdrc$_cm;

    private EnhancedSubSystem(
        @Nonnull final Galdr_Suppressed1ProtectedAccessComponentManagerRefSubSystem outer) {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    @Override
    @Nonnull
    protected ComponentManager<Suppressed1ProtectedAccessComponentManagerRefSubSystem.MyComponent> cm(
        ) {
      assert null != $galdrc$_cm;
      return $galdrc$_cm;
    }

    private void $galdr$_postConstruct() {
      $galdrc$_cm = $galdr$_outer.$galdr$_getWorld().getComponentByType( Suppressed1ProtectedAccessComponentManagerRefSubSystem.MyComponent.class );
    }

    private void $galdr$_process(final int delta) {
      runFrame();
    }

    @Nonnull
    private String $galdr$_getName() {
      return "Suppressed1ProtectedAccessComponentManagerRefSubSystem";
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
