package de.verpalnt.propertly.listener;

/**
 * @author PaL
 *         Date: 13.11.12
 *         Time: 20:17
 */
public interface IPropertyEventListener<S, T>
{

  void propertyChange(IPropertyEvent<S, T> pEvent);

}
