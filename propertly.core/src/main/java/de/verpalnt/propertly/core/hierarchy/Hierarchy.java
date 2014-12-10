package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.api.*;
import de.verpalnt.propertly.core.common.IFunction;

import java.util.*;

/**
 * @author PaL
 *         Date: 29.01.13
 *         Time: 23:13
 */
public class Hierarchy<T extends IPropertyPitProvider>
{

  private final INode node;
  private final List<IPropertyEventListener> listeners;


  public Hierarchy(final String pName, T pPPP)
  {
    this(new IFunction<Hierarchy, INode>()
    {
      @Override
      public INode run(Hierarchy pHierarchy)
      {
        return new Node(pHierarchy, null, PropertyDescription.create(
            IPropertyPitProvider.class, IPropertyPitProvider.class, pName));
      }
    }, pPPP);

  }

  protected Hierarchy(IFunction<Hierarchy, INode> pNodeSupplier, T pPPP)
  {
    node = pNodeSupplier.run(this);
    listeners = new ArrayList<IPropertyEventListener>();
    node.setValue(pPPP);
  }

  public IProperty<IPropertyPitProvider, T> getProperty()
  {
    //noinspection unchecked
    return getNode().getProperty();
  }

  public T getValue()
  {
    return getProperty().getValue();
  }

  public void addPropertyEventListener(IPropertyEventListener pListener)
  {
    listeners.add(pListener);
  }

  public void removePropertyEventListener(IPropertyEventListener pListener)
  {
    listeners.remove(pListener);
  }

  protected INode getNode()
  {
    return node;
  }

  protected void fireNodeChanged(IProperty pProperty, Object pOldValue, Object pNewValue)
  {
    for (IPropertyEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyChange(pProperty, pOldValue, pNewValue);
  }

  protected void firePropertyAdded(IPropertyPitProvider pPropertyPitProvider, IPropertyDescription pDescription)
  {
    for (IPropertyEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyAdded(pPropertyPitProvider, pDescription);
  }

  protected void firePropertyWillBeRemoved(IPropertyPitProvider pPropertyPitProvider, IPropertyDescription pDescription)
  {
    for (IPropertyEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyWillBeRemoved(pPropertyPitProvider, pDescription);
  }

  protected void firePropertyRemoved(IPropertyPitProvider pPropertyPitProvider, IPropertyDescription pDescription)
  {
    for (IPropertyEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyRemoved(pPropertyPitProvider, pDescription);
  }

}
