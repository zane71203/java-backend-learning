第四章講義：運算子（Operators / 运算符）
一、這章在學什麼

運算子（Operator / 运算符）就是：

拿一個或多個值來運算，產生新值。

最常見的像是：

- 加法

* 減法

- 乘法
  / 除法
  = 指派（賦值）

這章不只是背符號，而是在建立三個核心觀念：

運算順序
值怎麼被改掉
基本型別和物件在運算時差很多
二、運算子的核心觀念

1. 運算子會產生值

例如：

int a = 3 + 4;

3 + 4 這個運算式會產生 7。

也就是說，運算子不是只有「做事」，它通常還會「回傳一個結果值」。

2. 有些運算子有副作用（Side Effect / 副作用）

副作用的意思是：

除了產生結果，還會改變原本操作對象的狀態。

例如：

int x = 5;
x++;

這不只是算出值，還真的把 x 改成了 6。

像 =, +=, ++, -- 這些都常有副作用。

3. 幾乎所有運算子都主要操作基本型別

原文特別提醒：

大多數運算子主要用在基本型別（primitive types / 基本类型）
例外是 =, ==, !=，它們也可操作物件
String 還支援 + 和 += 做字串串接
三、優先順序（Precedence / 优先级）

1. 為什麼要懂優先順序

當一個表達式裡有很多運算子時，Java 要決定先算誰、後算誰。

例如：

int a = x + y - 2 / 2 + z;

這裡不是從左到右傻傻全部照順序算，而是先算 /，再算 + 和 -。

2. 最基本規則

最重要的一條：

乘除先於加減

例如：

int result = 1 + 2 \* 3;

不是 (1 + 2) _ 3
而是 1 + (2 _ 3)
所以結果是 7。

3. 真正實務上怎麼做

雖然 Java 有優先順序規則，但原文很明確建議：

不要太依賴記憶，應該用括號把意圖寫清楚。

這是 production code 很重要的習慣。

例如：

int b = x + (y - 2) / (2 + z);

這種寫法可讀性高很多。

四、+ 不一定是加法

1. 在字串裡，+ 是串接

例如：

System.out.println("a = " + a);

這裡 + 不是數學加法，而是把字串和變數接在一起。

Java 會把右邊的非字串值自動轉成字串。

2. 這是初學者常混亂的地方

同樣一個 +：

在數字之間：是加法
在字串旁邊：常變成字串串接

例如：

System.out.println(1 + 2); // 3
System.out.println("1" + 2); // "12"
五、指派（Assignment / 赋值）

1. = 的本質

= 的意思不是「左右相等」，

而是：

把右邊的值，放進左邊的變數。

例如：

int a = 4;

意思是把 4 放進變數 a。

2. 左邊一定要是可存放值的變數

可以：

a = 4;

不可以：

4 = a;

因為 4 不是變數，沒有空間可放資料。

六、基本型別指派 vs 物件指派

這是本章最重要的面試高頻觀念之一。

1. 基本型別：複製的是值

例如：

int a = 5;
int b = a;
b = 10;

結果：

a 還是 5
b 是 10

因為基本型別指派時，複製的是「值本身」。

2. 物件：複製的是參考（reference / 引用）

例如原文的 Tank 範例：

Tank t1 = new Tank();
Tank t2 = new Tank();
t1 = t2;

這裡不是把 t2 那個物件整份複製給 t1。

而是：

把 t2 持有的參考，交給 t1。

結果就是：

t1
t2

都指向同一個 heap 裡的物件。
所以改 t1.level，t2.level 也會一起變。

3. 這種現象叫 aliasing（別名 / 别名）

意思是：

多個參考名稱，指向同一個物件。

這不是 Java 的 bug，而是 Java 物件運作的基本方式。

4. 原文一個很重要的提醒

如果你只寫：

t1 = t2;

t1 原本指向的物件就失去參考了，之後可能會被垃圾回收（GC）清掉。

七、方法呼叫中的 aliasing

這也是超高頻面試點。

原文 PassObject.java 的意思是：

static void f(Letter y) {
y.c = 'z';
}

如果你把 x 傳進去：

f(x);

y 不是建立一個全新的 Letter 物件，
而是接到和 x 指向同一個物件的參考。

所以在方法裡改：

y.c = 'z';

方法外的 x.c 也真的被改掉了。

這裡真正要記住的一句話

Java 傳遞的是值，但如果那個值本身是 reference，那效果就像你能透過它改到同一個物件。

對初學者來說，先記成：

把物件傳進方法時，方法裡可以改到外面的那個物件內容。

這樣先夠用。

八、算術運算子（Arithmetic Operators / 算术运算符）

包含：

-

*

- /
  %

1. / 整數除法要特別注意

例如：

int a = 5 / 2;

結果不是 2.5，而是 2。

因為：

整數除法會直接砍掉小數部分。

這是非常常考、也非常常寫錯的點。

2. % 是取餘數

例如：

int a = 5 % 2; // 1 3. 複合指定運算子

例如：

x += 4;

等價於：

x = x + 4;

其他像：

-=
\*=
/=
%=

也同理。

九、一元加減（Unary Plus/Minus）

1. 一元減號

例如：

x = -a;

代表取 a 的負值。

2. 一元加號

例如：

x = +a;

通常存在感很低，實務幾乎很少特別使用。

原文提到它主要影響是：
可能把較小數值型別提升成 int。

3. 可讀性建議

例如：

x = a \* -b;

雖然編譯器看得懂，但人容易看亂。

建議寫成：

x = a \* (-b);

這是很好的工程習慣。

十、遞增與遞減（++, --）

1. 基本意思
   ++a

約等於：

a = a + 1;

--a 同理。

2. 前綴 vs 後綴
   前綴：先改，再拿值
   ++i

流程：

i 先加 1
再把新值拿去用
後綴：先拿值，再改
i++

流程：

先把舊值拿去用
再把 i 加 1

3. 最容易搞混的例子
   int i = 1;
   System.out.println(++i); // 2
   System.out.println(i++); // 2
   System.out.println(i); // 3

為什麼第二行也是 2？

因為 i++ 是：

先輸出舊值 2
再把 i 變成 3 4. 面試高頻提醒

++i 和 i++ 單獨成行時效果看起來很像，
但一旦放進運算式或方法參數裡，差異就很重要。

十一、關係運算子（Relational Operators / 关系运算符）

包含：

<

> # <=
>
> ==
> !=

它們的結果都是：

boolean

也就是 true 或 false。

1. boolean 不能拿來比大小

例如：

true > false

這種是沒有意義的。
原文特別提到：

==、!= 可用於 boolean
但 <, > 這些不行
十二、== 比較物件時的陷阱

這段非常重要。

1. == 比較物件時，比的是 reference

也就是：

是不是同一個物件
不是「內容看起來有沒有一樣」

2. 為什麼 Integer n1 = 47; Integer n2 = 47; 會印出 true

原文已經附註：

這是因為 IntegerCache。
[-128, 127] 範圍常被快取，所以兩個變數可能剛好拿到同一個快取物件。

所以：

Integer n1 = 47;
Integer n2 = 47;
System.out.println(n1 == n2); // true

不是因為 == 在比內容。

而是因為這次剛好指到同一個快取物件。

3. 比內容要用 equals()

例如：

n1.equals(n2)

這才是在問：

它們的內容是否相等。

4. 但 equals() 也不是永遠自動比內容

原文的 Value 類別範例：

class Value {
int i;
}

如果你沒有 override equals()，

那預設 equals() 還是很可能在比 reference，
所以 v1.equals(v2) 仍然會是 false。

真正要記住的結論
對物件：
==：看是不是同一個參考
equals()：通常拿來比較內容，但前提是這個類別有正確覆寫它

這是 Java 初學者一定要站穩的觀念。

十三、邏輯運算子（Logical Operators / 逻辑运算符）

包含：

&& AND
|| OR
! NOT

結果也是 boolean。

1. Java 不能像 C/C++ 那樣把 int 當 boolean 用

例如這種在 Java 不合法：

if (5) { ... }

或者：

i && j

若 i 和 j 是 int，也不行。

Java 在這點上更嚴格：

邏輯運算一定要是 boolean。

2. 常見例子
   (i < 10) && (j < 10)
   (i < 10) || (j < 10)

先得到兩個 boolean，再做邏輯運算。

十四、短路（Short-Circuit / 短路）

這段非常重要，而且實務常用。

1. 什麼叫短路

當 Java 已經能確定整個條件式的結果時，
後面就不再算了。

2. && 的短路
   false && 任何東西

整體一定是 false，
所以右邊不用再算。

3. || 的短路
   true || 任何東西

整體一定是 true，
所以右邊不用再算。

4. 為什麼重要

不只是效能，還常拿來防錯。

例如：

if (obj != null && obj.isReady()) {
...
}

先確認 obj != null，
若是 false，後面 obj.isReady() 根本不會執行，
就能避免 NullPointerException。

雖然你貼的原文主要從運算流程講，但這是實務上你最該會用的地方。

十五、字面值常量（Literal / 字面值常量）

就是你直接寫在程式裡的值，例如：

123
0x2f
0b1010
1.5
200L
1.39e-43f

1. 進位表示法
   十六進位

前綴：

0x
0X

例如：

int i = 0x2f;
八進位

前導 0

例如：

int i = 0177;
二進位

前綴：

0b
0B

例如：

int i = 0b1010;

Java 7 開始支援。

2. 型別後綴
   long
   200L
   float
   1F
   1f
   double
   1D
   1d

原文也提醒：

l 不建議，因為很像數字 1
float 常需要明確加 f
double 常可省略後綴
十六、數字中的底線 \_

Java 7 可在數字中加 \_ 增加可讀性，例如：

int n = 1_000_000;
double d = 341_435_936.445_667;

這對大數字很好用。

使用規則

原文列得很清楚：

不能一開始就放 _
不能結尾放 _
不能連很多個 \_
不能貼著型別後綴 F D L
不能貼著 0x、0b 的 x、b 前後放

十七、printf() 的 %n

原文有一個很容易被忽略、但很實務的點：

在：

System.out.printf()
System.out.format()

裡，建議用：

%n

表示跨平台換行。

因為：

Unix 常是 \n
Windows 常是 \r\n

%n 讓 Java 幫你處理平台差異。

但原文也特別提醒：

println() 不是用 %n 的地方。
在 println() 裡寫 %n，它只會印出 %n 文字本身。

十八、科學記號 / 指數表示法

例如：

1.39e-43f
47e47

這裡的 e 在程式語言裡表示：

乘上 10 的某次方

不是自然常數 e = 2.718... 的那個意思。

例子
double expDouble = 47e47;

等於：

47 × 10^47
一個實務提醒

原文指出：

編譯器通常把這種指數表示法先當作 double。

所以如果你要存進 float，常要加 f：

float f4 = 1e-43f;

不然可能報型別錯誤。

十九、這章最該記住的 10 個重點

1.

運算子會產生值，有些還會帶副作用。

2.

乘除優先於加減，但實務上應多用括號。

3.

- 在字串旁邊常不是加法，而是串接。

4.

基本型別指派時，複製的是值。

5.

物件指派時，複製的是 reference，不是整個物件。

6.

把物件傳進方法時，方法內可能改到外面的物件內容。

7.

整數除法會直接砍掉小數。

8.

++i 和 i++ 差在「先改再用」還是「先用再改」。

9.

== 比較物件時通常是比 reference，不是比內容。

10.

Java 的 && 和 || 有短路行為。

二十、面試高頻區

這章最容易被問的不是全部，而是這幾個：

高頻 1：== 和 equals() 差異

一定要會。

高頻 2：基本型別指派 vs 物件指派

一定要會。

高頻 3：前置遞增 vs 後置遞增

很常考小陷阱題。

高頻 4：短路求值

實務與面試都很常見。

高頻 5：整數除法

超基礎，但常被寫錯。

二十一、你目前讀這章時，建議真正先站穩的順序

如果你不是要一次全背，建議先按這個順序理解：

第一層：一定先懂
優先順序
指派
基本型別 vs 物件
算術運算
++ / --
關係運算
邏輯運算
短路
第二層：再補
字面值
進位表示法
\_
指數表示法
第三層：觀念加深
aliasing
== vs equals()
物件傳參效果

如果你要，我下一步可以直接把這份講義再壓成你習慣的格式：

1. 章節筆記版
2. 面試問答版
3. 易錯觀念釐清版

以下是依據你剛剛貼的 Operators（運算子）整章內容設計的 10 題能力檢查題（Checkpoint Quiz）。
涵蓋範圍：

assignment（指派）
primitive vs reference
aliasing
parameter passing
++ / --
== vs equals()
logical operators
short-circuit
arithmetic behavior
printf("%n")

這組題目目標不是考記憶，而是確認你是否真的理解語義模型。

請直接回答：

A1:
A2:
...
A10:
Operators 章節能力檢查題（10 題）
Q1（assignment 基礎）

下面程式輸出什麼？

int a = 5;
int b = a;
b = 10;
System.out.println(a);

A. 5
B. 10
C. 編譯錯誤
D. 不一定

Q2（reference assignment / aliasing）

下面輸出什麼？

class Box {
int value;
}

Box a = new Box();
a.value = 10;

Box b = a;
b.value = 20;

System.out.println(a.value);

A. 10
B. 20
C. 編譯錯誤
D. 不一定

Q3（method parameter aliasing）

下面輸出什麼？

class Letter {
char c;
}

static void f(Letter y) {
y.c = 'z';
}

Letter x = new Letter();
x.c = 'a';

f(x);

System.out.println(x.c);

A. a
B. z
C. null
D. 編譯錯誤

Q4（method parameter reassignment）

下面輸出什麼？

class Letter {
char c;
}

static void f(Letter y) {
y = new Letter();
y.c = 'z';
}

Letter x = new Letter();
x.c = 'a';

f(x);

System.out.println(x.c);

A. a
B. z
C. null
D. 編譯錯誤

Q5（pre-increment vs post-increment）

下面輸出什麼？

int i = 5;
int j = i++;

System.out.println(i + " " + j);

A. 5 5
B. 6 6
C. 6 5
D. 5 6

Q6（another increment variant）

下面輸出什麼？

int i = 5;
int j = ++i;

System.out.println(i + " " + j);

A. 5 5
B. 6 6
C. 6 5
D. 5 6

Q7（integer division）

下面輸出什麼？

int a = 5 / 2;
System.out.println(a);

A. 2
B. 2.5
C. 3
D. 編譯錯誤

Q8（equals vs ==）

下面輸出什麼？

class Value {
int i;
}

Value v1 = new Value();
Value v2 = new Value();

v1.i = 100;
v2.i = 100;

System.out.println(v1.equals(v2));

A. true
B. false
C. 編譯錯誤
D. 不一定

Q9（short-circuit）

下面程式會發生什麼？

String s = null;

if (s != null && s.length() > 0) {
System.out.println("OK");
}

A. 印出 OK
B. NullPointerException
C. 不會印任何東西，也不會錯
D. 編譯錯誤

Q10（printf vs println）

下面輸出什麼？

System.out.println("%n");

A. 換行
B. %n
C. 空字串
D. 編譯錯誤

你回答後，我會逐題解析你的理解是否正確，以及哪幾題屬於「面試高頻錯誤區」。

1.A
2.B
3.B
4.B 原本的a會被棄用
5.A
6.D
7.A
8.B
9.C
10.B

請詳解(包含問題、選項、正確答案、詳細解答)

好，下面我照你的要求做成完整解析版。
我會保留：

完整問題
完整選項
你的答案
正確答案
詳細解答
錯題釐清

這 10 題都根據你剛剛貼的 Operators 章節觀念整理。

Operators 章節能力檢查詳解
Q1（assignment 基礎）
題目

下面程式輸出什麼？

int a = 5;
int b = a;
b = 10;
System.out.println(a);
選項

A. 5
B. 10
C. 編譯錯誤
D. 不一定

你的答案

A

正確答案

A. 5

詳細解答

這題在考：

基本型別（primitive type / 基本型別）指派時，複製的是值本身。

一開始：

int a = 5;

此時：

a = 5

接著：

int b = a;

這不是讓 b 和 a 綁在一起，
而是把 a 目前的值 5 複製一份給 b。

所以此時：

a = 5
b = 5

再來：

b = 10;

只是把 b 自己改成 10，
不會影響 a。

最後：

System.out.println(a);

輸出就是：

5
錯題釐清

你這題答對。
這題的核心觀念是：

基本型別：copy value
不是共用、不是綁定、不是連動
Q2（reference assignment / aliasing）
題目

下面輸出什麼？

class Box {
int value;
}

Box a = new Box();
a.value = 10;

Box b = a;
b.value = 20;

System.out.println(a.value);
選項

A. 10
B. 20
C. 編譯錯誤
D. 不一定

你的答案

B

正確答案

B. 20

詳細解答

這題在考：

物件變數指派時，複製的是 reference，不是整顆物件。

先看：

Box a = new Box();
a.value = 10;

這時候有一顆 Box 物件，裡面：

value = 10

然後：

Box b = a;

這不是建立第二顆 Box。
而是讓 b 也指向和 a 同一顆物件。

所以現在是：

a ──► Box物件(value=10)
b ──► 同一顆 Box物件(value=10)

接著：

b.value = 20;

是透過 b 去改那顆共用物件的 value。

因為 a 和 b 都指向同一顆物件，
所以最後：

System.out.println(a.value);

看到的也是 20。

錯題釐清

你這題答對。
這題就是 aliasing（別名） 的典型題。

Q3（method parameter aliasing）
題目

下面輸出什麼？

class Letter {
char c;
}

static void f(Letter y) {
y.c = 'z';
}

Letter x = new Letter();
x.c = 'a';

f(x);

System.out.println(x.c);
選項

A. a
B. z
C. null
D. 編譯錯誤

你的答案

B

正確答案

B. z

詳細解答

這題在考：

把物件傳進方法時，方法裡可以改到那顆物件的內容。

先看：

Letter x = new Letter();
x.c = 'a';

此時有一顆 Letter 物件：

x ──► Letter物件(c='a')

然後：

f(x);

Java 是 pass by value，但這裡傳進去的 value 是 reference value。
所以 y 會拿到和 x 一樣的參考值。

進到方法後：

x ──► Letter物件(c='a')
y ──► 同一顆 Letter物件(c='a')

執行：

y.c = 'z';

這是改那顆共用物件的內容。

所以方法結束後，外面的 x.c 也變成 'z'。

最後輸出：

z
錯題釐清

你這題答對。
這題是本章非常重要的觀念題。

Q4（method parameter reassignment）
題目

下面輸出什麼？

class Letter {
char c;
}

static void f(Letter y) {
y = new Letter();
y.c = 'z';
}

Letter x = new Letter();
x.c = 'a';

f(x);

System.out.println(x.c);
選項

A. a
B. z
C. null
D. 編譯錯誤

你的答案

B

正確答案

A. a

詳細解答

這題是 Q3 的進階版，也是你這次最關鍵的錯題之一。

第一步：外面先建立 x
Letter x = new Letter();
x.c = 'a';

現在：

x ──► Letter物件1(c='a')
第二步：呼叫方法
f(x);

這時方法裡的 y 一開始拿到的是 x 的參考值。

所以剛進方法時：

x ──► Letter物件1(c='a')
y ──► Letter物件1(c='a')
第三步：注意這行
y = new Letter();

這行非常關鍵。

它的意思不是改掉 x 指向的東西，
而是：

讓方法裡自己的區域變數 y，改去指向另一顆全新的物件。

所以此時變成：

x ──► Letter物件1(c='a')
y ──► Letter物件2(c=預設值)
第四步
y.c = 'z';

這改的是 Letter物件2

所以現在：

x ──► Letter物件1(c='a')
y ──► Letter物件2(c='z')
第五步：方法結束

方法結束後，y 這個區域變數消失。
外面的 x 從頭到尾都還是指向 Letter物件1

所以：

System.out.println(x.c);

輸出仍然是：

a
為什麼你會選 B？

你寫：

原本的 a 會被棄用

這裡你混了兩件事：

你以為：

方法裡 y = new Letter();
會把外面的 x 一起改掉。

但其實不會。

因為：

y 是方法裡自己的區域變數
改 y 指向哪裡，不會反向改掉外面的 x
這題真正的核心結論
可以改到外面物件內容：
y.c = 'z';
但不能靠改 y 自己，去改外面 x 指向哪裡：
y = new Letter();
錯題釐清

這題你錯。
你要牢記這句：

方法裡改「物件內容」可以影響外面；
方法裡改「參考變數自己指向哪裡」不會影響外面。

這題很重要。

Q5（pre-increment vs post-increment）
題目

下面輸出什麼？

int i = 5;
int j = i++;

System.out.println(i + " " + j);
選項

A. 5 5
B. 6 6
C. 6 5
D. 5 6

你的答案

A

正確答案

C. 6 5

詳細解答

這題考的是：

後置遞增 i++：先取舊值，再加 1

一開始：

i = 5

然後：

int j = i++;

流程不是「先變 6 再給 j」。

而是：

先拿 i 目前的舊值 5
把這個舊值給 j
然後 i 再自己加 1，變成 6

所以最後：

i = 6
j = 5

輸出：

6 5
錯題釐清

你選 A，代表你把 i++ 誤看成「不會改 i」。

這是錯的。

i++ 不是不加

而是：

先用舊值，再加一

Q6（another increment variant）
題目

下面輸出什麼？

int i = 5;
int j = ++i;

System.out.println(i + " " + j);
選項

A. 5 5
B. 6 6
C. 6 5
D. 5 6

你的答案

D

正確答案

B. 6 6

詳細解答

這題考的是：

前置遞增 ++i：先加 1，再取值

一開始：

i = 5

然後：

int j = ++i;

流程是：

先把 i 變成 6
再把這個新值 6 給 j

所以最後：

i = 6
j = 6

輸出：

6 6
錯題釐清

你選 D（5 6），這代表你把 ++i 想成：

j 拿到新值
但 i 還維持舊值

這不可能。

因為 ++i 的本質就是：

先把 i 自己改掉

所以 i 不可能還是 5。

Q7（integer division）
題目

下面輸出什麼？

int a = 5 / 2;
System.out.println(a);
選項

A. 2
B. 2.5
C. 3
D. 編譯錯誤

你的答案

A

正確答案

A. 2

詳細解答

這題在考：

整數除法會直接砍掉小數部分。

因為：

5 是 int
2 是 int

所以 5 / 2 做的是 int 除法

結果不是 2.5，而是：

2

小數直接被截掉。

錯題釐清

你這題答對。
這題是 Java 超基礎高頻題。

Q8（equals vs ==）
題目

下面輸出什麼？

class Value {
int i;
}

Value v1 = new Value();
Value v2 = new Value();

v1.i = 100;
v2.i = 100;

System.out.println(v1.equals(v2));
選項

A. true
B. false
C. 編譯錯誤
D. 不一定

你的答案

B

正確答案

B. false

詳細解答

這題在考：

如果你自己的類別沒有 override equals()，那預設 equals() 通常不是比內容。

這裡：

Value v1 = new Value();
Value v2 = new Value();

建立了兩顆不同的物件。

雖然後面：

v1.i = 100;
v2.i = 100;

看起來內容一樣，
但它們仍然是：

物件 A
物件 B

兩顆不同的物件。

因為 Value 類別沒有自己覆寫 equals()，
所以這裡呼叫的還是預設的 equals() 行為，效果接近比較：

v1 == v2

問的是：

是不是同一顆物件？

答案當然是：

false
錯題釐清

你這題答對。
但這題一定要記住：

==：看是不是同一顆
equals()：要看類別有沒有 override
Q9（short-circuit）
題目

下面程式會發生什麼？

String s = null;

if (s != null && s.length() > 0) {
System.out.println("OK");
}
選項

A. 印出 OK
B. NullPointerException
C. 不會印任何東西，也不會錯
D. 編譯錯誤

你的答案

C

正確答案

C. 不會印任何東西，也不會錯

詳細解答

這題在考：

&& 有 short-circuit（短路）行為

先看左邊：

s != null

因為 s 是 null，
所以這個結果是：

false

而 && 的規則是：

如果左邊已經是 false，整個結果一定是 false，右邊不用再算。

所以：

s.length() > 0

根本不會被執行。

也就不會發生 NullPointerException。

最後 if 裡不進去，所以：

不會印 OK
不會丟例外

答案就是 C。

錯題釐清

你這題答對。
這是 short-circuit 很經典的防呆寫法。

Q10（printf vs println）
題目

下面輸出什麼？

System.out.println("%n");
選項

A. 換行
B. %n
C. 空字串
D. 編譯錯誤

你的答案

B

正確答案

B. %n

詳細解答

這題在考：

%n 是 printf() / format() 的格式符號，不是 println() 的格式符號。

這裡你寫的是：

System.out.println("%n");

println() 不會去解析 %n 的特殊意義，
它只會把你給它的字串原封不動印出來，然後再自己換行。

所以輸出是：

%n

不是換行符本身。

對照一下
這個會把 %n 當格式符號：
System.out.printf("Hello%n");
這個只會把 %n 當普通字串：
System.out.println("%n");
錯題釐清

你這題答對。
這題很容易搞混，但你有抓到。

總結你的作答結果
你的答案
Q1 ✅
Q2 ✅
Q3 ✅
Q4 ❌
Q5 ❌
Q6 ❌
Q7 ✅
Q8 ✅
Q9 ✅
Q10 ✅
你目前真正的弱點

這次不是整章都不會，你的弱點很集中：

1. 方法參數中的 reference 行為

你對這句還沒有完全穩：

改物件內容 和 改參考變數自己 是兩件不同的事

這反映在 Q4。

2. ++i / i++ 的運算順序

你對「先取值還是先遞增」還不穩，這反映在：

Q5
Q6

這是目前最明確的缺口。

這 3 題你要重新硬記的結論
Q4 類型
y = new Letter();

只改方法裡 y 自己，不改外面的 x

Q5 類型
j = i++;
j 拿舊值
i 之後加一
Q6 類型
j = ++i;
i 先加一
j 拿新值
