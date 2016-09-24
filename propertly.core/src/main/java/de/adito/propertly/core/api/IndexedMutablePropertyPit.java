package de.adito.propertly.core.api;

import de.adito.propertly.core.common.PropertlyUtility;
import de.adito.propertly.core.spi.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * @author PaL
 *         Date: 13.07.14
 *         Time. 22:42
 */
class IndexedMutablePropertyPit<P extends IPropertyPitProvider, S extends IIndexedMutablePropertyPitProvider<P, S, T>, T>
    extends MutablePropertyPit<P, S, T>
    implements IIndexedMutablePropertyPit<P, S, T>
{


  IndexedMutablePropertyPit(S pSource, Class<T> pAllowedChildType)
  {
    super(pSource, pAllowedChildType);
  }

  public static <P extends IPropertyPitProvider, S extends IIndexedMutablePropertyPitProvider<P, S, T>, T>
  IndexedMutablePropertyPit<P, S, T> create(S pCreateFor, Class<T> pAllowedChildType)
  {
    return new IndexedMutablePropertyPit<>(pCreateFor, pAllowedChildType);
  }

  @Nonnull
  @Override
  public IIndexedMutablePropertyPit<P, S, T> getPit()
  {
    return this;
  }

  @Override
  public int getSize()
  {
    return getNode().getChildrenStream().map(Stream::count).orElse(0L).intValue();
  }

  @Nonnull
  @Override
  public IProperty<S, T> getProperty(int pIndex)
  {
    //noinspection unchecked
    return getNode().getNode(pIndex).getProperty();
  }

  @Nonnull
  @Override
  public IProperty<S, T> addProperty(int pIndex, @Nonnull IPropertyDescription<S, T> pPropertyDescription, @Nullable Object... pAttributes)
  {
    getNode().addProperty(pIndex, pPropertyDescription, PropertlyUtility.toNonnullSet(pAttributes));
    return getProperty(pIndex);
  }

  @Override
  public void removeProperty(int pIndex, @Nullable Object... pAttributes)
  {
    getNode().getNode(pIndex).remove(PropertlyUtility.toNonnullSet(pAttributes));
  }

  @Override
  public int indexOf(@Nonnull IPropertyDescription<?, ?> pPropertyDescription)
  {
    return getNode().indexOf(pPropertyDescription.getName());
  }

  @Override
  public void reorder(@Nonnull Comparator<IProperty<S, T>> pComparator, @Nullable Object... pAttributes)
  {
    getNode().reorder(pComparator, PropertlyUtility.toNonnullSet(pAttributes));
  }
}
