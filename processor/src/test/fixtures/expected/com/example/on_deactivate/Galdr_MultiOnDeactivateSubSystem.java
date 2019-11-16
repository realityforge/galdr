package com.example.on_deactivate;

import galdr.Galdr;
import galdr.World;
import galdr.internal.OnDeactivateFn;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("galdr.processor.SubSystemProcessor")
public final class Galdr_MultiOnDeactivateSubSystem implements OnDeactivateFn {
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

  private static final class EnhancedSubSystem extends MultiOnDeactivateSubSystem {
    @Nonnull
    private final Galdr_MultiOnDeactivateSubSystem $galdr$_outer;

    private EnhancedSubSystem(@Nonnull final Galdr_MultiOnDeactivateSubSystem outer) {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    private void $galdr$_onDeactivate() {
      onDeactivate1();
      onDeactivate2();
    }

    @Nonnull
    private String $galdr$_getName() {
      return "MultiOnDeactivateSubSystem";
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
