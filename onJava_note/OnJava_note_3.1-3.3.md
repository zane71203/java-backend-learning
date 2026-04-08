# OnJava_note_3.1-3.3

# Java String 與記憶體模型完整筆記

本筆記整理 Java 中 String、Heap、String Pool、reference variable 與 `new String(...)` 行為模型，作為 Java Syntax Foundation 階段的核心記憶體理解基礎。

---

# 1. `String s` 的真正意思

```java
String s;
```

代表：

> 宣告一個只能存放「指向 String 物件的 reference」的變數

不是：

- 建立 String 物件
- 建立字串內容
- 指派值

只是宣告 reference variable。

理解模型：

```
我要準備一個變數 s
未來只能指向 String object
```

---

# 2. `=` 在物件型別中的意義

```java
String s = new String("asdf");
```

`=` 是：

```
assignment operator（賦值運算子）
```

作用：

```
把右邊產生的 reference 指派給左邊變數
```

不是：

- 複製物件
- 移動物件
- 建立物件

---

# 3. reference variable 存的是什麼？

物件型別變數存的是：

```
記憶體位址（reference）
```

不是物件本身。

例如：

```java
String s = "asdf";
```

實際模型：

```
s → 指向 "asdf"
```

---

# 4. Heap 是什麼？

Heap 是：

```
所有物件存放的位置
```

例如：

```java
new String("asdf")
new User()
new ArrayList<>()
```

都存在 Heap。

理解模型：

```
Heap = 物件倉庫
```

---

# 5. String Pool 是什麼？

String Pool 是：

```
Heap 裡專門管理 String literal 的區域
```

例如：

```java
String a = "asdf";
String b = "asdf";
```

結果：

```
String Pool:
    "asdf"

a → 指向同一個物件
b → 指向同一個物件
```

目的：

```
避免建立重複字串物件
節省記憶體
```

---

# 6. "asdf" 與 `new String("asdf")` 的差別

## 寫法 1

```java
String s = "asdf";
```

流程：

```
檢查 String Pool
不存在 → 建立
存在 → 重用
```

只使用 Pool 物件

---

## 寫法 2

```java
String s = new String("asdf");
```

流程：

① 處理 literal

```
"asdf"
```

```
檢查 Pool
不存在 → 建立
存在 → 重用
```

② 建立 Heap 新物件

```
new String("asdf")
```

③ s 指向 Heap 物件

結果：

```
String Pool:
    "asdf"

Heap:
    new String("asdf")

s → Heap 那個
```

---

# 7. `new String("asdf")` 會建立幾個物件？

情況如下：

| 狀況                 | 建立物件數量 |
| -------------------- | ------------ |
| Pool 原本沒有 "asdf" | 2 個         |
| Pool 已經有 "asdf"   | 1 個         |

最大：

```
2 個物件
```

最少：

```
1 個物件
```

---

# 8. 為什麼說它做了兩件事？

因為：

```java
new String("asdf");
```

包含：

```
① 處理 literal "asdf"
② 建立新的 Heap String object
```

所以邏輯上是兩個步驟

---

# 9. 沒有 reference variable 會怎樣？

例如：

```java
new String("asdf");
```

沒有變數接住：

```
Heap 裡的新 object
```

變成：

```
unreachable object
```

之後可能被：

```
Garbage Collector 回收
```

---

# 10. 但 String Pool 裡的字串仍可重用

例如：

```java
System.out.println("asdf".length());
System.out.println("asdf".length());
```

流程：

第一次：

```
Pool 建立 "asdf"
```

第二次：

```
直接重用
```

不會重新建立

---

# 11. 為什麼沒有變數仍然可以呼叫 `.length()`？

例如：

```java
"asdf".length();
```

流程：

```
JVM 取得 String Pool 裡的 "asdf" reference
直接呼叫 length()
```

不需要：

```java
String s = "asdf";
```

---

## 概念理解模型

可理解為：

```java
String temp = "asdf";
temp.length();
```

但注意：

```
這只是理解模型
不是 JVM 真正建立 temp
```

---

## temporary reference（暫時 reference）

流程：

```
取得 Pool 裡的 "asdf"
暫時使用 reference 呼叫方法
呼叫完成
reference 消失
```

但：

```
String Pool 裡的 "asdf" 仍然存在
可被未來程式碼重用
```

例如：

```java
System.out.println("asdf".length());
System.out.println("asdf".length());
System.out.println("asdf".length());
```

只會建立一次

---

## 與 `new String("asdf")` 的差別

```java
new String("asdf");
```

流程：

```
建立 Heap 新物件
沒有 reference 接住
變成 unreachable object
等待 GC 回收
```

但：

```java
"asdf".length();
```

流程：

```
取得 Pool 裡既有物件
呼叫方法
物件仍存在 Pool
```

---

## expression 可以直接呼叫方法（重要語言規則）

只要 expression 回傳 object reference，就可以直接呼叫方法：

```java
"asdf".length();
new String("abc").length();
createUser().getName();
getList().size();
```

共同原理：

```
expression → 回傳 reference → 呼叫 method
```

---

# 12. 物件是否分成 Pool 與 Heap 兩種？

不是。

正確說法：

```
所有物件都在 Heap
String Pool 是 Heap 裡的一個特殊管理區
```

模型：

```
Heap
 ├── 一般物件
 └── String Pool
```

---

# 13. literal 與 new 的語意差別（重要）

| 寫法               | 行為           |
| ------------------ | -------------- |
| "asdf"             | 使用 Pool      |
| new String("asdf") | 強制建立新物件 |

因此實務上通常避免：

```java
new String("asdf")
```

除非刻意需要不同 reference

---

# 14. 最核心記憶模型總結（建議背下來）

```java
String s = new String("asdf");
```

代表：

```
① 若 Pool 沒有 "asdf" → 建立
② 在 Heap 建立新的 String object
③ s 指向該 Heap object
```

而：

```java
String s = "asdf";
```

代表：

```
s 指向 String Pool 裡的 "asdf"
```

---

# 15. Stack 是什麼？

Stack（堆疊）是：

```
方法執行時的工作區域
```

裡面主要存放：

- primitive variables（如 `int`、`double`、`boolean`）
- reference variables（指向物件的位址）
- method call frame（方法呼叫時的執行框架）

不存放：

```
object 本體
```

---

## Stack vs Heap 的基本分工

| 區域  | 存什麼                |
| ----- | --------------------- |
| Stack | primitive + reference |
| Heap  | object                |

例如：

```java
String s = new String("asdf");
```

模型：

```
Stack:
s → reference

Heap:
String object("asdf")
```

---

## Stack 的運作方式

Stack 採用：

```
LIFO（Last In First Out）
後進先出
```

例如：

```java
public static void main(String[] args) {
    int x = 10;
    foo();
}

public static void foo() {
    int y = 20;
}
```

執行流程：

```
main() 進入 Stack
x 放入 main() 的 frame
foo() 被呼叫，建立新的 frame
y 放入 foo() 的 frame
foo() 結束，foo frame 移除
main() 結束，main frame 移除
```

---

## primitive 與 object 在 Stack / Heap 的差別

### primitive

```java
int x = 10;
```

模型：

```
Stack:
x = 10
```

primitive 直接存值。

---

### object reference

```java
String s = "abc";
```

模型：

```
Stack:
s → 某個位址

Heap / String Pool:
"abc"
```

reference 存在 Stack，object 本體存在 Heap。

---

## 方法參數也在 Stack

例如：

```java
void foo(int x) {
}

foo(10);
```

模型：

```
foo frame:
x = 10
```

若是 reference parameter：

```java
void foo(String s) {
}

foo("abc");
```

模型：

```
foo frame:
s → "abc"
```

注意：參數仍然只是 reference，不是 object 本體。

---

## Stack 為什麼快？

因為它：

- 結構簡單
- 依序進出
- 方法結束就自動釋放
- 不需要 GC 管理 Stack variable

GC 處理的是 Heap object，不是 Stack variable。

---

## Stack 的最重要一句話

```
Stack 放變數
Heap 放物件
reference 在 Stack
object 在 Heap
```

---

# 16. Stack vs Heap 的直覺比喻模型

可以這樣理解：

| 概念        | 比喻                   |
| ----------- | ---------------------- |
| Stack       | 便條紙                 |
| Heap        | 房子 / 車子 / 實體物件 |
| reference   | 地址                   |
| object      | 真正的房子本體         |
| String Pool | 共享公寓區             |

---

## Stack 像便條紙

特性：

- 小
- 快
- 暫時
- 記數字或地址
- 方法結束就丟掉

例如：

```java
int x = 10;
```

像是便條紙上寫：

```
10
```

再例如：

```java
String s = "asdf";
```

便條紙上不是寫 `asdf` 本身，而是寫：

```
某個地址
```

---

## Heap 像房子 / 車子 / 實體物件

特性：

- 大
- 慢
- 存真正資料
- 可被多個 reference 共用
- 需要 GC 管理

例如：

```java
new String("asdf")
```

像是蓋一棟房子，裡面住著 `"asdf"`。

而 `s` 只是寫著這棟房子的地址。

---

## String Pool 在比喻裡的意思

String Pool 可以想成：

```
共享公寓區
```

相同字串共用同一間房，避免重複建房。

例如：

```java
String a = "asdf";
String b = "asdf";
```

模型：

```
a → 同一個地址
b → 同一個地址
```

---

## `new String("asdf")` 為什麼多一棟房子？

因為：

1. Pool 可能先有一個共享的 `"asdf"`
2. `new String("asdf")` 又另外在 Heap 建一個新物件

所以會有：

- 一個共享公寓裡的 `"asdf"`
- 一個私人新房子的 `"asdf"`

---

## 沒有 reference 時會怎樣？

```java
new String("asdf");
```

等於：

- 蓋了一棟房子
- 但沒有任何便條紙記住地址

結果：

```
這棟房子之後可能被 GC 拆掉
```

---

## 比喻模型一句話總結

```
Stack 是便條紙（記地址）
Heap 是房子（存資料）
reference 是地址
object 是房子本體
String Pool 是共享公寓區
```

---

# 17. Stack Pointer（SP）是什麼？

Stack pointer 是：

```
指向目前 Stack 最上層位置的指標
```

更白話：

```
指向目前正在執行的那個 stack frame
```

它不是 Java 語法，而是 JVM / CPU 底層管理 Stack 的機制。

---

## 用積木 / 便條紙模型理解 SP

如果 Stack 是一層一層疊起來的工作區：

```
| bar() | ← SP
| foo() |
| main() |
```

那 SP 就像是一根手指，指著：

```
目前最上面、正在執行的那一層
```

---

## 方法呼叫時 SP 會怎樣？

例如：

```java
public static void main(String[] args) {
    foo();
}

public static void foo() {
    bar();
}

public static void bar() {
}
```

流程：

1. 進入 `main()`
2. 呼叫 `foo()`，建立新 frame，SP 上移
3. 呼叫 `bar()`，建立新 frame，SP 再上移
4. `bar()` 結束，bar frame 移除，SP 下移
5. `foo()` 結束，foo frame 移除，SP 下移
6. 回到 `main()`

模型：

```
開始:
| main() | ← SP

呼叫 foo():
| foo() | ← SP
| main() |

呼叫 bar():
| bar() | ← SP
| foo() |
| main() |
```

---

## 重要修正：SP 指向的是「正在執行」，不是「快被刪除」

這點很重要。

SP 指向最上層 frame，意思是：

```
這層現在正在執行
```

不是：

```
這層馬上要被銷毀
```

真正順序是：

```
先執行完
再 return
再把 frame 移除
再讓 SP 下移
```

---

## 為什麼遞迴會 StackOverflow？

例如：

```java
void foo() {
    foo();
}
```

每呼叫一次 `foo()`：

- 建立新的 frame
- SP 上移
- Stack 越疊越高

最後 Stack 空間用完，就會：

```
StackOverflowError
```

---

## SP 的一句話總結

```
Stack pointer 會隨著方法呼叫往上移
隨著方法結束往下移
永遠指向目前正在執行的 stack frame
```

---

# 18. Non-RAM 持久化：serialized 與 persistent 是什麼？

這部分是在講：

```
物件離開記憶體（RAM）後，如何被保存
```

---

## serialized 是什麼？

Serialization（序列化）是：

```
把記憶體中的 object 轉換成可傳輸或可儲存的格式
```

例如把 Java object 轉成：

- JSON
- byte stream
- XML
- Protocol Buffer

所以：

```
serialization 是動作
serialized data / serialized form 是結果
```

它不代表一定已經存進資料庫。

---

## persistent 是什麼？

Persistent（持久化）是：

```
資料已被寫入長期儲存系統
```

例如：

- SQL database
- NoSQL database
- file system
- object storage
- 某些 cache / queue 系統

所以：

```
persistent 是資料狀態
不是格式轉換動作
```

---

## serialized 與 persistent 的關係

兩者不是同一件事。

比較：

| 名詞          | 本質         |
| ------------- | ------------ |
| serialization | 格式轉換     |
| persistence   | 資料落地儲存 |

常見流程可能是：

```
object
↓
serialization / mapping
↓
storage format
↓
write into DB / file / queue
↓
persistent data
```

但不是所有 persistence 都一定要先做你想像中的 Java Serializable 那種 serialization。

---

## SQL / NoSQL 在這裡的角色

SQL / NoSQL 是：

```
persistent storage backend
```

它們是持久化的儲存系統，不是 serialized / persistent 的定義本身。

---

## 工程上更精確的說法

不要說：

```
serialized object 就是存在 SQL / NoSQL 的物件
```

更精確應該說：

```
object 可能先被 mapping 或 serialization 成某種儲存格式
再寫入 SQL / NoSQL
成為 persistent data
```

---

# 19. 物件永久化時一定要先 serialized 嗎？

答案：

```
通常需要經過 mapping 或 serialization
但不是一定經過 Java Serializable 那種形式
```

---

## SQL database 常見情況

例如：

```java
userRepository.save(user);
```

對 SQL 而言，常見流程是：

```
Java object
↓
ORM mapping（JPA / Hibernate）
↓
SQL row
↓
寫入資料庫
```

這比較像：

```
object → relational mapping → persistent row
```

不是單純的 Java byte serialization。

---

## NoSQL 常見情況

例如 MongoDB：

```
Java object
↓
JSON / BSON serialization
↓
document
↓
寫入 MongoDB
```

這種情況就比較接近：

```
serialize → persist
```

---

## 關鍵觀念

沒有任何物件可以「直接原封不動放進 DB」。

一定會經過：

- mapping
- serialization
- conversion

只是很多 framework 幫你自動做完了，所以你感覺像是直接存。

---

## 一句話總結

```
物件永久化通常要先被轉成儲存系統可接受的格式
再寫入 storage，才成為 persistent data
```

---

# 20. JSONB 是不是一種 serialization？

答案：

```
JSONB 可以視為 serialization 結果進入資料庫後的一種儲存格式
但 JSONB 本身不是 serialization 動作
```

---

## JSONB 是什麼？

JSONB 是 PostgreSQL 提供的：

```
binary JSON storage format
```

比較：

| 類型  | 說明            |
| ----- | --------------- |
| JSON  | 文字格式        |
| JSONB | binary 儲存格式 |

---

## 一般流程

典型模型：

```
Java object
↓
serialization（例如 Jackson）
↓
JSON
↓
PostgreSQL 轉成 JSONB
↓
存入資料庫
```

所以：

- serialization：動作
- JSON：中間表示格式
- JSONB：DB 內部儲存格式

---

## JSONB 與 Java Serializable 不同

這兩個常被混淆。

| 技術               | 意義                                     |
| ------------------ | ---------------------------------------- |
| JSON serialization | 把 object 轉成 JSON                      |
| JSONB              | PostgreSQL 的 JSON binary storage format |
| Java Serializable  | JVM byte stream serialization            |

三者不是同一件事。

---

## JSONB 一句話總結

```
JSONB 是 object 被轉成 JSON 後，
由 PostgreSQL 轉換並保存的 binary JSON 格式
```

---

# 21. 《On Java》3.2.3 這段在講什麼？（Java 中的數組）

這段文字的核心是：

```
Java 的陣列比 C / C++ 更安全
因為一定初始化，而且不能亂讀陣列範圍外記憶體
```

---

## 這段的三個重點

### 1. Java 陣列一定會初始化

例如：

```java
int[] arr = new int[5];
```

結果不是垃圾值，而是：

```
[0, 0, 0, 0, 0]
```

如果是：

```java
String[] arr = new String[3];
```

結果是：

```
[null, null, null]
```

---

### 2. Java 不允許讀取陣列邊界外的元素

例如：

```java
arr[10]
```

若長度不夠，Java 會直接丟：

```
ArrayIndexOutOfBoundsException
```

而不是像 C / C++ 那樣讀到未知記憶體。

---

### 3. 物件陣列裡存的是 reference，預設值是 null

例如：

```java
String[] arr = new String[2];
```

裡面不是兩個空字串，而是：

```
arr[0] = null
arr[1] = null
```

因為目前還沒有指向任何 `String object`。

---

## 為什麼這很重要？

因為如果你直接：

```java
arr[0].length();
```

會得到：

```
NullPointerException
```

必須先：

```java
arr[0] = "abc";
```

再用：

```java
arr[0].length();
```

---

## 基本型別陣列與 reference 陣列的預設值

| 類型      | 預設值 |
| --------- | ------ |
| int       | 0      |
| double    | 0.0    |
| boolean   | false  |
| char      | ' '     |
| reference | null   |

---

## 這段的一句話總結

```
Java 陣列一定初始化，且不能越界亂讀，
所以比 C / C++ 安全很多
```
