package top.hyizhou.framework.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 流工具类
 *
 * @author hyizhou
 * @date 2021/8/5 10:12
 */
public class StreamUtil {


    /**
     * 读取输入流，这个方法特别慢，不建议用
     *
     * @param inputStream 输入流
     * @return 读取字节
     * @throws IOException 输入流为空或读取异常
     */
    public static byte[] read0(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new IOException("读取的输入流不能null");
        }
        List<Byte> byteList = new ArrayList<>();
        int c;
        while (true) {
            c = inputStream.read();
            if (c == -1) {
                break;
            }
            byteList.add((byte) c);

        }
        return CastUtil.toPrimitive(byteList.toArray(new Byte[0]));
    }

    /**
     * 读取输入流，直接返回读取到的字节数组
     * 存在bug：若流没有结束符，将一直处于阻塞状态。读文件一般不会遇到此bug，一般是网络传输，如socket才会遇到这种情况
     *
     * @param inputStream 输入流
     * @return 数据byte数组格式
     */
    public static byte[] read(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new IOException("读取的输入流不能null");
        }
        // 一次读取最大长度
        int oneReadLen = 204800;
        // 所读字符总长度
        int length = 0;
        // 实际一次读取长度
        int readLen;
        // 一次读取存储字节数据
        byte[] tempBytes = new byte[oneReadLen];
        // 存储所有读取到的字节数据
        byte[] endBytes = new byte[length];

        while (true) {
            readLen = inputStream.read(tempBytes, 0, oneReadLen);
            // 若不等于读取数组长，则是因为读到流结尾才退出，等于读取数组长则是因为数组填满退出，需要继续循环
            if (readLen != oneReadLen) {
                break;
            }
            // 运行到这里说明读取字符长度为oneReadLen
            byte[] newEndBytes = new byte[length + oneReadLen];
            // 将存储之前循环所有二进制数据的数组，复制到新的数组
            System.arraycopy(endBytes, 0, newEndBytes, 0, length);
            endBytes = newEndBytes;
            System.arraycopy(tempBytes, 0, endBytes, length, oneReadLen);
            length += oneReadLen;
        }
        // 此处情况应该是正好读完或者流没有数据
        if (readLen == -1) {
            readLen = 0;
        }
        byte[] newEndBytes = new byte[length + readLen];
        System.arraycopy(endBytes, 0, newEndBytes, 0, length);
        System.arraycopy(tempBytes, 0, newEndBytes, length, readLen);
        return newEndBytes;
    }

    /**
     * 读取输入流，返回byte数组
     * 此方法旨在解决输入流阻塞问题，且性能更加优秀，通过设定长度，一次性读取出数据
     * 如果使用此方法，设定长度确保足够取出所有数据
     *
     * @param inputStream 输入流
     * @param maxLen      读取长度
     * @return 返回的byte数组形式
     * @throws IOException xx
     */
    public static byte[] read(InputStream inputStream, int maxLen) throws IOException {
        if (inputStream == null) {
            throw new IOException("读取的输入流不能null");
        }
        // 指定maxLen，一次取出最大长度
        byte[] tempBytes = new byte[maxLen];
        int len = inputStream.read(tempBytes, 0, maxLen);
        // 设定为实际取出的值长度，并赋值
        byte[] endBytes = new byte[len];
        System.arraycopy(tempBytes, 0, endBytes, 0, len);
        return endBytes;
    }

    /**
     * 写入输出流数据
     *
     * @param outputStream 输出流
     * @param data         byte[]数据
     * @param maxByte      一次写入长度
     * @return 写入成功长度
     * @throws IOException io异常
     */
    public static int write(OutputStream outputStream, byte[] data, int maxByte) throws IOException {
        if (outputStream == null) {
            throw new IOException("写入的输出流不能为null");
        }
        // 若date为空则转换成空数组，否则后面获取长度会空指针异常
        if (data == null) {
            data = new byte[0];
        }
        int index = 0;
        // 将数据写入输出流
        while (index < data.length) {
            // 数组剩下的长度比最大长度大，则可使用最大长度
            if (maxByte < data.length - index) {
                outputStream.write(data, index, maxByte);
                index += maxByte;
            } else {
                outputStream.write(data, index, data.length - index);
                index = data.length;
            }
        }
        return index;
    }

    /**
     * 写入输出流数据
     *
     * @param outputStream 输出流
     * @param data         byte[]数据
     * @return 写入成功长度
     * @throws IOException io异常
     */
    public static int write(OutputStream outputStream, byte[] data) throws IOException {
        return StreamUtil.write(outputStream, data, data == null ? 0 : data.length);
    }

    /**
     * 将输入流内容复制到输出流。方法内部不会对流执行关闭，记得在外部将输入输出流进行关闭
     * @param in 输入流
     * @param out 输出流
     * @return 复制文件大小，单位byte
     * @throws IOException 读取或写入时发生IO错误
     */
    public static long copy(InputStream in, OutputStream out) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(out);
        long len = 0L;
        byte[] buf = new byte[1024];
        int n;
        while ((n = bufferedInputStream.read(buf)) > 0){
            bufferedOutputStream.write(buf, 0, n);
            len += n;
        }
        bufferedOutputStream.flush();
        return len;
    }

}
