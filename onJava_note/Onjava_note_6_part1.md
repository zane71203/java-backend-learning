---
整理方式：最小改寫版；保留原本章節與範例順序，不大幅重寫。
語言：台灣繁體中文；主要術語首次以「繁中（English / 简中）」標註。
---

# 術語對照表

| 台灣用語        | English                     | 简中              |
| --------------- | --------------------------- | ----------------- |
| 初始化          | Initialization              | 初始化            |
| 清理            | Cleanup                     | 清理              |
| 建構子          | Constructor                 | 构造器 / 构造函数 |
| 方法多載        | Method Overloading          | 方法重载          |
| 無參數建構子    | No-argument Constructor     | 无参构造器        |
| 參考            | Reference                   | 引用              |
| 物件            | Object                      | 对象              |
| 類別            | Class                       | 类                |
| 欄位 / 成員變數 | Field / Member Variable     | 字段 / 成员变量   |
| 區域變數        | Local Variable              | 局部变量          |
| 基本型別        | Primitive Type              | 基本类型          |
| 垃圾回收器      | Garbage Collector, GC       | 垃圾回收器        |
| 堆積區          | Heap                        | 堆                |
| 堆疊區          | Stack                       | 栈                |
| 靜態            | static                      | 静态              |
| 陣列            | Array                       | 数组              |
| 可變參數        | Variable Arguments, varargs | 可变参数          |
| 列舉型別        | enum                        | 枚举类型          |

---

# 第六章 初始化與清理（Initialization and Cleanup / 初始化与清理）

「不安全」的寫程式是造成寫程式代價昂貴的罪魁禍首之一。有兩個安全性問題：初始化與清理。C 語言中很多的 bug 都是因為程式設計師忘記初始化導致的。尤其是很多類別庫的使用者不知道如何初始化類別庫元件，甚至他們必須得去初始化。清理則是另一個特殊的問題，因為當你使用一個元素做完事後就不會去關心這個元素，所以你很容易忘記清理它。這樣就造成了元素使用的資源滯留不會被回收，直到程式消耗完所有的資源（特別是記憶體）。

C++ 引入了建構子的概念，這是一種特殊的方法，每建立一個物件，這個方法就會被自動呼叫。Java 採用了建構子的概念，另外還使用了垃圾回收器（Garbage Collector, GC）去自動回收不再被使用的物件所佔的資源。這一章將討論初始化與清理的問題，以及在 Java 中對它們的支援。

## 利用建構子（Constructor / 构造器）保證初始化

你可能想為每個類別建立一個 initialize() 方法，該方法名稱暗示著在使用類別之前需要先呼叫它。不幸的是，使用者必須得記得去呼叫它。在 Java 中，類別的設計者透過建構子保證每個物件的初始化。如果一個類別有建構子，那麼 Java 會在使用者使用物件之前（即物件剛建立完成）自動呼叫物件的建構子方法，從而保證初始化。下個挑戰是如何命名建構子方法。存在兩個問題：第一個是任何命名都可能與類別中其他已有元素的命名衝突；第二個是編譯器必須始終知道建構子方法名稱，從而呼叫它。C++ 的解決方法看起來是最簡單且最符合邏輯的，所以 Java 中使用了同樣的方式：建構子名稱與類別名稱相同。在初始化過程中自動呼叫建構子方法是有意義的。

以下範例是一個包含建構子的類別：

// housekeeping/SimpleConstructor.java
// Demonstration of a simple constructor

class Rock {
Rock() { // 這是一個建構子
System.out.print("Rock ");
}
}

public class SimpleConstructor {
public static void main(String[] args) {
for (int i = 0; i < 10; i++) {
new Rock();
}
}
}
**輸出：**

Rock Rock Rock Rock Rock Rock Rock Rock Rock Rock
現在，當建立一個物件時：new Rock() ，記憶體被配置，建構子被呼叫。建構子保證了物件在你使用它之前進行了正確的初始化。

有一點需要注意，建構子方法名稱與類別名稱相同，不需要符合第一個字母小寫的寫程式風格。在 C++ 中，無參數建構子（No-argument Constructor / 无参构造器）被稱為預設建構子，這個術語在 Java 出現之前使用了很多年。但是，出於一些原因，Java 設計者們決定使用無參數建構子（No-argument Constructor / 无参构造器）這個名稱，我（作者）認為這種叫法不自然而且沒有必要，所以我打算繼續使用預設建構子。Java 8 引入了 default 關鍵字修飾方法，所以我還是用無參數建構子（No-argument Constructor / 无参构造器）的叫法吧。

跟其他方法一樣，建構子方法也可以傳入參數來定義如何建立一個物件。前面的範例稍微修改，使得建構子接收一個參數：

// housekeeping/SimpleConstructor2.java
// Constructors can have arguments

class Rock2 {
Rock2(int i) {
System.out.print("Rock " + i + " ");
}
}

public class SimpleConstructor2 {
public static void main(String[] args) {
for (int i = 0; i < 8; i++) {
new Rock2(i);
}
}
}
**輸出：**

Rock 0 Rock 1 Rock 2 Rock 3 Rock 4 Rock 5 Rock 6 Rock 7
如果類別 Tree 有一個建構子，只接收一個參數用來表示樹的高度，那麼你可以像下面這樣建立一棵樹:

Tree t = new Tree(12); // 12-foot 樹
如果 Tree(int) 是唯一的建構子，那麼編譯器就不允許你以其他任何方式建立 Tree 型別的物件。

建構子消除了一類重要的問題，使得程式碼更易讀。例如，在上面的程式碼塊中，你看不到對 initialize() 方法的明確呼叫，而從概念上來看，initialize() 方法應該與物件的建立分離。在 Java 中，物件的建立與初始化是統一的概念，二者不可分割。

建構子沒有回傳值，它是一種特殊的方法。但它和回傳型別為 void 的一般方法不同，一般方法可以回傳空值，你還能選擇讓它回傳別的型別；而建構子沒有回傳值，卻同時也沒有給你選擇的余地（new 表達式虽然回傳了剛建立的物件的參考，但建構子本身卻沒有回傳任何值）。如果它有回傳值，並且你也可以自己選擇讓它回傳什么，那麼編譯器就还得知道接下來該怎麼處理那個回傳值（這個回傳值沒有接收者）。

## 方法多載（Method Overloading / 方法重载）

任何寫程式語言中都具備的一項重要特性就是命名。當你建立一個物件時，就會給此物件配置的記憶體空間命名。方法是行為的命名。你透過名字指代所有的物件，屬性和方法。良好命名的系统易於理解和修改。就好比寫散文——目的是與讀者沟通。

將人類語言细微的差別映射到寫程式語言中會產生一個問題。通常，相同的词可以表達多種不同的含義——它們被"多載"了。特別是當含義的差別很小時，這會更有用。你會說"清洗襯衫"、"清洗車"和"清洗狗"。而如果硬要這么說就會顯得很愚蠢："以洗襯衫的方式洗襯衫"、"以洗車的方式洗車"和"以洗狗的方式洗狗"，因為聽众根本不需要區分行為的動作。大多數人類語言都具有"冗余"性，所以即使漏掉几個词，你也能明白含義。你不需要對每個概念都使用不同的词彙——可以從上下文推斷出含義。

大多數寫程式語言（尤其是 C 語言）要求為每個方法（在這些語言中經常稱為函式）提供一個獨一无二的識別名稱。所以，你不能有一個 print() 函式既能印出整數型別，也能印出浮點型——每個函式名都必須不同。

在 Java (C++) 中，還有一個因素也促使了必須使用方法多載（Method Overloading / 方法重载）：建構子。因為建構子方法名稱肯定是與類別名稱相同，所以一個類別中只會有一個建構子名。那麼你怎麼透過不同的方式建立一個物件呢？例如，你想建立一個類別，這個類別的初始化方式有兩種：一種是標準方式，另一種是從檔案中讀取資訊的方式。你需要兩個建構子：無參數建構子（No-argument Constructor / 无参构造器）和有一個 String 型別參數的建構子，該參數傳入檔案名。兩個建構子具有相同的名字——與類別名稱相同。因此，方法多載（Method Overloading / 方法重载）是必要的，它允許方法具有相同的方法名稱但接收的參數不同。儘管方法多載（Method Overloading / 方法重载）對於建構子是重要的，但是也可以很方便地對其他任何方法進行多載。

下例展示了如何多載建構子和方法：

// housekeeping/Overloading.java
// Both constructor and ordinary method overloading

class Tree {
int height;
Tree() {
System.out.println("Planting a seedling");
height = 0;
}
Tree(int initialHeight) {
height = initialHeight;
System.out.println("Creating new Tree that is " + height + " feet tall");
}
void info() {
System.out.println("Tree is " + height + " feet tall");
}
void info(String s) {
System.out.println(s + ": Tree is " + height + " feet tall");
}
}
public class Overloading {
public static void main(String[] args) {
for (int i = 0; i < 5; i++) {
Tree t = new Tree(i);
t.info();
t.info("overloaded method");
}
new Tree();
}
}
**輸出：**

Creating new Tree that is 0 feet tall
Tree is 0 feet tall
overloaded method: Tree is 0 feet tall
Creating new Tree that is 1 feet tall
Tree is 1 feet tall
overloaded method: Tree is 1 feet tall
Creating new Tree that is 2 feet tall
Tree is 2 feet tall
overloaded method: Tree is 2 feet tall
Creating new Tree that is 3 feet tall
Tree is 3 feet tall
overloaded method: Tree is 3 feet tall
Creating new Tree that is 4 feet tall
Tree is 4 feet tall
overloaded method: Tree is 4 feet tall
Planting a seedling
一個 Tree 物件既可以是一棵樹苗，使用無參數建構子（No-argument Constructor / 无参构造器）建立，也可以是一棵在溫室中已长大的樹，已經有一定高度，這時候，就需要使用有參數建構子建立。

你也許想以多種方式呼叫 info() 方法。比如，如果你想印出額外的消息，就可以使用 info(String) 方法。如果你无话可說，就可以使用 info() 方法。用兩個命名定義完全相同的概念看起來很奇怪，而使用方法多載（Method Overloading / 方法重载），你就可以使用一個命名來定義一個概念。

## 區分多載方法

如果兩個方法命名相同，Java是怎麼知道你呼叫的是哪個呢？有一條簡單的规則：每個被多載的方法必須有獨一无二的參數清單。你稍微思考下，就會很明了了，除了透過參數清單的不同來區分兩個相同命名的方法，其他也没什么方式了。你甚至可以根據參數清單中的參數順序來區分不同的方法，儘管這會造成程式碼難以維護。例如：

// housekeeping/OverloadingOrder.java
// Overloading based on the order of the arguments

public class OverloadingOrder {
static void f(String s, int i) {
System.out.println("String: " + s + ", int: " + i);
}

    static void f(int i, String s) {
        System.out.println("int: " + i + ", String: " + s);
    }

    public static void main(String[] args) {
        f("String first", 1);
        f(99, "Int first");
    }

}
**輸出：**

String: String first, int: 1
int: 99, String: Int first
兩個 f() 方法具有相同的參數，但是參數順序不同，根據這個就可以區分它們。

## 多載與基本型別（Primitive Types / 基本类型）

基本型別可以自動從較小的型別轉型為較大的型別。當這與多載結合時，這會令人有点困惑，下面是一個這樣的例子：

// housekeeping/PrimitiveOverloading.java
// Promotion of primitives and overloading

public class PrimitiveOverloading {
void f1(char x) {
System.out.print("f1(char)");
}
void f1(byte x) {
System.out.print("f1(byte)");
}
void f1(short x) {
System.out.print("f1(short)");
}
void f1(int x) {
System.out.print("f1(int)");
}
void f1(long x) {
System.out.print("f1(long)");
}
void f1(float x) {
System.out.print("f1(float)");
}
void f1(double x) {
System.out.print("f1(double)");
}
void f2(byte x) {
System.out.print("f2(byte)");
}
void f2(short x) {
System.out.print("f2(short)");
}
void f2(int x) {
System.out.print("f2(int)");
}
void f2(long x) {
System.out.print("f2(long)");
}
void f2(float x) {
System.out.print("f2(float)");
}
void f2(double x) {
System.out.print("f2(double)");
}
void f3(short x) {
System.out.print("f3(short)");
}
void f3(int x) {
System.out.print("f3(int)");
}
void f3(long x) {
System.out.print("f3(long)");
}
void f3(float x) {
System.out.print("f3(float)");
}
void f3(double x) {
System.out.print("f3(double)");
}
void f4(int x) {
System.out.print("f4(int)");
}
void f4(long x) {
System.out.print("f4(long)");
}
void f4(float x) {
System.out.print("f4(float)");
}
void f4(double x) {
System.out.print("f4(double)");
}
void f5(long x) {
System.out.print("f5(long)");
}
void f5(float x) {
System.out.print("f5(float)");
}
void f5(double x) {
System.out.print("f5(double)");
}
void f6(float x) {
System.out.print("f6(float)");
}
void f6(double x) {
System.out.print("f6(double)");
}
void f7(double x) {
System.out.print("f7(double)");
}
void testConstVal() {
System.out.print("5: ");
f1(5);f2(5);f3(5);f4(5);f5(5);f6(5);f7(5);
System.out.println();
}
void testChar() {
char x = 'x';
System.out.print("char: ");
f1(x);f2(x);f3(x);f4(x);f5(x);f6(x);f7(x);
System.out.println();
}
void testByte() {
byte x = 0;
System.out.print("byte: ");
f1(x);f2(x);f3(x);f4(x);f5(x);f6(x);f7(x);
System.out.println();
}
void testShort() {
short x = 0;
System.out.print("short: ");
f1(x);f2(x);f3(x);f4(x);f5(x);f6(x);f7(x);
System.out.println();
}
void testInt() {
int x = 0;
System.out.print("int: ");
f1(x);f2(x);f3(x);f4(x);f5(x);f6(x);f7(x);
System.out.println();
}
void testLong() {
long x = 0;
System.out.print("long: ");
f1(x);f2(x);f3(x);f4(x);f5(x);f6(x);f7(x);
System.out.println();
}
void testFloat() {
float x = 0;
System.out.print("float: ");
f1(x);f2(x);f3(x);f4(x);f5(x);f6(x);f7(x);
System.out.println();
}
void testDouble() {
double x = 0;
System.out.print("double: ");
f1(x);f2(x);f3(x);f4(x);f5(x);f6(x);f7(x);
System.out.println();
}

    public static void main(String[] args) {
        PrimitiveOverloading p = new PrimitiveOverloading();
        p.testConstVal();
        p.testChar();
        p.testByte();
        p.testShort();
        p.testInt();
        p.testLong();
        p.testFloat();
        p.testDouble();
    }

}
**輸出：**

5: f1(int)f2(int)f3(int)f4(int)f5(long)f6(float)f7(double)
char: f1(char)f2(int)f3(int)f4(int)f5(long)f6(float)f7(double)
byte: f1(byte)f2(byte)f3(short)f4(int)f5(long)f6(float)f7(double)
short: f1(short)f2(short)f3(short)f4(int)f5(long)f6(float)f7(double)
int: f1(int)f2(int)f3(int)f4(int)f5(long)f6(float)f7(double)
long: f1(long)f2(long)f3(long)f4(long)f5(long)f6(float)f7(double)
float: f1(float)f2(float)f3(float)f4(float)f5(float)f6(float)f7(double)
double: f1(double)f2(double)f3(double)f4(double)f5(double)f6(double)f7(double)
如果傳入的參數型別大於方法期望接收的參數型別，你必須首先做向下轉型，如果你不做的话，編譯器就會報錯。

## 回傳值不能用來多載

經常會有人困惑，"為什麼只能透過方法名稱和參數清單，不能透過方法名稱和回傳值區分方法呢?"。例如以下兩個方法，它們有相同的命名和參數，但是很容易區分：

void f(){}
int f() {return 1;}
有些情況下，編譯器很容易就可以從上下文準確推斷出該呼叫哪個方法，如 int x = f()。

但是，你可以呼叫一個方法且忽略回傳值。這叫做呼叫一個函式的副作用，因為你不在乎回傳值，只是想利用方法做些事。所以如果你直接呼叫 f()，Java 編譯器就不知道你想呼叫哪個方法，阅讀者也不明所以。因為這個原因，所以你不能根據回傳值型別區分多載的方法。為了支援新特性，Java 8 在一些具体情況下提高了猜測的準確度，但是通常來說並不起作用。

## 無參數建構子（No-argument Constructor / 无参构造器）

如前文所說，一個無參數建構子（No-argument Constructor / 无参构造器）就是不接收參數的建構子，用來建立一個"預設物件"。如果你建立一個類別，類別中沒有建構子，那麼編譯器就會自動為你建立一個無參數建構子（No-argument Constructor / 无参构造器）。例如：

// housekeeping/DefaultConstructor.java
class Bird {}
public class DefaultConstructor {
public static void main(String[] args) {
Bird bird = new Bird(); // 預設的
}
}
表達式 new Bird() 建立了一個新物件，呼叫了無參數建構子（No-argument Constructor / 无参构造器），儘管在 Bird 類別中並沒有明確的定義無參數建構子（No-argument Constructor / 无参构造器）。想像一下如果沒有建構子，我們如何建立一個物件呢。但是,一旦你明確地定義了建構子（无論有參數還是無參數），編譯器就不會自動為你建立無參數建構子（No-argument Constructor / 无参构造器）。如下：

// housekeeping/NoSynthesis.java
class Bird2 {
Bird2(int i) {}
Bird2(double d) {}
}
public class NoSynthesis {
public static void main(String[] args) {
//- Bird2 b = new Bird2(); // No default
Bird2 b2 = new Bird2(1);
Bird2 b3 = new Bird2(1.0);
}
}
如果你呼叫了 new Bird2() ，編譯器會提示找不到符合的建構子。當類別中沒有建構子時，編譯器會說"你一定需要建構子，那麼讓我為你建立一個吧"。但是如果類別中有建構子，編譯器會說"你已經寫了建構子了，所以肯定知道你在做什么，如果你沒有建立預設建構子，說明你本來就不需要"。

## this 關鍵字（this Keyword / this关键字）

對於兩個相同型別的物件 a 和 b，你可能在想如何呼叫這兩個物件的 peel() 方法：

// housekeeping/BananaPeel.java

class Banana {
void peel(int i) {
/_..._/
}
}
public class BananaPeel {
public static void main(String[] args) {
Banana a = new Banana(), b = new Banana();
a.peel(1);
b.peel(2);
}
}
如果只有一個方法 peel() ，那麼怎麼知道呼叫的是物件 a 的 peel()方法還是物件 b 的 peel() 方法呢？編譯器做了一些底層工作，所以你可以像這樣編寫程式碼。peel() 方法中第一個參數隱含地傳入了一個指向操作物件的

參考。因此，上述例子中的方法呼叫像下面這樣：

Banana.peel(a, 1)
Banana.peel(b, 2)
這是在内部實現的，你不可以直接這么編寫程式碼，編譯器不會接收，但能說明到底發生了什么。假設現在在方法内部，你想獲得對目前物件的參考。但是，物件參考是被隱含地傳達給編譯器——並不在參數清單中。方便的是，有一個關鍵字: this 。this 關鍵字只能在非靜態方法内部使用。當你呼叫一個物件的方法時，this 生成了一個物件參考。你可以像對待其他參考一樣對待這個參考。如果你在一個類別的方法里呼叫該類別的其他方法，不要使用 this，直接呼叫即可，this 自動地應用於其他方法上了。因此你可以像這樣：

// housekeeping/Apricot.java

public class Apricot {
void pick() {
/_ ... _/
}

    void pit() {
        pick();
        /* ... */
    }

}
在 pit() 方法中，你可以使用 this.pick()，但是沒有必要。編譯器自動為你做了這些。this 關鍵字只用在一些必須明確使用目前物件參考的特殊場合。例如，用在 return 敘述中回傳對目前物件的參考。

// housekeeping/Leaf.java
// Simple use of the "this" keyword

public class Leaf {

    int i = 0;

    Leaf increment() {
        i++;
        return this;
    }

    void print() {
        System.out.println("i = " + i);
    }

    public static void main(String[] args) {
        Leaf x = new Leaf();
        x.increment().increment().increment().print();
    }

}
**輸出：**

i = 3
因為 increment() 透過 this 關鍵字回傳目前物件的參考，因此在相同的物件上可以輕易地執行多次操作。

this 關鍵字在向其他方法傳遞目前物件時也很有用：

// housekeeping/PassingThis.java

class Person {
public void eat(Apple apple) {
Apple peeled = apple.getPeeled();
System.out.println("Yummy");
}
}

public class Peeler {
static Apple peel(Apple apple) {
// ... remove peel
return apple; // Peeled
}
}

public class Apple {
Apple getPeeled() {
return Peeler.peel(this);
}
}

public class PassingThis {
public static void main(String[] args) {
new Person().eat(new Apple());
}
}
**輸出：**

Yummy
Apple 因為某些原因（比如說工具類別中的方法在多個類別中重複出現，你不想程式碼重複），必須呼叫一個外部工具方法 Peeler.peel() 做一些行為。必須使用 this 才能將自身傳遞給外部方法。

## 在建構子中呼叫另一個建構子

當你在一個類別中寫了多個建構子，有時你想在一個建構子中呼叫另一個建構子來避免程式碼重複。你透過 this 關鍵字實現這樣的呼叫。

通常當你說 this，代表"這個物件"或"目前物件"，它本身生成對目前物件的參考。在一個建構子中，當你給 this 一個參數清單時，它是另一層意思。它透過最直接的方式明確地呼叫符合參數清單的建構子：

// housekeeping/Flower.java
// Calling constructors with "this"

public class Flower {
int petalCount = 0;
String s = "initial value";

    Flower(int petals) {
        petalCount = petals;
        System.out.println("Constructor w/ int arg only, petalCount = " + petalCount);
    }

    Flower(String ss) {
        System.out.println("Constructor w/ string arg only, s = " + ss);
        s = ss;
    }

    Flower(String s, int petals) {
        this(petals);
        //- this(s); // Can't call two!
        this.s = s; // Another use of "this"
        System.out.println("String & int args");
    }

    Flower() {
        this("hi", 47);
        System.out.println("no-arg constructor");
    }

    void printPetalCount() {
        //- this(11); // Not inside constructor!
        System.out.println("petalCount = " + petalCount + " s = " + s);
    }

    public static void main(String[] args) {
        Flower x = new Flower();
        x.printPetalCount();
    }

}
**輸出：**

Constructor w/ int arg only, petalCount = 47
String & int args
no-arg constructor
petalCount = 47 s = hi
從建構子 Flower(String s, int petals) 可以看出，其中只能透過 this 呼叫一次建構子。另外，必須首先呼叫建構子，否則編譯器會報錯。這個例子同樣展示了 this 的另一個用法。參數清單中的變數名 s 和成員變數名 s 相同，會引起混淆。你可以透過 this.s 表示你指的是成員變數 s，從而避免重複。你經常會在 Java 程式碼中看到這種用法，同時本书中也會多次出現這種寫法。在 printPetalCount() 方法中，編譯器不允許你在一個建構子之外的方法里呼叫建構子。

## static 的意思（static Keyword / static关键字）

記住了 this 關鍵字的內容，你會對 static 修飾的方法有更深入的理解：static 方法中不會存在 this。你不能在靜態方法中呼叫非靜態方法（反之可以）。靜態方法是為類而建立的，不需要任何物件。事實上，這就是靜態方法的主要目的，靜態方法看起來就像全域方法一樣，但是 Java 中不允許全域方法，一個類別中的靜態方法可以访問其他靜態方法和靜態屬性。一些人認為靜態方法不是面向物件的，因為它們的確具有全域方法的語義。使用靜態方法，因為不存在 this，所以你沒有向一個物件发送消息。的確，如果你發現程式碼中出現了大量的 static 方法，就該重新考慮自己的設計了。不過，static 的概念很實用，許多時候都要用到它。至於它是否真的"面向物件"，就留給理論家去討論吧。

## 垃圾回收器（Garbage Collector, GC / 垃圾回收器）

程式設計師都了解初始化的重要性，但通常會忽略清理的重要性。畢竟，谁會去清理一個 int 呢？但是使用完一個物件就不管它並非總是安全的。Java 中有垃圾回收器（Garbage Collector, GC / 垃圾回收器）回收无用物件佔用的記憶體。但現在考慮一種特殊情況：你建立的物件不是透過 new 來配置記憶體的，而垃圾回收器（Garbage Collector, GC / 垃圾回收器）只知道如何釋放用 new 建立的物件的記憶體，所以它不知道如何回收不是 new 配置的記憶體。為了處理這種情況，Java 允許在類別中定義一個名為 finalize() 的方法。

它的工作原理"假定"是這樣的：當垃圾回收器（Garbage Collector, GC / 垃圾回收器）準備回收物件的記憶體時，首先會呼叫其 finalize() 方法，並在下一轮的垃圾回收動作發生時，才會真正回收物件佔用的記憶體。所以如果你打算使用 finalize() ，就能在垃圾回收時做一些重要的清理工作。finalize() 是一個潛在的寫程式陷阱，因為一些程式設計師（尤其是 C++ 程式設計師）會一開始把它誤認為是 C++ 中的析構函式（C++ 在銷毀物件時會呼叫這個函式）。所以有必要明確區分一下：在 C++ 中，物件總是被銷毀的（在一個 bug-free 的程式中），而在 Java 中，物件並非總是被垃圾回收，或是換句话說：

物件可能不被垃圾回收。
垃圾回收不等同於析構。
這代表在你不再需要某個物件之前，如果必須執行某些動作，你得自己去做。Java 沒有解構器或類似的概念，所以你必須得自己建立一個一般的方法完成這項清理工作。例如，物件在建立的過程中會將自己繪制到螢幕上。如果不是明確地從螢幕上將其擦除，它可能永遠得不到清理。如果在 finalize() 方法中加入某種擦除功能，那麼當垃圾回收發生時，finalize() 方法被呼叫（不保證一定會發生），圖像就會被擦除，要是"垃圾回收"沒有發生，圖像則仍會保留下來。

也許你會發現，只要程式沒有濒臨記憶體用完的那一刻，物件佔用的空間就總也得不到釋放。如果程式執行結束，而垃圾回收器（Garbage Collector, GC / 垃圾回收器）一直沒有釋放你建立的任何物件的記憶體，則當程式結束時，那些資源會全部交还給作業系統。這個策略是適當的，因為垃圾回收本身也有成本，要是不使用它，那就不用支付這部分成本了。

## finalize() 的用途

如果你不能將 finalize() 作為通用的清理方法，那麼這個方法有什么用呢？

這引入了要記住的第3点：

垃圾回收只與記憶體有關。
也就是說，使用垃圾回收的唯一原因就是為了回收程式不再使用的記憶體。所以對於與垃圾回收有關的任何行為來說（尤其是 finalize() 方法），它們也必須同記憶體及其回收有關。

但這是否代表如果物件中包含其他物件，finalize() 方法就應該明確釋放那些物件呢？不是，无論物件是如何建立的，垃圾回收器（Garbage Collector, GC / 垃圾回收器）都會負責釋放物件所佔用的所有記憶體。這就將對 finalize() 的需求限制到一種特殊情況，即透過某種建立物件方式之外的方式為物件配置了儲存空間。不過，你可能會想，Java 中萬物皆物件，這種情況怎麼可能發生？

看起來之所以有 finalize() 方法，是因為在配置記憶體時可能採用了類似 C 語言中的做法，而非 Java 中的通常做法。這種情況主要發生在使用"原生方法"的情況下，原生方法是一種用 Java 語言呼叫非 Java 語言程式碼的形式（關於原生方法的討論，见本书电子版第2版的附錄B）。原生方法目前只支援 C 和 C++，但是它們可以呼叫其他語言寫的程式碼，所以實際上可以呼叫任何程式碼。在非 Java 程式碼中，也許會呼叫 C 的 malloc() 函式系列來配置儲存空間，而且除非呼叫 free() 函式，否則儲存空間永遠得不到釋放，造成記憶體洩漏。但是，free() 是 C 和 C++ 中的函式，所以你需要在 finalize() 方法里用原生方法呼叫它。

讀到這裡，你可能明白了不會過多使用 finalize() 方法。對，它確實不是進行一般的清理工作的合适场所。那麼，一般的清理工作在哪裡執行呢？

## 你必須自己執行清理

要清理一個物件，使用者必須在需要清理的時候呼叫執行清理動作的方法。這聽上去相當直接，但卻與 C++ 中的"析構函式"的概念稍有抵觸。在 C++ 中，所有物件都會被銷毀，或是說應該被銷毀。如果在 C++ 中建立了一個區域物件（在堆疊區（stack）上建立，在 Java 中不行），此時的銷毀動作發生在以"右大括號"為邊界的、此物件作用欄位的結尾處。如果物件是用 new 建立的（類似於 Java 中），那麼當程式設計師呼叫 C++ 的 delete 運算子時（Java 中不存在），就會呼叫相應的析構函式。如果程式設計師忘記呼叫 delete，那麼永遠不會呼叫析構函式，這樣就會導致記憶體洩漏，物件的其他部分也不會得到清理。這種 bug 很難跟蹤，也是讓 C++ 程式設計師转向 Java 的一個主要因素。相反，在 Java 中，沒有用於釋放物件的 delete，因為垃圾回收器（Garbage Collector, GC / 垃圾回收器）會幫助你釋放儲存空間。甚至可以粗略地認為，正是由於垃圾回收的存在，使得 Java 沒有析構函式。不過，隨著學習的深入，你會明白垃圾回收器（Garbage Collector, GC / 垃圾回收器）的存在並不能完全取代析構函式（而且绝對不能直接呼叫 finalize()，所以這也不是一種解決方案）。如果希望進行除釋放儲存空間之外的清理工作，還是得明確呼叫某個適當的 Java 方法：這就等同於使用析構函式了，只是沒有它方便。

記住，无論是"垃圾回收"還是"終結"，都不保證一定會發生。如果 Java 虚擬机（JVM）並未面臨記憶體耗盡的情況，它可能不會浪費時間執行垃圾回收以恢複記憶體。

## 終結條件

通常，不能期待 finalize() ，你必須建立其他的"清理"方法，並明確地呼叫它們。所以看起來，finalize() 只對大部分程式設計師很難用到的一些晦涩記憶體清理里有用了。但是，finalize() 還有一個有趣的用法，它不依賴於每次都要對 finalize() 進行呼叫，這就是物件終結條件的驗證。

當對某個物件不再關心時——也就是它將被清理了，這個物件應該處於某種状態，這種状態下它佔用的記憶體可以被安全地釋放掉。例如，如果物件代表了一個打開的檔案，在物件被垃圾回收之前程式設計師應該關閉這個檔案。只要物件中存在沒有被适當清理的部分，程式就存在很隱晦的 bug。finalize() 可以用來最終發現這個情況，儘管它並不總是被呼叫。如果某次 finalize() 的動作使得 bug 被發現，那麼就可以據此找出問題所在——這才是人們真正關心的。以下是個簡單的例子，示範了 finalize() 的可能使用方式：

// housekeeping/TerminationCondition.java
// Using finalize() to detect a object that
// hasn't been properly cleaned up

import onjava.\*;

class Book {
boolean checkedOut = false;

    Book(boolean checkOut) {
        checkedOut = checkOut;
    }

    void checkIn() {
        checkedOut = false;
    }

    @Override
    protected void finalize() throws Throwable {
        if (checkedOut) {
            System.out.println("Error: checked out");
        }
        // Normally, you'll also do this:
        // super.finalize(); // Call the base-class version
    }

}

public class TerminationCondition {

    public static void main(String[] args) {
        Book novel = new Book(true);
        // Proper cleanup:
        novel.checkIn();
        // Drop the reference, forget to clean up:
        new Book(true);
        // Force garbage collection & finalization:
        System.gc();
        new Nap(1); // One second delay
    }

}
**輸出：**

Error: checked out
本例的終結條件是：所有的 Book 物件在被垃圾回收之前必須被登記。但在 main() 方法中，有一本书沒有登記。要是沒有 finalize() 方法來驗證終結條件，將會很難發現這個 bug。

你可能注意到使用了 @Override。@ 代表這是一個註解，註解是關於程式碼的額外資訊。在這裡，該註解告訴編譯器這不是偶然地重定義在每個物件中都存在的 finalize() 方法——程式設計師知道自己在做什么。編譯器確保你沒有拼錯方法名稱，而且確保那個方法存在於基類別中。註解也是對讀者的提醒，@Override 在 Java 5 引入，在 Java 7 中改善，本书全文會出現。

注意，System.gc() 用於強制進行終結動作。但是即使不這么做，只要重複地執行程式（假設程式將配置大量的儲存空間而導致垃圾回收動作的執行），最終也能找出錯誤的 Book 物件。

你應該總是假設基底類別版本的 finalize() 也要做一些重要的事情，使用 super 呼叫它，就像在 Book.finalize() 中看到的那樣。本例中，它被注釋掉了，因為它需要進行例外處理，而我們到現在还沒有涉及到。

## 垃圾回收器（Garbage Collector, GC / 垃圾回收器）如何工作

如果你以前用過的語言，在堆積區（heap）上配置物件的代价十分高昂，你可能自然會覺得 Java 中所有物件（基本型別除外）在堆積區（heap）上配置的方式也十分高昂。不過，垃圾回收器（Garbage Collector, GC / 垃圾回收器）能很明顯地提高物件的建立速度。這聽起來很奇怪——儲存空間的釋放影響了儲存空間的配置，但這確實是某些 Java 虚擬机的工作方式。這也代表，Java 從堆積區空間配置的速度可以和其他語言在堆疊區（stack）上配置空間的速度相相當。

例如，你可以把 C++ 里的堆想像成一個院子，裡面每個物件都負責管理自己的地盘。一段時間後，物件可能被銷毀，但地盘必須複用。在某些 Java 虚擬机中，堆的實現完全不同：它更像一個傳送帶，每配置一個新物件，它就向前移動一格。這代表物件儲存空間的配置速度特別快。Java 的"堆積區指標"只是簡單地移動到尚未配置的區欄位，所以它的效率與 C++ 在堆疊區（stack）上配置空間的效率相當。當然實際過程中，在記帳工作方面還有少量額外成本，但是這部分成本比不上尋找可用空間成本大。

你可能意識到了，Java 中的堆並非完全像傳送帶那樣工作。要是那樣的话，势必會導致頻繁的記憶體分頁調度——將其移進移出硬碟，因此會顯得需要擁有比實際需要更多的記憶體。分頁調度會顯著影響效能。最終，在建立了足夠多的物件後，記憶體資源被耗盡。其中的關鍵在於垃圾回收器（Garbage Collector, GC / 垃圾回收器）的介入。當它工作時，一边回收記憶體，一边使堆中的物件緊密排列，這樣"堆積區指標"就可以很容易地移動到更靠近傳送帶的開始處，也就盡量避免了頁面錯誤。垃圾回收器（Garbage Collector, GC / 垃圾回收器）透過重新排列物件，實現了一種高速的、有无限空間可配置的堆模型。

要想理解 Java 中的垃圾回收，先了解其他系统中的垃圾回收機制將會很有幫助。一種簡單但速度很慢的垃圾回收機制叫做參考計數。每個物件中含有一個參考計數器，每當有參考指向該物件時，參考計數加 1。當參考離開作用欄位或被置為 null 時，參考計數減 1。因此，管理參考計數是一個成本不大但是在程式的整個生命週期頻繁發生的負擔。垃圾回收器（Garbage Collector, GC / 垃圾回收器）會走訪含有全部物件的清單，當發現某個物件的參考計數為 0 時，就釋放其佔用的空間（但是，參考計數模式經常會在計數為 0 時立即釋放物件）。這個機制存在一個缺點：如果物件之間存在循環參考，那麼它們的參考計數都不為 0，就會出現應該被回收但無法被回收的情況。對垃圾回收器（Garbage Collector, GC / 垃圾回收器）而言，定位這樣的循環參考所需的工作量極大。參考計數常用來說明垃圾回收的工作方式，但似乎從未被應用於任何一種 Java 虚擬机實現中。

在更快的策略中，垃圾回收器（Garbage Collector, GC / 垃圾回收器）並非基於參考計數。它們依據的是：對於任意"活"的物件，一定能最終追溯到其存在在栈或靜態存儲區中的參考。這個參考鏈條可能會穿過數個物件層次，由此，如果從栈或靜態存儲區出發，走訪所有的參考，你將會發現所有"活"的物件。對於發現的每個參考，必須追蹤它所參考的物件，然後是該物件包含的所有參考，如此反複進行，直到访問完"根源於栈或靜態存儲區的參考"所形成的整個網路。你所访問過的物件一定是"活"的。注意，這解決了物件間循環參考的問題，這些物件不會被發現，因此也就被自動回收了。

在這種方式下，Java 虚擬机採用了一種自适應的垃圾回收技術。至於如何處理找到的存在物件，取決於不同的 Java 虚擬机實現。其中有一種做法叫做停止-複製（stop-and-copy）。顾名思義，這需要先暫停程式的執行（不屬於背景回收模式），然後將所有存在的物件從目前堆複製到另一個堆，沒有複製的就是需要被垃圾回收的。另外，當物件被複製到新堆時，它們是一個挨著一個緊密排列，然後就可以按照前面描述的那樣簡單、直接地配置新空間了。

當物件從一處複製到另一處，所有指向它的參考都必須修正。位於栈或靜態存儲區的參考可以直接被修正，但可能還有其他指向這些物件的參考，它們在走訪的過程中才能被找到（可以想像成一個表格，將旧位址映射到新位址）。

這種所谓的"複製回收器"效率較低主要因為兩個原因。第一：得有兩個堆，然後在這兩個分離的堆之間來回折腾，得維護比實際需要多一倍的空間。某些 Java 虚擬机對此問題的處理方式是，依需求從堆中配置几塊較大的記憶體，複製動作發生在這些大塊記憶體之間。

第二在於複製本身。一旦程式進入穩定状態之後，可能只會產生少量垃圾，甚至沒有垃圾。儘管如此，複製回收器仍然會將所有記憶體從一處複製到另一處，這很浪費。為了避免這種状况，一些 Java 虚擬机會進行檢查：要是沒有新垃圾產生，就會轉換到另一種模式（即"自适應"）。這種模式稱為標記－清除（mark-and-sweep），Sun 公司早期版本的 Java 虚擬机一直使用這種技術。對一般用途而言，"標記－清除"方式速度相當慢，但是當你知道程式只會產生少量垃圾甚至不產生垃圾時，它的速度就很快了。

"標記－清除"所依據的想法仍然是從栈和靜態存儲區出發，走訪所有的參考，找出所有存在的物件。但是，每當找到一個存在物件，就給物件設一個標記，並不回收它。只有當標記過程完成後，清理動作才開始。在清理過程中，沒有標記的物件將被釋放，不會發生任何複製動作。"標記－清除"後剩下的堆積區空間是不连續的，垃圾回收器（Garbage Collector, GC / 垃圾回收器）要是希望得到连續空間的话，就需要重新整理剩下的物件。

"停止-複製"指的是這種垃圾回收動作不是在背景進行的；相反，垃圾回收動作發生的同時，程式將會暫停。在 Oracle 公司的文檔中會發現，許多參考文献將垃圾回收视為低優先權的背景進程，但是早期版本的 Java 虚擬机並不是這么實現垃圾回收器（Garbage Collector, GC / 垃圾回收器）的。當可用記憶體較低時，垃圾回收器（Garbage Collector, GC / 垃圾回收器）會暫停程式。同樣，"標記－清除"工作也必須在程式暫停的情況下才能進行。

如前面所說，這裡討論的 Java 虚擬机中，記憶體配置以較大的"塊"為單位。如果物件較大，它會佔用單獨的塊。嚴格來說，"停止-複製"要求在釋放旧物件之前，必須先將所有存在物件從旧堆複製到新堆，這導致了大量的記憶體複製行為。有了塊，垃圾回收器（Garbage Collector, GC / 垃圾回收器）就可以把物件複製到廢弃的塊。每個塊都有年代數來記錄自己是否存在。通常，如果塊在某處被參考，其年代數加 1，垃圾回收器（Garbage Collector, GC / 垃圾回收器）會對上次回收動作之後新配置的塊進行整理。這對處理大量短命的臨時物件很有幫助。垃圾回收器（Garbage Collector, GC / 垃圾回收器）會定期進行完整的清理動作——大型物件仍然不會複製（只是年代數會增加），含有小型物件的那些塊則被複製並整理。Java 虚擬机會監视，如果所有物件都很穩定，垃圾回收的效率降低的话，就切換到"標記－清除"方式。同樣，Java 虚擬机會跟蹤"標記－清除"的效果，如果堆積區空間出現很多碎片，就會切換回"停止-複製"方式。這就是"自适應"的由來，你可以給它個啰嗦的稱呼："自适應的、分代的、停止-複製、標記－清除"式的垃圾回收器（Garbage Collector, GC / 垃圾回收器）。

Java 虚擬机中有許多額外技術用來提升速度。尤其是與載入器操作有關的，被稱為"即時"（Just-In-Time, JIT）編譯器的技術。這種技術可以把程式全部或部分翻譯成原生機器碼，所以不需要 JVM 來進行翻譯，因此執行得更快。當需要載入某個類（通常是建立該類別的第一個物件）時，編譯器會先找到其 .class 檔案，然後將該類別的位元組碼装入記憶體。你可以讓即時編譯器編譯所有程式碼，但這種做法有兩個缺點：一是這種載入動作貫穿整個程式生命週期内，累積起來需要花更多時間；二是會增加可執行程式碼的長度（位元組碼要比即時編譯器展開後的原生機器碼小很多），這會導致分頁調度，從而一定降低程式速度。另一種做法稱為延遲評估，代表即時編譯器只有在必要的時候才編譯程式碼。這樣，從未被執行的程式碼也許就壓根不會被 JIT 編譯。新版 JDK 中的 Java HotSpot 技術就採用了類似的做法，程式碼每被執行一次就優化一些，所以執行的次數越多，它的速度就越快。

## 成員初始化（Member Initialization / 成员初始化）

Java 盡量保證所有變數在使用前都能得到適當的初始化。對於方法的區域變數，這種保證會以編譯時錯誤的方式呈現，所以如果寫成：

void f() {
int i;
i++;
}
你會得到一條錯誤資訊，告訴你 i 可能尚未初始化。編譯器可以為 i 赋一個預設值，但是未初始化的區域變數更有可能是程式設計師的疏忽，所以採用預設值反而會掩蓋這種失誤。強制程式設計師提供一個初始值，往往能幫助找出程式里的 bug。

要是類別的成員變數是基本型別，情況就會變得有些不同。正如在"萬物皆物件"一章中所看到的，類別的每個基本型別資料成員保證都會有一個初始值。下面的程式可以驗證這類情況，並顯示它們的值：

// housekeeping/InitialValues.java
// Shows default initial values

public class InitialValues {
boolean t;
char c;
byte b;
short s;
int i;
long l;
float f;
double d;
InitialValues reference;

    void printInitialValues() {
        System.out.println("Data type Initial value");
        System.out.println("boolean " + t);
        System.out.println("char[" + c + "]");
        System.out.println("byte " + b);
        System.out.println("short " + s);
        System.out.println("int " + i);
        System.out.println("long " + l);
        System.out.println("float " + f);
        System.out.println("double " + d);
        System.out.println("reference " + reference);
    }

    public static void main(String[] args) {
        new InitialValues().printInitialValues();
    }

}
**輸出：**

Data type Initial value
boolean false
char[NUL]
byte 0
short 0
int 0
long 0
float 0.0
double 0.0
reference null
可见儘管資料成員的初始值沒有給出，但它們確實有初始值（char 值為 0，所以顯示為空白）。所以這樣至少不會出現"未初始化變數"的風險了。

在類里定義一個物件參考時，如果不將其初始化，那麼參考就會被指定值為 null。

## 指定初始化（Explicit Initialization / 指定初始化）

怎麼給一個變數赋初始值呢？一種很直接的方法是在定義類成員變數的地方為其指定值。以下程式碼修改了 InitialValues 類成員變數的定義，直接提供了初始值：

// housekeeping/InitialValues2.java
// Providing explicit initial values

public class InitialValues2 {
boolean bool = true;
char ch = 'x';
byte b = 47;
short s = 0xff;
int i = 999;
long lng = 1;
float f = 3.14f;
double d = 3.14159;
}
你也可以用同樣的方式初始化非基本型別的物件。如果 Depth 是一個類別，那麼可以像下面這樣建立一個物件並初始化它：

// housekeeping/Measurement.java

class Depth {}

public class Measurement {
Depth d = new Depth();
// ...
}
如果沒有為 d 給予初始值就嘗試使用它，就會出現執行期錯誤，告訴你產生了一個例外（详细见"例外"章节）。

你也可以透過呼叫某個方法來提供初始值：

// housekeeping/MethodInit.java

public class MethodInit {
int i = f();

    int f() {
        return 11;
    }

}
這個方法可以帶有參數，但這些參數不能是未初始化的類成員變數。因此，可以這么寫：

// housekeeping/MethodInit2.java

public class MethodInit2 {
int i = f();
int j = g(i);

    int f() {
        return 11;
    }

    int g(int n) {
        return n * 10;
    }

}
但是你不能這么寫：

// housekeeping/MethodInit3.java

public class MethodInit3 {
//- int j = g(i); // Illegal forward reference
int i = f();

    int f() {
        return 11;
    }

    int g(int n) {
        return n * 10;
    }

}
顯然，上述程式的正確性取決於初始化順序（Initialization Order / 初始化顺序），而與其編譯方式无關。所以，編譯器適當地對"向前參考"发出了警告。

這種初始化方式簡單直观，但有個限制：類別 InitialValues 的每個物件都有相同的初始值，有時這的確是我們需要的，但有時卻需要更大的彈性。

## 建構子初始化（Constructor Initialization / 构造器初始化）

可以用建構子進行初始化，這種方式給了你更大的彈性，因為你可以在執行期呼叫方法進行初始化。但是，這無法阻止自動初始化的進行，他會在建構子被呼叫之前發生。因此，如果使用如下程式碼：

// housekeeping/Counter.java

public class Counter {
int i;

    Counter() {
        i = 7;
    }
    // ...

}
i 首先會被初始化為 0，然後變為 7。對於所有的基本型別和參考，包含在定義時已明確指定初始值的變數，這種情況都是成立的。因此，編譯器不會強制你一定要在建構子的某個地方或在使用它們之前初始化元素——初始化早已得到了保證。,

## 初始化順序（Initialization Order / 初始化顺序）

在類別中變數定義的順序決定了它們初始化順序（Initialization Order / 初始化顺序）。即使變數定義分散在方法定義之間，它們仍會在任何方法（包含建構子）被呼叫之前得到初始化。例如：

// housekeeping/OrderOfInitialization.java
// Demonstrates initialization order
// When the constructor is called to create a
// Window object, you'll see a message:

class Window {
Window(int marker) {
System.out.println("Window(" + marker + ")");
}
}

class House {
Window w1 = new Window(1); // Before constructor

    House() {
        // Show that we're in the constructor:
        System.out.println("House()");
        w3 = new Window(33); // Reinitialize w3
    }

    Window w2 = new Window(2); // After constructor

    void f() {
        System.out.println("f()");
    }

    Window w3 = new Window(3); // At end

}

public class OrderOfInitialization {
public static void main(String[] args) {
House h = new House();
h.f(); // Shows that construction is done
}
}
**輸出：**

Window(1)
Window(2)
Window(3)
House()
Window(33)
f()
在 House 類別中，故意把几個 Window 物件的定義分散在各處，以證明它們全都會在呼叫建構子或其他方法之前得到初始化。此外，w3 在建構子中被再次指定值。

由輸出可见，參考 w3 被初始化了兩次：一次在呼叫建構子前，一次在建構子呼叫期間（第一次參考的物件將被丟棄，並作為垃圾回收）。這乍看可能覺得效率不高，但保證了正確的初始化。想像一下，如果定義了一個多載建構子，在其中沒有初始化 w3，同時在定義 w3 時沒有給予初始值，那會產生怎样的後果呢？

## 靜態資料的初始化（Static Initialization / 静态初始化）

无論建立多少個物件，靜態資料都只佔用一份存儲區欄位。static 關鍵字不能應用於區域變數，所以只能作用於屬性（欄位、欄位）。如果一個欄位是靜態的基本型別，你沒有初始化它，那麼它就會獲得基本型別的標準初始值。如果它是物件參考，那麼它的預設初始值就是 null。

如果在定義時進行初始化，那麼靜態變數看起來就跟非靜態變數一樣。

下面例子顯示了靜態存儲區是何時初始化的：

// housekeeping/StaticInitialization.java
// Specifying initial values in a class definition

class Bowl {
Bowl(int marker) {
System.out.println("Bowl(" + marker + ")");
}

    void f1(int marker) {
        System.out.println("f1(" + marker + ")");
    }

}

class Table {
static Bowl bowl1 = new Bowl(1);

    Table() {
        System.out.println("Table()");
        bowl2.f1(1);
    }

    void f2(int marker) {
        System.out.println("f2(" + marker + ")");
    }

    static Bowl bowl2 = new Bowl(2);

}

class Cupboard {
Bowl bowl3 = new Bowl(3);
static Bowl bowl4 = new Bowl(4);

    Cupboard() {
        System.out.println("Cupboard()");
        bowl4.f1(2);
    }

    void f3(int marker) {
        System.out.println("f3(" + marker + ")");
    }

    static Bowl bowl5 = new Bowl(5);

}

public class StaticInitialization {
public static void main(String[] args) {
System.out.println("main creating new Cupboard()");
new Cupboard();
System.out.println("main creating new Cupboard()");
new Cupboard();
table.f2(1);
cupboard.f3(1);
}

    static Table table = new Table();
    static Cupboard cupboard = new Cupboard();

}
**輸出：**

Bowl(1)
Bowl(2)
Table()
f1(1)
Bowl(4)
Bowl(5)
Bowl(3)
Cupboard()
f1(2)
main creating new Cupboard()
Bowl(3)
Cupboard()
f1(2)
main creating new Cupboard()
Bowl(3)
Cupboard()
f1(2)
f2(1)
f3(1)
Bowl 類展示類別的建立，而 Table 和 Cupboard 在它們的類定義中包含 Bowl 型別的靜態資料成員。注意，在靜態資料成員定義之前，Cupboard 類別中先定義了一個 Bowl 型別的非靜態成員 b3。

由輸出可见，靜態初始化只有在必要時刻才會進行。如果不建立 Table 物件，也不參考 Table.bowl1 或 Table.bowl2，那麼靜態的 Bowl 類物件 bowl1 和 bowl2 永遠不會被建立。只有在第一個 Table 物件被建立（或被访問）時，它們才會被初始化。此後，靜態物件不會再次被初始化。

初始化順序先是靜態物件（如果它們之前沒有被初始化的話），然後是非靜態物件，從輸出中可以看出。要執行 main() 方法，必須載入 StaticInitialization 類別，它的靜態屬性 table 和 cupboard 隨後被初始化，這會導致它們對應的類也被載入，而由於它們都包含靜態的 Bowl 物件，所以 Bowl 類別也會被載入。因此，在這個特殊的程式中，所有的類別都會在 main() 方法之前被載入。實際情況通常並非如此，因為在典型的程式中，不會像本例中所示的那樣，將所有東西透過 static 联系起來。

概括一下建立物件的過程，假設有個名為 Dog 的類別：

即使沒有明確地使用 static 關鍵字，建構子實際上也是靜態方法。所以，當首次建立 Dog 型別的物件或是首次访問 Dog 類別的靜態方法或屬性時，Java 直譯器必須在類別路徑中尋找，以定位 Dog.class。
當載入完 Dog.class 後（後面會学到，這將建立一個 Class 物件），有關靜態初始化的所有動作都會執行。因此，靜態初始化只會在首次載入 Class 物件時初始化一次。
當用 new Dog() 建立物件時，首先會在堆積區（heap）上為 Dog 物件配置足夠的儲存空間。
配置的儲存空間首先會被清零，即會將 Dog 物件中的所有基本型別資料設置為預設值（數字會被置為 0，布林型別和字元型別也相同），參考被置為 null。
執行所有出現在欄位定義處的初始化動作。
執行建構子。你將會在"複用"這一章看到，這可能會牽涉到很多動作，尤其當涉及繼承的時候。

## 明確的靜態初始化

你可以將一組靜態初始化動作放在類裡面一個特殊的"靜態區塊"（有時叫做靜態塊）中。像下面這樣：

// housekeeping/Spoon.java

public class Spoon {
static int i;

    static {
        i = 47;
    }

}
這看起來像個方法，但實際上它只是一段跟在 static 關鍵字後面的程式碼塊。與其他靜態初始化動作一樣，這段程式碼僅執行一次：當首次建立這個類別的物件或首次访問這個類別的靜態成員（甚至不需要建立該類別的物件）時。例如：

// housekeeping/ExplicitStatic.java
// Explicit static initialization with "static" clause

class Cup {
Cup(int marker) {
System.out.println("Cup(" + marker + ")");
}

    void f(int marker) {
        System.out.println("f(" + marker + ")");
    }

}

class Cups {
static Cup cup1;
static Cup cup2;

    static {
        cup1 = new Cup(1);
        cup2 = new Cup(2);
    }

    Cups() {
        System.out.println("Cups()");
    }

}

public class ExplicitStatic {
public static void main(String[] args) {
System.out.println("Inside main()");
Cups.cup1.f(99); // [1]
}

    // static Cups cups1 = new Cups(); // [2]
    // static Cups cups2 = new Cups(); // [2]

}
**輸出：**

Inside main
Cup(1)
Cup(2)
f(99)
无論是透過標為 [1] 的行访問靜態的 cup1 物件，還是把標為 [1] 的行去掉，讓它去執行標為 [2] 的那行程式碼（去掉 [2] 的注釋），Cups 的靜態初始化動作都會執行。如果同時注釋 [1] 和 [2] 處，那麼 Cups 的靜態初始化就不會進行。此外，把標為 [2] 處的注釋都去掉還是只去掉一個，靜態初始化只會執行一次。

## 非靜態實例初始化（Instance Initialization / 实例初始化）

Java 提供了被稱為實例初始化的類似語法，用來初始化每個物件的非靜態變數，例如：

// housekeeping/Mugs.java
// Instance initialization

class Mug {
Mug(int marker) {
System.out.println("Mug(" + marker + ")");
}
}

public class Mugs {
Mug mug1;
Mug mug2;
{ // [1]
mug1 = new Mug(1);
mug2 = new Mug(2);
System.out.println("mug1 & mug2 initialized");
}

    Mugs() {
        System.out.println("Mugs()");
    }

    Mugs(int i) {
        System.out.println("Mugs(int)");
    }

    public static void main(String[] args) {
        System.out.println("Inside main()");
        new Mugs();
        System.out.println("new Mugs() completed");
        new Mugs(1);
        System.out.println("new Mugs(1) completed");
    }

}
**輸出：**

Inside main
Mug(1)
Mug(2)
mug1 & mug2 initialized
Mugs()
new Mugs() completed
Mug(1)
Mug(2)
mug1 & mug2 initialized
Mugs(int)
new Mugs(1) completed
看起來它很像靜態程式碼塊，只不過少了 static 關鍵字。這種語法對於支援"匿名内部類"（參见"内部類"一章）的初始化是必須的，但是你也可以使用它保證某些操作一定會發生，而不管哪個建構子被呼叫。從輸出看出，實例初始化區塊是在兩個建構子之前執行的。

## 陣列初始化（Array Initialization / 数组初始化）

陣列是相同型別的、用一個識別名稱名稱封裝到一起的一個物件序列或基本型別資料序列。陣列是透過中括號索引運算子 [] 來定義和使用的。要定義一個陣列參考，只需要在型別名加上中括號：

int[] a1;
中括號也可放在識別名稱的後面，兩者的含義是一樣的：

int a1[];
這種格式符合 C 和 C++ 程式設計師的習慣。不過前一種格式或許更合理，畢竟它表示型別是"一個 int 型陣列"。本书中採用這種格式。

編譯器不允許指定陣列的大小。這又把我們帶回有關"參考"的問題上。你所擁有的只是對陣列的一個參考（你已經為該參考配置了足夠的儲存空間），但是还沒有給陣列物件本身配置任何空間。為了給陣列建立相應的儲存空間，必須寫初始化表達式。對於陣列，初始化動作可以出現在程式碼的任何地方，但是也可以使用一種特殊的初始化表達式，它必須在建立陣列的地方出現。這種特殊的初始化是由一對大括號括起來的值組成。這種情況下，儲存空間的配置（相當於使用 new） 將由編譯器負責。例如：

int[] a1 = {1, 2, 3, 4, 5};
那麼為什麼在还沒有陣列的時候定義一個陣列參考呢？

int[] a2;
在 Java 中可以將一個陣列指定值給另一個陣列，所以可以這樣：

a2 = a1;
其實真正做的只是複製了一個參考，就像下面示範的這樣：

// housekeeping/ArraysOfPrimitives.java

public class ArraysOfPrimitives {
public static void main(String[] args) {
int[] a1 = {1, 2, 3, 4, 5};
int[] a2;
a2 = a1;
for (int i = 0; i < a2.length; i++) {
a2[i] += 1;
}
for (int i = 0; i < a1.length; i++) {
System.out.println("a1[" + i + "] = " + a1[i]);
}
}
}
**輸出：**

a1[0] = 2;
a1[1] = 3;
a1[2] = 4;
a1[3] = 5;
a1[4] = 6;
a1 初始化了，但是 a2 沒有；這裡，a2 在後面被赋給另一個陣列。由於 a1 和 a2 是相同陣列的別名，因此透過 a2 所做的修改在 a1 中也能看到。

所有的陣列（无論是物件陣列還是基本型別陣列）都有一個固定成員 length，告訴你這個陣列有多少個元素，你不能對其修改。與 C 和 C++ 類似，Java 陣列計數也是從 0 開始的，所能使用的最大索引數是 length - 1。超過這個邊界，C 和 C++ 會預設接收，允許你访問所有記憶體，許多聲名狼藉的 bug 都是由此而生。但是 Java 在你访問超出這個邊界時，會報執行期錯誤（例外），從而避免此類問題。

## 動態建立陣列

如果在編寫程式時，不確定陣列中需要多少個元素，可以使用 new 在陣列中建立元素。如下例所示，使用 new 建立基本型別陣列。new 不能建立非陣列以外的基本型別資料：

// housekeeping/ArrayNew.java
// Creating arrays with new
import java.util.\*;

public class ArrayNew {
public static void main(String[] args) {
int[] a;
Random rand = new Random(47);
a = new int[rand.nextInt(20)];
System.out.println("length of a = " + a.length);
System.out.println(Arrays.toString(a));
}
}
**輸出：**

length of a = 18
[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
陣列的大小是透過 Random.nextInt() 隨机確定的，這個方法會回傳 0 到輸入參數之間的一個值。 由於隨机性，很明顯陣列的建立確實是在執行期進行的。此外，程式輸出表示，陣列元素中的基本資料型別值會自動初始化為預設值（對於數字和字元是 0；對於布林型別是 false）。Arrays.toString() 是 java.util 標準類別庫中的方法，會產生一维陣列的可印出版本。

本例中，陣列也可以在定義的同時進行初始化：

int[] a = new int[rand.nextInt(20)];
如果可能的话，應該盡量這么做。

如果你建立了一個非基本型別的陣列，那麼你建立的是一個參考陣列。以整數型別的包裝型別 Integer 為例，它是一個類而非基本型別：

// housekeeping/ArrayClassObj.java
// Creating an array of nonprimitive objects

import java.util.\*;

public class ArrayClassObj {
public static void main(String[] args) {
Random rand = new Random(47);
Integer[] a = new Integer[rand.nextInt(20)];
System.out.println("length of a = " + a.length);
for (int i = 0; i < a.length; i++) {
a[i] = rand.nextInt(500); // Autoboxing
}
System.out.println(Arrays.toString(a));
}
}
**輸出：**

length of a = 18
[55, 193, 361, 461, 429, 368, 200, 22, 207, 288, 128, 51, 89, 309, 278, 498, 361, 20]
這裡，即使使用 new 建立陣列之後：

Integer[] a = new Integer[rand.nextInt(20)];  
它只是一個參考陣列，直到透過建立新的 Integer 物件（透過自動裝箱），並把物件指定值給參考，初始化才算結束：

a[i] = rand.nextInt(500);
如果忘記了建立物件，但试圖使用陣列中的空參考，就會在執行期產生例外。

也可以用大括號括起來的清單來初始化陣列，有兩種形式：

// housekeeping/ArrayInit.java
// Array initialization
import java.util.\*;

public class ArrayInit {
public static void main(String[] args) {
Integer[] a = {
1, 2,
3, // Autoboxing
};
Integer[] b = new Integer[] {
1, 2,
3, // Autoboxing
};
System.out.println(Arrays.toString(a));
System.out.println(Arrays.toString(b));

    }

}
**輸出：**

[1, 2, 3]
[1, 2, 3]
在這兩種形式中，初始化清單的最後一個逗號是可選的（這一特性使維護长清單變得更容易）。

儘管第一種形式很有用，但是它更受限制，因為它只能用於陣列定義處。第二種和第三種形式可以用在任何地方，甚至用在方法的内部。例如，你建立了一個 String 陣列，將其傳遞給另一個類別的 main() 方法，如下：

// housekeeping/DynamicArray.java
// Array initialization

public class DynamicArray {
public static void main(String[] args) {
Other.main(new String[] {"fiddle", "de", "dum"});
}
}

class Other {
public static void main(String[] args) {
for (String s: args) {
System.out.print(s + " ");
}
}
}
**輸出：**

fiddle de dum
Other.main() 的參數是在呼叫處建立的，因此你甚至可以在方法呼叫處提供可替換的參數。

## 可變參數列表（Variable Arguments, varargs / 可变参数列表）

你可以以一種類似 C 語言中的可變參數列表（Variable Arguments, varargs / 可变参数列表）（C 通常把它稱為"varargs"）來建立和呼叫方法。這可以應用在參數個數或型別未知的場合。由於所有的類別都最後繼承於 Object 類別（隨著本书的進展，你會對此有更深的認識），所以你可以建立一個以 Object 陣列為參數的方法，並像下面這樣呼叫：

// housekeeping/VarArgs.java
// Using array syntax to create variable argument lists

class A {}

public class VarArgs {
static void printArray(Object[] args) {
for (Object obj: args) {
System.out.print(obj + " ");
}
System.out.println();
}

    public static void main(String[] args) {
        printArray(new Object[] {47, (float) 3.14, 11.11});
        printArray(new Object[] {"one", "two", "three"});
        printArray(new Object[] {new A(), new A(), new A()});
    }

}
**輸出：**

47 3.14 11.11
one two three
A@15db9742 A@6d06d69c A@7852e922
printArray() 的參數是 Object 陣列，使用 for-in 語法走訪和印出陣列的每一項。標準 Java 庫能輸出有意義的內容，但這裡建立的是類別的物件，印出出的內容是類別名稱，後面跟著一個 @ 符號以及多個十六進位數字。因而，預設行為（如果沒有定義 toString() 方法的话，後面會讲這個方法）就是印出類別名稱和物件的位址。

你可能看到像上面這樣編寫的 Java 5 之前的程式碼，它們可以產生可變的參數清單。在 Java 5 中，這種期待已久的特性終於加入了進來，就像在 printArray() 中看到的那樣：

// housekeeping/NewVarArgs.java
// Using array syntax to create variable argument lists

public class NewVarArgs {
static void printArray(Object... args) {
for (Object obj: args) {
System.out.print(obj + " ");
}
System.out.println();
}

    public static void main(String[] args) {
        // Can take individual elements:
        printArray(47, (float) 3.14, 11.11);
        printArray(47, 3.14F, 11.11);
        printArray("one", "two", "three");
        printArray(new A(), new A(), new A());
        // Or an array:
        printArray((Object[]) new Integer[] {1, 2, 3, 4});
        printArray(); // Empty list is OK
    }

}
**輸出：**

47 3.14 11.11
47 3.14 11.11
one two three
A@15db9742 A@6d06d69c A@7852e922
1 2 3 4
有了可變參數，你就再也不用明確地編寫陣列語法了，當你指定參數時，編譯器實際上會為你補上陣列。你取得的仍然是一個陣列，這就是為什麼 printArray() 可以使用 for-in 走訪陣列的原因。但是，這不僅僅只是從元素清單到陣列的自動轉換。注意程式的倒數第二行，一個 Integer 陣列（透過自動裝箱建立）被轉型為一個 Object 陣列（為了移除編譯器的警告），並且傳遞給了 printArray()。顯然，編譯器會發現這是一個陣列，不會執行轉換。因此，如果你有一組東西，可以把它們當作清單傳遞，而如果你已經有了一個陣列，該方法會把它們當作可變參數列表（Variable Arguments, varargs / 可变参数列表）來接收。

程式的最後一行表示，可變參數的個數可以為 0。當具有可選的結尾參數時，這一特性會有幫助：

// housekeeping/OptionalTrailingArguments.java

public class OptionalTrailingArguments {
static void f(int required, String... trailing) {
System.out.print("required: " + required + " ");
for (String s: trailing) {
System.out.print(s + " ");
}
System.out.println();
}

    public static void main(String[] args) {
        f(1, "one");
        f(2, "two", "three");
        f(0);
    }

}
**輸出：**

required: 1 one
required: 2 two three
required: 0
這段程式展示了如何使用除了 Object 類別之外型別的可變參數列表（Variable Arguments, varargs / 可变参数列表）。這裡，所有的可變參數都是 String 物件。可變參數列表（Variable Arguments, varargs / 可变参数列表）中可以使用任何型別的參數，包含基本型別。下面例子展示了可變參數列表（Variable Arguments, varargs / 可变参数列表）變為陣列的情況，並且如果清單中沒有任何元素，那麼转變為大小為 0 的陣列：

// housekeeping/VarargType.java

public class VarargType {
static void f(Character... args) {
System.out.print(args.getClass());
System.out.println(" length " + args.length);
}

    static void g(int... args) {
        System.out.print(args.getClass());
        System.out.println(" length " + args.length)
    }

    public static void main(String[] args) {
        f('a');
        f();
        g(1);
        g();
        System.out.println("int[]: "+ new int[0].getClass());
    }

}
**輸出：**

class [Ljava.lang.Character; length 1
class [Ljava.lang.Character; length 0
class [I length 1
class [I length 0
int[]: class [I
getClass() 方法屬於 Object 類別，將在"型別資訊"一章中全面介紹。它會產生物件的類別，並在印出該類時，看到表示該類型別的編码字串。前導的 [ 代表這是一個後面緊接的型別的陣列，I 表示基本型別 int；為了進行雙重檢查，我在最後一行建立了一個 int 陣列，印出了其型別。這樣也驗證了使用可變參數列表（Variable Arguments, varargs / 可变参数列表）不依賴於自動裝箱，而使用的是基本型別。

不過，可變參數列表（Variable Arguments, varargs / 可变参数列表）與自動裝箱可以和諧共處，如下：

// housekeeping/AutoboxingVarargs.java

public class AutoboxingVarargs {
public static void f(Integer... args) {
for (Integer i: args) {
System.out.print(i + " ");
}
System.out.println();
}

    public static void main(String[] args) {
        f(1, 2);
        f(4, 5, 6, 7, 8, 9);
        f(10, 11, 12);

    }

}
**輸出：**

1 2
4 5 6 7 8 9
10 11 12
注意，你可以在單個參數清單中將型別混用在一起，自動裝箱機制會有選擇地把 int 型別的參數提升為 Integer。

可變參數列表（Variable Arguments, varargs / 可变参数列表）使得方法多載（Method Overloading / 方法重载）更複雜了，儘管乍看之下似乎足夠安全：

// housekeeping/OverloadingVarargs.java

public class OverloadingVarargs {
static void f(Character... args) {
System.out.print("first");
for (Character c: args) {
System.out.print(" " + c);
}
System.out.println();
}

    static void f(Integer... args) {
        System.out.print("second");
        for (Integer i: args) {
            System.out.print(" " + i);
        }
        System.out.println();
    }

    static void f(Long... args) {
        System.out.println("third");
    }

    public static void main(String[] args) {
        f('a', 'b', 'c');
        f(1);
        f(2, 1);
        f(0);
        f(0L);
        //- f(); // Won's compile -- ambiguous
    }

}
**輸出：**

first a b c
second 1
second 2 1
second 0
third
在每種情況下，編譯器都會使用自動裝箱來符合多載的方法，然後呼叫最明確符合的方法。

但是如果呼叫不含參數的 f()，編譯器就無法知道應該呼叫哪個方法了。儘管這個錯誤可以弄清楚，但是它可能會使客戶端程式設計師感到意外。

你可能會透過在某個方法中增加一個非可變參數解決這個問題：

// housekeeping/OverloadingVarargs2.java
// {WillNotCompile}

public class OverloadingVarargs2 {
static void f(float i, Character... args) {
System.out.println("first");
}

    static void f(Character... args) {
        System.out.println("second");
    }

    public static void main(String[] args) {
        f(1, 'a');
        f('a', 'b');
    }

}
{WillNotCompile} 注釋把該檔案排除在了本书的 Gradle 構建之外。如果你手動編譯它，會得到下面的錯誤資訊：

OverloadingVarargs2.java:14:error:reference to f is ambiguous f('a', 'b');
\^
both method f(float, Character...) in OverloadingVarargs2 and method f(Character...) in OverloadingVarargs2 match 1 error
如果你給這兩個方法都加入一個非可變參數，就可以解決問題了：

// housekeeping/OverloadingVarargs3

public class OverloadingVarargs3 {
static void f(float i, Character... args) {
System.out.println("first");
}

    static void f(char c, Character... args) {
        System.out.println("second");
    }

    public static void main(String[] args) {
        f(1, 'a');
        f('a', 'b');
    }

}
**輸出：**

first
second
你應該總是在多載方法的一個版本上使用可變參數列表（Variable Arguments, varargs / 可变参数列表），或是壓根不用它。

## 列舉型別（enum / 枚举类型）

Java 5 中加入了一個看似很小的特性 enum 關鍵字，它使得我們在需要群組並使用列舉型別（enum / 枚举类型）集時，可以很方便地處理。以前，你需要建立一個整數常數集，但是這些值並不會將自身限制在這個常數集的范圍内，因此使用它們更有風險，而且更難使用。列舉型別（enum / 枚举类型）屬於非常普遍的需求，C、C++ 和其他許多語言都已經擁有它了。在 Java 5 之前，Java 程式設計師必須了解許多细节並格外仔细地去達成 enum 的效果。現在 Java 也有了 enum，並且它的功能比 C/C++ 中的完備得多。下面是個簡單的例子：

// housekeeping/Spiciness.java

public enum Spiciness {
NOT, MILD, MEDIUM, HOT, FLAMING
}
這裡建立了一個名為 Spiciness 的列舉型別（enum / 枚举类型），它有5個值。由於列舉型別（enum / 枚举类型）的實例是常數，因此按照命名慣例，它們都用大寫字母表示（如果名稱中含有多個單词，使用下划線分隔）。

要使用 enum，需要建立一個該型別的參考，然後將其指定值給某個實例：

// housekeeping/SimpleEnumUse.java

public class SimpleEnumUse {
public static void main(String[] args) {
Spiciness howHot = Spiciness.MEDIUM;
System.out.println(howHot);
}
}
**輸出：**

MEDIUM
在你建立 enum 時，編譯器會自動加入一些有用的特性。例如，它會建立 toString() 方法，以便你方便地顯示某個 enum 實例的名稱，這從上面例子中的輸出可以看出。編譯器还會建立 ordinal() 方法表示某個特定 enum 常數的宣告順序，static values() 方法按照 enum 常數的宣告順序，生成這些常數值構成的陣列：

// housekeeping/EnumOrder.java

public class EnumOrder {
public static void main(String[] args) {
for (Spiciness s: Spiciness.values()) {
System.out.println(s + ", ordinal " + s.ordinal());
}
}
}
**輸出：**

NOT, ordinal 0
MILD, ordinal 1
MEDIUM, ordinal 2
HOT, ordinal 3
FLAMING, ordinal 4
儘管 enum 看起來像是一種新的資料型別，但是這個關鍵字只是在生成 enum 的類時，產生了某些編譯器行為，因此在很大程度上你可以將 enum 當作其他任何類別。事實上，enum 確實是類別，並且具有自己的方法。

enum 有一個很實用的特性，就是在 switch 敘述中使用：

// housekeeping/Burrito.java

public class Burrito {
Spiciness degree;

    public Burrito(Spiciness degree) {
        this.degree = degree;
    }

    public void describe() {
        System.out.print("This burrito is ");
        switch(degree) {
            case NOT:
                System.out.println("not spicy at all.");
                break;
            case MILD:
            case MEDIUM:
                System.out.println("a little hot.");
                break;
            case HOT:
            case FLAMING:
            default:
                System.out.println("maybe too hot");
        }
    }

    public static void main(String[] args) {
        Burrito plain = new Burrito(Spiciness.NOT),
        greenChile = new Burrito(Spiciness.MEDIUM),
        jalapeno = new Burrito(Spiciness.HOT);
        plain.describe();
        greenChile.describe();
        jalapeno.describe();
    }

}
**輸出：**

This burrito is not spicy at all.
This burrito is a little hot.
This burrito is maybe too hot.
由於 switch 是在有限的可能值集合中選擇，因此它與 enum 是很適合的組合。注意，enum 的名稱是如何能夠更清楚地表示程式的目的的。

通常，你可以將 enum 用作另一種建立資料型別的方式，然後使用所得到的型別。這正是關键所在，所以你不用過多地考慮它們。在 enum 被引入之前，你必須花費大量的心力去建立一個等同的列舉型別（enum / 枚举类型），並是安全可用的。

這些介紹對於你理解和使用基本的 enum 已經足夠了，我們會在"列舉"一章中進行更深入的探討。

## 本章小結

建構子，這種看起來精巧的初始化機制，應該給了你很强的暗示：初始化在寫程式語言中的重要地位。C++ 的發明者 Bjarne Stroustrup 在設計 C++ 期間，在针對 C 語言的生產效率進行的最初調查中發現，錯誤的初始化會導致大量寫程式錯誤。這些錯誤很難被發現，同樣，不合理的清理也會如此。因為建構子能保證進行正確的初始化與清理（沒有正確的建構子呼叫，編譯器就不允許建立物件），所以你就有了完全的控制和安全。

在 C++ 中，解構器很重要，因為用 new 建立的物件必須被明確地銷毀。在 Java 中，垃圾回收器（Garbage Collector, GC / 垃圾回收器）會自動地釋放所有物件的記憶體，所以很多時候類似的清理方法就不太需要了（但是當要用到的時候，你得自己動手）。在不需要類似解構器行為的時候，Java 的垃圾回收器（Garbage Collector, GC / 垃圾回收器）極大地簡化了寫程式，並加强了記憶體管理上的安全性。一些垃圾回收器（Garbage Collector, GC / 垃圾回收器）甚至能清理其他資源，如圖形和檔案handle（句柄）。不過，垃圾回收器（Garbage Collector, GC / 垃圾回收器）確實增加了執行期成本，由於 Java 直譯器從一開始就很慢，所以這種成本到底造成多大的影響很難看出來。隨著時間的推移，Java 在效能方面提升了很多，但是速度問題仍然是它進入某些特定寫程式领欄位的障碍。

由於要保證所有物件被建立，實際上建構子比這裡討論得更複雜。特別是當透過組合或繼承建立新類別的時候，這種保證仍然成立，並且需要一些額外的語法來支援。在後面的章节中，你會學習組合，繼承以及它們如何影響建構子。

# 第 6 章前半檢查題詳解

## 總評

你這次選擇題表現：

```text
選擇題：13 / 17
簡答題：0.5 / 3 左右
```

主要問題不是「完全不懂」，而是幾個核心觀念還沒有穩：

| 主題                      | 狀態     |
| ------------------------- | -------- |
| 建構子基本概念            | 大致 OK  |
| 方法多載                  | 基本 OK  |
| 無參建構子                | OK       |
| `this`                    | 需要重補 |
| `this(...)` 建構子串接    | 需要重補 |
| `static` 與 instance 差異 | 需要重補 |
| 建構子呼叫順序            | 需要重補 |

---

# Q1. 建構子基本概念

## 完整問題

下面哪一個最正確？

A. 建構子就是一種普通方法，只是通常會回傳物件
B. 建構子會在 `new` 物件時自動被呼叫，用來初始化物件
C. 建構子一定要寫 `void`
D. 建構子只能有一個，不能多載

## 你的答案

```text
B
```

## 正確答案

```text
B
```

## 判定

正確。

## 詳解

建構子（Constructor / 构造函数）是在建立物件時自動被呼叫的特殊初始化入口。

例如：

```java
Tree t = new Tree();
```

這行做了幾件事：

1. 在 HEAP 堆（Heap / 堆）配置 Tree 物件空間
2. 執行預設初始化
3. 呼叫 `Tree()` 建構子
4. 把新物件的參考（Reference / 引用）交給 `t`

建構子的核心用途是：

```text
保證物件在被使用前，已經完成必要初始化。
```

## 考得觀念

- 建構子不是普通方法
- 建構子由 `new` 觸發
- 建構子負責初始化物件狀態

## 面試常問

### Q：Constructor 跟普通 method 差在哪？

建構子會在 `new` 物件時自動執行，名稱必須跟 class 一樣，而且沒有回傳型別。普通 method 則需要被明確呼叫，可以有回傳型別，也可以叫任何合法名稱。

---

# Q2. 建構子名稱

## 完整問題

Java 的建構子名稱必須：

A. 跟方法名稱一樣
B. 跟檔案名稱一樣
C. 跟類別名稱一樣
D. 固定叫 `constructor`

## 你的答案

```text
C
```

## 正確答案

```text
C
```

## 判定

正確。

## 詳解

建構子名稱必須跟類別名稱完全相同。

例如：

```java
class Rock {
    Rock() {
    }
}
```

`Rock()` 是建構子，因為它跟 `class Rock` 的名稱相同，而且沒有回傳型別。

錯誤例子：

```java
class Rock {
    void Rock() {
    }
}
```

這不是建構子，這是一個普通方法，因為它有 `void` 回傳型別。

## 考得觀念

- 建構子的名稱規則
- 建構子與普通方法的語法差異

## 面試常問

### Q：如果建構子前面寫了 `void` 會怎樣？

它就不再是建構子，而是普通方法。這種錯誤很隱蔽，因為程式可能還能編譯，但你以為的初始化流程其實沒有發生。

---

# Q3. 建構子回傳值

## 完整問題

下面哪一個寫法是合法的建構子？

```java
class Rock {
    Rock() {
        System.out.println("Rock");
    }
}
```

A. 合法，因為建構子沒有回傳型別
B. 不合法，因為少了 `void`
C. 不合法，因為建構子一定要 `public`
D. 不合法，因為建構子不能印東西

## 你的答案

```text
A
```

## 正確答案

```text
A
```

## 判定

正確。

## 詳解

建構子沒有回傳型別，不是 `void`，也不是其他型別。

合法：

```java
Rock() {
}
```

不合法作為建構子：

```java
void Rock() {
}
```

第二個雖然語法上是合法方法，但它不是建構子。

建構子裡可以寫一般程式碼，例如印出文字、設定欄位、呼叫方法等。

## 考得觀念

- 建構子沒有回傳型別
- `void` 不等於「沒有回傳型別」
- 建構子內可以執行初始化邏輯

## 面試常問

### Q：Constructor 可以有 return 嗎？

不能有回傳值，但可以使用單獨的 `return;` 提早結束建構子。不過實務上少見，不建議常用。

---

# Q4. 物件建立流程

## 完整問題

執行下面程式時，哪一行會觸發建構子？

```java
class Tree {
    Tree() {
        System.out.println("new tree");
    }
}

public class Main {
    public static void main(String[] args) {
        Tree t = new Tree();
    }
}
```

A. `class Tree`
B. `Tree()` 的宣告
C. `Tree t = new Tree();`
D. `public static void main`

## 你的答案

```text
C
```

## 正確答案

```text
C
```

## 判定

正確。

## 詳解

真正觸發建構子的是：

```java
new Tree()
```

整行是：

```java
Tree t = new Tree();
```

其中：

```java
Tree t
```

宣告一個 Tree 型別的參考變數。

```java
new Tree()
```

建立 Tree 物件並呼叫建構子。

```java
=
```

把新物件的參考交給變數 `t`。

## 考得觀念

- `new` 才會建立物件
- 宣告變數不等於建立物件
- 建構子由 `new` 觸發

## 面試常問

### Q：`Tree t;` 跟 `Tree t = new Tree();` 差在哪？

`Tree t;` 只宣告參考變數，還沒有物件。
`Tree t = new Tree();` 才是真的建立物件，並讓 `t` 指向它。

---

# Q5. 方法多載判斷

## 完整問題

下面哪些方法構成合法多載？

```java
void print(int x) {}
void print(String x) {}
```

A. 合法，因為方法名稱相同、參數列表不同
B. 不合法，因為 Java 不允許同名方法
C. 不合法，因為回傳值一樣
D. 合法，因為參數名稱不同

## 你的答案

```text
A
```

## 正確答案

```text
A
```

## 判定

正確。

## 詳解

方法多載（Method Overloading / 方法重载）允許同一個 class 裡有多個同名方法，但參數列表必須不同。

這裡：

```java
void print(int x) {}
void print(String x) {}
```

方法名稱都是 `print`，但參數型別不同：

```text
int
String
```

所以是合法多載。

注意，不是因為參數名稱不同。下面這樣不合法：

```java
void print(int x) {}
void print(int y) {}
```

因為參數列表的型別一樣，只有參數名稱不同，不能構成多載。

## 考得觀念

- 多載看的是 method name + parameter list
- 參數型別比參數名稱重要
- 回傳型別不是多載判斷依據

## 面試常問

### Q：Overloading 跟 Overriding 差在哪？

Overloading 是同一個 class 中方法名稱相同、參數列表不同。
Overriding 是子類別重新定義父類別的方法，方法簽名通常相同，屬於多型機制。

---

# Q6. 回傳值不能用來多載

## 完整問題

下面程式為什麼不合法？

```java
void f() {}

int f() {
    return 1;
}
```

A. 因為 Java 不允許 `int` 當回傳型別
B. 因為兩個方法只有回傳型別不同，參數列表沒有不同
C. 因為 `void` 方法不能跟任何方法同名
D. 因為 `return 1;` 寫錯了

## 你的答案

```text
B
```

## 正確答案

```text
B
```

## 判定

正確。

## 詳解

Java 不能只靠回傳型別區分多載方法。

這兩個方法的參數列表完全一樣：

```java
f()
f()
```

差別只有回傳型別：

```java
void
int
```

但 Java 呼叫方法時，有可能忽略回傳值：

```java
f();
```

這時編譯器無法判斷你要呼叫哪一個。

所以 Java 規定：多載必須靠參數列表區分，不能只靠回傳型別。

## 考得觀念

- 回傳型別不能作為唯一多載依據
- 方法簽名主要看方法名與參數列表
- 呼叫者可能忽略回傳值

## 面試常問

### Q：Java method signature 包含 return type 嗎？

在多載判斷上，不能只靠 return type。通常說 method signature 時，重點是方法名稱與參數列表。

---

# Q7. 無參建構子

## 完整問題

下面程式可以編譯嗎？

```java
class Bird {
}

public class Main {
    public static void main(String[] args) {
        Bird b = new Bird();
    }
}
```

A. 可以，因為沒有寫任何建構子時，編譯器會自動提供無參建構子
B. 不可以，因為每個 class 都必須手寫建構子
C. 不可以，因為 `Bird()` 沒有被定義
D. 可以，但 `b` 會是 `null`

## 你的答案

```text
A
```

## 正確答案

```text
A
```

## 判定

正確。

## 詳解

如果一個 class 裡完全沒有寫任何建構子，Java 編譯器會自動提供一個無參建構子（No-argument Constructor / 无参构造器）。

所以：

```java
class Bird {
}
```

等效概念上可以理解成：

```java
class Bird {
    Bird() {
    }
}
```

但這是編譯器自動補的，不是你原始碼裡真的寫了。

## 考得觀念

- 沒寫任何建構子時，編譯器會自動補無參建構子
- `new Bird()` 會呼叫這個自動補的建構子
- `b` 會指向新物件，不是 `null`

## 面試常問

### Q：Default constructor 什麼時候會自動產生？

只有在 class 裡完全沒有任何建構子時，編譯器才會自動產生。

---

# Q8. 無參建構子不再自動產生

## 完整問題

下面程式可以編譯嗎？

```java
class Bird2 {
    Bird2(int i) {
    }
}

public class Main {
    public static void main(String[] args) {
        Bird2 b = new Bird2();
    }
}
```

A. 可以，因為 Java 一定會自動補無參建構子
B. 不可以，因為你已經寫了有參建構子，編譯器就不會自動補無參建構子
C. 可以，因為 `int i` 會自動變成 0
D. 不可以，因為 class 不能叫 Bird2

## 你的答案

```text
B（NOT SURE）
```

## 正確答案

```text
B
```

## 判定

正確。

## 詳解

你答對了。

只要你已經手寫任何一個建構子，不管是有參還是無參，Java 編譯器就不會再自動補無參建構子。

這個 class 只有：

```java
Bird2(int i) {
}
```

所以你只能這樣建立：

```java
Bird2 b = new Bird2(1);
```

不能這樣：

```java
Bird2 b = new Bird2();
```

除非你自己補：

```java
Bird2() {
}
```

## 考得觀念

- 自動無參建構子只在「完全沒寫建構子」時存在
- 寫了有參建構子後，無參建構子不會自動存在
- `new Bird2()` 必須找到匹配的建構子

## 面試常問

### Q：為什麼我寫了有參 constructor 後，`new User()` 不能用了？

因為 Java 認為你已經明確定義物件初始化方式，所以不再自動產生無參建構子。若需要 `new User()`，必須自己手寫無參建構子。

---

# Q9. `this` 基本概念

## 完整問題

在非 `static` 方法裡，`this` 代表：

A. 目前這個方法
B. 目前這個類別本身
C. 目前正在操作的那個物件
D. 目前這個檔案

## 你的答案

```text
A
```

## 正確答案

```text
C
```

## 判定

錯誤。

## 詳解

`this` 不是目前這個方法。

`this` 代表：

```text
目前正在執行這個 instance method 的那個物件本身。
```

例如：

```java
class User {
    String name;

    void printName() {
        System.out.println(this.name);
    }
}
```

如果你寫：

```java
User u1 = new User();
User u2 = new User();

u1.printName();
u2.printName();
```

第一次呼叫時：

```text
this = u1 指向的物件
```

第二次呼叫時：

```text
this = u2 指向的物件
```

所以 `this` 不是方法，也不是 class，而是「這次被操作的那個物件」。

## 考得觀念

- `this` 是目前物件的參考
- `this` 只能出現在 instance context
- `this` 不代表方法本身

## 面試常問

### Q：什麼是 `this`？

`this` 是目前物件的參考。當 instance method 被某個物件呼叫時，Java 會隱含把該物件傳進方法內部，方法裡可以用 `this` 指到它。

---

# Q10. `this.s = s`

## 完整問題

下面程式中的 `this.name = name;` 是什麼意思？

```java
class User {
    String name;

    User(String name) {
        this.name = name;
    }
}
```

A. 左邊的 `this.name` 是成員變數，右邊的 `name` 是參數
B. 左右兩邊都是參數
C. 左右兩邊都是成員變數
D. `this.name` 會建立一個新的區域變數

## 你的答案

```text
A
```

## 正確答案

```text
A
```

## 判定

正確。

## 詳解

這行：

```java
this.name = name;
```

左邊：

```java
this.name
```

代表目前物件的成員變數（Field / 字段）。

右邊：

```java
name
```

代表建構子的參數（Parameter / 参数）。

因為參數名稱跟欄位名稱一樣，所以如果你只寫：

```java
name = name;
```

左右兩邊都會優先指向參數，成員變數不會被設定。

所以要用：

```java
this.name = name;
```

明確表示：

```text
把參數 name 的值，存進目前物件的成員變數 name。
```

## 考得觀念

- 成員變數與參數同名時，參數會遮蔽成員變數
- `this.field` 用來明確指定目前物件的欄位
- 這是 Java constructor 很常見的寫法

## 面試常問

### Q：為什麼 constructor 裡常看到 `this.xxx = xxx`？

因為 constructor 參數常跟 field 同名。`this.xxx` 指的是物件欄位，右邊的 `xxx` 指的是參數，用來把外部傳入的值存到物件狀態中。

---

# Q11. `this(...)` 呼叫建構子

## 完整問題

下面程式是否合法？

```java
class Flower {
    Flower(int petals) {
        System.out.println(petals);
    }

    Flower() {
        this(47);
        System.out.println("no args");
    }
}
```

A. 合法，`this(47)` 會呼叫同一個 class 中符合參數的另一個建構子
B. 不合法，`this` 不能用在建構子裡
C. 不合法，建構子不能呼叫建構子
D. 合法，但 `this(47)` 會建立另一個新的 Flower 物件

## 你的答案

```text
D（NOT SURE）
```

## 正確答案

```text
A
```

## 判定

錯誤。

## 詳解

這題你的誤解很重要。

`this(47)` 的意思不是建立另一個新的 `Flower` 物件。

它的意思是：

```text
在目前這個物件的初始化流程中，呼叫同一個 class 裡另一個符合參數的建構子。
```

也就是：

```java
Flower() {
    this(47);
    System.out.println("no args");
}
```

會先呼叫：

```java
Flower(int petals)
```

然後再回來執行：

```java
System.out.println("no args");
```

整個過程仍然是在初始化同一個物件，不是建立兩個物件。

## 考得觀念

- `this(...)` 是建構子之間的呼叫
- `this(...)` 不等於 `new`
- 建構子串接是為了避免初始化邏輯重複

## 面試常問

### Q：`this(...)` 跟 `new Class(...)` 差在哪？

`this(...)` 是在同一個物件的建構流程中呼叫另一個建構子。
`new Class(...)` 是建立另一個全新的物件。

---

# Q12. `this(...)` 的限制

## 完整問題

下面程式為什麼不合法？

```java
class Flower {
    Flower(int petals) {
    }

    Flower(String s) {
    }

    Flower() {
        this(47);
        this("hi");
    }
}
```

A. 因為建構子裡不能印字串
B. 因為一個建構子裡只能用 `this(...)` 呼叫另一個建構子一次
C. 因為 `String` 不能當建構子參數
D. 因為無參建構子不能呼叫有參建構子

## 你的答案

```text
B
```

## 正確答案

```text
B
```

## 判定

正確。

## 詳解

一個建構子裡只能用一次 `this(...)` 呼叫另一個建構子。

原因是 `this(...)` 代表「把初始化流程交給另一個建構子先做」。如果同一個建構子可以呼叫兩個不同建構子，初始化流程就會變得混亂。

合法：

```java
Flower() {
    this(47);
}
```

不合法：

```java
Flower() {
    this(47);
    this("hi");
}
```

## 考得觀念

- 一個建構子只能呼叫另一個建構子一次
- `this(...)` 不是普通 method call
- 建構子串接必須形成清楚的初始化路徑

## 面試常問

### Q：一個 constructor 可以同時呼叫兩個 constructor 嗎？

不行。Java 規定一個 constructor 最多只能呼叫另一個 constructor 一次，而且必須放在第一行。

---

# Q13. `this(...)` 的位置

## 完整問題

下面程式為什麼不合法？

```java
class Flower {
    Flower(int petals) {
    }

    Flower() {
        System.out.println("start");
        this(47);
    }
}
```

A. 因為 `this(...)` 必須是建構子中的第一行
B. 因為 `System.out.println()` 不能放在建構子裡
C. 因為 `this(47)` 不能傳 int
D. 因為建構子一定要有回傳值

## 你的答案

```text
A
```

## 正確答案

```text
A
```

## 判定

正確。

## 詳解

`this(...)` 必須是建構子中的第一行。

不合法：

```java
Flower() {
    System.out.println("start");
    this(47);
}
```

合法：

```java
Flower() {
    this(47);
    System.out.println("start");
}
```

原因是 Java 要求建構子串接必須先決定初始化流程，再執行其他程式碼。

## 考得觀念

- `this(...)` 必須是 constructor 第一行
- 建構子串接優先於其他初始化邏輯
- `System.out.println()` 可以放建構子裡，但不能放在 `this(...)` 前面

## 面試常問

### Q：為什麼 `this(...)` 必須放第一行？

因為 Java 必須先完成另一個建構子的初始化流程，才能繼續執行目前建構子的其他程式碼，避免物件進入半初始化狀態。

---

# Q14. static 與 this

## 完整問題

為什麼 `static` 方法裡不能直接使用 `this`？

A. 因為 `static` 方法屬於 class，不屬於某個特定物件
B. 因為 `this` 只能用在 main 方法
C. 因為 `static` 方法不能有參數
D. 因為 `this` 是 C++ 的語法，不是 Java 的語法

## 你的答案

```text
A（NOT SURE）
```

## 正確答案

```text
A
```

## 判定

正確。

## 詳解

你答對了。

`static` 方法屬於 class，不屬於某個特定物件。

而 `this` 代表目前物件。

所以 static 方法裡沒有「目前物件」這個概念，也就不能直接使用 `this`。

例如：

```java
class Demo {
    static void f() {
        // System.out.println(this); // 不合法
    }
}
```

因為你可以這樣呼叫：

```java
Demo.f();
```

這個呼叫沒有建立任何 `Demo` 物件，所以 Java 不知道 `this` 是誰。

## 考得觀念

- static 屬於 class
- instance 屬於 object
- `this` 只存在於 instance context
- static context 沒有目前物件

## 面試常問

### Q：static method 為什麼不能直接存取 instance field？

因為 instance field 屬於某個物件，而 static method 可以在沒有任何物件的情況下被呼叫。沒有物件，就不知道要存取哪一份 instance field。

---

# Q15. static 方法呼叫非 static 方法

## 完整問題

下面程式為什麼不合法？

```java
class Demo {
    void instanceMethod() {
        System.out.println("instance");
    }

    static void staticMethod() {
        instanceMethod();
    }
}
```

A. 因為 `staticMethod()` 裡沒有任何特定物件，所以不能直接呼叫需要物件的 `instanceMethod()`
B. 因為 `static` 方法不能呼叫任何方法
C. 因為 `void` 方法不能被呼叫
D. 因為 `System.out.println()` 只能寫在 main 裡

## 你的答案

```text
B（NOT SURE）
```

## 正確答案

```text
A
```

## 判定

錯誤。

## 詳解

`static` 方法不是不能呼叫任何方法。

它可以呼叫：

```java
static method
```

例如：

```java
class Demo {
    static void helper() {
    }

    static void staticMethod() {
        helper(); // 合法
    }
}
```

但它不能直接呼叫 instance method，因為 instance method 必須透過某個物件呼叫。

錯誤：

```java
static void staticMethod() {
    instanceMethod();
}
```

正確：

```java
static void staticMethod() {
    Demo d = new Demo();
    d.instanceMethod();
}
```

因為這樣 Java 才知道你要呼叫哪一個物件的 `instanceMethod()`。

## 考得觀念

- static method 可以呼叫 static method
- static method 不能直接呼叫 instance method
- instance method 需要物件
- static context 沒有 `this`

## 面試常問

### Q：static method 可以呼叫 non-static method 嗎？

可以，但不能「直接」呼叫。你必須先有一個物件，再透過該物件呼叫 non-static method。

---

# Q16. 程式輸出題

## 完整問題

下面程式輸出什麼？

```java
class Rock {
    Rock(int i) {
        System.out.print("Rock " + i + " ");
    }
}

public class Main {
    public static void main(String[] args) {
        new Rock(1);
        new Rock(2);
        new Rock(3);
    }
}
```

A. `Rock Rock Rock`
B. `Rock 1 Rock 2 Rock 3`
C. `1 2 3`
D. 不會輸出，因為沒有變數接住物件

## 你的答案

```text
B（NOT SURE）
```

## 正確答案

```text
B
```

## 判定

正確。

## 詳解

你答對了。

雖然沒有變數接住物件，但只要執行：

```java
new Rock(1);
```

就會：

1. 建立物件
2. 呼叫 `Rock(int i)` 建構子
3. 執行裡面的 `System.out.print(...)`

所以三次 `new` 會輸出：

```text
Rock 1 Rock 2 Rock 3
```

沒有變數接住，只代表建立完後這些物件很快就變成沒有參考指向的物件，之後可能被垃圾回收。它不影響建構子當下的執行。

## 考得觀念

- `new` 會觸發建構子
- 不接住物件參考，建構子仍然會執行
- 物件是否被變數保存，不影響建立當下的輸出

## 面試常問

### Q：`new Object();` 沒有變數接住，物件有建立嗎？

有。物件仍會建立，建構子也會執行。只是建立後沒有參考保存它，之後會成為 GC eligible 的候選物件。

---

# Q17. 程式輸出題：建構子呼叫順序

## 完整問題

下面程式輸出順序是什麼？

```java
class Flower {
    Flower(int petals) {
        System.out.println("int constructor");
    }

    Flower(String s, int petals) {
        this(petals);
        System.out.println("String and int constructor");
    }

    Flower() {
        this("hi", 47);
        System.out.println("no-arg constructor");
    }
}

public class Main {
    public static void main(String[] args) {
        new Flower();
    }
}
```

A.

```text
no-arg constructor
String and int constructor
int constructor
```

B.

```text
int constructor
String and int constructor
no-arg constructor
```

C.

```text
String and int constructor
int constructor
no-arg constructor
```

D.

```text
int constructor
no-arg constructor
String and int constructor
```

## 你的答案

```text
A
```

## 正確答案

```text
B
```

## 判定

錯誤。

## 詳解

這題非常重要。

執行：

```java
new Flower();
```

先進入無參建構子：

```java
Flower() {
    this("hi", 47);
    System.out.println("no-arg constructor");
}
```

第一行是：

```java
this("hi", 47);
```

所以先跳去：

```java
Flower(String s, int petals) {
    this(petals);
    System.out.println("String and int constructor");
}
```

這裡第一行又是：

```java
this(petals);
```

所以再跳去：

```java
Flower(int petals) {
    System.out.println("int constructor");
}
```

因此第一個輸出：

```text
int constructor
```

然後回到 `Flower(String s, int petals)`，輸出：

```text
String and int constructor
```

最後回到 `Flower()`，輸出：

```text
no-arg constructor
```

所以正確順序是：

```text
int constructor
String and int constructor
no-arg constructor
```

## 考得觀念

- 建構子串接是「先一路呼叫進去，再一層一層回來」
- `this(...)` 必須先執行
- 輸出順序不是從外層建構子開始印

## 面試常問

### Q：Constructor chaining 的執行順序怎麼看？

先看最外層被 `new` 呼叫的 constructor，若第一行是 `this(...)`，就先跳到被呼叫的 constructor。一路跳到底後，才從最內層開始執行並一層層返回。

---

# Q18. 簡答題：建構子為什麼可以降低「忘記初始化」造成的錯誤？

## 完整問題

請用 2～4 句話回答：

```text
建構子為什麼可以降低「忘記初始化」造成的錯誤？
```

## 你的答案

```text
因為他會自動生成建構子
```

## 正確答案參考

建構子可以降低忘記初始化的錯誤，因為物件在 `new` 的時候會自動呼叫建構子。類別設計者可以把必要的初始化邏輯寫在建構子裡，讓使用者不能跳過初始化流程。這樣物件在被使用前，就能先進入合理狀態。

## 判定

部分錯誤。

你把重點放在「自動生成建構子」，但題目問的是「建構子為什麼能降低忘記初始化」。

自動生成無參建構子只是其中一個小規則，不是核心原因。

核心原因是：

```text
建構子會在 new 物件時自動執行，所以初始化流程不需要靠使用者記得手動呼叫 initialize()。
```

## 詳解

如果沒有建構子，可能要這樣：

```java
User u = new User();
u.initialize();
```

問題是使用者可能忘記呼叫：

```java
User u = new User();
// 忘記 u.initialize();
```

有建構子後：

```java
User u = new User("Zane");
```

初始化邏輯在 `new User("Zane")` 時自動發生，使用者不能跳過。

## 考得觀念

- constructor 的主要價值是保證初始化流程
- 不是靠使用者記得呼叫初始化方法
- 建構子把物件建立與初始化綁在一起

## 面試常問

### Q：為什麼 constructor 是 OOP 裡很重要的安全機制？

因為它讓物件在建立時就必須完成必要初始化，避免建立出狀態不完整的物件。這可以降低忘記呼叫初始化方法造成的 bug。

---

# Q19. 簡答題：方法多載是靠什麼區分不同方法？為什麼不能只靠回傳值？

## 完整問題

請用 2～4 句話回答：

```text
方法多載是靠什麼區分不同方法？為什麼不能只靠回傳值？
```

## 你的答案

```text
參數，不知道
```

## 正確答案參考

方法多載是靠方法名稱相同，但參數列表不同來區分，參數列表包含參數數量、型別、順序。不能只靠回傳值區分，因為呼叫方法時可以忽略回傳值，例如直接寫 `f();`，這時編譯器無法判斷要呼叫哪一個方法。

## 判定

部分正確。

你知道關鍵是「參數」，這是對的。
但你還缺兩塊：

1. 參數列表包含什麼
2. 為什麼不能靠回傳值

## 詳解

合法多載：

```java
void f(int x) {}
void f(String x) {}
void f(int x, String y) {}
void f(String y, int x) {}
```

這些都是靠參數列表不同區分。

不合法：

```java
void f() {}
int f() { return 1; }
```

因為如果有人寫：

```java
f();
```

Java 不知道你要呼叫 `void f()` 還是 `int f()`。

## 考得觀念

- 多載依靠參數列表
- 參數列表包含數量、型別、順序
- 回傳型別不能單獨決定多載

## 面試常問

### Q：Overloading 可以只改 return type 嗎？

不可以。只改 return type 不構成合法 overloading，因為呼叫端可能忽略回傳值，編譯器無法決定呼叫哪個方法。

---

# Q20. 簡答題：this 和 static 的差別是什麼？

## 完整問題

請用 2～4 句話回答：

```text
this 和 static 的差別是什麼？
```

## 你的答案

```text
不知道
```

## 正確答案參考

`this` 代表目前正在操作的物件，只能用在非 static 的 instance 方法或建構子中。`static` 代表成員屬於 class 本身，不屬於某一個特定物件。因為 static context 沒有目前物件，所以 static 方法裡不能直接使用 `this`，也不能直接呼叫非 static 方法。

## 判定

錯誤，但這是目前最值得補的核心觀念。

## 詳解

看這個：

```java
class User {
    String name;

    void printName() {
        System.out.println(this.name);
    }

    static void printType() {
        System.out.println("User type");
    }
}
```

`name` 是每個物件各自有一份。

```java
User u1 = new User();
User u2 = new User();
```

`u1.name` 和 `u2.name` 可以不同。

所以 `this.name` 必須知道目前是哪一個物件。

但 `static` 方法可以不用建立物件就呼叫：

```java
User.printType();
```

這時根本沒有 `u1` 或 `u2`，所以 static 方法裡沒有 `this`。

## 考得觀念

- `this` = 目前物件
- `static` = class 層級
- static 方法沒有目前物件
- instance 方法需要物件才能被呼叫

## 面試常問

### Q：static method 和 instance method 的差別？

static method 屬於 class，可以透過 class name 呼叫，不需要物件。instance method 屬於物件，必須透過某個物件呼叫，方法內部可以使用 `this` 指向目前物件。

---

# 錯題集中整理

## 你錯的題目

```text
Q9
Q11
Q15
Q17
Q18
Q19
Q20
```

## 錯題核心

| 題號 | 錯誤點                       | 正確觀念                                        |
| ---- | ---------------------------- | ----------------------------------------------- |
| Q9   | 以為 `this` 是目前方法       | `this` 是目前物件                               |
| Q11  | 以為 `this(47)` 會建立新物件 | `this(...)` 是呼叫同物件的另一個建構子          |
| Q15  | 以為 static 不能呼叫任何方法 | static 可以呼叫 static，但不能直接呼叫 instance |
| Q17  | 建構子串接順序看反           | 先一路呼叫到最內層，再往外執行                  |
| Q18  | 把建構子重點放成自動生成     | 重點是 `new` 時自動執行初始化                   |
| Q19  | 只知道參數，不知道完整原因   | 多載靠參數列表，不能靠回傳值                    |
| Q20  | 不知道 `this` 與 static 差異 | `this` 是物件層級，static 是 class 層級         |

---

# 這次最該補的 4 個觀念

## 1. `this` 是目前物件，不是目前方法

記成：

```text
誰呼叫 instance method，this 就是誰。
```

例如：

```java
u1.print();
```

方法裡：

```text
this = u1
```

---

## 2. `this(...)` 不是 new

記成：

```text
this(...) = 同一個物件內，建構子呼叫建構子
new (...) = 建立新物件
```

---

## 3. static 沒有 this

記成：

```text
static 屬於 class
this 屬於 object
```

所以 static 裡不能直接用 this。

---

## 4. 建構子串接順序

記成：

```text
先鑽到底，再一層一層回來。
```

例如：

```java
Flower()
→ Flower(String, int)
→ Flower(int)
```

輸出從最內層開始：

```text
Flower(int)
Flower(String, int)
Flower()
```

---
