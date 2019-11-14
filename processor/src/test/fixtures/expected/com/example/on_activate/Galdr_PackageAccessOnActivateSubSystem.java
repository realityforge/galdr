package com.example.on_activate;

import galdr.Galdr;
import galdr.World;
import galdr.internal.PostConstructFn;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("galdr.processor.SubSystemProcessor")
public final class Galdr_PackageAccessOnActivateSubSystem implements PostConstructFn {
  @Nonnull
  private final EnhancedSubSystem _subsystem = new EnhancedSubSystem( this );

  @Nonnull
  private World $galdr$_getWorld() {
    return World.current();
  }

  @Override
  public void postConstruct() {
    _subsystem.$galdr$_postConstruct();
  }

  private static final class EnhancedSubSystem extends PackageAccessOnActivateSubSystem {
    @Nonnull
    private final Galdr_PackageAccessOnActivateSubSystem $galdr$_outer;

    private EnhancedSubSystem(@Nonnull final Galdr_PackageAccessOnActivateSubSystem outer) {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    private void $galdr$_postConstruct() {
      onActivate();
    }

    @Nonnull
    private String $galdr$_getName() {
      return "PackageAccessOnActivateSubSystem";
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
