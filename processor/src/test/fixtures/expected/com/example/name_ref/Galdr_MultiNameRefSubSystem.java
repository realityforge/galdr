package com.example.name_ref;

import galdr.Galdr;
import galdr.World;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("galdr.processor.SubSystemProcessor")
public final class Galdr_MultiNameRefSubSystem {
  @Nonnull
  private final EnhancedSubSystem _subsystem = new EnhancedSubSystem( this );

  @Nonnull
  private World $galdr$_getWorld() {
    return World.current();
  }

  private static final class EnhancedSubSystem extends MultiNameRefSubSystem {
    @Nonnull
    final Galdr_MultiNameRefSubSystem $galdr$_outer;

    private EnhancedSubSystem(@Nonnull final Galdr_MultiNameRefSubSystem outer) {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    @Override
    @Nonnull
    String name1() {
      return $galdr$_getName();
    }

    @Override
    @Nonnull
    String name2() {
      return $galdr$_getName();
    }

    @Nonnull
    private String $galdr$_getName() {
      return "MultiNameRefSubSystem";
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
