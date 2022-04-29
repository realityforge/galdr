package com.example.component_manager_ref;

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
public final class Galdr_MultiComponentManagerRefSubSystem implements PostConstructFn, SubSystem {
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

  private static final class EnhancedSubSystem extends MultiComponentManagerRefSubSystem {
    @Nonnull
    private final Galdr_MultiComponentManagerRefSubSystem $galdr$_outer;

    @Nullable
    private ComponentManager<MultiComponentManagerRefSubSystem.MyComponent1> $galdrc$_cm1;

    @Nullable
    private ComponentManager<MultiComponentManagerRefSubSystem.MyComponent2> $galdrc$_cm2;

    private EnhancedSubSystem(@Nonnull final Galdr_MultiComponentManagerRefSubSystem outer) {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    @Override
    @Nonnull
    ComponentManager<MultiComponentManagerRefSubSystem.MyComponent1> cm1() {
      assert null != $galdrc$_cm1;
      return $galdrc$_cm1;
    }

    @Override
    @Nonnull
    ComponentManager<MultiComponentManagerRefSubSystem.MyComponent2> cm2() {
      assert null != $galdrc$_cm2;
      return $galdrc$_cm2;
    }

    private void $galdr$_postConstruct() {
      $galdrc$_cm1 = $galdr$_outer.$galdr$_getWorld().getComponentByType( MultiComponentManagerRefSubSystem.MyComponent1.class );
      $galdrc$_cm2 = $galdr$_outer.$galdr$_getWorld().getComponentByType( MultiComponentManagerRefSubSystem.MyComponent2.class );
    }

    private void $galdr$_process(final int delta) {
      runFrame();
    }

    @Nonnull
    private String $galdr$_getName() {
      return "MultiComponentManagerRefSubSystem";
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
