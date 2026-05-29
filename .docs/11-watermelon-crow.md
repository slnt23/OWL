# crow 模块：乌鸦（AI）

## 定位

AI 对话模块，预计作为项目的 AI 能力中心。集成 Spring AI 框架，支持 OpenAI 兼容接口，具备会话管理、技能注册与执行能力。后期计划引入 LangChain 开发 Agent。

## 包结构

```
xyz.nanian.owl.crow
├── config/
│   └── AiConfig.java                ← Spring AI 配置（模型、连接）
├── constant/
│   ├── AIConstant.java              ← AI 相关常量
│   └── ChatClientProperties.java    ← 聊天客户端属性配置
├── controller/
│   ├── AiChatController.java        ← AI 聊天接口
│   └── ConversationController.java  ← 会话管理接口
├── domain/
│   ├── dto/
│   │   ├── ChatRequestDTO.java           ← 聊天请求
│   │   └── CreateConversationDTO.java    ← 创建会话请求
│   ├── entity/
│   │   ├── ConversationDO.java     ← 会话实体
│   │   └── MessageDO.java          ← 消息实体
│   ├── pojo/
│   │   └── SkillMetadata.java      ← 技能元数据
│   └── vo/
│       ├── ConversationVO.java     ← 会话视图
│       └── MessageVO.java          ← 消息视图
├── mapper/
│   ├── ConversationMapper.java
│   └── MessageMapper.java
├── mapstruct/
│   └── ConversationConvert.java    ← 会话对象转换
└── service/
    ├── AiChatService.java / AiChatServiceImpl.java           ← AI 聊天服务
    ├── ConversationService.java / ConversationServiceImpl.java ← 会话管理
    ├── SkillRegistryService.java                             ← 技能注册接口
    ├── MinioSkillRegistryService.java                        ← 基于 MinIO 的技能存储
    └── SkillExecutorService.java                             ← 技能执行引擎
```

## 设计目标

- AI 聊天：支持多轮对话，会话持久化
- 技能系统：可注册、存储、执行自定义技能
- 技能存储基于 MinIO 对象存储
- 后期引入 LangChain 开发 Agent 能力
