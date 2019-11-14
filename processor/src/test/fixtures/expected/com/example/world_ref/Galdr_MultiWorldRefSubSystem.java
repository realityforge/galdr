package com.example.world_ref;

import galdr.Galdr;
import galdr.World;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("galdr.processor.SubSystemProcessor")
public final class Galdr_MultiWorldRefSubSystem {
  @Nonnull
  private final EnhancedSubSystem _subsystem = new EnhancedSubSystem( this );

  @Nonnull
  private World $galdr$_getWorld() {
    return World.current();
  }

  private static final class EnhancedSubSystem extends MultiWorldRefSubSystem {
    @Nonnull
    private final Galdr_MultiWorldRefSubSystem $galdr$_outer;

    private EnhancedSubSystem(@Nonnull final Galdr_MultiWorldRefSubSystem outer) {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    @Nonnull
    private String $galdr$_getName() {
      return "MultiWorldRefSubSystem";
    }

    @Override
    @Nonnull
    World world1() {
      return $galdr$_outer.$galdr$_getWorld();
    }

    @Override
    @Nonnull
    World world2() {
      return $galdr$_outer.$galdr$_getWorld();
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
