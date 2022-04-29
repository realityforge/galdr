import galdr.Stage;
import galdr.World;
import galdr.Worlds;
import javax.annotation.Nonnull;
import javax.annotation.processing.Generated;

@Generated("galdr.processor.ApplicationProcessor")
final class Galdr_DefaultPackageApplication extends DefaultPackageApplication {
  @Nonnull
  private final Stage _sim;

  Galdr_DefaultPackageApplication() {
    final World world = Worlds
        .world()
        .component( DefaultPackageApplication.MyComponent.class )
        .stage( "sim" )
        .subSystem( new DefaultPackageApplication_Galdr_MySubSystem() )
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
