
# Java Backend 學習路線圖（Roadmap vFinal）

本文件為最終可執行學習順序版本（Execution Roadmap）。
結構依序涵蓋 Java Core → Spring Boot → Production Backend 能力。

術語格式：
台灣繁體中文（簡體中文 / English）

---

# Phase 0 — Java 語法基礎（Java Syntax Foundation / Java语法基础）

## Methods（方法 / Method）

學習順序：

- 方法宣告（方法声明 / Method Declaration）
- 參數傳遞（参数传递 / Parameter Passing）
- 回傳流程（返回流程 / Return Flow）
- 方法呼叫鏈（方法调用链 / Method Call Chain）
- 作用域互動（作用域交互 / Scope Interaction）
- 堆疊框架（栈帧 / Stack Frame）
- 多方法拆解（多方法拆解 / Multi-method Decomposition）

通關條件：

- 能手寫 void / non-void 方法
- 理解 return 位置與用途
- 能拆解線性程式為多方法結構
- 能解釋 local variable scope

---

## Arrays（陣列 / 数组 / Array）

學習順序：

- 陣列宣告（数组声明 / Array Declaration）
- 陣列初始化（数组初始化 / Array Initialization）
- 索引存取（索引访问 / Index Access）
- 長度屬性（长度属性 / Length Property）
- 迴圈走訪（循环遍历 / Iteration Pattern）
- 記憶體模型（内存模型 / Memory Model）
- 參考語意（引用语义 / Reference Semantics）

通關條件：

- 理解 index 從 0 開始
- 能使用 for-loop 走訪陣列
- 理解陣列配置於堆（堆 / Heap）

---

## Class Basics（類別基礎 / 类基础 / Class Basics）

學習順序：

- 類別與物件（类与对象 / Class vs Object）
- 欄位與區域變數（字段与局部变量 / Field vs Local Variable）
- 類別方法（类方法 / Method Inside Class）
- 物件建立（对象创建 / Object Creation）
- 建構子基礎（构造函数 / Constructor）
- this 關鍵字（this关键字 / this Keyword）
- 物件參考傳遞（对象引用传递 / Object Reference Passing）

---

# Phase 1 — OOP 工程基礎（面向对象工程基础 / OOP Engineering Foundation）

學習順序：

- 建構子鏈（构造函数链 / Constructor Chaining）
- 封裝策略（封装策略 / Encapsulation Strategy）
- 存取修飾詞（访问修饰符 / Access Modifier）
- 靜態與實例（静态与实例 / Static vs Instance）
- 組合與繼承（组合与继承 / Composition vs Inheritance）
- 多型派發（多态派发 / Polymorphism Dispatch）

通關條件：

可設計：

- 使用者模型（用户模型 / User Domain Model）
- 訂單模型（订单模型 / Order Domain Model）
- 商品模型（商品模型 / Product Domain Model）

---

# Phase 2 — 集合與例外與泛型（集合 / 异常 / 泛型 / Collections + Exception + Generics）

學習順序：

## Exception（例外處理 / 异常处理 / Exception）

- try-catch
- throw（抛出异常 / Throw Exception）
- throws（声明异常 / Throws Declaration）
- 檢查型例外（检查型异常 / Checked Exception）
- 執行期例外（运行时异常 / Unchecked Exception）
- 自訂例外（自定义异常 / Custom Exception）

## Collections（集合框架 / 集合框架 / Collections Framework）

- 串列（列表 / List）
- 動態陣列（动态数组 / ArrayList）
- 鏈結串列（链表 / LinkedList）
- 集合（集合 / Set）
- 對映（映射 / Map）
- 雜湊對映（哈希映射 / HashMap）

## Generics（泛型 / 泛型 / Generics）

- 泛型類別（泛型类 / Generic Class）
- 泛型方法（泛型方法 / Generic Method）
- 通配符（通配符 / Wildcard）
- 上界（上界 / Upper Bound）
- 下界（下界 / Lower Bound）
- 型別擦除（类型擦除 / Type Erasure）

---

# Phase 3 — JVM 基礎（JVM 基础 / JVM Essentials）

學習順序：

- 呼叫堆疊（调用栈 / Call Stack）
- 區域變數生命週期（局部变量生命周期 / Local Variable Lifecycle）
- 堆記憶體模型（堆内存模型 / Heap Model）
- 物件生命週期（对象生命周期 / Object Lifecycle）
- 垃圾回收（垃圾回收 / Garbage Collection）
- 類別載入流程（类加载流程 / Class Loading Lifecycle）
- 反射機制（反射机制 / Reflection）

完成後能力：

Spring Boot Ready Java Foundation

---

# Phase 4 — Spring Boot 基礎（Spring Boot 基础 / Spring Boot Baseline）

學習順序：

- 專案結構（项目结构 / Project Structure）
- 依賴注入（依赖注入 / Dependency Injection）
- Bean 生命週期（Bean生命周期 / Bean Lifecycle）
- 設定模型（配置模型 / Configuration Model）
- 控制器（控制器 / Controller）
- 請求映射（请求映射 / Request Mapping）
- DTO 模式（数据传输对象模式 / DTO Pattern）
- 分層架構（分层架构 / Layered Architecture）

通關條件：

完成 CRUD REST API

---

# Phase 5 — 資料庫與交易模型（数据库与事务模型 / Database + Transaction）

學習順序：

- 實體映射（实体映射 / Entity Mapping）
- 關聯映射（关联映射 / Relation Mapping）
- 延遲載入（延迟加载 / Lazy Loading）
- 索引策略（索引策略 / Index Strategy）
- 執行計畫（执行计划 / Execution Plan）
- 隔離等級（隔离级别 / Isolation Level）
- 交易邊界（事务边界 / Transaction Boundary）
- 回滾範圍（回滚范围 / Rollback Scope）

完成後能力：

可投 Junior Backend Engineer

---

# Phase 6 — 安全模型（安全模型 / Security Baseline）

學習順序：

- 身分驗證（身份认证 / Authentication）
- 權限控制（权限控制 / Authorization）
- Session 機制（会话机制 / Session）
- Token 機制（令牌机制 / Token）
- JWT（JSON Web Token）
- 角色控制（角色控制 / Role Control）

---

# Phase 7 — Redis 整合（Redis 集成 / Redis Integration）

學習順序：

- 快取旁路模式（旁路缓存模式 / Cache Aside Pattern）
- 存活時間策略（存活时间策略 / TTL Strategy）
- Session 快取（会话缓存 / Session Storage）
- 限流策略（限流策略 / Rate Limiting）
- 冪等鍵（幂等键 / Idempotency Key）
- 分散式鎖（分布式锁 / Distributed Lock）

---

# Phase 8 — 可觀測性（可观测性 / Observability）

學習順序：

- 結構化日誌（结构化日志 / Structured Logging）
- 指標監控（指标监控 / Metrics）
- 健康檢查（健康检查 / Health Check）
- Micrometer
- Prometheus（概念理解 / Concept Level）
- Grafana（判讀能力 / Reading Level）
- 追蹤模型（追踪模型 / Tracing Model）

---

# Phase 9 — 雲端部署（云端部署 / Cloud Deployment）

學習順序：

- EC2 部署流程（EC2部署流程 / EC2 Deployment Workflow）
- RDS 整合（RDS集成 / RDS Integration）
- S3 儲存服務（S3存储服务 / S3 Object Storage）
- 設定分離（配置分离 / Config Separation）
- 憑證策略（凭证策略 / Credential Strategy）
- VPC 基礎概念（VPC基础概念 / VPC Mental Model）

---

# Phase 10 — 旗艦專案整合（旗舰项目整合 / Flagship Project Assembly）

最終專案需包含：

- 分層架構（分层架构 / Layered Architecture）
- DTO 管線（DTO管线 / DTO Pipeline）
- JWT 驗證流程（JWT认证流程 / JWT Auth Flow）
- Redis 快取層（Redis缓存层 / Redis Cache Layer）
- 指標監控（指标监控 / Metrics Exposure）
- 雲端部署（云端部署 / Cloud Deployment）
- 結構化日誌（结构化日志 / Structured Logging）
- 例外階層設計（异常层级设计 / Exception Hierarchy）
