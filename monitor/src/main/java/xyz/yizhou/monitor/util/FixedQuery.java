package xyz.yizhou.monitor.util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 先进先出队列，若超过给定容量，则会将最早对象（队头）删除再插入（队尾）
 *
 * @author huanggc
 * @date 2021/10/12 16:25
 */
public class FixedQuery<T> {
    /** 功能实现本质是对LinkedList的扩展 */
    private final Queue<T> historyMemory = new LinkedList<>();
    /** 队列默认长度 */
    private int defaultSize = 60;

    public FixedQuery() {
    }

    public FixedQuery(int size){
        this.defaultSize = size;
    }

    /**
     * 插入内存信息到队列
     *
     * @param t 加入队列对象
     */
    public void add(T t){
        if (isFull()){
            historyMemory.poll();
        }
        historyMemory.offer(t);
    }

    /**
     * 获取队列现长度
     * @return 长度
     */
    public int len(){
        return historyMemory.size();
    }

    /**
     * 队头抛出
     * @return 队头对象
     */
    public T poll(){
        return historyMemory.poll();
    }

    /**
     * 判断队列是否已满
     * @return 若满则是true
     */
    private boolean isFull(){
        return historyMemory.size() >= defaultSize;
    }

    /**
     * 将队列输出为list
     * @return list对象
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<T> toList(){
        return (List) historyMemory;
    }

    public static void main(String[] args) {
        FixedQuery<Integer> historyMemory = new FixedQuery(2);
        historyMemory.add(1);
        historyMemory.add(2);
        historyMemory.add(3);
        historyMemory.add(4);
        System.out.println(historyMemory.len());
        System.out.println(historyMemory.poll());
        System.out.println(historyMemory.poll());
        System.out.println(historyMemory.poll());
        System.out.println(Arrays.toString(historyMemory.toList().toArray()));
    }
}
