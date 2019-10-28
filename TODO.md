# TODO

This document is essentially a list of shorthand notes describing work yet to completed.
Unfortunately it is not complete enough for other people to pick work off the list and
complete as there is too much un-said.

### Design

* `World` (a.k.a. a `Context`?)
  * Contain 0 or more `Entity` instances. These are dynamically added and removed over time.
  * Contain 1 or more `ProcessorStage` instances. These are statically defined when the `World` is constructed.
  * Contain 1 or more `ComponentType` instances. These are statically defined when the `World` is constructed.

* `Entity`
  * A container into which components are added.
  * Has a unique identifier.
  * Contains 0 or more `Component` instances.

* `ComponentType`
  * Represents a type of a `Component` available in the `World`

* `Component`
  * A passive data container that may be attached to an `Entity` instance.
  * A typical `Component` is a fine-grain data element where all the fields are used to achieve a single function.
    e.g. A `Component` may be `Position`, `Velocity`, `Acceleration`, `CameraTransform`, `Health`, `Sprite` etc.
  * In the early iterations of this toolkit the `Component` instances are expected to be instances of a class
    but in the future they may instead be views into underlying linear arrays (As provided by toolkits such as
    [FlatBuffers](https://google.github.io/flatbuffers/index.html)). There may also be separation between a
    read-only view and a read-write view and the toolkit could use annotation processors to compile away the
    differences.

* `ProcessorStage`
  * Contains 1 or more `Processor` instances that are statically defined when the stage is constructed.
  * Orders the `Processor` instances by user-controlled priority. If processors have the same priority then
    order the processors based on data dependencies between `Processor` instances. i.e. If a `Processor`
    reads a `Component` than order it after an `Processor` with the same priority that writes a component.
  * Applications are expected to explicitly invoke the `ProcessorStage` when they are expected to run.

* `AreaOfInterest`
  * Declares a set of rules that match components. These rules include which components MUST be present,
    which components MUST NOT be present and a set of component where at least one component MUST be present.

* `Subscription`
  * A subscription is a set of entities maintained by the toolkit that match a specific `AreaOfInterest`.

* `AccessControl`
  * Rules that declare how a `Processor` accesses `Component` instances. This includes whether the `Processor`
    accesses the component for read or write access and potentially whether the component creates or disposes
    components or entities.

* `Processor` (a.k.a. Systems in other ECS frameworks)
  * A processor may receive 0 or more event types and applies changes to entities. See the "event" design for
    further details.
  * A processor reads and writes to `Entity` instances and may reflect state from entities as side-effects.
    i.e. The `Renderer` processor probably generates many OpenGL commands to put entities onto the screen.
  * A processor may declare an `AreaOfInterest` that would result in it being passed `Entity` instances that
    match the `AreaOfInterest`.
  * A processor may declare that they only want to receive a entity if an `Component` in the `AreaOfInterest`
    has changed. This involves more significant architecture around change tracking and may come at a later
    time.
  * May operate in parallel or concurrently if `Component` types do not overlap or the `Processor` instances
    only need to read a component type.
  * In some cases it may be useful to explicitly declare dependency relationships between `Processor` instances
    as they may generate side-effects other that manipulating component state as a side-effect and thus you want
    to order processes explicitly.

The toolkit will use annotation processors to build an efficient representation of component accessors and schedulers
for systems/processors ahead of time.

* Consider creation of some higher level `Archetype`, `Plan`, `Blueprint`, `Prefab` or `Template` classes that act
  as a stamp that creates the structure of 1 or more `Entity` instances with specific `Component` instances and
  `Link` defined between `Entity` instances. There could be two parts of this library - the first part creates
  the structure and the second part gives the components values. The second part could be done at a higher level
  subordinate library that reads data from json/xml/whatever to provide values. The first part could also be adapted
  so that it takes a lambda and thus the components could be populated prior to events being propagated and the rest
  of the system being made aware of the entities etc. The mechanisms for storing state should be linked in to the
  same mechanisms used to serialize world state so that both are optimized together. It should also be noted that
  the templates may be loaded from data files or they could be created in-code. However they should be relatively
  efficient to represent regardless of how they are used from the application.

### Tasks

* Merge `ComponentAPI` into `ComponentManager`
* Change `Subscription*Event` to be parameterized by info objects where possible.
* Add `CollectionAttachEvent` spy event.
* Move `galdr.Processor` to `galdr.internal.Processor` or similar. To do this we are probably going to have
  to have a way to identify processors by name or id so that we can refer to them in our error handler.
* `AreaOfInterest` should generate an error if there is no components contained.
* Replace `ComponentStorage` with set of int flags to optimize code size in the context of GWT. We can probably
  use the existing flags on the `ComponentManager` to store this state.
* Review other storage strategies - See https://slide-rs.github.io/specs/05_storages.html
* Change _cascadeSourceRemoveToTarget and cascadeTargetRemoveToSource on Link to flags and populate spy events with flags.
* Add API to expose `Link` objects for entities in Spy infrastructure.
* Add a suite of "integration" tests that operate at the public API level.
* Add a tool that visualizes `Component` -> `Processor` matrix. Another way to view this rather than via a matrix is
  to select a `Processor` in left column and see `Component` highlighted in right column or vice versa.
* Add a tool that visualizes `Entity` -> `Component` matrix.
* Change component into an annotation `@Component`
* Add `CollectionsUtil.unmodifiableMap(Map<K,V> input)` that either returns input or wraps it in a
  `Collections.unmodifiableMap()` if `Galdr.enforceUnmodifiableCOllections()` returns `true`.

* Add additional `ComponentManager` implementations.
  - `Lazy` implementation that does not allocate the component instance until it is first accessed.
  - `CopyOnWrite` implementation that uses a global shared component instance until write occurs.  This may be
    useful for `Health` on walls which may have many wall instances but rarely need t track changes.
  - `LookupAndArray` implementation that uses a dense array for storage but stores the index in a separate
    `entityId` => `index` map.
  - A [FlatBuffers](https://google.github.io/flatbuffers/index.html) based implementation for cache friendly
    access patterns.

* Change the strategy for iteration over `Entity` instances in an `EntityCollection` so that they can be based
  on the `"primary"` component. This would allow `FlatBuffers` and `LookupAndArray` `ComponentManager`
  implementations to optimize cache access patterns.

* Consider adding application-level (i.e. non-spy) events when Entities are created/disposed and when components
  are allocated/removed. These events would remain in production applications and thus able to perform application
  logic, unlike spy events that would be optimized out in production environments.

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

* Replace `BitSet` with equivalent variant that is more optimized for our use-case?
  - It is only exposed in the Spy API `galdr.spy.*Event` and we should jsut replace with `ComponentSetId`
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

* Make sure that all entities (mobiles, items, spells, and their effects) can be represented in Galdr.

* Change at the API and/or GWT configuration level such that `check_api_invariants=true` => `check_invariants=true`

### Other ECS Systems

We should investigate other ECS systems in other languages and with other design constraints. Galdr is designed
for web platform where there will be different `World` instances, some of which are turn based and some of which
are "realtime". Inspiration from other frameworks could help move it forward.

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
