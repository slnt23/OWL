# 新模块添加手册

本手册记录了在 OWL 项目中添加一个新的 watermelon 业务模块的完整步骤。

## 命名约定

| 项目 | 约定 | 示例 |
|------|------|------|
| 模块名（Maven artifactId） | 小写英文单词 | `blog` |
| 模块目录 | `watermelon/<英文名>` | `watermelon/blog` |
| 基础包名 | `xyz.nanian.owl.<英文名>` | `xyz.nanian.owl.blog` |
| 中文代号 | 水果名 | 博客 = kiwi（猕猴桃） |

## 步骤

### 1. 创建目录结构

```
watermelon/<name>/src/main/java/xyz/nanian/owl/<name>/
├── config/             # 模块私有配置（如 SecurityConfig）
├── constant/           # 模块常量
├── controller/         # REST 控制器
├── domain/
│   ├── entity/         # 数据表实体（*DO.java）
│   ├── dto/            # 入参 DTO
│   └── vo/             # 出参 VO
├── mapper/             # MyBatis-Plus Mapper 接口
├── mapstruct/          # MapStruct Entity ↔ DTO/VO 转换器
├── service/
│   └── impl/           # 服务接口 + 实现
└── component/          # 其他 Spring Bean（可选）

watermelon/<name>/src/main/resources/
├── mapper/             # MyBatis XML（如需要）
└── (其他配置文件)
```

### 2. 编写 pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>xyz.nanian</groupId>
        <artifactId>OWL</artifactId>
        <version>${revision}</version>
        <relativePath>../../pom.xml</relativePath>
    </parent>

    <artifactId>模块英文名</artifactId>
    <packaging>jar</packaging>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <!-- 接入依赖链：api → log → infra → common -->
        <dependency>
            <groupId>xyz.nanian</groupId>
            <artifactId>api</artifactId>
        </dependency>
    </dependencies>
</project>
```

**注意**：不要在子模块重复声明 Lombok / MapStruct 的 annotationProcessorPaths，这些由根 pom 统一管理。`spring-boot-maven-plugin` 只在 `start` 模块中开启。

### 3. 注册到根 pom.xml

**3a. 在 `<modules>` 块中添加：**

```xml
<module>watermelon/模块英文名</module>
```

**3b. 在 `<dependencyManagement>` 中添加：**

```xml
<dependency>
    <groupId>xyz.nanian</groupId>
    <artifactId>模块英文名</artifactId>
    <version>${revision}</version>
</dependency>
```

### 4. 注册到 start 模块

在 `start/pom.xml` 中添加依赖（让启动模块聚合该业务模块）：

```xml
<dependency>
    <groupId>xyz.nanian</groupId>
    <artifactId>模块英文名</artifactId>
</dependency>
```

### 5. 注册拦截器路径排除

在 `common/src/main/java/.../config/WebMvcConfig.java` 中，将本模块中不需要登录验证的接口路径加入 `excludePathPatterns`：

```java
registry.addInterceptor(loginInterceptor)
        .addPathPatterns("/**")
        .excludePathPatterns(
                // ... 已有排除 ...
                "/模块路径/public/**"   // 公开接口
        );
```

### 6. 注册 API 文档分组

在 `common/src/main/java/.../config/SpringdocConfig.java` 中，添加本模块的 API 分组：

```java
GroupedOpenApi 模块名Api = GroupedOpenApi.builder()
        .group("模块中文描述")
        .packagesToScan("xyz.nanian.owl.模块英文")
        .build();
```

## 代码规范速查

### Controller

```java
@RestController
@RequestMapping("/模块路径")
@RequiredArgsConstructor
@Tag(name = "模块中文名")
public class XxxController {

    private final XxxService xxxService;

    @GetMapping
    @Operation(summary = "查询列表")
    public Result<List<XxxVO>> list() {
        return Result.success(xxxService.list());
    }

    @PostMapping
    @Operation(summary = "新增")
    public Result<Void> create(@Valid @RequestBody XxxDTO dto) {
        xxxService.create(dto);
        return Result.success();
    }
}
```

- 所有接口返回 `Result<T>`（统一响应包装）
- 入参用 `@Valid` + DTO 做参数校验
- 使用 `@Tag` / `@Operation` 标注 API 文档

### Service

```java
@Service
@RequiredArgsConstructor
public class XxxServiceImpl implements XxxService {
    private final XxxMapper xxxMapper;
    private final XxxConvert xxxConvert;  // MapStruct

    @Override
    @BizLog(module = "模块中文", action = "具体操作")
    public void create(XxxDTO dto) {
        // ...
    }
}
```

- 构造器注入（`@RequiredArgsConstructor`）
- 需要记录操作日志的方法加 `@BizLog`

### Entity（*DO.java）

```java
@Data
@TableName("表名")
public class XxxDO {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
```

- 类名后缀 `DO`
- 表名用 `@TableName`
- 主键用 `@TableId`
- 创建/更新时间用 `FieldFill.INSERT` / `FieldFill.INSERT_UPDATE`

### Mapper

```java
@Mapper
public interface XxxMapper extends BaseMapper<XxxDO> {
    // 复杂查询写在这里，简单 CRUD 直接用 BaseMapper
}
```

### MapStruct Converter

```java
@Mapper(componentModel = "spring")
public interface XxxConvert {
    XxxVO toVO(XxxDO entity);
    XxxDO toEntity(XxxDTO dto);
}
```

### DTO / VO

```java
// DTO：入参，带校验注解
@Data
public class XxxDTO {
    @NotBlank(message = "名称不能为空")
    @Schema(description = "名称")
    private String name;
}

// VO：出参，带文档注解
@Data
public class XxxVO {
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "名称")
    private String name;
}
```

## 验证清单

- [ ] `mvn compile` 通过
- [ ] `start` 模块能启动（`java -jar start/target/start-*.jar`）
- [ ] Swagger 文档能看到新模块的 API 分组（`/doc.html`）
- [ ] 公开接口不需要 token 即可访问
- [ ] 需要登录的接口带 `Authorization: Bearer <token>` 头可正常访问
