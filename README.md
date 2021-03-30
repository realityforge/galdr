# galdr: An ECS implementation

[![Build Status](https://api.travis-ci.com/realityforge/galdr.svg?branch=master)](http://travis-ci.com/realityforge/galdr)
[<img src="https://img.shields.io/maven-central/v/org.realityforge.galdr/galdr.svg?label=latest%20release"/>](https://search.maven.org/search?q=g:org.realityforge.galdr%20a:galdr)
[![codecov](https://codecov.io/gh/realityforge/galdr/branch/master/graph/badge.svg)](https://codecov.io/gh/realityforge/galdr)
![GWT3/J2CL compatible](https://img.shields.io/badge/GWT3/J2CL-compatible-brightgreen.svg)

## What is galdr?

`galdr` is an ancient form of sung incantation, intended to have magical effects. Or in more practical
terms, Galdr is an implementation of the entity–component–system (ECS) architectural pattern. Elements in a
world are entities and consist of a unique id and a set of components that are processed by systems. Behaviour of
an entity is changed by adding, updating and removing components. This architecture is typically used in games and
is often used to support data-oriented design techniques.

A `Component` instance is a passive data container that can be attached to an `Entity` instance. A typical
`Component` is a fine-grain data element where all the fields are used to achieve a single logical function.
i.e. A `Component` may be `Position`, `Velocity`, `Body`, `CameraTransform`, `Health`, `Sprite` etc.

A `System` is responsible for updating the `Component` instances attached to `Entity` instances. A system typically
selects the `Entity` instances to process based on the set of `Component` instances. i.e. A `RenderSystem` may
read the `Position` and `Spite` components to render the entity on the screen, a `PhysicsSystem` may read the `Body`,
`Position` and `Velocity` components and update the `Position` component etc. It should be noted that in Galdr, the
term `SubSystem` is used instead of `System` to avoid a name collision with the `java.lang.System` class.

### Getting Started

The tool is released to Maven Central and can be downloaded using normal dependency download mechanisms.
The Maven dependency is:

```xml
<dependency>
  <groupId>org.realityforge.galdr</groupId>
  <artifactId>galdr</artifactId>
  <version>0.04</version>
</dependency>
```

# Contributing

The project was released as open source so others could benefit from the project. We are thankful for any
contributions from the community. A [Code of Conduct](CODE_OF_CONDUCT.md) has been put in place and
a [Contributing](CONTRIBUTING.md) document is under development.

# License

The project is licensed under [Apache License, Version 2.0](LICENSE).
