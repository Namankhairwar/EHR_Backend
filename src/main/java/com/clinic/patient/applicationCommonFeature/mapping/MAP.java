package com.clinic.patient.applicationCommonFeature.mapping;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Krishana dubey
 */
public class MAP {


    public static <T1,T2> T2 map(T1 obj1, Supplier<T2> obj, String ...o ){
       T2 obj2 =  obj.get();
        BeanUtils.copyProperties(obj1,obj2,emptyFunction(
                        obj1,obj2
                ,       Arrays.stream(o).
                        collect(Collectors.toSet())
        ));
        return obj2;
    }
    public static <T1,T2> void copyInTheObject(T1 obj1, T2 obj2, String ...o ){
        BeanUtils.copyProperties(obj1,obj2,emptyFunction(
                obj1,obj2
                ,       Arrays.stream(o).
                        collect(Collectors.toSet())
        ));

    }
    private static <T1,T2>String[] emptyFunction(T1 obj1,T2 obj2, Set<String> empty){
       BeanWrapper src = PropertyAccessorFactory.forBeanPropertyAccess(obj1);
       PropertyDescriptor[]  propertyDescriptors = src.getPropertyDescriptors();


      for(PropertyDescriptor val:propertyDescriptors){
          //.. since passwordEncoder is not able to give its value so manually added it throws a ClassNotFoundError
          if(val.getName().equals("passwordEncoder")){
              empty.add(val.getName());
              continue;
          }
        if( src.getPropertyValue(val.getName()) == null){
            empty.add(val.getName());
        }
      }

        return empty.toArray(new String[0]);
    }




     public static <T1,T2> T2 mapRequired(T1 obj1, Supplier<T2> obj, String ...o ){
       T2 obj2 =  obj.get();
        BeanUtils.copyProperties(obj1,obj2,onlyThisFunction(
                        obj1,obj2
                ,       Arrays.stream(o).
                        collect(Collectors.toSet())
        ));
        return obj2;
    }
    public static <T1,T2> void copyInTheObjectRequired(T1 obj1, T2 obj2, String ...o ){
        BeanUtils.copyProperties(obj1,obj2,onlyThisFunction(
                obj1,obj2
                ,       Arrays.stream(o).
                        collect(Collectors.toSet())
        ));

    }

     private static <T1,T2>String[] onlyThisFunction(T1 obj1,T2 obj2, Set<String> req){
       BeanWrapper src = PropertyAccessorFactory.forBeanPropertyAccess(obj1);
       PropertyDescriptor[]  propertyDescriptors = src.getPropertyDescriptors();
        Set<String> filled = new HashSet<>();

      for(PropertyDescriptor val:propertyDescriptors){
                 
        if( !req.contains(val.getName())){
            filled.add(val.getName());
        }
      }

        return filled.toArray(new String[0]);
    }

}
