# 创建数据表

create table file
(
    id int unsigned not null comment '所属用户id',
    file_id int unsigned auto_increment comment '文件id'
        primary key,
    file_name varchar(100) not null comment '文件名',
    file_path varchar(100) not null comment '文件所在目录路径',
    is_deleted tinyint(1) null comment '是否被删除',
    file_size bigint unsigned null comment '文件大小',
    path_id int unsigned null comment '目录id',
    constraint file_file_name_IDX
        unique (file_name, file_path)
)
    comment '文件基础信息表';

create table onlinedisk
(
    user_id int unsigned not null comment '用户id'
        primary key,
    dir_name varchar(100) not null comment '所属目录名',
    all_size bigint unsigned not null comment '总空间大小(byte)',
    use_size bigint unsigned not null comment '使用空间大小(byte)',
    shutoff tinyint(1) null comment '是否被关闭'
)
    comment '用户网盘信息';

create table shared
(
    id int unsigned auto_increment comment '共享id'
        primary key,
    user_id int unsigned not null comment '分享用户',
    path varchar(100) not null comment '文件/目录路径',
    is_file tinyint(1) null comment '是否为文件',
    info_id int unsigned null comment '详细信息id，若为文件则是file表，若为目录则是目录表，若是0则没有数据库记录',
    shared_time datetime null comment '共享截止时间',
    nu_down int unsigned null comment '下载次数'
)
    comment '共享信息表';

create table users
(
    id int unsigned auto_increment comment 'id值'
        primary key,
    name varchar(100) not null comment '用户名',
    password varchar(100) not null comment '密码',
    create_time datetime default CURRENT_TIMESTAMP not null comment '注册时间',
    is_admin tinyint(1) default 0 not null comment '管理员标识',
    is_delete tinyint(1) default 0 not null comment '已经被删除标识',
    email varchar(100) null comment '邮箱',
    phone varchar(100) null comment '电话号码',
    account_name varchar(100) not null comment '账户名，唯一',
    Column2 varchar(100) null comment '备用'
)
    comment '用户信息表';

create table file
(
    id int unsigned not null comment '所属用户id',
    file_id int unsigned auto_increment comment '文件id'
        primary key,
    file_name varchar(100) not null comment '文件名',
    file_path varchar(100) not null comment '文件所在目录路径',
    is_deleted tinyint(1) null comment '是否被删除',
    file_size bigint unsigned null comment '文件大小',
    path_id int unsigned null comment '目录id',
    constraint file_file_name_IDX
        unique (file_name, file_path)
)
    comment '文件基础信息表';

create table onlinedisk
(
    user_id int unsigned not null comment '用户id'
        primary key,
    dir_name varchar(100) not null comment '所属目录名',
    all_size bigint unsigned not null comment '总空间大小(byte)',
    use_size bigint unsigned not null comment '使用空间大小(byte)',
    shutoff tinyint(1) null comment '是否被关闭'
)
    comment '用户网盘信息';

create table shared
(
    id int unsigned auto_increment comment '共享id'
        primary key,
    user_id int unsigned not null comment '分享用户',
    path varchar(100) not null comment '文件/目录路径',
    is_file tinyint(1) null comment '是否为文件',
    info_id int unsigned null comment '详细信息id，若为文件则是file表，若为目录则是目录表，若是0则没有数据库记录',
    shared_time datetime null comment '共享截止时间',
    nu_down int unsigned null comment '下载次数'
)
    comment '共享信息表';

create table users
(
    id int unsigned auto_increment comment 'id值'
        primary key,
    name varchar(100) not null comment '用户名',
    password varchar(100) not null comment '密码',
    create_time datetime default CURRENT_TIMESTAMP not null comment '注册时间',
    is_admin tinyint(1) default 0 not null comment '管理员标识',
    is_delete tinyint(1) default 0 not null comment '已经被删除标识',
    email varchar(100) null comment '邮箱',
    phone varchar(100) null comment '电话号码',
    account_name varchar(100) not null comment '账户名，唯一',
    Column2 varchar(100) null comment '备用'
)
    comment '用户信息表';

