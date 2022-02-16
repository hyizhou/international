package top.hyizhou.framework.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 编辑工具类，主要用于按字符或流形式读取/写入文件
 * @author hyizhou
 * @date 2021/9/2 10:39
 */
public class EditUtil {
    private static final String CHARSET = "utf8";

    /**
     * 读取整个文件字符串内容
     * @param fileName 文件路径
     * @param charset 编码方式
     * @return 字符串，若文件不是字符串大概会乱码
     */
    public static String readStr(String fileName, String charset ) throws IOException {
        return readStr(loadFile(fileName), charset);
    }
    /**
     * 读取整个文件字符串内容
     * @param file 文件
     * @param charset 编码方式
     * @return 字符串，若文件不是字符串大概会乱码
     */
    public static String readStr(File file, String charset) throws IOException {
        return new String(readBytes(file), Charset.forName(charset));
    }

    /**
     * 以二进制方式读取整个文件内容
     * @param file 文件
     * @return 读取出的二进制数组
     * @throws IOException 源文件为null或者无法读取等
     */
    public static byte[] readBytes(File file) throws IOException {
        try(FileInputStream in = new FileInputStream(file)){
            return StreamUtil.read(in);
        }
    }

    /**
     * 按路径查找文件创建File对象，并检查路径表示文件是否存在
     * @param fileName 文件名或路径
     * @return 返回创建的File对象
     * @throws IOException 文件不存在或路径所指不是文件
     */
    private static File loadFile(String fileName) throws IOException {
        if (!new File(fileName).isFile()){
            throw new IOException("路径不存在或不是文件");
        }
        return new File(fileName);
    }

    /**
     * 按路径查找文件创建File对象，若文件不存在则尝试创建
     * @param fileName 文件路径
     * @return 返回创建的File对象
     * @throws IOException 创建文件失败或路径所指不是文件
     */
    private static File createFile(String fileName) throws IOException {
        return createFile(new File(fileName));
    }
    @SuppressWarnings( "ResultOfMethodCallIgnored")
    private static File createFile(File file) throws IOException {
        String path;
        // 获取规范文件路径，若失败则获取绝对文件路径
        try {
             path = file.getCanonicalPath();
        } catch (IOException e) {
            path = file.getAbsolutePath();
        }
        if (file.exists()){
            if (!file.isFile()){
                throw new IOException("路径不是文件："+path);
            }
        }else {
            // 路径不存在，判断是否是父目录不存在，不存在则创建
            if (!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            try {
                // true 创建成功， false 文件已存在(此处不会出现false情况)， IOE 创建失败
                file.createNewFile();
            }catch (IOException e){
                throw new IOException("创建文件失败("+ e.getLocalizedMessage() +")："+path);
            }
        }
        return file;
    }

    /**
     * 写入二进制数据到文件
     * @param data 二进制数据
     * @param fileName 文件名/路径
     * @throws IOException 文件不存在或路径所指不是文件，或文件写入错误
     */
    public static void write(String fileName, byte[] data) throws IOException {
        try(final FileOutputStream out = new FileOutputStream(createFile(fileName))){
            StreamUtil.write(out, data);
        }
    }

    /**
     * 写入字符串文件，编码为默认编码utf8
     * @param fileName 文件路径
     * @param str 写入文件字符串
     * @throws IOException 文件不存在或路径所指不是文件，或写入文件错误
     */
    public static void write(String fileName, String str) throws IOException{
        write(fileName, str, CHARSET);
    }

    /**
     * 按指定编码写入字符串到文件
     * @param fileName 文件路径
     * @param str 写入字符
     * @param charset 字符编码，如utf8
     * @throws IOException 文件不存在或路径所指不是文件，或写入文件错误
     */
    public static void write(String fileName, String str, String charset) throws IOException {
        write(fileName, str.getBytes(charset));
    }

    /**
     * 将byte二进制数据写入文件
     * @param file 文件对象
     * @param data 写入数据
     * @throws IOException 文件无法创建或无法写入
     */
    public static void write(File file, byte[] data) throws IOException{
        try(FileOutputStream out = new FileOutputStream(createFile(file))){
            StreamUtil.write(out, data);
        }
    }

    /**
     * 将多个目录拼接成完整路径
     * @param dirs 目录
     * @return 拼接后的路径
     */
    public static String join(String... dirs){
        StringBuilder str = new StringBuilder();
        if (dirs == null){
            return null;
        }
        int i = 0;
        if (dirs.length > 0){
            str.append(dirs[0]);
            i++;
        }
        for (; i < dirs.length; i++) {
            str.append(File.separatorChar).append(dirs[i]);
        }
        return str.toString();
    }


}
