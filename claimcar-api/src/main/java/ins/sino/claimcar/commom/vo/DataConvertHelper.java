package ins.sino.claimcar.commom.vo;


import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.cglib.beans.BeanCopier;

class DataConvertHelper
{
  private static Map<Class<?>, String> supportTypeMap = new HashMap();
  private static Object[] ZERO_ARRAY = new Object[0];

  public static void convertComplexObjectToTargetFromSource(Object target, Object source)
    throws Exception
  {
    Class sourceClass = source.getClass();
    Class destClass = target.getClass();
    BeanCopier copier = BeanCopier.create(sourceClass, destClass, false);
    copier.copy(source, target, null);

    List<Method> srcGetMethods = BeanUtils.getGetter(sourceClass);
    List<Method> destSetMethods = BeanUtils.getSetter(destClass);
    Map srcMethodMap = new HashMap();
    for (Method method : srcGetMethods) {
      String name = method.getName().toUpperCase();
      if (name.startsWith("IS"))
        name = name.substring(2);
      else {
        name = name.substring(3);
      }
      srcMethodMap.put(name, method);
    }
    Object[] paramArray = new Object[1];
    String targetMethodName = null;
    Method targetGetter = null;
    for (Method setter : destSetMethods) {
      Class[] targetTypes = setter.getParameterTypes();
      if (targetTypes == null) continue; if (targetTypes.length != 1) {
        continue;
      }
      Class targetType = targetTypes[0];
      if (supportTypeMap.containsKey(targetType)) {
        continue;
      }
      String name = setter.getName().toUpperCase();
      if (name.startsWith("IS"))
        name = name.substring(2);
      else {
        name = name.substring(3);
      }
      Method getter = (Method)srcMethodMap.get(name);
      if (getter == null) {
        continue;
      }

      if (targetType == List.class)
      {
        targetMethodName = setter.getName().substring(3);
        targetGetter = destClass.getMethod("get" + targetMethodName, new Class[0]);

        List source2 = (List)getter.invoke(source, ZERO_ARRAY);

        if (source2 == null) {
          continue;
        }
        List dest2 = new ArrayList(source2.size());
        paramArray[0] = dest2;
        setter.invoke(target, paramArray);

        Type genericType = targetGetter.getGenericReturnType();
        ParameterizedType paramType = (ParameterizedType)genericType;

        Class genericClazz = (Class)paramType.getActualTypeArguments()[0];
        if (supportTypeMap.containsKey(genericClazz)) {
          for (int i = 0; i < source2.size(); ++i)
            dest2.add(source2.get(i));
        }
        else {
          for (int i = 0; i < source2.size(); ++i) {
            Object destObject = genericClazz.newInstance();
            dest2.add(destObject);
          }
          for (int i = 0; i < source2.size(); ++i) {
            convertComplexObjectToTargetFromSource(dest2.get(i), source2.get(i));
          }

        }

        setter.invoke(target, new Object[] { dest2 });
      } else {
        Object source3 = getter.invoke(source, ZERO_ARRAY);
        Object dest3 = targetType.newInstance();
        if (source3 != null) {
          convertComplexObjectToTargetFromSource(dest3, source3);
          paramArray[0] = dest3;
          setter.invoke(target, paramArray);
        }
      }
    }
  }

  static
  {
    supportTypeMap.put(Short.TYPE, "");
    supportTypeMap.put(Integer.TYPE, "");
    supportTypeMap.put(Long.TYPE, "");
    supportTypeMap.put(Float.TYPE, "");
    supportTypeMap.put(Double.TYPE, "");
    supportTypeMap.put(Character.TYPE, "");
    supportTypeMap.put(Boolean.TYPE, "");

    supportTypeMap.put(Short.class, "");
    supportTypeMap.put(Integer.class, "");
    supportTypeMap.put(Long.class, "");
    supportTypeMap.put(Float.class, "");
    supportTypeMap.put(Double.class, "");
    supportTypeMap.put(Character.class, "");
    supportTypeMap.put(Boolean.class, "");
    supportTypeMap.put(String.class, "");
    supportTypeMap.put(Date.class, "");
    supportTypeMap.put(BigDecimal.class, "");

    supportTypeMap.put(Byte.TYPE, "");
  }
}
