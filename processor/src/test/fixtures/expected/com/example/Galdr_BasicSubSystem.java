package com.example;

import galdr.Galdr;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("galdr.processor.SubSystemProcessor")
public final class Galdr_BasicSubSystem {
  private static final class EnhancedSubSystem {
    @Nonnull
    final Galdr_BasicSubSystem $galdr$_outer;

    private EnhancedSubSystem(@Nonnull final Galdr_BasicSubSystem outer) {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    @Nonnull
    private String $galdr$_getName() {
      return "BasicSubSystem";
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
