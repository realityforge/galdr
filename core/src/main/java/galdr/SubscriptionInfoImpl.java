package galdr;

import galdr.spy.CollectionInfo;
import galdr.spy.SubscriptionInfo;
import java.util.Objects;
import javax.annotation.Nonnull;

final class SubscriptionInfoImpl
  implements SubscriptionInfo
{
  @Nonnull
  private final Subscription _subscription;

  SubscriptionInfoImpl( @Nonnull final Subscription subscription )
  {
    _subscription = Objects.requireNonNull( subscription );
  }

  @Override
  public int getId()
  {
    return _subscription.getId();
  }

  @Nonnull
  @Override
  public String getName()
  {
    return _subscription.getName();
  }

  @Override
  public boolean isDisposed()
  {
    return _subscription.isDisposed();
  }

  @Nonnull
  @Override
  public CollectionInfo getCollection()
  {
    return _subscription.getCollection().asInfo();
  }
}
