第4章 Operators（4.9～4.17）講義

本段核心主題：

bit-level computation
shift semantics
expression evaluation rules
type promotion
casting behavior
string concatenation semantics
operator pitfalls

這是 execution model 與 memory model 的前置基礎。

一、位運算符（Bitwise Operators）

操作對象：

整數的二進位位元（bit）

包含：

運算符 名稱 規則
& AND 兩位都為1才是1
`	` OR
^ XOR 兩位不同才是1
~ NOT 反轉所有位

例：

int a = 5; // 0101
int b = 3; // 0011

a & b // 0001
a | b // 0111
a ^ b // 0110
~a
boolean 與位運算符

Java允許：

boolean x = true;
boolean y = false;

x & y
x | y
x ^ y

但：

~x

不可用。

差異：

& 不會 short-circuit
&& 會 short-circuit
二、移位運算符（Shift Operators）

只能作用於：

整數型別

包含：

運算符 名稱 行為
<< 左移 右側補0

> >     帶符號右移	保留符號位
> >
> > >     無符號右移	左側補0
> > >
> > > 範例
> > > int x = 8; // 00001000

x << 1 // 00010000
x >> 1 // 00000100

> > vs >>>

差異：

int x = -1;

二進位：

11111111111111111111111111111111

運算：

x >> 1

結果：

仍為負數

但：

x >>> 1

結果：

高位補0
變成正數
三、移位運算的型別提升規則

若操作：

byte
short
char

會先提升為：

int

例如：

byte b = 1;

b << 2

實際型別：

int

不是：

byte
四、>>> 搭配 byte / short 的陷阱

例如：

byte b = -1;

b >>>= 10;

行為：

先提升為 int
再右移
再截斷回 byte

結果：

錯誤值（通常仍為 -1）

原因：

重新指派時發生截斷
五、補碼（Two’s Complement）

Java 整數表示方式：

two’s complement

最高位：

值 意義
0 正
1 負

例如：

int -1

11111111111111111111111111111111
六、三元運算符（Ternary Operator）

語法：

condition ? value1 : value2

例：

int result = x < 10 ? 100 : 10;

等價：

if(x < 10)
result = 100;
else
result = 10;
三元運算符本質

它是：

expression

不是：

statement

所以可以：

return x < 10 ? 100 : 10;
使用原則

優點：

簡潔

缺點：

可讀性下降

建議：

只用於簡單條件
七、String 運算符（+）

Java允許：

String s = "Hello " + "World";

本質：

String concatenation

不是數值運算。

規則：只要出現 String

整段 expression 變成：

String expression

例：

System.out.println("x" + 1 + 2);

結果：

x12

但：

System.out.println(1 + 2 + "x");

結果：

3x

原因：

左到右 evaluation
強制轉 String 技巧

常見寫法：

"" + number

例如：

String s = "" + 10;

等價：

Integer.toString(10)
八、常見運算符陷阱（Operator Pitfalls）

C/C++ 常見錯誤：

while(x = y)

但 Java：

編譯錯誤

因為：

int 無法轉 boolean

例外：

boolean x = y;

合法。

另一常見錯誤：

&
vs
&&

Java 編譯器可避免錯誤型別使用。

九、型別轉換（Casting）

型別轉換語法：

(type)value

例如：

long l = (long)i;
自動轉型（Widening）

例如：

int → long

不需 cast：

long l = i;
強制轉型（Narrowing）

例如：

long → int

需要：

int x = (int)l;

原因：

可能資料遺失
十、截斷 vs 四捨五入

例：

(int)29.7

結果：

29

不是：

30

原因：

Java truncates decimal

若需要四捨五入：

Math.round(29.7)

結果：

30
十一、型別提升（Type Promotion）

規則：

若運算涉及：

byte
short
char

先轉：

int

例：

char a = 'A';
char b = 'B';

a + b

結果：

int

另一規則：

expression 結果型別：

由最大型別決定

例如：

int + long → long
float + double → double
十二、Java 沒有 sizeof

原因：

Java 保證：

primitive size platform-independent

例如：

型別 大小
int 32-bit
long 64-bit
float 32-bit
double 64-bit

跨平台一致。

十三、本段總結（4.9～4.17核心規則）

請記住這五條：

Rule 1

位運算：

直接操作 binary bit
Rule 2

shift 運算：

byte / short / char
先提升為 int
Rule 3

> > ：

保留符號位

> > > ：

高位補0
Rule 4

出現 String：

整段 expression → String
Rule 5

narrowing cast：

可能資料遺失
必須顯式轉型

如果你接下來要銜接 methods（參數傳遞 / stack frame / expression evaluation order），這一段的 type promotion + expression behavior 就會直接用到。

題目 1（String evaluation order）
System.out.println(1 + 2 + "3");

輸出？

A. 123
B. 33
C. 15
D. 編譯錯誤

題目 2（String evaluation order）
System.out.println("3" + 1 + 2);

輸出？

A. 33
B. 312
C. 123
D. 編譯錯誤

題目 3（casting truncation）
int x = (int) 5.9;
System.out.println(x);

輸出？

A. 6
B. 5
C. 編譯錯誤
D. Runtime error

題目 4（compound assignment）
byte b = 10;
b += 5;

結果？

A. 編譯錯誤
B. 合法，b = 15
C. Runtime error
D. b 變成 int

題目 5（type promotion）
byte a = 1;
byte b = 2;

byte c = a + b;

結果？

A. 編譯成功
B. 編譯錯誤
C. Runtime error
D. c = 3

題目 6（ternary operator）
int x = 8;

int y = (x > 5) ? 100 : 200;

y 是？

A. 5
B. 8
C. 100
D. 200

題目 7（ternary operator expression）

哪個合法？

A.

(x > 5) ? return 1 : return 2;

B.

return (x > 5) ? 1 : 2;

C.

(x > 5) ? break : continue;

D. 全部合法

題目 8（shift operator backend practical）
int x = 8;
System.out.println(x << 1);

輸出？

A. 4
B. 8
C. 16
D. 32

題目 9（sign-preserving shift）
int x = -8;
System.out.println(x >> 1);

結果？

A. 正數
B. 負數
C. 0
D. 編譯錯誤

題目 10（String coercion trick）

哪個可以把 int 轉成 String？

A.

"" + 10

B.

Integer.toString(10)

C.

String.valueOf(10)

D. 全部可以

B/B/B/D/B/C/C/不知道/D?

很好，這組題目你抓到大多數 實務會用到的 operator 規則。下面逐題精解（含你的答案、正確答案、工程語義）。

答題總覽

你的答案：

1.B ✅
2.B ✅
3.B ✅
4.D ❌
5.B ✅
6.C ✅
7.C ❌
8.C ✅ 9.不知道
請你回答包含完整題目、完整選項、我的回答、你的詳解

下面是 完整題目 + 完整選項 + 你的回答 + 正確答案 + 詳細解析 + 實務意義（是否 backend 常見） 的逐題講解版本。
只涵蓋 實戰會用到 / 面試常考 / 語義核心規則。

題目 1（String evaluation order）
題目
System.out.println(1 + 2 + "3");

輸出？

A. 123
B. 33
C. 15
D. 編譯錯誤

你的答案：

B

正確答案：

B
詳解

運算順序：

1 + 2 → 3
3 + "3" → "33"

規則：

只要 expression 中出現 String
之後全部變字串串接
實務意義（高頻）

常見於：

log.info("count=" + count);

面試常考：

expression evaluation order

題目 2（String evaluation order）
題目
System.out.println("3" + 1 + 2);

輸出？

A. 33
B. 312
C. 123
D. 編譯錯誤

你的答案：

B

正確答案：

B
詳解

運算順序：

"3" + 1 → "31"
"31" + 2 → "312"

規則：

Java expression 左到右 evaluation
實務意義（高頻）

避免錯誤：

System.out.println("total=" + a + b);

應寫：

System.out.println("total=" + (a + b));
題目 3（casting truncation）
題目
int x = (int) 5.9;
System.out.println(x);

輸出？

A. 6
B. 5
C. 編譯錯誤
D. Runtime error

你的答案：

B

正確答案：

B
詳解

Java casting：

截斷小數（truncate）
不是四捨五入

即：

(int)5.9 → 5
實務意義（高頻）

常見錯誤：

int percent = (int)(ratio \* 100);

必須知道：

precision 會消失

題目 4（compound assignment）
題目
byte b = 10;
b += 5;

結果？

A. 編譯錯誤
B. 合法，b = 15
C. Runtime error
D. b 變成 int

你的答案：

D

正確答案：

B
詳解

關鍵規則：

compound assignment 內建隱式 cast

實際等價：

b = (byte)(b + 5);

所以合法。

但：

b = b + 5;

才會編譯錯誤。

實務意義（高頻）

面試超常考：

+= vs =

Java type promotion 規則核心題

題目 5（type promotion）
題目
byte a = 1;
byte b = 2;

byte c = a + b;

結果？

A. 編譯成功
B. 編譯錯誤
C. Runtime error
D. c = 3

你的答案：

B

正確答案：

B
詳解

規則：

byte / short / char 運算時
先提升為 int

實際：

a + b → int

變成：

byte c = int

編譯錯誤

正確寫法：

byte c = (byte)(a + b);
實務意義（中頻）

常見於：

bit mask
network protocol parsing
binary IO

題目 6（ternary operator）
題目
int x = 8;

int y = (x > 5) ? 100 : 200;

y 是？

A. 5
B. 8
C. 100
D. 200

你的答案：

C

正確答案：

C
詳解

條件成立：

x > 5

因此：

y = 100
實務意義（高頻）

常見：

return success ? OK : ERROR;

Spring controller 很常出現

題目 7（ternary operator expression）
題目

哪個合法？

A.

(x > 5) ? return 1 : return 2;

B.

return (x > 5) ? 1 : 2;

C.

(x > 5) ? break : continue;

D. 全部合法

你的答案：

C

正確答案：

B
詳解

關鍵規則：

ternary operator 是 expression
不是 statement

因此：

合法：

return (x > 5) ? 1 : 2;

非法：

(x > 5) ? return 1 : return 2;

因為：

return 是 statement

同理：

(x > 5) ? break : continue;

也非法

實務意義（高頻）

常見：

return user == null ? null : user.getName();
題目 8（shift operator backend practical）
題目
int x = 8;
System.out.println(x << 1);

輸出？

A. 4
B. 8
C. 16
D. 32

你的答案：

C

正確答案：

C
詳解

規則：

左移一位 = 乘以 2

因此：

8 << 1 = 16
實務意義（低頻但面試常考）

常見於：

hash algorithm
bit mask
permission flag

題目 9（sign-preserving shift）
題目
int x = -8;
System.out.println(x >> 1);

結果？

A. 正數
B. 負數
C. 0
D. 編譯錯誤

你的答案：

不知道

正確答案：

B
詳解

規則：

> > 保留符號位

稱為：

sign-preserving shift

因此：

負數右移仍為負數
實務意義（中頻）

常見：

hash 計算
位元序列處理
壓縮演算法

題目 10（String coercion trick）
題目

哪個可以把 int 轉成 String？

A.

"" + 10

B.

Integer.toString(10)

C.

String.valueOf(10)

D. 全部可以

你的答案：

D

正確答案：

D
詳解

三種都合法：

方法 1
"" + 10

利用：

String concatenation coercion
方法 2
Integer.toString(10)

標準寫法

方法 3
String.valueOf(10)

最推薦（泛型友善）

實務意義（高頻）

Spring / logging 常見：

String.valueOf(id)

---

# 一、什麼是 expression（表達式）

定義：

```text
會產生一個值（value）的語法單位
```

例如：

```java
1 + 2
x > 5
a * b
(x > 5) ? 100 : 200
"Hello" + name
```

這些都會產生結果。

例如：

```java
int y = (x > 5) ? 100 : 200;
```

這裡：

```text
(x > 5) ? 100 : 200
```

就是 expression。

因為它產生：

```text
100 或 200
```

---

# 二、什麼是 statement（語句）

定義：

```text
執行一個動作，但不產生值
```

例如：

```java
return 5;
break;
continue;
if (...)
for (...)
while (...)
```

這些不是值。

它們只是控制流程。

---

# 三、為什麼 ternary operator 只能接 expression？

看這題（你剛剛錯的）：

錯誤：

```java
(x > 5) ? return 1 : return 2;
```

原因：

```text
return 是 statement
不是 expression
```

但這個合法：

```java
return (x > 5) ? 1 : 2;
```

因為：

```text
ternary operator 產生一個值
return 接收這個值
```

流程是：

```text
先算 expression
再 return
```

---

# 四、這個概念其實藏在你的講義哪裡？

在 4.9～4.17 裡面其實出現在這段語義：

```text
condition ? value1 : value2
```

注意：

```text
value1
value2
```

書不是寫：

```text
statement1
statement2
```

這已經暗示：

```text
ternary operator 只能放 expression
```

只是書沒有明說。

---

# 五、工程上最常見的 expression vs statement 差異場景

## 情境 1（return）

合法：

```java
return x > 5 ? 1 : 2;
```

非法：

```java
x > 5 ? return 1 : return 2;
```

---

## 情境 2（assignment）

合法：

```java
int y = x > 5 ? 1 : 2;
```

因為右側是 expression。

---

## 情境 3（method parameter）

合法：

```java
print(x > 5 ? 1 : 2);
```

因為：

```text
method argument 必須是 expression
```

---

# 六、面試為什麼會考這個？

因為這題其實在考：

```text
Java expression model
```

Spring / Stream API / Lambda 都依賴這個模型。

例如：

```java
Optional.of(user)
        .map(u -> u.isActive() ? u.getName() : "inactive");
```

這裡：

```text
u.isActive() ? u.getName() : "inactive"
```

一定要是 expression。

不能是：

```java
if (...) return ...
```

---

# 七、你現在需要記住的版本（夠用工程實務）

記這條就好：

```text
expression = 會產生值
statement = 不會產生值
```

而：

```text
ternary operator 只能接 expression
```
