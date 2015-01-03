package de.adito.propertly.test.core;

import de.adito.propertly.core.api.IProperty;
import de.adito.propertly.core.api.IPropertyDescription;
import de.adito.propertly.core.api.IPropertyEventListener;
import de.adito.propertly.core.api.IPropertyPitProvider;
import de.adito.propertly.core.common.PropertyEventAdapter;
import de.adito.propertly.core.hierarchy.Hierarchy;
import de.adito.propertly.test.core.impl.PropertyTestChildren;
import de.adito.propertly.test.core.impl.TProperty;
import de.adito.propertly.test.core.impl.VerifyingHierarchy;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

/**
 * @author PaL
 *         Date: 20.08.12
 *         Time: 00:55
 */
public class PropertyTest
{

  @Test
  public void simpleTest()
  {
    final StringBuilder resultStringBuild = new StringBuilder();

    Hierarchy<TProperty> hierarchy = new VerifyingHierarchy<TProperty>(new Hierarchy<TProperty>("root", new TProperty()));
    hierarchy.addPropertyEventListener(new IPropertyEventListener()
    {
      @Override
      public void propertyChange(IProperty pProperty, Object pOldValue, Object pNewValue)
      {
        _append(resultStringBuild, "hierarchy propertyChange", pOldValue, pNewValue, pProperty.getName(), pProperty);
      }

      @Override
      public void propertyAdded(IPropertyPitProvider pSource, IPropertyDescription pPropertyDescription)
      {
        _append(resultStringBuild, "hierarchy propertyAdded", pSource, pPropertyDescription);
      }

      @Override
      public void propertyWillBeRemoved(IPropertyPitProvider pSource, IPropertyDescription pPropertyDescription)
      {
        _append(resultStringBuild, "hierarchy propertyWillBeRemoved", pSource, pPropertyDescription);
      }

      @Override
      public void propertyRemoved(IPropertyPitProvider pSource, IPropertyDescription pPropertyDescription)
      {
        _append(resultStringBuild, "hierarchy propertyRemoved", pSource, pPropertyDescription);
      }
    });
    TProperty tProperty = hierarchy.getValue();
    //GetterSetterGen.run(tProperty);
    tProperty.getPit().addPropertyEventListener(new PropertyEventAdapter()
    {
      @Override
      public void propertyChange(IProperty pProperty, Object pOldValue, Object pNewValue)
      {
        _append(resultStringBuild, "tProperty propertyChange", pProperty);
      }
    });
    PropertyTestChildren children = tProperty.setCHILD(new PropertyTestChildren());
    children.addPropertyEventListener(new PropertyEventAdapter()
    {
      @Override
      public void propertyAdded(IPropertyPitProvider pSource, IPropertyDescription pPropertyDescription)
      {
        _append(resultStringBuild, "tProperty propertyAdded", pPropertyDescription);
      }
    });

    tProperty.setX(123);
    tProperty.setFF(new Dimension(123, 456));

    children.addProperty(Color.class, "color1", null).setValue(Color.BLACK);
    children.addProperty(Color.class, "color2", null).setValue(Color.RED);

    _append(resultStringBuild, "child parent", tProperty.getCHILD().getParent());
    for (IProperty property : tProperty.getCHILD())
      _append(resultStringBuild, "tProperty child property", property);

    _append(resultStringBuild, "tProperty parent", tProperty.getPit().getParent());
    for (IProperty property : tProperty.getPit())
      _append(resultStringBuild, "tProperty property", property);


    Exception ex = null;
    try
    {
      tProperty.setX(-1);
    } catch (Exception e)
    {
      ex = e;
    }
    Assert.assertNotNull(ex);


    String expected = "hierarchy propertyChange: null, MutablePropertyPit, root, property(root, IPropertyPitProvider, PropertyPit)\n" +
        "hierarchy propertyChange: null, MutablePropertyPit, CHILD, property(CHILD, PropertyTestChildren, MutablePropertyPit)\n" +
        "tProperty propertyChange: property(CHILD, PropertyTestChildren, MutablePropertyPit)\n" +
        "hierarchy propertyChange: null, 123, root, property(root, IPropertyPitProvider, PropertyPit)\n" +
        "hierarchy propertyChange: null, 123, X, property(X, Integer, 123)\n" +
        "tProperty propertyChange: property(X, Integer, 123)\n" +
        "hierarchy propertyChange: null, java.awt.Dimension[width=123,height=456], root, property(root, IPropertyPitProvider, PropertyPit)\n" +
        "hierarchy propertyChange: null, java.awt.Dimension[width=123,height=456], FF, property(FF, Dimension, java.awt.Dimension[width=123,height=456])\n" +
        "tProperty propertyChange: property(FF, Dimension, java.awt.Dimension[width=123,height=456])\n" +
        "hierarchy propertyAdded: MutablePropertyPit, description(CHILD, class de.adito.propertly.test.core.impl.PropertyTestChildren)\n" +
        "tProperty propertyAdded: description(color1, class java.awt.Color)\n" +
        "hierarchy propertyChange: null, java.awt.Color[r=0,g=0,b=0], CHILD, property(CHILD, PropertyTestChildren, MutablePropertyPit)\n" +
        "tProperty propertyChange: property(CHILD, PropertyTestChildren, MutablePropertyPit)\n" +
        "hierarchy propertyChange: null, java.awt.Color[r=0,g=0,b=0], color1, property(color1, Color, java.awt.Color[r=0,g=0,b=0])\n" +
        "hierarchy propertyAdded: MutablePropertyPit, description(CHILD, class de.adito.propertly.test.core.impl.PropertyTestChildren)\n" +
        "tProperty propertyAdded: description(color2, class java.awt.Color)\n" +
        "hierarchy propertyChange: null, java.awt.Color[r=255,g=0,b=0], CHILD, property(CHILD, PropertyTestChildren, MutablePropertyPit)\n" +
        "tProperty propertyChange: property(CHILD, PropertyTestChildren, MutablePropertyPit)\n" +
        "hierarchy propertyChange: null, java.awt.Color[r=255,g=0,b=0], color2, property(color2, Color, java.awt.Color[r=255,g=0,b=0])\n" +
        "child parent: PropertyPit\n" +
        "tProperty child property: property(color1, Color, java.awt.Color[r=0,g=0,b=0])\n" +
        "tProperty child property: property(color2, Color, java.awt.Color[r=255,g=0,b=0])\n" +
        "tProperty parent: null\n" +
        "tProperty property: property(X, Integer, 123)\n" +
        "tProperty property: property(Y, Integer, null)\n" +
        "tProperty property: property(FF, Dimension, java.awt.Dimension[width=123,height=456])\n" +
        "tProperty property: property(MAP, Map, null)\n" +
        "tProperty property: property(CHILD, PropertyTestChildren, MutablePropertyPit)\n" +
        "tProperty property: property(WIDTH, Integer, null)\n" +
        "tProperty property: property(HEIGHT, Integer, null)";

    Assert.assertEquals(expected,
        resultStringBuild.toString());
  }

  private static void _append(StringBuilder pStrBuilder, String pEvent, Object... pAdd)
  {
    if (pStrBuilder.length() != 0)
      pStrBuilder.append("\n");
    pStrBuilder.append(pEvent).append(": ").append(_toString(pAdd));
  }

  private static String _toString(Object... pObj)
  {
    String r = "";
    for (Object o : pObj)
    {
      if (!r.isEmpty())
        r += ", ";

      if (o instanceof IPropertyPitProvider)
      {
        IPropertyPitProvider p = (IPropertyPitProvider) o;
        r += p.getPit().getClass().getSimpleName();
      }
      else if (o instanceof IProperty)
      {
        IProperty p = (IProperty) o;
        r += "property(" + _toString(p.getName(), p.getType().getSimpleName(), _toString(p.getValue())) + ")";
      }
      else if (o instanceof IPropertyDescription)
      {
        IPropertyDescription p = (IPropertyDescription) o;
        r += "description(" + _toString(p.getName(), p.getType() + ")");
      }
      else
        r += o;
    }
    return r;
  }

}
