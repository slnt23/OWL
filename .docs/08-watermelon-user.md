# user 模块：用户中心

## 定位

负责用户注册登录、角色权限管理、收货地址管理。采用 Spring Security Crypto 进行密码加密。

## 包结构

```
xyz.nanian.owl.user
├── config/
│   └── SecurityConfig.java       ← Spring Security 密码加密配置
├── constant/
│   └── UserConstant.java         ← 用户相关常量
├── controller/
│   ├── LoginController.java      ← 登录/注册接口
│   ├── RoleController.java       ← 角色管理接口
│   ├── UserController.java       ← 用户 CRUD 接口
│   ├── UserAddressController.java ← 用户地址接口
│   └── UserRoleController.java   ← 用户-角色关联接口
├── domain/
│   ├── dto/
│   │   ├── EmailLoginOrRegisterDTO.java  ← 邮箱登录/注册请求
│   │   ├── PasswordLoginDTO.java         ← 密码登录请求
│   │   ├── RoleDTO.java                  ← 角色 DTO
│   │   ├── SendCodeDTO.java              ← 发送验证码请求
│   │   └── UserInfoDTO.java              ← 用户信息 DTO
│   ├── entity/
│   │   ├── UserDO.java          ← 用户实体
│   │   ├── RoleDO.java          ← 角色实体
│   │   ├── UserRoleDO.java      ← 用户-角色关联实体
│   │   └── UserAddressDO.java   ← 用户地址实体
│   └── vo/
│       └── UserInfoVO.java      ← 用户信息视图对象
├── mapper/
│   ├── LoginMapper.java
│   ├── UserMapper.java
│   ├── RoleMapper.java
│   ├── UserRoleMapper.java
│   └── UserAddressMapper.java
├── mapstruct/
│   └── UserConvert.java          ← 用户对象转换器
└── service/
    ├── LoginService.java / impl
    ├── UserService.java / impl
    ├── RoleService.java / impl
    ├── UserRoleService.java / impl
    └── UserAddressService.java / impl
```

## 分层说明

1. **Controller**：接请求、做参数校验、返回结果
2. **Service**：业务编排（缓存、事务、并发控制）
3. **Mapper**：只管数据库操作

## 未来规划

- 博客功能将加入本模块
- 个人博客信息展示
