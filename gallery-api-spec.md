# 画廊模块 API 接口文档

> 本文档用于前后端联调，定义了画廊（Gallery）模块的所有接口规范。

## 基础信息

| 项目         | 说明                                     |
| ------------ | ---------------------------------------- |
| 基础路径     | `/gallery`                               |
| 请求格式     | JSON / `multipart/form-data`（上传接口） |
| 统一返回格式 | `Result<T>`                              |
| 鉴权         | 需要登录（`requiresAuth: true`）         |

---

## 统一返回格式

```json
{
  "code": 200,
  "message": "success",
  "data": { ... }
}
```

### 分页返回格式

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "currentPage": 1,
    "pageSize": 10,
    "total": 100,
    "totalPage": 10,
    "records": [ ... ]
  }
}
```

---

## 数据模型

### GalleryItemVO（视图对象 - 返回给前端）

| 字段           | 类型     | 必填 | 说明       |
| -------------- | -------- | ---- | ---------- |
| `id`           | `number` | 是   | 画廊项 ID  |
| `title`        | `string` | 是   | 标题       |
| `description`  | `string` | 是   | 描述       |
| `imageUrl`     | `string` | 是   | 大图 URL   |
| `thumbnailUrl` | `string` | 是   | 缩略图 URL |
| `sortOrder`    | `number` | 是   | 排序序号   |

### GalleryItemDTO（传输对象 - 前端提交）

| 字段          | 类型     | 必填 | 说明                    |
| ------------- | -------- | ---- | ----------------------- |
| `id`          | `number` | 否   | 画廊项 ID（更新时携带） |
| `title`       | `string` | 是   | 标题                    |
| `description` | `string` | 否   | 描述                    |
| `image`       | `File`   | 否   | 大图文件                |
| `thumbnail`   | `File`   | 否   | 缩略图文件              |
| `sortOrder`   | `number` | 是   | 排序序号                |

---

## 接口列表

### 1. 获取画廊列表

| 项目 | 说明                      |
| ---- | ------------------------- |
| 路径 | `GET /gallery`            |
| 参数 | 无                        |
| 返回 | `Result<GalleryItemVO[]>` |

**请求示例**

```http
GET /gallery
```

**响应示例**

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "title": "示例图片",
      "description": "这是一张示例图片",
      "imageUrl": "https://example.com/images/1.jpg",
      "thumbnailUrl": "https://example.com/images/1_thumb.jpg",
      "sortOrder": 1
    }
  ]
}
```

---

### 2. 分页获取画廊列表

| 项目 | 说明                                       |
| ---- | ------------------------------------------ |
| 路径 | `GET /gallery/page`                        |
| 参数 | `pageNum`（默认 1）, `pageSize`（默认 10） |
| 返回 | `Result<PageResult<GalleryItemVO>>`        |

**请求示例**

```http
GET /gallery/page?pageNum=1&pageSize=10
```

**响应示例**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "currentPage": 1,
    "pageSize": 10,
    "total": 50,
    "totalPage": 5,
    "records": [
      {
        "id": 1,
        "title": "示例图片",
        "description": "这是一张示例图片",
        "imageUrl": "https://example.com/images/1.jpg",
        "thumbnailUrl": "https://example.com/images/1_thumb.jpg",
        "sortOrder": 1
      }
    ]
  }
}
```

---

### 3. 获取画廊详情

| 项目 | 说明                    |
| ---- | ----------------------- |
| 路径 | `GET /gallery/{id}`     |
| 参数 | `id`（路径参数）        |
| 返回 | `Result<GalleryItemVO>` |

**请求示例**

```http
GET /gallery/1
```

**响应示例**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "title": "示例图片",
    "description": "这是一张示例图片",
    "imageUrl": "https://example.com/images/1.jpg",
    "thumbnailUrl": "https://example.com/images/1_thumb.jpg",
    "sortOrder": 1
  }
}
```

---

### 4. 新增画廊项

| 项目         | 说明                                                         |
| ------------ | ------------------------------------------------------------ |
| 路径         | `POST /gallery`                                              |
| Content-Type | `multipart/form-data`                                        |
| 参数         | `title`, `description?`, `image?`, `thumbnail?`, `sortOrder` |
| 返回         | `Result<number>`（返回新增 ID）                              |

**请求示例**

```http
POST /gallery
Content-Type: multipart/form-data

title: 新图片
description: 新图片描述
sortOrder: 1
image: (文件)
thumbnail: (文件)
```

**响应示例**

```json
{
  "code": 200,
  "message": "success",
  "data": 123
}
```

---

### 5. 更新画廊项

| 项目         | 说明                                                                           |
| ------------ | ------------------------------------------------------------------------------ |
| 路径         | `PUT /gallery/{id}`                                                            |
| Content-Type | `multipart/form-data`                                                          |
| 参数         | `id`（路径参数）, `title`, `description?`, `image?`, `thumbnail?`, `sortOrder` |
| 返回         | `Result<null>`                                                                 |

**请求示例**

```http
PUT /gallery/1
Content-Type: multipart/form-data

title: 更新后的标题
description: 更新后的描述
sortOrder: 2
image: (文件，可选)
thumbnail: (文件，可选)
```

**响应示例**

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 6. 删除画廊项

| 项目 | 说明                   |
| ---- | ---------------------- |
| 路径 | `DELETE /gallery/{id}` |
| 参数 | `id`（路径参数）       |
| 返回 | `Result<null>`         |

**请求示例**

```http
DELETE /gallery/1
```

**响应示例**

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

## 前端调用方式

```typescript
import { galleryApi } from '@/modules/gallery/api';

// 获取列表
const list = await galleryApi.list();

// 分页
const page = await galleryApi.page(1, 10);

// 详情
const detail = await galleryApi.getById(1);

// 新增
const newId = await galleryApi.create({
  title: '新图片',
  description: '描述',
  image: file,
  sortOrder: 1,
});

// 更新
await galleryApi.update(1, {
  id: 1,
  title: '更新标题',
  sortOrder: 2,
});

// 删除
await galleryApi.deleteById(1);
```

---

## 注意事项

1. **图片上传**：新增和更新接口使用 `multipart/form-data`，图片文件通过 `image` 和 `thumbnail` 字段上传
2. **缩略图**：如果后端支持自动生成缩略图，前端可以不传 `thumbnail` 字段
3. **排序**：`sortOrder` 字段用于控制展示顺序，数值越小越靠前
4. **鉴权**：所有接口都需要登录态，请求头需携带 token
5. **错误处理**：非 200 状态码时，前端会显示 `message` 中的错误信息
