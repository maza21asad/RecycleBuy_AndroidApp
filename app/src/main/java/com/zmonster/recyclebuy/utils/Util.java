package com.zmonster.recyclebuy.utils;


import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Util<T> {


    public final static String[] sex = new String[]{
            "male",
            "female"

    };
    public static String parseDouble(double num, String fmt) {
        DecimalFormat df = new DecimalFormat(fmt);
        return df.format(num);
    }
    public static String getUnique() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    /**
     * Traverse all the attributes of the class,
     */
    public static void setDefault(Class srcClass,Class dstClass) throws InstantiationException, IllegalAccessException {
        Map<String,Object> dstMap = new HashMap<>();
        Object dst = dstClass.newInstance();
        Field[] dstFields = dstClass.getDeclaredFields();
        for (Field dstField : dstFields) {
            dstMap.put(dstField.getName(),dstField.get(dst));
        }
        Object src = srcClass.newInstance();
        Field[] fields = srcClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object fieldValue = field.get(src);
            if(fieldValue == null){
                field.set(src,dstMap.get(field.getName()));
            }
        }
    }

    /**
     * Update the value of the attribute in the object
     * @param srcClass original object
     * @param dstClass new object
     * @throws Exception Exception
     */
    public void reflect(T srcClass, T dstClass) throws Exception{
        //Get raw data
        Class src = srcClass.getClass();
        Field[] fields = src.getDeclaredFields();
        //Get new data
        Class dst = dstClass.getClass();
        Field[] dstFields = dst.getDeclaredFields();
        //Traverse the original data, if the attribute value of the new data exists, update the corresponding value of the original data
        for (Field field : fields) {
            //Set private attributes to be accessible
            field.setAccessible(true);
            //Iterate over new data
            for (Field dstField : dstFields) {
                //Set private attributes to be accessible
                dstField.setAccessible(true);
                //Get attribute value
                Object dstFieldValue = dstField.get(dstClass);
                if(dstFieldValue != null && dstField.getName().equals(field.getName())){
                    field.set(srcClass,dstField.get(dstClass));
                    //Out of this cycle
                    break;
                }
            }
            System.out.println("Original "+srcClass.getClass()+"AttributeName:" + field.getName() + " AttributeValue:" + field.get(srcClass)+"\n");
        }
    }

}
