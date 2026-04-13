收到。下面我直接按你要的格式重整，不再放 roadmap 位置那段。

這次整理分成兩大部分：

# 1. 章節筆記

# 2. 完整題目整理

包含：

- **完整問題**
- **完整選項**
- **你的回答**
- **正確答案**
- **不論對錯都給的詳細解答**
- **錯題釐清**
- **面試常問問題**
- **詳細回答**

---

# 1. 章節筆記

範圍：**On Java 3.5 使用 class 關鍵字創建新類型**、**3.6 方法、參數以及返回值**

---

## 1-1 `class`（類別 / 类 / Class）是什麼

Java 用 `class` 來定義一種**新型別**。

```java
class ATypeName {
}
```

這代表你定義了一個叫做 `ATypeName` 的類別。
它不是物件本身，而是「這一類物件應該長什麼樣、可以做什麼事」的描述。

### 核心觀念

- `class` 是模板
- `object`（物件 / 对象 / Object）是依照 class 建立出來的實例
- Java 裡自訂型別的基本方式就是 `class`

---

## 1-2 object（物件 / 对象 / Object）怎麼建立

建立物件要用 `new`：

```java
ATypeName a = new ATypeName();
```

這行要拆開看：

- `ATypeName`：類別名稱
- `a`：reference variable（參考變數 / 引用變數 / 引用变量）
- `new ATypeName()`：建立一個新的 `ATypeName` 物件

### 你現在要先這樣理解

這行不是把整個物件「塞進」變數 `a`。
比較接近的理解是：

- `new ATypeName()` 建出物件
- `a` 保存的是對那個物件的 reference（參考）

---

## 1-3 field（欄位 / 字段 / Field）是什麼

當你定義一個 class 時，可以在 class 內定義資料。

```java
class DataOnly {
    int i;
    double d;
    boolean b;
}
```

這裡的：

- `i`
- `d`
- `b`

都叫做 **field**

### field 的位置

**在 class 裡，method 外面**

這一點很重要，因為它跟 local variable 會混。

---

## 1-4 local variable（區域變數 / 局部變數 / 局部变量）是什麼

如果變數寫在 method 裡面，它就不是 field，而是 local variable。

```java
class Test {
    void f() {
        int x; // local variable
    }
}
```

### field 與 local variable 的差別

## field

- 在 class 裡
- 屬於物件狀態的一部分
- 有 default value（預設值 / 默认值 / Default Value）

## local variable

- 在 method 裡
- 只在 method 執行期間存在
- **沒有 default value**
- 使用前必須手動賦值

---

## 1-5 `.`（member access）是什麼

你在書上看到：

```java
objectReference.member
```

這表示：

> 透過某個 object reference，去存取那個物件的 member（成員）

member 可以是：

- field
- method

例如：

## 存取 field

```java
data.i = 47;
```

## 呼叫 method

```java
obj.f();
```

所以 `member` 不只欄位，也可以是方法。

---

## 1-6 field 的 default value（預設值）

如果 field 沒有手動初始化，Java 會給它預設值。

常見的有：

- `boolean` → `false`
- `char` → `\u0000`
- `byte` → `(byte)0`
- `short` → `(short)0`
- `int` → `0`
- `long` → `0L`
- `float` → `0.0f`
- `double` → `0.0d`

例如：

```java
class DataOnly {
    int i;
}
```

```java
DataOnly data = new DataOnly();
System.out.println(data.i);
```

輸出會是：

```java
0
```

---

## 1-7 local variable 沒有 default value

這是本節非常關鍵的點。

```java
class Test {
    void f() {
        int x;
        System.out.println(x); // 編譯錯誤
    }
}
```

這裡 `x` 是 local variable，Java **不會自動幫它補 0**。
所以在使用它之前，你必須自己先賦值：

```java
int x = 0;
System.out.println(x);
```

---

## 1-8 method（方法 / 方法 / Method）是什麼

Java 中 method 是 class 的一部分，用來描述物件能做的事。

method 的基本結構：

```java
ReturnType methodName(/* 參數列表 */) {
    // 方法體
}
```

例如：

```java
int storage(String s) {
    return s.length() * 2;
}
```

這個 method：

- 名字叫 `storage`
- 參數是 `String s`
- 回傳型別是 `int`

---

## 1-9 method 的組成

一個 method 最基本有幾個部分：

### 1. return type（返回型別 / 返回类型 / Return Type）

method 執行完之後，會回傳什麼型別的值。

例如：

```java
int f()
```

表示它要回傳 `int`

```java
void f()
```

表示它不回傳值

---

### 2. method name（方法名 / 方法名 / Method Name）

例如：

```java
add
storage
flag
```

---

### 3. parameter list（參數列表 / 参数列表 / Parameter List）

method 接收外部資料的地方。

例如：

```java
int add(int a, int b)
```

這裡的 `int a, int b` 就是參數列表。

---

### 4. method body（方法體 / 方法体 / Method Body）

真正執行內容的大括號區塊。

---

## 1-10 parameter（參數 / 参数 / Parameter）是什麼

parameter 是 method 定義時，用來接收輸入資料的變數。

例如：

```java
int add(int a, int b)
```

- `a` 是 parameter
- `b` 是 parameter

當呼叫：

```java
add(2, 3)
```

- `2`、`3` 是 argument（實際傳入值）

### parameter 與 argument 差別

## parameter

定義 method 時寫的形式變數

## argument

呼叫 method 時實際傳進去的值

---

## 1-11 return（回傳 / 返回 / return）是什麼

`return` 有兩個作用：

### 1. 結束 method

執行到 `return`，這個 method 就離開了。

### 2. 把值回傳給呼叫者

例如：

```java
int f() {
    return 5;
}
```

這代表把 `5` 回傳出去。

---

## 1-12 `void` 與 non-void 的差別

### `void`

表示 method 不回傳值。

```java
void nothing() {
}
```

也可以寫：

```java
void nothing() {
    return;
}
```

這裡的 `return;` 只是提前離開 method，不帶任何值。

---

### non-void

表示 method **一定要回傳對應型別的值**

```java
int f() {
    return 5;
}
```

如果你寫：

```java
int f() {
}
```

就不合法，因為你承諾要回傳 `int`，但實際上沒有。

---

## 1-13 method 呼叫語法

書上的形式是：

```java
objectReference.methodName(arg1, arg2, arg3);
```

這代表：

- 透過某個 object reference
- 呼叫它的某個 method
- 並傳入參數

例如：

```java
Test t = new Test();
t.add(2, 3);
```

---

## 1-14 這兩節最重要的核心結論

你現在至少要完全站穩這幾個點：

### A. `class` 是模板，`object` 是實例

### B. 建立物件用 `new`

### C. field 在 class 裡、method 外

### D. local variable 在 method 裡

### E. field 有 default value，local variable 沒有

### F. method 有 return type、name、parameter list、body

### G. `return` 會結束 method，必要時回傳值

### H. `void` 不回傳值，non-void 必須回傳對應型別

---

# 2. 完整問題整理

---

## Q1

### 完整問題

Java 中 `class` 的作用是什麼？

### 選項

A. 宣告變數
B. 建立物件模板
C. 建立方法呼叫順序
D. 宣告記憶體位址

### 你的回答

B

### 正確答案

**B. 建立物件模板**

### 詳細解答

`class` 是用來定義一種新型別，描述某一類物件應有哪些資料與行為。

例如：

```java
class Dog {
    int age;
    void bark() {
        System.out.println("wang");
    }
}
```

這裡 `Dog` 不是一隻真實存在的狗，而是「狗這種類型」的定義。
之後你才能用 `new Dog()` 建立真正的物件。

### 錯題釐清

你這題答對，沒有錯題。
但要再精準一點：
`class` 不是單純「做模板」這麼抽象，而是 **Java 用來宣告新型別的核心語法**。

### 面試常問問題

**Q：class 跟 object 差在哪？**

### 詳細回答

- `class` 是類別，是一種型別定義
- `object` 是依據 class 建立出來的實例
- `class` 描述結構與行為
- `object` 是實際存在、可被操作的實體

---

## Q2

### 完整問題

下面哪個敘述正確？

### 選項

A. 物件就是 class
B. class 是物件的實例
C. 物件是 class 的實例
D. class 是方法集合而已

### 你的回答

C

### 正確答案

**C. 物件是 class 的實例**

### 詳細解答

這題在檢查你是否分得清：

- `class`
- `object`
- `instance`

例如：

```java
class Car {}
Car c = new Car();
```

這裡：

- `Car` 是 class
- `new Car()` 建出的是 object
- 那個 object 就是 `Car` 的 instance

### 錯題釐清

你這題答對。

### 面試常問問題

**Q：什麼是 instance？**

### 詳細回答

instance 就是某個 class 建立出來的具體物件。
如果 `User` 是 class，那 `new User()` 建立出來的東西就是 `User` 的 instance。

---

## Q3

### 完整問題

下面哪個屬於 `field`（欄位）？

### 選項

A.

```java
int x;
```

（在 class 裡）

B.

```java
int x;
```

（在 method 裡）

C.

```java
void x(){}
```

D.

```java
return x;
```

### 你的回答

A（你說你是猜的）

### 正確答案

**A**

### 詳細解答

field 是定義在 **class 裡、method 外** 的變數。

例如：

```java
class Test {
    int x; // field
}
```

如果寫在 method 裡：

```java
class Test {
    void f() {
        int x; // local variable
    }
}
```

那就不是 field，而是 local variable。

### 錯題釐清

你這題答對，但屬於**猜中**，還不算真正掌握。
你要牢記一個判斷法：

## 判斷 field 的標準

不是看它長得像不像 `int x;`
而是看它**寫在哪裡**

- class 裡、method 外 → field
- method 裡 → local variable

### 面試常問問題

**Q：field 跟 local variable 差在哪？**

### 詳細回答

差異至少有三個層面：

#### 1. 定義位置

- field：class 內、method 外
- local variable：method 內

#### 2. 所屬範圍

- field：屬於物件狀態
- local variable：屬於某次 method 執行過程

#### 3. 初始化規則

- field：有 default value
- local variable：沒有 default value，使用前要先賦值

---

## Q4

### 完整問題

下面哪個是正確建立物件的語法？

### 選項

A.

```java
ATypeName a = ATypeName();
```

B.

```java
ATypeName a = new ATypeName();
```

C.

```java
new ATypeName = a;
```

D.

```java
ATypeName = new a();
```

### 你的回答

B

### 正確答案

**B**

### 詳細解答

建立物件的標準語法是：

```java
ATypeName a = new ATypeName();
```

拆解如下：

- 左邊 `ATypeName a`：宣告一個 `ATypeName` 型別的 reference variable
- 右邊 `new ATypeName()`：建立一個新的 `ATypeName` 物件
- `=`：把 reference 指向那個物件

### 錯題釐清

你這題答對。
但你後面要更進一步理解：

`a` 不是整個物件本身，`a` 是**指向該物件的 reference**

### 面試常問問題

**Q：`new` 做了什麼？**

### 詳細回答

在你現在這個學習深度，先記這個 baseline：

- `new` 會建立新物件
- 建立完成後，會得到對該物件的 reference
- 這個 reference 可以存入變數中，供後續操作

---

## Q5

### 完整問題

下面程式輸出是什麼？

```java
class DataOnly {
    int i;
}

public class Test {
    public static void main(String[] args) {
        DataOnly data = new DataOnly();
        System.out.println(data.i);
    }
}
```

### 選項

A. 編譯錯誤
B. 0
C. null
D. 未定義值

### 你的回答

B
你補充：「class內建立的字段會有初始值」

### 正確答案

**B. 0**

### 詳細解答

`i` 是 `DataOnly` 這個 class 的 field，型別是 `int`。
Java 會為 field 提供 default value。

`int` 的 default value 是：

```java
0
```

所以輸出是 `0`。

### 錯題釐清

你這題答對，而且理由方向也正確。
但你要把用詞從「字段」固定成你現在學習系統裡比較穩定的詞：

- field（欄位 / 字段 / Field）

### 面試常問問題

**Q：Java 的 default value 有哪些？**

### 詳細回答

對 field 而言，常見 default value 包含：

- `boolean` → `false`
- `char` → `\u0000`
- `byte` → `(byte)0`
- `short` → `(short)0`
- `int` → `0`
- `long` → `0L`
- `float` → `0.0f`
- `double` → `0.0d`
- reference type → `null`

但這些 **只適用於 field**，不適用於 local variable。

---

## Q6

### 完整問題

下面程式是否合法？

```java
class Test {
    void f() {
        int x;
        System.out.println(x);
    }
}
```

### 選項

A. 合法
B. 不合法（未初始化）
C. 合法但輸出 0
D. 合法但輸出 null

### 你的回答

C

### 正確答案

**B. 不合法（未初始化）**

### 詳細解答

這裡的 `x` 是宣告在 method 裡：

```java
void f() {
    int x;
}
```

所以它是 **local variable**，不是 field。

Java 對 local variable 的規則是：

> 不會自動初始化
> 使用前一定要先賦值

所以：

```java
System.out.println(x);
```

會造成編譯錯誤。

### 錯題釐清

這題是你本輪最重要的錯題之一。

你把 **field 的規則** 錯套到 **local variable** 身上。

#### 你錯的地方

你以為只要是 `int`，Java 就會自動給它 `0`

#### 正確觀念

不是所有 `int` 都自動是 `0`
只有 **field** 才有 default value

#### 對照記憶

## 合法

```java
class Test {
    int x;
    void f() {
        System.out.println(x); // 0
    }
}
```

## 不合法

```java
class Test {
    void f() {
        int x;
        System.out.println(x); // 編譯錯誤
    }
}
```

### 面試常問問題

**Q：local variable 會有 default value 嗎？**

### 詳細回答

不會。

Java 只會對 field 提供 default value。
local variable 必須在使用前手動初始化，否則編譯器會報錯。

---

## Q7

### 完整問題

下面 method：

```java
int f() {
    return 5;
}
```

`return` 的作用是什麼？

### 選項

A. 結束 method
B. 回傳值給呼叫者
C. 宣告變數
D. 建立物件

### 你的回答

A、B、D

### 正確答案

**A、B**

### 詳細解答

`return` 有兩個核心作用：

### 1. 結束 method

執行到 `return` 時，這個 method 的流程就結束了。

### 2. 回傳值給呼叫者

如果 method 有回傳型別，例如這裡是 `int`，那 `return 5;` 代表把 `5` 傳回去。

### 為什麼 D 錯

建立物件的是 `new`，不是 `return`

例如：

```java
String s = new String("abc");
```

真正建立物件的是：

```java
new String("abc")
```

如果你寫：

```java
String f() {
    return new String("abc");
}
```

那也是：

- `new String("abc")` 建立物件
- `return` 把這個結果傳回去

### 錯題釐清

這題也是你的核心錯題。

#### 你錯的地方

把 `return` 的功能想成「也能建立物件」

#### 正確觀念

- `new`：建立物件
- `return`：離開 method，必要時把值傳回去

### 面試常問問題

**Q：`return` 一定只能寫在最後一行嗎？**

### 詳細回答

不一定。

`return` 可以出現在 method 中任何合法的位置，只要語法與控制流程正確即可。

例如：

```java
int f(int x) {
    if (x > 0) {
        return 1;
    }
    return -1;
}
```

這樣是合法的。

---

## Q8

### 完整問題

下面哪個 method 是合法？

### 選項

A.

```java
int f() {
}
```

B.

```java
void f() {
}
```

C.

```java
int f() {
    return;
}
```

D.

```java
void f() {
    return 5;
}
```

### 你的回答

B

### 正確答案

**B**

### 詳細解答

#### A 為什麼錯

```java
int f() {
}
```

`int f()` 表示這個 method 承諾要回傳 `int`，但 method body 裡沒有任何 return 值，所以不合法。

#### B 為什麼對

```java
void f() {
}
```

`void` 表示不回傳值，所以 method 可以直接結束，不需要回傳任何東西。

#### C 為什麼錯

```java
int f() {
    return;
}
```

`return;` 只表示離開 method，沒有回傳任何值。
但 `int f()` 需要回傳 `int`，所以不合法。

#### D 為什麼錯

```java
void f() {
    return 5;
}
```

`void` method 表示不能回傳值。
所以 `return 5;` 不合法。

### 錯題釐清

你這題答對。
但要順便把這個規則記死：

#### 規則 1

non-void method 一定要回傳對應型別的值

#### 規則 2

void method 不能回傳值

#### 規則 3

`return;` 只表示離開 method，不帶值

### 面試常問問題

**Q：`void` method 可以寫 `return;` 嗎？**

### 詳細回答

可以。

例如：

```java
void f() {
    return;
}
```

這是合法的。
這裡的 `return;` 只是提前結束 method，不會回傳任何值。

---

## Q9

### 完整問題

下面程式輸出什麼？

```java
class Test {

    int add(int a, int b) {
        return a + b;
    }

    public static void main(String[] args) {
        Test t = new Test();
        System.out.println(t.add(2, 3));
    }
}
```

### 你的回答

5

### 正確答案

**5**

### 詳細解答

當執行：

```java
t.add(2, 3)
```

代表呼叫 `add` method，並把：

- `2` 傳給 `a`
- `3` 傳給 `b`

method 裡計算：

```java
a + b
```

也就是 `2 + 3 = 5`

然後：

```java
return 5;
```

最後 `System.out.println(...)` 印出 `5`

### 錯題釐清

你這題答對。

### 面試常問問題

**Q：parameter 與 argument 差在哪？**

### 詳細回答

以這段為例：

```java
int add(int a, int b)
```

這裡的 `a`、`b` 是 **parameter**，因為它們出現在 method 定義裡。

而：

```java
t.add(2, 3)
```

這裡的 `2`、`3` 是 **argument**，因為它們是實際呼叫時傳進去的值。

---

## Q10

### 完整問題

下面哪個敘述正確？

### 選項

A. method 可以沒有參數
B. method 必須有參數
C. method 只能回傳 int
D. method 不能回傳值

### 你的回答

A

### 正確答案

**A. method 可以沒有參數**

### 詳細解答

Java method 可以有很多種形式：

## 沒參數

```java
void hello() {
    System.out.println("hi");
}
```

## 有參數

```java
int add(int a, int b) {
    return a + b;
}
```

## 有回傳值

```java
int f() {
    return 1;
}
```

## 沒回傳值

```java
void f() {
}
```

所以只有 A 正確。

### 錯題釐清

你這題答對。

### 面試常問問題

**Q：method signature 是什麼？**

### 詳細回答

在你目前這個層次，可以先這樣記：

method signature 主要是由：

- method name
- parameter list

共同決定。

例如：

```java
add(int a, int b)
```

這個 method name 是 `add`，參數列表是 `(int, int)`。
這會形成 method 的辨識特徵。

---

## Q11

### 完整問題

下面語法作用是什麼？

```java
data.i = 47;
```

### 你的回答

把data這個object的i這個變數被47賦值

### 正確答案

你的方向正確。

### 更精準的標準答案

> 將 `data` 所參考物件的 `i` 欄位賦值為 `47`

### 詳細解答

這行可以拆成：

- `data`：object reference
- `.`：member access
- `i`：該物件的 field
- `= 47`：把 47 指派給這個 field

所以這行的本質是：

> 透過 `data` 這個 reference，修改它所指向物件中的 `i`

### 錯題釐清

你方向對，但有兩個地方可以再修：

#### 1. `i` 最精準叫法是 field，不是泛稱變數

因為你現在這章就在分：

- field
- local variable

#### 2. 不是在改 `data` 本身

是在改 `data` 所指向物件裡的狀態

### 面試常問問題

**Q：`obj.field = value` 改的是 reference 還是 object？**

### 詳細回答

改的是 **object 的 field 狀態**，不是 reference 變數本身。

reference 只是用來找到那個 object。
真正被改的是 object 裡的欄位值。

---

## Q12

### 完整問題

下面語法作用是什麼？

```java
objectReference.member
```

代表什麼？

### 你的回答

某個存放真實物件Reference的變數中呼叫他的其中一個字段(member)

### 正確答案

你的方向接近，但表達需要修正。

### 更精準的標準答案

> `objectReference.member` 表示：透過一個物件參考去存取該物件的某個成員。這個 member 可以是 field，也可以是 method。

### 詳細解答

這個語法的核心是：

- `objectReference`：某個指向物件的 reference
- `.`：member access operator
- `member`：該物件的某個成員

例如：

## 存取 field

```java
data.i
```

## 呼叫 method

```java
obj.f()
```

所以 `member` 不只欄位，也可能是 method。

### 錯題釐清

你這題的主要問題不是方向錯，而是**不夠精準**。

#### 你需要修正的點

1. `member` 不一定只是字段 / 欄位
2. `objectReference` 是 reference variable，不是「真實物件本身」
3. `.` 的作用是透過 reference 存取 object 的 member

### 面試常問問題

**Q：Java 中 `.` 的作用是什麼？**

### 詳細回答

`.` 是 member access operator，用來透過某個 object reference 存取該物件的成員。

成員可以是：

- field
- method

例如：

```java
user.name
user.login()
```

本質上都屬於 member access。

---

# 最後整理：你的錯題總結

---

## 錯題 1：Q6

### 你選了

C. 合法但輸出 0

### 正解

B. 不合法（未初始化）

### 你真正要修正的核心

**local variable 不會自動初始化**

### 你要背熟的修正句

> field 有 default value，local variable 沒有。

---

## 錯題 2：Q7

### 你選了

A、B、D

### 正解

A、B

### 你真正要修正的核心

**`return` 不會建立物件，建立物件的是 `new`**

### 你要背熟的修正句

> `new` 負責建立物件，`return` 負責結束 method 並回傳值。

---

# 補充：本章最常被拿來考的面試題與完整回答

---

## 面試題 1

**class 與 object 差在哪？**

### 回答

class 是型別定義，是模板；object 是依照 class 建立出來的實例。
class 用來描述資料與行為，object 是實際被建立並可被操作的實體。

---

## 面試題 2

**field 與 local variable 差在哪？**

### 回答

差異主要有三個：

1. **位置不同**
   - field 在 class 裡、method 外
   - local variable 在 method 裡

2. **生命週期不同**
   - field 跟物件一起存在
   - local variable 只在 method 執行期間存在

3. **初始化規則不同**
   - field 有 default value
   - local variable 沒有，必須先賦值才能使用

---

## 面試題 3

**Java 的 default value 適用在哪些地方？**

### 回答

default value 適用於 class 的 field。
例如 `int` field 預設值是 `0`，`boolean` field 預設值是 `false`，reference field 預設值是 `null`。

但 local variable 不適用 default value，使用前必須先初始化。

---

## 面試題 4

**`return` 的作用是什麼？**

### 回答

`return` 有兩個主要作用：

1. 結束目前這個 method 的執行
2. 如果 method 有回傳型別，則把對應的值回傳給呼叫者

例如：

```java
int f() {
    return 5;
}
```

這會結束 `f()`，並把 `5` 回傳出去。

---

## 面試題 5

**`void` method 跟 `int` method 差在哪？**

### 回答

`void` method 不回傳值，所以可以直接結束，或寫 `return;` 提前離開。

`int` method 屬於 non-void method，必須回傳一個 `int` 值。
如果沒有回傳對應型別的值，就是不合法。

---

## 面試題 6

**parameter 與 argument 差在哪？**

### 回答

parameter 是 method 定義時用來接收資料的形式變數。
argument 是 method 呼叫時實際傳入的值。

例如：

```java
int add(int a, int b)
```

這裡 `a`、`b` 是 parameter。

```java
add(2, 3)
```

這裡 `2`、`3` 是 argument。

---

## 面試題 7

**`objectReference.member` 在做什麼？**

### 回答

它表示透過一個物件參考，去存取該物件的某個成員。
成員可以是 field，也可以是 method。

例如：

```java
data.i
obj.f()
```

都屬於 `objectReference.member` 的應用。

---

這版已經按你要求整理成完整格式了。
下一輪我可以直接接著做 **3.5–3.6 第二輪進階題組整理版**，格式會完全沿用這一版。
