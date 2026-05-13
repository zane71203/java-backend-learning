# 第七章 封裝（Encapsulation / 封装）

> 調整原則：保留原章節順序與核心觀念；改成台灣繁體中文用詞；重要術語附上英文與簡體中文；過時或現在較少手動處理的區塊只保留必要理解。

---

## 1. 為什麼需要封裝？

存取控制（Access Control / 访问控制），也常被稱為隱藏實作（Implementation Hiding / 隐藏实现），核心問題是：

> 程式碼一開始寫出來時，很可能不是最好的版本。  
> 之後你會想重構（Refactoring / 重构），但別人的程式可能已經依賴你的程式碼。

好的程式通常不是一次寫完就完美，而是經過多次調整。當你把一段可運作的程式放一陣子再回頭看，常常會發現更清楚、更簡潔、更容易維護的寫法。這就是重構的動機。

但是一旦你的程式被別人使用，事情就不只是「我想怎麼改就怎麼改」。

例如：

- 你是類別庫（Library / 类库）的開發者
- 其他人是客戶端程式設計師（Client Programmer / 客户端程序员）
- 他們的程式已經依賴你提供的類別、方法或欄位

這時你會遇到一個重要問題：

> 哪些東西應該允許外部使用？  
> 哪些東西只是內部實作，未來可以自由修改？

這就是封裝要解決的問題。

---

## 2. 變動與不變：類別庫設計的核心問題

在物件導向設計（Object-Oriented Design / 面向对象设计）中，常見的一個基本問題是：

> 如何區分「可以改的內部實作」與「不能隨便改的外部介面」？

對類別庫來說，這特別重要。

類別庫使用者希望：

- 升級到新版類別庫時，不需要大量修改自己的程式
- 原本可用的方法仍然可用
- 原本依賴的公開介面不要突然消失

類別庫開發者則希望：

- 可以改善內部實作
- 可以修正錯誤
- 可以替換效能較差的設計
- 不要因為外部使用者亂用內部細節，而被迫保留壞設計

如果所有欄位（Field / 字段）和方法（Method / 方法）都對外公開，類別庫開發者就會被綁死。因為你不知道外部使用者到底用了哪些東西，任何修改都有可能破壞別人的程式。

因此 Java 提供了存取修飾子（Access Modifier / 访问修饰符），讓你明確標示：

- 哪些東西可以被外部使用
- 哪些東西只給同一個套件使用
- 哪些東西只給子類別使用
- 哪些東西只允許類別自己使用

---

## 3. Java 的四種存取層級

Java 常見的存取層級，從最開放到最封閉，大致如下：

| Java 關鍵字 | 台灣常用說法 | 英文 / 簡中                 | 意義                                   |
| ----------- | ------------ | --------------------------- | -------------------------------------- |
| `public`    | 公開         | Public / 公开               | 幾乎任何地方都能存取                   |
| `protected` | 受保護       | Protected / 保护            | 同套件可存取；不同套件的子類別也可存取 |
| 無關鍵字    | 套件存取權限 | Package Access / 包访问权限 | 只有同一個套件內可存取                 |
| `private`   | 私有         | Private / 私有              | 只有同一個類別內可存取                 |

> 注意：Java 沒有 `package` 這個存取修飾子關鍵字。  
> 你「不寫」`public`、`protected`、`private` 時，就是套件存取權限（Package Access / 包访问权限）。

作為類別設計者，通常會盡量把內部細節設為 `private`，只把真正要給外部使用的方法設為 `public`。這樣未來你才有空間修改內部實作。

---

## 4. 套件（Package / 包）的概念

套件（Package / 包）是一組類別的命名空間（Namespace / 命名空间）。

你可以把它想成：

> package 是 Java 用來整理類別、避免名稱衝突、控制存取範圍的分類機制。

例如 Java 標準函式庫中有一個套件：

```java
java.util
```

裡面有很多常用類別，例如：

```java
java.util.ArrayList
```

如果你不用 `import`，就必須寫完整名稱：

```java
public class FullQualification {
    public static void main(String[] args) {
        java.util.ArrayList list = new java.util.ArrayList();
    }
}
```

這種寫法很完整，但很冗長。

因此可以用 `import` 匯入（Import / 导入）：

```java
import java.util.ArrayList;

public class SingleImport {
    public static void main(String[] args) {
        ArrayList list = new ArrayList();
    }
}
```

也可以一次匯入同一個套件下的所有類別：

```java
import java.util.*;
```

但實務上通常比較建議明確匯入需要的類別，因為可讀性較高，也比較不容易產生命名衝突。

---

## 5. 為什麼需要命名空間？

類別名稱有可能撞名。

例如：

- 你自己寫了一個 `Stack`
- Java 標準函式庫也可能有 `Stack`
- 第三方函式庫也可能有 `Stack`

如果所有類別都放在同一個全域空間，名稱很快就會衝突。

Java 透過套件（Package / 包）讓類別名稱有完整路徑。

例如：

```java
java.util.ArrayList
```

這裡的完整名稱可以理解為：

```text
java 套件下的 util 套件裡面的 ArrayList 類別
```

因此即使不同套件都有同名類別，也可以用完整名稱區分。

---

## 6. 預設套件（Default Package / 默认包）

如果你的 `.java` 檔案沒有寫 `package`，它就會放在預設套件（Default Package / 默认包）中。

這在初學範例裡很常見，因為簡單、少打一行。

但在正式專案裡，不建議使用預設套件。原因是：

- 不利於大型專案整理
- 不利於被其他套件引用
- 不符合 Spring Boot 等現代 Java 專案慣例
- 容易讓類別關係變混亂

實務上，你在 Spring Boot 專案通常會看到這種結構：

```java
package com.example.demo.user;
```

或你的專案可能會像：

```java
package com.stepjourney.member.user;
```

---

## 7. 編譯單元（Compilation Unit / 编译单元）

一個 Java 原始碼檔案（Source File / 源文件）可以稱為一個編譯單元（Compilation Unit / 编译单元）。

規則：

1. 檔案副檔名必須是 `.java`
2. 一個 `.java` 檔案最多只能有一個 `public` 類別
3. 如果有 `public` 類別，類別名稱必須和檔名完全相同，包含大小寫
4. 同一個檔案可以有其他非 `public` 類別，但通常只當作輔助類別使用

例如：

```java
// MyClass.java
public class MyClass {
    // ...
}
```

這是合法的。

但下面這種不合法：

```java
// MyClass.java
public class OtherName {
    // ...
}
```

因為 `public class OtherName` 的名稱沒有和 `MyClass.java` 對上。

---

## 8. 程式碼組織：`.java`、`.class`、JAR

當你編譯一個 `.java` 檔案後，Java 編譯器（Compiler / 编译器）會產生 `.class` 檔案。

例如：

```text
User.java  →  User.class
```

`.class` 是 Java bytecode（位元組碼 / 字节码），由 JVM（Java Virtual Machine / Java 虚拟机）載入並執行。

很多 `.class` 檔案可以再打包成 JAR（Java Archive / Java 归档文件）：

```text
app.jar
```

在現代 Spring Boot 專案中，你通常不需要手動管理 `.class` 或 JAR 的細節，Gradle 或 Maven 會幫你處理。但理解這個概念有助於你知道：

- `.java` 是你寫的原始碼
- `.class` 是編譯後結果
- JAR 是可散佈、可執行或可被引用的打包格式

---

## 9. `package` 的位置與命名慣例

如果你要使用 `package`，它必須是檔案中第一行非註解程式碼。

```java
package hiding;
```

這表示這個檔案中的類別屬於 `hiding` 這個套件。

如果檔案路徑是：

```text
hiding/mypackage/MyClass.java
```

通常檔案內會寫：

```java
package hiding.mypackage;

public class MyClass {
    // ...
}
```

Java 套件命名慣例：

- 全部小寫
- 不用駝峰式命名
- 常用反向網域名稱避免衝突

例如：

```java
package com.example.project;
package com.stepjourney.member.auth;
```

反向網域名稱（Reverse Domain Name / 反向域名）是指把網域倒過來使用。

例如：

```text
example.com → com.example
```

這樣可以降低不同公司或不同專案之間的命名衝突。

---

## 10. `import` 與名稱衝突

如果你同時匯入兩個套件，而它們都有同名類別，就可能發生名稱衝突。

例如：

```java
import com.mindviewinc.simple.*;
import java.util.*;
```

如果兩邊都有 `Vector`，那麼這行就會讓編譯器無法判斷你要哪一個：

```java
Vector v = new Vector();
```

這時必須使用完整名稱：

```java
java.util.Vector v = new java.util.Vector();
```

實務建議：

- 少用 `import *`
- 儘量明確匯入需要的類別
- 遇到同名類別時，用完整類別名稱消除歧義

---

## 11. CLASSPATH：保留基本概念即可

CLASSPATH（Class Path / 类路径）是 JVM 或編譯器用來尋找 `.class` 檔案與 JAR 檔案的位置。

以前學 Java 常需要手動設定 CLASSPATH，但現在大多數情境下：

- IDE 會幫你處理
- Gradle / Maven 會幫你處理
- Spring Boot 專案通常不用手動設定

所以這裡只需要理解：

> JVM 執行程式時，需要知道去哪裡找類別。  
> CLASSPATH 就是「找類別的搜尋路徑」。

如果你用 Gradle：

```bash
./gradlew run
./gradlew test
./gradlew bootRun
```

Gradle 會幫你處理大部分 classpath 問題。

因此原本書中大量手動設定 CLASSPATH 的範例，在現代開發中可以降低優先度。你只需要知道它存在、它負責找類別即可。

---

## 12. 建立自己的工具類別庫

當你有一些常用方法時，可以把它們整理成自己的工具類別（Utility Class / 工具类）。

例如：

```java
package onjava;

public class Range {
    public static int[] range(int n) {
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[i] = i;
        }
        return result;
    }

    public static int[] range(int start, int end) {
        int size = end - start;
        int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = start + i;
        }
        return result;
    }

    public static int[] range(int start, int end, int step) {
        int size = (end - start) / step;
        int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = start + (i * step);
        }
        return result;
    }
}
```

這段的重點不是 `range()` 本身，而是：

- 多個相關工具方法可以放在同一個類別中
- 用 `package` 把它們分類
- 用 `public static` 讓外部不用建立物件也能呼叫

實務補充：

在現代 Java 專案中，不要濫用工具類別。若邏輯屬於某個業務物件或服務，通常應該放進合適的 domain class 或 service class，而不是全部塞進 `Utils`。

---

## 13. 使用 `import` 改變行為：現代開發中較少這樣做

原文提到可以透過改變 `import` 的套件，切換不同版本的行為，例如 debug 版與 release 版。

這個概念可以理解，但在現代 Java / Spring Boot 開發中，比較常用的是：

- Profile（設定檔環境切換）
- Dependency Injection（依賴注入 / 依赖注入）
- Configuration（組態 / 配置）
- Interface + implementation（介面與實作切換）

例如 Spring Boot 會用：

```text
application-dev.yml
application-prod.yml
```

或使用不同的 Bean 來切換行為。

所以這段只需要保留概念：

> Java 沒有像 C 那樣常見的條件編譯（Conditional Compilation / 条件编译）模式。  
> 現代 Java 通常用組態、介面、依賴注入來切換行為。

---

## 14. 使用 package 的提醒

當你建立 package 時，package 名稱會對應到目錄結構。

例如：

```java
package com.stepjourney.member.user;
```

通常會對應到：

```text
src/main/java/com/stepjourney/member/user/User.java
```

如果 package 宣告和檔案所在目錄不一致，可能會導致：

- 編譯失敗
- IDE 顯示錯誤
- JVM 找不到類別
- 測試跑不起來

現代 IDE 通常會提醒你這類錯誤。你要記住的規則是：

> package 宣告要和資料夾路徑一致。

---

# 存取權限修飾子（Access Modifiers / 访问修饰符）

## 15. 存取修飾子的基本位置

Java 的存取修飾子通常放在類別、欄位、方法或建構子的前面。

例如：

```java
public class User {
    private String email;

    public String getEmail() {
        return email;
    }
}
```

這裡：

- `public class User`：外部可以使用 `User` 類別
- `private String email`：`email` 欄位只能在 `User` 類別內部使用
- `public String getEmail()`：外部可以透過方法讀取 email

這就是典型封裝：

> 欄位藏起來，透過受控方法對外提供操作。

---

## 16. 套件存取權限（Package Access / 包访问权限）

如果你不寫任何存取修飾子，就是套件存取權限。

例如：

```java
class Pie {
    void f() {
        System.out.println("Pie.f()");
    }
}
```

這裡：

- `Pie` 類別沒有 `public`
- `f()` 方法也沒有 `public`、`protected`、`private`

所以它們都是套件存取權限。

意思是：

> 同一個 package 裡的其他類別可以使用；不同 package 的類別不能使用。

這常用來表示：

- 這個類別只是同一個模組內部要用
- 不希望外部模組直接依賴它
- 未來可以比較自由地修改

---

## 17. `public`：公開介面

`public` 表示外部可以存取。

例如：

```java
package hiding.dessert;

public class Cookie {
    public Cookie() {
        System.out.println("Cookie constructor");
    }

    void bite() {
        System.out.println("bite");
    }
}
```

這裡：

- `Cookie` 是 `public`，所以其他 package 可以使用這個類別
- `Cookie()` 建構子是 `public`，所以其他 package 可以建立物件
- `bite()` 沒有寫修飾子，所以是 package access，其他 package 不能呼叫

外部程式：

```java
import hiding.dessert.*;

public class Dinner {
    public static void main(String[] args) {
        Cookie x = new Cookie();
        // x.bite(); // 無法存取，因為 bite() 不是 public
    }
}
```

這段重點：

> 類別是 public，不代表裡面所有方法都 public。  
> 每個類別、欄位、方法、建構子都各自有自己的存取權限。

---

## 18. `private`：只給自己類別內部使用

`private` 表示只有同一個類別內部可以存取。

```java
class Sundae {
    private Sundae() {}

    static Sundae makeASundae() {
        return new Sundae();
    }
}

public class IceCream {
    public static void main(String[] args) {
        // Sundae x = new Sundae(); // 不允許，建構子是 private
        Sundae x = Sundae.makeASundae();
    }
}
```

這裡的重點：

- `Sundae()` 建構子是 `private`
- 外部不能直接 `new Sundae()`
- 外部只能透過 `makeASundae()` 建立物件

這是一種控制物件建立方式的設計。

實務上，`private` 常用在：

- 欄位（Field / 字段）
- 內部輔助方法（Helper Method / 辅助方法）
- 不希望外部直接呼叫的建構子

常見寫法：

```java
public class User {
    private String email;

    public User(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
```

不要把欄位全部設成 `public`，否則外部可以隨意修改物件狀態，類別就失去封裝能力。

---

## 19. `protected`：繼承相關的存取權限

`protected` 和繼承（Inheritance / 继承）有關。

它的規則較容易混淆，可以先記成：

> `protected` 允許同 package 存取，也允許不同 package 的子類別存取。

例如：

```java
package hiding.cookie2;

public class Cookie {
    public Cookie() {
        System.out.println("Cookie constructor");
    }

    protected void bite() {
        System.out.println("bite");
    }
}
```

子類別：

```java
import hiding.cookie2.*;

public class ChocolateChip2 extends Cookie {
    public ChocolateChip2() {
        System.out.println("ChocolateChip2 constructor");
    }

    public void chomp() {
        bite(); // 可以呼叫 protected 方法
    }
}
```

這裡 `ChocolateChip2` 繼承 `Cookie`，所以可以呼叫 `protected void bite()`。

但你現在不用把 `protected` 學到非常深入，因為它會在「繼承」與「多型」章節更完整地出現。這裡先記住：

- `protected` 不是單純「比 private 開放一點」
- 它主要是為了讓子類別能使用基底類別（Base Class / 基类）的某些成員
- 實務上不要濫用，否則會讓父類別和子類別耦合太重

---

## 20. 套件存取類別 + public 建構子：外部仍然不能用

如果一個類別本身不是 `public`，即使它的建構子是 `public`，外部 package 還是不能建立它。

例如：

```java
package hiding.packageaccess;

class PublicConstructor {
    public PublicConstructor() {}
}
```

雖然建構子是 `public`，但類別 `PublicConstructor` 本身沒有 `public`，所以它只具備 package access。

外部 package 這樣寫會失敗：

```java
import hiding.packageaccess.*;

public class CreatePackageAccessObject {
    public static void main(String[] args) {
        new PublicConstructor(); // 編譯錯誤
    }
}
```

重點：

> 類別本身不可見時，外部就無法使用它的建構子。  
> 所以「public 建構子」不會讓一個 package-private 類別變成外部可用。

---

# 介面與實作（Interface and Implementation / 接口与实现）

## 21. 封裝真正想保護的是什麼？

封裝（Encapsulation / 封装）不是只是把欄位改成 `private` 而已。

它真正要做的是：

> 把「外部需要使用的介面」和「內部可以改變的實作」分開。

外部使用者應該依賴：

- public 類別
- public 方法
- 穩定的行為約定

外部使用者不應該依賴：

- 內部欄位
- 內部 helper 方法
- 暫時性的實作細節
- 未來可能重構的流程

如果你把內部實作藏好，未來就可以：

- 改寫內部演算法
- 換資料結構
- 改欄位名稱
- 改儲存方式
- 改效能策略

而不破壞外部使用者的程式。

這就是封裝在工程上的真正價值。

---

## 22. 類別內部的排列風格

原文建議可以把類別成員依照存取權限排列：

1. `public`
2. `protected`
3. package access
4. `private`

例如：

```java
public class OrganizedByAccess {
    public void pub1() {}
    public void pub2() {}
    public void pub3() {}

    private void priv1() {}
    private void priv2() {}
    private void priv3() {}

    private int i;
}
```

這樣做可以讓使用者先看到公開介面，再往下看到內部實作。

不過現代實務上，更常見的是依照「責任」與「可讀性」排列，例如：

- 欄位放前面
- 建構子接著放
- public 方法放在主要位置
- private helper 方法放在被使用的方法附近或類別下方

重點不是死背排列規則，而是讓類別容易閱讀。

---

# 類別存取權限（Class Access / 类访问权限）

## 23. 類別也有存取權限

存取修飾子也可以用在類別上。

例如：

```java
public class Widget {
    // ...
}
```

如果一個類別是 `public`，其他 package 才能直接使用它。

如果類別沒有 `public`：

```java
class InternalHelper {
    // ...
}
```

它就是 package access，只能被同一個 package 裡的其他類別使用。

---

## 24. public 類別的限制

Java 對 `public` 類別有幾個限制：

1. 每個 `.java` 檔案最多只能有一個 `public` 類別
2. `public` 類別名稱必須和檔名完全相同
3. 一個 `.java` 檔案也可以沒有任何 `public` 類別，但實務上不常這樣做

例如：

```java
// Widget.java
public class Widget {
    // ...
}
```

這是合法的。

但下面不合法：

```java
// Widget.java
public class Gadget {
    // ...
}
```

因為檔名和 public 類別名稱不同。

---

## 25. 類別不能直接是 `private` 或 `protected`

一般頂層類別（Top-Level Class / 顶层类）只能是：

- `public`
- package access（不寫修飾子）

不能寫：

```java
private class User {}
protected class User {}
```

這是非法的。

如果你不想讓外部使用某個類別，就不要加 `public`，讓它只在同一個 package 中可見。

---

## 26. 用 private 建構子控制物件建立

即使類別本身可見，你也可以透過 `private` 建構子控制是否能被外部直接建立。

```java
class Soup1 {
    private Soup1() {}

    public static Soup1 makeSoup() {
        return new Soup1();
    }
}
```

這裡：

- 外部不能直接 `new Soup1()`
- 只能呼叫 `Soup1.makeSoup()`

這種模式可以用來控制物件建立流程。

另一個例子是單例模式（Singleton Pattern / 单例模式）：

```java
class Soup2 {
    private Soup2() {}

    private static Soup2 instance = new Soup2();

    public static Soup2 access() {
        return instance;
    }

    public void f() {}
}
```

單例模式的意思是：

> 整個程式中只提供一個物件實例。

不過現代 Spring Boot 中，很多物件生命週期會交給 Spring 容器（Spring Container / Spring 容器）管理，不需要自己到處手寫 Singleton。這裡先理解設計概念即可。

---

# 本章小結

封裝（Encapsulation / 封装）和存取控制（Access Control / 访问控制）的目的，不只是限制別人，而是讓程式可以長期維護。

本章核心觀念如下：

## 1. package 用來管理命名空間

套件（Package / 包）可以：

- 組織類別
- 避免類別名稱衝突
- 影響 package access 的可見範圍
- 對應到實際資料夾結構

## 2. import 用來簡化類別名稱

`import` 可以讓你不用每次都寫完整類別名稱。

但如果不同套件有同名類別，可能會產生命名衝突，需要使用完整名稱解決。

## 3. Java 有四種主要存取層級

| 層級           | 意義                    |
| -------------- | ----------------------- |
| `public`       | 對外公開                |
| `protected`    | 同 package + 子類別可用 |
| package access | 同 package 可用         |
| `private`      | 只有同類別內部可用      |

## 4. 封裝是為了分離介面與實作

外部應該依賴穩定的 public 介面，而不是依賴類別內部細節。

這樣類別開發者未來才能安全重構。

## 5. 實務上欄位通常設為 private

典型寫法：

```java
public class User {
    private String email;

    public User(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
```

欄位不直接公開，外部透過方法取得或修改，類別才能保有控制權。

## 6. 類別本身也有可見性

一個類別如果要給其他 package 使用，就要宣告成 `public`。

如果只是套件內部輔助類別，就可以不加 `public`。

---

# 面試高頻觀念

## Q1：Java 的四種 access modifier 差在哪？

回答方向：

- `public`：任何地方都可存取
- `protected`：同 package 可存取，不同 package 的子類別也可存取
- package access：不寫修飾子，只能同 package 存取
- `private`：只有同類別內部可存取

## Q2：為什麼 field 通常要設成 private？

回答方向：

因為 field 是物件內部狀態。若直接公開，外部可以任意修改，類別無法維持自己的不變條件（Invariant / 不变量）。設成 `private` 後，可以透過方法控制讀取與修改方式。

## Q3：封裝的目的只是資料隱藏嗎？

不是。封裝更重要的是分離介面與實作。外部只依賴 public API，內部實作就能安全重構。

## Q4：package access 是什麼？

不寫 `public`、`protected`、`private` 時，就是 package access。它表示只有同一個 package 內的類別可以存取。

## Q5：一個 `.java` 檔案可以有幾個 public class？

最多一個，而且 public class 名稱必須與檔名完全相同。

---

# 目前學習位置

這一章在你的 Java Backend roadmap 中屬於：

```text
Phase 1 — OOP Engineering Foundation
主題：encapsulation + package + access modifier
```

它是後面 Spring Boot 分層設計的基礎，尤其會影響：

- Entity 欄位設計
- DTO 封裝
- Service 方法可見性
- Package 結構
- Domain model 邊界
- API 對外介面設計

可以。這次先用 **10 題混合題** 檢驗你對第七章「封裝（Encapsulation / 封装）」的理解。

範圍：

```text
package
import
public / private / protected / package-private
class access
constructor access
封裝目的
interface vs implementation
```

你直接回答：

```text
1.A
2.B
3.一句話回答
...
```

不確定可以標 `NOT SURE`。

---

# 第七章封裝檢驗題

## Q1. package 最主要的用途是什麼？

A. 讓 Java 程式跑得比較快
B. 管理命名空間，避免類別名稱衝突
C. 讓所有 class 自動變成 public
D. 讓 JVM 自動產生 constructor

---

## Q2. `import java.util.ArrayList;` 的意思是什麼？

A. 把 `ArrayList` 的原始碼複製到目前檔案
B. 讓目前檔案可以直接使用 `ArrayList` 這個類別名稱
C. 建立一個新的 `ArrayList` 物件
D. 把 `java.util` 裡所有類別都變成 public

---

## Q3. 如果一個 class 沒有寫 `public`，例如：

```java
class Helper {
}
```

這個 class 的存取權限是什麼？

A. public
B. private
C. protected
D. package-private，也就是包存取權限（package access / 包访问权限）

---

## Q4. 以下哪個敘述正確？

```java
package com.example.user;

class UserHelper {
    public UserHelper() {}
}
```

A. 因為 constructor 是 public，所以任何 package 都可以 `new UserHelper()`
B. 因為 class 不是 public，所以 package 外不能使用這個 class，即使 constructor 是 public
C. 這段會直接編譯失敗，因為 package-private class 不能有 public constructor
D. constructor 會自動變成 private

---

## Q5. `private` 成員的意思是什麼？

A. 只有同一個 package 可以存取
B. 只有子類別可以存取
C. 只有宣告它的那個 class 內部可以存取
D. 任何地方都可以存取，但不能修改

---

## Q6. 以下程式碼中，`bite()` 為什麼在 `Dinner` 裡不能呼叫？

```java
package dessert;

public class Cookie {
    public Cookie() {}

    void bite() {
        System.out.println("bite");
    }
}
```

```java
package app;

import dessert.Cookie;

public class Dinner {
    public static void main(String[] args) {
        Cookie c = new Cookie();
        c.bite();
    }
}
```

A. 因為 `Cookie` 不是 public
B. 因為 `Cookie` 的 constructor 不是 public
C. 因為 `bite()` 沒有寫 access modifier，所以是 package-private，package 外不能呼叫
D. 因為 `bite()` 回傳 void

---

## Q7. `protected` 的核心用途比較接近哪一個？

A. 讓所有人都能用
B. 完全禁止任何外部存取
C. 主要給繼承者使用，同時同 package 也可以存取
D. 只給同一個 method 裡的 local variable 使用

---

## Q8. 在 Java 中，頂層 class 可以直接宣告成 `private` 嗎？

A. 可以
B. 不可以，頂層 class 只能是 public 或 package-private
C. 可以，但只能在同一個 package 使用
D. 可以，但必須有 private constructor

---

## Q9. 封裝（Encapsulation / 封装）的主要目的，下列哪一個最完整？

A. 把所有東西都設成 public，方便使用
B. 把資料與行為放在 class 裡，並透過 access control 隱藏不該被外部依賴的實作細節
C. 讓 Java 不需要 package
D. 讓 method 一定不能回傳物件

---

## Q10. 為什麼通常建議 field 設成 private？

例如：

```java
public class User {
    private String email;
}
```

A. 因為 private field 執行速度一定比較快
B. 因為這樣可以避免外部程式直接依賴或任意修改內部資料結構
C. 因為 Java 規定所有 field 都必須 private
D. 因為 private field 不能被 constructor 使用

---

# 加分題 1：看程式判斷

以下程式碼是否可以編譯？為什麼？

```java
// file: User.java
public class User {
}

public class Admin {
}
```

請回答：

```text
可以 / 不可以
原因：
```

---

# 加分題 2：工程理解題

請用自己的話回答：

```text
為什麼 access modifier 不只是語法，而是「類庫設計者和使用者之間的合約」？
```

不用寫很長，3～5 句即可。

# 第七章封裝檢驗結果

結論：

```text
Q1～Q10：10 / 10
加分題 1：基本正確
加分題 2：正確，而且抓到核心
```

你這次對 **package / import / access modifier / 封裝目的** 的理解是穩的。

---

# 一、選擇題批改

| 題號 | 你的答案 | 正確答案 | 結果 |
| ---- | -------: | -------: | ---- |
| Q1   |        B |        B | 正確 |
| Q2   |        B |        B | 正確 |
| Q3   |        D |        D | 正確 |
| Q4   |        B |        B | 正確 |
| Q5   |        C |        C | 正確 |
| Q6   |        C |        C | 正確 |
| Q7   |        C |        C | 正確 |
| Q8   |        B |        B | 正確 |
| Q9   |        B |        B | 正確 |
| Q10  |        B |        B | 正確 |

---

# 二、逐題重點確認

## Q1：package 的用途

你的答案：B
正確。

`package` 主要是用來管理 **命名空間（namespace / 命名空间）**。

白話：

```text
不同團隊都可以有 User、Order、Service 這些 class 名稱，
但只要 package 不同，就不會撞名。
```

例如：

```java
com.stepjourney.member.User
com.example.shop.User
```

兩個都叫 `User`，但完整名稱不同。

---

## Q2：import 的意思

你的答案：B
正確。

`import` 不會複製原始碼，也不會建立物件。

它只是讓你可以少寫完整類別名稱。

```java
import java.util.ArrayList;

ArrayList list = new ArrayList();
```

等價於：

```java
java.util.ArrayList list = new java.util.ArrayList();
```

---

## Q3：class 沒寫 public

你的答案：D
正確。

```java
class Helper {
}
```

這是：

```text
package-private / package access（包存取權限 / 包访问权限）
```

意思是：

```text
同一個 package 裡可以用；
不同 package 不能用。
```

---

## Q4：package-private class + public constructor

你的答案：B
正確。

```java
package com.example.user;

class UserHelper {
    public UserHelper() {}
}
```

即使 constructor 是 `public`，外部 package 還是不能 `new UserHelper()`。

原因是：

```text
外部 package 連 UserHelper 這個 class 本身都不能存取。
constructor 再 public 也沒用。
```

這題是第七章很重要的觀念。

---

## Q5：private 成員

你的答案：C
正確。

`private` 的意思是：

```text
只有宣告它的那個 class 內部可以直接存取。
```

不是同 package。
不是子類別。
不是外部任何人。

例如：

```java
public class User {
    private String email;

    public void changeEmail(String newEmail) {
        this.email = newEmail;
    }
}
```

`email` 只能在 `User` 這個 class 裡直接用。

---

## Q6：package-private method

你的答案：C
正確。

```java
void bite() {
    System.out.println("bite");
}
```

沒有寫 `public / private / protected`，所以是：

```text
package-private
```

`Dinner` 在 `app` package，`Cookie` 在 `dessert` package，所以不能呼叫 `bite()`。

---

## Q7：protected

你的答案：C
正確。

`protected` 不是「只給子類別」，這點很多人會答錯。

比較完整是：

```text
protected = 子類別可用 + 同 package 也可用
```

所以它比 `package-private` 多了「跨 package 的子類別存取能力」。

---

## Q8：頂層 class 能不能 private

你的答案：B
正確。

頂層 class 只能是：

```text
public
package-private
```

不能直接是：

```text
private
protected
```

注意：**inner class / nested class** 可以有 `private`，但那不是這題的頂層 class。

---

## Q9：封裝目的

你的答案：B
正確。

封裝（Encapsulation / 封装）不是單純「把 field 設 private」。

更完整是：

```text
把資料與行為包進 class，
再用 access modifier 控制哪些東西能被外部依賴，
哪些東西只是內部實作細節。
```

這會直接影響工程維護能力。

---

## Q10：field 通常 private

你的答案：B
正確。

field 設成 private 的核心原因不是速度，也不是 Java 強制規定。

而是：

```text
不要讓外部直接依賴內部資料結構。
```

例如這樣不好：

```java
public class User {
    public String email;
}
```

因為外部可以直接亂改：

```java
user.email = "";
user.email = null;
user.email = "not-an-email";
```

比較好的做法是：

```java
public class User {
    private String email;

    public void changeEmail(String email) {
        // 可以在這裡檢查格式
        this.email = email;
    }
}
```

這樣 class 可以控制「email 怎麼被改」。

---

# 三、加分題 1 批改

題目：

```java
// file: User.java
public class User {
}

public class Admin {
}
```

你的回答：

> 不可以，public 只能有一個

結果：**基本正確。**

更完整答案是：

```text
不可以編譯。
一個 .java 編譯單元中最多只能有一個 public class。
而且 public class 的名稱必須和檔名相同。
```

這個檔案叫：

```text
User.java
```

所以可以有：

```java
public class User {
}
```

但不能同時再有：

```java
public class Admin {
}
```

如果要讓 `Admin` 也 public，就要拆成：

```text
User.java
Admin.java
```

---

# 四、加分題 2 批改

你的回答：

> 他可以區分 那一些是 給外部使用的接口 那一些是內部需要迭代的程式
> 透過access modifier可以讓 外部“固定”接口 依賴的地方不要頻繁改動 而內部也不會因為外部的訪問 導致被修改

結果：**正確，而且答到本章核心。**

我幫你修成比較面試可用的版本：

```text
Access modifier 不只是語法，而是類別對外提供的使用合約。

public 成員代表外部程式可以依賴的穩定接口；
private、package-private、protected 則代表比較偏內部實作，類別作者可以比較自由地修改。

這樣可以讓外部使用者只依賴穩定 API，
而類別內部仍然保有重構與調整實作的空間，
不會因為外部程式直接依賴內部細節而被綁死。
```

這段你要記起來。這是 **面試高頻觀念**。

---

# 五、這次代表你已經掌握的觀念

你目前第七章前半到中段的核心觀念已經過關：

```text
package 是命名空間
import 只是簡化類別名稱
package-private 不是 private
private 是 class 內部限定
protected = 繼承者 + 同 package
頂層 class 不能 private/protected
public constructor 無法突破 package-private class 限制
封裝是接口與實作分離
```

尤其是這兩題你答對很重要：

```text
Q4：package-private class + public constructor
Q9：封裝真正目的
```

這兩題不是背語法，是工程理解。

---

# 六、目前 roadmap 位置

```text
CURRENT_PHASE = Phase 1 — OOP Engineering Foundation
CURRENT_MODULE = encapsulation / access control
STATUS = baseline 通過
```

這代表你可以進入第七章下一層：

```text
package-private / public / private / protected 的程式碼判讀題
```

也就是不只問概念，而是給你多個 package、多個 class，讓你判斷：

```text
哪一行會編譯成功？
哪一行會編譯失敗？
為什麼？
```

這才是面試最常見的考法。
