package com.example.on_deactivate;

import galdr.Galdr;
import galdr.World;
import galdr.internal.OnDeactivateFn;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("galdr.processor.SubSystemProcessor")
public final class Galdr_PackageAccessOnDeactivateSubSystem implements OnDeactivateFn {
  @Nonnull
  private final EnhancedSubSystem _subsystem = new EnhancedSubSystem( this );

  @Nonnull
  private World $galdr$_getWorld() {
    return World.current();
  }

  @Override
  public void deactivate() {
    _subsystem.$galdr$_onDeactivate();
  }

  private static final class EnhancedSubSystem extends PackageAccessOnDeactivateSubSystem {
    @Nonnull
    private final Galdr_PackageAccessOnDeactivateSubSystem $galdr$_outer;

    private EnhancedSubSystem(@Nonnull final Galdr_PackageAccessOnDeactivateSubSystem outer) {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    private void $galdr$_onDeactivate() {
      onDeactivate();
    }

    @Nonnull
    private String $galdr$_getName() {
      return "PackageAccessOnDeactivateSubSystem";
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