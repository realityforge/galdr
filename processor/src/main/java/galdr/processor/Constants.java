package galdr.processor;

final class Constants
{
  static final String COMPONENT_CLASSNAME = "galdr.annotations.Component";
  static final String COMPONENT_MANAGER_REF_CLASSNAME = "galdr.annotations.ComponentManagerRef";
  static final String ENTITY_PROCESSOR_CLASSNAME = "galdr.annotations.EntityProcessor";
  static final String APPLICATION_CLASSNAME = "galdr.annotations.GaldrApplication";
  static final String ON_ACTIVATE_CLASSNAME = "galdr.annotations.OnActivate";
  static final String ON_DEACTIVATE_CLASSNAME = "galdr.annotations.OnDeactivate";
  static final String PROCESSOR_CLASSNAME = "galdr.annotations.Processor";
  static final String STAGE_CLASSNAME = "galdr.annotations.Stage";
  static final String SUB_SYSTEM_CLASSNAME = "galdr.annotations.SubSystem";
  static final String NAME_REF_CLASSNAME = "galdr.annotations.NameRef";
  static final String WORLD_REF_CLASSNAME = "galdr.annotations.WorldRef";
  static final String SUPPRESS_GALDR_WARNINGS_ANNOTATION_CLASSNAME = "galdr.annotations.SuppressGaldrWarnings";
  static final String NONNULL_CLASSNAME = "javax.annotation.Nonnull";
  static final String NULLABLE_CLASSNAME = "javax.annotation.Nullable";
  static final String DEPRECATED_CLASSNAME = "java.lang.Deprecated";
  static final String GENERATED_CLASSNAME = "javax.annotation.Generated";
  static final String JAVA9_GENERATED_CLASSNAME = "javax.annotation.processing.Generated";
  static final String COMPONENT_MANAGER_CLASSNAME = "galdr.ComponentManager";
  static final String WORLD_CLASSNAME = "galdr.World";

  static final String SENTINEL_NAME = "<default>";
  static final String WARNING_PUBLIC_REF_METHOD = "Galdr:PublicRefMethod";
  static final String WARNING_PROTECTED_REF_METHOD = "Galdr:ProtectedRefMethod";

  private Constants()
  {
  }
}
