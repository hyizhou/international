package top.hyizhou.framework.utils;

/**
 * 转换类型相关
 * @author hyizhou
 * @date 2021/7/29 16:45
 */
public class CastUtil {

    /**
     * 使用此方法转换类型，去除转换类型的警告
     * @param obj 原始类型
     * @param <T> 目标类型
     * @return 转换后的类型对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj){
        return (T) obj;
    }

    /**
     * 数组转换类型，去除转换类型的警告
     * 注意：没有对数组元素进行类型转换，仅仅对数组引用类型进行了转换而已
     * @param array 数组
     * @param <T> 目标类型
     * @return 抓换后的类型数组
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] cast(Object[] array){
        return (T[]) array;
    }

    /**
     * 将包装byte数组拆解成原始基本类型数组
     * @param bytes 包装类型的byte数组
     * @return 基本类型的byte数组
     */
    public static byte[] toPrimitive(Byte[] bytes){
        byte[] baseBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            baseBytes[i] = bytes[i];
        }
        return baseBytes;
    }

    /**
     * 将基本类型的int[]转换成包装类数组
     * @param array int[]类型数组
     * @return Integer[]类型数组
     */
    public static Integer[] boxed(int[] array){
        Integer[] result = new Integer[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }
}
