package com.example.on_activate;

import galdr.Galdr;
import galdr.SubSystem;
import galdr.World;
import galdr.internal.OnActivateFn;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.processing.Generated;

@Generated("galdr.processor.SubSystemProcessor")
public final class Galdr_MultiOnActivateSubSystem implements OnActivateFn, SubSystem {
  @Nonnull
  private final EnhancedSubSystem _subsystem = new EnhancedSubSystem( this );

  @Nonnull
  private World $galdr$_getWorld() {
    return World.current();
  }

  @Override
  public void activate() {
    _subsystem.$galdr$_onActivate();
  }

  @Override
  public void process(final int delta) {
    _subsystem.$galdr$_process( delta );
  }

  private static final class EnhancedSubSystem extends MultiOnActivateSubSystem {
    @Nonnull
    private final Galdr_MultiOnActivateSubSystem $galdr$_outer;

    private EnhancedSubSystem(@Nonnull final Galdr_MultiOnActivateSubSystem outer) {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    private void $galdr$_onActivate() {
      onActivate1();
      onActivate2();
    }

    private void $galdr$_process(final int delta) {
      runFrame();
    }

    @Nonnull
    private String $galdr$_getName() {
      return "MultiOnActivateSubSystem";
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
