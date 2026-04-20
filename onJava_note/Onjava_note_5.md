第五章筆記：控制流程（Control Flow）

控制流程（Control Flow）的本質是：

決定「程式下一步要走哪裡」

Java 控制流程分為三大類：

類型 說明
條件判斷 if / switch
迴圈 while / do-while / for / for-each
跳轉控制 return / break / continue / labeled break
一、boolean 與條件判斷基礎

1. Java 的條件判斷只能是 boolean

Java 和某些語言不同：

❌ 不允許

if (a)

✅ 必須

if (a != 0)

原因：

Java 是 強型別語言（Strongly Typed Language）

條件判斷只能接受：

boolean

例如：

System.out.println(1 == 1); // true
System.out.println(1 == 2); // false
面試高頻考點

常見問題：

Java 可以把 int 當 boolean 用嗎？

答案：

不能。

二、if / else 控制流程
基本語法
單分支
if (condition)
statement;
雙分支
if (condition)
statement;
else
statement;
多分支
if (condition1)
...
else if (condition2)
...
else
...

注意：

else if

不是關鍵字，而是：

else + if

的組合。

execution model（執行模型）

流程如下：

判斷 condition
↓
true → 執行 if block
false → 執行 else block
工程實務寫法（推薦）

避免：

if (a > b)
result = 1;
else
result = -1;

推薦：

if (a > b) {
result = 1;
} else {
result = -1;
}

原因：

可讀性 + 防止 bug

return + if 的進階寫法

推薦這種：

int test(int a, int b) {

    if (a > b)
        return 1;

    if (a < b)
        return -1;

    return 0;

}

優點：

減少巢狀層數（nesting depth）

提升可讀性

面試高頻考點

問題：

為什麼這段程式碼沒有 else？

答案：

因為：

return 會直接離開 method
三、while 迴圈
語法
while (condition)
statement;

執行順序：

先判斷 condition
成立 → 執行
不成立 → 離開
execution timeline

例如：

while(condition())
doSomething();

實際流程：

condition()
true → doSomething()
condition()
true → doSomething()
condition()
false → exit
典型用途

適合：

未知次數迴圈

例如：

讀檔案直到 EOF
等待 socket response
等待 queue 有資料
四、do-while 迴圈
語法
do {
statement;
} while(condition);

特性：

至少執行一次

execution timeline
先執行 statement
再判斷 condition

例如：

do {
input = read();
} while(input != -1);
使用時機

當你需要：

先執行一次再判斷

例如：

選單系統
使用者輸入驗證
五、for 迴圈

最常見迴圈

語法：

for(init; condition; step)

例如：

for(int i = 0; i < 10; i++)

執行順序：

init
condition
statement
step
condition
statement
step
...
scope（變數作用域）

重要：

for(int i = 0; i < 10; i++)

這裡：

i 只存在於 for block

例如：

for(int i = 0; i < 10; i++) {}

System.out.println(i); // ❌ error
engineering best practice

推薦：

for (int i = 0; i < list.size(); i++)

但如果只是遍歷：

改用

for-each
六、for-in（for-each）迴圈

語法：

for(Type x : collection)

例如：

for(int x : arr)

等價：

for(int i = 0; i < arr.length; i++)
execution model

流程：

取第一個元素
assign → x
執行 block

取第二個元素
assign → x
執行 block
...
使用限制

不能取得 index

不能修改集合

例如：

for(int x : arr)
x++;

不會改變原陣列

backend 常見使用場景

遍歷：

List
Set
Map.values()
DTO list
Entity list

例如：

for(User user : users)
七、Comma Operator（逗號運算子）

Java 只有一個用途：

for-loop 多變數初始化

例如：

for(int i = 0, j = 10; i < 5; i++, j--)

注意：

型別必須一致

八、return

功能：

1. 回傳值

例如：

return result; 2. 結束方法

例如：

if(error)
return;
void method 特性

例如：

void test() {
}

其實等價：

void test() {
return;
}
面試高頻考點

問題：

非 void method 可以沒有 return 嗎？

答案：

不行

例如：

int test() {

}

compile error

九、break

功能：

跳出迴圈

例如：

for(int i = 0; i < 10; i++) {

    if(i == 5)
        break;

}

結果：

0 1 2 3 4
十、continue

功能：

跳過本次迴圈

例如：

for(int i = 0; i < 5; i++) {

    if(i == 2)
        continue;

    System.out.println(i);

}

結果：

0 1 3 4
十一、無限迴圈

兩種寫法：

while(true)

或

for(;;)

等價

十二、Label（標籤 break / continue）

語法：

label:
for(...)

用途：

跳出多層巢狀迴圈

例如：

outer:
for(int i=0;i<5;i++) {

    for(int j=0;j<5;j++) {

        break outer;
    }

}

效果：

直接跳出兩層 loop

何時使用？

只有一種情況：

多層巢狀迴圈 early exit

例如：

搜尋矩陣

find:
for(...) {
for(...) {

        if(found)
            break find;
    }

}
engineering 建議

實務中：

極少使用

通常改寫為：

return

更乾淨

十三、switch

語法：

switch(selector) {

    case value:
        statement;
        break;

    default:
        statement;

}
execution model

流程：

selector 計算值
比對 case
命中 → 執行
fall-through 機制（重要）

例如：

case 1:
case 2:
case 3:

表示：

1 OR 2 OR 3
break 的作用

沒有 break：

會繼續執行下一個 case

例如：

case 1:
print("A");

case 2:
print("B");

輸入：

1

輸出：

A
B
switch 支援型別

可用：

int
char
enum
String(Java7+)

不可用：

float
double
boolean
switch(String)

例如：

switch(color)

比：

if-else chain

更乾淨

十四、Math.random()

回傳範圍：

[0.0 , 1.0)

包含：

0.0

不包含：

1.0
十五、本章核心總結（面試版）

最重要觀念：

1

Java 條件判斷只能使用 boolean

2

for-each 無法修改集合內容

3

break 離開 loop

continue 跳過本次 loop

4

return 離開 method

5

switch 需要 break 防止 fall-through

6

labeled break 用於多層巢狀 loop

Control Flow 檢核題（Level：Phase 0 → Phase 1 準備線）
Q1

下列哪一個可以作為 Java 的 if 條件？

A

if(1)

B

if(a = 1)

C

if(a == 1)

D

if("true")
Q2

程式輸出結果為何？

int a = 5;

if(a > 3)
if(a > 10)
System.out.println("A");
else
System.out.println("B");

A

A

B

B

C

無輸出

D

compile error

Q3

下列程式輸出？

for(int i = 0; i < 3; i++)
System.out.print(i);

A

012

B

123

C

0123

D

compile error

Q4

下列哪個敘述正確？

A
while 迴圈一定至少執行一次

B
do-while 迴圈一定至少執行一次

C
for 迴圈一定至少執行一次

D
三者都至少執行一次

Q5

輸出結果？

for(int i = 0; i < 5; i++) {

    if(i == 2)
        continue;

    System.out.print(i);

}

A

01234

B

0134

C

1234

D

034
Q6

輸出結果？

for(int i = 0; i < 5; i++) {

    if(i == 2)
        break;

    System.out.print(i);

}

A

01234

B

01

C

12

D

034
Q7

以下程式輸出？

int i = 0;

while(i < 3) {
System.out.print(i);
i++;
}

A

012

B

123

C

0123

D

無輸出

Q8

for-each 的限制是？

A
不能讀取元素

B
不能修改元素

C
不能遍歷陣列

D
不能遍歷集合

Q9

輸出結果？

switch(2) {

    case 1:
        System.out.print("A");

    case 2:
        System.out.print("B");

    case 3:
        System.out.print("C");

}

A

A

B

B

C

BC

D

ABC
Q10

下列哪個敘述正確？

A
break 可以離開 method

B
continue 可以離開 method

C
return 可以離開 method

D
switch 不需要 break

請直接作答，我會逐題解析（含 execution flow 與面試觀點）。

C/A/A/B/B/B/A/B/D/B

你的答案：

1.C
2.A
3.A
4.B
5.B
6.B
7.A
8.B
9.D
10.B

正確率：7 / 10

錯題：Q2、Q9、Q10

下面逐題完整解析（含 execution flow + 面試觀點）。

Q1

題目：

哪個可以作為 if 條件？

正確答案：C ✅

if(a == 1)

原因：

Java 條件必須是：

boolean

例如：

a == 1 → boolean

錯誤選項：

if(1) ❌
if("true") ❌

面試常問：

Java 是否允許 int 當 boolean？

答案：不允許。

Q2 ❌（錯）

你的答案：A
正確答案：B

題目：

int a = 5;

if(a > 3)
if(a > 10)
System.out.println("A");
else
System.out.println("B");

輸出？

答案：

B

原因：

Java 的 else 會匹配最近的 if

實際解析：

if(a > 3)
if(a > 10)
A
else
B

等價：

if(a > 3) {
if(a > 10)
A;
else
B;
}

因為：

5 > 3 true
5 > 10 false

所以輸出：

B

面試高頻題：

這叫：

dangling else problem
Q3

正確答案：A ✅

for(int i = 0; i < 3; i++)

流程：

i=0 → print
i=1 → print
i=2 → print
i=3 stop

輸出：

012
Q4

正確答案：B ✅

題目：

哪個至少執行一次？

答案：

do-while

因為：

先執行
再判斷
Q5

正確答案：B ✅

題目：

if(i == 2)
continue;

continue：

跳過本次 iteration

流程：

0 print
1 print
2 skip
3 print
4 print

輸出：

0134
Q6

正確答案：B ✅

題目：

if(i == 2)
break;

break：

直接離開 loop

流程：

0 print
1 print
2 break

輸出：

01
Q7

正確答案：A ✅

題目：

while(i < 3)

流程：

0 print
1 print
2 print
3 stop

輸出：

012
Q8

正確答案：B ✅

for-each 限制：

不能修改元素

例如：

for(int x : arr)
x++;

不會改變原陣列內容

因為：

x 是 copy
不是 reference

面試常問：

for-each 能不能修改集合？

答案：

不能。

Q9 ❌（錯）

你的答案：D
正確答案：C

題目：

switch(2) {

    case 1:
        System.out.print("A");

    case 2:
        System.out.print("B");

    case 3:
        System.out.print("C");

}

沒有 break

發生：

fall-through

流程：

match case 2
print B
continue
print C

輸出：

BC

不是：

ABC

因為：

case 1 沒被命中

Q10 ❌（錯）

你的答案：B
正確答案：C

題目：

哪個可以離開 method？

答案：

return

說明：

keyword 功能
break 離開 loop
continue 跳下一輪 loop
return 離開 method

例如：

void test() {

    return;

}

直接結束 method
