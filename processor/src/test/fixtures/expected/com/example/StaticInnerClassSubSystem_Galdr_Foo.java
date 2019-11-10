package com.example;

import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("galdr.processor.SubSystemProcessor")
public final class StaticInnerClassSubSystem_Galdr_Foo {
  private static final class EnhancedSubSystem {
    @Nonnull
    final StaticInnerClassSubSystem_Galdr_Foo $galdr$_outer;

    private EnhancedSubSystem(@Nonnull final StaticInnerClassSubSystem_Galdr_Foo outer) {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    @Nonnull
    String $galdr$_getName() {
      return "foo";
    }
  }
}
