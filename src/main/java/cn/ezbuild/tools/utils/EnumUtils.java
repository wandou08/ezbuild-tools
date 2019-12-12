package cn.ezbuild.tools.utils;

import cn.hutool.core.util.ReflectUtil;
import lombok.experimental.UtilityClass;

/**
 * 枚举工具类
 * @author wandoupeas
 */
@UtilityClass
public class EnumUtils {

	public static <T> T getEnumByValue(String value, Class<T> enumClass){
		for(T t:enumClass.getEnumConstants()){
			String enumValue = (String) ReflectUtil.getFieldValue(enumClass, "value");
			if(enumValue.equals(value)){
				return t;
			}
		}
		return null;
	}

}
