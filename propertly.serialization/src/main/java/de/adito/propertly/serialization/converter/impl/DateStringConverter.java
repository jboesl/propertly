package de.adito.propertly.serialization.converter.impl;

import javax.annotation.*;
import java.text.*;
import java.util.Date;

/**
 * @author j.boesl, 04.03.15
 */
public class DateStringConverter extends AbstractObjectStringConverter<Date>
{
  private final SimpleDateFormat dateFormat;

  public DateStringConverter()
  {
    super(Date.class);
    dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S z");
    registerSourceTargetConverter(new SourceTargetConverter<Date, String>(String.class)
    {
      @Nonnull
      @Override
      public String sourceToTarget(@Nonnull Date pSource)
      {
        return dateFormat.format(pSource);
      }

      @Nullable
      @Override
      public Date targetToSource(@Nonnull String pTarget)
      {
        try
        {
          return dateFormat.parse(pTarget);
        }
        catch (ParseException e)
        {
          throw new RuntimeException(e);
        }
      }
    });
    registerSourceTargetConverter(new SourceTargetConverter<Date, Number>(Number.class)
    {
      @Nonnull
      @Override
      public Number sourceToTarget(@Nonnull Date pSource)
      {
        return pSource.getTime();
      }

      @Nullable
      @Override
      public Date targetToSource(@Nonnull Number pTarget)
      {
        return new Date(pTarget.longValue());
      }
    });
  }
}
