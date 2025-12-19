# 模块简介

## 启动类模块

1. 多模块项目里，只有一个模块能启动（app），其他都是插件（user/pitaya/common），全部由 app 加载。
2. 部署更改
   - redis: port 改为自己的端口，
   - mysql: 改为自己的端口，