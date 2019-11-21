import galdr.Stage;
import galdr.World;
import galdr.Worlds;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("galdr.processor.ApplicationProcessor")
final class Galdr_DefaultPackageApplication extends DefaultPackageApplication {
  @Nonnull
  private final Stage _sim;

  Galdr_DefaultPackageApplication() {
    final World world = Worlds
        .world()
        .stage( "sim" )
        .endStage()
        .build();
    _sim = world.getStageByName( "sim" );
  }

  @Override
  @Nonnull
  Stage sim() {
    return _sim;
  }
}
