# 第十二章 集合（Collections / 集合）— 台灣繁中可讀整理版

> 調整方向：保留原章節主軸，不重排大架構；將用詞改為台灣繁體中文；第一次出現的重要術語附上英文與簡中；過時或現代較少用的區塊改為提示，不再展開太多；較難懂的寵物範例改成更直觀的資料範例。

---

## 0. 本章先抓大方向

如果程式只需要固定數量的物件（Object / 对象），而且每個物件什麼時候建立、什麼時候消失都很清楚，那程式會很簡單。

但實際開發通常不是這樣。很多物件是在執行期（Runtime / 运行时）才知道要建立幾個。例如：

- 使用者登入後，查出他的所有訂單。
- API 回傳一批商品清單。
- 從資料庫讀出所有符合條件的紀錄。
- 從檔案讀出不固定筆數的資料。

這時候你不可能事先寫：

```java
MyType aReference;
MyType bReference;
MyType cReference;
```

因為你根本不知道需要幾個參考（Reference / 引用）。

Java 可以用陣列（Array / 数组）保存一組資料，但陣列有一個重要限制：

```text
陣列一旦建立，長度就固定。
```

所以 Java 提供 `java.util` 套件中的集合框架（Collections Framework / 集合框架），用來保存數量不固定的物件。

集合框架最常用的四大核心是：

| 類型         | 英文 / 簡中  | 主要用途                        |
| ------------ | ------------ | ------------------------------- |
| `List` 串列  | List / 列表  | 有順序，可重複，用 index 取資料 |
| `Set` 集合   | Set / 集合   | 不允許重複元素                  |
| `Queue` 佇列 | Queue / 队列 | 先進先出，常用於排隊處理        |
| `Map` 對映   | Map / 映射   | 用 key 找 value                 |

一句話先記：

```text
List 看順序。
Set 看唯一性。
Queue 看處理順序。
Map 看 key-value 對應。
```

---

## 1. 泛型與型別安全的集合

### 1.1 為什麼需要泛型？

早期 Java 還沒有泛型（Generics / 泛型）時，集合裡面放的是 `Object`。這代表任何物件都能被放進去。

例如：

```java
import java.util.*;

class Apple {
    long id() {
        return 1;
    }
}

class Orange {}

public class WithoutGenericsDemo {
    public static void main(String[] args) {
        ArrayList apples = new ArrayList();
        apples.add(new Apple());
        apples.add(new Orange()); // 編譯時不會擋

        for (Object item : apples) {
            Apple apple = (Apple) item; // 這裡可能出事
            System.out.println(apple.id());
        }
    }
}
```

這段會編譯成功，但執行到 `Orange` 時會丟出：

```text
ClassCastException
```

原因很直接：

```text
Orange 不是 Apple，不能被強制轉型成 Apple。
```

這種錯誤很危險，因為它不是在編譯期（Compile Time / 编译期）被抓到，而是到執行期才爆。

---

### 1.2 使用泛型後，錯誤會提早到編譯期

改成：

```java
import java.util.*;

class Apple {
    long id() {
        return 1;
    }
}

class Orange {}

public class WithGenericsDemo {
    public static void main(String[] args) {
        ArrayList<Apple> apples = new ArrayList<>();
        apples.add(new Apple());
        // apples.add(new Orange()); // 編譯失敗

        for (Apple apple : apples) {
            System.out.println(apple.id());
        }
    }
}
```

`ArrayList<Apple>` 的意思是：

```text
這個 ArrayList 只能保存 Apple 或 Apple 的子類別。
```

所以 `Orange` 會直接在編譯期被擋下來。

這就是型別安全（Type Safety / 类型安全）。

---

### 1.3 菱形語法

```java
ArrayList<Apple> apples = new ArrayList<>();
```

右邊的 `<>` 稱為菱形語法（Diamond Syntax / 菱形语法）。

在 Java 7 之前，通常要寫成：

```java
ArrayList<Apple> apples = new ArrayList<Apple>();
```

現在右邊可以省略型別，因為編譯器可以從左邊推斷出來。

---

### 1.4 泛型與向上轉型

泛型不代表只能放「完全同一個類別」。如果集合宣告成 `ArrayList<Apple>`，也可以放 `Apple` 的子類別。

```java
class Apple {}
class Fuji extends Apple {}
class Gala extends Apple {}

public class GenericsUpcastingDemo {
    public static void main(String[] args) {
        ArrayList<Apple> apples = new ArrayList<>();
        apples.add(new Fuji());
        apples.add(new Gala());
    }
}
```

這裡發生的是向上轉型（Upcasting / 向上转型）：

```text
Fuji 是 Apple，所以可以被當成 Apple 放進集合。
```

---

## 2. 基本概念：Collection 與 Map

Java 集合框架大致分成兩種核心概念。

### 2.1 Collection：保存一個一個獨立元素

Collection（Collection / 集合）代表「一組元素」。

常見子類型：

| 類型    | 特性             |
| ------- | ---------------- |
| `List`  | 有順序，可重複   |
| `Set`   | 不重複           |
| `Queue` | 依照排隊規則取出 |

例如：

```java
Collection<Integer> numbers = new ArrayList<>();
numbers.add(1);
numbers.add(2);
numbers.add(3);
```

這裡的 `Collection<Integer>` 只保證它是一組 `Integer`，但不特別強調它底層是 `ArrayList`。

---

### 2.2 Map：保存鍵值對

Map 對映（Map / 映射）保存的是鍵值對（Key-Value Pair / 键值对）。

```java
Map<String, Integer> scores = new HashMap<>();
scores.put("Amy", 90);
scores.put("Ben", 80);

System.out.println(scores.get("Amy")); // 90
```

可以把 `Map` 想成：

```text
用一個 key 去查一個 value。
```

Backend 很常用 Map，例如：

- 用 `userId` 查使用者資料。
- 用錯誤碼查錯誤訊息。
- 統計某個值出現幾次。
- 暫存查詢結果。

---

## 3. 介面型別與實作類別

Java 集合很常這樣宣告：

```java
List<String> names = new ArrayList<>();
```

左邊是介面（Interface / 接口）：

```java
List<String>
```

右邊是實作類別（Implementation Class / 实现类）：

```java
new ArrayList<>()
```

這樣寫的好處是：大部分程式只依賴 `List` 這個抽象能力，不綁死在 `ArrayList`。

未來如果真的要換成：

```java
List<String> names = new LinkedList<>();
```

其他使用 `names` 的地方通常不需要大改。

### 3.1 但不是永遠都要用介面型別

如果你需要使用某個具體類別才有的方法，就不能只宣告成介面。

例如 `LinkedList` 有一些 `List` 介面沒有的方法，像 `addFirst()`、`removeFirst()`。如果你真的需要這些方法，就要宣告成：

```java
LinkedList<String> queue = new LinkedList<>();
queue.addFirst("first");
```

但一般情況下，先使用介面型別是比較穩的習慣。

---

## 4. 加入一組元素

Java 常用兩個工具方法加入一批資料。

### 4.1 `Arrays.asList()`

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
```

注意：`Arrays.asList()` 回傳的 `List` 底層仍然連著原本的陣列，大小固定。

所以這可以：

```java
numbers.set(0, 99);
```

但這不行：

```java
numbers.add(6);    // 可能丟 UnsupportedOperationException
numbers.remove(0); // 可能丟 UnsupportedOperationException
```

原因：`add()` / `remove()` 會改變大小，但底層陣列大小不能變。

---

### 4.2 想要可增刪的 List，要包一層 ArrayList

```java
List<Integer> numbers = new ArrayList<>(Arrays.asList(1, 2, 3));
numbers.add(4); // OK
```

這是比較常用、比較安全的寫法。

---

### 4.3 `Collections.addAll()`

```java
List<String> names = new ArrayList<>();
Collections.addAll(names, "Amy", "Ben", "Cindy");
```

這適合先建立空集合，再一次加進多個元素。

---

## 5. 集合的印出

陣列如果直接印，通常會看到奇怪的型別資訊，所以常用：

```java
Arrays.toString(array)
```

集合則已經有比較好讀的 `toString()`。

```java
List<String> list = new ArrayList<>();
list.add("rat");
list.add("cat");
list.add("dog");
list.add("dog");

System.out.println(list);
```

輸出：

```text
[rat, cat, dog, dog]
```

Set 會自動移除重複值：

```java
Set<String> set = new HashSet<>();
set.add("rat");
set.add("cat");
set.add("dog");
set.add("dog");

System.out.println(set);
```

可能輸出：

```text
[rat, cat, dog]
```

注意：`HashSet` 不保證順序。不要因為某次輸出剛好有順序，就寫依賴那個順序的程式。

Map 印出時會長這樣：

```java
Map<String, String> map = new HashMap<>();
map.put("rat", "Fuzzy");
map.put("cat", "Rags");
map.put("dog", "Spot");

System.out.println(map);
```

可能輸出：

```text
{rat=Fuzzy, cat=Rags, dog=Spot}
```

---

## 6. List 串列

List 串列（List / 列表）承諾：

```text
元素有順序，可以用 index 取值，也允許重複。
```

常用方法：

| 方法                | 用途               |
| ------------------- | ------------------ |
| `add(value)`        | 加到尾端           |
| `add(index, value)` | 插入指定位置       |
| `get(index)`        | 依 index 取值      |
| `set(index, value)` | 替換指定位置的元素 |
| `remove(index)`     | 依 index 刪除      |
| `remove(object)`    | 依物件刪除         |
| `contains(object)`  | 是否包含某元素     |
| `indexOf(object)`   | 找元素位置         |
| `size()`            | 元素數量           |
| `clear()`           | 清空               |
| `isEmpty()`         | 是否為空           |

---

### 6.1 ArrayList

ArrayList（ArrayList / 动态数组列表）可以先想成：

```text
可以自動變大的陣列。
```

優點：

- 用 index 隨機存取很快。
- Backend 回傳資料清單時很常用。
- 一般情境首選。

缺點：

- 在中間插入或刪除大量元素時，成本較高，因為後面的元素可能要搬動。

```java
List<String> users = new ArrayList<>();
users.add("Amy");
users.add("Ben");
users.add("Cindy");

System.out.println(users.get(1)); // Ben
```

---

### 6.2 LinkedList

LinkedList 鏈結串列（LinkedList / 链表）底層不是連續陣列，而是節點彼此連接。

優點：

- 頭尾操作方便。
- 可當作 Queue 或 Deque 使用。

缺點：

- 用 index 隨機存取通常比 `ArrayList` 慢。
- 現代 Java 實務中，不是因為「插入刪除」就無腦改用 `LinkedList`。多數一般清單需求仍會先用 `ArrayList`。

簡化判斷：

```text
一般清單：先用 ArrayList。
明確需要 Queue / Deque 行為：再考慮 LinkedList 或 ArrayDeque。
```

---

### 6.3 `remove(object)` 與 `equals()`

List 的 `contains()`、`indexOf()`、`remove(object)` 都會用 `equals()` 判斷兩個物件是否相等。

例如 `String` 是看內容：

```java
List<String> names = new ArrayList<>();
names.add("Amy");

System.out.println(names.contains(new String("Amy"))); // true
```

因為兩個字串內容一樣。

但如果是你自己寫的 class，沒有覆寫 `equals()`，預設通常是看是不是同一個物件參考。

這是之後學 `HashSet`、`HashMap`、`equals()`、`hashCode()` 時的核心觀念。

---

## 7. 迭代器 Iterator

Iterator 迭代器（Iterator / 迭代器）是一個用來走訪集合的物件。

它的重點不是「比較簡短」，而是：

```text
把走訪集合的方式，和集合底層實作分開。
```

基本方法：

| 方法        | 用途                           |
| ----------- | ------------------------------ |
| `hasNext()` | 後面還有沒有元素               |
| `next()`    | 取得下一個元素                 |
| `remove()`  | 移除剛剛由 `next()` 取出的元素 |

範例：

```java
List<String> names = Arrays.asList("Amy", "Ben", "Cindy");
Iterator<String> iterator = names.iterator();

while (iterator.hasNext()) {
    String name = iterator.next();
    System.out.println(name);
}
```

如果只是單純走訪，通常用 for-each 更直覺：

```java
for (String name : names) {
    System.out.println(name);
}
```

---

### 7.1 `Iterable` 與 for-each

Iterable 可迭代物件（Iterable / 可迭代）代表：

```text
這個物件可以產生 Iterator。
```

`for-each` 背後就是依賴 `Iterable`。

所以 Collection 類型通常都可以這樣跑：

```java
for (String name : names) {
    System.out.println(name);
}
```

但 Map 本身不是直接拿來 for-each 的 Collection。通常要透過：

```java
map.keySet()
map.values()
map.entrySet()
```

例如：

```java
Map<String, Integer> scores = new HashMap<>();
scores.put("Amy", 90);
scores.put("Ben", 80);

for (Map.Entry<String, Integer> entry : scores.entrySet()) {
    System.out.println(entry.getKey() + " = " + entry.getValue());
}
```

---

## 8. ListIterator

ListIterator（ListIterator / 列表迭代器）是 Iterator 的加強版，只能用在 List。

它可以：

- 往前走。
- 往後走。
- 取得目前位置前後的 index。
- 用 `set()` 替換最近走訪到的元素。

一般初學先知道即可。實務中最常見的仍是：

```java
for-each
for index loop
stream
```

ListIterator 不是第一優先要熟練的 API。

---

## 9. LinkedList、Queue 與 Deque

LinkedList 除了實作 `List`，也可以當作 Queue 佇列（Queue / 队列）或 Deque 雙端佇列（Deque / 双端队列）。

常見方法：

| 方法                         | 空集合時行為 | 說明               |
| ---------------------------- | ------------ | ------------------ |
| `getFirst()` / `element()`   | 丟例外       | 看第一個，不刪除   |
| `peek()`                     | 回傳 `null`  | 看第一個，不刪除   |
| `removeFirst()` / `remove()` | 丟例外       | 刪除並回傳第一個   |
| `poll()`                     | 回傳 `null`  | 刪除並回傳第一個   |
| `addFirst()`                 | -            | 加到最前面         |
| `addLast()` / `offer()`      | -            | 加到最後面         |
| `removeLast()`               | 丟例外       | 刪除並回傳最後一個 |

工程上要注意：

```text
會丟例外的方法適合「空集合是錯誤」的場景。
會回傳 null 的方法適合「空集合是正常結果」的場景。
```

---

## 10. Stack 堆疊

Stack 堆疊（Stack / 栈）是後進先出（LIFO / 后进先出）：

```text
最後放進去的，最先拿出來。
```

舊版 Java 有 `java.util.Stack`，但它是早期設計，現在新程式通常不建議優先使用。

現代較常使用 `Deque` 搭配 `ArrayDeque`：

```java
Deque<String> stack = new ArrayDeque<>();

stack.push("first");
stack.push("second");

System.out.println(stack.pop()); // second
System.out.println(stack.pop()); // first
```

結論：

```text
新程式要 Stack 行為，優先用 Deque + ArrayDeque。
不要優先用 java.util.Stack。
```

---

## 11. Set 集合

Set 集合（Set / 集合）不保存重複元素。

```java
Set<String> tags = new HashSet<>();
tags.add("java");
tags.add("backend");
tags.add("java");

System.out.println(tags);
```

輸出只會有一個 `java`。

---

### 11.1 HashSet

HashSet（HashSet / 哈希集合）特性：

- 不重複。
- 查找通常很快。
- 不保證順序。

適合：

```text
只關心某個元素有沒有出現。
```

例如：

```java
Set<String> registeredEmails = new HashSet<>();
registeredEmails.add("amy@example.com");

if (registeredEmails.contains("amy@example.com")) {
    System.out.println("already registered");
}
```

---

### 11.2 TreeSet

TreeSet（TreeSet / 树集合）特性：

- 不重複。
- 會依照排序規則保存元素。
- 通常比 HashSet 慢，但可以取得排序結果。

```java
Set<String> names = new TreeSet<>();
names.add("Cindy");
names.add("Amy");
names.add("Ben");

System.out.println(names); // [Amy, Ben, Cindy]
```

---

### 11.3 LinkedHashSet

LinkedHashSet（LinkedHashSet / 链接哈希集合）特性：

- 不重複。
- 保留插入順序。
- 查找也有雜湊（Hash / 哈希）結構的優勢。

```java
Set<String> names = new LinkedHashSet<>();
names.add("Cindy");
names.add("Amy");
names.add("Ben");

System.out.println(names); // [Cindy, Amy, Ben]
```

---

### 11.4 Set 常見操作

| 方法                    | 用途                       |
| ----------------------- | -------------------------- |
| `contains(x)`           | 是否包含 x                 |
| `containsAll(otherSet)` | 是否包含另一組全部元素     |
| `remove(x)`             | 刪除 x                     |
| `removeAll(otherSet)`   | 刪除另一組集合中的所有元素 |
| `addAll(otherSet)`      | 加入另一組集合中的所有元素 |
| `retainAll(otherSet)`   | 只保留兩邊都有的元素       |

這些可以對應到數學集合的：

- 包含。
- 差集。
- 聯集。
- 交集。

---

## 12. Map 對映

Map 對映（Map / 映射）不是 Collection 的子介面。它是另一套結構。

Map 的核心是：

```text
key -> value
```

範例：

```java
Map<String, Integer> inventory = new HashMap<>();
inventory.put("apple", 10);
inventory.put("banana", 5);

System.out.println(inventory.get("apple")); // 10
```

---

### 12.1 `put()` 覆蓋舊值

同一個 key 只能對應一個 value。

```java
Map<String, String> map = new HashMap<>();
map.put("dog", "Bosco");
map.put("dog", "Spot");

System.out.println(map.get("dog")); // Spot
```

第二次 `put()` 會覆蓋第一次的值。

---

### 12.2 HashMap

HashMap（HashMap / 哈希映射）特性：

- 最常用。
- 查找通常很快。
- 不保證 key 的順序。

適合大多數一般 key-value 查詢。

---

### 12.3 TreeMap

TreeMap（TreeMap / 树映射）特性：

- key 會排序。
- 適合需要依 key 升序取得資料的情境。
- 通常比 HashMap 慢。

```java
Map<String, Integer> scores = new TreeMap<>();
scores.put("Cindy", 70);
scores.put("Amy", 90);
scores.put("Ben", 80);

System.out.println(scores); // {Amy=90, Ben=80, Cindy=70}
```

---

### 12.4 LinkedHashMap

LinkedHashMap（LinkedHashMap / 链接哈希映射）特性：

- 保留插入順序。
- 同時有 HashMap 類似的查找特性。

常見用途：

- 需要輸出順序穩定。
- 做簡單 LRU cache 的基礎。
- API response 希望順序可預期。

---

### 12.5 統計出現次數

Map 很適合做統計。

```java
List<String> words = Arrays.asList("java", "spring", "java", "backend");
Map<String, Integer> count = new HashMap<>();

for (String word : words) {
    Integer oldCount = count.get(word);
    if (oldCount == null) {
        count.put(word, 1);
    } else {
        count.put(word, oldCount + 1);
    }
}

System.out.println(count);
```

也可以用現代寫法：

```java
for (String word : words) {
    count.put(word, count.getOrDefault(word, 0) + 1);
}
```

這在 backend 很常見，例如統計：

- 每種狀態的訂單數。
- 每個使用者的操作次數。
- 每個錯誤碼出現幾次。

---

## 13. Queue 佇列

Queue 佇列（Queue / 队列）通常是先進先出（FIFO / 先进先出）：

```text
先放進去的，先拿出來。
```

```java
Queue<String> queue = new LinkedList<>();
queue.offer("first");
queue.offer("second");

System.out.println(queue.poll()); // first
System.out.println(queue.poll()); // second
```

常見方法：

| 方法        | 說明           | 空佇列時           |
| ----------- | -------------- | ------------------ |
| `offer(x)`  | 加入尾端       | 失敗時回傳 `false` |
| `peek()`    | 看隊首，不刪除 | 回傳 `null`        |
| `poll()`    | 取出並刪除隊首 | 回傳 `null`        |
| `element()` | 看隊首，不刪除 | 丟例外             |
| `remove()`  | 取出並刪除隊首 | 丟例外             |

簡化記法：

```text
peek / poll：空了回 null。
element / remove：空了丟例外。
```

---

## 14. PriorityQueue 優先佇列

PriorityQueue 優先佇列（PriorityQueue / 优先队列）不是單純 FIFO。

它的規則是：

```text
每次取出優先權最高的元素。
```

在 Java 預設情況下，數字越小越先被取出。

```java
PriorityQueue<Integer> pq = new PriorityQueue<>();
pq.offer(30);
pq.offer(10);
pq.offer(20);

System.out.println(pq.poll()); // 10
System.out.println(pq.poll()); // 20
System.out.println(pq.poll()); // 30
```

如果要反過來，讓數字大的先出來：

```java
PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
pq.offer(30);
pq.offer(10);
pq.offer(20);

System.out.println(pq.poll()); // 30
```

若要放自己的 class，通常要提供：

- 自然排序（Natural Ordering / 自然排序），例如實作 `Comparable`。
- 或比較器（Comparator / 比较器）。

---

## 15. Collection 與 Iterator 的關係

Collection 是「資料容器」的共同抽象。

Iterator 是「走訪資料」的共同抽象。

所以你可以寫出不依賴特定集合型別的方法：

```java
static void printAll(Iterator<String> iterator) {
    while (iterator.hasNext()) {
        System.out.println(iterator.next());
    }
}
```

也可以直接吃 `Collection`：

```java
static void printAll(Collection<String> items) {
    for (String item : items) {
        System.out.println(item);
    }
}
```

差別：

| 寫法              | 特性                                    |
| ----------------- | --------------------------------------- |
| 接收 `Iterator`   | 最低限度，只需要能走訪                  |
| 接收 `Collection` | 可以用 `size()`、`isEmpty()` 等集合能力 |

實務中，如果你只是要走訪，`Iterable` 或 `Collection` 通常比較好讀。

---

## 16. for-each、陣列與 Iterable

for-each 可以用在：

- 陣列。
- 實作 `Iterable` 的物件。

```java
String[] names = {"Amy", "Ben", "Cindy"};

for (String name : names) {
    System.out.println(name);
}
```

但注意：

```text
陣列可以用 for-each，不代表陣列本身就是 Iterable。
```

所以這不一定成立：

```java
static <T> void test(Iterable<T> iterable) {}

String[] names = {"Amy", "Ben"};
// test(names); // 編譯失敗
```

要轉成 List：

```java
test(Arrays.asList(names));
```

---

## 17. 適配器方法慣用法

適配器方法（Adapter Method / 适配器方法）的重點是：

```text
同一份資料，可以提供不同走訪方式。
```

例如：一個 List 預設是正向走訪，但你想提供反向走訪。

簡化範例：

```java
class ReversibleList<T> extends ArrayList<T> {
    ReversibleList(Collection<T> data) {
        super(data);
    }

    public Iterable<T> reversed() {
        return () -> new Iterator<T>() {
            private int index = size() - 1;

            public boolean hasNext() {
                return index >= 0;
            }

            public T next() {
                return get(index--);
            }
        };
    }
}
```

使用：

```java
ReversibleList<String> words =
    new ReversibleList<>(Arrays.asList("A", "B", "C"));

for (String word : words.reversed()) {
    System.out.println(word);
}
```

這段是進階觀念。先知道它的用途即可，不需要一開始就熟練匿名內部類別或 Lambda。

---

## 18. 過時或較不建議的新程式用法

原文有提到一些舊類別。這裡只保留現代 Java 開發需要知道的結論。

| 舊類別            | 現代建議                                                 |
| ----------------- | -------------------------------------------------------- |
| `Vector`          | 新程式通常用 `ArrayList`                                 |
| `Hashtable`       | 新程式通常用 `HashMap`；需要併發再看 `ConcurrentHashMap` |
| `java.util.Stack` | 新程式通常用 `Deque` + `ArrayDeque`                      |

這些舊類別不是「完全不能用」，而是：

```text
除非維護舊系統，不然新程式通常不要優先選它們。
```

---

## 19. 本章小結

Java 提供多種保存物件的方式。

### 19.1 陣列

陣列（Array / 数组）：

- 長度固定。
- 可以保存基本型別（Primitive Types / 基本类型）。
- 隨機存取快。
- 適合資料數量固定或效能很敏感的場景。

---

### 19.2 Collection

Collection 保存單一元素。

常見子類型：

| 類型    | 重點                       |
| ------- | -------------------------- |
| `List`  | 有順序，可重複，可用 index |
| `Set`   | 不重複                     |
| `Queue` | 排隊規則                   |

集合不能直接保存基本型別，例如 `int`。但 Java 會透過自動裝箱（Autoboxing / 自动装箱）把 `int` 轉成 `Integer`。

```java
List<Integer> numbers = new ArrayList<>();
numbers.add(1); // int 自動裝箱成 Integer
```

---

### 19.3 Map

Map 保存 key-value。

| 類型            | 重點               |
| --------------- | ------------------ |
| `HashMap`       | 查找快，不保證順序 |
| `TreeMap`       | key 會排序         |
| `LinkedHashMap` | 保留插入順序       |

---

### 19.4 常用選擇表

| 需求                     | 常用選擇                                   |
| ------------------------ | ------------------------------------------ |
| 一般清單                 | `ArrayList`                                |
| 不重複元素               | `HashSet`                                  |
| 不重複且要排序           | `TreeSet`                                  |
| 不重複且保留插入順序     | `LinkedHashSet`                            |
| key-value 查詢           | `HashMap`                                  |
| key-value 且 key 排序    | `TreeMap`                                  |
| key-value 且保留插入順序 | `LinkedHashMap`                            |
| FIFO 佇列                | `Queue`，常用 `LinkedList` 或 `ArrayDeque` |
| Stack 行為               | `Deque` + `ArrayDeque`                     |
| 優先權取出               | `PriorityQueue`                            |

---

## 20. 面試高頻觀念

### 20.1 `ArrayList` vs `LinkedList`

| 比較         | ArrayList        | LinkedList                      |
| ------------ | ---------------- | ------------------------------- |
| 底層         | 動態陣列         | 節點鏈結                        |
| 隨機存取     | 快               | 慢                              |
| 頭尾操作     | 一般             | 較方便                          |
| 中間插入刪除 | 可能需要搬動元素 | 找到位置後改鏈結                |
| 實務首選     | 一般清單首選     | 明確需要 queue/deque 行為時再用 |

面試答法不要只背「LinkedList 插入刪除快」。要補一句：

```text
如果要先用 index 找到中間位置，LinkedList 找位置本身也有成本。
```

---

### 20.2 `HashMap` vs `TreeMap` vs `LinkedHashMap`

| 類型            | 順序     | 查找特性     | 使用情境         |
| --------------- | -------- | ------------ | ---------------- |
| `HashMap`       | 不保證   | 通常最快     | 一般 key-value   |
| `TreeMap`       | key 排序 | 通常較慢     | 需要排序         |
| `LinkedHashMap` | 插入順序 | 接近 HashMap | 需要穩定輸出順序 |

---

### 20.3 `HashSet` 怎麼判斷重複？

不是只看 `==`。

HashSet 主要依賴：

```text
hashCode() + equals()
```

之後學 `equals()` 與 `hashCode()` 時要特別注意：

```text
兩個物件 equals() 相等時，hashCode() 必須一致。
```

---

### 20.4 Collection 與 Map 的差別

```text
Collection：一格放一個元素。
Map：一格是一組 key-value。
```

`Map` 不是 `Collection` 的子類型，但可以產生集合視圖：

```java
map.keySet();    // Set<K>
map.values();    // Collection<V>
map.entrySet();  // Set<Map.Entry<K, V>>
```

---

## 21. 你現在學這章要抓的主線

以 Java backend 學習來說，本章不是要把每個 API 全背起來。

你最需要掌握的是：

```text
1. List / Set / Map / Queue 的用途差異。
2. ArrayList / LinkedList 的取捨。
3. HashMap / TreeMap / LinkedHashMap 的取捨。
4. Iterator / Iterable / for-each 的走訪模型。
5. 泛型如何讓集合型別安全。
6. equals() / hashCode() 會影響 Set / Map 的行為。
7. 舊類別 Vector / Hashtable / Stack 新程式不要優先用。
```

能把這些講清楚，這章才算進入 backend 可用程度。

可以。這章改成 **刷題理解模式**。

這一回先不要追求「答對幾題」，而是用題目把底層觀念逼出來。
建議你閱讀方式是：

```text
先看題目
→ 自己心裡猜答案
→ 立刻看詳解
→ 重點放在「為什麼」
```

---

# Java Collections 刷題理解 — 第 1 回

範圍：

```text
Collection / Map
List / Set / Queue
ArrayList / LinkedList
HashSet / TreeSet
HashMap / TreeMap
Iterator / Iterable
Arrays.asList()
PriorityQueue
Deque / Stack
泛型與型別安全
```

---

## Q1. 為什麼 Java 集合框架分成 `Collection` 和 `Map` 兩大類？

### 題目

下列哪個說法最正確？

A. `Map` 是 `Collection` 的子介面，所以所有 `Map` 都可以用 `for-each` 直接遍歷
B. `Collection` 存單一元素，`Map` 存 key-value pair，所以 `Map` 不是 `Collection`
C. `List`、`Set`、`Map`、`Queue` 都直接繼承 `Collection`
D. `Map` 只是 `List` 的加強版，可以用 index 取值

### 正確答案

**B**

---

### 詳解

Java 集合框架裡最重要的分界是：

```text
Collection：一包「單一元素」
Map：一包「key-value 對」
```

例如：

```java
List<String> names = new ArrayList<>();
names.add("Amy");
names.add("Bob");
```

這裡每個元素都是一個 `String`。

但：

```java
Map<String, Integer> scores = new HashMap<>();
scores.put("Amy", 90);
scores.put("Bob", 80);
```

這裡每一筆資料不是單一元素，而是：

```text
key = "Amy"
value = 90
```

所以 `Map` 的資料單位是「一組關聯」。

---

### 為什麼 A 錯？

`Map` **不是** `Collection` 的子介面。

所以不能這樣：

```java
Map<String, Integer> map = new HashMap<>();

for (String s : map) { // 編譯失敗
}
```

會編譯失敗，因為 `Map` 本身不是 `Iterable`。

如果要遍歷 `Map`，通常要用：

```java
for (Map.Entry<String, Integer> entry : map.entrySet()) {
    System.out.println(entry.getKey());
    System.out.println(entry.getValue());
}
```

或只遍歷 key：

```java
for (String key : map.keySet()) {
    System.out.println(key);
}
```

或只遍歷 value：

```java
for (Integer value : map.values()) {
    System.out.println(value);
}
```

---

### 本題觀念

| 類型         | 角色           |
| ------------ | -------------- |
| `Collection` | 存一批單一元素 |
| `List`       | 有順序、可重複 |
| `Set`        | 不重複         |
| `Queue`      | 排隊取出       |
| `Map`        | key-value 對映 |

---

### 面試高頻

**高頻。**

常見問法：

```text
Collection 和 Map 有什麼差別？
Map 為什麼不是 Collection？
HashMap 怎麼遍歷？
```

---

## Q2. `List` 和 `ArrayList` 差在哪？

### 題目

下列程式碼為什麼常這樣寫？

```java
List<String> names = new ArrayList<>();
```

而不是：

```java
ArrayList<String> names = new ArrayList<>();
```

A. 因為 `List` 比 `ArrayList` 執行速度快
B. 因為左邊用介面，可以降低程式對具體實作的依賴
C. 因為 `ArrayList` 已經過時
D. 因為 `List` 可以直接 new

### 正確答案

**B**

---

### 詳解

這行：

```java
List<String> names = new ArrayList<>();
```

要拆成兩邊看。

左邊：

```java
List<String> names
```

意思是：

> 這個變數只承諾自己是一個 `List`。

右邊：

```java
new ArrayList<>()
```

意思是：

> 實際建立的是 `ArrayList` 物件。

---

### 為什麼這樣設計？

因為你的程式大多數時候只需要知道：

```text
這是一個可以依照順序放資料、可以用 index 取資料的 List
```

不一定需要知道它底層是 `ArrayList` 還是 `LinkedList`。

例如：

```java
List<String> names = new ArrayList<>();
names.add("Amy");
names.add("Bob");
System.out.println(names.get(0));
```

如果以後想換實作：

```java
List<String> names = new LinkedList<>();
```

其他程式碼通常不用改。

---

### 但不是永遠都用介面嗎？

不是。

如果你真的需要 `ArrayList` 專屬方法，例如：

```java
ArrayList<String> names = new ArrayList<>();
names.ensureCapacity(1000);
```

那左邊就必須寫 `ArrayList`。

但日常 backend 多數情況只需要：

```java
List
Set
Map
Queue
```

這些介面型別。

---

### 本題觀念

這叫做：

```text
Program to an interface
針對介面寫程式
```

台灣工程上常會講：

> 左邊用介面，右邊用實作類別。

---

### 面試高頻

**高頻。**

常見問法：

```text
List list = new ArrayList() 是什麼意思？
為什麼變數型別用 List 而不是 ArrayList？
```

---

## Q3. `Arrays.asList()` 回傳的 List 為什麼不能 `add()`？

### 題目

下面程式會怎樣？

```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Amy", "Bob");
        names.add("Cindy");
        System.out.println(names);
    }
}
```

A. 輸出 `[Amy, Bob, Cindy]`
B. 編譯失敗
C. 執行時丟出 `UnsupportedOperationException`
D. 輸出 `[Cindy, Amy, Bob]`

### 正確答案

**C**

---

### 詳解

`Arrays.asList("Amy", "Bob")` 會產生一個 `List`，但它不是一般可自由增減長度的 `ArrayList`。

它背後是：

```text
固定長度的陣列包裝成 List
```

所以可以改元素：

```java
List<String> names = Arrays.asList("Amy", "Bob");
names.set(0, "Cindy");

System.out.println(names);
```

輸出：

```text
[Cindy, Bob]
```

但是不能改大小：

```java
names.add("David");    // 執行時錯誤
names.remove("Amy");   // 執行時錯誤
```

因為底層陣列長度固定。

---

### 正確寫法

如果你想要可以 `add()`、`remove()` 的 List，要包一層：

```java
List<String> names = new ArrayList<>(Arrays.asList("Amy", "Bob"));
names.add("Cindy");

System.out.println(names);
```

輸出：

```text
[Amy, Bob, Cindy]
```

---

### 本題觀念

`Arrays.asList()` 回傳的不是「不能改內容」的 List，而是：

```text
可以 set()
不能 add()
不能 remove()
```

原因是它的大小固定。

---

### 面試高頻

**中高頻。**

常見問法：

```text
Arrays.asList() 有什麼坑？
Arrays.asList() 回傳的 List 能不能 add？
```

---

## Q4. `ArrayList` 和 `LinkedList` 到底差在哪？

### 題目

下列哪個說法最精準？

A. `LinkedList` 永遠比 `ArrayList` 快
B. `ArrayList` 適合隨機存取，`LinkedList` 適合已經定位後的插入與刪除
C. `ArrayList` 不能刪除元素
D. `LinkedList` 可以用 index 快速取值

### 正確答案

**B**

---

### 詳解

先講結論：

```text
ArrayList：底層像可變長度陣列
LinkedList：底層像一串節點連起來
```

---

## `ArrayList`

底層概念：

```text
[ A ][ B ][ C ][ D ]
  0    1    2    3
```

優點：

```java
list.get(2);
```

很快，因為可以直接算位置。

缺點：

如果你在中間插入：

```java
list.add(1, "X");
```

原本的元素要往後搬：

```text
原本：
[ A ][ B ][ C ][ D ]

插入 X 到 index 1：
[ A ][ X ][ B ][ C ][ D ]
       ↑
       B、C、D 都要搬
```

---

## `LinkedList`

底層概念：

```text
A <-> B <-> C <-> D
```

每個節點知道前一個和後一個。

如果你已經站在 B 那個節點附近，要插入 X：

```text
A <-> X <-> B <-> C <-> D
```

調整連結就好，不用大量搬資料。

---

### 但 LinkedList 不是中間插入永遠快

這裡很重要。

如果你寫：

```java
list.add(5000, "X");
```

對 `LinkedList` 來說，它仍然要先從頭或尾走到 index 5000。

也就是：

```text
定位很慢
插入本身快
```

所以不能粗暴背：

```text
LinkedList 插入刪除一定比較快
```

比較精準是：

```text
LinkedList 在「已經透過 Iterator 定位」後，插入刪除成本低。
```

---

### 本題觀念

| 操作         | ArrayList          | LinkedList               |
| ------------ | ------------------ | ------------------------ |
| `get(index)` | 快                 | 慢                       |
| 尾端新增     | 快                 | 快                       |
| 中間插入     | 可能慢，因為搬元素 | 定位慢，但節點調整快     |
| 記憶體       | 較省               | 每個節點要額外存前後連結 |

---

### 面試高頻

**非常高頻。**

但面試官通常不喜歡聽：

> LinkedList 插入快，ArrayList 查詢快。

更好的回答是：

> `ArrayList` 適合 random access；`LinkedList` 適合透過 iterator 已定位後的插入刪除。但在現代實務中，多數情況 `ArrayList` 更常用，因為 CPU cache locality 較好，而且很多所謂中間插入場景其實沒有想像多。

---

## Q5. `Set` 怎麼判斷重複？

### 題目

下面程式會輸出什麼？

```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Set<String> set = new HashSet<>();

        set.add("Java");
        set.add("Java");
        set.add(new String("Java"));

        System.out.println(set.size());
    }
}
```

A. `1`
B. `2`
C. `3`
D. 編譯失敗

### 正確答案

**A**

---

### 詳解

`Set` 的核心規則是：

```text
不允許重複元素
```

但「重複」不是看你是不是同一個變數名稱，而是看物件的相等邏輯。

對 `String` 來說，只要字串內容一樣：

```java
"Java".equals(new String("Java"))
```

結果是：

```text
true
```

所以這三次加入：

```java
set.add("Java");
set.add("Java");
set.add(new String("Java"));
```

對 `HashSet` 來說都代表同一個值。

最後只保留一個。

---

### 但如果是自己寫的類別呢？

例如：

```java
class User {
    String email;

    User(String email) {
        this.email = email;
    }
}
```

然後：

```java
Set<User> users = new HashSet<>();
users.add(new User("a@test.com"));
users.add(new User("a@test.com"));

System.out.println(users.size());
```

如果你沒有覆寫 `equals()` 和 `hashCode()`，結果會是：

```text
2
```

因為 Java 預設會把它們當成兩個不同物件。

---

### 本題觀念

`HashSet` 判斷重複會依賴：

```text
hashCode()
equals()
```

簡化流程是：

```text
先看 hashCode 分類
再用 equals 確認是否真的相等
```

---

### 面試高頻

**非常高頻。**

常見問法：

```text
HashSet 如何判斷元素重複？
equals 和 hashCode 有什麼關係？
為什麼重寫 equals 通常也要重寫 hashCode？
```

---

## Q6. `HashSet` 的輸出順序可不可靠？

### 題目

下面程式輸出順序一定是 `[A, B, C]` 嗎？

```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Set<String> set = new HashSet<>();

        set.add("A");
        set.add("B");
        set.add("C");

        System.out.println(set);
    }
}
```

A. 一定是 `[A, B, C]`
B. 一定是 `[C, B, A]`
C. 不保證順序
D. 編譯失敗

### 正確答案

**C**

---

### 詳解

`HashSet` 的重點是：

```text
快速查找
不保證插入順序
不保證排序順序
```

你可能某次執行看到：

```text
[A, B, C]
```

但這不代表語言規格保證如此。

`HashSet` 底層使用 hash 結構，元素放在哪裡跟：

```text
hashCode
table size
rehash
JDK 實作細節
```

都有關。

所以不能寫依賴順序的邏輯。

---

### 如果我要保留插入順序呢？

用：

```java
Set<String> set = new LinkedHashSet<>();
```

例如：

```java
Set<String> set = new LinkedHashSet<>();
set.add("A");
set.add("B");
set.add("C");

System.out.println(set);
```

通常輸出：

```text
[A, B, C]
```

---

### 如果我要排序呢？

用：

```java
Set<String> set = new TreeSet<>();
```

例如：

```java
Set<String> set = new TreeSet<>();
set.add("C");
set.add("A");
set.add("B");

System.out.println(set);
```

輸出：

```text
[A, B, C]
```

---

### 本題觀念

| 類別            | 特性               |
| --------------- | ------------------ |
| `HashSet`       | 不保證順序，查找快 |
| `LinkedHashSet` | 保留插入順序       |
| `TreeSet`       | 依排序規則排列     |

---

### 面試高頻

**高頻。**

常見問法：

```text
HashSet、LinkedHashSet、TreeSet 差在哪？
HashSet 為什麼不保證順序？
```

---

## Q7. `TreeSet` 判斷重複的邏輯跟 `HashSet` 一樣嗎？

### 題目

下面程式會輸出什麼？

```java
import java.util.*;

class User implements Comparable<User> {
    String name;
    int age;

    User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public int compareTo(User other) {
        return this.age - other.age;
    }

    @Override
    public String toString() {
        return name + ":" + age;
    }
}

public class Main {
    public static void main(String[] args) {
        Set<User> users = new TreeSet<>();

        users.add(new User("Amy", 20));
        users.add(new User("Bob", 20));
        users.add(new User("Cindy", 30));

        System.out.println(users);
    }
}
```

A. `[Amy:20, Bob:20, Cindy:30]`
B. `[Amy:20, Cindy:30]`
C. `[Bob:20, Cindy:30]`
D. 編譯失敗

### 正確答案

**B**

---

### 詳解

`TreeSet` 不靠 `hashCode()` 來排位置。

它靠排序規則：

```java
compareTo()
```

這裡的排序規則是：

```java
return this.age - other.age;
```

所以 `TreeSet` 只看年齡。

---

### 插入流程

第一次：

```java
users.add(new User("Amy", 20));
```

放入。

第二次：

```java
users.add(new User("Bob", 20));
```

比較：

```java
Bob.age - Amy.age
20 - 20 = 0
```

`compareTo()` 回傳 `0`，對 `TreeSet` 來說代表：

```text
這兩個元素排序上相等
```

所以它會把 `Bob:20` 當成重複元素，不加入。

第三次：

```java
users.add(new User("Cindy", 30));
```

比較結果不是 0，所以加入。

最後：

```text
[Amy:20, Cindy:30]
```

---

### 重要觀念

對 `HashSet` 來說，重複主要看：

```text
equals + hashCode
```

對 `TreeSet` 來說，重複主要看：

```text
compareTo / Comparator 的比較結果是否為 0
```

這是很多人會搞錯的地方。

---

### 本題觀念

如果你用 `TreeSet<User>`，你的排序規則必須設計清楚。

如果只用 age 排序：

```java
return this.age - other.age;
```

那同年齡的人會被視為同一個排序位置。

比較安全的寫法通常會補第二層比較：

```java
@Override
public int compareTo(User other) {
    int ageCompare = Integer.compare(this.age, other.age);

    if (ageCompare != 0) {
        return ageCompare;
    }

    return this.name.compareTo(other.name);
}
```

這樣 age 一樣時，再比 name。

---

### 面試高頻

**中高頻。**

尤其考：

```text
TreeSet 如何排序？
Comparable 和 Comparator 差在哪？
TreeSet 判斷重複看什麼？
```

---

## Q8. `HashMap.put()` 遇到同一個 key 會怎樣？

### 題目

下面程式輸出什麼？

```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Map<String, Integer> scores = new HashMap<>();

        scores.put("Amy", 80);
        scores.put("Amy", 90);

        System.out.println(scores.size());
        System.out.println(scores.get("Amy"));
    }
}
```

A.

```text
1
90
```

B.

```text
2
80
```

C.

```text
2
90
```

D. 執行時錯誤

### 正確答案

**A**

---

### 詳解

`Map` 的規則是：

```text
key 不可重複
value 可以重複
```

第一次：

```java
scores.put("Amy", 80);
```

Map 裡面是：

```text
Amy -> 80
```

第二次：

```java
scores.put("Amy", 90);
```

同一個 key 又放一次。

所以不是新增一筆，而是覆蓋原本的 value：

```text
Amy -> 90
```

因此：

```java
scores.size()
```

是：

```text
1
```

而：

```java
scores.get("Amy")
```

是：

```text
90
```

---

### key 和 value 的差別

這樣可以：

```java
scores.put("Amy", 90);
scores.put("Bob", 90);
```

因為 value 可以重複。

結果：

```text
Amy -> 90
Bob -> 90
```

但 key 不能重複。

---

### 本題觀念

`Map.put(key, value)` 的語義是：

```text
如果 key 不存在：新增
如果 key 已存在：覆蓋 value
```

---

### 面試高頻

**非常高頻。**

常見問法：

```text
HashMap key 重複會怎樣？
Map 的 key 和 value 哪個可以重複？
put 回傳什麼？
```

補充：`put()` 其實會回傳舊值。

```java
Integer oldValue = scores.put("Amy", 90);
```

如果原本有：

```text
Amy -> 80
```

那 `oldValue` 會是：

```text
80
```

---

## Q9. `Map.get()` 回傳 `null` 一定代表 key 不存在嗎？

### 題目

下面哪個說法正確？

A. `map.get(key) == null` 一定代表 key 不存在
B. `map.get(key) == null` 可能代表 key 不存在，也可能代表 key 對應的 value 本來就是 null
C. `HashMap` 不允許 value 是 null
D. `HashMap` 不允許 key 是 null

### 正確答案

**B**

---

### 詳解

看這段：

```java
Map<String, String> map = new HashMap<>();

map.put("Amy", null);

System.out.println(map.get("Amy"));
System.out.println(map.get("Bob"));
```

兩行都會輸出：

```text
null
null
```

但意思不同。

第一個：

```java
map.get("Amy")
```

是：

```text
key 存在，但 value 是 null
```

第二個：

```java
map.get("Bob")
```

是：

```text
key 不存在，所以 get 回傳 null
```

---

### 所以怎麼判斷 key 是否存在？

用：

```java
map.containsKey("Amy")
```

例如：

```java
if (map.containsKey("Amy")) {
    System.out.println("Amy exists");
}
```

這才是判斷 key 是否存在。

---

### 本題觀念

`get()` 是取值，不是判斷 key 存不存在的完美工具。

比較安全的思路：

```text
我要知道 value 是什麼 → get()
我要知道 key 是否存在 → containsKey()
```

---

### 面試高頻

**中高頻。**

常見問法：

```text
HashMap get 回傳 null 有哪些可能？
containsKey 和 get 的差別？
```

---

## Q10. 為什麼 enhanced for 裡面不能直接 remove？

### 題目

下面程式會怎樣？

```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>();

        names.add("Amy");
        names.add("Bob");
        names.add("Cindy");

        for (String name : names) {
            if (name.equals("Bob")) {
                names.remove(name);
            }
        }

        System.out.println(names);
    }
}
```

A. 正常輸出 `[Amy, Cindy]`
B. 編譯失敗
C. 可能丟出 `ConcurrentModificationException`
D. 輸出 `[Amy, Bob, Cindy]`

### 正確答案

**C**

---

### 詳解

`for-each` 背後其實是用 `Iterator`。

這段：

```java
for (String name : names) {
    ...
}
```

大致等於：

```java
Iterator<String> it = names.iterator();

while (it.hasNext()) {
    String name = it.next();
    ...
}
```

問題在於：

```java
names.remove(name);
```

你是直接透過 `List` 本身刪元素，不是透過 iterator 刪。

Iterator 正在遍歷時，集合結構被外部改掉，它會偵測到資料結構被修改，可能丟出：

```text
ConcurrentModificationException
```

---

### 正確寫法

用 Iterator 自己的 `remove()`：

```java
Iterator<String> it = names.iterator();

while (it.hasNext()) {
    String name = it.next();

    if (name.equals("Bob")) {
        it.remove();
    }
}

System.out.println(names);
```

輸出：

```text
[Amy, Cindy]
```

---

### 為什麼 Iterator.remove() 可以？

因為 Iterator 自己知道目前走到哪裡，也知道它刪掉的是哪一個元素。

它可以維持遍歷狀態一致。

但你直接呼叫：

```java
names.remove(name);
```

Iterator 會覺得：

```text
我正在照原本結構走，結果集合被別人改掉了。
```

所以報錯。

---

### 本題觀念

遍歷時要刪元素，分三種常見方式：

### 方式 1：Iterator

```java
Iterator<String> it = names.iterator();

while (it.hasNext()) {
    if (it.next().equals("Bob")) {
        it.remove();
    }
}
```

### 方式 2：removeIf

```java
names.removeIf(name -> name.equals("Bob"));
```

### 方式 3：建立新 List

```java
List<String> result = new ArrayList<>();

for (String name : names) {
    if (!name.equals("Bob")) {
        result.add(name);
    }
}
```

---

### 面試高頻

**非常高頻。**

常見問法：

```text
for-each 裡面 remove 會怎樣？
ConcurrentModificationException 是什麼？
Iterator.remove() 跟 list.remove() 差在哪？
```

---

## Q11. `Queue` 的 `poll()` 和 `remove()` 差在哪？

### 題目

下面哪個說法正確？

A. `poll()` 和 `remove()` 完全一樣
B. 佇列空的時候，`poll()` 回傳 null，`remove()` 丟例外
C. 佇列空的時候，`poll()` 丟例外，`remove()` 回傳 null
D. `Queue` 不能取出元素

### 正確答案

**B**

---

### 詳解

`Queue` 佇列（Queue / 队列）是：

```text
先進先出 FIFO
First In, First Out
```

例如排隊買票：

```text
先排隊的人先被服務
```

Java 的 `Queue` 有兩組很像的方法。

---

## 失敗時回傳特殊值

| 操作     | 方法      | 空佇列時     |
| -------- | --------- | ------------ |
| 加入     | `offer()` | 回傳 `false` |
| 查看頭部 | `peek()`  | 回傳 `null`  |
| 取出頭部 | `poll()`  | 回傳 `null`  |

---

## 失敗時丟例外

| 操作     | 方法        | 空佇列時 |
| -------- | ----------- | -------- |
| 加入     | `add()`     | 丟例外   |
| 查看頭部 | `element()` | 丟例外   |
| 取出頭部 | `remove()`  | 丟例外   |

---

### 範例

```java
Queue<String> queue = new LinkedList<>();

System.out.println(queue.poll());
```

輸出：

```text
null
```

但：

```java
Queue<String> queue = new LinkedList<>();

System.out.println(queue.remove());
```

會丟：

```text
NoSuchElementException
```

---

### 實務上常用哪個？

通常比較常用：

```java
offer()
peek()
poll()
```

因為它們不會因為空佇列直接炸掉，比較適合一般流程控制。

---

### 本題觀念

`Queue` 不是只有 `add/remove`。

它有一組比較安全的 API：

```text
offer / peek / poll
```

---

### 面試高頻

**中高頻。**

常見問法：

```text
Queue 的 poll 和 remove 差在哪？
peek 和 element 差在哪？
offer 和 add 差在哪？
```

---

## Q12. `PriorityQueue` 是 FIFO 嗎？

### 題目

下面程式輸出什麼？

```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Queue<Integer> queue = new PriorityQueue<>();

        queue.offer(30);
        queue.offer(10);
        queue.offer(20);

        while (!queue.isEmpty()) {
            System.out.print(queue.poll() + " ");
        }
    }
}
```

A. `30 10 20`
B. `10 20 30`
C. `30 20 10`
D. 不一定，完全隨機

### 正確答案

**B**

---

### 詳解

`PriorityQueue` 不是一般 FIFO 佇列。

它的規則是：

```text
每次取出「優先順序最高」的元素
```

對 `Integer` 來說，預設自然排序是：

```text
小的數字優先
```

所以：

```java
queue.offer(30);
queue.offer(10);
queue.offer(20);
```

雖然加入順序是：

```text
30 → 10 → 20
```

但取出時是：

```text
10 → 20 → 30
```

所以輸出：

```text
10 20 30
```

---

### 如果想要大的數字先出來？

可以用：

```java
Queue<Integer> queue = new PriorityQueue<>(Comparator.reverseOrder());
```

完整範例：

```java
Queue<Integer> queue = new PriorityQueue<>(Comparator.reverseOrder());

queue.offer(30);
queue.offer(10);
queue.offer(20);

while (!queue.isEmpty()) {
    System.out.print(queue.poll() + " ");
}
```

輸出：

```text
30 20 10
```

---

### 本題觀念

`PriorityQueue` 的「Queue」容易誤導。

它不是保證先進先出，而是：

```text
依優先順序取出
```

---

### 面試高頻

**中高頻。**

常見問法：

```text
PriorityQueue 是不是 FIFO？
PriorityQueue 預設怎麼排序？
如何改成最大值優先？
```

---

## Q13. 為什麼現在不建議新程式用 `java.util.Stack`？

### 題目

如果你要在現代 Java 寫一個堆疊（Stack / 栈），比較推薦哪種寫法？

A.

```java
Stack<String> stack = new Stack<>();
```

B.

```java
Deque<String> stack = new ArrayDeque<>();
```

C.

```java
List<String> stack = new LinkedList<>();
```

D.

```java
Map<String, String> stack = new HashMap<>();
```

### 正確答案

**B**

---

### 詳解

堆疊 Stack 的規則是：

```text
後進先出 LIFO
Last In, First Out
```

例如：

```text
push A
push B
push C

pop 出來是 C
再來 B
再來 A
```

---

### 現代 Java 推薦

```java
Deque<String> stack = new ArrayDeque<>();

stack.push("A");
stack.push("B");
stack.push("C");

System.out.println(stack.pop());
```

輸出：

```text
C
```

---

### 為什麼不優先用 `java.util.Stack`？

`java.util.Stack` 是舊類別。

它繼承自 `Vector`，而 `Vector` 也是早期設計的 legacy collection。

問題是：

```text
設計過舊
API 暴露過多
同步機制不符合多數現代使用場景
```

所以新程式通常用：

```java
Deque<E> stack = new ArrayDeque<>();
```

---

### 為什麼左邊寫 `Deque`？

因為 `ArrayDeque` 實作了 `Deque`。

`Deque` 是雙端佇列（Double-ended Queue / 双端队列），可以從頭尾兩邊操作。

當成 stack 用時，常用：

```java
push()
pop()
peek()
```

---

### 本題觀念

| 舊寫法          | 現代建議             |
| --------------- | -------------------- |
| `Stack<E>`      | `Deque<E>`           |
| `new Stack<>()` | `new ArrayDeque<>()` |

---

### 面試高頻

**中高頻。**

常見問法：

```text
為什麼不建議用 Stack？
ArrayDeque 可以當 Stack 嗎？
Deque 是什麼？
```

---

## Q14. 泛型為什麼能讓集合更安全？

### 題目

下面哪一段程式會在編譯期就阻止錯誤？

A.

```java
ArrayList list = new ArrayList();
list.add("Java");
list.add(123);
```

B.

```java
ArrayList<String> list = new ArrayList<>();
list.add("Java");
list.add(123);
```

C.

```java
ArrayList list = new ArrayList<String>();
list.add(123);
```

D.

```java
Object list = new ArrayList<String>();
```

### 正確答案

**B**

---

### 詳解

泛型（Generics / 泛型）的核心目標是：

```text
在編譯期檢查型別
```

這段：

```java
ArrayList<String> list = new ArrayList<>();
```

意思是：

```text
這個 ArrayList 只能放 String
```

所以：

```java
list.add("Java");
```

可以。

但：

```java
list.add(123);
```

會編譯失敗。

因為 `123` 是 `int`，自動裝箱後是 `Integer`，不是 `String`。

---

### 沒有泛型會怎樣？

```java
ArrayList list = new ArrayList();

list.add("Java");
list.add(123);
```

這種叫 raw type。

它可以混放不同型別。

問題會延後到取出資料時才爆。

例如：

```java
String s = (String) list.get(1);
```

如果 `list.get(1)` 是 `Integer`，執行時會丟：

```text
ClassCastException
```

---

### 泛型的價值

泛型把錯誤從：

```text
執行時才爆
```

提前到：

```text
編譯時就擋下來
```

這在 backend 很重要，因為你希望錯誤越早被發現越好。

---

### 本題觀念

沒有泛型：

```java
ArrayList list = new ArrayList();
```

不安全。

有泛型：

```java
ArrayList<String> list = new ArrayList<>();
```

安全很多。

---

### 面試高頻

**高頻。**

常見問法：

```text
Java 泛型解決什麼問題？
什麼是 raw type？
為什麼 List<String> 比 List 安全？
```

---

## Q15. `List<Dog>` 可以指派給 `List<Animal>` 嗎？

### 題目

下面程式會怎樣？

```java
import java.util.*;

class Animal {}
class Dog extends Animal {}
class Cat extends Animal {}

public class Main {
    public static void main(String[] args) {
        List<Dog> dogs = new ArrayList<>();
        List<Animal> animals = dogs;

        animals.add(new Cat());
    }
}
```

A. 可以編譯，因為 Dog 是 Animal
B. 編譯失敗
C. 可以編譯，但執行時丟例外
D. 會輸出 Cat

### 正確答案

**B**

---

### 詳解

這題是泛型最容易卡住的地方。

你可能直覺想：

```text
Dog 是 Animal
所以 List<Dog> 應該也是 List<Animal>
```

但 Java 不允許這樣。

原因是如果允許：

```java
List<Dog> dogs = new ArrayList<>();
List<Animal> animals = dogs;
```

那接下來就可以：

```java
animals.add(new Cat());
```

可是 `animals` 實際上指向的是：

```text
一個 List<Dog>
```

如果可以把 `Cat` 放進去，這個 `List<Dog>` 就被污染了。

所以 Java 直接在編譯期禁止：

```java
List<Animal> animals = dogs;
```

---

### 正確理解

物件本身可以向上轉型：

```java
Dog dog = new Dog();
Animal animal = dog;
```

可以。

但泛型集合不自動跟著成立：

```java
List<Dog> dogs = new ArrayList<>();
List<Animal> animals = dogs; // 不可以
```

這叫：

```text
泛型不變性
Invariance
```

---

### 如果只是讀取 Dog 當 Animal 呢？

可以用 wildcard：

```java
List<? extends Animal> animals = dogs;
```

這表示：

```text
這是一個裝著 Animal 或 Animal 子類型的 List
```

可以讀：

```java
Animal a = animals.get(0);
```

但不能安全地加：

```java
animals.add(new Cat()); // 編譯失敗
animals.add(new Dog()); // 也編譯失敗
```

因為編譯器不知道這個 List 真正是：

```text
List<Dog>
List<Cat>
List<Animal>
```

哪一種。

---

### 本題觀念

| 程式                                                  | 可不可以           |
| ----------------------------------------------------- | ------------------ |
| `Animal a = new Dog();`                               | 可以               |
| `List<Animal> list = new ArrayList<Dog>();`           | 不可以             |
| `List<? extends Animal> list = new ArrayList<Dog>();` | 可以，但主要用來讀 |

---

### 面試高頻

**高頻，但偏進階。**

常見問法：

```text
List<Dog> 是不是 List<Animal>？
什麼是泛型不變性？
? extends 和 ? super 差在哪？
```

---

# 這一回最重要的 10 個結論

```text
1. Collection 存單一元素，Map 存 key-value。
2. Map 不是 Collection。
3. 左邊用 List / Set / Map 介面，右邊用具體實作類別。
4. Arrays.asList() 可以 set，但不能 add/remove。
5. ArrayList 擅長 random access。
6. LinkedList 不是永遠比較快，定位成本很高。
7. HashSet 不保證順序。
8. TreeSet 用排序規則判斷位置，compareTo 回傳 0 會被視為重複。
9. HashMap 同 key put 會覆蓋 value。
10. for-each 裡直接 remove 集合，可能 ConcurrentModificationException。
```

---

LinkedList`。
關係。這章最容易卡住的點是：**有些是介面，有些是實作類別，有些只是資料結構概念**。

---

# 1. 先看整體關係圖

```text
Iterable
└── Collection
    ├── List
    │   ├── ArrayList
    │   └── LinkedList
    │
    ├── Set
    │   ├── HashSet
    │   ├── LinkedHashSet
    │   └── TreeSet
    │
    └── Queue
        ├── PriorityQueue
        └── Deque
            ├── ArrayDeque
            └── LinkedList
```

另外：

```text
Iterator 不是集合
Iterator 是「走訪集合的游標物件」
```

還有：

```text
Stack 是一種資料結構概念
java.util.Stack 是舊類別
現代 Java 通常用 Deque / ArrayDeque 來當 Stack
```

---

# 2. 先分清楚：介面、實作類別、概念

| 名稱            | 類型            | 角色                                           |
| --------------- | --------------- | ---------------------------------------------- |
| `Iterable`      | 介面            | 表示「這個東西可以被 for-each 走訪」           |
| `Iterator`      | 介面 / 游標物件 | 實際負責一個一個取元素                         |
| `Collection`    | 介面            | 大多數集合的共同父介面                         |
| `List`          | 介面            | 有順序、可重複、可用 index                     |
| `Queue`         | 介面            | 佇列，通常表示排隊取出                         |
| `Deque`         | 介面            | 雙端佇列，頭尾都能操作                         |
| `ArrayList`     | 類別            | `List` 的常用實作                              |
| `LinkedList`    | 類別            | 同時實作 `List` 和 `Deque`                     |
| `ArrayDeque`    | 類別            | `Deque` 的常用實作                             |
| `PriorityQueue` | 類別            | 依優先順序取出，不是 FIFO                      |
| `Stack`         | 概念 / 舊類別   | 後進先出，現代 Java 不優先用 `java.util.Stack` |

核心差異：

```text
介面 = 規格 / 能做什麼
類別 = 實際怎麼做
概念 = 資料操作規則
```

例如：

```java
List<String> list = new ArrayList<>();
```

意思是：

```text
我需要的是 List 規格
實際用 ArrayList 來做
```

---

# 3. `Iterable`、`Iterator`、`for-each` 的關係

## 3-1. `Iterable` 是什麼？

`Iterable`（可迭代 / 可迭代）代表：

```text
這個物件可以產生 Iterator
```

它的核心方法是：

```java
Iterator<T> iterator();
```

所以只要某個類別實作了 `Iterable`，它就可以被 `for-each` 使用。

例如：

```java
List<String> names = new ArrayList<>();
names.add("Amy");
names.add("Bob");

for (String name : names) {
    System.out.println(name);
}
```

為什麼 `List` 可以用 `for-each`？

因為：

```text
List
→ Collection
→ Iterable
```

所以所有 `List` 都是 `Iterable`。

---

## 3-2. `Iterator` 是什麼？

`Iterator` 是實際走訪資料的人。

它有三個核心方法：

```java
hasNext()
next()
remove()
```

範例：

```java
List<String> names = new ArrayList<>();
names.add("Amy");
names.add("Bob");
names.add("Cindy");

Iterator<String> it = names.iterator();

while (it.hasNext()) {
    String name = it.next();
    System.out.println(name);
}
```

你可以把它想成：

```text
Iterator 是一個游標
它目前站在集合的某個位置
每次 next() 就往下一格
```

---

## 3-3. `for-each` 其實是語法糖

這段：

```java
for (String name : names) {
    System.out.println(name);
}
```

大致等價於：

```java
Iterator<String> it = names.iterator();

while (it.hasNext()) {
    String name = it.next();
    System.out.println(name);
}
```

所以：

```text
for-each 背後其實就是 Iterator
```

這就是為什麼你要理解 `Iterator`，不然 `for-each remove` 的問題會看不懂。

---

# 4. 為什麼 `for-each` 裡直接 `remove()` 會出事？

看這段：

```java
List<String> names = new ArrayList<>();
names.add("Amy");
names.add("Bob");
names.add("Cindy");

for (String name : names) {
    if (name.equals("Bob")) {
        names.remove(name);
    }
}
```

這段可能丟：

```text
ConcurrentModificationException
```

原因不是「不能刪資料」。

真正原因是：

```text
Iterator 正在遍歷集合
但你繞過 Iterator，直接改了集合本體
Iterator 發現集合結構被外部修改
所以丟 ConcurrentModificationException
```

更具體一點：

```text
for-each 背後有一個 Iterator
Iterator 期待集合狀態穩定
你卻用 names.remove(name) 改集合
Iterator 的遍歷狀態失效
```

---

## 正確刪法 1：用 `Iterator.remove()`

```java
Iterator<String> it = names.iterator();

while (it.hasNext()) {
    String name = it.next();

    if (name.equals("Bob")) {
        it.remove();
    }
}
```

這樣可以，因為：

```text
刪除動作是由 Iterator 自己執行
Iterator 知道自己刪掉了哪個元素
遍歷狀態還能維持一致
```

---

## 正確刪法 2：用 `removeIf()`

現代 Java 常用：

```java
names.removeIf(name -> name.equals("Bob"));
```

這比較簡潔。

---

## 正確刪法 3：建立新 List

```java
List<String> result = new ArrayList<>();

for (String name : names) {
    if (!name.equals("Bob")) {
        result.add(name);
    }
}
```

這種方式在資料轉換、篩選流程也很常見。

---

# 5. `Iterator` 和 `ListIterator` 差在哪？

`Iterator` 是一般集合都能用的走訪工具。

```java
Iterator<String> it = names.iterator();
```

它通常只能：

```text
往前走
讀取
刪除目前走過的元素
```

但 `ListIterator` 是 `List` 專用的加強版。

```java
ListIterator<String> it = names.listIterator();
```

它可以：

```text
往前走
往後走
取得 index
set 替換元素
add 插入元素
remove 刪除元素
```

也就是：

```text
Iterator：一般遍歷工具
ListIterator：List 專用強化版
```

但日常 backend 開發不會一直手寫 `ListIterator`，你先知道它存在即可。

---

# 6. `Queue` 是什麼？

`Queue`（佇列 / 队列）是一種排隊模型。

最典型規則是：

```text
FIFO
First In, First Out
先進先出
```

像排隊買便當：

```text
先排隊的人先拿到
後排隊的人後拿到
```

範例：

```java
Queue<String> queue = new LinkedList<>();

queue.offer("A");
queue.offer("B");
queue.offer("C");

System.out.println(queue.poll()); // A
System.out.println(queue.poll()); // B
System.out.println(queue.poll()); // C
```

---

# 7. `Queue` 的兩組 API

這裡很重要。

`Queue` 有兩組很像的方法。

## 7-1. 失敗時回傳特殊值

| 操作     | 方法      | 失敗時       |
| -------- | --------- | ------------ |
| 加入     | `offer()` | 回傳 `false` |
| 查看頭部 | `peek()`  | 回傳 `null`  |
| 取出頭部 | `poll()`  | 回傳 `null`  |

常用：

```java
queue.offer("A");

String head = queue.peek();

String value = queue.poll();
```

---

## 7-2. 失敗時丟例外

| 操作     | 方法        | 失敗時 |
| -------- | ----------- | ------ |
| 加入     | `add()`     | 丟例外 |
| 查看頭部 | `element()` | 丟例外 |
| 取出頭部 | `remove()`  | 丟例外 |

例如空 queue：

```java
Queue<String> queue = new LinkedList<>();

System.out.println(queue.poll());   // null
System.out.println(queue.remove()); // NoSuchElementException
```

所以實務上通常偏好：

```text
offer / peek / poll
```

因為比較好控制流程。

---

# 8. `Deque` 是什麼？

`Deque` 讀作：

```text
deck
```

全名：

```text
Double-ended Queue
雙端佇列 / 双端队列
```

意思是：

```text
頭可以進出
尾也可以進出
```

一般 `Queue` 比較像：

```text
尾巴加入
頭部取出
```

圖：

```text
head                  tail
  ↓                    ↓
[ A ][ B ][ C ][ D ]
取出                加入
```

但 `Deque` 是：

```text
head                  tail
  ↓                    ↓
[ A ][ B ][ C ][ D ]
可加入/取出          可加入/取出
```

---

## `Deque` 常用方法

| 操作 | 頭部                          | 尾部                        |
| ---- | ----------------------------- | --------------------------- |
| 加入 | `addFirst()` / `offerFirst()` | `addLast()` / `offerLast()` |
| 查看 | `peekFirst()`                 | `peekLast()`                |
| 取出 | `pollFirst()`                 | `pollLast()`                |

---

# 9. 為什麼 `Deque` 可以當 Stack？

Stack（堆疊 / 栈）的規則是：

```text
LIFO
Last In, First Out
後進先出
```

像疊盤子：

```text
最後放上去的盤子
最先被拿走
```

`Deque` 有這幾個方法：

```java
push()
pop()
peek()
```

所以可以這樣用：

```java
Deque<String> stack = new ArrayDeque<>();

stack.push("A");
stack.push("B");
stack.push("C");

System.out.println(stack.pop()); // C
System.out.println(stack.pop()); // B
System.out.println(stack.pop()); // A
```

這就是 Stack 行為。

---

# 10. 那 `java.util.Stack` 呢？

Java 裡真的有一個類別叫：

```java
java.util.Stack
```

但它是舊設計。

現代 Java 通常不優先用：

```java
Stack<String> stack = new Stack<>();
```

而是用：

```java
Deque<String> stack = new ArrayDeque<>();
```

原因：

```text
java.util.Stack 是 legacy class
它繼承自 Vector
設計過舊
API 暴露不夠乾淨
```

所以你要把兩件事分開：

```text
Stack 作為資料結構概念：重要
java.util.Stack 這個類別：新程式不優先用
```

---

# 11. `ArrayDeque` 是什麼？

`ArrayDeque` 是 `Deque` 的常用實作。

```java
Deque<String> deque = new ArrayDeque<>();
```

它底層類似可擴充的陣列結構，適合拿來做：

```text
Queue
Deque
Stack
```

常見現代寫法：

## 當 Queue 用

```java
Queue<String> queue = new ArrayDeque<>();

queue.offer("A");
queue.offer("B");

System.out.println(queue.poll()); // A
```

## 當 Stack 用

```java
Deque<String> stack = new ArrayDeque<>();

stack.push("A");
stack.push("B");

System.out.println(stack.pop()); // B
```

所以 `ArrayDeque` 很實用。

---

# 12. `LinkedList` 和 `Deque` 的關係

`LinkedList` 比較特別。

它同時是：

```text
List
Deque
Queue
```

也就是它可以這樣：

```java
List<String> list = new LinkedList<>();
```

也可以這樣：

```java
Queue<String> queue = new LinkedList<>();
```

也可以這樣：

```java
Deque<String> deque = new LinkedList<>();
```

因為 `LinkedList` 的類別宣告大致是這種概念：

```text
LinkedList implements List, Deque
```

所以它有多重角色。

---

## 但實務上怎麼選？

雖然 `LinkedList` 能做很多事，但不代表它是首選。

通常：

| 需求                   | 常見選擇            |
| ---------------------- | ------------------- |
| 一般 List              | `ArrayList`         |
| Stack                  | `ArrayDeque`        |
| Queue                  | `ArrayDeque`        |
| Deque                  | `ArrayDeque`        |
| 需要大量從中間節點操作 | 才考慮 `LinkedList` |

為什麼？

因為 `LinkedList` 每個元素都是節點，要存前後連結，記憶體成本較高，而且 random access 很慢。

---

# 13. `PriorityQueue` 是什麼？

`PriorityQueue`（優先佇列 / 优先队列）也是 `Queue` 的實作。

但它不是 FIFO。

一般 Queue：

```text
先進先出
```

PriorityQueue：

```text
優先順序高的先出
```

對 `Integer` 來說，預設小的數字優先：

```java
Queue<Integer> queue = new PriorityQueue<>();

queue.offer(30);
queue.offer(10);
queue.offer(20);

System.out.println(queue.poll()); // 10
System.out.println(queue.poll()); // 20
System.out.println(queue.poll()); // 30
```

加入順序是：

```text
30 → 10 → 20
```

取出順序是：

```text
10 → 20 → 30
```

所以：

```text
PriorityQueue 不是 FIFO
```

---

## 如果要大的數字先出？

```java
Queue<Integer> queue = new PriorityQueue<>(Comparator.reverseOrder());

queue.offer(30);
queue.offer(10);
queue.offer(20);

System.out.println(queue.poll()); // 30
System.out.println(queue.poll()); // 20
System.out.println(queue.poll()); // 10
```

---

## PriorityQueue 不是排序好的 List

這點很常考。

你不能以為：

```java
System.out.println(priorityQueue);
```

一定會印出排序後結果。

`PriorityQueue` 保證的是：

```text
每次 poll() 會拿到目前優先順序最高的元素
```

但它不保證整個內部結構印出來就是排序好的。

所以：

```text
poll 順序有保證
iterator / println 順序不要依賴
```

---

# 14. `List`、`Queue`、`Deque`、`Stack`、`PriorityQueue` 怎麼選？

先用需求判斷。

---

## 你需要用 index 取資料

例如：

```java
list.get(3);
```

用：

```java
List
ArrayList
```

範例：

```java
List<String> names = new ArrayList<>();
```

---

## 你需要一般排隊，先進先出

用：

```java
Queue
ArrayDeque
```

範例：

```java
Queue<String> queue = new ArrayDeque<>();
```

---

## 你需要頭尾都可以操作

用：

```java
Deque
ArrayDeque
```

範例：

```java
Deque<String> deque = new ArrayDeque<>();
```

---

## 你需要後進先出 Stack

用：

```java
Deque
ArrayDeque
```

範例：

```java
Deque<String> stack = new ArrayDeque<>();
```

---

## 你需要優先順序

用：

```java
PriorityQueue
```

範例：

```java
Queue<Integer> pq = new PriorityQueue<>();
```

---

# 15. 最重要的關係整理

## `ArrayList`

```text
是 List
不是 Queue
不是 Deque
適合 index 存取
```

---

## `LinkedList`

```text
是 List
也是 Queue
也是 Deque
但通常不是首選
```

---

## `ArrayDeque`

```text
不是 List
是 Queue
是 Deque
很適合當 Queue / Stack
```

---

## `PriorityQueue`

```text
是 Queue
不是 Deque
不是 List
依優先順序 poll
```

---

## `Stack`

```text
Stack 是 LIFO 概念
java.util.Stack 是舊類別
現代 Java 通常用 Deque + ArrayDeque
```

---

# 16. 最容易混淆的 6 個點

## ① `Queue` 不一定是 FIFO

一般 `Queue` 概念常講 FIFO。

但 `PriorityQueue` 也是 `Queue`，它不是 FIFO。

所以更精準：

```text
Queue 表示「取出元素有某種佇列規則」
一般 LinkedList / ArrayDeque 是 FIFO
PriorityQueue 是 priority order
```

---

## ② `Deque` 可以當 Queue，也可以當 Stack

因為它兩端都能操作。

```text
Queue：尾進頭出
Stack：頭進頭出
Deque：頭尾都可以
```

---

## ③ `LinkedList` 是多功能，但不代表最好用

它可以當：

```text
List
Queue
Deque
```

但大多數實務場景：

```text
List → ArrayList
Queue / Stack → ArrayDeque
```

---

## ④ `PriorityQueue` 的印出順序不要信

只信：

```java
poll()
```

不要信：

```java
System.out.println(priorityQueue)
```

---

## ⑤ `for-each` 不是魔法

它背後是：

```text
Iterable → Iterator
```

所以修改集合時會牽涉 Iterator 狀態。

---

## ⑥ `Iterator.remove()` 和 `list.remove()` 不一樣

遍歷中：

```java
it.remove();
```

通常可以。

但：

```java
list.remove(value);
```

可能讓 Iterator 失效。

---

# 17. 用一句話把這堆東西串起來

你可以先記這版：

```text
Iterable 讓物件可以被 for-each。
Iterator 是實際走訪集合的游標。
Collection 是大多數集合的共同父介面。
List 用於有順序、可重複、可 index 存取的資料。
Queue 用於排隊取出資料。
Deque 是雙端 Queue，可以當 Queue，也可以當 Stack。
ArrayDeque 是現代 Java 常用的 Queue / Stack 實作。
PriorityQueue 是依優先順序取出，不是 FIFO。
LinkedList 同時是 List 和 Deque，但多數場景不是首選。
java.util.Stack 是舊類別，新程式通常用 Deque + ArrayDeque。
```

---

# 18. 你現在應該先掌握的最小版本

先不用背全部 API。

先記這幾個就好：

```java
// 一般 List
List<String> list = new ArrayList<>();

// 一般 Queue
Queue<String> queue = new ArrayDeque<>();
queue.offer("A");
queue.poll();
queue.peek();

// Stack
Deque<String> stack = new ArrayDeque<>();
stack.push("A");
stack.pop();
stack.peek();

// PriorityQueue
Queue<Integer> pq = new PriorityQueue<>();
pq.offer(10);
pq.poll();

// Iterator remove
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    String value = it.next();
    if (value.equals("A")) {
        it.remove();
    }
}
```

Q1
完整問題

關於 Collection 與 Map，下列何者正確？

完整選項

A. Map 繼承自 Collection
B. Collection 儲存單一元素，Map 儲存 key-value pair
C. List、Set、Queue、Map 都是 Collection 的子介面
D. Map 可以直接用 enhanced for 走訪，不需要 entrySet()

我的回答

B

正確答案

B

完整詳解

Collection 和 Map 是 Java Collections Framework 中兩條不同主線。

Collection 表示一批單一元素，例如：

List<String> names = new ArrayList<>();
Set<String> tags = new HashSet<>();
Queue<String> jobs = new ArrayDeque<>();

Map 表示 key-value pair，例如：

Map<String, Integer> scores = new HashMap<>();
scores.put("Amy", 90);

Map 不是 Collection 的子介面，所以不能直接：

for (String s : map) { }

要走訪 Map 通常用：

for (Map.Entry<String, Integer> entry : scores.entrySet()) {
System.out.println(entry.getKey());
System.out.println(entry.getValue());
}
本題觀念
Collection：單一元素集合
Map：key-value 對映
Map 不是 Collection
面試高頻

高頻。常問：

Collection 和 Map 差在哪？
Map 可以 for-each 嗎？
HashMap 怎麼遍歷？
Q2
完整問題

下列哪個寫法最符合一般 Java 實務？

完整選項

A.

ArrayList<String> list = new List<>();

B.

List<String> list = new ArrayList<>();

C.

Collection<String> list = new Map<>();

D.

Map<String> map = new HashMap<>();
我的回答

B

正確答案

B

完整詳解

一般實務會寫：

List<String> list = new ArrayList<>();

左邊用介面 List，右邊用實作類別 ArrayList。

這代表：

我需要的是 List 的行為規格
目前實際用 ArrayList 實作

未來如果要換成：

List<String> list = new LinkedList<>();

其他依賴 List 介面的程式碼通常不用改。

A 錯，因為 List 是 interface，不能 new List<>()。
C 錯，Map 不是 Collection。
D 錯，Map 需要兩個泛型參數：Map<K, V>。

本題觀念
左邊用介面
右邊用實作類別
Program to an interface
面試高頻

高頻。常問：

為什麼寫 List list = new ArrayList？
List 和 ArrayList 差在哪？
Q3
完整問題

下列哪個集合允許重複元素，並且保留插入順序？

完整選項

A. HashSet
B. TreeSet
C. ArrayList
D. HashMap

我的回答

D

正確答案

C

完整詳解

ArrayList 的特性是：

有順序
可重複
可用 index 存取
保留插入順序

例如：

List<String> list = new ArrayList<>();
list.add("A");
list.add("B");
list.add("A");

System.out.println(list);

輸出：

[A, B, A]

HashSet 和 TreeSet 都不允許重複元素。
HashMap 是 key-value 結構，不是「單一元素集合」。而且 HashMap 的 key 不可重複，也不保證插入順序。

本題觀念
類型 可重複 插入順序
ArrayList 可以 保留
HashSet 不可以 不保證
TreeSet 不可以 依排序
HashMap key 不重複 不保證
面試高頻

中高頻。常問：

List 和 Set 差在哪？
哪個集合可以重複？
哪個集合保留順序？
Q4
完整問題

關於 ArrayList 與 LinkedList，下列何者最精準？

完整選項

A. LinkedList 在所有情況都比 ArrayList 快
B. ArrayList 適合隨機存取，LinkedList 適合已定位後的插入與刪除
C. ArrayList 不能刪除元素
D. LinkedList 不屬於 List

我的回答

B

正確答案

B

完整詳解

ArrayList 底層接近可變長度陣列，適合：

list.get(index);

因為能直接根據 index 算出位置。

LinkedList 底層是節點串接，若已經定位到某個節點，插入與刪除只要調整前後節點連結。

但要注意：
LinkedList 用 index 找中間元素並不快，因為它要從頭或尾一路走過去。

所以精準說法是：

ArrayList 隨機存取快
LinkedList 已定位後插入刪除成本低

不是：

LinkedList 插入刪除永遠比較快
本題觀念
操作 ArrayList LinkedList
get(index) 快 慢
尾端新增 快 尚可
中間插入 可能需要搬元素 定位慢，但節點調整快
面試高頻

非常高頻。

Q5
完整問題

下列哪個方法可以用來判斷 List 是否包含某個元素？

完整選項

A. has()
B. contains()
C. include()
D. exists()

我的回答

B

正確答案

B

完整詳解

List、Set 等 Collection 類型可以用：

list.contains(value);

例如：

List<String> names = new ArrayList<>();
names.add("Amy");

System.out.println(names.contains("Amy")); // true

contains() 內部會根據元素的 equals() 判斷是否相等。

本題觀念
contains() 用來判斷集合是否包含元素
底層通常依賴 equals()
面試高頻

中頻。

Q6
完整問題

HashSet 判斷兩個元素是否重複時，主要依賴什麼？

完整選項

A. toString()
B. clone()
C. equals() 與 hashCode()
D. compareTo() 一定優先於 equals()

我的回答

D

正確答案

C

完整詳解

HashSet 判斷元素是否重複，主要依賴：

hashCode()
equals()

簡化流程：

先用 hashCode 找桶位置
再用 equals 判斷是否真的相等

例如 String 已經正確實作 equals() 和 hashCode()：

Set<String> set = new HashSet<>();
set.add("Java");
set.add(new String("Java"));

System.out.println(set.size()); // 1

因為兩個字串內容相同，equals() 是 true，hashCode() 也一致。

compareTo() 是排序相關，主要出現在 TreeSet、TreeMap、Comparable、Comparator 場景，不是 HashSet 的主要判斷方式。

本題觀念
HashSet：equals + hashCode
TreeSet：compareTo / Comparator
面試高頻

非常高頻。這題要補強。

Q7
完整問題

TreeSet 的主要特性是什麼？

完整選項

A. 依插入順序保存元素
B. 不允許重複，並依排序規則保存元素
C. 允許重複，並依排序規則保存元素
D. 只能存 String

我的回答

B

正確答案

B

完整詳解

TreeSet 是一種有排序規則的 Set。

它具備兩個重點：

不允許重複
依排序規則保存元素

排序規則可以來自：

元素本身的 Comparable
或建立 TreeSet 時傳入 Comparator

例如：

Set<Integer> set = new TreeSet<>();
set.add(30);
set.add(10);
set.add(20);

System.out.println(set);

輸出：

[10, 20, 30]
本題觀念
TreeSet = Set + sorted order
面試高頻

高頻。

Q8
完整問題

HashMap.put(key, value) 如果 key 已經存在，會發生什麼事？

完整選項

A. 新增一筆 key 相同的資料
B. 丟出 DuplicateKeyException
C. 覆蓋原本 key 對應的 value
D. 編譯失敗

我的回答

B

正確答案

C

完整詳解

Map 的 key 不可重複。

如果對同一個 key 呼叫 put()：

Map<String, Integer> scores = new HashMap<>();

scores.put("Amy", 80);
scores.put("Amy", 100);

第二次不會新增一筆，也不會丟例外，而是覆蓋原本 value。

結果是：

Amy -> 100

補充：put() 會回傳舊值。

Integer old = scores.put("Amy", 100);

如果原本是 80，old 就是 80。

本題觀念
Map key 不可重複
put same key = replace value
面試高頻

非常高頻。

Q9
完整問題

關於 Map.get(key)，下列何者正確？

完整選項

A. 回傳 null 一定代表 key 不存在
B. 回傳 null 可能代表 key 不存在，也可能代表 value 本來就是 null
C. HashMap 不允許 value 是 null
D. HashMap 不允許 key 是 null

我的回答

B

正確答案

B

完整詳解

在 HashMap 中：

Map<String, String> map = new HashMap<>();
map.put("Amy", null);

System.out.println(map.get("Amy")); // null
System.out.println(map.get("Bob")); // null

兩個都是 null，但意義不同：

Amy：key 存在，但 value 是 null
Bob：key 不存在

所以要判斷 key 是否存在，要用：

map.containsKey("Amy");
本題觀念
get() 是取值
containsKey() 是判斷 key 是否存在
面試高頻

中高頻。

Q10
完整問題

Arrays.asList() 回傳的 List，下列何者正確？

完整選項

A. 可以 add()、remove()、set()
B. 可以 set()，但不能改變大小，例如 add() 或 remove()
C. 完全不可修改任何元素
D. 一定回傳 ArrayList

我的回答

B

正確答案

B

完整詳解

Arrays.asList() 回傳的是固定大小的 List，底層由陣列支撐。

可以：

List<String> names = Arrays.asList("Amy", "Bob");
names.set(0, "Cindy");

不能：

names.add("David");
names.remove("Amy");

因為 add() / remove() 會改變大小，底層陣列不能改長度。

如果需要可增刪的 List，要寫：

List<String> names = new ArrayList<>(Arrays.asList("Amy", "Bob"));
本題觀念
Arrays.asList()：可 set，不可 add/remove
面試高頻

中高頻。

Q11
完整問題

關於 Iterator，下列何者正確？

完整選項

A. Iterator 是集合本身
B. Iterator 是用來走訪集合元素的游標
C. Iterator 只能用在 Map
D. Iterator 沒有 remove() 方法

我的回答

B

正確答案

B

完整詳解

Iterator 不是集合本身，而是走訪集合的游標。

典型用法：

Iterator<String> it = names.iterator();

while (it.hasNext()) {
String name = it.next();
System.out.println(name);
}

它的核心方法：

hasNext()
next()
remove()
本題觀念
Iterator = cursor / traversal object
面試高頻

高頻，尤其會搭配 ConcurrentModificationException 一起考。

Q12
完整問題

enhanced for 背後主要依賴哪個介面？

完整選項

A. Serializable
B. Comparable
C. Iterable
D. Cloneable

我的回答

C

正確答案

C

完整詳解

enhanced for，也就是：

for (String name : names) {
System.out.println(name);
}

背後依賴 Iterable。

Iterable 的核心方法是：

Iterator<T> iterator();

所以任何實作 Iterable 的物件，都可以被 enhanced for 走訪。

本題觀念
Iterable 產生 Iterator
for-each 使用 Iterator 走訪
面試高頻

高頻。

Q13
完整問題

關於 Queue 的 poll() 與 remove()，下列何者正確？

完整選項

A. 空佇列時，poll() 回傳 null，remove() 丟例外
B. 空佇列時，poll() 丟例外，remove() 回傳 null
C. 兩者完全一樣
D. Queue 沒有 poll()

我的回答

A

正確答案

A

完整詳解

Queue 有兩組 API：

比較安全、失敗回傳特殊值：

offer()
peek()
poll()

失敗丟例外：

add()
element()
remove()

空佇列時：

queue.poll(); // null
queue.remove(); // NoSuchElementException
本題觀念
poll vs remove
peek vs element
offer vs add
面試高頻

中高頻。

Q14
完整問題

關於 PriorityQueue，下列何者正確？

完整選項

A. 一定依照插入順序取出元素
B. 一定是後進先出
C. 依優先順序取出元素，預設依自然排序
D. 它是 List 的一種

我的回答

C

正確答案

C

完整詳解

PriorityQueue 是 Queue 的一種，但不是 FIFO。

它依照優先順序取出元素。
對 Integer 來說，預設自然排序是小到大：

Queue<Integer> pq = new PriorityQueue<>();
pq.offer(30);
pq.offer(10);
pq.offer(20);

System.out.println(pq.poll()); // 10
System.out.println(pq.poll()); // 20
System.out.println(pq.poll()); // 30
本題觀念
PriorityQueue：poll() 依優先順序
不是 FIFO
不是 Stack
不是 List
面試高頻

中高頻。

Q15
完整問題

現代 Java 若要實作 stack 行為，通常推薦哪個寫法？

完整選項

A.

Stack<String> stack = new Stack<>();

B.

Deque<String> stack = new ArrayDeque<>();

C.

Map<String, String> stack = new HashMap<>();

D.

Set<String> stack = new HashSet<>();
我的回答

B

正確答案

B

完整詳解

Stack 是後進先出：

LIFO
Last In, First Out

現代 Java 通常推薦：

Deque<String> stack = new ArrayDeque<>();

使用：

stack.push("A");
stack.pop();
stack.peek();

java.util.Stack 是舊類別，繼承自 Vector，新程式通常不優先使用。

本題觀念
Stack 是資料結構概念
java.util.Stack 是 legacy class
現代 Java：Deque + ArrayDeque
面試高頻

中高頻。

Q16
完整問題

Map 不是 Collection 的子介面。

完整選項

T / F

我的回答

T

正確答案

T

完整詳解

正確。Map 與 Collection 是兩條不同主線。

本題觀念
Collection：單一元素
Map：key-value
面試高頻

高頻。

Q17
完整問題

List 的元素一定不能重複。

完整選項

T / F

我的回答

F

正確答案

F

完整詳解

List 可以重複。

例如：

List<String> list = new ArrayList<>();
list.add("A");
list.add("A");

合法。

本題觀念
List 可重複
Set 不重複
面試高頻

高頻。

Q18
完整問題

HashSet 不保證元素的輸出順序。

完整選項

T / F

我的回答

T

正確答案

T

完整詳解

HashSet 不保證插入順序，也不保證排序。

本題觀念
HashSet unordered
LinkedHashSet insertion order
TreeSet sorted order
面試高頻

高頻。

Q19
完整問題

LinkedHashSet 會保留元素的插入順序。

完整選項

T / F

我的回答

T

正確答案

T

完整詳解

LinkedHashSet 是：

Set + insertion order

它仍然不允許重複，但會保留加入順序。

本題觀念
LinkedHashSet 保留插入順序
面試高頻

中高頻。

Q20
完整問題

TreeMap 會依 key 的排序規則保存資料。

完整選項

T / F

我的回答

T

正確答案

T

完整詳解

TreeMap 依 key 排序，不是依 value 排序。

例如：

Map<String, Integer> map = new TreeMap<>();
map.put("C", 3);
map.put("A", 1);
map.put("B", 2);

System.out.println(map);

輸出 key 會依排序：

{A=1, B=2, C=3}
本題觀念
TreeMap sorted by key
面試高頻

中高頻。

Q21
完整問題

ArrayList 隨機存取通常比 LinkedList 更適合。

完整選項

T / F

我的回答

T

正確答案

T

完整詳解

ArrayList 可以透過 index 直接定位元素。
LinkedList 需要從頭或尾走到指定位置。

本題觀念
ArrayList random access
LinkedList sequential traversal
面試高頻

非常高頻。

Q22
完整問題

for-each 走訪集合時，直接呼叫集合本身的 remove() 永遠安全。

完整選項

T / F

我的回答

F

正確答案

F

完整詳解

不安全。

for-each 背後使用 Iterator。
如果遍歷中直接呼叫集合本身的 remove()，可能導致 ConcurrentModificationException。

正確方式：

Iterator<String> it = list.iterator();

while (it.hasNext()) {
String value = it.next();
if (value.equals("A")) {
it.remove();
}
}

或：

list.removeIf(value -> value.equals("A"));
本題觀念
遍歷中修改集合，要理解 Iterator 狀態
面試高頻

非常高頻。

Q23
完整問題

Iterator.remove() 刪除的是最近一次由 next() 回傳的元素。

完整選項

T / F

我的回答

T

正確答案

T

完整詳解

正確。

Iterator<String> it = list.iterator();

String value = it.next();
it.remove();

remove() 刪掉的是剛剛 next() 回傳的元素。

本題觀念
Iterator.remove() 必須在 next() 後使用
面試高頻

高頻。

Q24
完整問題

PriorityQueue 的 System.out.println(queue) 輸出順序一定等於 poll() 順序。

完整選項

T / F

我的回答

F

正確答案

F

完整詳解

PriorityQueue 保證的是：

每次 poll() 會取出目前優先順序最高的元素

但它不保證內部 iterator 或 println() 印出來的順序是排序好的。

所以不要依賴：

System.out.println(priorityQueue);

要看優先順序，應該用：

while (!pq.isEmpty()) {
System.out.println(pq.poll());
}
本題觀念
PriorityQueue 只保證 poll 順序
不保證印出順序
面試高頻

中高頻。

Q25
完整問題

java.util.Stack 是舊類別，新程式通常可優先考慮 Deque 搭配 ArrayDeque。

完整選項

T / F

我的回答

T

正確答案

T

完整詳解

正確。

現代 Java 常用：

Deque<String> stack = new ArrayDeque<>();

而不是：

Stack<String> stack = new Stack<>();
本題觀念
Stack 行為用 Deque 實作
java.util.Stack 屬於 legacy class
面試高頻

中頻。

Q26
完整問題

請判斷輸出：

import java.util.\*;

public class Main {
public static void main(String[] args) {
List<String> list = new ArrayList<>();
list.add("A");
list.add("B");
list.add("A");

        System.out.println(list.size());
        System.out.println(list);
    }

}
完整選項

無選項，程式輸出題。

我的回答
2
A
B
正確答案
3
[A, B, A]
完整詳解

ArrayList 是 List，允許重複元素。

程式加入：

list.add("A");
list.add("B");
list.add("A");

三筆都會被保留。

所以：

System.out.println(list.size());

輸出：

3

而：

System.out.println(list);

會用 List 的 toString() 印成：

[A, B, A]

你這題把 List 誤判成 Set 了。
如果是 Set，重複的 "A" 才會被去掉。

本題觀念
List 允許重複
Set 不允許重複
System.out.println(list) 會印出 [元素1, 元素2, ...]
面試高頻

高頻基礎題。

Q27
完整問題

請判斷輸出：

import java.util.\*;

public class Main {
public static void main(String[] args) {
Set<String> set = new HashSet<>();
set.add("Java");
set.add("Java");
set.add(new String("Java"));

        System.out.println(set.size());
    }

}
完整選項

無選項，程式輸出題。

我的回答
1
正確答案
1
完整詳解

HashSet 不允許重複元素。

"Java" 和 new String("Java") 雖然不一定是同一個物件，但字串內容相同，equals() 結果為 true，hashCode() 也相同。

因此 HashSet 只保留一個。

本題觀念
HashSet 判斷重複：equals + hashCode
String 已正確實作 equals/hashCode
面試高頻

非常高頻。

Q28
完整問題

請判斷輸出：

import java.util.\*;

public class Main {
public static void main(String[] args) {
Map<String, Integer> scores = new HashMap<>();

        scores.put("Amy", 80);
        scores.put("Bob", 90);
        scores.put("Amy", 100);

        System.out.println(scores.size());
        System.out.println(scores.get("Amy"));
    }

}
完整選項

無選項，程式輸出題。

我的回答
2
100
正確答案
2
100
完整詳解

Map 的 key 不可重複。

第一次：

Amy -> 80
Bob -> 90

第二次放入 Amy：

scores.put("Amy", 100);

會覆蓋原本的 Amy -> 80。

最後：

Amy -> 100
Bob -> 90

所以 size 是 2，Amy 對應 100。

本題觀念
Map key 不重複
put same key 會覆蓋 value
面試高頻

非常高頻。

Q29
完整問題

請判斷會不會編譯成功；若不會，說明原因：

import java.util.\*;

class Animal {}
class Dog extends Animal {}
class Cat extends Animal {}

public class Main {
public static void main(String[] args) {
List<Dog> dogs = new ArrayList<>();
List<Animal> animals = dogs;

        animals.add(new Cat());
    }

}
完整選項

無選項，編譯判斷題。

我的回答
不會編譯成功
因為 List 無法判斷 dogs 內的到底是 Dog Cat Animal

向上轉型可以 但是只能讀
正確答案

不會編譯成功。

完整詳解

你的結論正確，但說法要修正得更精準。

這題的核心是：

Dog 是 Animal
但 List<Dog> 不是 List<Animal>

Java 泛型預設是不變的，稱為：

invariance
泛型不變性

如果允許：

List<Dog> dogs = new ArrayList<>();
List<Animal> animals = dogs;

那接著就可以：

animals.add(new Cat());

但 animals 實際指向的是 List<Dog>。
這樣就會把 Cat 放進 Dog 的 list，型別被污染。

所以 Java 在編譯期直接禁止。

你提到「只能讀」接近 ? extends Animal 的概念：

List<? extends Animal> animals = dogs;

這樣可以讀：

Animal a = animals.get(0);

但不能安全地 add Dog 或 Cat。

本題觀念
Animal a = new Dog(); 可以
List<Animal> animals = new ArrayList<Dog>(); 不可以
List<? extends Animal> 可讀為 Animal，但不能安全加入具體子類
面試高頻

高頻偏進階。

Q30
完整問題

請判斷執行結果：

import java.util.\*;

public class Main {
public static void main(String[] args) {
List<String> names = Arrays.asList("Amy", "Bob");
names.set(0, "Cindy");
System.out.println(names);
}
}
完整選項

無選項，程式輸出題。

我的回答
Cindy
Bob
正確答案
[Cindy, Bob]
完整詳解

你的內容判斷對，但輸出格式不精準。

Arrays.asList() 回傳的 List 可以 set()，所以：

names.set(0, "Cindy");

會把 "Amy" 改成 "Cindy"。

但：

System.out.println(names);

會呼叫 List 的 toString()，所以輸出是一行：

[Cindy, Bob]

不是分兩行。

本題觀念
Arrays.asList() 可 set
println(list) 會印 [a, b, c]
面試高頻

中高頻。

Q31
完整問題

請判斷執行結果：

import java.util.\*;

public class Main {
public static void main(String[] args) {
List<String> names = Arrays.asList("Amy", "Bob");
names.add("Cindy");
System.out.println(names);
}
}

會正常輸出、編譯失敗，還是執行期錯誤？請說明。

完整選項

無選項，執行判斷題。

我的回答
編譯失敗 Array 無法改變長度
正確答案

執行期錯誤，會丟出：

UnsupportedOperationException
完整詳解

這題你觀念方向對：「不能改變長度」。
但錯在錯誤發生時機。

這段會編譯成功：

List<String> names = Arrays.asList("Amy", "Bob");
names.add("Cindy");

因為變數型別是 List<String>，而 List 介面本來就有 add() 方法。
所以編譯器看不出這個 List 是固定大小的。

但執行時，Arrays.asList() 回傳的是固定大小 List，不支援改變大小的操作。
因此會在執行時丟：

UnsupportedOperationException
本題觀念
編譯期：看型別是否有 add()
執行期：實際物件是否支援 add()
面試高頻

中高頻。這題要補強。

Q32
完整問題

請判斷輸出：

import java.util.\*;

public class Main {
public static void main(String[] args) {
Queue<Integer> queue = new PriorityQueue<>();

        queue.offer(30);
        queue.offer(10);
        queue.offer(20);

        while (!queue.isEmpty()) {
            System.out.print(queue.poll() + " ");
        }
    }

}
完整選項

無選項，程式輸出題。

我的回答
queue.poll() + 10
queue.poll() + 20
queue.poll() + 30
正確答案
10 20 30
完整詳解

你的取出順序是對的，但輸出格式要修正。

PriorityQueue<Integer> 預設使用自然排序。
對 Integer 而言，就是小的優先。

加入順序：

30, 10, 20

取出順序：

10, 20, 30

System.out.print(queue.poll() + " "); 每次印出數字加空白，所以是：

10 20 30
本題觀念
PriorityQueue 不是 FIFO
PriorityQueue poll() 依優先順序
Integer 預設小到大
面試高頻

中高頻。

Q33
完整問題

請判斷輸出：

import java.util.\*;

public class Main {
public static void main(String[] args) {
Deque<String> stack = new ArrayDeque<>();

        stack.push("A");
        stack.push("B");
        stack.push("C");

        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.peek());
    }

}
完整選項

無選項，程式輸出題。

我的回答
C
B
A
正確答案
C
B
A
完整詳解

Deque 搭配 ArrayDeque 可以實作 Stack 行為。

Stack 是 LIFO：

Last In, First Out
後進先出

push 順序：

A -> B -> C

pop 順序：

C -> B

此時剩下 A，所以：

stack.peek();

回傳 A，但不移除。

本題觀念
Deque + ArrayDeque 可作為現代 Stack
push / pop / peek
面試高頻

中頻。

Q34
完整問題

請判斷輸出：

import java.util.\*;

class User implements Comparable<User> {
String name;
int age;

    User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public int compareTo(User other) {
        return this.age - other.age;
    }

    @Override
    public String toString() {
        return name + ":" + age;
    }

}

public class Main {
public static void main(String[] args) {
Set<User> users = new TreeSet<>();

        users.add(new User("Amy", 20));
        users.add(new User("Bob", 20));
        users.add(new User("Cindy", 30));

        System.out.println(users);
    }

}

請寫出輸出，並說明為什麼。

完整選項

無選項，程式輸出題。

我的回答
Bob:20
Cindy:30

TreeSet 照規矩 compareTo 排序。
Amy 被 Bob 覆蓋。
正確答案
[Amy:20, Cindy:30]
完整詳解

這題你抓到 TreeSet 依照 compareTo()，但錯在「Amy 被 Bob 覆蓋」。

TreeSet 不是 Map，沒有 key-value 覆蓋行為。

流程如下：

第一次加入：

users.add(new User("Amy", 20));

成功加入。

第二次加入：

users.add(new User("Bob", 20));

比較：

return this.age - other.age;

Bob.age - Amy.age：

20 - 20 = 0

對 TreeSet 來說，compareTo() 回傳 0 表示：

排序上視為同一個元素

所以 Bob 不會加入。
不是 Bob 覆蓋 Amy，而是 Bob 被拒絕加入。

第三次加入 Cindy：

30 - 20 != 0

所以成功加入。

最後輸出：

[Amy:20, Cindy:30]
本題觀念
TreeSet 判斷重複依 compareTo / Comparator 是否回傳 0
TreeSet add 重複元素時，不會覆蓋舊元素
面試高頻

中高頻。這題要補強。

Q35
完整問題

請判斷執行結果：

import java.util.\*;

public class Main {
public static void main(String[] args) {
List<String> names = new ArrayList<>();

        names.add("Amy");
        names.add("Bob");
        names.add("Cindy");

        for (String name : names) {
            if (name.equals("Bob")) {
                names.remove(name);
            }
        }

        System.out.println(names);
    }

}

會正常輸出、編譯失敗，還是可能執行期錯誤？請說明。

完整選項

無選項，執行判斷題。

我的回答
執行期錯誤，因為 ArrayList 發現他的長度被改變了，會報錯。

要改的話：

1. 使用 interable 方法
2. 使用 removeIf
3. 建一個新的 ArrayList 篩選
   正確答案

考題預期答案：可能執行期錯誤，通常是 ConcurrentModificationException。

但要補一個精準細節：這段特定程式在某些 JDK / 某些情況下可能剛好不丟例外，但寫法仍然是不安全的，不應依賴。

完整詳解

for-each 背後使用 Iterator。

這段：

for (String name : names) {
...
}

大致等價於：

Iterator<String> it = names.iterator();

while (it.hasNext()) {
String name = it.next();
...
}

問題在於你在遍歷中呼叫：

names.remove(name);

這是直接修改集合本體，不是透過 Iterator 修改。
Iterator 會用類似 modCount 的機制偵測集合是否被外部結構性修改。

因此可能丟：

ConcurrentModificationException

你的修正方向對，但第 1 點應該說：

使用 Iterator.remove()

不是 Iterable 方法。

正確寫法：

Iterator<String> it = names.iterator();

while (it.hasNext()) {
String name = it.next();
if (name.equals("Bob")) {
it.remove();
}
}

或：

names.removeIf(name -> name.equals("Bob"));

或建立新 List。

本題觀念
for-each 背後是 Iterator
遍歷中不要直接 list.remove()
要用 Iterator.remove() / removeIf() / 新集合
面試高頻

非常高頻。

Q36
完整問題

請用自己的話說明：

Iterable、Iterator、for-each 三者的關係。
完整選項

無選項，簡答題。

我的回答
Iterable 是一種 interface
Iterator 是一種 Iterable 在迭代時的指標
for-each 是一種 Iterable 迭代的語法糖
正確答案參考
Iterable 表示一個物件可以產生 Iterator。
Iterator 是實際走訪元素的游標物件。
for-each 是 Java 提供的語法糖，背後會呼叫 Iterable.iterator() 取得 Iterator 來遍歷。
完整詳解

你的答案方向正確，但要修正一句：

Iterator 不是一種 Iterable
Iterator 是 Iterable 產生出來的走訪器

精準關係是：

Iterable
提供 iterator()

Iterator
提供 hasNext() / next() / remove()

for-each
背後使用 Iterable.iterator() 取得 Iterator

例如：

for (String name : names) {
System.out.println(name);
}

背後大致是：

Iterator<String> it = names.iterator();

while (it.hasNext()) {
String name = it.next();
System.out.println(name);
}
本題觀念
Iterable = 可被迭代
Iterator = 實際迭代的游標
for-each = 語法糖
面試高頻

高頻。

Q37
完整問題

請說明：

ArrayList 和 LinkedList 的差異。

不要只寫「ArrayList 查詢快、LinkedList 插入快」，請補上原因。

完整選項

無選項，簡答題。

我的回答
ArrayList 適合大部分的現代 Java 開發情景，主要是查詢快，排序慢。
LinkedList 則在一些需要大量在中間插入的情景適合，所以理論上插入快，但是當元素很多的時候，查詢很久，插入速度不一定比較快。
正確答案參考
ArrayList 底層接近可變長度陣列，適合隨機存取，get(index) 快。
LinkedList 底層是節點串接，已定位到節點後插入刪除成本低，但用 index 定位元素較慢。
實務上多數一般 List 場景優先使用 ArrayList。
完整詳解

你的大方向對，但「排序慢」這句不精準。

ArrayList 不是主要因為「排序慢」，而是：

中間插入 / 刪除可能慢，因為需要搬移後面的元素

例如：

[A, B, C, D]
在 index 1 插入 X
[A, X, B, C, D]

B, C, D 都要往後搬。

LinkedList 的優勢是：
如果已經定位到某個節點，插入 / 刪除只要調整節點連結。

但問題是：
如果你用 index 找中間位置，LinkedList 需要一路走過去，定位成本高。

所以最精準說法是：

ArrayList：random access 快
LinkedList：已定位後插入 / 刪除成本低，但定位慢
本題觀念
ArrayList：array-like
LinkedList：node-based
不要背成 LinkedList 插入一定快
面試高頻

非常高頻。

Q38
完整問題

請說明：

HashSet、LinkedHashSet、TreeSet 的差異。
完整選項

無選項，簡答題。

我的回答
HashSet 隨機排序
LinkedHashSet 後端插入快
TreeSet 依照規則排序
正確答案參考
HashSet：不允許重複，不保證順序，查找通常快。
LinkedHashSet：不允許重複，保留插入順序。
TreeSet：不允許重複，依排序規則保存元素。
完整詳解

這題你只答對一部分。

HashSet 不是「隨機排序」。
更精準是：

不保證順序

它不是故意亂排，也不是每次隨機，而是順序由 hash 結構、JDK 實作、容量等因素決定，不應依賴。

LinkedHashSet 不是重點在「後端插入快」。
它的重點是：

保留插入順序

例如：

Set<String> set = new LinkedHashSet<>();
set.add("B");
set.add("A");
set.add("C");

System.out.println(set);

輸出：

[B, A, C]

TreeSet 你答對：依排序規則排列。

本題觀念
類型 重複 順序
HashSet 不允許 不保證
LinkedHashSet 不允許 插入順序
TreeSet 不允許 排序順序
面試高頻

非常高頻。這題要補強。

Q39
完整問題

請說明：

HashMap、LinkedHashMap、TreeMap 的差異。
完整選項

無選項，簡答題。

我的回答
基本上同 38
正確答案參考
HashMap：key 不重複，不保證順序，通常查找快。
LinkedHashMap：key 不重複，保留插入順序。
TreeMap：key 不重複，依 key 的排序規則保存資料。
完整詳解

你說「同 38」方向可以理解，但不夠精準。

Map 是 key-value pair，所以要講 key 的行為。

HashMap：

key 不重複
不保證順序
通常查找快

LinkedHashMap：

key 不重複
保留 key 的插入順序

TreeMap：

key 不重複
依 key 的排序規則排列

注意：
TreeMap 是依 key 排序，不是依 value 排序。

本題觀念
類型 key 是否重複 順序
HashMap 不可重複 不保證
LinkedHashMap 不可重複 插入順序
TreeMap 不可重複 key 排序
面試高頻

高頻。

Q40
完整問題

請說明：

Queue、Deque、PriorityQueue、Stack 的關係。
完整選項

無選項，簡答題。

我的回答
Queue 是一種基本上 FIFO 的 interface（PriorityQueue 例外）
Deque 是頭尾都可以 FIFO 的 Queue
PriorityQueue 是有特定規則 pop 的 Queue
Stack 一種原始的 interface，現代幾乎不用，要實踐會用 Deque/ArrayDeque
正確答案參考
Queue 是佇列介面，通常表示依某種排隊規則取出元素，常見 FIFO。
Deque 是 Queue 的子介面，代表雙端佇列，頭尾都能加入與取出。
PriorityQueue 是 Queue 的實作類別，依優先順序 poll，不是 FIFO。
Stack 是 LIFO 資料結構概念；java.util.Stack 是舊類別，現代 Java 通常用 Deque + ArrayDeque 實作 Stack 行為。
完整詳解

你這題大方向接近，但有兩個要修正。

第一，Deque 不是「頭尾都可以 FIFO」。
Deque 是雙端佇列，頭尾都可以操作。
它可以拿來實作 FIFO，也可以拿來實作 LIFO。

第二，Stack 不是 interface。
Stack 是資料結構概念。
java.util.Stack 是 Java 的舊類別。

比較正確的關係：

Queue 是介面
Deque 是 Queue 的子介面
ArrayDeque 是 Deque 的常用實作
PriorityQueue 是 Queue 的實作
Stack 是 LIFO 概念
java.util.Stack 是 legacy class
本題觀念
Queue：排隊規則
Deque：雙端操作
PriorityQueue：優先順序
Stack：LIFO
ArrayDeque：現代常用 Queue / Stack 實作
面試高頻

中高頻。

Q41
完整問題

請說明：

為什麼泛型可以提高集合的型別安全？

請至少提到「編譯期」或「執行期」其中一個角度。

完整選項

無選項，簡答題。

我的回答
因為在編譯期，編譯器能先檢查後續程式碼的 class 有沒有正確，有衝突會先報錯。

不使用的話，在執行期才會報錯。
因此使用泛型可以提高集合的型別安全，避免在執行期才會報錯。
正確答案參考
泛型讓集合在編譯期就能檢查元素型別。例如 List<String> 只能加入 String。如果嘗試加入 Integer，編譯器會直接報錯。沒有泛型時，錯誤可能延後到取出並轉型時才在執行期丟出 ClassCastException。
完整詳解

這題答得不錯。

你抓到重點：

泛型把型別錯誤提前到編譯期

例如：

List<String> names = new ArrayList<>();
names.add("Amy");
names.add(123); // 編譯失敗

沒有泛型時：

List names = new ArrayList();
names.add("Amy");
names.add(123);

String s = (String) names.get(1); // 執行期 ClassCastException

泛型的價值就是降低 runtime type error。

本題觀念
Generics = compile-time type safety
Raw type = runtime risk
面試高頻

高頻。

Q42
完整問題

請說明：

為什麼新程式通常不建議優先使用 Vector、Hashtable、java.util.Stack？
完整選項

無選項，簡答題。

我的回答
因為 Vector、Hashtable、java.util.Stack 這些都是很舊的東西。

有曝露過度等等的問題。
正確答案參考
Vector、Hashtable、java.util.Stack 都是 Java 早期 legacy collection。它們設計較舊，API 不夠乾淨，部分同步設計不符合現代多數使用場景。新程式通常會優先使用 ArrayList、HashMap、ConcurrentHashMap 或 Deque / ArrayDeque。
完整詳解

你的答案方向正確，但要補上替代品。

Vector 通常改用：

List<String> list = new ArrayList<>();

如果真的需要執行緒安全，要根據場景選：

Collections.synchronizedList(...)
CopyOnWriteArrayList

Hashtable 通常改用：

Map<String, String> map = new HashMap<>();

如果需要 concurrent map：

ConcurrentHashMap

java.util.Stack 通常改用：

Deque<String> stack = new ArrayDeque<>();
本題觀念
Legacy collection 要能看懂，但新程式不優先用
面試高頻

中頻。

你這次最需要補強的 6 個點

1. List 和 Set 不要混

你在 Q26 把 List 當成 Set。

記法：

List：可重複
Set：不重複 2. HashSet vs TreeSet 的重複判斷
HashSet：equals + hashCode
TreeSet：compareTo / Comparator 回傳 0 3. Arrays.asList() 是執行期錯誤，不是編譯錯誤
List<String> list = Arrays.asList("A", "B");
list.add("C"); // 編譯過，執行期 UnsupportedOperationException 4. LinkedHashSet / LinkedHashMap 是「保留插入順序」

不是「後端插入快」。

LinkedHashSet：Set + insertion order
LinkedHashMap：Map + insertion order 5. TreeSet 不會覆蓋元素

compareTo() 回傳 0 時：

新元素不加入
舊元素保留

不是覆蓋。

6. Stack 不是 interface
   Stack 是資料結構概念
   java.util.Stack 是舊類別
   Deque 是介面
   ArrayDeque 是實作類別

---

Q1
完整問題

下列哪個集合「允許重複」且「保留插入順序」？

完整選項

A. HashSet
B. TreeSet
C. ArrayList
D. HashMap

我的回答

C

正確答案

C

完整詳解

ArrayList 是 List 的常見實作，特性是：

有順序
允許重複
可用 index 存取

例如：

List<String> list = new ArrayList<>();

list.add("A");
list.add("B");
list.add("A");

System.out.println(list);

輸出：

[A, B, A]

HashSet、TreeSet 都是 Set，不允許重複。
HashMap 是 key-value 結構，不是單一元素集合。

本題觀念
List：可重複、有順序
Set：不可重複
Map：key-value
面試高頻

高頻。常問：

List 和 Set 差在哪？
哪些集合允許重複？
ArrayList 是否保留插入順序？
Q2
完整問題

下列哪個集合「不允許重複」且「不保證順序」？

完整選項

A. ArrayList
B. HashSet
C. LinkedHashSet
D. TreeSet

我的回答

B

正確答案

B

完整詳解

HashSet 的核心特性是：

不允許重複
不保證順序
查找通常快

注意：HashSet 不是「隨機排序」，而是「不保證順序」。
它印出來的順序可能看起來固定，但不能依賴。

本題觀念
類型 重複 順序
ArrayList 可重複 插入順序
HashSet 不可重複 不保證
LinkedHashSet 不可重複 插入順序
TreeSet 不可重複 排序順序
面試高頻

高頻。

Q3
完整問題

下列哪個集合「不允許重複」且「保留插入順序」？

完整選項

A. HashSet
B. LinkedHashSet
C. TreeSet
D. PriorityQueue

我的回答

B

正確答案

B

完整詳解

LinkedHashSet 是：

Set + insertion order

也就是：

不允許重複
保留插入順序

例如：

Set<String> set = new LinkedHashSet<>();

set.add("B");
set.add("A");
set.add("C");
set.add("A");

System.out.println(set);

輸出：

[B, A, C]

第二個 "A" 不會加入，因為 Set 不允許重複。

本題觀念
LinkedHashSet = HashSet 的不重複特性 + 插入順序
面試高頻

高頻。常問：

HashSet、LinkedHashSet、TreeSet 差在哪？
Q4
完整問題

下列哪個集合「不允許重複」且「依排序規則保存元素」？

完整選項

A. ArrayList
B. LinkedList
C. LinkedHashSet
D. TreeSet

我的回答

D

正確答案

D

完整詳解

TreeSet 是排序型的 Set。

它的特性是：

不允許重複
依排序規則保存元素

排序規則可以來自：

Comparable
Comparator

例如：

Set<Integer> set = new TreeSet<>();

set.add(30);
set.add(10);
set.add(20);

System.out.println(set);

輸出：

[10, 20, 30]
本題觀念
TreeSet = Set + sorted order
面試高頻

高頻。

Q5
完整問題

關於 HashMap、LinkedHashMap、TreeMap，下列何者正確？

完整選項

A. 三者都會依 key 排序
B. HashMap 保留插入順序
C. LinkedHashMap 保留插入順序
D. TreeMap 依 value 排序

我的回答

D

正確答案

C

完整詳解

LinkedHashMap 的重點是：

保留 key 第一次插入的順序

TreeMap 是依 key 排序，不是依 value 排序。

例如：

Map<String, Integer> map = new TreeMap<>();

map.put("B", 2);
map.put("A", 1);
map.put("C", 3);

System.out.println(map);

輸出：

{A=1, B=2, C=3}

這是 key 排序，不是 value 排序。

本題觀念
類型 順序
HashMap 不保證順序
LinkedHashMap 保留插入順序
TreeMap 依 key 排序
面試高頻

高頻。這題你要補強。

Q6
完整問題

關於 TreeSet 判斷重複元素，下列何者最正確？

完整選項

A. 主要看 hashCode()
B. 主要看 toString()
C. 主要看 compareTo() 或 Comparator 比較結果是否為 0
D. 只要物件記憶體位址不同，就一定可以加入

我的回答

C

正確答案

C

完整詳解

TreeSet 判斷兩個元素是否重複，主要看排序比較結果。

如果：

compareTo(other) == 0

或 Comparator.compare(a, b) == 0，TreeSet 會認為兩個元素在排序上相等，因此不加入第二個。

這跟 HashSet 不一樣：

HashSet：equals() + hashCode()
TreeSet：compareTo() / Comparator
本題觀念
TreeSet 的重複判斷來自排序規則
compare 結果為 0 = 視為重複
面試高頻

中高頻。

Q7
完整問題

Arrays.asList() 回傳的 List，哪個操作通常可以成功？

完整選項

A. add()
B. remove()
C. set()
D. clear()

我的回答

C

正確答案

C

完整詳解

Arrays.asList() 回傳的是固定大小的 List。

它可以改元素：

List<String> list = Arrays.asList("A", "B");

list.set(0, "C");

System.out.println(list);

輸出：

[C, B]

但是不能改變大小：

list.add("D"); // 執行期錯誤
list.remove("A"); // 執行期錯誤
list.clear(); // 執行期錯誤
本題觀念
Arrays.asList()：可 set，不可 add / remove / clear
面試高頻

中高頻。

Q8
完整問題

Arrays.asList("A", "B").add("C") 的錯誤發生在什麼階段？

完整選項

A. 編譯期錯誤
B. 執行期錯誤
C. 不會錯，正常加入
D. JVM 啟動前錯誤

我的回答

B

正確答案

B

完整詳解

這段會編譯成功：

List<String> list = Arrays.asList("A", "B");
list.add("C");

原因是變數型別是 List<String>，而 List 介面有 add() 方法。

但是執行時，實際物件是 Arrays.asList() 回傳的固定大小 List，不支援 add()。

所以執行期會丟：

UnsupportedOperationException
本題觀念
編譯期看宣告型別
執行期看實際物件支不支援該操作
面試高頻

中高頻。

Q9
完整問題

關於 Stack，下列何者正確？

完整選項

A. Stack 是 Java 現代集合框架中的核心介面
B. Stack 是 LIFO 資料結構概念，現代 Java 常用 Deque + ArrayDeque 實作
C. Stack 一定比 ArrayDeque 新
D. Stack 是 FIFO 結構

我的回答

B

正確答案

B

完整詳解

Stack 是一種資料結構概念：

LIFO
Last In, First Out
後進先出

現代 Java 通常不優先使用 java.util.Stack，而是用：

Deque<String> stack = new ArrayDeque<>();

操作：

stack.push("A");
stack.push("B");

System.out.println(stack.pop()); // B
本題觀念
Stack 是概念
java.util.Stack 是舊類別
Deque + ArrayDeque 是現代常用寫法
面試高頻

中頻。

Q10
完整問題

關於 PriorityQueue，下列何者正確？

完整選項

A. PriorityQueue 一定依插入順序取出
B. PriorityQueue 是 List 的子介面
C. PriorityQueue 的 poll() 依優先順序取出元素
D. PriorityQueue 不能存放 Integer

我的回答

C

正確答案

C

完整詳解

PriorityQueue 是 Queue 的實作類別。
它不是 FIFO，而是依優先順序取出。

例如：

Queue<Integer> queue = new PriorityQueue<>();

queue.offer(5);
queue.offer(1);
queue.offer(3);

System.out.println(queue.poll()); // 1
System.out.println(queue.poll()); // 3
System.out.println(queue.poll()); // 5

Integer 預設自然排序是小到大，所以小的數字先出來。

本題觀念
PriorityQueue：poll() 依 priority
不是 FIFO
不是 LIFO
面試高頻

中高頻。

Q11
完整問題

請判斷輸出：

import java.util.\*;

public class Main {
public static void main(String[] args) {
List<String> list = new ArrayList<>();

        list.add("A");
        list.add("B");
        list.add("A");

        System.out.println(list.size());
        System.out.println(list);
    }

}
完整選項

無，程式輸出題。

我的回答
33
A
B
A
正確答案
3
[A, B, A]
完整詳解

ArrayList 是 List，允許重複元素。

加入順序：

A → B → A

所以共有三個元素。

第一行：

System.out.println(list.size());

輸出：

3

第二行：

System.out.println(list);

會印出整個 List 的 toString()：

[A, B, A]

不是逐行印出每個元素。

本題觀念
List 可重複
println(list) 會印成 [A, B, A]
面試高頻

高頻基礎題。

Q12
完整問題

請判斷輸出：

import java.util.\*;

public class Main {
public static void main(String[] args) {
Set<String> set = new HashSet<>();

        set.add("A");
        set.add("B");
        set.add("A");

        System.out.println(set.size());
    }

}
完整選項

無，程式輸出題。

我的回答
2
正確答案
2
完整詳解

HashSet 不允許重複元素。

加入：

A
B
A

第二個 A 會被視為重複，不加入。

所以 size 是：

2
本題觀念
Set 不允許重複
HashSet 對 String 判斷重複會依 equals() / hashCode()
面試高頻

高頻。

Q13
完整問題

請判斷輸出：

import java.util.\*;

public class Main {
public static void main(String[] args) {
Set<String> set = new LinkedHashSet<>();

        set.add("B");
        set.add("A");
        set.add("C");
        set.add("A");

        System.out.println(set);
    }

}
完整選項

無，程式輸出題。

我的回答
B
A
C
正確答案
[B, A, C]
完整詳解

LinkedHashSet 的特性：

不允許重複
保留插入順序

加入流程：

B → 加入
A → 加入
C → 加入
A → 已存在，不加入

所以最後是：

[B, A, C]

你觀念對，但輸出格式要注意：System.out.println(set) 是印整個集合，不是逐行印元素。

本題觀念
LinkedHashSet = Set + insertion order
面試高頻

高頻。

Q14
完整問題

請判斷輸出：

import java.util.\*;

public class Main {
public static void main(String[] args) {
Set<Integer> set = new TreeSet<>();

        set.add(30);
        set.add(10);
        set.add(20);
        set.add(10);

        System.out.println(set);
    }

}
完整選項

無，程式輸出題。

我的回答
10
20
30
正確答案
[10, 20, 30]
完整詳解

TreeSet 的特性：

不允許重複
依排序規則保存元素

加入：

30
10
20
10

第二個 10 是重複，不加入。
Integer 自然排序是小到大，所以結果：

[10, 20, 30]

你順序對，但輸出格式錯。

本題觀念
TreeSet 會排序
TreeSet 不允許重複
println(set) 印成 [10, 20, 30]
面試高頻

高頻。

Q15
完整問題

請判斷輸出：

import java.util.\*;

public class Main {
public static void main(String[] args) {
Map<String, Integer> map = new LinkedHashMap<>();

        map.put("B", 2);
        map.put("A", 1);
        map.put("C", 3);
        map.put("A", 100);

        System.out.println(map.size());
        System.out.println(map);
    }

}
完整選項

無，程式輸出題。

我的回答
3
[("B", 2),("C", 3),("A", 100)]
正確答案
3
{B=2, A=100, C=3}
完整詳解

LinkedHashMap 保留的是 key 第一次插入的順序。

加入流程：

map.put("B", 2); // B 第一次出現
map.put("A", 1); // A 第一次出現
map.put("C", 3); // C 第一次出現
map.put("A", 100); // A 已存在，只更新 value

第一次插入順序是：

B → A → C

最後一次 put("A", 100) 只是更新 A 的 value，不會把 A 移到最後。

所以：

size = 3
map = {B=2, A=100, C=3}

你的錯點是把更新後的 A 移到最後。
預設 LinkedHashMap 不會這樣。

本題觀念
LinkedHashMap 保留 key 第一次插入順序
put 既有 key 只覆蓋 value，不新增 key，也不改變插入位置
面試高頻

中高頻。

Q16
完整問題

請判斷輸出：

import java.util.\*;

public class Main {
public static void main(String[] args) {
Map<String, Integer> map = new TreeMap<>();

        map.put("B", 2);
        map.put("A", 1);
        map.put("C", 3);

        System.out.println(map);
    }

}
完整選項

無，程式輸出題。

我的回答
A,1
B,2
C,3
正確答案
{A=1, B=2, C=3}
完整詳解

TreeMap 依 key 排序。

加入順序是：

B → A → C

但 key 排序後是：

A → B → C

所以印出：

{A=1, B=2, C=3}

你觀念正確，但格式要修正。Map 的 toString() 格式是：

{key=value, key=value}
本題觀念
TreeMap 依 key 排序
println(map) 印成 {A=1, B=2}
面試高頻

中高頻。

Q17
完整問題

請判斷輸出，並簡單說明原因：

import java.util.\*;

class User {
String email;

    User(String email) {
        this.email = email;
    }

}

public class Main {
public static void main(String[] args) {
Set<User> users = new HashSet<>();

        users.add(new User("a@test.com"));
        users.add(new User("a@test.com"));

        System.out.println(users.size());
    }

}
完整選項

無，程式輸出題。

我的回答
2
自訂class 所以算兩個物件
正確答案
2
完整詳解

你的答案正確，但原因可以更精準。

User 沒有 override：

equals()
hashCode()

所以預設使用 Object 的相等判斷，基本上是看是不是同一個物件。

這兩個是不同物件：

new User("a@test.com")
new User("a@test.com")

即使 email 內容一樣，HashSet 也會把它們視為不同元素。

所以 size 是：

2
本題觀念
自訂 class 若要在 HashSet 判斷邏輯相等
通常必須 override equals() 和 hashCode()
面試高頻

非常高頻。

Q18
完整問題

請判斷輸出，並簡單說明原因：

import java.util.\*;

class User {
String email;

    User(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User other)) {
            return false;
        }
        return this.email.equals(other.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

}

public class Main {
public static void main(String[] args) {
Set<User> users = new HashSet<>();

        users.add(new User("a@test.com"));
        users.add(new User("a@test.com"));

        System.out.println(users.size());
    }

}
完整選項

無，程式輸出題。

我的回答
1
你改了比較方法 equals()
所以是視為同一個
正確答案
1
完整詳解

你的答案正確。
但更精準地說，不只改了 equals()，也改了 hashCode()。

HashSet 判斷重複主要依賴：

hashCode()
equals()

這題兩個 User 的 email 一樣：

a@test.com
a@test.com

equals() 會回傳 true。
hashCode() 也會根據 email 回傳相同 hash。

因此 HashSet 視為同一個元素，第二個不加入。

最後 size 是：

1
本題觀念
HashSet 判斷重複：hashCode + equals
自訂 class 只改 equals 不夠，通常 hashCode 也要一起改
面試高頻

非常高頻。

Q19
完整問題

請判斷輸出，並說明 Bob 是否有加入：

import java.util.\*;

class User implements Comparable<User> {
String name;
int age;

    User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public int compareTo(User other) {
        return this.age - other.age;
    }

    @Override
    public String toString() {
        return name + ":" + age;
    }

}

public class Main {
public static void main(String[] args) {
Set<User> users = new TreeSet<>();

        users.add(new User("Amy", 20));
        users.add(new User("Bob", 20));
        users.add(new User("Cindy", 30));

        System.out.println(users);
    }

}
完整選項

無，程式輸出題。

我的回答
"Amy", 20
Cindy", 30

TreeSet 不做覆蓋
正確答案
[Amy:20, Cindy:30]

Bob 沒有加入。

完整詳解

你這次觀念對了：TreeSet 不做覆蓋。

流程如下：

第一次加入：

users.add(new User("Amy", 20));

成功加入。

第二次加入：

users.add(new User("Bob", 20));

比較 Bob 和 Amy：

return this.age - other.age;

結果：

20 - 20 = 0

對 TreeSet 來說，compareTo() 回傳 0 代表排序上相等。
所以 Bob 被視為重複，不加入。

第三次加入 Cindy：

30 - 20 != 0

所以 Cindy 加入。

最後：

[Amy:20, Cindy:30]
本題觀念
TreeSet 判斷重複看 compareTo / Comparator 是否回傳 0
回傳 0 時，新元素不加入，舊元素保留
不是覆蓋
面試高頻

中高頻。

Q20
完整問題

請判斷執行結果：

import java.util.\*;

public class Main {
public static void main(String[] args) {
List<String> list = Arrays.asList("A", "B");

        list.set(1, "C");

        System.out.println(list);
    }

}
完整選項

無，程式輸出題。

我的回答
A
C
正確答案
[A, C]
完整詳解

Arrays.asList() 回傳固定大小 List，但可以 set()。

原本：

[A, B]

執行：

list.set(1, "C");

index 1 的 "B" 改成 "C"。

所以最後：

[A, C]

你觀念正確，但輸出格式錯。
System.out.println(list) 印的是整個 List。

本題觀念
Arrays.asList() 可以 set()
println(list) 輸出 [A, C]
面試高頻

中高頻。

Q21
完整問題

請判斷執行結果：

import java.util.\*;

public class Main {
public static void main(String[] args) {
List<String> list = Arrays.asList("A", "B");

        list.remove("A");

        System.out.println(list);
    }

}

請回答：正常輸出、編譯失敗，還是執行期錯誤？原因是什麼？

完整選項

無，執行判斷題。

我的回答
執行期錯誤
陣列長度改變
會報錯
正確答案
執行期錯誤
UnsupportedOperationException
完整詳解

你的方向正確：remove() 會改變大小，所以不支援。
但要講得更精準。

這段會編譯成功，因為：

List<String> list

List 介面本身有 remove() 方法。

但是執行時，Arrays.asList() 回傳的是固定大小 List。
固定大小 List 不支援：

add()
remove()
clear()

所以執行時丟：

UnsupportedOperationException
本題觀念
Arrays.asList() 回傳固定大小 List
remove() 編譯會過
remove() 執行期會丟 UnsupportedOperationException
面試高頻

中高頻。

Q22
完整問題

請判斷輸出：

import java.util.\*;

public class Main {
public static void main(String[] args) {
Queue<Integer> queue = new PriorityQueue<>();

        queue.offer(5);
        queue.offer(1);
        queue.offer(3);

        System.out.print(queue.poll() + " ");
        System.out.print(queue.poll() + " ");
        System.out.print(queue.poll() + " ");
    }

}
完整選項

無，程式輸出題。

我的回答
3
1
5

LIFO
正確答案
1 3 5
完整詳解

這題你錯在把 PriorityQueue 當成 Stack。

PriorityQueue 不是 LIFO。
它是依優先順序取出。

對 Integer 來說，預設自然排序是小到大。

加入：

5
1
3

取出順序：

1
3
5

而且程式用的是 System.out.print()，不是 println()，所以輸出在同一行：

1 3 5
本題觀念
PriorityQueue 不是 FIFO
PriorityQueue 也不是 LIFO
PriorityQueue poll() 依優先順序
面試高頻

中高頻。

Q23
完整問題

請判斷輸出：

import java.util.\*;

public class Main {
public static void main(String[] args) {
Queue<String> queue = new ArrayDeque<>();

        queue.offer("A");
        queue.offer("B");
        queue.offer("C");

        System.out.println(queue.poll());
        System.out.println(queue.peek());
        System.out.println(queue.poll());
    }

}
完整選項

無，程式輸出題。

我的回答
A
B
B
正確答案
A
B
B
完整詳解

Queue 一般 FIFO：

First In, First Out
先進先出

加入：

A → B → C

第一個：

queue.poll()

取出 A。

此時 queue：

B → C

第二個：

queue.peek()

查看但不移除，所以是 B。

第三個：

queue.poll()

取出 B。

本題觀念
poll()：取出並移除
peek()：查看但不移除
面試高頻

中頻。

Q24
完整問題

請判斷輸出：

import java.util.\*;

public class Main {
public static void main(String[] args) {
Deque<String> stack = new ArrayDeque<>();

        stack.push("A");
        stack.push("B");
        stack.push("C");

        System.out.println(stack.pop());
        System.out.println(stack.peek());
        System.out.println(stack.pop());
    }

}
完整選項

無，程式輸出題。

我的回答
C
B
B
正確答案
C
B
B
完整詳解

Deque + ArrayDeque 可以用來實作 Stack。

Stack 是：

LIFO
Last In, First Out
後進先出

push 順序：

A → B → C

頂端是 C。

第一個：

stack.pop()

取出 C。

剩下：

B → A

第二個：

stack.peek()

查看頂端 B，不移除。

第三個：

stack.pop()

取出 B。

本題觀念
push()：放到 stack 頂端
pop()：取出並移除頂端
peek()：查看頂端但不移除
面試高頻

中頻。

Q25
完整問題

請判斷輸出：

import java.util.\*;

public class Main {
public static void main(String[] args) {
Deque<String> deque = new ArrayDeque<>();

        deque.addFirst("A");
        deque.addLast("B");
        deque.addFirst("C");

        System.out.println(deque.pollFirst());
        System.out.println(deque.pollLast());
        System.out.println(deque.pollFirst());
    }

}
完整選項

無，程式輸出題。

我的回答
C
B
A
正確答案
C
B
A
完整詳解

流程：

deque.addFirst("A");

變成：

[A]

接著：

deque.addLast("B");

變成：

[A, B]

再來：

deque.addFirst("C");

變成：

[C, A, B]

所以：

pollFirst()

取出 C。

pollLast()

取出 B。

pollFirst()

取出 A。

本題觀念
Deque 是雙端佇列
First 端和 Last 端都能加入 / 取出
面試高頻

中頻。

Q26
完整問題

請判斷執行結果：

import java.util.\*;

public class Main {
public static void main(String[] args) {
Queue<String> queue = new ArrayDeque<>();

        System.out.println(queue.poll());
    }

}
完整選項

無，程式輸出題。

我的回答
null?
正確答案
null
完整詳解

空 queue 呼叫：

queue.poll()

會回傳：

null

不會丟例外。

Queue 有兩組 API：

操作 回傳特殊值 丟例外
查看 peek() element()
取出 poll() remove()
加入 offer() add()
本題觀念
poll() 空佇列時回傳 null
remove() 空佇列時丟例外
面試高頻

中高頻。

Q27
完整問題

請判斷執行結果：

import java.util.\*;

public class Main {
public static void main(String[] args) {
Queue<String> queue = new ArrayDeque<>();

        System.out.println(queue.remove());
    }

}

請回答：正常輸出、編譯失敗，還是執行期錯誤？原因是什麼？

完整選項

無，執行判斷題。

我的回答
執行期錯誤
陣列被remove改變
正確答案
執行期錯誤
NoSuchElementException
完整詳解

你的結果判斷正確，但原因錯誤。

這題不是陣列，也不是 Arrays.asList()。

這裡是空的 Queue：

Queue<String> queue = new ArrayDeque<>();

呼叫：

queue.remove()

代表：

取出 queue 頭部元素

但 queue 是空的，所以會丟：

NoSuchElementException

如果你不想空 queue 時丟例外，應該用：

queue.poll()

空時會回傳 null。

本題觀念
Queue.remove()：空佇列丟 NoSuchElementException
Queue.poll()：空佇列回傳 null
面試高頻

中高頻。

Q28
完整問題

關於下面程式，哪一個說法正確？

import java.util.\*;

public class Main {
public static void main(String[] args) {
PriorityQueue<Integer> queue = new PriorityQueue<>();

        queue.offer(30);
        queue.offer(10);
        queue.offer(20);

        System.out.println(queue);
    }

}
完整選項

A. 一定輸出 [10, 20, 30]
B. 一定輸出 [30, 10, 20]
C. println(queue) 的順序不應依賴；只保證 poll() 順序
D. 編譯失敗

我的回答

C

正確答案

C

完整詳解

PriorityQueue 保證的是：

每次 poll() 會取出目前優先順序最高的元素

但它不保證：

System.out.println(queue)

印出來的整體順序一定是排序後的順序。

要確認取出順序，應該用：

while (!queue.isEmpty()) {
System.out.println(queue.poll());
}
本題觀念
PriorityQueue 只保證 poll 順序
不保證 iterator / println 順序
面試高頻

中高頻。

Q29
完整問題

下列哪一行最適合表示「我想要 Stack 行為」？

完整選項

A.

Stack<String> stack = new Stack<>();

B.

Deque<String> stack = new ArrayDeque<>();

C.

Queue<String> stack = new PriorityQueue<>();

D.

Map<String, String> stack = new HashMap<>();
我的回答

B

正確答案

B

完整詳解

現代 Java 若要 Stack 行為，通常使用：

Deque<String> stack = new ArrayDeque<>();

Stack 行為：

LIFO
後進先出

使用方法：

stack.push("A");
stack.push("B");

System.out.println(stack.pop()); // B

java.util.Stack 是舊類別，不是新程式優先選擇。

本題觀念
Stack 行為：Deque + ArrayDeque
面試高頻

中頻。

Q30
完整問題

下列哪一行最適合表示「我想要一般 FIFO Queue 行為」？

完整選項

A.

List<String> queue = new ArrayList<>();

B.

Queue<String> queue = new ArrayDeque<>();

C.

Set<String> queue = new HashSet<>();

D.

Map<String, String> queue = new HashMap<>();
我的回答

B

正確答案

B

完整詳解

一般 FIFO Queue 行為適合：

Queue<String> queue = new ArrayDeque<>();

使用：

queue.offer("A");
queue.offer("B");

System.out.println(queue.poll()); // A
System.out.println(queue.poll()); // B

ArrayDeque 是 Deque 的實作，也可以當一般 Queue 使用。

本題觀念
Queue + ArrayDeque = 常見 FIFO queue 實作
面試高頻

中頻。

Q31
完整問題

請說明：

List、Set、Map 最核心的差異。
完整選項

無，簡答題。

我的回答
List 單一值 可重複
Set 單一值 不可重複
Map 鍵值對 KEY不可重複
正確答案
List 是有順序的單一元素集合，允許重複，可以用 index 存取。
Set 是不允許重複的單一元素集合，通常不靠 index 存取。
Map 是 key-value pair 結構，不是 Collection；key 不可重複，value 可以重複。
完整詳解

你的回答大方向正確，但還要補三個重點：

List 有順序，可以 index 存取
Map 不是 Collection
Map 的 value 可以重複

List：

List<String> list = new ArrayList<>();
list.add("A");
list.add("B");
list.add("A");

結果：

[A, B, A]

Set：

Set<String> set = new HashSet<>();
set.add("A");
set.add("B");
set.add("A");

結果只會有兩個元素。

Map：

Map<String, Integer> map = new HashMap<>();
map.put("Amy", 90);
map.put("Bob", 90);

這是合法的，因為 value 可以重複。

但：

map.put("Amy", 100);

會覆蓋原本 Amy 的 value。

本題觀念
類型 資料型態 重複 順序 / 存取
List 單一元素 可重複 有順序，可 index
Set 單一元素 不可重複 視實作決定順序
Map key-value key 不可重複，value 可重複 用 key 取 value
面試高頻

非常高頻。

Q32
完整問題

請說明：

HashSet、LinkedHashSet、TreeSet 的差異。

請一定要提到：

是否允許重複
是否保留順序
是否排序
完整選項

無，簡答題。

我的回答
HashSet 不允許重複 保留順序
LinkedHashSet 不允許重複 不保留順序
TreeSet 不允許重複 排序
正確答案
HashSet：不允許重複，不保證順序，不排序。
LinkedHashSet：不允許重複，保留插入順序，不排序。
TreeSet：不允許重複，不保留插入順序，依排序規則排序。
完整詳解

你把 HashSet 和 LinkedHashSet 的順序特性講反了。

正確是：

HashSet 不保證順序
LinkedHashSet 保留插入順序

HashSet：

Set<String> set = new HashSet<>();

重點是查找快、不重複，但不保證順序。

LinkedHashSet：

Set<String> set = new LinkedHashSet<>();

重點是保留插入順序。

TreeSet：

Set<String> set = new TreeSet<>();

重點是依排序規則排列。

本題觀念
類型 重複 插入順序 排序
HashSet 不允許 不保證 不排序
LinkedHashSet 不允許 保留 不排序
TreeSet 不允許 不保留 會排序
面試高頻

非常高頻。這題一定要記熟。

Q33
完整問題

請說明：

HashMap、LinkedHashMap、TreeMap 的差異。

請一定要提到：

key 是否重複
插入順序
key 排序
完整選項

無，簡答題。

我的回答
HashMap key 不重複 自訂義class不一定 隨機插入順序
LinkedHashMap key 不重複 自訂義class不一定 保留插入順序
TreeMap key 不重複 自訂義class不一定 key 排序
正確答案
HashMap：key 不可重複，不保證順序，不排序。
LinkedHashMap：key 不可重複，保留插入順序，不依 key 排序。
TreeMap：key 不可重複，依 key 的排序規則保存資料。
完整詳解

你的大方向有抓到，但有兩個地方要修正。

第一，HashMap 不是「隨機插入順序」。
正確說法是：

不保證順序

第二，自訂 class 不是「key 不一定重複」。
更精準是：

key 是否被視為重複，取決於 equals/hashCode 或 compareTo/Comparator 是否正確設計。

在 HashMap 裡，自訂 key 要正確 override：

equals()
hashCode()

在 TreeMap 裡，自訂 key 要有：

Comparable

或提供：

Comparator
本題觀念
類型 key 重複 順序
HashMap key 不可重複 不保證順序
LinkedHashMap key 不可重複 保留插入順序
TreeMap key 不可重複 依 key 排序
面試高頻

高頻。

Q34
完整問題

請說明：

HashSet 和 TreeSet 判斷「重複」的方式有什麼不同？
完整選項

無，簡答題。

我的回答
HashSet hashcode equal
TreeSet 兩個相減
正確答案
HashSet 判斷重複主要依賴 hashCode() 與 equals()。
TreeSet 判斷重複主要依賴 compareTo() 或 Comparator.compare() 的結果是否為 0。
完整詳解

你的回答方向有一點點碰到，但太簡化，而且 TreeSet 兩個相減 不是通用規則。

HashSet：

先看 hashCode()
再用 equals() 確認是否相等

例如：

Set<String> set = new HashSet<>();
set.add("Java");
set.add(new String("Java"));

結果 size 是 1，因為 String 的 equals() 和 hashCode() 依內容判斷。

TreeSet：

看 compareTo() 或 Comparator.compare() 是否回傳 0

例如：

@Override
public int compareTo(User other) {
return this.age - other.age;
}

這只是其中一種寫法，不代表 TreeSet 一定是「兩個相減」。

更安全寫法是：

return Integer.compare(this.age, other.age);

因為直接相減可能有 overflow 風險。

本題觀念
HashSet：hashCode + equals
TreeSet：compareTo / Comparator 回傳 0
面試高頻

非常高頻。

Q35
完整問題

請說明：

Arrays.asList() 回傳的 List 有什麼限制？

請一定要提到：

set
add
remove
編譯期 / 執行期
完整選項

無，簡答題。

我的回答
add
remove 會改變陣列長度不能直接用 要改用 iterattor 方法
編譯期用的話可以過 執行期可能拋出錯誤
set則可以使用
正確答案
Arrays.asList() 回傳固定大小的 List。
set() 可以使用，因為它不改變 List 大小。
add() / remove() 會改變大小，所以編譯期可通過，但執行期會丟 UnsupportedOperationException。
若需要可增刪的 List，應該包成 new ArrayList<>(Arrays.asList(...))。
完整詳解

你大方向正確，但有一個重要錯點：

不是改用 Iterator 方法

Iterator.remove() 本質上還是刪除元素，也會改變大小。
對 Arrays.asList() 回傳的固定大小 List 來說，這一樣不支援。

錯誤示範：

List<String> list = Arrays.asList("A", "B");

Iterator<String> it = list.iterator();
it.next();
it.remove(); // 仍可能丟 UnsupportedOperationException

正確做法是建立真正可變大小的 ArrayList：

List<String> list = new ArrayList<>(Arrays.asList("A", "B"));

list.add("C");
list.remove("A");
本題觀念
Arrays.asList() 固定大小
set() 可用
add/remove 編譯會過，執行期 UnsupportedOperationException
要增刪請 new ArrayList<>(Arrays.asList(...))
面試高頻

中高頻。

Q36
完整問題

請說明：

Queue、Deque、PriorityQueue、Stack、ArrayDeque 的關係。
完整選項

無，簡答題。

我的回答
父interface
Queue >> 子interface PriorityQueue
Deque >> 子interface ArrayDeque

程式語言的概念：Stack
正確答案
Queue 是介面，代表佇列型資料結構，常見語義是 FIFO。
Deque 是 Queue 的子介面，代表雙端佇列，可以從頭尾兩端操作。
PriorityQueue 是 Queue 的實作類別，不是子介面；它依優先順序 poll，不是 FIFO。
ArrayDeque 是 Deque 的實作類別，不是子介面；常用來實作 Queue、Deque、Stack 行為。
Stack 是 LIFO 資料結構概念；java.util.Stack 是舊類別，新程式通常用 Deque + ArrayDeque。
完整詳解

你這題最大的錯點是把 class 和 interface 關係搞錯。

正確關係是：

Queue 是 interface
Deque 是 interface，且 extends Queue
PriorityQueue 是 class，implements Queue
ArrayDeque 是 class，implements Deque
Stack 是資料結構概念
java.util.Stack 是 legacy class

可以想成：

Collection
└── Queue
├── PriorityQueue class
└── Deque interface
└── ArrayDeque class

但更精準地說，PriorityQueue 和 ArrayDeque 不是子 interface，而是實作類別。

常見寫法：

一般 FIFO Queue：

Queue<String> queue = new ArrayDeque<>();

Stack 行為：

Deque<String> stack = new ArrayDeque<>();

Priority Queue：

Queue<Integer> pq = new PriorityQueue<>();
本題觀念
名稱 類型 重點
Queue interface 佇列規格
Deque interface 雙端佇列，extends Queue
PriorityQueue class 依優先順序取出
ArrayDeque class 常用 Queue / Deque / Stack 實作
Stack 概念 / 舊 class LIFO；新程式不優先用 java.util.Stack
面試高頻

中高頻。尤其常問：

Queue 和 Deque 差在哪？
PriorityQueue 是不是 FIFO？
為什麼 Stack 建議用 Deque？
ArrayDeque 是什麼？
最後整理：你現在還要修正的核心錯點

1. HashSet / LinkedHashSet 順序
   HashSet：不保證順序
   LinkedHashSet：保留插入順序
   TreeSet：排序
2. HashMap / LinkedHashMap / TreeMap 順序
   HashMap：不保證順序
   LinkedHashMap：保留插入順序
   TreeMap：依 key 排序
3. PriorityQueue 不是 Stack
   PriorityQueue：依優先順序
   Stack：LIFO
   Queue：通常 FIFO
4. Arrays.asList() 不能靠 Iterator 解決 remove
   固定大小 List 不支援 add/remove
   Iterator.remove() 也不是解法
   要用 new ArrayList<>(Arrays.asList(...))
5. interface / class 關係
   Queue：interface
   Deque：interface
   PriorityQueue：class
   ArrayDeque：class
   Stack：資料結構概念，java.util.Stack 是舊 class
