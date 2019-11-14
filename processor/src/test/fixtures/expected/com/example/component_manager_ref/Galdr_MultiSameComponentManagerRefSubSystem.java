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
public final class Galdr_MultiSameComponentManagerRefSubSystem implements PostConstructFn {
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

  private static final class EnhancedSubSystem extends MultiSameComponentManagerRefSubSystem {
    @Nonnull
    private final Galdr_MultiSameComponentManagerRefSubSystem $galdr$_outer;

    @Nullable
    private ComponentManager<MultiSameComponentManagerRefSubSystem.MyComponent1> $galdrc$_cm1;

    @Nullable
    private ComponentManager<MultiSameComponentManagerRefSubSystem.MyComponent1> $galdrc$_cm2;

    private EnhancedSubSystem(@Nonnull final Galdr_MultiSameComponentManagerRefSubSystem outer) {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    @Override
    @Nonnull
    ComponentManager<MultiSameComponentManagerRefSubSystem.MyComponent1> cm1() {
      assert null != $galdrc$_cm1;
      return $galdrc$_cm1;
    }

    @Override
    @Nonnull
    ComponentManager<MultiSameComponentManagerRefSubSystem.MyComponent1> cm2() {
      assert null != $galdrc$_cm2;
      return $galdrc$_cm2;
    }

    @Nonnull
    private String $galdr$_getName() {
      return "MultiSameComponentManagerRefSubSystem";
    }

    private void $galdr$_postConstruct() {
      $galdrc$_cm1 = $galdr$_outer.$galdr$_getWorld().getComponentByType( MultiSameComponentManagerRefSubSystem.MyComponent1.class );
      $galdrc$_cm2 = $galdr$_outer.$galdr$_getWorld().getComponentByType( MultiSameComponentManagerRefSubSystem.MyComponent1.class );
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
