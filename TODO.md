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
  * Declares rules that indicate the `Component` types that a `Processor` is interested in processing.
  * Each rule declares that an `Entity` MUST or MUST NOT have a `Component` of a particular type.
  * A rule can indicate that it may access a `Component` type if it is available.
  * Each rule will declare how a `Processor` interacts with `Component` type. A `Processor` may only
    require read-only view or may be supplied a read-write value. Read-write also indicates creating and
    disposing components.

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

The toolkit will use annotation processors to build an efficient representation of component accessors and schedulers
for systems/processors ahead of time.

### Tasks

* Add support for Components that have no state. They are effectively flags/markers. Potentially the
  ComponentManager implementation just keeps bitset for free/notfree and generates an error if component
  is accessed (or returns a singleton).
* We should consider changing Entity so that they have a unique id that maps to a index in the entity
  array. The API would change to pass around a `EntityRef` or `Designator` or similar that contains the
  unique id as well as entity index. This would mean that after an entity is disposed it would never come
  back to life at the expense of some minimal overhead. However it is unclear if this is really a problem
  that we are likely to run into in an ECS system where systems process entities. It is is only really
  problematic when a component wants to maintain a reference to another entity. Of course it also means
  a lookup every time you want to interact with entity to retrieve ref or the creation of a new ref, neither
  of which is efficient. Perhaps a mechanism for managing links to Entity is better suited for this purpose.
* `ComponentManager` should generate a event/message when a component is added/removed outside of entity creation
  and there is some party that has registered interest in listening to that component.
* Add a suite of "integration" tests that operate at the public API level.
* Add `MapComponentManager` that stores components in a HashMap
* Add `LookupAndArrayComponentManager` that stores entityId -> index in lookup map which then is used to address
  component in array. This is halfway in perf tradeoffs between the `FastArray` and `Map` implementations.
* Optionally validate EntityIds event in `FastArrayComponentManager`
* Add a tool that visualizes `Component` -> `Processor` matrix. Another way to view this rather than via a matrix is
  to select a `Processor` in left column and see `Component` highlighted in right column or vice versa.
* Add a tool that visualizes `Entity` -> `Component` matrix.
* Change component into an annotation `@Component`
* Change `Entity` to be hierarchical. i.e. An `Entity` can have child `Entity` instances.
* Add `CollectionsUtil.unmodifiableMap(Map<K,V> input)` that either returns input or wraps it in a
  `Collections.unmodifiableMap()` if `Galdr.enforceUnmodifiableCOllections()` returns `true`.
* Add different implementations of `ComponentStore` that optimize for different distributions of entities. The
  current implementation is a java array. We could create a store where there is a dense lookup array that looks
  into a very sparse component array. Useful when there is a low proportion of components. We could create a
  implementation based on [FlatBuffers](https://google.github.io/flatbuffers/index.html) for cache friendly
  access. We could use a `Map` based implementation that is used when there is relative low density component
  distribution that does not need fast access times.

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
