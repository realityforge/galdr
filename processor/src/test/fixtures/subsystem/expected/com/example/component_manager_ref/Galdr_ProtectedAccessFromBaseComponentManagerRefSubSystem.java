package com.example.component_manager_ref;

import com.example.component_manager_ref.other.BaseProtectedAccessComponentManagerRefSubSystem;
import galdr.ComponentManager;
import galdr.Galdr;
import galdr.SubSystem;
import galdr.World;
import galdr.internal.PostConstructFn;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("galdr.processor.SubSystemProcessor")
public final class Galdr_ProtectedAccessFromBaseComponentManagerRefSubSystem implements PostConstructFn, SubSystem {
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

  private static final class EnhancedSubSystem extends ProtectedAccessFromBaseComponentManagerRefSubSystem {
    @Nonnull
    private final Galdr_ProtectedAccessFromBaseComponentManagerRefSubSystem $galdr$_outer;

    @Nullable
    private ComponentManager<BaseProtectedAccessComponentManagerRefSubSystem.MyComponent> $galdrc$_cm;

    private EnhancedSubSystem(
        @Nonnull final Galdr_ProtectedAccessFromBaseComponentManagerRefSubSystem outer) {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    @Override
    @Nonnull
    protected ComponentManager<BaseProtectedAccessComponentManagerRefSubSystem.MyComponent> cm() {
      assert null != $galdrc$_cm;
      return $galdrc$_cm;
    }

    private void $galdr$_postConstruct() {
      $galdrc$_cm = $galdr$_outer.$galdr$_getWorld().getComponentByType( BaseProtectedAccessComponentManagerRefSubSystem.MyComponent.class );
    }

    private void $galdr$_process(final int delta) {
      runFrame();
    }

    @Nonnull
    private String $galdr$_getName() {
      return "ProtectedAccessFromBaseComponentManagerRefSubSystem";
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
