package com.example.on_deactivate;

import galdr.Galdr;
import galdr.World;
import galdr.internal.OnDeactivateFn;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("galdr.processor.SubSystemProcessor")
public final class Galdr_Suppressed1ProtectedAccessOnDeactivateSubSystem implements OnDeactivateFn {
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

  private static final class EnhancedSubSystem extends Suppressed1ProtectedAccessOnDeactivateSubSystem {
    @Nonnull
    private final Galdr_Suppressed1ProtectedAccessOnDeactivateSubSystem $galdr$_outer;

    private EnhancedSubSystem(
        @Nonnull final Galdr_Suppressed1ProtectedAccessOnDeactivateSubSystem outer) {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    private void $galdr$_onDeactivate() {
      onDeactivate();
    }

    @Nonnull
    private String $galdr$_getName() {
      return "Suppressed1ProtectedAccessOnDeactivateSubSystem";
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
