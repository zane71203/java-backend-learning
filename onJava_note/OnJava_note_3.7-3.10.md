了解你的意思。你要的是**真正可當筆記檔使用、可直接存成 `.md` 的乾淨 Markdown 文件**，不是混雜說明文字或程式區塊標記的版本。

下面是**整理完成的 canonical MD 講義檔**（可直接貼到 `chapter3_7-3_10.md` 使用）。

---

# On Java Chapter 3.7–3.10 筆記（Phase 0 — Java Syntax Foundation）

涵蓋主題：

- package 命名空間
- import 機制
- static 關鍵字
- main() 方法
- System.out.println()
- java.lang 預設匯入
- 第一個 Java 程式
- JVM entry model
- 命名規範
- System properties

---

# 1. Java Package（命名空間）

Java 使用 **反轉網域名稱（reverse domain name）** 作為 package 命名規則：

```java
package com.ituring.utility.foibles;
```

對應資料夾結構：

```
com/
 └── ituring/
      └── utility/
           └── foibles/
```

目的：

- 避免 class name collision
- 支援大型專案模組化
- 支援 library 生態系
- 確保 namespace 唯一性

---

## 面試題

**Q：為什麼 Java package 要使用反轉網域名稱？**

**A：**

因為網域名稱全球唯一，可避免類別命名衝突並支援大型專案模組化管理。

---

# 2. import 機制

範例：

```java
import java.util.Date;
```

作用：

告訴 compiler：

```
Date 位於 java.util package
```

否則：

```
compiler 找不到 class
```

---

## wildcard import

```java
import java.util.*;
```

表示：

```
載入整個 package
```

但 production code 不建議使用。

原因：

- 降低可讀性
- 增加 namespace ambiguity
- 增加 compiler 搜尋成本

---

## 面試題

**Q：為什麼 production code 不建議 wildcard import？**

**A：**

因為會降低可讀性、可能造成命名衝突並增加編譯器搜尋成本。

---

# 3. java.lang 預設匯入

Java 自動 import：

```java
java.lang.*
```

因此以下類別可直接使用：

```
String
System
Math
Integer
Object
```

但以下不行：

```
Date
ArrayList
Scanner
```

必須手動 import：

```java
import java.util.Date;
```

---

## 面試題

**Q：哪些 package 會自動 import？**

**A：**

```
java.lang
```

---

# 4. static 關鍵字（核心）

定義：

```
static 成員屬於 class
不是 object
```

---

## static field 範例

```java
class StaticTest {
    static int i = 47;
}
```

建立兩個物件：

```java
StaticTest st1 = new StaticTest();
StaticTest st2 = new StaticTest();
```

實際 memory：

```
只有一份 StaticTest.i
```

不是：

```
st1.i
st2.i
```

---

## 呼叫 static field 正確方式

推薦：

```java
StaticTest.i++;
```

不推薦：

```java
st1.i++;
```

原因：

static 屬於 class-level member

---

## static method

```java
class Incrementable {
    static void increment() {
        StaticTest.i++;
    }
}
```

呼叫方式：

```java
Incrementable.increment();
```

---

## static method 限制

可直接存取：

```
static variable
static method
```

不可直接存取：

```
instance variable
instance method
```

原因：

沒有 object context

---

## 面試題

**Q：static method 為什麼不能直接使用 instance variable？**

**A：**

因為 static method 屬於 class-level，不依附任何 object，因此沒有 instance context。

---

# 5. main() 方法（JVM Entry Point）

標準格式：

```java
public static void main(String[] args)
```

拆解：

| 關鍵字        | 意義                        |
| ------------- | --------------------------- |
| public        | JVM 可呼叫                  |
| static        | 不需建立物件                |
| void          | 無回傳值                    |
| String[] args | 接收 command-line arguments |

---

## JVM 呼叫流程

```
JVM 啟動
↓
載入 class
↓
直接呼叫 main()
```

因此 main() 必須是：

```
static
```

---

## 面試題

**Q：為什麼 main() 必須是 static？**

**A：**

因為 JVM 啟動程式時尚未建立任何物件，因此只能呼叫 class-level method。

---

# 6. System.out.println()

完整結構：

```
System
 └── out
      └── println()
```

實際型態：

```java
System.out
```

是：

```
PrintStream object
```

println：

```
PrintStream method
```

範例：

```java
System.out.println("Hello");
```

等價：

```
呼叫 PrintStream.println()
```

---

# 7. HelloDate 程式（第一個 Java 程式）

```java
import java.util.*;

public class HelloDate {

    public static void main(String[] args) {

        System.out.println("Hello, it's:");
        System.out.println(new Date());

    }
}
```

---

## 執行流程

Step 1

```
javac HelloDate.java
```

產生：

```
HelloDate.class
```

Step 2

```
java HelloDate
```

執行：

```
main()
```

---

# 8. System.getProperties()

列出 JVM 環境資訊：

```java
System.getProperties().list(System.out);
```

取得單一屬性：

```java
System.getProperty("user.name");
```

常見用途：

```
logging
environment detection
debug
config fallback
```

---

# 9. Java 命名規範（CamelCase）

| 類型     | 命名方式   |
| -------- | ---------- |
| class    | PascalCase |
| method   | camelCase  |
| variable | camelCase  |
| constant | UPPER_CASE |

範例：

```java
AllTheColorsOfTheRainbow
```

```java
changeTheHueOfTheColor()
```

```java
anIntegerRepresentingColors
```

```java
static final int MAX_SIZE = 100;
```

---

# 10. class 名稱必須與檔名一致

合法：

```
HelloDate.java
```

內容：

```java
public class HelloDate
```

否則：

```
compile error
```

---

# static vs instance 差異總表（核心模型）

| 類型            | 是否需要 object | memory           |
| --------------- | --------------- | ---------------- |
| static field    | 否              | 一份             |
| instance field  | 是              | 每個 object 一份 |
| static method   | 否              | class-level      |
| instance method | 是              | object-level     |

---

# 測驗題（完整題目＋選項＋答案）

---

## 題目 A1

下面哪一個 main 宣告合法？

A

```java
static void main(String args)
```

B

```java
public static void main(String[] args)
```

C

```java
public void main(String[] args)
```

D

```java
public static int main(String[] args)
```

答案：

```
B
```

---

## 題目 A2

哪個 class 不需要 import？

A

```
ArrayList
```

B

```
Date
```

C

```
System
```

D

```
Scanner
```

答案：

```
C
```

---

## 題目 A3

以下哪個方式推薦呼叫 static variable？

A

```java
new Counter().count++;
```

B

```java
Counter.count++;
```

C

```java
count++;
```

D

```java
this.count++;
```

答案：

```
B
```

---

## 題目 B1

輸出結果？

```java
class Test {
    static int x = 5;
}

public class Main {

    public static void main(String[] args) {

        Test a = new Test();
        Test b = new Test();

        a.x++;
        b.x++;

        System.out.println(Test.x);

    }
}
```

答案：

```
7
```

---

## 題目 B2

哪個敘述正確？

A

```
static method 可直接呼叫 instance variable
```

B

```
instance method 可直接呼叫 static variable
```

C

```
static method 必須透過 object 呼叫
```

D

```
instance variable 屬於 class
```

答案：

```
B
```

---

## 題目 B3

哪一行建立 object？

A

```java
Date d;
```

B

```java
Date d = new Date();
```

C

```java
import java.util.Date;
```

D

```java
Date();
```

答案：

```
B
```

---

## 題目 C1

為什麼 main() 必須 static？

A

```
語法規定
```

B

```
JVM 尚未建立 object
```

C

```
比較快
```

D

```
比較安全
```

答案：

```
B
```

---

## 題目 C2

輸出結果？

```java
class Example {

    static int x = 10;

}

public class Main {

    public static void main(String[] args) {

        Example e1 = new Example();
        Example e2 = new Example();

        e1.x = 20;

        System.out.println(e2.x);

    }
}
```

答案：

```
20
```
