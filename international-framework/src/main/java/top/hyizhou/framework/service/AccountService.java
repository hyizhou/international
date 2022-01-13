package top.hyizhou.framework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final static Logger log = LoggerFactory.getLogger(AccountService.class);

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

    /**
     * 修改基础用户信息，
     * 注意，不能修改账户名：accountName，修改账户名使用方法：modifyAccountName
     * 不能修改id、accountName、isAdmin、isDelete
     * @param user 新的用户信息
     * @return 成功则返回null，失败返回错误提示
     */
    public String modify(User user){
        // 根据账户名找到用户id，再进行修改
        if (user.getAccountName() == null){
            return "修改信息时账户名缺失";
        }
        // 判断用户是否存在
        User beforeUser = findUser(user.getId(), user.getAccountName());
        if (beforeUser == null){
            return "修改信息是账户不存在";
        }
        user.setId(beforeUser.getId());

        // 将不能修改的参数赋值为null
        user.setAccountName(null);
        user.setAdmin(null);
        user.setDelete(null);
        if (usersMapper.update(user) == 1) {
            return null;
        }else {
            return "数据库更新失败，可能是数据产生变动，请稍后再试";
        }
    }

    /**
     * 修改账户名，由于账户名是唯一的，所以修改账户名需要保证唯一性
     * @param oldName 旧账户名
     * @param newName 新账户名
     * @return 成功则返回null，否则就是失败，此时返回错误提示，如不存在此账户、账户名重复
     */
    public String modifyAccountName(String oldName, String newName){
        if (usersMapper.countAccountName(newName) != 0) {
            return "新账户名已被注册";
        }
        User user = findUser(null, oldName);
        if (user == null){
            return "找不到此用户";
        }
        user.setAccountName(newName);
        if (usersMapper.update(user) == 1) {
            return null;
        }else {
            return "数据库修改失败，可能是数据发生变动，请稍后再试";
        }
    }
}
