package com.example.world_ref;

import galdr.Galdr;
import galdr.World;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("galdr.processor.SubSystemProcessor")
public final class Galdr_PublicAccessViaInterfaceWorldRefSubSystem {
  @Nonnull
  private final EnhancedSubSystem _subsystem = new EnhancedSubSystem( this );

  @Nonnull
  private World $galdr$_getWorld() {
    return World.current();
  }

  private static final class EnhancedSubSystem extends PublicAccessViaInterfaceWorldRefSubSystem {
    @Nonnull
    private final Galdr_PublicAccessViaInterfaceWorldRefSubSystem $galdr$_outer;

    private EnhancedSubSystem(
        @Nonnull final Galdr_PublicAccessViaInterfaceWorldRefSubSystem outer) {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    @Override
    @Nonnull
    public World world() {
      return $galdr$_outer.$galdr$_getWorld();
    }

    @Nonnull
    private String $galdr$_getName() {
      return "PublicAccessViaInterfaceWorldRefSubSystem";
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