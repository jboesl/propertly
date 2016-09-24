package de.adito.propertly.core.api;

import de.adito.propertly.core.spi.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author PaL
 *         Date: 03.10.11
 *         Time: 22:02
 */
class PropertyPit<P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, T>, T>
    implements IPropertyPit<P, S, T>
{

  private S source;
  private INode node;


  PropertyPit(S pSource)
  {
    source = pSource;
  }

  public static <P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, T>, T> PropertyPit<P, S, T> create(S pCreateFor)
  {
    return new PropertyPit<>(pCreateFor);
  }

  @Override
  @Nonnull
  public S getSource()
  {
    return source;
  }

  @Override
  public boolean isValid()
  {
    return node != null && node.isValid();
  }

  @Nonnull
  @Override
  public IHierarchy<?> getHierarchy()
  {
    return getNode().getHierarchy();
  }

  @Override
  @Nullable
  public final P getParent()
  {
    INode parent = getNode().getParent();
    //noinspection unchecked
    return parent == null ? null : (P) parent.getProperty().getValue();
  }

  @Nonnull
  @Override
  public IProperty<P, S> getOwnProperty()
  {
    //noinspection unchecked
    return getNode().getProperty();
  }

  @Nullable
  @Override
  public IProperty<S, T> findProperty(@Nonnull String pName)
  {
    INode childNode = getNode().findNode(pName);
    //noinspection unchecked
    return childNode == null ? null : childNode.getProperty();
  }

  @Nullable
  @Override
  public <R> IProperty<S, R> findProperty(@Nonnull IPropertyDescription<?, R> pPropertyDescription)
  {
    INode childNode = getNode().findNode(pPropertyDescription.getName());
    if (childNode == null)
      return null;
    boolean fittingTypeAndSourceType = pPropertyDescription.getType().isAssignableFrom(childNode.getProperty().getType()) &&
        pPropertyDescription.getSourceType().isAssignableFrom(childNode.getProperty().getDescription().getSourceType());
    return fittingTypeAndSourceType ? childNode.getProperty() : null;
  }

  @Nonnull
  @Override
  public <E extends T> IProperty<S, E> getProperty(@Nonnull IPropertyDescription<? super S, E> pPropertyDescription)
  {
    IProperty<?, E> property = findProperty(pPropertyDescription);
    if (property == null)
      throw new RuntimeException("Property for " + pPropertyDescription + " doesn't exist at " + this + ".");
    //noinspection unchecked
    return (IProperty<S, E>) property;
  }

  @Override
  @Nullable
  public final <E extends T> E getValue(@Nonnull IPropertyDescription<? super S, E> pPropertyDescription)
  {
    return getProperty(pPropertyDescription).getValue();
  }

  @Override
  @Nullable
  public final <E extends T> E setValue(@Nonnull IPropertyDescription<? super S, E> pPropertyDescription, @Nullable E pValue)
  {
    return getProperty(pPropertyDescription).setValue(pValue);
  }

  @Nonnull
  @Override
  public final Set<IPropertyDescription<S, T>> getPropertyDescriptions()
  {
    //noinspection unchecked
    return (Set)getNode().getChildrenStream()
        .map(stream -> stream.map(node -> node.getProperty().getDescription())
            .collect(Collectors.toCollection(() -> (Set) new LinkedHashSet<>())))
        .orElseGet(Collections::emptySet);
  }

  @Override
  @Nonnull
  public List<IProperty<S, T>> getProperties()
  {
    //noinspection unchecked
    return (List)getNode().getChildrenStream()
        .map(stream -> stream.map(INode::getProperty)
            .collect(Collectors.toList()))
        .orElseGet(Collections::emptyList);
  }

  @Nonnull
  @Override
  public List<T> getValues()
  {
    //noinspection unchecked
    return (List)getNode().getChildrenStream()
        .map(stream -> stream.map(INode::getValue)
            .collect(Collectors.toList()))
        .orElseGet(Collections::emptyList);
  }

  @Override
  public Class<T> getChildType()
  {
    return (Class<T>) Object.class;
  }

  @Override
  public Iterator<IProperty<S, T>> iterator()
  {
    return getProperties().iterator();
  }

  @Override
  public void addWeakListener(@Nonnull IPropertyPitEventListener pListener)
  {
    getNode().addWeakListener(pListener);
  }

  @Override
  public void addStrongListener(@Nonnull IPropertyPitEventListener pListener)
  {
    getNode().addStrongListener(pListener);
  }

  @Override
  public void removeListener(@Nonnull IPropertyPitEventListener pListener)
  {
    getNode().removeListener(pListener);
  }

  @Nonnull
  public IPropertyPit<P, S, T> getPit()
  {
    return this;
  }

  void setNode(INode pNode)
  {
    node = pNode;
  }

  @Nonnull
  INode getNode()
  {
    if (isValid())
      return node;
    throw new RuntimeException("'" + this + "' for " + source + " has not been initialized, yet.");
  }

}
