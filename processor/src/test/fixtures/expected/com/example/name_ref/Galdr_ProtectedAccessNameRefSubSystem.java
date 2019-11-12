package com.example.name_ref;

import galdr.Galdr;
import galdr.World;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("galdr.processor.SubSystemProcessor")
public final class Galdr_ProtectedAccessNameRefSubSystem {
  @Nonnull
  private final EnhancedSubSystem _subsystem = new EnhancedSubSystem( this );

  @Nonnull
  private World $galdr$_getWorld() {
    return World.current();
  }

  private static final class EnhancedSubSystem extends ProtectedAccessNameRefSubSystem {
    @Nonnull
    private final Galdr_ProtectedAccessNameRefSubSystem $galdr$_outer;

    private EnhancedSubSystem(@Nonnull final Galdr_ProtectedAccessNameRefSubSystem outer) {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    @Override
    @Nonnull
    protected String name() {
      return $galdr$_getName();
    }

    @Nonnull
    private String $galdr$_getName() {
      return "ProtectedAccessNameRefSubSystem";
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
