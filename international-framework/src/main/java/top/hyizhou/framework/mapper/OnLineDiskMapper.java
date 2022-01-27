package top.hyizhou.framework.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.hyizhou.framework.pojo.OnLineDisk;

import java.util.List;

/**
 * onlinedisk表数据访问层
 * @author hyizhou
 * @date 2022/1/27 16:33
 */
@Mapper
public interface OnLineDiskMapper {
    /**
     * 根据userId查找
     * @param userId 主键，用户id
     * @return 查询到的数据
     */
    OnLineDisk findById(@Param("userId") Integer userId);

    /**
     * 得到所有数据
     * @return 所有数据
     */
    List<OnLineDisk> findAll();

    /**
     * 添加一行数据
     * @param onLineDisk 一行实体类
     * @return 成功则返回1
     */
    int insertOne(OnLineDisk onLineDisk);

}
