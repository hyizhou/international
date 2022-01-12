package top.hyizhou.framework.service;

import org.springframework.stereotype.Service;
import top.hyizhou.framework.entity.User;
import top.hyizhou.framework.mapper.UsersMapper;

/**
 * 账户服务
 * @author hyizhou
 * @date 2021/12/31 17:37
 */
@Service
public class AccountService {
    /** 用户信息表users的mapper */
    private final UsersMapper usersMapper;

    public AccountService(UsersMapper usersMapper) {
        this.usersMapper = usersMapper;
    }

    /**
     * 验证账户名与密码
     * @param accountName 账户名
     * @param password 密码
     * @return 验证成功则返回账户信息实体类，失败则返回null
     */
    public User verify(String accountName, String password){
        User user = usersMapper.findByAccountName(accountName);
        if (user == null){
            return null;
        }
        // TODO 此处增加对密码加密
        if (user.getPassword().equals(password)){
            return user;
        }
        return null;
    }

    /**
     * 注册账号的方法，会验证参数是否齐全和规范，并更新获得的id
     * @param user 实体类，注册成功将被更新id值
     * @return 返回值作为错误提示，若注册成功返回null，注册失败则返回错误原因
     */
    public String register(User user){
        if (isRepeat(user.getAccountName())) {
            return "账户名重复";
        }
        // TODO 可以增加更多验证
        usersMapper.insertOne(user);
        return null;
    }

    /**
     * 根据id或账户名，或这两者组合一起查找用户，当只使用其中一个进行查找时，另一个为null就行
     * @param id id值，唯一标识符
     * @param accountName 账户名，也是唯一的
     * @return 为null则没查到，否则返回对应实体类
     */
    public User findUser(Integer id, String accountName){
        if (id != null) {
            User user = usersMapper.findById(id);
            // 未匹配到id
            if (user == null){
                return null;
            }
            // 当只需匹配id，或id与账户名都匹配
            if (accountName == null || user.getAccountName().equals(accountName)){
                return user;
            }
            // id匹配但账户名不匹配
            return null;
        }else {
            // id不用管，只需要查询账户名
            return usersMapper.findByAccountName(accountName);
        }
    }

    /**
     * 判断用户名是否重复
     * @return 重复在返回true
     */
    public boolean isRepeat(String accountName){
        return usersMapper.countAccountName(accountName) >= 1;
    }
}
