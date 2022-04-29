package com.example.world_ref;

import galdr.Galdr;
import galdr.SubSystem;
import galdr.World;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.processing.Generated;

@Generated("galdr.processor.SubSystemProcessor")
public final class Galdr_BasicWorldRefSubSystem implements SubSystem {
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

  private static final class EnhancedSubSystem extends BasicWorldRefSubSystem {
    @Nonnull
    private final Galdr_BasicWorldRefSubSystem $galdr$_outer;

    private EnhancedSubSystem(@Nonnull final Galdr_BasicWorldRefSubSystem outer) {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    @Override
    @Nonnull
    World world() {
      return $galdr$_outer.$galdr$_getWorld();
    }

    private void $galdr$_process(final int delta) {
      runFrame();
    }

    @Nonnull
    private String $galdr$_getName() {
      return "BasicWorldRefSubSystem";
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
