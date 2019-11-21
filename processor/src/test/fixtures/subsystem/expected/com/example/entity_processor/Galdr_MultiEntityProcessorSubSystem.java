package com.example.entity_processor;

import galdr.AreaOfInterest;
import galdr.Galdr;
import galdr.SubSystem;
import galdr.Subscription;
import galdr.World;
import galdr.internal.OnActivateFn;
import galdr.internal.OnDeactivateFn;
import galdr.internal.PostConstructFn;
import java.util.Collections;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("galdr.processor.SubSystemProcessor")
public final class Galdr_MultiEntityProcessorSubSystem implements PostConstructFn, OnActivateFn, OnDeactivateFn, SubSystem {
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

  private static final class EnhancedSubSystem extends MultiEntityProcessorSubSystem {
    @Nonnull
    private final Galdr_MultiEntityProcessorSubSystem $galdr$_outer;

    @Nullable
    private Subscription $galdrs$_process1;

    @Nullable
    private AreaOfInterest $galdraoi$_process1;

    @Nullable
    private Subscription $galdrs$_process2;

    @Nullable
    private AreaOfInterest $galdraoi$_process2;

    private EnhancedSubSystem(@Nonnull final Galdr_MultiEntityProcessorSubSystem outer) {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    private void $galdr$_postConstruct() {
      $galdraoi$_process1 = $galdr$_outer.$galdr$_getWorld().createAreaOfInterest( Collections.singleton( MultiEntityProcessorSubSystem.MyComponent1.class ) );
      $galdraoi$_process2 = $galdr$_outer.$galdr$_getWorld().createAreaOfInterest( Collections.singleton( MultiEntityProcessorSubSystem.MyComponent2.class ) );
    }

    private void $galdr$_onActivate() {
      assert null == $galdrs$_process1;
      $galdrs$_process1 = $galdr$_outer.$galdr$_getWorld().createSubscription( Galdr.areNamesEnabled() ? $galdr$_getName() : null, $galdraoi$_process1 );
      assert null == $galdrs$_process2;
      $galdrs$_process2 = $galdr$_outer.$galdr$_getWorld().createSubscription( Galdr.areNamesEnabled() ? $galdr$_getName() : null, $galdraoi$_process2 );
    }

    private void $galdr$_onDeactivate() {
      assert null != $galdrs$_process1;
      $galdrs$_process1.dispose();
      $galdrs$_process1 = null;
      assert null != $galdrs$_process2;
      $galdrs$_process2.dispose();
      $galdrs$_process2 = null;
    }

    private void $galdr$_process(final int delta) {
      assert null != $galdrs$_process1;
      $galdrs$_process1.beginIteration();
      int $galdrs$_process1_entityId;
      while ( -1 != ( $galdrs$_process1_entityId = $galdrs$_process1.nextEntity() ) ) {
        process1( $galdrs$_process1_entityId );
      }
      assert null != $galdrs$_process2;
      $galdrs$_process2.beginIteration();
      int $galdrs$_process2_entityId;
      while ( -1 != ( $galdrs$_process2_entityId = $galdrs$_process2.nextEntity() ) ) {
        process2( $galdrs$_process2_entityId );
      }
    }

    @Nonnull
    private String $galdr$_getName() {
      return "MultiEntityProcessorSubSystem";
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
