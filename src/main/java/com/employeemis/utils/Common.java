package com.employeemis.utils;

import java.lang.reflect.Method;

public class Common {
  private static Class<?> getPrimitiveType(Class<?> wrapper) {
    if (wrapper == Integer.class) return int.class;
    if (wrapper == Boolean.class) return boolean.class;
    if (wrapper == Double.class) return double.class;
    if (wrapper == Float.class) return float.class;
    if (wrapper == Long.class) return long.class;
    if (wrapper == Short.class) return short.class;
    if (wrapper == Byte.class) return byte.class;
    if (wrapper == Character.class) return char.class;
    return wrapper;
  }

  public static <T> Method hasSetter(Class<?> clazz, String attrib, T value) throws NoSuchMethodException {
    for (Method method : clazz.getMethods()) {
      if (method.getName().equals("set" + attrib.substring(0, 1).toUpperCase() + attrib.substring(1)) && method.getParameterCount() == 1) {
        Class<?> paramType = method.getParameterTypes()[0];
        if (paramType.isPrimitive()) {
          // Unbox the wrapper type to compare with primitive
          if (getPrimitiveType(value.getClass()) == paramType) {
            return method;
          }
        } else if (paramType.isAssignableFrom(value.getClass())) {
          return method;
        }
      }
    }
    throw new NoSuchMethodException(String.format("%s has no setter for %s", clazz, attrib));
  }

  public static Method hasGetter(Class<?> clazz, String attrib) throws NoSuchMethodException {
    for (Method method : clazz.getMethods())
      if (method.getName().equals("get" + attrib.substring(0, 1).toUpperCase() + attrib.substring(1)))
        return method;
    throw new NoSuchMethodException(String.format("%s has no getter for %s", clazz, attrib));
  }

  public static void raiseIfIllegal(boolean isIlLegal, String msg) throws IllegalArgumentException {
    if (isIlLegal)
      throw new IllegalArgumentException(msg);
  }
}
