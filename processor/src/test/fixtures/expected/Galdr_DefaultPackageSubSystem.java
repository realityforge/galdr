import galdr.Galdr;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("galdr.processor.SubSystemProcessor")
public final class Galdr_DefaultPackageSubSystem {
  private static final class EnhancedSubSystem {
    @Nonnull
    final Galdr_DefaultPackageSubSystem $galdr$_outer;

    private EnhancedSubSystem(@Nonnull final Galdr_DefaultPackageSubSystem outer) {
      $galdr$_outer = Objects.requireNonNull( outer );
    }

    @Nonnull
    private String $galdr$_getName() {
      return "DefaultPackageSubSystem";
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
