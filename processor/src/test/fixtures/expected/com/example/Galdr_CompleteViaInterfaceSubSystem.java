package com.example;

import com.example.other.CompleteInterfaceSubSystem;
import galdr.ComponentManager;
import galdr.Galdr;
import galdr.World;
import galdr.internal.OnActivateFn;
import galdr.internal.OnDeactivateFn;
import galdr.internal.PostConstructFn;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("galdr.processor.SubSystemProcessor")
public final class Galdr_CompleteViaInterfaceSubSystem implements PostConstructFn, OnActivateFn, OnDeactivateFn {
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
  public void activate() {
    _subsystem.$galdr$_onActivate();
  }

  @Override
  public void deactivate() {
    _subsystem.$galdr$_onDeactivate();
  }

  private static final class EnhancedSubSystem extends CompleteViaInterfaceSubSystem {
    @Nonnull
    private final Galdr_CompleteViaInterfaceSubSystem $galdr$_outer;

    @Nullable
    private ComponentManager<CompleteInterfaceSubSystem.MyComponent1> $galdrc$_cm1;

    @Nullable
    private ComponentManager<CompleteInterfaceSubSystem.MyComponent2> $galdrc$_cm2;

    private EnhancedSubSystem(@Nonnull final Galdr_CompleteViaInterfaceSubSystem outer) {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    @Override
    @Nonnull
    public ComponentManager<CompleteInterfaceSubSystem.MyComponent1> cm1() {
      assert null != $galdrc$_cm1;
      return $galdrc$_cm1;
    }

    @Override
    @Nonnull
    public ComponentManager<CompleteInterfaceSubSystem.MyComponent2> cm2() {
      assert null != $galdrc$_cm2;
      return $galdrc$_cm2;
    }

    @Override
    @Nonnull
    public String name1() {
      return $galdr$_getName();
    }

    @Override
    @Nonnull
    public String name2() {
      return $galdr$_getName();
    }

    @Override
    @Nonnull
    public World world1() {
      return $galdr$_outer.$galdr$_getWorld();
    }

    @Override
    @Nonnull
    public World world2() {
      return $galdr$_outer.$galdr$_getWorld();
    }

    private void $galdr$_postConstruct() {
      $galdrc$_cm1 = $galdr$_outer.$galdr$_getWorld().getComponentByType( CompleteInterfaceSubSystem.MyComponent1.class );
      $galdrc$_cm2 = $galdr$_outer.$galdr$_getWorld().getComponentByType( CompleteInterfaceSubSystem.MyComponent2.class );
    }

    private void $galdr$_onActivate() {
      onActivate1();
      onActivate2();
    }

    private void $galdr$_onDeactivate() {
      onDeactivate1();
      onDeactivate2();
    }

    @Nonnull
    private String $galdr$_getName() {
      return "CompleteViaInterfaceSubSystem";
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
