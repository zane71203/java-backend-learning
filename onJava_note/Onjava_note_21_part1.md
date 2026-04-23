Day 1 — Array Memory Model（Heap allocation model + reference behavior）

本日重點：

陣列不是語法問題，是記憶體模型問題

1. Array 是什麼？

Java 陣列本質：

object（物件）

例如：

int[] arr = new int[5];

實際發生：

Stack
arr ─────► Heap: [0,0,0,0,0]

arr 是：

reference variable（參考變數）

不是資料本身

2. Heap allocation model（核心面試題）

建立陣列時：

int[] arr = new int[3];

發生三件事：

① 在 heap 建立 array object
② 元素初始化為 default value
③ arr 指向該物件

default value：

type default
int 0
double 0.0
boolean false
reference null

例如：

String[] names = new String[3];

實際內容：

[null, null, null]

不是空字串

3. Array 是 reference type

關鍵理解：

int[] a = new int[3];
int[] b = a;

不是 copy

而是：

a ─┐
├──► heap array
b ─┘

所以：

b[0] = 10;

會影響：

a[0] 4. Array parameter passing（超高頻面試題）

Example：

static void modify(int[] arr) {
arr[0] = 99;
}

呼叫：

int[] data = {1,2,3};
modify(data);

結果：

data[0] = 99

原因：

Java 傳的是

reference copy

不是 array copy

5. Array vs primitive 傳遞差異

primitive：

void f(int x)

傳值

array：

void f(int[] x)

傳 reference copy

差異：

type behavior
int pass value
array pass reference copy 6. Array length 是什麼？
arr.length

不是 method

而是：

field

正確：

arr.length

錯誤：

arr.length()
Day 1 面試高頻題

必會：

Q1

下面輸出？

int[] a = {1,2,3};
int[] b = a;

b[0] = 10;

System.out.println(a[0]);

答案：

10

原因：

shared reference
