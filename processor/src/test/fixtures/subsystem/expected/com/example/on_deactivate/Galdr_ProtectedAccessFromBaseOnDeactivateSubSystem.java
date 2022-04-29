package com.example.on_deactivate;

import galdr.Galdr;
import galdr.SubSystem;
import galdr.World;
import galdr.internal.OnDeactivateFn;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.processing.Generated;

@Generated("galdr.processor.SubSystemProcessor")
public final class Galdr_ProtectedAccessFromBaseOnDeactivateSubSystem implements OnDeactivateFn, SubSystem {
  @Nonnull
  private final EnhancedSubSystem _subsystem = new EnhancedSubSystem( this );

  @Nonnull
  private World $galdr$_getWorld() {
    return World.current();
  }

  @Override
  public void deactivate() {
    _subsystem.$galdr$_onDeactivate();
  }

  @Override
  public void process(final int delta) {
    _subsystem.$galdr$_process( delta );
  }

  private static final class EnhancedSubSystem extends ProtectedAccessFromBaseOnDeactivateSubSystem {
    @Nonnull
    private final Galdr_ProtectedAccessFromBaseOnDeactivateSubSystem $galdr$_outer;

    private EnhancedSubSystem(
        @Nonnull final Galdr_ProtectedAccessFromBaseOnDeactivateSubSystem outer) {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    private void $galdr$_onDeactivate() {
      onDeactivate();
    }

    private void $galdr$_process(final int delta) {
      runFrame();
    }

    @Nonnull
    private String $galdr$_getName() {
      return "ProtectedAccessFromBaseOnDeactivateSubSystem";
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
