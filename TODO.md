# TODO

This document is essentially a list of shorthand notes describing work yet to be completed.
Unfortunately it is not complete enough for other people to pick work off the list and
complete as there is too much un-said.

### Tasks

* Use an entity command buffer to make structural changes. Different buffers exist at different sync points - see unity dots

* Derive functionality from [Bevy](https://bevyengine.org/news/introducing-bevy/)

* Derive functionality from [Hecs](https://github.com/gohyperr/hecs/tree/master/packages/hecs)

* Add the `EntityPlan` element above.
* Replace `ComponentStorage` with set of int flags to optimize code size in the context of GWT. We can probably
  use the existing flags on the `ComponentManager` to store this state.
* Add API to expose `Link` objects for entities in Spy infrastructure.
* Add a suite of "integration" tests that operate at the public API level.
* Add a tool that visualizes `Component` -> `Processor` matrix. Another way to view this rather than via a matrix is
  to select a `Processor` in left column and see `Component` highlighted in right column or vice versa.
* Add a tool that visualizes `Entity` -> `Component` matrix.
* Change _cascadeSourceRemoveToTarget and cascadeTargetRemoveToSource on Link to flags and populate spy events with flags.
* Consider adding a `default` `AreaOfInterest` either in component layer or in core framework that is merged
  into all `AreaOfInterest` unless `includeDefault=false`. This would allow you to do things like add
  `exclude=Disabled.class` to exclude disabled entities. It seems likely that the Component layer is the best place to
  layer this functionality by adding the `@GaldrApplication.defaultExclude` parameter and the
  `@GaldrSubSystem.includeDefault` parameter. The `default` will also include the tag component `Archetype`
  which is used to describe entities that to be used as templates for creating other entities.

* Consider creation of some higher level `EntityPlan` (or `Archetype`, `Plan`, `Blueprint`, `Prefab` `Template`)
  classes that act as a stamp that creates the structure of 1 or more `Entity` instances with specific `Component`
  instances and `Link` defined between `Entity` instances. There could be two parts of this library - the first part
  creates the structure and the second part gives the components values. The second part could be done at a higher level
  subordinate library that reads data from json/xml/whatever to provide values. The first part could also be adapted
  so that it takes a lambda and thus the components could be populated prior to events being propagated and the rest
  of the system being made aware of the entities etc. The mechanisms for storing state should be linked in to the
  same mechanisms used to serialize world state so that both are optimized together. It should also be noted that
  the templates may be loaded from data files or they could be created in-code. However they should be relatively
  efficient to represent regardless of how they are used from the application.
  - `EntityPlan` == object structure and relationships between `Entity` instances
  - `Prefab` or `Assemblage` = `EntityPlan` + `default values` (See [http://vasir.net/blog/game-development/how-to-build-entity-component-system-in-javascript](http://vasir.net/blog/game-development/how-to-build-entity-component-system-in-javascript))

* An `Archetype` is actually a description of an `Entity` and the set of components it must have. It has more inline
  with the normal definition of archetypes. Some ECS systems also maintain a list of all the different archetypes in
  the world. Rather than arranging memory layout in components, you layout memory so that all entities with the
  same archetype are grouped together. It is unclear where the performance benefit is for this other than some
  components are always accessed together and benefit from locality. (The question is why not make this a single
  component ... and answer is that maybe it feels like should be different. Maybe we could have subviews of
  components to support this use case.)

* Add additional `ComponentManager` implementations.
  - `Lazy` implementation that does not allocate the component instance until it is first accessed.
  - `CopyOnWrite` implementation that uses a global shared component instance until write occurs.  This may be
    useful for `Health` on walls which may have many wall instances but rarely need t track changes.
  - `LookupAndArray` implementation that uses a dense array for storage but stores the index in a separate
    `entityId` => `index` map.
  - A [FlatBuffers](https://google.github.io/flatbuffers/index.html) based implementation for cache friendly
    access patterns.
  - A variable-sized, stretchy buffer component. Each component instance has a capacity that can be changed over
    time. Useful when need an array of floats for a mesh etc.
  - Review other storage strategies - See https://slide-rs.github.io/specs/05_storages.html

* Consider adding `GaldrSubSystem.processBefore=[]` and `GaldrSubSystem.processAfter=[]` so can order
  subsystems in a stage without explicit ordering. But if we are doing that then we should also do the
  add `GaldrSubSystem.addToStages=[]`... which seems suboptimal. Maybe `GaldrSubSystem.mustBeBefore=[]`
  style parameters that declare requirements that are checked by annotation processor may be a better
  approach. We could also do things like `GaldrSubSystem.afterComponentUpdates=[]` to specify that processor
  expects that writes to a component be completed by the time it runs.

* Change the strategy for iteration over `Entity` instances in an `EntityCollection` so that they can be based
  on the `"primary"` component. This would allow `FlatBuffers` and `LookupAndArray` `ComponentManager`
  implementations to optimize cache access patterns.

* Another strategy for scheduling entities is based on "shared components". So some component types would be
  references to shared assets (like a mesh or a texture). Components that share the assets could be scheduled
  together. The component could also be shared between components on component state impacts all components.
  However in this scenario the component state is often immutable.

* Consider adding application-level (i.e. non-spy) events when Entities are created/disposed and when components
  are allocated/removed. These events would remain in production applications and thus able to perform application
  logic, unlike spy events that would be optimized out in production environments. Also add application events for
  when a entity attaches to a collection or is removed from a collection.
  - This would essentially be callbacks on processors. This would also allows to do incremental changes so that
    `@EntityProcessor` could only be called with added entities.

* Some useful operations that may want to support when using the toolkit.
  - `createMultipleEntities()` create N entities given an input `EntityPlan`/`Archetype`
  - `cloneMultipleEntities()` clone N entities given an input Entity instance. The components from that entity are
    copied to all the clones including all the data.
  - `cloneEntity()` create a new entity with all the component types and component data from an existing Entity.

* Some ECS systems have a mechanism for deferring work in a stage. So zero or more processors queue work that is
  picked up by a processor later in the stage which applies the work items. The later processor could also filter,
  drop, reorder or modify the work items as it sees fit, potentially spreading the work over several invocations
  of the stage. The example in one talk was impact effects being accumulated during stage and applied a the end
  which overlapping effects omitted and the effects spread over multiple frames if they exceed a stage budget.
  We could provide some sort of built in mechanisms to do this, whether it be work queues or a method that exposes
  how much time the current stage has left in it's budget.

* Should processors expect to run at a fixed frequency (i.e. simulate at 30 frames per second) and thus delta
  parameter for the processor is ignored. They only care that the next frame has occurred. Some processors will
  run when they are able time (i.e. idleTime processors like in browsers) while others care about the delta between
  the current time and next time (render/physics/etc). Should we build in any support for these sorts of systems.

* A lot of different ECS systems have different mechanisms for increasing locality of data access when scheduling
  entities.
  - We can increase locality by allocating every entity with the same set of components in one block. And then
    scheduling processing within systems so that entities are processed in order they appear in each block, one
    block after another.
  - Rather than allocating all components in a block we could instead just allocate a cluster of components
    together if they are usually accessed together.
  - We could also move to processing a single component at a time but schedule the entity according to the
    entities that contain that component.

* Replace `BitSet` with equivalent variant that is more optimized for our use-case?
  - a fixed size `BitSet` implementation that implements `hashCode()` and `equals()` and `toString()` as a
    binary bit string ala `00010101001` (if size low enough) or a ordered flag set `(1, 4, 6, 33)` if size is
    too large. This would be used to contain `ComponentIds` as in `AreaOfInterest` or `Entity` and would have
    operations optimized for our uses (i.e. `containsAll()` for use in `AreaOfInterest`).
    There would also be a `MutableFixedBitSet` and `UnmodificableFixedBitSet` so that can be propagated via
    `EntityAddCompleteEvent` although the second kind may be just a proxy that is eliminated in production code.
  - a dynamically sized `BitSet` implementation containing entity ids with a different set of optimizations and
    no `hashCode()` and `equals()` implementations etc. A particular implementation of value is `boolean clearIfSet(bit)`
    used in `Subscription.entityRemove(int entityId)`.
  - Consider hierarchical bitsets. A hierarchical bitset is essentially a multi-layer bitset, where each upper
    layer "summarizes" multiple bits of the underlying layers. That means as soon as one of the underlying bits
    is 1, the upper one also becomes 1, so that we can skip a whole range of indices if an upper bit is 0 in
    that section. In case it's 1, we go down by one layer and perform the same steps again (it currently has 4
    layers).

* Replace usages of `Objects.requireNonNull` with a local `Galdr.requireNonNull()` so that it can be replaced
  with a noop in JRE code.

* In `ApplicationAnnotationProcessor`
  - Ensure component types declared in `@ComponentManagerRef` in contained subsystems are present in application.
  - Ensure component types declared in `@EntityProcess` in contained subsystems are present in application.

* Add error handling for processors. Either allow error handling locally or bubble up to application handlers.
  The error handlers will catch errors generated in user code and pass to error handling code.

* Consider automatically determining the order of `SubSystem` instances within a `Stage` by priority and or
  declared component dependencies. i.e. If `SubSystem` instances are of the same priority then a `SubSystem`
  that reads a component should be scheduled after `SubSystem` instances that can write to the same component.

* In the current iteration the toolkit the `Component` instances are expected to be instances of a class
  but in the future they may instead be views into underlying linear arrays (As provided by toolkits such as
  [FlatBuffers](https://google.github.io/flatbuffers/index.html)). There may also be separation between a
  read-only view and a read-write view and the toolkit could use annotation processors to compile away the
  differences. In an ideal world we could also mark components as not being able to hold references to normal
  garbage collectable values. In this case we could even guarantee access allowed from multiple workers etc.

* Change `@ComponentManagerRef` infrastructure so that it is possible to determine whether the `SubSystem`
  wants read-only or read-write access to `Component` instances based on the type parameter. This ultimately
  requires difference interfaces for read-only and read-write of a single component type. (This seems to be the
  way forward. Generating a `FooView` for every `Foo` component will give us mechanisms for representing
  read-only views while still being able to optimize away at compile time the overhead)

* Once we can determine the type of access required by a `SubSystem`, the framework may be able to operate in
  parallel or concurrently if `Component` types do not overlap or the `SubSystem` instances only need to read
  a component type.

* In the future, a `SubSystem` may declare that they only want to receive a entity if a `Component` in the
  `AreaOfInterest` has changed. This involves more significant architecture around change tracking and will
  not be implemented unless there is a clear demand.

* A `SubSystem` may declare that they are interested in 0 or more event types. The `SubSystem` will react and
  to events when they occur. See the "event" design for further details.

* Is `@OnActivate` called when a processor transitions to a non-empty entity collection or on construction? Should
  we have different annotations for processor setup/shutdown (i.e. `@PostConstruct` and `@PreDestroy`) versus
  transitioning from 0 entities -> non-zero entities and vice cersae (i.e. `@OnActivate` and `@OnDeactivate`)

* If we ever decide to go multi-threaded either within a processor or between processors or between stages then
  we will need to figure out a way to batch entities to process into a buffer and send them to the separate
  thread. These command buffers will effectively get read-locks on any components that the processor expects to
  read from and write locks from any components that will be written to and it is up to the scheduler to make
  sure they can never collide when entity ids are passed to the workers. It may still be possible for the workers
  to aquire additional locks if they are following links but the code will need to guard that if it is likely to
  be a problem.

* A future, future version of this system may have a prebuild step. One input is a list of archetypes and their
  fields/attributes. Another input is a list of systems which indicate which fields are read, which are written.
  Another input is the runtime details like the number of instances of each archetype, the number of times a system
  matches an archetype etc. Other inputs may be whether archetypes are created in bulk or not etc. From this an
  optimizing compiler will analyze the model and determine what the most efficient mechanism for breaking the
  application into components is. So N fields that are mostly accessed together may be placed into a single
  component. Archetypes may include some fields in side-car storage (i.e. hash maps or another lookup storage)
  while the most frequently used data is collected inline. etc

### Games to Experiment with

* A simple BoulderDash clone [Digger](https://github.com/lutzroeder/digger)

### Other ECS Systems

We should investigate other ECS systems in other languages and with other design constraints. Galdr is designed
for web platform where there will be different `World` instances, some of which are turn based and some of which
are "realtime". Inspiration from other frameworks could help move it forward.

* [Ecsy](https://blog.mozvr.com/introducing-ecsy/) an ECS for the web.

* A [Rust ECS](https://www.youtube.com/watch?v=SofC6c9xQv4) talk. It is very interesting as it explicitly calls
  out the different `ComponentStore` implementations for different performance characteristics.

* [Artemis](https://code.google.com/archive/p/artemis-framework) was one of the original java based ECS toolkits
  that inspired a whole line of different ECS implementations in java. The code is no longer maintained and there
  is ome significant improvements that could be made to it but it still serves as a useful starting point.

* [Artemis-ODB](https://github.com/junkdog/artemis-odb) is a maintained successor to Artemis that includes a
  lot more features. In particular it includes runtime code generation to improve performance, an injection framework
  to ease development frameworks, generation of fluid entity API, `Prefab` and `Archetype` classes to help create
  entities from blueprints, loading and saving entity state to files etc. It also has a reasonably active community
  with lots of interesting support libraries and tools to [visualize entities at runtime](https://github.com/Namek/artemis-odb-entity-tracker)
  as well as [kick-starters](https://github.com/DaanVanYperen/artemis-odb-contrib/tree/master/contrib-jam) like
  needed when at a game jam. All of these tools come at a complexity cost as does the legacy of maintaining
  compatibility with prior ECS systems (i.e. Artemis and ODB).

* [Ashley](https://github.com/libgdx/ashley/wiki) is another java ECS that took ideas from other ECS implementations
  (including Artemis above). It is significantly simpler and more explicit than Artemis and Artemis-ODB and it is
  dependent upon libgdx. It has significantly worse performance.

* [Amethyst](https://github.com/Hexworks/amethyst) describes itself as "Simple multiplatform SEA (Systems,
  Entities, Attributes) library written in multiplatform Kotlin". It's most interesting concept is that it
  explicitly represents `Commands` that can be sent to either entities or systems. This is versy similar to the
  proposed eventing framework without the sophistication. It is not a strict ECS system with entities declaring
  the associated systems that process them. The entities also can directly handle commands or apss it through
  systems until a system consumes the command. The framework also distinguishes between two types of systems;
  A `Behaviour` is a system which performs actions autonomously on entities whenever they are updated while a
  `Facet` is a system that performs actions based on the`Command`s they receive. An `Actor` is a system combining
  the aspects of a `Behaviour` and a `Facet`. Each tick an Entity will process all received events either directly
  or via facets and then process all other state updates via behaviours. (FWIW it is useful to think of `Behaviour`
  as letting entity interact with the world and the `Facet` which lets whe world interact with our entity.)

* [Unity ECS](https://docs.unity3d.com/Packages/com.unity.entities@0.1/manual/index.html) is integrated into their
  game engine and is primarily focused on performance and efficiency. So rather than co-locating all components
  together, the toolkit co-locates entities that have the same components together. Hence an archetype is not
  just a design or programmer affordance but is integral to how the engine optimizes code as all entities with the
  same archetype are colocated. Archetypes are allocated to 16kb Chunks. If an entity adds a component then it's
  archetype changes and it is migrated to a different chunk.
