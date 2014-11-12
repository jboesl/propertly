package de.verpalnt.propertly.core.api;

/**
 * @author PaL
 *         Date: 14.10.12
 *         Time: 14:52
 */
public interface IPropertyPitProvider<S extends IPropertyPitProvider, T>
{

  IPropertyPit<S, T> getPit();

}
