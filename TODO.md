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
    require read-only view or may be supplied a read-write value.

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

* Change component into an annotation `@Component`
* Change `Entity` to be hierarchical. i.e. An `Entity` can have child `Entity` instances.
* Add `CollectionsUtil.unmodifiableMap(Map<K,V> input)` that either returns input or wraps it in a
  `Collections.unmodifiableMap()` if `Galdr.enforceUnmodifiableCOllections()` returns `true`.