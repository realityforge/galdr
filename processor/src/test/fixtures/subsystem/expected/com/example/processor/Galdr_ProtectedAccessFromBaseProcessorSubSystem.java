package com.example.processor;

import galdr.Galdr;
import galdr.SubSystem;
import galdr.World;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.processing.Generated;

@Generated("galdr.processor.SubSystemProcessor")
public final class Galdr_ProtectedAccessFromBaseProcessorSubSystem implements SubSystem {
  @Nonnull
  private final EnhancedSubSystem _subsystem = new EnhancedSubSystem( this );

  @Nonnull
  private World $galdr$_getWorld() {
    return World.current();
  }

  @Override
  public void process(final int delta) {
    _subsystem.$galdr$_process( delta );
  }

  private static final class EnhancedSubSystem extends ProtectedAccessFromBaseProcessorSubSystem {
    @Nonnull
    private final Galdr_ProtectedAccessFromBaseProcessorSubSystem $galdr$_outer;

    private EnhancedSubSystem(
        @Nonnull final Galdr_ProtectedAccessFromBaseProcessorSubSystem outer) {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    private void $galdr$_process(final int delta) {
      runFrame();
    }

    @Nonnull
    private String $galdr$_getName() {
      return "ProtectedAccessFromBaseProcessorSubSystem";
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
