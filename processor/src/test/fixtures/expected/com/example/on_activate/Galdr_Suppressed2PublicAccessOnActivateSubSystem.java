package com.example.on_activate;

import galdr.Galdr;
import galdr.ProcessorFn;
import galdr.World;
import galdr.internal.OnActivateFn;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("galdr.processor.SubSystemProcessor")
public final class Galdr_Suppressed2PublicAccessOnActivateSubSystem implements OnActivateFn, ProcessorFn {
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

  private static final class EnhancedSubSystem extends Suppressed2PublicAccessOnActivateSubSystem {
    @Nonnull
    private final Galdr_Suppressed2PublicAccessOnActivateSubSystem $galdr$_outer;

    private EnhancedSubSystem(
        @Nonnull final Galdr_Suppressed2PublicAccessOnActivateSubSystem outer) {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    private void $galdr$_onActivate() {
      onActivate();
    }

    private void $galdr$_process(final int delta) {
      runFrame();
    }

    @Nonnull
    private String $galdr$_getName() {
      return "Suppressed2PublicAccessOnActivateSubSystem";
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
