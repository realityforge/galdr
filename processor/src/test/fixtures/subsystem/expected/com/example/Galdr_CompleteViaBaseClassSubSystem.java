package com.example;

import com.example.other.CompleteBaseSubSystem;
import galdr.ComponentManager;
import galdr.Galdr;
import galdr.SubSystem;
import galdr.World;
import galdr.internal.OnActivateFn;
import galdr.internal.OnDeactivateFn;
import galdr.internal.PostConstructFn;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("galdr.processor.SubSystemProcessor")
public final class Galdr_CompleteViaBaseClassSubSystem implements PostConstructFn, OnActivateFn, OnDeactivateFn, SubSystem {
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

  @Override
  public void process(final int delta) {
    _subsystem.$galdr$_process( delta );
  }

  private static final class EnhancedSubSystem extends CompleteViaBaseClassSubSystem {
    @Nonnull
    private final Galdr_CompleteViaBaseClassSubSystem $galdr$_outer;

    @Nullable
    private ComponentManager<CompleteBaseSubSystem.MyComponent1> $galdrc$_cm1;

    @Nullable
    private ComponentManager<CompleteBaseSubSystem.MyComponent2> $galdrc$_cm2;

    private EnhancedSubSystem(@Nonnull final Galdr_CompleteViaBaseClassSubSystem outer) {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    @Override
    @Nonnull
    protected ComponentManager<CompleteBaseSubSystem.MyComponent1> cm1() {
      assert null != $galdrc$_cm1;
      return $galdrc$_cm1;
    }

    @Override
    @Nonnull
    protected ComponentManager<CompleteBaseSubSystem.MyComponent2> cm2() {
      assert null != $galdrc$_cm2;
      return $galdrc$_cm2;
    }

    @Override
    @Nonnull
    protected String name1() {
      return $galdr$_getName();
    }

    @Override
    @Nonnull
    protected String name2() {
      return $galdr$_getName();
    }

    @Override
    @Nonnull
    protected World world1() {
      return $galdr$_outer.$galdr$_getWorld();
    }

    @Override
    @Nonnull
    protected World world2() {
      return $galdr$_outer.$galdr$_getWorld();
    }

    private void $galdr$_postConstruct() {
      $galdrc$_cm1 = $galdr$_outer.$galdr$_getWorld().getComponentByType( CompleteBaseSubSystem.MyComponent1.class );
      $galdrc$_cm2 = $galdr$_outer.$galdr$_getWorld().getComponentByType( CompleteBaseSubSystem.MyComponent2.class );
    }

    private void $galdr$_onActivate() {
      onActivate1();
      onActivate2();
    }

    private void $galdr$_onDeactivate() {
      onDeactivate1();
      onDeactivate2();
    }

    private void $galdr$_process(final int delta) {
      runFrame1();
      runFrame2();
    }

    @Nonnull
    private String $galdr$_getName() {
      return "CompleteViaBaseClassSubSystem";
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
