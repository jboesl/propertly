package de.verpalnt.propertly.test.common;

import de.verpalnt.propertly.core.api.IPropertyDescription;
import de.verpalnt.propertly.core.api.IPropertyPitProvider;
import de.verpalnt.propertly.core.common.PD;

/**
 * @author PaL
 *         Date: 14.11.12
 *         Time: 00:41
 */
public interface ITest<T extends ITest> extends IPropertyPitProvider<T, Object>
{

  IPropertyDescription<ITest, PropertyTestChildren> CHILD = PD.create(ITest.class);

}
