# Change Log

### Unreleased

* Update the `org.realityforge.proton` artifacts to version `0.64`.
* Update the `org.realityforge.grim` artifacts to version `0.09`.

### [v0.05](https://github.com/realityforge/galdr/tree/v0.05) (2021-11-10) · [Full Changelog](https://github.com/spritz/spritz/compare/v0.04...v0.05)

Changes in this release:

* Upgrade the `org.realityforge.braincheck` artifacts to version `1.31.0`.
* Upgrade the `org.realityforge.proton` artifacts to version `0.52`. This fixes a crash that occurs with concurrent, incremental builds within the IntelliJ IDE. (This is the crash reported with message `javax.annotation.processing.FilerException: Attempt to recreate a file for type ...`).

### [v0.04](https://github.com/realityforge/galdr/tree/v0.04) (2021-03-30) · [Full Changelog](https://github.com/realityforge/galdr/compare/v0.03...v0.04)

* Upgrade the `au.com.stocksoftware.idea.codestyle` artifact to version `1.17`.
* Upgrade the `org.realityforge.braincheck` artifact to version `1.29.0`.
* Upgrade the `org.realityforge.grim` artifacts to version `0.05`.
* Upgrade the `com.google.truth` artifact to version `0.45`.
* Upgrade the `com.google.testing.compile` artifact to version `0.18-rf`.
* Upgrade the `com.squareup` artifact to version `1.13.0`.
* Upgrade the `org.realityforge.proton` artifacts to version `0.51`.
* Add the `galdr.debug` annotation option to help debug annotation processing.
* Update release process.

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
