package top.hyizhou.framework.utils;

import java.lang.reflect.Array;

/**
 * 数组工具类，作为官方 {@link java.util.Arrays} 补充
 * @author huanggc
 * @date 2021/12/14 16:10
 */
public class ArrayUtil {
    /**
     * 数组添加值，毕竟每次调用需要申请内存，需要连续多次添加值的时候尽量不要使用
     * @param array 原数组
     * @param elements 拼接到数组上的值，可为单一值或多个值或数组
     * @param <T> 泛型，定义数组元素类型
     * @return 添加完成后的数组，这个数组是新建的，原数组依然存在且没有改变
     */
    public static <T> T[] add(T[] array, T... elements){
        if (null == elements){
            return array;
        }
        if (null == array){
            return elements;
        }
        // 上面已经判断null
        int length = array.length + elements.length;
        T[] result = CastUtil.cast(Array.newInstance(array.getClass().getComponentType(), length));
        System.arraycopy(array,0,result,0,array.length);
        System.arraycopy(elements,0, result, array.length, elements.length);
        return result;
    }

}
