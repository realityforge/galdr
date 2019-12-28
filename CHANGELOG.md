# Change Log

### [v0.03](https://github.com/realityforge/galdr/tree/v0.03) (2019-12-29)
[Full Changelog](https://github.com/realityforge/galdr/compare/v0.02...v0.03)

* Use the `org.realityforge.proton:proton` project for processor utility methods.
* Include the location of the problem when generating warnings for unnecessary public or protected access on members.
* Decouple the `processor` artifact from the `com.google.auto:auto-common` dependency and thus the `com.google.guava:guava` dependency. This significantly reduces the build time for the processor and the size of the processor artifact.

### [v0.02](https://github.com/realityforge/galdr/tree/v0.02) (2019-11-30)
[Full Changelog](https://github.com/realityforge/galdr/compare/v0.01...v0.02)

* Add missing inherit for `grim.annotations.Annotations` to `Galdr.gwt.cml` GWT module.

### [v0.01](https://github.com/realityforge/galdr/tree/v0.01) (2019-11-30)
[Full Changelog](https://github.com/realityforge/galdr/compare/50b6987ae83935f316c69b87c275549ef168e1d2...v0.01)

 ‎🎉	Initial super-alpha release ‎🎉.
