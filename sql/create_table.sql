# 数据库初始化

-- 创建库
create database if not exists my_db;

-- 切换库
use my_db;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    unionId      varchar(256)                           null comment '微信开放平台id',
    mpOpenId     varchar(256)                           null comment '公众号openId',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    accessKey    varchar(512)                           not null comment 'accessKey',
    secretKey    varchar(512)                           not null comment 'secretKey',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_unionId (unionId)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 帖子表
create table if not exists post
(
    id         bigint auto_increment comment 'id' primary key,
    title      varchar(512)                       null comment '标题',
    content    text                               null comment '内容',
    tags       varchar(1024)                      null comment '标签列表（json 数组）',
    thumbNum   int      default 0                 not null comment '点赞数',
    favourNum  int      default 0                 not null comment '收藏数',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '帖子' collate = utf8mb4_unicode_ci;

-- 帖子点赞表（硬删除）
create table if not exists post_thumb
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子点赞';

-- 帖子收藏表（硬删除）
create table if not exists post_favour
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子收藏';

-- api接口信息表
create table if not exists interface_info
(
    id             bigint auto_increment comment '主键id' primary key,
    name           varchar(256)                       not null comment '接口名称',
    description    varchar(256)                       null comment '接口描述',
    url            varchar(512)                       not null comment '接口地址',
    requestParams  text                               null comment '请求参数',
    method         varchar(256)                       not null comment '接口类型',
    requestHeader  text                               null comment '请求头',
    responseHeader text                               null comment '响应头',
    status         int      default 0                 not null comment '接口状态 0 - 关闭， 1 - 开启',
    userId         bigint                             not null comment '创建人id',
    createTime     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete       tinyint  default 0                 not null comment '是否删除'
) comment 'api接口';

-- 用户调用接口关系表
create table if not exists user_interface_info
(
    id                      bigint auto_increment comment '主键id' primary key,
    userId                  bigint                             not null comment '调用用户id',
    interfaceInfoId         bigint                             not null comment '接口id',
    totalNum                int      default 0                 not null comment '总调用次数',
    leftNum                 int      default 0                 not null comment '剩余调用次数',
    status                  int      default 0                 not null comment '接口状态 0 - 正常， 1 - 禁用',
    createTime              datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime              datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete                tinyint  default 0                 not null comment '是否删除'
) comment '用户调用接口关系';
