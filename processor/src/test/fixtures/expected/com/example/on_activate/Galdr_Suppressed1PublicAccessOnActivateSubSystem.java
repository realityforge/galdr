package com.example.on_activate;

import galdr.Galdr;
import galdr.World;
import galdr.internal.OnActivateFn;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("galdr.processor.SubSystemProcessor")
public final class Galdr_Suppressed1PublicAccessOnActivateSubSystem implements OnActivateFn {
  @Nonnull
  private final EnhancedSubSystem _subsystem = new EnhancedSubSystem( this );

  @Nonnull
  private World $galdr$_getWorld() {
    return World.current();
  }

  @Override
  public void activate() {
    _subsystem.$galdr$_onActivate();
  }

  private static final class EnhancedSubSystem extends Suppressed1PublicAccessOnActivateSubSystem {
    @Nonnull
    private final Galdr_Suppressed1PublicAccessOnActivateSubSystem $galdr$_outer;

    private EnhancedSubSystem(
        @Nonnull final Galdr_Suppressed1PublicAccessOnActivateSubSystem outer) {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    private void $galdr$_onActivate() {
      onActivate();
    }

    @Nonnull
    private String $galdr$_getName() {
      return "Suppressed1PublicAccessOnActivateSubSystem";
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
