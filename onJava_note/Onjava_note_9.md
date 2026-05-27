# 第九章 多型（Polymorphism / 多态）

> 「如果把錯誤的數字輸入機器，會得到正確結果嗎？」  
> —— Charles Babbage

多型（Polymorphism / 多态）是物件導向程式設計（Object-Oriented Programming, OOP / 面向对象编程）中，非常核心的一個觀念。

前面已經學過：

- 資料抽象（Data Abstraction / 数据抽象）
- 繼承（Inheritance / 继承）
- 封裝（Encapsulation / 封装）

而多型的重點是：

> **讓同一段程式碼可以面對不同子類別物件，並在執行時自動呼叫正確的方法。**

白話說：

```java
Instrument i = new Wind();
i.play();
```

雖然變數型別是 `Instrument`，但真正物件是 `Wind`，所以執行時會呼叫 `Wind` 的 `play()`。

這就是多型的核心。

---

# 1. 多型解決什麼問題？

多型讓我們可以把：

```text
「做什麼」
```

和：

```text
「怎麼做」
```

分開。

例如：

```java
tune(instrument);
```

這段程式只需要知道：

> 我拿到的是一個樂器（Instrument）。

它不需要知道這個樂器到底是：

- 長笛（Wind）
- 弦樂器（Stringed）
- 銅管樂器（Brass）

真正要怎麼演奏，交給各子類別自己決定。

---

# 2. 向上轉型（Upcasting / 向上转型）回顧

向上轉型（Upcasting / 向上转型）指的是：

> 把子類別物件，當成父類別型別使用。

例如：

```java
Instrument i = new Wind();
```

這裡：

- `Wind` 是子類別
- `Instrument` 是父類別
- `new Wind()` 建立的是 Wind 物件
- 但變數 `i` 的型別是 Instrument

這是合法的，因為：

```text
Wind is an Instrument
Wind 是一種 Instrument
```

---

## 範例：樂器與演奏

先定義一個 `Note` 列舉（enum / 枚举）：

```java
package polymorphism.music;

public enum Note {
    MIDDLE_C, C_SHARP, B_FLAT
}
```

`Instrument` 是父類別：

```java
package polymorphism.music;

class Instrument {
    public void play(Note n) {
        System.out.println("Instrument.play()");
    }
}
```

`Wind` 是子類別，並覆寫（Override / 重写）父類別的 `play()`：

```java
package polymorphism.music;

public class Wind extends Instrument {
    @Override
    public void play(Note n) {
        System.out.println("Wind.play() " + n);
    }
}
```

主程式：

```java
package polymorphism.music;

public class Music {
    public static void tune(Instrument i) {
        i.play(Note.MIDDLE_C);
    }

    public static void main(String[] args) {
        Wind flute = new Wind();
        tune(flute); // Upcasting
    }
}
```

輸出：

```text
Wind.play() MIDDLE_C
```

---

## 這裡發生什麼事？

`flute` 是 `Wind` 型別。

但 `tune()` 接收的是：

```java
Instrument i
```

所以呼叫：

```java
tune(flute);
```

時，`Wind` 會自動向上轉型成 `Instrument`。

可是最後執行的是：

```java
Wind.play()
```

不是：

```java
Instrument.play()
```

原因就是：

> Java 的一般 instance method 會使用動態繫結（Dynamic Binding / 动态绑定）。

---

# 3. 為什麼要「忘掉」具體型別？

一開始你可能會覺得奇怪：

> 既然物件是 `Wind`，那 `tune()` 直接接收 `Wind` 不就好了？

例如：

```java
public static void tune(Wind i) {
    i.play(Note.MIDDLE_C);
}
```

這樣當然可以。

但問題是：如果後來多了更多樂器呢？

```java
class Stringed extends Instrument {
    @Override
    public void play(Note n) {
        System.out.println("Stringed.play() " + n);
    }
}

class Brass extends Instrument {
    @Override
    public void play(Note n) {
        System.out.println("Brass.play() " + n);
    }
}
```

如果不用多型，就要寫很多個 `tune()`：

```java
public static void tune(Wind i) {
    i.play(Note.MIDDLE_C);
}

public static void tune(Stringed i) {
    i.play(Note.MIDDLE_C);
}

public static void tune(Brass i) {
    i.play(Note.MIDDLE_C);
}
```

這樣會有幾個問題：

| 問題           | 說明                                     |
| -------------- | ---------------------------------------- |
| 重複程式碼變多 | 每一種樂器都要寫一個 `tune()`            |
| 擴充困難       | 新增一種樂器，就要新增一個方法           |
| 容易漏寫       | 編譯器不一定會提醒你「少支援某一種樂器」 |
| 耦合變高       | `tune()` 必須知道太多具體子類別          |

比較好的做法是：

```java
public static void tune(Instrument i) {
    i.play(Note.MIDDLE_C);
}
```

也就是：

> 程式只依賴父類別介面，真正行為由子類別決定。

這就是多型的價值。

---

# 4. 方法呼叫繫結（Method Binding / 方法绑定）

繫結（Binding / 绑定）指的是：

> 把一個方法呼叫，連到實際要執行的方法本體。

例如：

```java
i.play(Note.MIDDLE_C);
```

Java 必須決定：

到底要執行哪一個 `play()`？

---

## 4.1 前期繫結（Early Binding / 前期绑定）

前期繫結指的是：

> 在程式執行前，通常是編譯階段，就決定要呼叫哪個方法。

C 語言大多就是這種模式。

---

## 4.2 後期繫結 / 動態繫結（Late Binding / Dynamic Binding / 后期绑定 / 动态绑定）

後期繫結指的是：

> 到程式執行時，根據實際物件型別，決定要呼叫哪個方法。

例如：

```java
Instrument i = new Wind();
i.play(Note.MIDDLE_C);
```

編譯器看到的是：

```java
Instrument i
```

但執行時 JVM 看到的是：

```java
new Wind()
```

所以會呼叫：

```java
Wind.play()
```

---

# 5. Java 中哪些方法會多型？

Java 中，一般情況下：

> **非 static、非 final、非 private 的 instance method，都會使用動態繫結。**

簡化成表格：

| 成員                 | 是否具有多型？ | 說明                            |
| -------------------- | -------------: | ------------------------------- |
| 一般 instance method |             會 | 最常見的多型來源                |
| `static` method      |           不會 | static 屬於 class，不屬於物件   |
| `final` method       |     不會被覆寫 | 因為禁止 override               |
| `private` method     |           不會 | 子類別看不到，等同不能 override |
| field 欄位           |           不會 | 欄位存取看變數宣告型別          |

---

# 6. Shape 範例：多型的典型模型

多型常用 Shape 例子說明。

概念是：

```text
Circle is a Shape
Square is a Shape
Triangle is a Shape
```

也就是：

```java
Shape s = new Circle();
s.draw();
```

雖然 `s` 的宣告型別是 `Shape`，但實際物件是 `Circle`，所以會呼叫 `Circle.draw()`。

---

## Shape 父類別

```java
package polymorphism.shape;

public class Shape {
    public void draw() {}
    public void erase() {}
}
```

---

## 子類別

```java
package polymorphism.shape;

public class Circle extends Shape {
    @Override
    public void draw() {
        System.out.println("Circle.draw()");
    }

    @Override
    public void erase() {
        System.out.println("Circle.erase()");
    }
}
```

```java
package polymorphism.shape;

public class Square extends Shape {
    @Override
    public void draw() {
        System.out.println("Square.draw()");
    }

    @Override
    public void erase() {
        System.out.println("Square.erase()");
    }
}
```

```java
package polymorphism.shape;

public class Triangle extends Shape {
    @Override
    public void draw() {
        System.out.println("Triangle.draw()");
    }

    @Override
    public void erase() {
        System.out.println("Triangle.erase()");
    }
}
```

---

## 隨機產生 Shape

```java
package polymorphism.shape;

import java.util.Random;

public class RandomShapes {
    private Random rand = new Random(47);

    public Shape get() {
        switch (rand.nextInt(3)) {
            default:
            case 0:
                return new Circle();
            case 1:
                return new Square();
            case 2:
                return new Triangle();
        }
    }

    public Shape[] array(int size) {
        Shape[] shapes = new Shape[size];

        for (int i = 0; i < shapes.length; i++) {
            shapes[i] = get();
        }

        return shapes;
    }
}
```

注意：

```java
return new Circle();
```

這裡實際建立的是 `Circle`，但方法回傳型別是：

```java
Shape
```

所以發生了向上轉型。

---

## 主程式

```java
import polymorphism.shape.*;

public class Shapes {
    public static void main(String[] args) {
        RandomShapes gen = new RandomShapes();

        for (Shape shape : gen.array(9)) {
            shape.draw();
        }
    }
}
```

可能輸出：

```text
Triangle.draw()
Triangle.draw()
Square.draw()
Triangle.draw()
Square.draw()
Triangle.draw()
Square.draw()
Triangle.draw()
Circle.draw()
```

---

## 本例重點

`for` 迴圈裡的變數型別是：

```java
Shape shape
```

但每一個元素實際可能是：

- `Circle`
- `Square`
- `Triangle`

呼叫：

```java
shape.draw();
```

時，Java 會在執行時自動選擇正確版本。

這就是多型。

---

# 7. 多型讓程式更容易擴充

回到樂器例子。

如果 `tune()` 寫成：

```java
public static void tune(Instrument i) {
    i.play(Note.MIDDLE_C);
}
```

那以後新增：

```java
class Percussion extends Instrument
class Woodwind extends Wind
class Brass extends Wind
```

`Music.tune()` 都不用改。

這叫做：

> 程式依賴穩定的父類別介面，而不是依賴每個具體子類別。

這對工程設計很重要。

---

## 簡化範例

```java
class Instrument {
    void play(Note n) {
        System.out.println("Instrument.play() " + n);
    }

    String what() {
        return "Instrument";
    }

    void adjust() {
        System.out.println("Adjusting Instrument");
    }
}
```

```java
class Wind extends Instrument {
    @Override
    void play(Note n) {
        System.out.println("Wind.play() " + n);
    }

    @Override
    String what() {
        return "Wind";
    }

    @Override
    void adjust() {
        System.out.println("Adjusting Wind");
    }
}
```

```java
class Percussion extends Instrument {
    @Override
    void play(Note n) {
        System.out.println("Percussion.play() " + n);
    }

    @Override
    String what() {
        return "Percussion";
    }

    @Override
    void adjust() {
        System.out.println("Adjusting Percussion");
    }
}
```

主程式：

```java
public class Music3 {
    public static void tune(Instrument i) {
        i.play(Note.MIDDLE_C);
    }

    public static void tuneAll(Instrument[] instruments) {
        for (Instrument i : instruments) {
            tune(i);
        }
    }

    public static void main(String[] args) {
        Instrument[] orchestra = {
            new Wind(),
            new Percussion(),
            new Stringed(),
            new Brass(),
            new Woodwind()
        };

        tuneAll(orchestra);
    }
}
```

輸出會依照每個實際物件呼叫自己的 `play()`。

---

# 8. 陷阱一：private 方法不能被真正覆寫

看這個例子：

```java
public class PrivateOverride {
    private void f() {
        System.out.println("private f()");
    }

    public static void main(String[] args) {
        PrivateOverride po = new Derived();
        po.f();
    }
}

class Derived extends PrivateOverride {
    public void f() {
        System.out.println("public f()");
    }
}
```

輸出：

```text
private f()
```

---

## 為什麼不是 public f()？

因為：

```java
private void f()
```

在子類別中看不到。

所以 `Derived` 裡面的：

```java
public void f()
```

不是 override，而是一個全新的方法。

也就是：

```text
父類別 private f()
子類別 public f()
```

這兩個方法沒有多型關係。

---

## 正確防呆：使用 @Override

如果你寫：

```java
class Derived2 extends PrivateOverride2 {
    @Override
    public void f() {
        System.out.println("public f()");
    }
}
```

編譯器會報錯：

```text
method does not override or implement a method from a supertype
```

所以實務上：

> 只要你以為自己在覆寫，就應該加 `@Override`。

這是 Java 面試與實務都很重要的習慣。

---

# 9. 陷阱二：欄位沒有多型

欄位（Field / 字段）不是方法。

欄位存取在編譯時就決定了，不會根據實際物件型別動態選擇。

---

## 範例

```java
class Super {
    public int field = 0;

    public int getField() {
        return field;
    }
}

class Sub extends Super {
    public int field = 1;

    @Override
    public int getField() {
        return field;
    }

    public int getSuperField() {
        return super.field;
    }
}

public class FieldAccess {
    public static void main(String[] args) {
        Super sup = new Sub();

        System.out.println(
            "sup.field = " + sup.field +
            ", sup.getField() = " + sup.getField()
        );

        Sub sub = new Sub();

        System.out.println(
            "sub.field = " + sub.field +
            ", sub.getField() = " + sub.getField() +
            ", sub.getSuperField() = " + sub.getSuperField()
        );
    }
}
```

輸出：

```text
sup.field = 0, sup.getField() = 1
sub.field = 1, sub.getField() = 1, sub.getSuperField() = 0
```

---

## 怎麼理解？

這行：

```java
Super sup = new Sub();
```

實際物件是 `Sub`。

但變數宣告型別是 `Super`。

所以：

```java
sup.field
```

看的是 `Super.field`。

但：

```java
sup.getField()
```

是方法呼叫，會使用多型，所以呼叫的是 `Sub.getField()`。

---

## 結論

| 寫法             | 結果               |
| ---------------- | ------------------ |
| `sup.field`      | 看宣告型別 `Super` |
| `sup.getField()` | 看實際物件 `Sub`   |

實務上通常會把欄位設成 `private`，透過方法存取，所以比較少直接遇到這種問題。

---

# 10. 陷阱三：static 方法沒有多型

`static` 方法屬於 class，不屬於物件。

所以 static method 不會多型。

---

## 範例

```java
class StaticSuper {
    public static String staticGet() {
        return "Base staticGet()";
    }

    public String dynamicGet() {
        return "Base dynamicGet()";
    }
}

class StaticSub extends StaticSuper {
    public static String staticGet() {
        return "Derived staticGet()";
    }

    @Override
    public String dynamicGet() {
        return "Derived dynamicGet()";
    }
}

public class StaticPolymorphism {
    public static void main(String[] args) {
        StaticSuper sup = new StaticSub();

        System.out.println(StaticSuper.staticGet());
        System.out.println(sup.dynamicGet());
    }
}
```

輸出：

```text
Base staticGet()
Derived dynamicGet()
```

---

## 結論

| 成員           | 是否多型 |
| -------------- | -------: |
| `staticGet()`  |       否 |
| `dynamicGet()` |       是 |

static method 應該用 class name 呼叫：

```java
StaticSuper.staticGet();
```

不要用物件變數呼叫 static method，雖然 Java 允許，但可讀性不好。

---

# 11. 建構子與多型（Constructors and Polymorphism / 构造器和多态）

建構子（Constructor / 构造函数）不是一般方法。

建構子本身沒有多型。

但要注意：

> 如果建構子裡面呼叫了可被覆寫的方法，可能會觸發多型，造成危險行為。

這是本章重要陷阱。

---

# 12. 建構子呼叫順序

建立子類別物件時，Java 一定會先建立父類別部分。

順序大致是：

```text
1. 父類別建構子
2. 子類別欄位初始化
3. 子類別建構子本體
```

如果有多層繼承，會從最上層父類別一路往下。

---

## 範例：Sandwich

```java
class Meal {
    Meal() {
        System.out.println("Meal()");
    }
}

class Bread {
    Bread() {
        System.out.println("Bread()");
    }
}

class Cheese {
    Cheese() {
        System.out.println("Cheese()");
    }
}

class Lettuce {
    Lettuce() {
        System.out.println("Lettuce()");
    }
}

class Lunch extends Meal {
    Lunch() {
        System.out.println("Lunch()");
    }
}

class PortableLunch extends Lunch {
    PortableLunch() {
        System.out.println("PortableLunch()");
    }
}

public class Sandwich extends PortableLunch {
    private Bread b = new Bread();
    private Cheese c = new Cheese();
    private Lettuce l = new Lettuce();

    public Sandwich() {
        System.out.println("Sandwich()");
    }

    public static void main(String[] args) {
        new Sandwich();
    }
}
```

輸出：

```text
Meal()
Lunch()
PortableLunch()
Bread()
Cheese()
Lettuce()
Sandwich()
```

---

## 本例重點

建立 `Sandwich` 時：

```text
Meal()
→ Lunch()
→ PortableLunch()
→ Bread()
→ Cheese()
→ Lettuce()
→ Sandwich()
```

也就是：

1. 先初始化父類別
2. 再初始化目前類別的欄位
3. 最後執行目前類別建構子內容

---

# 13. 繼承與清理：現代 Java 觀點

原文這裡有一大段討論 `dispose()` 與引用計數。

這在理解物件生命週期上有幫助，但在現代 Java 裡，日常開發比較少手動管理物件釋放，因為一般物件會交給垃圾回收（Garbage Collection, GC / 垃圾回收）。

但仍然要知道：

> GC 只負責回收記憶體，不一定會幫你關閉外部資源。

例如：

- 檔案
- Socket
- Database connection
- Stream
- Thread pool
- 外部系統連線

這些資源需要明確關閉。

現代 Java 更常用：

```java
try-with-resources
```

或框架生命週期管理，而不是手寫複雜的引用計數。

---

## 清理順序原則

如果真的有繼承層級下的清理流程，通常順序是：

```text
先清理子類別
再清理父類別
```

因為子類別可能還需要用到父類別提供的狀態或方法。

簡化成：

```java
@Override
protected void dispose() {
    // 先清理子類別自己的資源

    super.dispose(); // 最後再交給父類別清理
}
```

---

# 14. 建構子內呼叫多型方法的陷阱

這是非常重要的一節。

先看程式：

```java
class Glyph {
    void draw() {
        System.out.println("Glyph.draw()");
    }

    Glyph() {
        System.out.println("Glyph() before draw()");
        draw();
        System.out.println("Glyph() after draw()");
    }
}

class RoundGlyph extends Glyph {
    private int radius = 1;

    RoundGlyph(int r) {
        radius = r;
        System.out.println("RoundGlyph.RoundGlyph(), radius = " + radius);
    }

    @Override
    void draw() {
        System.out.println("RoundGlyph.draw(), radius = " + radius);
    }
}

public class PolyConstructors {
    public static void main(String[] args) {
        new RoundGlyph(5);
    }
}
```

輸出：

```text
Glyph() before draw()
RoundGlyph.draw(), radius = 0
Glyph() after draw()
RoundGlyph.RoundGlyph(), radius = 5
```

---

## 為什麼 radius 是 0，不是 1 或 5？

建立：

```java
new RoundGlyph(5);
```

時，流程是：

```text
1. 物件記憶體先被清成 0 / null / false
2. 呼叫父類別 Glyph 建構子
3. Glyph 建構子裡呼叫 draw()
4. 因為多型，實際呼叫 RoundGlyph.draw()
5. 但 RoundGlyph 的欄位初始化還沒完成
6. 所以 radius 還是預設值 0
7. 之後才執行 radius = 1
8. 最後才執行 RoundGlyph 建構子，把 radius 改成 5
```

---

## 這是什麼問題？

`Glyph` 建構子裡面呼叫：

```java
draw();
```

但 `draw()` 被子類別覆寫。

所以父類別建構子還沒跑完，就呼叫到了子類別的方法。

這時子類別還沒初始化完成，容易出現錯誤。

---

## 實務規則

> **建構子裡盡量不要呼叫可以被覆寫的方法。**

也就是避免在 constructor 裡呼叫非 `private`、非 `final` 的 instance method。

比較安全的是：

- 呼叫 `private` 方法
- 呼叫 `final` 方法
- 只做必要欄位初始化
- 複雜初始化交給 factory method 或初始化流程處理

這是面試高頻陷阱。

---

# 15. 協變回傳型別（Covariant Return Type / 协变返回类型）

協變回傳型別指的是：

> 子類別覆寫父類別方法時，回傳型別可以是父類別回傳型別的子類別。

---

## 範例

```java
class Grain {
    @Override
    public String toString() {
        return "Grain";
    }
}

class Wheat extends Grain {
    @Override
    public String toString() {
        return "Wheat";
    }
}

class Mill {
    Grain process() {
        return new Grain();
    }
}

class WheatMill extends Mill {
    @Override
    Wheat process() {
        return new Wheat();
    }
}

public class CovariantReturn {
    public static void main(String[] args) {
        Mill m = new Mill();
        Grain g = m.process();
        System.out.println(g);

        m = new WheatMill();
        g = m.process();
        System.out.println(g);
    }
}
```

輸出：

```text
Grain
Wheat
```

---

## 重點

父類別：

```java
Grain process()
```

子類別：

```java
Wheat process()
```

這是合法的，因為：

```text
Wheat is a Grain
```

也就是 `Wheat` 是 `Grain` 的子類別。

---

# 16. 使用繼承設計：不要一開始就選繼承

學會多型之後，很容易想把所有東西都做成繼承。

但這通常不是好設計。

更穩定的原則是：

> **優先使用組合（Composition / 组合），需要表達 is-a 關係時才使用繼承。**

---

# 17. 組合比繼承更彈性

範例：

```java
class Actor {
    public void act() {}
}

class HappyActor extends Actor {
    @Override
    public void act() {
        System.out.println("HappyActor");
    }
}

class SadActor extends Actor {
    @Override
    public void act() {
        System.out.println("SadActor");
    }
}

class Stage {
    private Actor actor = new HappyActor();

    public void change() {
        actor = new SadActor();
    }

    public void performPlay() {
        actor.act();
    }
}

public class Transmogrify {
    public static void main(String[] args) {
        Stage stage = new Stage();

        stage.performPlay();
        stage.change();
        stage.performPlay();
    }
}
```

輸出：

```text
HappyActor
SadActor
```

---

## 這裡用到什麼設計？

`Stage` 裡面有一個欄位：

```java
private Actor actor
```

一開始指向：

```java
new HappyActor()
```

後來改成：

```java
new SadActor()
```

所以 `Stage` 的行為在執行時改變了。

這是組合帶來的彈性。

---

## 設計原則

可以簡化成：

```text
使用繼承表達行為差異
使用欄位表達狀態變化
```

或更常見的說法：

> **組合優於繼承（Composition over Inheritance / 组合优于继承）。**

---

# 18. 替代 vs 擴展

繼承有兩種常見設計方式。

---

## 18.1 純粹替代：is-a

這種情況下，子類別完全可以替代父類別。

例如：

```text
Circle is a Shape
Wind is an Instrument
```

子類別主要是覆寫父類別已有的方法。

這種設計最適合多型。

---

## 18.2 擴展：is-like-a

有時子類別不只覆寫父類別方法，還新增自己的方法。

例如：

```java
class Useful {
    public void f() {}
    public void g() {}
}

class MoreUseful extends Useful {
    @Override
    public void f() {}

    @Override
    public void g() {}

    public void u() {}
    public void v() {}
    public void w() {}
}
```

`MoreUseful` 仍然是一種 `Useful`，但它有更多功能。

問題是：

```java
Useful x = new MoreUseful();
```

這時你只能呼叫：

```java
x.f();
x.g();
```

不能直接呼叫：

```java
x.u();
```

因為 `u()` 不在 `Useful` 的介面裡。

---

# 19. 向下轉型（Downcasting / 向下转型）與 RTTI

向上轉型會讓你失去具體型別資訊。

例如：

```java
Useful x = new MoreUseful();
```

你現在只知道 `x` 是 `Useful`，不能直接用 `MoreUseful` 的新方法。

如果要拿回具體型別，就需要向下轉型（Downcasting / 向下转型）：

```java
((MoreUseful) x).u();
```

---

## RTTI 是什麼？

RTTI 是：

```text
Runtime Type Information
執行期型別資訊 / 运行时类型信息
```

意思是：

> Java 會在執行時檢查物件真正的型別。

---

## 範例

```java
class Useful {
    public void f() {}
    public void g() {}
}

class MoreUseful extends Useful {
    @Override
    public void f() {}

    @Override
    public void g() {}

    public void u() {}
    public void v() {}
    public void w() {}
}

public class RTTI {
    public static void main(String[] args) {
        Useful[] x = {
            new Useful(),
            new MoreUseful()
        };

        x[0].f();
        x[1].g();

        // x[1].u(); // 編譯錯誤，因為 x[1] 的宣告型別是 Useful

        ((MoreUseful) x[1]).u(); // 正確，因為 x[1] 實際上是 MoreUseful

        ((MoreUseful) x[0]).u(); // 執行時錯誤
    }
}
```

最後一行會丟出：

```text
ClassCastException
```

原因是：

```java
x[0]
```

實際物件是：

```java
new Useful()
```

不是 `MoreUseful`。

所以不能硬轉。

---

## 向下轉型的風險

向上轉型通常安全：

```java
Useful u = new MoreUseful();
```

因為 `MoreUseful` 一定有 `Useful` 的方法。

但向下轉型不一定安全：

```java
MoreUseful m = (MoreUseful) someUseful;
```

你必須確認 `someUseful` 實際上真的是 `MoreUseful`。

否則會出現：

```java
ClassCastException
```

---

## 實務建議

向下轉型不要濫用。

如果你一直需要向下轉型，通常代表設計可能有問題：

- 父類別介面設計不夠完整
- 子類別責任切分不清楚
- 可能應該改用介面
- 可能應該改用組合
- 可能應該用多型方法，而不是到處判斷型別

---

# 20. 本章重點整理

## 20.1 多型是什麼？

多型就是：

> 同一個父類別型別的參考，在執行時可以指向不同子類別物件，並自動呼叫對應子類別的方法。

例如：

```java
Instrument i = new Wind();
i.play();
```

執行的是：

```java
Wind.play()
```

---

## 20.2 多型成立的條件

通常需要：

```text
1. 有繼承或介面關係
2. 子類別覆寫父類別方法
3. 使用父類別型別接住子類別物件
4. 呼叫被覆寫的方法
```

---

## 20.3 多型的價值

多型可以讓程式：

| 價值         | 說明                                          |
| ------------ | --------------------------------------------- |
| 更容易擴充   | 新增子類別，不一定要改舊程式                  |
| 降低耦合     | 使用者只依賴父類別或介面                      |
| 程式更穩定   | 不需要到處判斷具體型別                        |
| 符合工程設計 | 常用於 framework、plugin、service abstraction |

---

## 20.4 常見陷阱

| 陷阱                         | 結論                                     |
| ---------------------------- | ---------------------------------------- |
| private method               | 不能真正 override                        |
| static method                | 沒有多型                                 |
| field                        | 沒有多型                                 |
| constructor 裡呼叫可覆寫方法 | 危險，可能呼叫到未初始化完成的子類別方法 |
| 向下轉型                     | 可能發生 `ClassCastException`            |

---

# 21. 面試高頻觀念

這章非常常考，尤其是以下題型。

---

## Q1：Override 和 Overload 差在哪？

| 項目     | Override 覆寫     | Overload 多載                    |
| -------- | ----------------- | -------------------------------- |
| 中文     | 覆寫              | 多載                             |
| 英文     | Method Overriding | Method Overloading               |
| 簡中     | 方法重写          | 方法重载                         |
| 發生位置 | 父子類別之間      | 同一類別或父子類別都可能         |
| 方法名稱 | 一樣              | 一樣                             |
| 參數列表 | 必須一樣          | 必須不同                         |
| 回傳型別 | 相同或協變回傳    | 可不同，但不能只靠回傳型別區分   |
| 是否多型 | 是                | 不是 runtime polymorphism 的核心 |

---

## Q2：Java 的多型是編譯時決定還是執行時決定？

一般 instance method 是：

```text
執行時決定
```

也就是：

```text
Dynamic Binding / 動態繫結 / 动态绑定
```

---

## Q3：static 方法可以被 override 嗎？

不可以。

static method 可以在子類別中宣告同名方法，但那不是 override，而是 hiding。

所以 static method 不具備多型。

---

## Q4：private 方法可以被 override 嗎？

不可以。

因為子類別看不到父類別的 private method。

子類別如果寫同名方法，只是新增一個方法，不是 override。

---

## Q5：field 有多型嗎？

沒有。

field access 看的是：

```text
變數宣告型別
```

method call 看的是：

```text
實際物件型別
```

---

## Q6：為什麼 constructor 裡不建議呼叫可覆寫方法？

因為可能在子類別尚未初始化完成時，就呼叫到子類別覆寫的方法。

這可能導致欄位還是預設值：

```text
0 / null / false
```

造成隱藏 bug。

---

# 22. 本章工程意義

多型不是單純語法，而是 backend 工程裡非常常見的設計基礎。

例如 Spring Boot 中常見：

```java
interface PaymentService {
    void pay();
}
```

不同實作：

```java
class CreditCardPaymentService implements PaymentService
class LinePayPaymentService implements PaymentService
class ApplePayPaymentService implements PaymentService
```

呼叫端只依賴：

```java
PaymentService
```

不需要知道具體是哪種付款方式。

這就是多型在工程上的價值：

```text
用穩定抽象隔離變動實作
```

也就是：

> 讓會變的東西藏在子類別或實作類別裡，讓主要流程依賴穩定介面。

---

# 23. 本章一句話總結

多型的核心是：

```text
父類別型別負責接住物件，
實際子類別決定執行行為。
```

更工程化一點說：

```text
Program to an interface / base type,
let runtime dispatch choose the actual implementation.
```

中文白話：

> 程式不要一直綁死具體類別，而是依賴共同的父類別或介面，讓真正的物件在執行時做正確的事。

# 第 9 章多型測驗批改完整版

## 總分概況

| 區塊            |                 結果 |
| --------------- | -------------------: |
| Q1～Q10 選擇題  |              10 / 10 |
| Q11～Q15 是非題 |                5 / 5 |
| Q16 程式輸出    |                 正確 |
| Q17 程式輸出    |               部分錯 |
| Q18 程式輸出    | 觀念對，但輸出不完整 |
| Q19 簡答        |                 正確 |
| Q20 簡答        |           前兩項反了 |
| Q21 簡答        |             大致正確 |
| Q22 簡答        |       方向對，但太短 |

---

# Q1

## 完整問題

Java 多型的核心是什麼？

## 完整選項

A. 一個 class 可以有很多 constructor
B. 同一個父類別型別的參考，可以在執行時呼叫不同子類別覆寫後的方法
C. 一個 method 可以有很多不同參數版本
D. 一個 class 可以同時繼承多個 class

## 我的回答

B

## 正確答案

B

## 完整詳解

多型（Polymorphism / 多态）的核心是：

```text
父類別型別的變數，可以接住子類別物件。
呼叫被 override 的 method 時，執行時會依照真正的物件型別決定要跑哪個版本。
```

例如：

```java
Instrument i = new Wind();
i.play();
```

這裡：

```java
Instrument i
```

是父類別型別。

```java
new Wind()
```

是真正建立出來的子類別物件。

如果 `Wind` 覆寫了 `play()`，執行時會呼叫：

```java
Wind.play()
```

不是 `Instrument.play()`。

A 是 constructor overloading。
C 是 method overloading。
D 錯，Java class 不支援多重繼承 class。

## 本題觀念

```text
多型 = 父類別型別接住子類別物件，執行時呼叫子類別 override 後的方法。
```

## 面試高頻

高頻。這是 Java OOP 最基本題。

---

# Q2

## 完整問題

下列哪一個最接近「向上轉型（Upcasting / 向上转型）」？

## 完整選項

A.

```java
Wind w = new Instrument();
```

B.

```java
Instrument i = new Wind();
```

C.

```java
Wind w = (Wind) instrument;
```

D.

```java
Object o = "hello";
String s = (String) o;
```

## 我的回答

B

## 正確答案

B

## 完整詳解

向上轉型（Upcasting / 向上转型）是：

```text
把子類別物件，當成父類別型別使用。
```

所以：

```java
Instrument i = new Wind();
```

代表：

```text
Wind 是子類別物件
Instrument 是父類別型別
```

這是安全的，因為：

```text
Wind is an Instrument
Wind 是一種 Instrument
```

A 錯，不能直接把父類別物件指定給子類別變數。
C 是向下轉型（Downcasting / 向下转型）。
D 也是向下轉型，從 `Object` 轉回 `String`。

## 本題觀念

```text
Upcasting = 子類別 → 父類別。
```

## 面試高頻

高頻。常和 dynamic binding 一起考。

---

# Q3

## 完整問題

關於這段程式，哪個敘述正確？

```java
Instrument i = new Wind();
i.play();
```

假設 `Wind` 覆寫了 `Instrument` 的 `play()`。

## 完整選項

A. 編譯時就一定決定呼叫 `Instrument.play()`
B. 編譯時就一定決定呼叫 `Wind.play()`
C. 執行時根據實際物件 `new Wind()` 決定呼叫 `Wind.play()`
D. 這段程式無法編譯

## 我的回答

C

## 正確答案

C

## 完整詳解

這裡最重要的是分清楚：

```java
Instrument i = new Wind();
```

| 項目         | 型別         |
| ------------ | ------------ |
| 宣告型別     | `Instrument` |
| 實際物件型別 | `Wind`       |

呼叫：

```java
i.play();
```

時，因為 `play()` 是 instance method，而且被 `Wind` override，所以 Java 會使用動態繫結（Dynamic Binding / 动态绑定）。

也就是：

```text
編譯時只知道 i 是 Instrument。
執行時才根據實際物件 new Wind() 呼叫 Wind.play()。
```

## 本題觀念

```text
override method 的實際執行版本，看 runtime 的實際物件型別。
```

## 面試高頻

非常高頻。這是多型基本觀念題。

---

# Q4

## 完整問題

下列哪一種成員「不會」產生多型？

## 完整選項

A. 一般 instance method
B. 被子類別 override 的 method
C. `static` method
D. 非 `final` 的 public method

## 我的回答

C

## 正確答案

C

## 完整詳解

`static` method 屬於 class，不屬於單一 object。

所以 static method 沒有真正的 runtime polymorphism。

例如：

```java
class Parent {
    static void test() {
        System.out.println("Parent");
    }
}

class Child extends Parent {
    static void test() {
        System.out.println("Child");
    }
}
```

這不是 override，而是 method hiding。

真正有多型的是：

```java
Parent p = new Child();
p.instanceMethod();
```

不是：

```java
p.staticMethod();
```

## 本題觀念

```text
static method 不參與 dynamic binding。
```

## 面試高頻

高頻。常考「static method 可以 override 嗎？」

答案是：不可以，只能 hiding。

---

# Q5

## 完整問題

關於 `private` method，下列何者正確？

## 完整選項

A. `private` method 可以被子類別真正 override
B. `private` method 會參與 dynamic binding
C. 子類別寫同名 method 時，只是新方法，不是 override
D. `private` method 一定會造成編譯錯誤

## 我的回答

C

## 正確答案

C

## 完整詳解

`private` method 只在自己的 class 裡可見。

子類別看不到父類別的 private method，所以不能真正 override。

例如：

```java
class Parent {
    private void f() {
        System.out.println("Parent.f()");
    }
}

class Child extends Parent {
    public void f() {
        System.out.println("Child.f()");
    }
}
```

`Child.f()` 不是 override。

它只是 `Child` 自己新增的一個 method。

所以如果你加上：

```java
@Override
public void f() {
}
```

編譯器會報錯。

## 本題觀念

```text
private method 不會被 override。
子類別同名 method 只是新方法。
```

## 面試高頻

高頻。尤其常和 `@Override` 一起考。

---

# Q6

## 完整問題

下列哪一個是 `@Override` 的主要好處？

## 完整選項

A. 讓程式執行速度變快
B. 讓 private method 也可以被 override
C. 讓編譯器檢查你是不是真的有覆寫父類別方法
D. 讓 static method 變成 polymorphic

## 我的回答

C

## 正確答案

C

## 完整詳解

`@Override` 的主要用途是讓編譯器幫你檢查：

```text
你是不是真的 override 了父類別或介面的方法。
```

例如：

```java
class Parent {
    private void f() {}
}

class Child extends Parent {
    @Override
    public void f() {}
}
```

這會編譯失敗，因為父類別的 `f()` 是 private，子類別其實沒有 override 到它。

所以 `@Override` 是一種防呆工具。

## 本題觀念

```text
@Override 可以避免你以為自己有 override，但其實沒有。
```

## 面試高頻

中高頻。實務上也非常重要。

---

# Q7

## 完整問題

關於 field 欄位與 method 方法的多型，下列何者正確？

## 完整選項

A. field 和 method 都會根據實際物件型別動態決定
B. field 看宣告型別，method 看實際物件型別
C. field 看實際物件型別，method 看宣告型別
D. field 和 method 都只看宣告型別

## 我的回答

B

## 正確答案

B

## 完整詳解

這題是你後面 Q17 失分的核心。

規則：

```text
field 欄位存取：看宣告型別
method 方法呼叫：看實際物件型別
```

例如：

```java
Super sup = new Sub();
```

這裡：

| 項目         | 型別    |
| ------------ | ------- |
| 宣告型別     | `Super` |
| 實際物件型別 | `Sub`   |

所以：

```java
sup.field
```

看的是 `Super.field`。

但：

```java
sup.getField()
```

如果 `getField()` 被 override，就看實際物件 `Sub`，呼叫 `Sub.getField()`。

## 本題觀念

```text
field 不多型，method 才多型。
```

## 面試高頻

高頻。常用來測你是否真的理解 dynamic dispatch。

---

# Q8

## 完整問題

為什麼 constructor 裡面不建議呼叫可以被 override 的 method？

## 完整選項

A. 因為 Java 不允許 constructor 呼叫 method
B. 因為會造成 stack overflow
C. 因為可能在子類別欄位初始化完成前，就呼叫到子類別覆寫後的方法
D. 因為 constructor 裡只能呼叫 static method

## 我的回答

C

## 正確答案

C

## 完整詳解

建立子類別物件時，Java 會先執行父類別 constructor。

如果父類別 constructor 裡呼叫了一個可被 override 的 method，就可能呼叫到子類別版本。

但那時子類別自己的欄位還沒初始化完成。

例如：

```java
class Parent {
    Parent() {
        draw();
    }

    void draw() {}
}

class Child extends Parent {
    int radius = 1;

    @Override
    void draw() {
        System.out.println(radius);
    }
}
```

建立：

```java
new Child();
```

時，`Parent()` 先跑，呼叫 `draw()`，但實際跑的是 `Child.draw()`。

此時 `radius = 1` 還沒執行，所以 `radius` 可能還是預設值 `0`。

## 本題觀念

```text
constructor 裡不要呼叫可被 override 的 method。
```

## 面試高頻

高頻。這題常用來考初始化順序。

---

# Q9

## 完整問題

下列哪一個最接近「協變回傳型別（Covariant Return Type / 协变返回类型）」？

## 完整選項

A. 子類別 override method 時，可以回傳父類別回傳型別的子類別
B. 子類別 override method 時，參數型別可以任意改變
C. 子類別 method 名稱可以不同，但功能一樣
D. 子類別 field 型別可以改成父類別型別

## 我的回答

A

## 正確答案

A

## 完整詳解

協變回傳型別是指：

```text
子類別 override 父類別 method 時，回傳型別可以更具體。
```

例如：

```java
class Grain {}

class Wheat extends Grain {}

class Mill {
    Grain process() {
        return new Grain();
    }
}

class WheatMill extends Mill {
    @Override
    Wheat process() {
        return new Wheat();
    }
}
```

父類別回傳：

```java
Grain
```

子類別回傳：

```java
Wheat
```

這是合法的，因為：

```text
Wheat is a Grain
```

## 本題觀念

```text
override 時，return type 可以是原回傳型別的子類別。
```

## 面試高頻

中頻。比 override / overload 低，但 Java OOP 仍常出現。

---

# Q10

## 完整問題

關於向下轉型（Downcasting / 向下转型），下列何者正確？

## 完整選項

A. 向下轉型永遠安全
B. 向下轉型一定會編譯失敗
C. Java 會在執行時檢查轉型是否正確，錯誤時可能丟出 `ClassCastException`
D. 向下轉型不需要實際物件型別符合，只要變數型別符合即可

## 我的回答

C

## 正確答案

C

## 完整詳解

向下轉型是：

```text
父類別型別 → 子類別型別
```

例如：

```java
Animal a = new Dog();
Dog d = (Dog) a;
```

這是成功的，因為 `a` 實際上指向的是 `new Dog()`。

但如果：

```java
Animal a = new Cat();
Dog d = (Dog) a;
```

編譯可能過，但執行時會丟：

```text
ClassCastException
```

因為實際物件是 `Cat`，不是 `Dog`。

## 本題觀念

```text
downcasting 是否成功，看實際物件型別。
```

## 面試高頻

高頻。常和 `instanceof` 一起考。

---

# Q11

## 完整問題

```java
Animal a = new Dog();
```

這是一種向上轉型。

## 完整選項

T / F

## 我的回答

T

## 正確答案

T

## 完整詳解

`Dog` 是 `Animal` 的子類別時：

```java
Animal a = new Dog();
```

就是把子類別物件 `Dog` 當成父類別 `Animal` 使用。

這就是 upcasting。

## 本題觀念

```text
子類別物件可以安全地當成父類別型別使用。
```

## 面試高頻

高頻。

---

# Q12

## 完整問題

只要子類別寫了一個跟父類別 private method 同名的方法，就一定是 override。

## 完整選項

T / F

## 我的回答

F

## 正確答案

F

## 完整詳解

父類別的 private method 對子類別不可見。

所以子類別寫同名 method，不是 override，而是新增一個 method。

例如：

```java
class Parent {
    private void f() {}
}

class Child extends Parent {
    public void f() {}
}
```

`Child.f()` 不是 override。

## 本題觀念

```text
private method 不能被 override。
```

## 面試高頻

高頻。

---

# Q13

## 完整問題

Java 的一般 instance method 呼叫，通常會使用 dynamic binding。

## 完整選項

T / F

## 我的回答

T

## 正確答案

T

## 完整詳解

Java 中，一般 instance method 如果沒有被 `static`、`final`、`private` 限制，通常會使用 dynamic binding。

也就是：

```text
執行時根據實際物件型別決定要呼叫哪個 method。
```

## 本題觀念

```text
Java instance method 通常支援多型。
```

## 面試高頻

高頻。

---

# Q14

## 完整問題

`static` method 屬於 class，不屬於單一物件，所以沒有真正的多型。

## 完整選項

T / F

## 我的回答

T

## 正確答案

T

## 完整詳解

`static` method 是綁在 class 上的。

例如：

```java
Parent.test();
Child.test();
```

這種呼叫不是根據 object runtime type 決定，而是根據 class 決定。

所以 static method 沒有真正的 dynamic binding。

## 本題觀念

```text
static method 不屬於物件，不參與 runtime polymorphism。
```

## 面試高頻

高頻。

---

# Q15

## 完整問題

如果一直需要向下轉型，可能代表父類別或介面設計不夠好。

## 完整選項

T / F

## 我的回答

T

## 正確答案

T

## 完整詳解

如果程式常常需要這樣寫：

```java
if (x instanceof Dog) {
    ((Dog) x).bark();
}
```

代表你可能沒有把共同操作設計在父類別或介面裡。

比較好的設計可能是：

```java
interface Animal {
    void makeSound();
}
```

然後讓：

```java
class Dog implements Animal
class Cat implements Animal
```

各自實作 `makeSound()`。

呼叫端只要：

```java
animal.makeSound();
```

不用一直 downcast。

## 本題觀念

```text
一直 downcast 通常是設計味道。
```

## 面試高頻

中高頻。會連到設計原則與介面設計。

---

# Q16

## 完整問題

多型基本輸出。

```java
class Instrument {
    void play() {
        System.out.println("Instrument.play()");
    }
}

class Wind extends Instrument {
    @Override
    void play() {
        System.out.println("Wind.play()");
    }
}

public class Main {
    static void tune(Instrument i) {
        i.play();
    }

    public static void main(String[] args) {
        Wind flute = new Wind();
        tune(flute);
    }
}
```

請問輸出是什麼？

## 完整選項

無，輸出題。

## 我的回答

```text
Wind.play()
```

## 正確答案

```text
Wind.play()
```

## 完整詳解

這裡：

```java
Wind flute = new Wind();
tune(flute);
```

`tune()` 的參數是：

```java
Instrument i
```

所以 `flute` 傳入時會 upcast 成 `Instrument`。

但 `i.play()` 是 instance method 呼叫。

實際物件是：

```java
new Wind()
```

而 `Wind` override 了 `play()`。

所以執行：

```java
Wind.play()
```

## 本題觀念

```text
方法呼叫看實際物件型別。
```

## 面試高頻

高頻。基本多型輸出題。

---

# Q17

## 完整問題

field 沒有多型。

```java
class Super {
    public int field = 0;

    public int getField() {
        return field;
    }
}

class Sub extends Super {
    public int field = 1;

    @Override
    public int getField() {
        return field;
    }

    public int getSuperField() {
        return super.field;
    }
}

public class Main {
    public static void main(String[] args) {
        Super sup = new Sub();

        System.out.println("sup.field = " + sup.field);
        System.out.println("sup.getField() = " + sup.getField());

        Sub sub = new Sub();

        System.out.println("sub.field = " + sub.field);
        System.out.println("sub.getField() = " + sub.getField());
        System.out.println("sub.getSuperField() = " + sub.getSuperField());
    }
}
```

請問完整輸出是什麼？

## 完整選項

無，輸出題。

## 我的回答

```text
sup.field = 0
sup.getField() = 0

sub.field = 1
sub.getField() = 1
sub.getSuperField() = 1
```

## 正確答案

```text
sup.field = 0
sup.getField() = 1
sub.field = 1
sub.getField() = 1
sub.getSuperField() = 0
```

## 完整詳解

關鍵在這行：

```java
Super sup = new Sub();
```

它的意思是：

| 項目         | 型別    |
| ------------ | ------- |
| 宣告型別     | `Super` |
| 實際物件型別 | `Sub`   |

---

### 1. `sup.field`

```java
sup.field
```

是 field 欄位存取。

field 不多型，所以看宣告型別。

`sup` 的宣告型別是：

```java
Super
```

所以：

```java
sup.field
```

讀到的是：

```java
Super.field
```

值是：

```text
0
```

所以輸出：

```text
sup.field = 0
```

---

### 2. `sup.getField()`

```java
sup.getField()
```

是 method 呼叫。

method 有多型，所以看實際物件型別。

實際物件是：

```java
new Sub()
```

所以呼叫：

```java
Sub.getField()
```

`Sub.getField()` 裡面：

```java
return field;
```

這裡的 `field` 是 `Sub.field`，值是：

```text
1
```

所以輸出：

```text
sup.getField() = 1
```

---

### 3. `sub.field`

```java
Sub sub = new Sub();
```

這裡宣告型別和實際物件型別都是 `Sub`。

所以：

```java
sub.field
```

讀到 `Sub.field`，值是：

```text
1
```

輸出：

```text
sub.field = 1
```

---

### 4. `sub.getField()`

`sub` 實際物件是 `Sub`，呼叫的是：

```java
Sub.getField()
```

所以輸出：

```text
sub.getField() = 1
```

---

### 5. `sub.getSuperField()`

```java
public int getSuperField() {
    return super.field;
}
```

`super.field` 明確指定讀父類別的 field。

父類別 `Super.field` 是：

```java
public int field = 0;
```

所以輸出：

```text
sub.getSuperField() = 0
```

## 本題觀念

```text
field 看宣告型別。
method 看實際物件型別。
super.field 明確看父類別欄位。
```

## 面試高頻

非常高頻。這題你要重練一次。

---

# Q18

## 完整問題

constructor 內呼叫 override method。

```java
class Glyph {
    Glyph() {
        System.out.println("Glyph() before draw()");
        draw();
        System.out.println("Glyph() after draw()");
    }

    void draw() {
        System.out.println("Glyph.draw()");
    }
}

class RoundGlyph extends Glyph {
    private int radius = 1;

    RoundGlyph(int radius) {
        this.radius = radius;
        System.out.println("RoundGlyph(), radius = " + this.radius);
    }

    @Override
    void draw() {
        System.out.println("RoundGlyph.draw(), radius = " + radius);
    }
}

public class Main {
    public static void main(String[] args) {
        new RoundGlyph(5);
    }
}
```

請問完整輸出是什麼？

## 完整選項

無，輸出題。

## 我的回答

```text
全打有點麻煩 但大概是 constuctor的多型 private int radius = 1;
一開始還沒初始化 所以第一次輸出會radius=0
之後初始化後才會  radius = 1
```

## 正確答案

```text
Glyph() before draw()
RoundGlyph.draw(), radius = 0
Glyph() after draw()
RoundGlyph(), radius = 5
```

## 完整詳解

執行：

```java
new RoundGlyph(5);
```

時，不是直接先跑 `RoundGlyph` constructor。

Java 會先建立父類別部分。

完整流程：

---

### 第 1 步：記憶體先給預設值

在任何 constructor 真正執行前，物件記憶體會先有預設值。

所以：

```java
private int radius = 1;
```

這行還沒跑之前，`radius` 是：

```text
0
```

---

### 第 2 步：先呼叫父類別 `Glyph()` constructor

所以先進入：

```java
Glyph() {
    System.out.println("Glyph() before draw()");
    draw();
    System.out.println("Glyph() after draw()");
}
```

先輸出：

```text
Glyph() before draw()
```

---

### 第 3 步：`Glyph()` 裡呼叫 `draw()`

這裡很危險：

```java
draw();
```

因為 `draw()` 被 `RoundGlyph` override。

所以雖然目前正在跑 `Glyph()` constructor，實際呼叫的是：

```java
RoundGlyph.draw()
```

但這時 `RoundGlyph` 的欄位初始化還沒完成。

所以 `radius` 仍然是預設值：

```text
0
```

輸出：

```text
RoundGlyph.draw(), radius = 0
```

---

### 第 4 步：回到 `Glyph()` constructor

接著輸出：

```text
Glyph() after draw()
```

---

### 第 5 步：開始初始化 `RoundGlyph` 欄位

這時才執行：

```java
private int radius = 1;
```

所以 `radius` 變成：

```text
1
```

但是注意：這一行沒有 `System.out.println()`，所以不會輸出 `radius = 1`。

---

### 第 6 步：執行 `RoundGlyph(int radius)` constructor

```java
RoundGlyph(int radius) {
    this.radius = radius;
    System.out.println("RoundGlyph(), radius = " + this.radius);
}
```

傳入的是：

```java
5
```

所以：

```java
this.radius = radius;
```

會把欄位改成：

```text
5
```

最後輸出：

```text
RoundGlyph(), radius = 5
```

## 本題觀念

```text
父類別 constructor 執行時，子類別欄位還沒初始化完成。
constructor 裡呼叫可 override method，可能會呼叫到未初始化完成的子類別 method。
```

## 面試高頻

高頻。這是 constructor + polymorphism 經典陷阱。

---

# Q19

## 完整問題

為什麼 `tune(Instrument i)` 比 `tune(Wind w)` 更有擴充性？

請用自己的話回答。

## 完整選項

無，簡答題。

## 我的回答

```text
因為可以透過多型 只規定父類別的方法 再由各子方法去override 具體要做什麼
減少耦合 擴充性上升 未來只需要多些新的子類別overide 就可以
```

## 標準答案

```text
tune(Instrument i) 只依賴父類別 Instrument。
只要新的樂器類別也繼承 Instrument，並 override play()，
tune() 就不用修改。

如果寫成 tune(Wind w)，那每新增一種樂器，
就可能要新增 tune(Stringed)、tune(Brass)、tune(Percussion) 等方法。

所以 tune(Instrument i) 耦合比較低，擴充性比較好。
```

## 完整詳解

`Instrument` 是比較穩定的抽象。

`Wind` 是具體類別。

如果你寫：

```java
static void tune(Wind w) {
    w.play();
}
```

那這個方法只能處理 `Wind`。

如果後來新增：

```java
class Brass extends Instrument
class Stringed extends Instrument
class Percussion extends Instrument
```

你就要一直加新的 `tune()`。

但如果一開始就寫：

```java
static void tune(Instrument i) {
    i.play();
}
```

那所有 `Instrument` 的子類別都可以傳進來。

真正要執行哪個 `play()`，交給 runtime polymorphism 決定。

## 本題觀念

```text
依賴父類別或介面，比依賴具體類別更容易擴充。
```

## 面試高頻

高頻。會連到：

```text
Program to an interface, not an implementation.
```

---

# Q20

## 完整問題

請解釋：

```java
Instrument i = new Wind();
```

這行中：

- 宣告型別是什麼？
- 實際物件型別是什麼？
- 呼叫 override method 時看哪一個？

## 完整選項

無，簡答題。

## 我的回答

```text
Wind
Instrument
Wind
```

## 正確答案

```text
宣告型別：Instrument
實際物件型別：Wind
呼叫 override method 時：看實際物件型別，也就是 Wind
```

## 完整詳解

這行要拆成左右兩邊看：

```java
Instrument i = new Wind();
```

左邊：

```java
Instrument i
```

是變數宣告。

所以：

```text
宣告型別 = Instrument
```

右邊：

```java
new Wind()
```

是真正建立物件。

所以：

```text
實際物件型別 = Wind
```

當你呼叫：

```java
i.play();
```

如果 `Wind` override 了 `play()`，實際執行哪個版本要看 runtime 的實際物件。

也就是：

```text
看 new Wind()
所以呼叫 Wind.play()
```

你的第三個答案是對的，但前兩個反了。

## 本題觀念

```text
左邊宣告型別。
右邊 new 實際物件型別。
override method 看實際物件型別。
field 看宣告型別。
```

## 面試高頻

非常高頻。這是你這次最需要補強的地方。

---

# Q21

## 完整問題

請說明 `Override` 和 `Overload` 的差別。

至少回答：

- 發生位置
- 參數是否要一樣
- 是否和 runtime polymorphism 直接相關

## 完整選項

無，簡答題。

## 我的回答

```text
Override 是 子class 用了 父class 的 instance method 是和 runtime polymorphism 直接相關
Overload 是 同個 method 名 傳的參數不同 會有不同的效果 否和 runtime polymorphism 直接相關
```

## 標準答案

| 項目                                 | Override 覆寫        | Overload 多載                   |
| ------------------------------------ | -------------------- | ------------------------------- |
| 中文                                 | 覆寫                 | 多載                            |
| 英文                                 | Method Overriding    | Method Overloading              |
| 發生位置                             | 父子類別之間         | 同一類別，或父子類別中也可能    |
| 方法名稱                             | 必須相同             | 必須相同                        |
| 參數列表                             | 必須相同             | 必須不同                        |
| 回傳型別                             | 相同或協變回傳型別   | 不能只靠回傳型別不同來 overload |
| 是否和 runtime polymorphism 直接相關 | 是                   | 否                              |
| 決定時機                             | 執行時看實際物件型別 | 編譯時看參數列表                |

## 完整詳解

### Override

Override 是子類別重新定義父類別已有的方法。

例如：

```java
class Animal {
    void makeSound() {
        System.out.println("animal");
    }
}

class Dog extends Animal {
    @Override
    void makeSound() {
        System.out.println("dog");
    }
}
```

呼叫：

```java
Animal a = new Dog();
a.makeSound();
```

會輸出：

```text
dog
```

因為這是 runtime polymorphism。

---

### Overload

Overload 是同一個 method name，但參數列表不同。

例如：

```java
class Printer {
    void print(String text) {}

    void print(int number) {}

    void print(String text, int times) {}
}
```

這不是 runtime polymorphism 的核心。

編譯器會根據你傳入的參數，在編譯階段選擇要呼叫哪個版本。

## 本題觀念

```text
Override = 父子類別 + 參數相同 + runtime polymorphism。
Overload = 同名方法 + 參數不同 + compile-time selection。
```

## 面試高頻

非常高頻。幾乎 Java OOP 面試必問。

---

# Q22

## 完整問題

為什麼「組合優於繼承（Composition over Inheritance / 组合优于继承）」通常是比較穩的設計原則？

請用白話說明。

## 完整選項

無，簡答題。

## 我的回答

```text
耦合較少？
```

## 標準答案

```text
組合通常比繼承穩，是因為繼承會讓子類別強烈依賴父類別的結構與行為。
父類別一改，子類別可能被影響。

組合則是把另一個物件放進自己的 field 裡，
透過呼叫它的方法來完成工作。

這樣可以比較容易替換行為，也比較不會被父類別的內部設計綁死。
```

## 完整詳解

繼承是強關係：

```text
A is a B
A 是 B 的一種
```

例如：

```java
class Dog extends Animal
```

這代表 `Dog` 很深地依賴 `Animal`。

如果 `Animal` 的設計改了，`Dog` 可能會被影響。

---

組合是比較鬆的關係：

```text
A has a B
A 擁有 / 使用 B
```

例如：

```java
class NotificationService {
    private MessageSender sender;
}
```

`NotificationService` 不需要繼承 `MessageSender`。

它只是使用 `MessageSender`。

未來可以替換成：

```java
EmailSender
SmsSender
LineSender
```

這樣擴充彈性更高。

---

比較白話：

```text
繼承：我是你的一種，關係很強。
組合：我使用你，關係比較鬆。
```

所以你回答「耦合較少」方向是對的，但要補上：

```text
比較容易替換、比較不會被父類別內部設計綁死、擴充比較穩。
```

## 本題觀念

```text
繼承適合穩定的 is-a 關係。
組合適合可替換的 has-a / use-a 關係。
```

## 面試高頻

中高頻。常和設計模式、Spring service 設計、策略模式一起考。

---

# 最後整理：你這次真正要補的 3 個點

## 1. 宣告型別 vs 實際物件型別

```java
Instrument i = new Wind();
```

| 區塊           | 答案         |
| -------------- | ------------ |
| `Instrument i` | 宣告型別     |
| `new Wind()`   | 實際物件型別 |

---

## 2. field vs method

```java
Super sup = new Sub();
```

| 寫法             | 看誰               |
| ---------------- | ------------------ |
| `sup.field`      | 宣告型別 `Super`   |
| `sup.getField()` | 實際物件型別 `Sub` |

---

## 3. constructor 裡呼叫 override method

```text
會先跑父類別 constructor。
如果父類別 constructor 呼叫可 override method，
可能會呼叫到子類別 method。
但此時子類別欄位還沒初始化完成。
```

所以會出現：

```text
radius = 0
```

不是：

```text
radius = 1
```

也不是：

```text
radius = 5
```

---

# 本章目前判定

```text
第 9 章多型：觀念通過，但 field/method 與宣告型別/實際型別需要補強。
```
