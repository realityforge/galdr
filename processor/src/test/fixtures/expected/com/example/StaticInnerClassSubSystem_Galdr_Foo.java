package com.example;

import galdr.Galdr;
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
    private String $galdr$_getName() {
      return "Foo";
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
