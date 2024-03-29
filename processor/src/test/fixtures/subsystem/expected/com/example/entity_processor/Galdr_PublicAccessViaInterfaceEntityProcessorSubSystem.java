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
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("galdr.processor.SubSystemProcessor")
public final class Galdr_PublicAccessViaInterfaceEntityProcessorSubSystem implements PostConstructFn, OnActivateFn, OnDeactivateFn, SubSystem {
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

  private static final class EnhancedSubSystem extends PublicAccessViaInterfaceEntityProcessorSubSystem {
    @Nonnull
    private final Galdr_PublicAccessViaInterfaceEntityProcessorSubSystem $galdr$_outer;

    @Nullable
    private Subscription $galdrs$_process;

    @Nullable
    private AreaOfInterest $galdraoi$_process;

    private EnhancedSubSystem(
        @Nonnull final Galdr_PublicAccessViaInterfaceEntityProcessorSubSystem outer) {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    private void $galdr$_postConstruct() {
      $galdraoi$_process = $galdr$_outer.$galdr$_getWorld().createAreaOfInterest( Collections.singleton( PublicAccessViaInterfaceEntityProcessorSubSystem.MyComponent.class ) );
    }

    private void $galdr$_onActivate() {
      assert null == $galdrs$_process;
      $galdrs$_process = $galdr$_outer.$galdr$_getWorld().createSubscription( Galdr.areNamesEnabled() ? $galdr$_getName() : null, $galdraoi$_process );
    }

    private void $galdr$_onDeactivate() {
      assert null != $galdrs$_process;
      $galdrs$_process.dispose();
      $galdrs$_process = null;
    }

    private void $galdr$_process(final int delta) {
      assert null != $galdrs$_process;
      $galdrs$_process.beginIteration();
      int $galdrs$_process_entityId;
      while ( -1 != ( $galdrs$_process_entityId = $galdrs$_process.nextEntity() ) ) {
        process( $galdrs$_process_entityId );
      }
    }

    @Nonnull
    private String $galdr$_getName() {
      return "PublicAccessViaInterfaceEntityProcessorSubSystem";
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
