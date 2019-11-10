package com.example;

import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("galdr.processor.SubSystemProcessor")
public final class Galdr_PackageAccessSubSystem {
  private static final class EnhancedSubSystem {
    @Nonnull
    final Galdr_PackageAccessSubSystem $galdr$_outer;

    private EnhancedSubSystem(@Nonnull final Galdr_PackageAccessSubSystem outer) {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    @Nonnull
    String $galdr$_getName() {
      return "packageAccessSubSystem";
    }
  }
}
