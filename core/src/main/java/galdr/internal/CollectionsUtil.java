package galdr.internal;

import galdr.Galdr;
import grim.annotations.OmitType;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

/**
 * Utility methods used to create unmodifiable collections if enabled.
 */
@OmitType( unless = "galdr.enforce_unmodifiable_collections" )
public final class CollectionsUtil
{
  private CollectionsUtil()
  {
  }

  /**
   * Wrap specified parameter with unmodifiable map if and only if {@link Galdr#enforceUnmodifiableCollections()}
   * returns true, otherwise return the supplied map.
   *
   * @param <K> the type of key elements in map.
   * @param <V> the type of value elements in map.
   * @param map the input map.
   * @return the output map.
   */
  @Nonnull
  public static <K, V> Map<K, V> wrap( @Nonnull final Map<K, V> map )
  {
    return Galdr.enforceUnmodifiableCollections() ? Collections.unmodifiableMap( map ) : map;
  }

  /**
   * Wrap specified list with unmodifiable list if and only if {@link Galdr#enforceUnmodifiableCollections()}
   * returns true, otherwise return the supplied list.
   *
   * @param <T>  the type of elements in collection.
   * @param list the input collection.
   * @return the output collection.
   */
  @Nonnull
  public static <T> List<T> wrap( @Nonnull final List<T> list )
  {
    return Galdr.enforceUnmodifiableCollections() ? Collections.unmodifiableList( list ) : list;
  }
}
