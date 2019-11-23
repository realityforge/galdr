package galdr.processor;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.lang.model.type.TypeMirror;

final class ComponentDescriptor
{
  @Nonnull
  private final TypeMirror _component;
  @Nonnull
  private final StorageType _storageType;

  ComponentDescriptor( @Nonnull final TypeMirror component, @Nonnull final StorageType storageType )
  {
    _component = Objects.requireNonNull( component );
    _storageType = Objects.requireNonNull( storageType );
  }

  @Nonnull
  TypeMirror getComponent()
  {
    return _component;
  }

  @Nonnull
  StorageType getStorageType()
  {
    return _storageType;
  }
}
