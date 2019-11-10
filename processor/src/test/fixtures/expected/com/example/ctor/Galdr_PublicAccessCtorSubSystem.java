package com.example.ctor;

import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("galdr.processor.SubSystemProcessor")
public final class Galdr_PublicAccessCtorSubSystem {
  private static final class EnhancedSubSystem {
    @Nonnull
    final Galdr_PublicAccessCtorSubSystem $galdr$_outer;

    private EnhancedSubSystem(@Nonnull final Galdr_PublicAccessCtorSubSystem outer) {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    @Nonnull
    String $galdr$_getName() {
      return "PublicAccessCtorSubSystem";
    }
  }
}
