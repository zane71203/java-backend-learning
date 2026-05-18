# 第八章 複用（Reuse / 复用）

程式碼複用（Code Reuse / 代码复用）是物件導向程式設計（Object-Oriented Programming, OOP / 面向对象编程）最有價值的原因之一。

在像 C 語言這類偏程序式程式設計（Procedural Programming / 面向过程编程）的語言裡，「複用」很多時候只是把一段程式碼複製到另一個地方。這雖然也算重複使用，但長期來看不好維護。Java 則主要透過類別（Class / 类）來組織程式。你可以直接使用別人已經寫好、測試過、除錯過的類別，而不是每次都從零開始。

本章重點是：**如何在不修改原本類別原始碼的情況下，重用既有程式碼。**

主要有兩種方式：

1. **組合（Composition / 组合）**  
   在新類別裡面放入既有類別的物件。也就是「我的類別裡有另一個物件」。  
   這種方式重用的是「功能」，不是繼承它的身份。

2. **繼承（Inheritance / 继承）**  
   建立一個既有類別的新版本。也就是「我的類別是一種既有類別」。  
   使用 `extends` 後，子類別（Subclass / 子类）可以取得父類別（Superclass / 父类，也常叫 Base Class / 基类）的欄位與方法。

組合與繼承在語法和行為上有一些相似之處，因為它們都是「用既有型別建立新型別」。但它們背後代表的設計關係不同：

| 關係  | 適合做法                   | 白話意思   |
| ----- | -------------------------- | ---------- |
| has-a | 組合（Composition / 组合） | A 裡面有 B |
| is-a  | 繼承（Inheritance / 继承） | A 是一種 B |

---

## 一、組合語法（Composition Syntax / 组合语法）

組合其實前面已經用過很多次。你只要把某個物件的參考（Object Reference / 对象引用）放進一個新的類別裡，就是在使用組合。

例如：你想建立一個灑水系統 `SprinklerSystem`，它裡面有幾個 `String` 欄位、一些基本型別（Primitive Type / 基本类型），還有一個 `WaterSource` 物件。

```java
class WaterSource {
  private String s;

  WaterSource() {
    System.out.println("WaterSource()");
    s = "Constructed";
  }

  @Override
  public String toString() {
    return s;
  }
}

public class SprinklerSystem {
  private String valve1, valve2, valve3, valve4;
  private WaterSource source = new WaterSource();
  private int i;
  private float f;

  @Override
  public String toString() {
    return
      "valve1 = " + valve1 + " " +
      "valve2 = " + valve2 + " " +
      "valve3 = " + valve3 + " " +
      "valve4 = " + valve4 + "\n" +
      "i = " + i + " " + "f = " + f + " " +
      "source = " + source;
  }

  public static void main(String[] args) {
    SprinklerSystem sprinklers = new SprinklerSystem();
    System.out.println(sprinklers);
  }
}
```

輸出：

```text
WaterSource()
valve1 = null valve2 = null valve3 = null valve4 = null
i = 0 f = 0.0 source = Constructed
```

### 1. `toString()` 是什麼？

`toString()` 是 Java 中非常重要的方法。每個非基本型別物件都繼承自 `Object`，而 `Object` 裡面有 `toString()` 方法。

當 Java 需要一個字串，但你丟給它一個物件時，Java 會嘗試呼叫那個物件的 `toString()`，把物件轉成字串。

例如：

```java
"source = " + source
```

左邊是字串，右邊是 `WaterSource` 物件。字串只能和字串拼接，所以 Java 會自動做：

```java
source.toString()
```

所以輸出會是：

```text
source = Constructed
```

因為 `WaterSource` 的 `toString()` 回傳 `s`，而 `s` 的值是 `"Constructed"`。

### 2. `@Override` 的用途

`@Override` 是註解（Annotation / 注解），不是必要語法，但很建議使用。

它的作用是告訴編譯器：

> 我現在是要覆寫（Override / 覆写）父類別的方法。

如果你方法名稱拼錯、參數寫錯，編譯器會直接報錯。

例如你不小心寫成：

```java
public String tostring() { ... }
```

Java 會把它當成一個新方法，而不是覆寫 `toString()`。加上 `@Override` 後，這種錯誤就會被編譯器抓到。

### 3. 欄位的預設值

在類別欄位（Field / 字段）中：

| 型別      | 預設值  |
| --------- | ------- |
| `int`     | `0`     |
| `float`   | `0.0`   |
| `boolean` | `false` |
| 物件參考  | `null`  |

所以在上面的範例裡：

```java
private String valve1, valve2, valve3, valve4;
private int i;
private float f;
```

如果沒有手動指定值，`String` 會是 `null`，`int` 會是 `0`，`float` 會是 `0.0`。

注意：

```java
System.out.println(valve1);
```

印出 `null` 不會報錯。

但如果你對 `null` 呼叫方法，就會出錯：

```java
valve1.length(); // 會發生 NullPointerException
```

---

## 二、物件參考的四種初始化方式

Java 不會自動幫每個物件參考建立預設物件。這是合理的，因為很多情況下你不一定真的需要那個物件。如果 Java 每次都自動建立，會造成不必要的記憶體與效能成本。

初始化物件參考常見有四種方式：

1. **在欄位定義時初始化**  
   也就是宣告欄位時直接 `new`。

2. **在建構子（Constructor / 构造器）中初始化**  
   建立物件時，由建構子負責初始化。

3. **在真正使用前才初始化**  
   這叫延遲初始化（Lazy Initialization / 延迟初始化）。適合物件建立成本高，而且不一定每次都會用到的情況。

4. **使用實例初始化區塊（Instance Initialization Block / 实例初始化块）**  
   這是類別內 `{ ... }` 的區塊，會在建構子執行前被執行。

範例：

```java
class Soap {
  private String s;

  Soap() {
    System.out.println("Soap()");
    s = "Constructed";
  }

  @Override
  public String toString() {
    return s;
  }
}

public class Bath {
  private String
    s1 = "Happy",
    s2 = "Happy",
    s3, s4;

  private Soap castille;
  private int i;
  private float toy;

  public Bath() {
    System.out.println("Inside Bath()");
    s3 = "Joy";
    toy = 3.14f;
    castille = new Soap();
  }

  {
    i = 47;
  }

  @Override
  public String toString() {
    if (s4 == null) {
      s4 = "Joy";
    }

    return
      "s1 = " + s1 + "\n" +
      "s2 = " + s2 + "\n" +
      "s3 = " + s3 + "\n" +
      "s4 = " + s4 + "\n" +
      "i = " + i + "\n" +
      "toy = " + toy + "\n" +
      "castille = " + castille;
  }

  public static void main(String[] args) {
    Bath b = new Bath();
    System.out.println(b);
  }
}
```

輸出：

```text
Inside Bath()
Soap()
s1 = Happy
s2 = Happy
s3 = Joy
s4 = Joy
i = 47
toy = 3.14
castille = Constructed
```

### 這段程式要看懂的重點

| 欄位                    | 初始化方式                               |
| ----------------------- | ---------------------------------------- |
| `s1`, `s2`              | 定義時初始化                             |
| `s3`, `toy`, `castille` | 建構子中初始化                           |
| `i`                     | 實例初始化區塊                           |
| `s4`                    | 延遲初始化，在 `toString()` 使用前才設定 |

如果一個物件參考還是 `null`，你就不能對它呼叫方法。否則會發生 `NullPointerException`。

---

## 三、繼承語法（Inheritance Syntax / 继承语法）

繼承是物件導向語言的重要機制。

在 Java 裡，只要你建立一個類別，它一定會繼承某個類別。如果你沒有明確寫 `extends`，Java 會讓它隱性繼承 `Object`。

繼承使用關鍵字：

```java
extends
```

意思是：

> 這個新類別是一種既有類別。

範例：

```java
class Cleanser {
  private String s = "Cleanser";

  public void append(String a) {
    s += a;
  }

  public void dilute() {
    append(" dilute()");
  }

  public void apply() {
    append(" apply()");
  }

  public void scrub() {
    append(" scrub()");
  }

  @Override
  public String toString() {
    return s;
  }

  public static void main(String[] args) {
    Cleanser x = new Cleanser();
    x.dilute();
    x.apply();
    x.scrub();
    System.out.println(x);
  }
}

public class Detergent extends Cleanser {
  @Override
  public void scrub() {
    append(" Detergent.scrub()");
    super.scrub();
  }

  public void foam() {
    append(" foam()");
  }

  public static void main(String[] args) {
    Detergent x = new Detergent();
    x.dilute();
    x.apply();
    x.scrub();
    x.foam();
    System.out.println(x);

    System.out.println("Testing base class:");
    Cleanser.main(args);
  }
}
```

輸出：

```text
Cleanser dilute() apply() Detergent.scrub() scrub() foam()
Testing base class:
Cleanser dilute() apply() scrub()
```

### 1. `Detergent extends Cleanser` 是什麼意思？

```java
public class Detergent extends Cleanser
```

意思是：

> `Detergent` 是一種 `Cleanser`。

所以 `Detergent` 會取得 `Cleanser` 中可被子類別使用的方法，例如：

```java
x.dilute();
x.apply();
x.scrub();
```

即使 `Detergent` 裡沒有明確寫出 `dilute()` 和 `apply()`，它仍然可以使用，因為這些方法來自父類別 `Cleanser`。

### 2. 覆寫方法（Override / 覆写）

`Detergent` 重新定義了 `scrub()`：

```java
@Override
public void scrub() {
  append(" Detergent.scrub()");
  super.scrub();
}
```

這叫覆寫（Override / 覆写）。意思是：

> 子類別提供自己的版本，取代父類別原本的方法行為。

### 3. `super` 是什麼？

`super` 代表父類別那一邊。

```java
super.scrub();
```

意思是：

> 呼叫父類別版本的 `scrub()`。

如果你在 `scrub()` 裡直接寫：

```java
scrub();
```

那就會變成自己呼叫自己，形成遞迴（Recursion / 递归），最後可能造成 `StackOverflowError`。

### 4. 子類別可以新增方法

`Detergent` 除了繼承 `Cleanser` 的方法，也可以新增自己的方法：

```java
public void foam() {
  append(" foam()");
}
```

所以 `Detergent` 物件可以呼叫：

- 來自 `Cleanser` 的 `dilute()`、`apply()`
- 自己覆寫的 `scrub()`
- 自己新增的 `foam()`

---

## 四、初始化父類別（Base-Class Initialization / 初始化基类）

使用繼承後，建立物件時就不只是一個類別的事。

例如：

```java
class Drawing extends Art
class Cartoon extends Drawing
```

當你建立 `Cartoon` 物件時，它裡面會包含：

- `Art` 那一層
- `Drawing` 那一層
- `Cartoon` 自己這一層

也就是說，子類別物件裡面包含父類別的子物件（Subobject / 子对象）。

範例：

```java
class Art {
  Art() {
    System.out.println("Art constructor");
  }
}

class Drawing extends Art {
  Drawing() {
    System.out.println("Drawing constructor");
  }
}

public class Cartoon extends Drawing {
  public Cartoon() {
    System.out.println("Cartoon constructor");
  }

  public static void main(String[] args) {
    Cartoon x = new Cartoon();
  }
}
```

輸出：

```text
Art constructor
Drawing constructor
Cartoon constructor
```

### 初始化順序

建立子類別物件時，初始化順序是：

```text
最上層父類別 → 中間父類別 → 目前子類別
```

也就是：

```text
Art → Drawing → Cartoon
```

原因很簡單：子類別可能會依賴父類別的狀態，所以父類別必須先準備好。

---

## 五、帶參數的建構子（Constructors with Arguments / 带参数的构造器）

如果父類別只有無參數建構子，Java 可以自動呼叫它。

但如果父類別沒有無參數建構子，而是只有帶參數的建構子，那子類別就必須明確用 `super(...)` 呼叫父類別建構子。

範例：

```java
class Game {
  Game(int i) {
    System.out.println("Game constructor");
  }
}

class BoardGame extends Game {
  BoardGame(int i) {
    super(i);
    System.out.println("BoardGame constructor");
  }
}

public class Chess extends BoardGame {
  Chess() {
    super(11);
    System.out.println("Chess constructor");
  }

  public static void main(String[] args) {
    Chess x = new Chess();
  }
}
```

輸出：

```text
Game constructor
BoardGame constructor
Chess constructor
```

### 關鍵規則

```java
super(i);
```

必須是建構子裡的第一行有效語句。

如果沒有寫，編譯器會嘗試自動呼叫：

```java
super();
```

但 `Game` 沒有 `Game()` 這種無參數建構子，所以會編譯失敗。

---

## 六、委派（Delegation / 委托）

Java 沒有語言層級直接支援委派（Delegation / 委托），但這是一種常見設計做法。

委派介於組合和繼承之間：

- 像組合一樣：在新類別裡放入另一個物件。
- 像繼承一樣：新類別對外提供那個物件的一些方法。

假設有一個太空船控制模組：

```java
public class SpaceShipControls {
  void up(int velocity) {}
  void down(int velocity) {}
  void left(int velocity) {}
  void right(int velocity) {}
  void forward(int velocity) {}
  void back(int velocity) {}
  void turboBoost() {}
}
```

如果用繼承：

```java
public class DerivedSpaceShip extends SpaceShipControls {
  private String name;

  public DerivedSpaceShip(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }

  public static void main(String[] args) {
    DerivedSpaceShip protector = new DerivedSpaceShip("NSEA Protector");
    protector.forward(100);
  }
}
```

這樣寫雖然能用，但語意不太精準。

因為太空船不是「一種控制模組」，而是「有一個控制模組」。

更合理的是委派：

```java
public class SpaceShipDelegation {
  private String name;
  private SpaceShipControls controls = new SpaceShipControls();

  public SpaceShipDelegation(String name) {
    this.name = name;
  }

  public void back(int velocity) {
    controls.back(velocity);
  }

  public void down(int velocity) {
    controls.down(velocity);
  }

  public void forward(int velocity) {
    controls.forward(velocity);
  }

  public void left(int velocity) {
    controls.left(velocity);
  }

  public void right(int velocity) {
    controls.right(velocity);
  }

  public void turboBoost() {
    controls.turboBoost();
  }

  public void up(int velocity) {
    controls.up(velocity);
  }

  public static void main(String[] args) {
    SpaceShipDelegation protector = new SpaceShipDelegation("NSEA Protector");
    protector.forward(100);
  }
}
```

### 委派的重點

`SpaceShipDelegation` 對外提供 `forward()`：

```java
public void forward(int velocity) {
  controls.forward(velocity);
}
```

但真正做事的是裡面的 `controls`。

這樣你可以控制要暴露哪些方法，而不是像繼承一樣把父類別的公開方法幾乎全部帶進來。

---

## 七、組合與繼承一起使用

實務上，你常常會同時用到組合和繼承。

範例：

```java
class Plate {
  Plate(int i) {
    System.out.println("Plate constructor");
  }
}

class DinnerPlate extends Plate {
  DinnerPlate(int i) {
    super(i);
    System.out.println("DinnerPlate constructor");
  }
}

class Utensil {
  Utensil(int i) {
    System.out.println("Utensil constructor");
  }
}

class Spoon extends Utensil {
  Spoon(int i) {
    super(i);
    System.out.println("Spoon constructor");
  }
}

class Fork extends Utensil {
  Fork(int i) {
    super(i);
    System.out.println("Fork constructor");
  }
}

class Knife extends Utensil {
  Knife(int i) {
    super(i);
    System.out.println("Knife constructor");
  }
}

class Custom {
  Custom(int i) {
    System.out.println("Custom constructor");
  }
}

public class PlaceSetting extends Custom {
  private Spoon sp;
  private Fork frk;
  private Knife kn;
  private DinnerPlate pl;

  public PlaceSetting(int i) {
    super(i + 1);
    sp = new Spoon(i + 2);
    frk = new Fork(i + 3);
    kn = new Knife(i + 4);
    pl = new DinnerPlate(i + 5);
    System.out.println("PlaceSetting constructor");
  }

  public static void main(String[] args) {
    PlaceSetting x = new PlaceSetting(9);
  }
}
```

輸出：

```text
Custom constructor
Utensil constructor
Spoon constructor
Utensil constructor
Fork constructor
Utensil constructor
Knife constructor
Plate constructor
DinnerPlate constructor
PlaceSetting constructor
```

### 這段程式的初始化順序

`PlaceSetting` 繼承 `Custom`，所以父類別 `Custom` 會先初始化：

```java
super(i + 1);
```

接著才初始化自己內部的組合物件：

```java
sp = new Spoon(i + 2);
frk = new Fork(i + 3);
kn = new Knife(i + 4);
pl = new DinnerPlate(i + 5);
```

重點：

- Java 會強制你先初始化父類別。
- 但 Java 不會強制你一定要初始化所有成員物件。
- 如果你忘記初始化成員物件，它可能就是 `null`。

---

## 八、確保適當清理（Cleanup / 清理）

Java 沒有 C++ 那種解構子（Destructor / 析构函数）。

在 Java 裡，很多時候你只要讓物件失去參考，之後交給垃圾回收器（Garbage Collector, GC / 垃圾回收器）處理即可。

但是有些資源不是單純記憶體，例如：

- 檔案
- 網路連線
- 資料庫連線
- 圖形資源
- 外部系統資源

這些東西通常需要你明確關閉或釋放。

所以如果類別有需要清理的資源，你應該自己定義清理方法，例如：

```java
dispose()
close()
release()
```

並且在 `finally` 或 `try-with-resources` 中確保它會被執行。

> 現代 Java 實務中，如果是需要關閉的資源，通常會實作 `AutoCloseable`，然後搭配 `try-with-resources`。本章原本用 `dispose()` 來示範清理順序，觀念仍然有用。

範例：

```java
class Shape {
  Shape(int i) {
    System.out.println("Shape constructor");
  }

  void dispose() {
    System.out.println("Shape dispose");
  }
}

class Circle extends Shape {
  Circle(int i) {
    super(i);
    System.out.println("Drawing Circle");
  }

  @Override
  void dispose() {
    System.out.println("Erasing Circle");
    super.dispose();
  }
}

class Triangle extends Shape {
  Triangle(int i) {
    super(i);
    System.out.println("Drawing Triangle");
  }

  @Override
  void dispose() {
    System.out.println("Erasing Triangle");
    super.dispose();
  }
}

class Line extends Shape {
  private int start, end;

  Line(int start, int end) {
    super(start);
    this.start = start;
    this.end = end;
    System.out.println("Drawing Line: " + start + ", " + end);
  }

  @Override
  void dispose() {
    System.out.println("Erasing Line: " + start + ", " + end);
    super.dispose();
  }
}

public class CADSystem extends Shape {
  private Circle c;
  private Triangle t;
  private Line[] lines = new Line[3];

  public CADSystem(int i) {
    super(i + 1);

    for (int j = 0; j < lines.length; j++) {
      lines[j] = new Line(j, j * j);
    }

    c = new Circle(1);
    t = new Triangle(1);
    System.out.println("Combined constructor");
  }

  @Override
  public void dispose() {
    System.out.println("CADSystem.dispose()");

    t.dispose();
    c.dispose();

    for (int i = lines.length - 1; i >= 0; i--) {
      lines[i].dispose();
    }

    super.dispose();
  }

  public static void main(String[] args) {
    CADSystem x = new CADSystem(47);

    try {
      // 執行主要程式邏輯
    } finally {
      x.dispose();
    }
  }
}
```

### 清理順序

清理通常要和初始化相反：

```text
最後建立的，先清理。
最先建立的，最後清理。
```

原因是後建立的物件可能依賴先建立的物件。如果你先把父類別或底層資源清掉，子物件清理時可能會找不到它依賴的東西。

### 不要依賴 `finalize()`

舊 Java 曾經有 `finalize()`，但它不可靠，也已經不建議使用。你不能保證垃圾回收器什麼時候執行，也不能保證它一定會執行。

如果需要清理資源，請明確寫清理流程。

---

## 九、名稱隱藏（Name Hiding / 名称隐藏）

如果父類別有多個同名但參數不同的方法，這叫方法多載（Overloading / 重载）。

在 Java 中，子類別新增一個同名但參數不同的方法，不會把父類別的其他多載版本藏起來。

範例：

```java
class Homer {
  char doh(char c) {
    System.out.println("doh(char)");
    return 'd';
  }

  float doh(float f) {
    System.out.println("doh(float)");
    return 1.0f;
  }
}

class Milhouse {}

class Bart extends Homer {
  void doh(Milhouse m) {
    System.out.println("doh(Milhouse)");
  }
}

public class Hide {
  public static void main(String[] args) {
    Bart b = new Bart();
    b.doh(1);
    b.doh('x');
    b.doh(1.0f);
    b.doh(new Milhouse());
  }
}
```

輸出：

```text
doh(float)
doh(char)
doh(float)
doh(Milhouse)
```

### 多載 vs 覆寫

| 名稱 | 英文        | 簡中 | 判斷方式                       |
| ---- | ----------- | ---- | ------------------------------ |
| 多載 | Overloading | 重载 | 方法名稱相同，但參數不同       |
| 覆寫 | Overriding  | 覆写 | 子類別重新定義父類別同簽名方法 |

如果你本來想覆寫，但不小心寫成多載，`@Override` 會幫你抓錯。

例如：

```java
class Lisa extends Homer {
  @Override
  void doh(Milhouse m) {
    System.out.println("doh(Milhouse)");
  }
}
```

這會編譯失敗，因為 `Homer` 沒有 `doh(Milhouse)` 這個方法可讓 `Lisa` 覆寫。

---

## 十、組合與繼承怎麼選？

組合與繼承都可以用既有類別建立新類別，但它們代表不同的設計關係。

### 使用組合的情況

當你只是想在新類別中使用既有類別的功能，通常應該使用組合。

也就是：

```text
新類別有一個既有類別物件。
```

例如：

```text
Car 有 Engine。
Car 有 Wheel。
Car 有 Door。
```

這是 has-a 關係。

範例：

```java
class Engine {
  public void start() {}
  public void rev() {}
  public void stop() {}
}

class Wheel {
  public void inflate(int psi) {}
}

class Window {
  public void rollup() {}
  public void rolldown() {}
}

class Door {
  public Window window = new Window();

  public void open() {}
  public void close() {}
}

public class Car {
  public Engine engine = new Engine();
  public Wheel[] wheel = new Wheel[4];
  public Door left = new Door(), right = new Door();

  public Car() {
    for (int i = 0; i < 4; i++) {
      wheel[i] = new Wheel();
    }
  }

  public static void main(String[] args) {
    Car car = new Car();
    car.left.window.rollup();
    car.wheel[0].inflate(72);
  }
}
```

這個例子把成員設為 `public`，是為了示範「車由這些部件組成」。

但實務上大多數欄位仍然應該設為 `private`，再透過方法控制外部可以怎麼用。

### 使用繼承的情況

當新類別真的是既有類別的一種，才適合使用繼承。

也就是：

```text
新類別是一種既有類別。
```

例如：

```text
Car 是一種 Vehicle。
Dog 是一種 Animal。
AdminUser 是一種 User。
```

這是 is-a 關係。

### 判斷問題

你可以問自己：

> 我之後是否需要把這個新類別向上轉型成父類別來使用？

如果答案是「會」，繼承可能合理。

如果答案是「不會」，通常優先考慮組合或委派。

---

## 十一、`protected`

當你學到繼承後，`protected` 就開始有意義。

理想情況下，欄位應該盡量 `private`，不讓外部直接碰到內部實作。

但有時候你希望：

- 對一般使用者隱藏
- 但允許子類別使用

這時候可以使用 `protected`。

`protected` 的意思是：

> 對外部使用者來說像 `private`，但同一個 package 裡的類別，以及繼承它的子類別可以存取。

範例：

```java
class Villain {
  private String name;

  protected void set(String nm) {
    name = nm;
  }

  Villain(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "I'm a Villain and my name is " + name;
  }
}

public class Orc extends Villain {
  private int orcNumber;

  public Orc(String name, int orcNumber) {
    super(name);
    this.orcNumber = orcNumber;
  }

  public void change(String name, int orcNumber) {
    set(name);
    this.orcNumber = orcNumber;
  }

  @Override
  public String toString() {
    return "Orc " + orcNumber + ": " + super.toString();
  }

  public static void main(String[] args) {
    Orc orc = new Orc("Limburger", 12);
    System.out.println(orc);
    orc.change("Bob", 19);
    System.out.println(orc);
  }
}
```

輸出：

```text
Orc 12: I'm a Villain and my name is Limburger
Orc 19: I'm a Villain and my name is Bob
```

`Orc` 可以呼叫：

```java
set(name);
```

因為 `set()` 是 `protected`，子類別可以使用。

### 實務建議

通常不要把欄位設成 `protected`。

比較好的做法是：

```java
private String name;

protected void setName(String name) {
  this.name = name;
}
```

也就是：

- 欄位保持 `private`
- 必要時用 `protected method` 開放給子類別

這樣比較能保留未來修改內部實作的彈性。

---

## 十二、向上轉型（Upcasting / 向上转型）

繼承最重要的意義，不只是讓子類別取得父類別的方法，而是建立一種型別關係：

> 子類別是父類別的一種。

例如：

```text
Wind 是一種 Instrument。
```

範例：

```java
class Instrument {
  public void play() {}

  static void tune(Instrument i) {
    i.play();
  }
}

public class Wind extends Instrument {
  public static void main(String[] args) {
    Wind flute = new Wind();
    Instrument.tune(flute);
  }
}
```

`tune()` 要的是：

```java
Instrument i
```

但你傳進去的是：

```java
Wind flute
```

這可以成立，因為 `Wind extends Instrument`，所以 `Wind` 也是一種 `Instrument`。

這種把子類別參考轉成父類別參考的行為，叫做向上轉型（Upcasting / 向上转型）。

### 為什麼叫「向上」？

如果用繼承圖來看：

```text
Instrument
    ↑
   Wind
```

`Wind` 比較具體，`Instrument` 比較抽象。

從具體的 `Wind` 轉成比較一般化的 `Instrument`，就叫向上轉型。

### 向上轉型是安全的

因為子類別一定至少擁有父類別的公開方法。

也就是說：

```java
Instrument.tune(flute);
```

`tune()` 只會把 `flute` 當成 `Instrument` 使用。它只要求 `play()`，而 `Wind` 一定有 `play()`。

---

## 十三、再談組合與繼承

雖然物件導向教學常常強調繼承，但實務上不應該看到可以繼承就繼承。

更好的預設策略是：

```text
優先使用組合或委派。
真的需要 is-a 關係與向上轉型時，再使用繼承。
```

原因是組合比較有彈性。

繼承一旦建立後，子類別和父類別會形成比較強的耦合（Coupling / 耦合）。父類別改動時，可能會影響子類別。

組合則比較像把一個功能物件放進來用，你可以替換它、包裝它，也可以只開放部分方法。

---

## 十四、`final` 關鍵字

`final` 的核心意思是：

> 這個東西不能再被改變。

但要看它放在哪裡。

`final` 可以用在：

1. 資料 / 欄位（Data / 数据）
2. 參數（Parameter / 参数）
3. 方法（Method / 方法）
4. 類別（Class / 类）

---

## 十五、`final` 資料

### 1. `final` 基本型別

如果 `final` 修飾基本型別，代表值不能再被改。

```java
private final int valueOne = 9;
```

這個值設定後就不能改：

```java
valueOne++; // 編譯失敗
```

### 2. `static final` 常數

常見常數寫法：

```java
public static final int VALUE_THREE = 39;
```

意思是：

| 修飾詞   | 意義                         |
| -------- | ---------------------------- |
| `public` | 外部可以使用                 |
| `static` | 屬於類別本身，不屬於個別物件 |
| `final`  | 值不可再改                   |

命名慣例：

```java
public static final int MAX_RETRY_COUNT = 3;
```

全部大寫，單字用底線 `_` 分隔。

### 3. `final` 物件參考

這裡很容易誤解。

```java
private final Value v2 = new Value(22);
```

這代表：

> `v2` 不能再指向另一個 `Value` 物件。

但不代表 `Value` 物件本身不能被修改。

例如：

```java
fd1.v2.i++; // 可以，因為物件內容仍可改
fd1.v2 = new Value(0); // 不可以，因為 v2 不能改指向別的物件
```

對陣列也一樣：

```java
private final int[] a = {1, 2, 3};
```

你不能讓 `a` 指向另一個陣列，但你可以改裡面的元素：

```java
a[0] = 99; // 可以
```

---

## 十六、空白 `final`（Blank Final / 空白 final）

空白 `final` 是指宣告時沒有給值的 `final` 欄位。

```java
private final int j;
private final Poppet p;
```

這種欄位必須在每個建構子中初始化。

範例：

```java
class Poppet {
  private int i;

  Poppet(int ii) {
    i = ii;
  }
}

public class BlankFinal {
  private final int i = 0;
  private final int j;
  private final Poppet p;

  public BlankFinal() {
    j = 1;
    p = new Poppet(1);
  }

  public BlankFinal(int x) {
    j = x;
    p = new Poppet(x);
  }

  public static void main(String[] args) {
    new BlankFinal();
    new BlankFinal(47);
  }
}
```

重點：

> `final` 欄位一定要在物件建立完成前被初始化。

所以你可以：

- 在欄位宣告時初始化
- 在每個建構子中初始化

但不能完全不初始化。

---

## 十七、`final` 參數

如果方法參數加上 `final`，代表你不能在方法內改變這個參數變數本身。

範例：

```java
class Gizmo {
  public void spin() {}
}

public class FinalArguments {
  void with(final Gizmo g) {
    // g = new Gizmo(); // 不可以，g 是 final
  }

  void without(Gizmo g) {
    g = new Gizmo(); // 可以，g 不是 final
    g.spin();
  }

  int g(final int i) {
    return i + 1;
  }

  public static void main(String[] args) {
    FinalArguments bf = new FinalArguments();
    bf.without(null);
    bf.with(null);
  }
}
```

注意：

如果參數是物件參考，`final` 代表不能改變參考指向，不代表物件內容不能改。

---

## 十八、`final` 方法

`final` 方法代表：

> 子類別不能覆寫這個方法。

主要用途是鎖住方法行為，避免子類別改掉。

```java
public final void login() {
  // 固定流程，不允許子類別改寫
}
```

### 不建議為了效能亂加 `final`

舊版 Java 曾經有人建議用 `final` 讓方法呼叫變快。但在現代 Java 中，JVM（Java Virtual Machine / Java 虚拟机）和 JIT（Just-In-Time Compiler / 即时编译器）已經會做很多最佳化。

所以現在比較合理的結論是：

> 不要為了微小效能猜測而加 `final`。只有在你真的要禁止覆寫時，才把方法設成 `final`。

---

## 十九、`final` 與 `private`

類別中的 `private` 方法本來就不能被子類別覆寫。

所以 `private` 方法可以理解成隱性 `final`。

```java
private final void f() {}
private void g() {}
```

對 `private` 方法加 `final` 通常沒有額外意義。

範例：

```java
class WithFinals {
  private final void f() {
    System.out.println("WithFinals.f()");
  }

  private void g() {
    System.out.println("WithFinals.g()");
  }
}

class OverridingPrivate extends WithFinals {
  private final void f() {
    System.out.println("OverridingPrivate.f()");
  }

  private void g() {
    System.out.println("OverridingPrivate.g()");
  }
}

class OverridingPrivate2 extends OverridingPrivate {
  public final void f() {
    System.out.println("OverridingPrivate2.f()");
  }

  public void g() {
    System.out.println("OverridingPrivate2.g()");
  }
}

public class FinalOverridingIllusion {
  public static void main(String[] args) {
    OverridingPrivate2 op2 = new OverridingPrivate2();
    op2.f();
    op2.g();
  }
}
```

輸出：

```text
OverridingPrivate2.f()
OverridingPrivate2.g()
```

### 這裡最容易誤會的地方

你可能以為 `OverridingPrivate` 覆寫了 `WithFinals` 的 `f()` 和 `g()`。

但其實沒有。

因為父類別的 `f()` 和 `g()` 是 `private`，它們根本不是父類別對外提供的介面，也不能被子類別看見。

所以子類別只是建立了剛好同名的新方法，不是覆寫。

---

## 二十、`final` 類別

如果一個類別被宣告成 `final`：

```java
final class Dinosaur {
  int i = 7;
  int j = 1;
  void f() {}
}
```

意思是：

> 這個類別不能被繼承。

下面這樣會編譯失敗：

```java
class Further extends Dinosaur {}
```

完整範例：

```java
class SmallBrain {}

final class Dinosaur {
  int i = 7;
  int j = 1;
  SmallBrain x = new SmallBrain();

  void f() {}
}

public class Jurassic {
  public static void main(String[] args) {
    Dinosaur n = new Dinosaur();
    n.f();
    n.i = 40;
    n.j++;
  }
}
```

`final class` 不能被繼承，所以它的方法也不可能被覆寫。

不過它的欄位是否可以修改，要看欄位本身有沒有 `final`。

```java
n.i = 40; // 可以，因為 i 不是 final
```

---

## 二十一、`final` 使用建議

不要太早把類別或方法設成 `final`。

原因是你很難在設計初期就完全預測別人會怎麼使用你的類別。

合理使用 `final` 的情況：

- 你明確不希望類別被繼承。
- 你明確不希望某個方法被覆寫。
- 你要定義常數。
- 你要保證某個欄位初始化後不能重新指定。

不合理使用 `final` 的情況：

- 只是覺得「可能比較快」。
- 沒有明確設計理由，只是到處加。
- 尚未確定類別未來不會被擴充。

> 舊版 Java 關於 `Vector`、`Hashtable`、`final` 效能的討論，現在主要當歷史背景看即可。現代實務上，集合通常優先使用 `ArrayList`、`HashMap` 等較新的集合類別；`Vector` 和 `Hashtable` 多半只會在維護舊系統時遇到。

---

## 二十二、類別初始化與載入（Class Initialization and Loading / 类初始化和加载）

在一些傳統語言裡，程式啟動時可能會一次載入大量程式碼。

Java 的方式不同。Java 類別通常是在「第一次被使用」時才載入。

常見觸發類別載入的情況：

- 建立該類別的第一個物件
- 存取該類別的 `static` 欄位
- 呼叫該類別的 `static` 方法

建構子雖然沒有寫 `static`，但從類別載入角度看，建立物件也會觸發該類別的載入。

### static 初始化順序

類別載入後，`static` 欄位與 `static` 區塊會依照程式碼中的出現順序初始化。

而且 `static` 欄位只會初始化一次。

---

## 二十三、繼承與初始化完整流程

範例：

```java
class Insect {
  private int i = 9;
  protected int j;

  Insect() {
    System.out.println("i = " + i + ", j = " + j);
    j = 39;
  }

  private static int x1 = printInit("static Insect.x1 initialized");

  static int printInit(String s) {
    System.out.println(s);
    return 47;
  }
}

public class Beetle extends Insect {
  private int k = printInit("Beetle.k initialized");

  public Beetle() {
    System.out.println("k = " + k);
    System.out.println("j = " + j);
  }

  private static int x2 = printInit("static Beetle.x2 initialized");

  public static void main(String[] args) {
    System.out.println("Beetle constructor");
    Beetle b = new Beetle();
  }
}
```

輸出：

```text
static Insect.x1 initialized
static Beetle.x2 initialized
Beetle constructor
i = 9, j = 0
Beetle.k initialized
k = 47
j = 39
```

### 完整順序

當執行：

```bash
java Beetle
```

Java 會先找 `Beetle.main()`。因為 `main()` 是 `static` 方法，所以會觸發 `Beetle` 類別載入。

但 `Beetle extends Insect`，所以 Java 會先載入父類別 `Insect`。

順序大致是：

1. 載入父類別 `Insect`
2. 初始化 `Insect` 的 `static` 欄位
3. 載入子類別 `Beetle`
4. 初始化 `Beetle` 的 `static` 欄位
5. 進入 `main()`
6. 建立 `Beetle` 物件
7. 物件記憶體先歸零：基本型別變 `0`，物件參考變 `null`
8. 呼叫父類別建構子 `Insect()`
9. 初始化子類別的實例欄位 `k`
10. 執行子類別建構子 `Beetle()` 的內容

### 為什麼 `j` 一開始是 `0`？

在 `Insect()` 裡：

```java
System.out.println("i = " + i + ", j = " + j);
j = 39;
```

`j` 還沒被手動設成 `39` 前，它是 `int` 的預設值 `0`。

所以先印出：

```text
i = 9, j = 0
```

接著才執行：

```java
j = 39;
```

後面 `Beetle()` 印出 `j` 時，就會看到：

```text
j = 39
```

---

## 本章小結

本章重點是用既有型別建立新型別。

### 1. 組合

組合（Composition / 组合）是把既有類別的物件放進新類別裡。

白話：

```text
A 有一個 B。
```

適合 has-a 關係。

### 2. 繼承

繼承（Inheritance / 继承）是建立既有類別的新版本。

白話：

```text
A 是一種 B。
```

適合 is-a 關係。

### 3. 優先順序

實務設計時，通常優先考慮：

```text
組合 / 委派 > 繼承
```

除非你真的需要：

- is-a 關係
- 向上轉型
- 多型（Polymorphism / 多态）

否則不要急著使用繼承。

### 4. 初始化順序

繼承中的初始化大方向是：

```text
父類別 static → 子類別 static → 父類別建構子 → 子類別欄位初始化 → 子類別建構子
```

### 5. `final`

`final` 的核心概念是「不能再改」，但不同位置意思不同：

| 位置             | 意思                                   |
| ---------------- | -------------------------------------- |
| `final` 基本型別 | 值不能改                               |
| `final` 物件參考 | 不能改指向別的物件，但物件內容可能可改 |
| `final` 方法     | 子類別不能覆寫                         |
| `final` 類別     | 不能被繼承                             |

### 6. 面試高頻重點

這章和面試高度相關，尤其是：

- 組合 vs 繼承怎麼選
- has-a vs is-a
- `super` 的用途
- 建構子初始化順序
- `@Override` 的用途
- overload vs override
- `protected` 和 `private` 的差異
- upcasting 為什麼安全
- `final` 修飾基本型別、參考、方法、類別的差異
- 為什麼不要依賴 `finalize()` 做資源清理

---

## 本章一句話總結

**組合是「我有什麼」，繼承是「我是什麼」。實務上先想組合，真的需要 is-a 和向上轉型時，再使用繼承。**

## 批改總覽

| 區塊            |             結果 |
| --------------- | ---------------: |
| Q1～Q10 選擇題  |          10 / 10 |
| Q11～Q15 判斷題 |            4 / 5 |
| Q16～Q20 觀念題 |       約 4.5 / 5 |
| Q21～Q25 輸出題 |            3 / 5 |
| **總分**        | **約 21.5 / 25** |

主要錯在：

- **Q13**：Java 不會強制你初始化所有成員物件。
- **Q23**：`toString()` 回傳 `Constructed`，不是印出 `Constructed` 一行，也不是 `source = sprinklers`。
- **Q25**：向上轉型後呼叫覆寫方法，執行的是實際物件 `Wind` 的 `play()`，不是父類別的 `play()`。

---

# Q1

## 完整問題

「組合（Composition）」最接近哪一種關係？

A. A 是一種 B
B. A 有一個 B
C. A 取代 B
D. A 複製 B 的程式碼

## 我的回答

B

## 正確答案

B

## 完整詳解

組合（Composition）代表一個類別裡面**持有另一個物件**。

例如：

```java
class Car {
    private Engine engine = new Engine();
}
```

意思是：

```text
Car 有一個 Engine。
```

所以是 **has-a 關係**。

## 本題觀念

- Composition / 組合 / 组合
- has-a 關係

## 面試高頻

高。常問「組合 vs 繼承」。

---

# Q2

## 完整問題

「繼承（Inheritance）」最接近哪一種關係？

A. A 有一個 B
B. A 使用 B 的工具方法
C. A 是一種 B
D. A 把 B 當成參數傳入

## 我的回答

C

## 正確答案

C

## 完整詳解

繼承代表子類別是父類別的一種。

例如：

```java
class Dog extends Animal {}
```

意思是：

```text
Dog 是一種 Animal。
```

所以是 **is-a 關係**。

## 本題觀念

- Inheritance / 繼承 / 继承
- is-a 關係

## 面試高頻

高。

---

# Q3

## 完整問題

下面哪一行最明顯是「組合」？

```java
class Engine {}

class Car {
    private Engine engine = new Engine();
}
```

A. `class Engine {}`
B. `class Car {`
C. `private Engine engine = new Engine();`
D. `}`

## 我的回答

C

## 正確答案

C

## 完整詳解

這行：

```java
private Engine engine = new Engine();
```

代表 `Car` 裡面放了一個 `Engine` 物件。

也就是：

```text
Car has an Engine.
Car 有一個 Engine。
```

這就是組合。

## 本題觀念

- 類別欄位持有另一個物件
- has-a 關係

## 面試高頻

高。

---

# Q4

## 完整問題

下面哪一行最明顯是「繼承」？

```java
class Animal {}

class Dog extends Animal {}
```

A. `class Animal {}`
B. `class Dog extends Animal {}`
C. `Animal` 是物件
D. `Dog` 是方法

## 我的回答

B

## 正確答案

B

## 完整詳解

`extends` 是 Java 繼承的關鍵字。

```java
class Dog extends Animal {}
```

意思是：

```text
Dog 是一種 Animal。
```

## 本題觀念

- `extends`
- 子類別 / 父類別
- is-a 關係

## 面試高頻

高。

---

# Q5

## 完整問題

如果類別欄位是物件參考，但沒有初始化，它的預設值是什麼？

A. `0`
B. `false`
C. `null`
D. 空字串 `""`

## 我的回答

C

## 正確答案

C

## 完整詳解

物件參考欄位如果沒有初始化，預設值是：

```java
null
```

例如：

```java
private String name;
private User user;
```

如果沒有指定值：

```text
name = null
user = null
```

注意：印出 `null` 不會報錯，但對 `null` 呼叫方法會發生 `NullPointerException`。

## 本題觀念

- 物件參考預設值
- `null`
- `NullPointerException`

## 面試高頻

高。

---

# Q6

## 完整問題

如果類別欄位是 `int`，但沒有初始化，它的預設值是什麼？

A. `null`
B. `0`
C. `false`
D. `1`

## 我的回答

B

## 正確答案

B

## 完整詳解

`int` 是基本型別（Primitive Type）。

類別欄位中的 `int` 如果沒有初始化，預設值是：

```java
0
```

例如：

```java
private int count;
```

預設：

```text
count = 0
```

## 本題觀念

- 基本型別預設值
- `int` 預設為 `0`

## 面試高頻

中高。

---

# Q7

## 完整問題

`System.out.println(someObject);` 通常會自動呼叫哪個方法？

A. `print()`
B. `main()`
C. `toString()`
D. `equals()`

## 我的回答

C

## 正確答案

C

## 完整詳解

當你印出一個物件：

```java
System.out.println(someObject);
```

Java 會嘗試把物件轉成字串。

所以會呼叫：

```java
someObject.toString()
```

這也是為什麼自訂類別常常會覆寫 `toString()`。

## 本題觀念

- `toString()`
- 物件轉字串
- `System.out.println(object)`

## 面試高頻

中高。

---

# Q8

## 完整問題

`@Override` 的主要用途是什麼？

A. 強制方法變成 `static`
B. 告訴編譯器：我打算覆寫父類別方法
C. 讓方法執行更快
D. 讓欄位變成不可改

## 我的回答

B

## 正確答案

B

## 完整詳解

`@Override` 是註解（Annotation）。

它的意思是：

```text
我打算覆寫父類別的方法。
```

如果你拼錯方法名稱，或參數列表不一樣，編譯器會報錯。

例如：

```java
@Override
public String tostring() {
    return "x";
}
```

這會錯，因為正確方法名稱是：

```java
toString()
```

不是：

```java
tostring()
```

## 本題觀念

- `@Override`
- 覆寫檢查
- 防止把覆寫誤寫成多載

## 面試高頻

高。

---

# Q9

## 完整問題

在子類別建構子中，`super(...)` 的作用是什麼？

A. 呼叫目前類別自己的建構子
B. 呼叫父類別建構子
C. 建立一個新的物件陣列
D. 呼叫 `main()`

## 我的回答

B

## 正確答案

B

## 完整詳解

`super(...)` 是呼叫父類別建構子。

例如：

```java
class Animal {
    Animal(String name) {}
}

class Dog extends Animal {
    Dog() {
        super("dog");
    }
}
```

這裡：

```java
super("dog");
```

是在呼叫：

```java
Animal(String name)
```

## 本題觀念

- `super(...)`
- 父類別建構子
- 子類別初始化前必須先初始化父類別

## 面試高頻

高。

---

# Q10

## 完整問題

如果父類別只有帶參數建構子，子類別建構子沒有寫 `super(...)`，通常會怎樣？

A. 正常編譯
B. Java 自動猜一個參數
C. 編譯失敗
D. 執行時才失敗

## 我的回答

C

## 正確答案

C

## 完整詳解

如果父類別只有：

```java
class Game {
    Game(int i) {}
}
```

沒有：

```java
Game() {}
```

那子類別如果沒寫：

```java
super(某個值);
```

編譯器會自動嘗試呼叫：

```java
super();
```

但父類別沒有無參數建構子，所以會編譯失敗。

## 本題觀念

- 父類別無參數建構子
- `super()`
- `super(...)` 必須明確呼叫

## 面試高頻

高。

---

# Q11

## 完整問題

`private Spoon sp;` 這一行會直接建立一個 `Spoon` 物件。

請答：對 / 錯

## 我的回答

F / 錯

## 正確答案

錯

## 完整詳解

```java
private Spoon sp;
```

這只是宣告一個參考變數。

它的狀態是：

```java
sp = null;
```

它不會自動建立 `Spoon` 物件。

真的建立物件要寫：

```java
sp = new Spoon();
```

## 本題觀念

- 宣告參考變數
- `new` 才會建立物件
- 預設值 `null`

## 面試高頻

高。

---

# Q12

## 完整問題

`sp = new Spoon();` 才是真的建立 `Spoon` 物件。

請答：對 / 錯

## 我的回答

T / 對

## 正確答案

對

## 完整詳解

這行：

```java
sp = new Spoon();
```

分成兩件事：

```java
new Spoon()
```

建立一個新的 `Spoon` 物件。

```java
sp = ...
```

讓 `sp` 指向這個物件。

所以這才是真的建立物件。

## 本題觀念

- `new`
- 物件建立
- 參考指向物件

## 面試高頻

高。

---

# Q13

## 完整問題

Java 會強制你初始化所有成員物件，不然不能編譯。

請答：對 / 錯

## 我的回答

T / 對

## 正確答案

錯

## 完整詳解

Java **不會強制你初始化所有成員物件**。

例如：

```java
class A {
    private Spoon sp;
}
```

這可以編譯。

只是 `sp` 會是：

```java
null
```

如果你之後呼叫：

```java
sp.toString();
```

才會在執行時發生：

```text
NullPointerException
```

所以這題的關鍵是：

```text
沒有初始化成員物件，不一定編譯錯。
但使用 null 物件呼叫方法時，會執行期錯誤。
```

## 本題觀念

- Java 不強制初始化所有物件欄位
- `null`
- 編譯期錯誤 vs 執行期錯誤

## 面試高頻

高。

---

# Q14

## 完整問題

`private` 方法不能被子類別真正覆寫。

請答：對 / 錯

## 我的回答

T / 對

## 正確答案

對

## 完整詳解

`private` 方法只屬於目前類別內部，子類別看不到。

所以子類別就算寫了同名方法，也不是覆寫。

例如：

```java
class Parent {
    private void run() {}
}

class Child extends Parent {
    public void run() {}
}
```

`Child.run()` 不是覆寫 `Parent.run()`。

它只是建立了一個新的同名方法。

## 本題觀念

- `private`
- 覆寫限制
- 父類別介面的一部分才有覆寫意義

## 面試高頻

高。

---

# Q15

## 完整問題

`final` 修飾物件參考時，代表「參考不能改指向別的物件」，但物件內容可能仍可修改。

請答：對 / 錯

## 我的回答

T / 對

## 正確答案

對

## 完整詳解

例如：

```java
final User user = new User();
```

這代表：

```text
user 這個變數不能再指向別的 User 物件。
```

所以不能：

```java
user = new User();
```

但如果 `User` 物件本身有可修改欄位，仍可能可以：

```java
user.setName("Zane");
```

所以 `final` 修飾物件參考時，鎖住的是「參考」，不是一定鎖住「物件內容」。

## 本題觀念

- `final` reference
- 參考不可重新指定
- 物件內容未必不可變

## 面試高頻

高。

---

# Q16

## 完整問題

請用一句話說明：組合和繼承最大的差別是什麼？

## 我的回答

is A or has A 的差別

## 正確答案

方向正確，但建議寫完整一點：

```text
繼承是 is-a 關係，代表 A 是一種 B；組合是 has-a 關係，代表 A 裡面有一個 B。
```

## 完整詳解

你答的核心是對的。

只是考試或面試時建議補完整：

- 組合：A has a B
- 繼承：A is a B

例如：

```java
class Car {
    private Engine engine;
}
```

這是組合：

```text
Car 有 Engine。
```

```java
class Dog extends Animal {}
```

這是繼承：

```text
Dog 是一種 Animal。
```

## 本題觀念

- has-a
- is-a
- 組合 vs 繼承

## 面試高頻

高。

---

# Q17

## 完整問題

為什麼實務上通常建議「優先使用組合或委派，而不是一開始就用繼承」？

## 我的回答

避免耦合

## 正確答案

方向正確，但可以補成：

```text
因為繼承會讓子類別和父類別形成強耦合，父類別改動可能影響子類別；組合和委派比較彈性，可以替換內部物件，也可以只暴露需要的方法。
```

## 完整詳解

你答「避免耦合」是核心重點，正確。

但面試時最好講完整：

繼承的問題是：

```text
子類別依賴父類別的設計。
父類別一改，子類別可能受影響。
```

組合的好處是：

```text
我只是持有一個物件。
我要換掉它、包裝它、只開放部分功能，都比較容易。
```

例如在 Spring 專案裡：

```java
private final JwtService jwtService;
```

通常比：

```java
class AuthController extends JwtService
```

合理。

因為 `AuthController` 不是一種 `JwtService`，只是「有一個 JwtService 可以用」。

## 本題觀念

- Coupling / 耦合
- Composition over inheritance
- 委派
- 可替換性

## 面試高頻

非常高。

---

# Q18

## 完整問題

什麼是向上轉型（Upcasting）？請用 `Wind extends Instrument` 的例子說明。

## 我的回答

你貼了這段程式：

```java
class Instrument {
    public void play() {
        System.out.println("Instrument play");
    }

    static void tune(Instrument i) {
        i.play();
    }
}

public class Wind extends Instrument {
    @Override
    public void play() {
        System.out.println("Wind play");
    }

    public static void main(String[] args) {
        Wind flute = new Wind();
        Instrument.tune(flute);
    }
}
```

## 正確答案

你的程式例子是對的，但少了文字解釋。

完整答案應該是：

```text
Wind extends Instrument，所以 Wind 是一種 Instrument。
當 tune(Instrument i) 需要 Instrument 型別時，可以把 Wind flute 傳進去。
這種把子類別物件用父類別型別接住或傳遞的行為，就是向上轉型。
```

也可以寫成：

```java
Wind flute = new Wind();
Instrument i = flute;
```

這就是向上轉型。

## 完整詳解

重點不是只貼程式，而是要說出：

```text
子類別物件可以被當成父類別型別使用。
```

在你的程式裡：

```java
static void tune(Instrument i) {
    i.play();
}
```

`tune()` 要的是：

```java
Instrument
```

但你傳的是：

```java
Wind
```

```java
Instrument.tune(flute);
```

這可以成立，因為：

```java
Wind extends Instrument
```

所以：

```text
Wind 是一種 Instrument。
```

## 本題觀念

- Upcasting / 向上轉型 / 向上转型
- 子類別轉父類別
- 多型基礎

## 面試高頻

高。

---

# Q19

## 完整問題

為什麼向上轉型是安全的？

## 我的回答

應為你可以確保每個子CLASS都有父CLASS的方法

## 正確答案

正確。建議修成：

```text
因為子類別一定至少擁有父類別定義的可見方法，所以把子類別當成父類別使用時，父類別能呼叫的方法子類別一定也有。
```

## 完整詳解

例如：

```java
class Instrument {
    public void play() {}
}

class Wind extends Instrument {}
```

因為 `Wind extends Instrument`，所以 `Wind` 一定有 `play()`。

因此：

```java
static void tune(Instrument i) {
    i.play();
}
```

不管你傳進去的是：

```java
new Wind()
```

或其他 `Instrument` 子類別，只要它是 `Instrument`，就一定有 `play()`。

## 本題觀念

- 子類別至少具備父類別能力
- Upcasting 安全性
- 編譯器型別檢查

## 面試高頻

高。

---

# Q20

## 完整問題

`protected` 和 `private` 最大差異是什麼？請用「子類別能不能存取」來回答。

## 我的回答

protected子類別能存取 private則否

## 正確答案

正確。

完整說法：

```text
protected 成員可以被子類別存取；private 成員只能在原本類別內部存取，子類別不能直接存取。
```

## 完整詳解

例如：

```java
class Parent {
    protected void a() {}
    private void b() {}
}

class Child extends Parent {
    void test() {
        a(); // 可以
        b(); // 不可以
    }
}
```

補充：`protected` 在同一個 package 內也可存取，不只是子類別。

## 本題觀念

- `protected`
- `private`
- 繼承中的可見性

## 面試高頻

高。

---

# Q21

## 完整問題

請寫出輸出結果

```java
class A {
    A() {
        System.out.println("A constructor");
    }
}

class B extends A {
    B() {
        System.out.println("B constructor");
    }
}

public class Test {
    public static void main(String[] args) {
        B b = new B();
    }
}
```

## 我的回答

```text
A constructor
B constructor
```

## 正確答案

```text
A constructor
B constructor
```

## 完整詳解

建立：

```java
new B();
```

因為：

```java
class B extends A
```

所以建立 `B` 之前，要先建立父類別 `A` 那一層。

因此順序是：

```text
A constructor
B constructor
```

## 本題觀念

- 父類別建構子先執行
- 子類別建構子後執行

## 面試高頻

高。

---

# Q22

## 完整問題

請寫出輸出結果

```java
class Tool {
    Tool() {
        System.out.println("Tool constructor");
    }
}

class Hammer extends Tool {
    Hammer() {
        super();
        System.out.println("Hammer constructor");
    }
}

public class Test {
    public static void main(String[] args) {
        Hammer h = new Hammer();
    }
}
```

## 我的回答

```text
Tool constructor
Hammer constructor
```

## 正確答案

```text
Tool constructor
Hammer constructor
```

## 完整詳解

`Hammer` 繼承 `Tool`：

```java
class Hammer extends Tool
```

`Hammer()` 裡面第一行：

```java
super();
```

會呼叫父類別：

```java
Tool()
```

所以先印：

```text
Tool constructor
```

再印：

```text
Hammer constructor
```

## 本題觀念

- `super()`
- 父類別無參數建構子
- 建構子順序

## 面試高頻

高。

---

# Q23

## 完整問題

請寫出輸出結果

```java
class WaterSource {
    private String s;

    WaterSource() {
        System.out.println("WaterSource()");
        s = "Constructed";
    }

    @Override
    public String toString() {
        return s;
    }
}

public class SprinklerSystem {
    private WaterSource source = new WaterSource();

    @Override
    public String toString() {
        return "source = " + source;
    }

    public static void main(String[] args) {
        SprinklerSystem sprinklers = new SprinklerSystem();
        System.out.println(sprinklers);
    }
}
```

## 我的回答

```text
WaterSource()
Constructed
source = sprinklers
```

## 正確答案

```text
WaterSource()
source = Constructed
```

## 完整詳解

先看：

```java
SprinklerSystem sprinklers = new SprinklerSystem();
```

建立 `SprinklerSystem` 時，會初始化欄位：

```java
private WaterSource source = new WaterSource();
```

所以會呼叫 `WaterSource()` 建構子：

```java
WaterSource() {
    System.out.println("WaterSource()");
    s = "Constructed";
}
```

因此第一行輸出是：

```text
WaterSource()
```

接著：

```java
System.out.println(sprinklers);
```

會自動呼叫：

```java
sprinklers.toString()
```

而 `SprinklerSystem.toString()` 回傳：

```java
return "source = " + source;
```

這裡的 `source` 是物件，所以 Java 會自動呼叫：

```java
source.toString()
```

`WaterSource.toString()` 是：

```java
public String toString() {
    return s;
}
```

而 `s` 已經被設定成：

```java
"Constructed"
```

所以最後是：

```text
source = Constructed
```

注意：

```java
s = "Constructed";
```

這行只是賦值，不會自己印出 `Constructed`。

所以不會有單獨一行：

```text
Constructed
```

也不會是：

```text
source = sprinklers
```

因為 `source` 不是 `sprinklers`，`source` 是 `WaterSource` 物件。

## 本題觀念

- 欄位初始化
- `new WaterSource()`
- `toString()` 自動呼叫
- 賦值不等於印出

## 面試高頻

高。尤其是 `toString()` 和物件初始化流程。

---

# Q24

## 完整問題

請寫出輸出結果

```java
class Insect {
    private int i = 9;
    protected int j;

    Insect() {
        System.out.println("i = " + i + ", j = " + j);
        j = 39;
    }
}

public class Beetle extends Insect {
    private int k = 47;

    public Beetle() {
        System.out.println("k = " + k);
        System.out.println("j = " + j);
    }

    public static void main(String[] args) {
        Beetle b = new Beetle();
    }
}
```

## 我的回答

```text
i=9,j=0
k=47
j=39
```

## 正確答案

```text
i = 9, j = 0
k = 47
j = 39
```

## 完整詳解

你的答案邏輯正確，只是少了空格，不影響觀念。

建立：

```java
new Beetle();
```

因為：

```java
Beetle extends Insect
```

所以先處理父類別 `Insect`。

`Insect` 裡：

```java
private int i = 9;
protected int j;
```

`i` 被初始化成：

```text
9
```

`j` 沒有指定值，所以是 `int` 預設值：

```text
0
```

所以 `Insect()` 先印：

```text
i = 9, j = 0
```

接著執行：

```java
j = 39;
```

所以之後 `j` 變成 `39`。

接著回到 `Beetle`，欄位：

```java
private int k = 47;
```

所以 `k = 47`。

最後 `Beetle()` 印：

```text
k = 47
j = 39
```

## 本題觀念

- 父類別建構子先跑
- 欄位預設值
- 父類別建構子修改 `protected` 欄位
- 子類別可讀取 `protected j`

## 面試高頻

非常高。

---

# Q25

## 完整問題

請寫出輸出結果

```java
class Instrument {
    public void play() {
        System.out.println("Instrument play");
    }

    static void tune(Instrument i) {
        i.play();
    }
}

public class Wind extends Instrument {
    @Override
    public void play() {
        System.out.println("Wind play");
    }

    public static void main(String[] args) {
        Wind flute = new Wind();
        Instrument.tune(flute);
    }
}
```

## 我的回答

```text
Instrument play
Wind play
```

## 正確答案

```text
Wind play
```

## 完整詳解

這題是本次最重要的一題。

先看：

```java
Wind flute = new Wind();
```

建立的是一個實際物件：

```text
Wind 物件
```

接著：

```java
Instrument.tune(flute);
```

`tune()` 要的是：

```java
Instrument i
```

但傳入的是：

```java
Wind flute
```

因為：

```java
Wind extends Instrument
```

所以 `Wind` 可以被當成 `Instrument` 使用。這是向上轉型。

進入方法後：

```java
static void tune(Instrument i) {
    i.play();
}
```

雖然 `i` 的宣告型別是 `Instrument`，但它實際指向的物件是：

```text
Wind 物件
```

而 `Wind` 覆寫了 `play()`：

```java
@Override
public void play() {
    System.out.println("Wind play");
}
```

所以執行的是 `Wind.play()`。

因此只會輸出：

```text
Wind play
```

不會先呼叫 `Instrument.play()`。

除非你在 `Wind.play()` 裡面明確寫：

```java
super.play();
```

例如：

```java
@Override
public void play() {
    super.play();
    System.out.println("Wind play");
}
```

這樣才會輸出：

```text
Instrument play
Wind play
```

但原題沒有寫 `super.play()`，所以不會印 `Instrument play`。

## 本題觀念

- 向上轉型
- 覆寫
- 動態方法分派（Dynamic Dispatch）
- 多型基礎

## 面試高頻

非常高。這是 Java OOP 面試核心題。

---

# 你目前最需要補強的 3 個點

## 1. `new`、欄位初始化、`toString()` 的流程

Q23 錯在把：

```java
s = "Constructed";
```

理解成會印出 `Constructed`。

但它只是賦值。

會印出內容的只有：

```java
System.out.println(...)
```

或被 `System.out.println(object)` 間接呼叫的 `toString()`。

---

## 2. Java 不會強制初始化所有成員物件

Q13 錯在這裡。

這可以編譯：

```java
private Spoon sp;
```

但 `sp` 是：

```java
null
```

真正危險發生在執行期：

```java
sp.someMethod(); // NullPointerException
```

---

## 3. 向上轉型後，覆寫方法看「實際物件」

Q25 錯在這裡。

這行：

```java
Instrument.tune(flute);
```

雖然方法參數型別是 `Instrument`，但實際物件是 `Wind`。

所以：

```java
i.play();
```

會執行：

```java
Wind.play()
```

不是 `Instrument.play()`。

---

# 建議你現在要記的版本

```text
1. 組合：A 有 B。
2. 繼承：A 是一種 B。
3. 宣告參考變數不等於 new 物件。
4. 物件參考沒初始化就是 null。
5. 子類別建構前，一定先跑父類別建構子。
6. super(...) 是呼叫父類別建構子。
7. private 方法不能被真正覆寫。
8. final 參考不能改指向，但物件內容可能能改。
9. 向上轉型：子類別物件可以當父類別使用。
10. 覆寫方法執行時看實際物件，不是只看參考型別。
```

這份批改是依你上傳的第八章複用講義與原文內容整理；其中組合、繼承、`super`、委派、向上轉型與初始化順序都在講義中集中說明。
