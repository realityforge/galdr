package com.example.name_ref;

import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("galdr.processor.SubSystemProcessor")
public final class Galdr_PublicAccessNameRefSubSystem {
  private static final class EnhancedSubSystem {
    @Nonnull
    final Galdr_PublicAccessNameRefSubSystem $galdr$_outer;

    private EnhancedSubSystem(@Nonnull final Galdr_PublicAccessNameRefSubSystem outer) {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    @Nonnull
    String $galdr$_getName() {
      return "PublicAccessNameRefSubSystem";
    }
  }
}
