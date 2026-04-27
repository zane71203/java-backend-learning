以下是依你貼的 **On Java 第 21 章：陣列（Arrays / 数组）** 內容整理成的講義版。
我有照你的要求處理：

- 用**台灣繁體中文用法**
- 語氣改得比較白話
- **不破壞原本章節結構**
- 保留重點，不整段硬翻原文
- 程式碼只放必要片段，避免太長不好讀

來源：

---

# 第 21 章 陣列（Arrays / 数组）講義

## 0. 本章定位

這章在講 Java 的 **陣列（Array / 数组）**。

你前面已經學過怎麼宣告、建立、初始化陣列，例如：

```java
int[] numbers = new int[5];
```

但這章不是只講「怎麼用陣列」，而是要你理解：

```text
陣列在 Java 裡到底是什麼？
陣列跟物件、heap、reference 有什麼關係？
陣列跟 Collection 差在哪？
Arrays 工具類可以做什麼？
```

對你目前的 Java Backend roadmap 來說，這章最重要的是：

```text
array memory model
reference behavior
multidimensional arrays
Arrays utility
```

也就是：

```text
陣列記憶體模型
參考行為
多維陣列
Arrays 工具類
```

---

# 1. 陣列特性

## 1.1 陣列為什麼特別？

Java 裡可以存很多資料的東西不只陣列，還有：

```java
ArrayList
List
Set
Map
```

那為什麼還要學陣列？

原文提到陣列有三個主要特色：

```text
1. 效率高
2. 型別固定
3. 可以直接存基本型別
```

---

## 1.2 效率高

陣列是很單純的線性資料結構。

例如：

```java
int[] nums = {10, 20, 30};
System.out.println(nums[1]);
```

這裡的 `nums[1]` 可以很快找到第二個元素。

因為陣列在記憶體模型上是固定長度、固定索引的資料結構。

白話講：

```text
陣列像一排固定編號的櫃子。
你知道第幾格，就可以直接拿。
```

---

## 1.3 代價：長度固定

陣列的缺點也很明顯：

```java
int[] nums = new int[3];
```

這個陣列一旦建立，就是 3 格。

不能後來直接變成 4 格、5 格。

如果你需要「可以一直新增」的結構，通常會用：

```java
ArrayList<Integer> list = new ArrayList<>();
list.add(10);
list.add(20);
```

ArrayList 內部其實也是用陣列包起來，但它幫你處理「空間不夠時重新配置」的問題。

---

## 1.4 實務建議

一般 Java 程式開發中，尤其 backend 開發，通常：

```text
預設先用 Collection，例如 List、Map
真的有性能需求，才考慮直接用陣列
```

也就是：

```text
一般情況：List
性能敏感、資料長度固定：Array
```

但你還是要懂陣列，因為：

```text
1. Java 底層很多東西跟陣列有關
2. 面試常問
3. Collections 的底層常常包陣列
4. 別人的程式碼一定會看到陣列
```

---

# 2. 陣列 vs Collection

## 2.1 陣列寫法

```java
BerylliumSphere[] spheres = new BerylliumSphere[10];
```

這代表：

```text
建立一個可以放 10 個 BerylliumSphere 參考的陣列
```

注意，不是建立 10 個物件。

這一點非常重要。

---

## 2.2 物件陣列的預設值是 null

```java
BerylliumSphere[] spheres = new BerylliumSphere[10];
```

此時陣列裡面是：

```text
[null, null, null, null, null, null, null, null, null, null]
```

因為你只是建立「可以放物件參考的容器」，還沒有真的 new 出物件。

你要自己放：

```java
spheres[0] = new BerylliumSphere();
```

---

## 2.3 基本型別陣列會有預設值

例如：

```java
int[] nums = new int[5];
```

預設是：

```text
[0, 0, 0, 0, 0]
```

不同型別的預設值：

| 型別                      | 預設值   |
| ------------------------- | -------- |
| int / long / short / byte | 0        |
| double / float            | 0.0      |
| boolean                   | false    |
| char                      | `\u0000` |
| 物件參考                  | null     |

---

## 2.4 ArrayList 寫法

```java
List<Integer> list = new ArrayList<>();
list.add(10);
list.add(20);
```

ArrayList 比陣列彈性高。

你不用先決定固定長度，也可以一直新增元素。

---

## 2.5 陣列和 List 的差異

| 比較項目 | 陣列 Array   | List / ArrayList  |
| -------- | ------------ | ----------------- |
| 長度     | 固定         | 可變              |
| 存取方式 | `arr[index]` | `list.get(index)` |
| 新增元素 | 不方便       | `add()`           |
| 可讀性   | 簡單直接     | 功能更多          |
| 效率     | 通常較高     | 稍有額外成本      |
| 實務常用 | 特定場景     | 一般開發常用      |

---

# 3. 用來顯示陣列的工具

Java 提供：

```java
Arrays.toString(array)
```

例如：

```java
int[] nums = {1, 2, 3};
System.out.println(Arrays.toString(nums));
```

輸出：

```text
[1, 2, 3]
```

---

## 3.1 為什麼不能直接印陣列？

如果你寫：

```java
System.out.println(nums);
```

你不會得到：

```text
[1, 2, 3]
```

而是類似：

```text
[I@5acf9800
```

因為陣列本身是物件，直接印會印出物件識別資訊，不會自動幫你把每個元素列出來。

所以要用：

```java
Arrays.toString(nums)
```

多維陣列要用：

```java
Arrays.deepToString(array)
```

---

# 4. 陣列是一等物件

## 4.1 陣列本身是物件

這章最重要的觀念之一：

```text
Java 的陣列本身是物件。
```

所以這行：

```java
int[] nums = new int[5];
```

不是單純開五個 int 而已。

更精準地說：

```text
在 heap 裡建立一個 int array 物件，
nums 這個變數保存的是那個陣列物件的 reference。
```

---

## 4.2 陣列 reference

例如：

```java
int[] a = {1, 2, 3};
int[] b = a;
```

這裡不是複製一份新陣列。

而是：

```text
a 和 b 指向同一個陣列物件。
```

所以：

```java
b[0] = 99;
System.out.println(a[0]);
```

輸出會是：

```text
99
```

因為 `a` 和 `b` 其實看的是同一份資料。

---

## 4.3 length 是陣列的固定屬性

每個陣列都有：

```java
arr.length
```

例如：

```java
int[] nums = new int[5];
System.out.println(nums.length);
```

輸出：

```text
5
```

注意：

```text
length 代表陣列容量，不代表裡面有幾個「有效資料」。
```

例如：

```java
String[] names = new String[5];
names[0] = "Zane";
```

此時：

```java
names.length
```

還是：

```text
5
```

但真正有放資料的只有第 0 格。

---

# 5. 陣列初始化方式

## 5.1 宣告但不初始化

```java
int[] nums;
```

這只是宣告變數，還沒有指向任何陣列物件。

local variable 如果沒初始化就使用，會編譯錯誤。

---

## 5.2 使用 new 建立固定長度

```java
int[] nums = new int[5];
```

這會建立長度 5 的 int 陣列。

預設值是：

```text
[0, 0, 0, 0, 0]
```

---

## 5.3 聚合初始化

```java
int[] nums = {11, 47, 93};
```

這是最簡潔的初始化方式。

Java 會自動建立長度 3 的陣列。

---

## 5.4 動態聚合初始化

```java
int[] nums = new int[]{1, 2, 3};
```

這種寫法常用在：

```text
你想在某個地方直接建立一個陣列物件
```

例如當作方法參數：

```java
printNumbers(new int[]{1, 2, 3});
```

---

# 6. 返回陣列

Java 方法可以直接回傳陣列。

例如：

```java
static String[] getNames() {
    return new String[]{"Amy", "Bob", "Cindy"};
}
```

使用：

```java
String[] names = getNames();
```

這裡回傳的不是把整個陣列複製出來，而是：

```text
回傳陣列物件的 reference。
```

---

## 6.1 為什麼 Java 可以放心回傳陣列？

因為 Java 有垃圾回收機制（Garbage Collection / 垃圾回收）。

只要這個陣列還被外部變數參考，就會留著。

沒有人再使用它時，之後會被 GC 清理。

---

# 7. 多維陣列

## 7.1 二維陣列

```java
int[][] matrix = {
    {1, 2, 3},
    {4, 5, 6}
};
```

可以想成：

```text
matrix 是一個陣列，
裡面的每個元素又是一個 int 陣列。
```

也就是：

```text
int[] 的陣列
```

---

## 7.2 多維陣列不是魔法

```java
int[][] a
```

可以理解成：

```text
a 是一個 reference，
指向一個陣列物件，
這個陣列裡面每一格又指向另一個 int[] 陣列。
```

所以 Java 的二維陣列不是一定要長得像整齊矩陣。

---

## 7.3 不規則陣列

例如：

```java
int[][] nums = new int[3][];
nums[0] = new int[2];
nums[1] = new int[5];
nums[2] = new int[1];
```

這是合法的。

因為每一列其實都是獨立的陣列物件。

所以它可以長這樣：

```text
[
  [0, 0],
  [0, 0, 0, 0, 0],
  [0]
]
```

這叫做：

```text
不規則陣列（Ragged Array）
```

---

## 7.4 顯示多維陣列

一維陣列用：

```java
Arrays.toString(array)
```

多維陣列用：

```java
Arrays.deepToString(array)
```

例如：

```java
int[][] matrix = {
    {1, 2},
    {3, 4}
};

System.out.println(Arrays.deepToString(matrix));
```

輸出：

```text
[[1, 2], [3, 4]]
```

---

# 8. 泛型陣列

## 8.1 泛型和陣列不太合

這章提到：

```text
陣列和泛型搭配起來不太自然。
```

例如你不能這樣：

```java
List<String>[] lists = new List<String>[10]; // 不合法
```

原因是：

```text
泛型有 type erasure（型別擦除）
陣列又需要在 runtime 知道明確型別
兩者設計理念衝突
```

---

## 8.2 可以宣告 reference，但不能直接建立

可以寫：

```java
List<String>[] lists;
```

但不能直接：

```java
new List<String>[10]
```

通常如果你需要很多泛型資料，實務上會改用：

```java
List<List<String>> lists = new ArrayList<>();
```

比較安全，也比較符合 Java 實務風格。

---

## 8.3 實務建議

這裡先記住一句：

```text
如果你想把泛型和陣列混在一起，通常代表你應該先考慮 Collection。
```

也就是：

```text
優先用 List<T>
少用 T[]
```

---

# 9. Arrays.fill()

## 9.1 fill() 的用途

`Arrays.fill()` 可以把同一個值塞滿整個陣列。

例如：

```java
int[] nums = new int[5];
Arrays.fill(nums, 7);
System.out.println(Arrays.toString(nums));
```

輸出：

```text
[7, 7, 7, 7, 7]
```

---

## 9.2 填部分範圍

```java
String[] words = new String[6];
Arrays.fill(words, "Hello");
Arrays.fill(words, 3, 5, "World");
```

結果：

```text
[Hello, Hello, Hello, World, World, Hello]
```

注意範圍：

```java
Arrays.fill(array, fromIndex, toIndex, value);
```

是：

```text
包含 fromIndex
不包含 toIndex
```

所以：

```java
Arrays.fill(words, 3, 5, "World");
```

會改：

```text
index 3 和 index 4
```

不包含 index 5。

---

# 10. Arrays.setAll()

## 10.1 setAll() 的用途

`Arrays.setAll()` 比 `fill()` 彈性更高。

`fill()` 是全部填一樣的值。

`setAll()` 可以依照 index 產生不同的值。

例如：

```java
int[] nums = new int[5];
Arrays.setAll(nums, i -> i);
System.out.println(Arrays.toString(nums));
```

輸出：

```text
[0, 1, 2, 3, 4]
```

---

## 10.2 Lambda 裡的 i 是什麼？

```java
i -> i
```

這裡的 `i` 是目前元素的 index。

所以：

```java
Arrays.setAll(nums, i -> i * 10);
```

會得到：

```text
[0, 10, 20, 30, 40]
```

---

## 10.3 setAll() 可以建立物件陣列

例如：

```java
Bob[] bobs = new Bob[5];
Arrays.setAll(bobs, i -> new Bob(i));
```

意思是：

```text
幫每一格建立一個 Bob 物件
並把 index 傳進去
```

---

# 11. 遞增產生器 Count

原文建立了一個 `Count` 工具，用來產生遞增資料。

概念上就是：

```text
每次呼叫，就產生下一個值。
```

例如：

```text
0, 1, 2, 3, 4...
```

這種工具主要是為了方便測試陣列。

你目前不用急著完全掌握原文那一整包工具類。

你要先懂核心概念：

```text
產生器 generator 可以自動產生測試資料
Arrays.setAll() 可以用 generator 填陣列
```

---

# 12. 隨機產生器 Rand

原文也建立了 `Rand` 工具，用來產生隨機資料。

例如：

```text
隨機 int
隨機 boolean
隨機 char
隨機 String
```

這一段的目的不是要你背工具類，而是讓你理解：

```text
測試陣列操作時，常常需要快速產生假資料。
```

在實務上，你之後會常見類似概念：

```text
mock data
test data
faker data
```

---

# 13. 泛型與基本型別陣列

## 13.1 基本型別不能直接用泛型

Java 泛型不能直接放基本型別。

不能寫：

```java
List<int> nums; // 不合法
```

要寫：

```java
List<Integer> nums;
```

---

## 13.2 自動裝箱與拆箱

```java
Integer x = 10; // autoboxing
int y = x;      // auto-unboxing
```

白話講：

```text
int 和 Integer 之間，Java 會在很多情況自動幫你轉。
```

---

## 13.3 primitive array 和 wrapper array

基本型別陣列：

```java
int[] nums = {1, 2, 3};
```

包裝型別陣列：

```java
Integer[] nums = {1, 2, 3};
```

差異：

| 類型            | 例子        | 裡面放什麼             |
| --------------- | ----------- | ---------------------- |
| primitive array | `int[]`     | 真正的 int 值          |
| wrapper array   | `Integer[]` | Integer 物件 reference |

---

# 14. 修改陣列元素

`Arrays.setAll()` 不只可以初始化陣列，也可以修改既有陣列。

例如：

```java
double[] nums = {10.0, 20.0, 30.0};
Arrays.setAll(nums, i -> nums[i] / 10);
```

結果：

```text
[1.0, 2.0, 3.0]
```

因為 lambda 裡可以拿到：

```java
nums[i]
```

所以可以根據舊值產生新值。

---

# 15. 陣列並行

## 15.1 不要一開始就追求並行

原文這段很重要。

它的意思是：

```text
並行不一定比較快。
```

很多人聽到 parallel 就以為一定更快，但實務上不是。

因為並行也有成本：

```text
切分任務的成本
執行緒協調成本
合併結果成本
記憶體成本
```

---

## 15.2 實務建議

先記這句：

```text
先寫簡單版本。
真的遇到性能瓶頸，再考慮 parallel。
```

這對你以後做 backend 也很重要。

不要一開始就做複雜架構。

應該是：

```text
simple first
measure later
optimize when needed
```

---

# 16. parallelSetAll()

`Arrays.parallelSetAll()` 是 `setAll()` 的並行版本。

例如：

```java
Arrays.parallelSetAll(nums, i -> i);
```

理論上，大陣列可能更快。

但你目前先知道：

```text
setAll()：一般版本
parallelSetAll()：可能平行處理的大資料版本
```

初學階段不要優先使用 `parallelSetAll()`。

---

# 17. Arrays 工具類總覽

`java.util.Arrays` 提供很多陣列工具。

重要方法如下：

| 方法               | 用途             |
| ------------------ | ---------------- |
| `toString()`       | 顯示一維陣列     |
| `deepToString()`   | 顯示多維陣列     |
| `fill()`           | 填入同一個值     |
| `setAll()`         | 依 index 產生值  |
| `copyOf()`         | 複製陣列         |
| `copyOfRange()`    | 複製部分範圍     |
| `equals()`         | 比較一維陣列內容 |
| `deepEquals()`     | 比較多維陣列內容 |
| `sort()`           | 排序             |
| `parallelSort()`   | 平行排序         |
| `binarySearch()`   | 二分搜尋         |
| `stream()`         | 轉成 Stream      |
| `parallelPrefix()` | 平行前綴累積     |

你目前最該先熟的是：

```text
toString
deepToString
fill
setAll
copyOf
copyOfRange
equals
sort
binarySearch
```

---

# 18. 陣列拷貝

## 18.1 copyOf()

```java
int[] a = {1, 2, 3};
int[] b = Arrays.copyOf(a, a.length);
```

這會建立一個新陣列。

所以：

```java
a[0] = 99;
System.out.println(Arrays.toString(b));
```

`b` 不會被影響。

---

## 18.2 複製時可以改長度

```java
int[] a = {1, 2, 3};
int[] b = Arrays.copyOf(a, 5);
```

結果：

```text
[1, 2, 3, 0, 0]
```

如果新長度比原本長，多出來的部分補預設值。

---

## 18.3 copyOfRange()

```java
int[] a = {10, 20, 30, 40, 50};
int[] b = Arrays.copyOfRange(a, 1, 4);
```

結果：

```text
[20, 30, 40]
```

一樣是：

```text
包含起點，不包含終點
```

---

## 18.4 物件陣列是淺拷貝

如果你複製的是物件陣列：

```java
User[] users2 = Arrays.copyOf(users1, users1.length);
```

這裡只複製 reference。

不是把每個 User 物件也完整複製一份。

這叫：

```text
淺拷貝（Shallow Copy / 浅拷贝）
```

白話講：

```text
新陣列是新的。
但裡面的物件還是同一批。
```

---

# 19. 陣列比較

## 19.1 不要用 `==` 比較陣列內容

```java
int[] a = {1, 2, 3};
int[] b = {1, 2, 3};

System.out.println(a == b);
```

結果是：

```text
false
```

因為 `a == b` 比的是：

```text
是不是同一個陣列物件
```

不是內容是否一樣。

---

## 19.2 用 Arrays.equals()

```java
System.out.println(Arrays.equals(a, b));
```

結果：

```text
true
```

因為它會逐格比較內容。

---

## 19.3 多維陣列用 deepEquals()

```java
int[][] a = {{1, 2}, {3, 4}};
int[][] b = {{1, 2}, {3, 4}};

System.out.println(Arrays.deepEquals(a, b));
```

結果：

```text
true
```

多維陣列不要只用 `Arrays.equals()`，要用：

```java
Arrays.deepEquals()
```

---

# 20. Stream 和陣列

陣列可以轉成 Stream。

例如：

```java
int[] nums = {1, 2, 3, 4, 5};

Arrays.stream(nums)
      .map(n -> n * 10)
      .forEach(System.out::println);
```

---

## 20.1 只有部分 primitive array 支援 Arrays.stream()

直接支援：

```text
int[]
long[]
double[]
```

例如：

```java
Arrays.stream(new int[]{1, 2, 3});
```

但下面這些不直接支援 primitive stream：

```text
boolean[]
byte[]
char[]
short[]
float[]
```

如果要處理，通常要轉成包裝型別陣列，或用其他方式。

---

# 21. 陣列排序

## 21.1 基本排序

```java
int[] nums = {3, 1, 2};
Arrays.sort(nums);
System.out.println(Arrays.toString(nums));
```

輸出：

```text
[1, 2, 3]
```

---

## 21.2 String 排序

```java
String[] words = {"banana", "apple", "cat"};
Arrays.sort(words);
```

結果：

```text
[apple, banana, cat]
```

---

## 21.3 物件排序：Comparable

如果你要讓自己的類別可以排序，可以實作：

```java
Comparable<T>
```

核心方法：

```java
compareTo()
```

例如概念上：

```java
class User implements Comparable<User> {
    int age;

    @Override
    public int compareTo(User other) {
        return Integer.compare(this.age, other.age);
    }
}
```

這代表：

```text
User 預設用 age 排序。
```

---

## 21.4 Comparator

如果你不想改類別本身，或想有不同排序方式，可以用：

```java
Comparator<T>
```

例如：

```java
Arrays.sort(users, Comparator.comparing(user -> user.age));
```

白話講：

```text
Comparable：物件自己知道怎麼比
Comparator：外部提供一套比較規則
```

---

## 21.5 面試高頻

這裡很常問：

```text
Comparable 和 Comparator 差在哪？
```

簡答：

```text
Comparable 是類別本身的自然排序。
Comparator 是外部提供的排序策略。
```

---

# 22. parallelSort()

`Arrays.parallelSort()` 是平行排序版本。

```java
Arrays.parallelSort(nums);
```

大資料量時可能比 `sort()` 快。

但一樣：

```text
不是所有情況都比較快。
```

初學與一般業務程式，先用：

```java
Arrays.sort()
```

就好。

---

# 23. binarySearch 二分搜尋

## 23.1 使用前一定要排序

```java
int[] nums = {5, 1, 3, 2, 4};
Arrays.sort(nums);

int index = Arrays.binarySearch(nums, 3);
```

如果找到，會回傳 index。

---

## 23.2 沒排序就使用，結果不可靠

這點很重要：

```text
binarySearch() 必須用在已排序陣列。
```

如果沒排序就查，結果不可預期。

---

## 23.3 找不到時回傳負數

如果找不到，它會回傳負數。

這個負數不是單純的 `-1`，而是跟插入點有關。

初學先記：

```text
>= 0：有找到
< 0：沒找到
```

就夠了。

---

## 23.4 如果排序用了 Comparator，搜尋也要用同一個 Comparator

例如：

```java
Arrays.sort(words, String.CASE_INSENSITIVE_ORDER);

int index = Arrays.binarySearch(
    words,
    "apple",
    String.CASE_INSENSITIVE_ORDER
);
```

排序規則和搜尋規則要一致。

---

# 24. parallelPrefix()

## 24.1 它在做什麼？

`parallelPrefix()` 類似累積運算。

例如：

```java
int[] nums = {0, 1, 2, 3, 4};
Arrays.parallelPrefix(nums, Integer::sum);
```

結果：

```text
[0, 1, 3, 6, 10]
```

計算方式：

```text
index 0: 0
index 1: 0 + 1 = 1
index 2: 0 + 1 + 2 = 3
index 3: 0 + 1 + 2 + 3 = 6
index 4: 0 + 1 + 2 + 3 + 4 = 10
```

---

## 24.2 初學階段怎麼看？

這個方法比較進階。

你現在先知道：

```text
parallelPrefix() 會把陣列改成「累積結果」。
```

實務上不常一開始就用。

如果只是要得到總和，用 Stream 或 for loop 通常比較直覺。

---

# 25. 本章總結

## 25.1 陣列的本質

請記住：

```text
Java 的陣列是物件。
陣列變數保存的是 reference。
陣列長度固定。
陣列元素可以是基本型別，也可以是物件 reference。
```

---

## 25.2 陣列 vs Collection

現代 Java 實務中通常：

```text
優先用 Collection
必要時才用陣列
```

原因：

```text
Collection 彈性更高
泛型支援更自然
功能更完整
```

但陣列仍然重要，因為：

```text
1. 它是 Java 基礎資料結構
2. 很多底層和效能場景會用到
3. Collection 底層常常和陣列有關
4. 面試會問
```

---

## 25.3 你這章最該掌握的能力

不是背所有 `Arrays` 方法，而是掌握這些：

```text
1. 會建立陣列
2. 會走訪陣列
3. 知道陣列是物件
4. 知道 object array 預設是 null
5. 知道 primitive array 有預設值
6. 知道陣列 reference aliasing
7. 會用 Arrays.toString()
8. 會用 Arrays.deepToString()
9. 會用 Arrays.copyOf()
10. 會用 Arrays.equals()
11. 會用 Arrays.sort()
12. 知道 binarySearch 前要先排序
```

---

# 26. 面試高頻整理

## Q1：Java 陣列是不是物件？

是。

陣列本身是 heap 上的物件，陣列變數保存的是 reference。

---

## Q2：`int[] a = new int[5];` 裡面預設值是什麼？

全部是 `0`。

---

## Q3：`String[] s = new String[5];` 裡面預設值是什麼？

全部是 `null`。

因為 String 是物件型別，陣列裡放的是 reference。

---

## Q4：陣列長度能不能改？

不能。

建立後長度固定。

如果需要可變長度，通常用 `ArrayList`。

---

## Q5：`arr.length` 是方法嗎？

不是。

`length` 是陣列的屬性。

所以寫：

```java
arr.length
```

不是：

```java
arr.length()
```

但 String 是：

```java
str.length()
```

這點很常搞混。

---

## Q6：陣列和 ArrayList 差在哪？

陣列長度固定，ArrayList 長度可變。

陣列用 `[]` 存取，ArrayList 用 `get()` / `add()` 等方法操作。

---

## Q7：為什麼物件陣列 new 完之後裡面是 null？

因為你只建立了「放 reference 的陣列」，還沒有建立每一個物件。

例如：

```java
User[] users = new User[3];
```

這不是建立 3 個 User。

只是建立一個可以放 3 個 User reference 的陣列。

---

## Q8：`Arrays.equals()` 和 `==` 差在哪？

`==` 比的是兩個 reference 是否指向同一個陣列物件。

`Arrays.equals()` 比的是陣列內容是否相同。

---

## Q9：多維陣列要怎麼印？

用：

```java
Arrays.deepToString(array)
```

不要用：

```java
Arrays.toString(array)
```

---

## Q10：binarySearch 使用前要注意什麼？

陣列必須先排序。

否則結果不可靠。

---

# 27. 你現在的學習重點

你現在不用把本章所有進階工具都吃完。

你的優先順序應該是：

```text
第一優先：
array declaration / initialization / index / length / loop

第二優先：
array 是 object
reference behavior
object array vs primitive array

第三優先：
multidimensional arrays
Arrays.toString / deepToString / copyOf / equals / sort

第四優先：
generic array
parallelSetAll
parallelSort
parallelPrefix
```

對你目前 Phase 0 來說，最重要的是前兩層。

---

# 28. 下一步建議練習

你下一次可以直接練這幾題：

```java
// 1. 建立 int[]，放 5 個數字，印出總和

// 2. 建立 String[]，用 for loop 印出每個名字

// 3. 建立 int[]，找最大值

// 4. 建立 int[]，複製成另一個陣列，修改原陣列，觀察副本有沒有變

// 5. 建立 User[]，觀察 new User[3] 之後裡面是不是 null

// 6. 建立 int[][]，用 Arrays.deepToString() 印出來
```

這幾題做完，你對這章的 baseline 就會穩很多。

下面直接給你 **6 題完整詳解**。
這組題目的目的不是考語法而已，而是要確認你真的理解：

```text
array declaration
array initialization
index
length
for loop
reference
copy
object array default null
multidimensional array
Arrays utility
```

---

# Exercise 1：建立 `int[]`，放 5 個數字，印出總和

## 題目

```java
// 1. 建立 int[]，放 5 個數字，印出總和
```

---

## 完整範例

```java
public class Main {
    public static void main(String[] args) {
        int[] nums = {10, 20, 30, 40, 50};

        int sum = 0;

        for (int i = 0; i < nums.length; i++) {
            sum = sum + nums[i];
        }

        System.out.println(sum);
    }
}
```

---

## 輸出

```text
150
```

---

## 詳解

這行：

```java
int[] nums = {10, 20, 30, 40, 50};
```

意思是建立一個 `int` 陣列，裡面有 5 個元素。

它的 index 是：

| index | value |
| ----: | ----: |
|     0 |    10 |
|     1 |    20 |
|     2 |    30 |
|     3 |    40 |
|     4 |    50 |

注意 Java 陣列 index 從 `0` 開始，不是從 `1` 開始。

---

這行：

```java
int sum = 0;
```

是用來累加總和的變數。

一開始設成 `0`，因為還沒有加任何數字。

---

這段：

```java
for (int i = 0; i < nums.length; i++) {
    sum = sum + nums[i];
}
```

意思是：

```text
從 index 0 開始
一路跑到 index nums.length - 1
每次把 nums[i] 加進 sum
```

`nums.length` 是 `5`。

所以實際上會跑：

```text
i = 0
i = 1
i = 2
i = 3
i = 4
```

不會跑 `i = 5`，因為條件是：

```java
i < nums.length
```

也就是：

```java
i < 5
```

---

## 執行過程

|   i | nums[i] | sum 原本 | sum 更新後 |
| --: | ------: | -------: | ---------: |
|   0 |      10 |        0 |         10 |
|   1 |      20 |       10 |         30 |
|   2 |      30 |       30 |         60 |
|   3 |      40 |       60 |        100 |
|   4 |      50 |      100 |        150 |

---

## 面試常考點

### Q：為什麼條件是 `i < nums.length`，不是 `i <= nums.length`？

因為最後一個 index 是：

```java
nums.length - 1
```

如果陣列長度是 `5`，合法 index 是：

```text
0, 1, 2, 3, 4
```

如果寫：

```java
i <= nums.length
```

就會跑到：

```java
i = 5
```

此時：

```java
nums[5]
```

會發生：

```text
ArrayIndexOutOfBoundsException
```

---

# Exercise 2：建立 `String[]`，用 for loop 印出每個名字

## 題目

```java
// 2. 建立 String[]，用 for loop 印出每個名字
```

---

## 完整範例

```java
public class Main {
    public static void main(String[] args) {
        String[] names = {"Amy", "Bob", "Cindy"};

        for (int i = 0; i < names.length; i++) {
            System.out.println(names[i]);
        }
    }
}
```

---

## 輸出

```text
Amy
Bob
Cindy
```

---

## 詳解

這行：

```java
String[] names = {"Amy", "Bob", "Cindy"};
```

建立一個 `String` 陣列。

內容如下：

| index | value     |
| ----: | --------- |
|     0 | `"Amy"`   |
|     1 | `"Bob"`   |
|     2 | `"Cindy"` |

---

這段：

```java
for (int i = 0; i < names.length; i++) {
    System.out.println(names[i]);
}
```

每次印出一個名字。

執行順序是：

```text
i = 0 → names[0] → Amy
i = 1 → names[1] → Bob
i = 2 → names[2] → Cindy
```

---

## 也可以用 enhanced for loop

這題也可以寫成：

```java
public class Main {
    public static void main(String[] args) {
        String[] names = {"Amy", "Bob", "Cindy"};

        for (String name : names) {
            System.out.println(name);
        }
    }
}
```

---

## `for-each` 寫法解釋

```java
for (String name : names)
```

意思是：

```text
從 names 陣列裡，每次拿一個 String 出來，暫時叫它 name。
```

這種寫法比較簡潔，但缺點是：

```text
拿不到 index。
```

所以如果你需要 index，例如：

```text
第 0 個名字是 Amy
第 1 個名字是 Bob
```

就用傳統 for loop。

---

## 面試常考點

### Q：`String[]` 裡面放的是 String 物件本身嗎？

更精準地說，陣列裡放的是：

```text
String 物件的 reference
```

也就是陣列每一格指向某個 String 物件。

這點之後會影響你理解：

```text
object array
null
reference
aliasing
```

---

# Exercise 3：建立 `int[]`，找最大值

## 題目

```java
// 3. 建立 int[]，找最大值
```

---

## 完整範例

```java
public class Main {
    public static void main(String[] args) {
        int[] nums = {12, 45, 7, 99, 23};

        int max = nums[0];

        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > max) {
                max = nums[i];
            }
        }

        System.out.println(max);
    }
}
```

---

## 輸出

```text
99
```

---

## 詳解

這行：

```java
int[] nums = {12, 45, 7, 99, 23};
```

建立一個數字陣列。

---

這行很重要：

```java
int max = nums[0];
```

意思是：

```text
先假設第一個元素是最大值。
```

為什麼不要寫：

```java
int max = 0;
```

因為如果陣列裡都是負數，例如：

```java
int[] nums = {-10, -3, -99};
```

如果你把 `max` 設成 `0`，答案就會錯，因為陣列裡根本沒有 `0`。

所以比較安全的做法是：

```java
int max = nums[0];
```

---

這段：

```java
for (int i = 1; i < nums.length; i++) {
    if (nums[i] > max) {
        max = nums[i];
    }
}
```

從 `i = 1` 開始，因為 `nums[0]` 已經拿來當初始最大值了。

---

## 執行過程

陣列：

```text
[12, 45, 7, 99, 23]
```

初始：

```text
max = 12
```

|   i | nums[i] | max 原本 | 是否更新 | max 更新後 |
| --: | ------: | -------: | -------- | ---------: |
|   1 |      45 |       12 | 是       |         45 |
|   2 |       7 |       45 | 否       |         45 |
|   3 |      99 |       45 | 是       |         99 |
|   4 |      23 |       99 | 否       |         99 |

最後答案：

```text
99
```

---

## 面試常考點

### Q：找最大值為什麼通常從 `nums[0]` 開始？

因為這樣可以處理任何整數陣列，包括全部是負數的情況。

錯誤寫法：

```java
int max = 0;
```

如果資料是：

```java
int[] nums = {-5, -2, -9};
```

結果會錯誤地得到 `0`。

---

# Exercise 4：建立 `int[]`，複製成另一個陣列，修改原陣列，觀察副本有沒有變

## 題目

```java
// 4. 建立 int[]，複製成另一個陣列，修改原陣列，觀察副本有沒有變
```

---

## 正確複製版本：`Arrays.copyOf()`

```java
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[] original = {1, 2, 3};

        int[] copy = Arrays.copyOf(original, original.length);

        original[0] = 99;

        System.out.println("original = " + Arrays.toString(original));
        System.out.println("copy = " + Arrays.toString(copy));
    }
}
```

---

## 輸出

```text
original = [99, 2, 3]
copy = [1, 2, 3]
```

---

## 詳解

這行：

```java
int[] copy = Arrays.copyOf(original, original.length);
```

意思是：

```text
建立一個新的 int 陣列，
把 original 裡面的值複製過去。
```

所以 `original` 和 `copy` 是兩個不同的陣列物件。

---

這行：

```java
original[0] = 99;
```

只會改 `original` 的第 0 格。

不會影響 `copy`。

---

## 記憶體概念

原本：

```text
original → [1, 2, 3]

copy     → [1, 2, 3]
```

修改後：

```text
original → [99, 2, 3]

copy     → [1, 2, 3]
```

兩個陣列是分開的。

---

## 對照：錯誤理解版本

如果你寫：

```java
int[] original = {1, 2, 3};
int[] copy = original;

original[0] = 99;

System.out.println(Arrays.toString(copy));
```

輸出會是：

```text
[99, 2, 3]
```

因為：

```java
int[] copy = original;
```

不是複製陣列內容。

它只是讓 `copy` 也指向同一個陣列物件。

---

## 兩種寫法差異

| 寫法                                       | 是否建立新陣列 | 修改 original 會不會影響 copy |
| ------------------------------------------ | -------------: | ----------------------------: |
| `int[] copy = original;`                   |             否 |                            會 |
| `Arrays.copyOf(original, original.length)` |             是 |                          不會 |

---

## 面試常考點

### Q：`int[] copy = original;` 是 copy 嗎？

不是。

這只是複製 reference。

真正的陣列內容複製要用：

```java
Arrays.copyOf()
```

或自己寫 for loop 一格一格複製。

---

# Exercise 5：建立 `User[]`，觀察 `new User[3]` 之後裡面是不是 null

## 題目

```java
// 5. 建立 User[]，觀察 new User[3] 之後裡面是不是 null
```

---

## 完整範例

```java
import java.util.Arrays;

class User {
    String name;

    User(String name) {
        this.name = name;
    }
}

public class Main {
    public static void main(String[] args) {
        User[] users = new User[3];

        System.out.println(Arrays.toString(users));

        System.out.println(users[0] == null);
        System.out.println(users[1] == null);
        System.out.println(users[2] == null);
    }
}
```

---

## 輸出

```text
[null, null, null]
true
true
true
```

---

## 詳解

這行：

```java
User[] users = new User[3];
```

很多初學者會誤會成：

```text
建立 3 個 User 物件
```

但其實不是。

真正意思是：

```text
建立一個長度 3 的 User reference 陣列。
```

裡面三格目前都沒有指向任何 User 物件，所以是：

```text
[null, null, null]
```

---

## 如果要真的建立 User 物件

你要自己寫：

```java
users[0] = new User("Amy");
users[1] = new User("Bob");
users[2] = new User("Cindy");
```

完整版本：

```java
import java.util.Arrays;

class User {
    String name;

    User(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

public class Main {
    public static void main(String[] args) {
        User[] users = new User[3];

        users[0] = new User("Amy");
        users[1] = new User("Bob");
        users[2] = new User("Cindy");

        System.out.println(Arrays.toString(users));
    }
}
```

輸出：

```text
[Amy, Bob, Cindy]
```

---

## 為什麼要加 `toString()`？

如果沒有加：

```java
@Override
public String toString() {
    return name;
}
```

你印出 `User` 物件時可能會看到類似：

```text
User@5acf9800
```

因為 Java 不知道你想怎麼顯示 User，所以會用 Object 預設的顯示方式。

加上 `toString()` 之後，Java 才知道：

```text
印 User 時，就印 name。
```

---

## 面試常考點

### Q：`new User[3]` 會呼叫 User constructor 三次嗎？

不會。

這行：

```java
User[] users = new User[3];
```

只建立陣列。

不會建立 `User` 物件。

所以 constructor 不會被呼叫。

只有這樣才會呼叫：

```java
users[0] = new User("Amy");
```

---

# Exercise 6：建立 `int[][]`，用 `Arrays.deepToString()` 印出來

## 題目

```java
// 6. 建立 int[][]，用 Arrays.deepToString() 印出來
```

---

## 完整範例

```java
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6}
        };

        System.out.println(Arrays.deepToString(matrix));
    }
}
```

---

## 輸出

```text
[[1, 2, 3], [4, 5, 6]]
```

---

## 詳解

這行：

```java
int[][] matrix = {
    {1, 2, 3},
    {4, 5, 6}
};
```

建立一個二維陣列。

你可以先把它理解成表格：

| row | values  |
| --: | ------- |
|   0 | 1, 2, 3 |
|   1 | 4, 5, 6 |

也就是：

```text
matrix[0][0] = 1
matrix[0][1] = 2
matrix[0][2] = 3

matrix[1][0] = 4
matrix[1][1] = 5
matrix[1][2] = 6
```

---

## 為什麼用 `deepToString()`？

如果是一維陣列：

```java
int[] nums = {1, 2, 3};
System.out.println(Arrays.toString(nums));
```

可以正常印：

```text
[1, 2, 3]
```

但如果是二維陣列：

```java
int[][] matrix = {
    {1, 2, 3},
    {4, 5, 6}
};

System.out.println(Arrays.toString(matrix));
```

你會看到類似：

```text
[[I@5acf9800, [I@4617c264]
```

因為 `matrix` 的每一格其實又是一個 `int[]`。

所以要用：

```java
Arrays.deepToString(matrix)
```

它會深入每一層，把裡面的陣列也展開。

---

## 二維陣列本質

這個：

```java
int[][] matrix
```

不是很神秘的東西。

你可以理解成：

```text
matrix 是一個陣列
matrix 裡面的每一格又是一個 int[]
```

所以：

```java
matrix[0]
```

是一個：

```java
int[]
```

而：

```java
matrix[0][1]
```

才是一個：

```java
int
```

---

## 面試常考點

### Q：Java 的二維陣列一定是規則矩陣嗎？

不一定。

Java 可以有不規則陣列：

```java
int[][] nums = new int[3][];

nums[0] = new int[]{1, 2};
nums[1] = new int[]{3, 4, 5, 6};
nums[2] = new int[]{7};
```

印出來：

```java
System.out.println(Arrays.deepToString(nums));
```

結果：

```text
[[1, 2], [3, 4, 5, 6], [7]]
```

因為每一列其實是獨立的陣列物件。

---

# 六題總整理

## 你這六題要學會的核心

| 題目               | 核心能力                              |
| ------------------ | ------------------------------------- |
| 1. int[] 總和      | for loop + index + 累加               |
| 2. String[] 印名字 | object array + traversal              |
| 3. 找最大值        | 初始化策略 + 比較邏輯                 |
| 4. 複製陣列        | reference vs real copy                |
| 5. User[]          | object array 預設 null                |
| 6. int[][]         | multidimensional array + deepToString |

---

# 最重要觀念整理

## 1. 陣列 index 從 0 開始

長度 5 的陣列：

```text
合法 index：0, 1, 2, 3, 4
非法 index：5
```

---

## 2. `arr.length` 不是 method

陣列是：

```java
arr.length
```

不是：

```java
arr.length()
```

但 `String` 是：

```java
str.length()
```

這點很常錯。

---

## 3. object array 預設是 null

```java
User[] users = new User[3];
```

結果是：

```text
[null, null, null]
```

不是 3 個 User 物件。

---

## 4. `copy = original` 不是複製內容

```java
int[] copy = original;
```

只是兩個 reference 指向同一個陣列。

真的要複製內容，用：

```java
Arrays.copyOf(original, original.length)
```

---

## 5. 多維陣列要用 `deepToString()`

```java
Arrays.deepToString(matrix)
```

不要用：

```java
Arrays.toString(matrix)
```

---

# 建議你真正手寫的版本

你可以把六題合在一個檔案裡練：

```java
import java.util.Arrays;

class User {
    String name;

    User(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

public class Main {
    public static void main(String[] args) {
        // 1. 總和
        int[] nums = {10, 20, 30, 40, 50};
        int sum = 0;

        for (int i = 0; i < nums.length; i++) {
            sum = sum + nums[i];
        }

        System.out.println("sum = " + sum);

        // 2. 印名字
        String[] names = {"Amy", "Bob", "Cindy"};

        for (int i = 0; i < names.length; i++) {
            System.out.println(names[i]);
        }

        // 3. 找最大值
        int[] values = {12, 45, 7, 99, 23};
        int max = values[0];

        for (int i = 1; i < values.length; i++) {
            if (values[i] > max) {
                max = values[i];
            }
        }

        System.out.println("max = " + max);

        // 4. 複製陣列
        int[] original = {1, 2, 3};
        int[] copy = Arrays.copyOf(original, original.length);

        original[0] = 99;

        System.out.println("original = " + Arrays.toString(original));
        System.out.println("copy = " + Arrays.toString(copy));

        // 5. User[]
        User[] users = new User[3];

        System.out.println(Arrays.toString(users));
        System.out.println(users[0] == null);

        users[0] = new User("Amy");
        users[1] = new User("Bob");
        users[2] = new User("Cindy");

        System.out.println(Arrays.toString(users));

        // 6. int[][]
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6}
        };

        System.out.println(Arrays.deepToString(matrix));
    }
}
```

---

# 這六題的通關標準

你不用背程式碼。

你要能自己說出：

```text
1. 為什麼 for loop 條件是 i < arr.length
2. 為什麼 max 從 nums[0] 開始
3. 為什麼 copy = original 不是複製內容
4. 為什麼 new User[3] 裡面是 null
5. 為什麼二維陣列要用 deepToString()
6. 為什麼 String[] 裡面放的是 reference
```

這六點會了，陣列 baseline 就穩了。
