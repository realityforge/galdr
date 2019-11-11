package com.example.name_ref;

import galdr.Galdr;
import galdr.World;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("galdr.processor.SubSystemProcessor")
public final class Galdr_PackageAccessNameRefSubSystem {
  @Nonnull
  private World $galdr$_getWorld() {
    return World.current();
  }

  private static final class EnhancedSubSystem {
    @Nonnull
    final Galdr_PackageAccessNameRefSubSystem $galdr$_outer;

    private EnhancedSubSystem(@Nonnull final Galdr_PackageAccessNameRefSubSystem outer) {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    @Nonnull
    String name() {
      return $galdr$_getName();
    }

    @Nonnull
    private String $galdr$_getName() {
      return "PackageAccessNameRefSubSystem";
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
