# 第十五章 例外（Exceptions / 異常）— 臺灣繁中整理版

> 本版以原文〈第十五章 異常〉為基礎，在不大幅改動章節脈絡的前提下，改成較符合臺灣工程師閱讀習慣的說法。  
> 術語格式：**臺灣用詞（English / 簡中）**。

---

## 0. 本章核心一句話

Java 的基本理念之一是：**結構不佳、錯誤沒有被妥善處理的程式，不應該假裝正常執行。**

**例外處理（Exception Handling / 异常处理）** 是 Java 官方且一致的錯誤回報機制。它的目的不是讓程式「永遠不出錯」，而是讓錯誤在發生時可以被清楚回報、適當傳遞，並在正確的層級被處理。

---

## 1. 重要術語對照表

| 臺灣常用詞              | English                                | 簡中                    | 說明                                           |
| ----------------------- | -------------------------------------- | ----------------------- | ---------------------------------------------- |
| 例外                    | Exception                              | 异常                    | 程式在正常流程中無法繼續處理的狀況。           |
| 例外處理                | Exception Handling                     | 异常处理                | 使用 `try / catch / finally` 等機制處理錯誤。  |
| 拋出例外                | throw an exception                     | 抛出异常                | 用 `throw` 把錯誤交給外層處理。                |
| 宣告例外                | declare exception                      | 声明异常                | 用 `throws` 告知呼叫者此方法可能丟出哪些例外。 |
| 受檢例外                | Checked Exception                      | 被检查的异常            | 編譯器強制要求處理或宣告的例外。               |
| 非受檢例外 / 執行期例外 | Unchecked Exception / RuntimeException | 不检查异常 / 运行时异常 | 編譯器不強制處理，通常代表程式邏輯錯誤。       |
| 呼叫堆疊                | Call Stack                             | 调用栈                  | 方法呼叫的層級紀錄。                           |
| 堆疊追蹤                | Stack Trace                            | 栈轨迹                  | 例外發生時的呼叫路徑。                         |
| 例外鏈                  | Exception Chaining                     | 异常链                  | 用 `cause` 保存原始例外。                      |
| 資源自動關閉            | try-with-resources                     | try-with-resources      | Java 7 後用來自動關閉資源的語法。              |
| 清理                    | Cleanup                                | 清理                    | 關閉檔案、連線、釋放非記憶體資源。             |

---

## 2. 例外的基本概念

早期語言常用「特殊回傳值」或「錯誤旗標」來表示錯誤，例如：

```java
int result = doSomething();
if (result == -1) {
    // handle error
}
```

這種方式的問題是：

1. 呼叫者可能忘記檢查錯誤。
2. 正常邏輯與錯誤邏輯混在一起，程式碼變難讀。
3. 大型系統中，錯誤可能被一路忽略，最後很難追。

Java 使用例外來統一錯誤回報：

```java
throw new IllegalArgumentException("age must be positive");
```

當例外被拋出後，Java 會中止目前方法的正常執行路徑，沿著呼叫堆疊往外找可以處理它的 `catch` 區塊。

---

## 3. 什麼情況適合丟例外？

**例外情境（Exceptional Condition / 异常情况）** 指的是：目前方法已經沒有足夠資訊繼續處理，必須把問題交給外層。

例如除法：

```java
public int divide(int a, int b) {
    if (b == 0) {
        throw new IllegalArgumentException("divisor cannot be zero");
    }
    return a / b;
}
```

如果你在目前方法中知道怎麼處理，就在目前方法處理。  
如果不知道，就不要假裝知道，應該拋出例外或讓例外往外傳。

---

## 4. throw：拋出例外

`throw` 用來丟出一個例外物件。

```java
if (user == null) {
    throw new NullPointerException("user is null");
}
```

實務上，`NullPointerException` 多數情況不需要手動丟，因為對 `null` 呼叫方法時 Java 會自動丟出。不過你可以使用更清楚的例外，例如：

```java
if (user == null) {
    throw new IllegalArgumentException("user must not be null");
}
```

這樣語意更清楚：問題是「傳入參數不合法」，不是單純發生 NPE。

---

## 5. try / catch：捕捉例外

### 5.1 try 區塊

`try` 放可能發生例外的程式碼：

```java
try {
    int result = divide(10, 0);
    System.out.println(result);
}
```

### 5.2 catch 區塊

`catch` 放例外處理邏輯：

```java
try {
    int result = divide(10, 0);
    System.out.println(result);
} catch (IllegalArgumentException e) {
    System.out.println("輸入錯誤：" + e.getMessage());
}
```

`catch` 會依照**由上到下**的順序比對，找到第一個符合型別的處理器後就執行。

因此，越具體的例外要放越前面，越籠統的例外要放越後面：

```java
try {
    // risky code
} catch (NumberFormatException e) {
    // 先處理具體例外
} catch (Exception e) {
    // 最後才處理通用例外
}
```

如果把 `Exception` 放最前面，後面的具體例外永遠不會被執行，編譯器會報錯。

---

## 6. 終止模型與恢復模型

例外處理有兩種理論模型：

### 6.1 終止模型（Termination Model / 终止模型）

Java 採用這個模型。

意思是：例外發生後，不會回到原本出錯的位置繼續執行。程式會跳到對應的 `catch`，處理完後從 `try-catch` 後面繼續。

### 6.2 恢復模型（Resumption Model / 恢复模型）

意思是：例外處理器修好問題後，再回到原本出錯的位置重試。

Java 沒有直接支援這種模型。若要重試，通常用迴圈包住 `try`：

```java
while (true) {
    try {
        connect();
        break;
    } catch (ConnectionException e) {
        retryLater();
    }
}
```

實務上，Java 後端常見的是終止模型：錯誤發生後，回傳錯誤、記錄 log、或交給上層處理。

---

## 7. 自訂例外

Java 內建很多例外，但不可能涵蓋你所有業務場景。因此可以定義自己的例外。

```java
class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
```

使用：

```java
public void withdraw(int amount) throws InsufficientBalanceException {
    if (amount > balance) {
        throw new InsufficientBalanceException("餘額不足");
    }
    balance -= amount;
}
```

### 7.1 例外類別名稱很重要

例外最重要的資訊通常不是欄位，而是類別名稱本身。

例如：

```java
UserNotFoundException
InvalidPasswordException
OrderAlreadyPaidException
```

這些名稱本身就能清楚表達問題。

---

## 8. printStackTrace 與堆疊追蹤

所有例外都繼承自 `Throwable`，常用方法如下：

| 方法                | 用途                 |
| ------------------- | -------------------- |
| `getMessage()`      | 取得例外訊息。       |
| `toString()`        | 顯示例外型別與訊息。 |
| `printStackTrace()` | 印出堆疊追蹤。       |
| `getStackTrace()`   | 取得堆疊資訊陣列。   |
| `getCause()`        | 取得原始原因例外。   |

範例：

```java
try {
    throw new Exception("Something wrong");
} catch (Exception e) {
    System.out.println(e.getMessage());
    e.printStackTrace();
}
```

在正式後端專案中，通常不要直接到處 `printStackTrace()`，而是交給 logging framework，例如 SLF4J + Logback。

---

## 9. 例外與日誌

原文使用 `java.util.logging` 示範。它仍可用，但現代 Spring Boot 專案更常見的是：

- **SLF4J（Simple Logging Facade for Java / Java 簡易日誌門面）**
- **Logback（Logback / Logback）**
- Spring Boot 預設整合 logging

實務寫法通常像這樣：

```java
private static final Logger log = LoggerFactory.getLogger(UserService.class);

try {
    userRepository.findById(id);
} catch (Exception e) {
    log.error("Failed to find user. id={}", id, e);
    throw e;
}
```

重點：

1. log 要包含足夠的上下文，例如 `userId`、`orderId`。
2. 不要只寫 `error happened`。
3. 不要吞掉例外。

---

## 10. throws：宣告方法可能丟出的例外

`throws` 放在方法宣告上，告訴呼叫者：這個方法可能丟出某些例外。

```java
public void readFile(String path) throws IOException {
    Files.readString(Path.of(path));
}
```

如果方法內產生的是**受檢例外（Checked Exception / 被检查的异常）**，你必須二選一：

1. 在方法內 `try-catch` 處理。
2. 在方法宣告上用 `throws` 往外丟。

```java
public void process() {
    try {
        readFile("data.txt");
    } catch (IOException e) {
        System.out.println("檔案讀取失敗");
    }
}
```

或：

```java
public void process() throws IOException {
    readFile("data.txt");
}
```

---

## 11. Checked Exception vs RuntimeException

### 11.1 Checked Exception（受檢例外 / 被检查的异常）

編譯器會強迫你處理或宣告。

常見例子：

```java
IOException
SQLException
FileNotFoundException
```

通常代表：外部環境可能失敗，例如檔案不存在、網路中斷、資料庫錯誤。

### 11.2 RuntimeException（執行期例外 / 运行时异常）

編譯器不強迫你處理。

常見例子：

```java
NullPointerException
IllegalArgumentException
IndexOutOfBoundsException
IllegalStateException
```

通常代表：程式邏輯錯誤或使用方式錯誤。

### 11.3 後端實務判斷

在 Spring Boot 專案中，業務例外常做成 `RuntimeException` 的子類，因為：

1. 不想讓每一層方法都被 `throws` 汙染。
2. 可以交給全域例外處理器統一轉成 HTTP response。

```java
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User not found: " + id);
    }
}
```

---

## 12. 捕捉所有例外：catch(Exception e)

你可以用：

```java
catch (Exception e) {
    // handle
}
```

它會捕捉多數程式相關例外。  
但要注意：

1. 不要太早捕捉 `Exception`。
2. 不要空的 `catch`。
3. 不知道怎麼處理時，通常應該記錄後重新拋出。

錯誤示範：

```java
try {
    doSomething();
} catch (Exception e) {
    // 什麼都不做，例外被吞掉
}
```

較好的寫法：

```java
try {
    doSomething();
} catch (Exception e) {
    log.error("doSomething failed", e);
    throw e;
}
```

---

## 13. 多重捕捉（Multi-Catch / 多重捕获）

Java 7 後可以用 `|` 一次捕捉多種例外：

```java
try {
    parseAndSave(input);
} catch (NumberFormatException | DateTimeParseException e) {
    throw new InvalidInputException("格式錯誤", e);
}
```

適合用在：不同例外需要相同處理邏輯時。

---

## 14. 重新拋出例外

有時候你會先在目前層級做一點事，再把例外交給外層：

```java
try {
    paymentService.pay(orderId);
} catch (PaymentException e) {
    log.warn("Payment failed. orderId={}", orderId, e);
    throw e;
}
```

這叫做**重新拋出（Rethrow / 重新抛出）**。

如果你改丟新的例外，建議保留原始例外：

```java
try {
    paymentService.pay(orderId);
} catch (PaymentException e) {
    throw new OrderPaymentFailedException("訂單付款失敗", e);
}
```

這樣外層仍可透過 `getCause()` 找到原始原因。

---

## 15. 例外鏈（Exception Chaining / 异常链）

例外鏈的目的：**換成更高層語意的例外時，不要遺失底層原因。**

例如資料庫發生錯誤，但你不想把 `SQLException` 直接暴露到業務層：

```java
try {
    repository.save(order);
} catch (SQLException e) {
    throw new OrderSaveException("訂單儲存失敗", e);
}
```

此時：

- 外層看到的是 `OrderSaveException`
- 原始原因仍保存在 `cause` 裡

這在 debug production 問題時非常重要。

---

## 16. Java 標準例外結構

Java 中可以被丟出的根類別是 `Throwable`。

```text
Throwable
├── Error
└── Exception
    ├── RuntimeException
    └── 其他 Checked Exception
```

### 16.1 Error

`Error` 通常代表 JVM 或系統層級嚴重問題，例如：

```java
OutOfMemoryError
StackOverflowError
```

一般應用程式不應該嘗試捕捉並恢復。

### 16.2 Exception

一般 Java 程式主要處理的是 `Exception` 及其子類。

### 16.3 RuntimeException

代表編譯器不強制處理的例外。常見用於參數錯誤、狀態錯誤、業務錯誤。

---

## 17. finally：一定會執行的清理區塊

`finally` 放在 `try-catch` 後面，無論是否發生例外，通常都會執行。

```java
try {
    openConnection();
    doWork();
} catch (Exception e) {
    handle(e);
} finally {
    closeConnection();
}
```

用途：

- 關閉檔案
- 關閉資料庫連線
- 關閉網路連線
- 釋放外部資源

注意：Java 的垃圾回收只負責記憶體，不會自動幫你關檔案或網路連線。

---

## 18. finally 與 return

即使 `try` 裡面有 `return`，`finally` 仍然會執行。

```java
public int test() {
    try {
        return 1;
    } finally {
        System.out.println("cleanup");
    }
}
```

輸出：

```text
cleanup
```

回傳值是 `1`。

### 實務警告

不要在 `finally` 裡面寫 `return`，因為它可能蓋掉原本的例外或回傳值。

錯誤示範：

```java
try {
    throw new RuntimeException("failed");
} finally {
    return; // 會把例外吃掉，非常危險
}
```

---

## 19. 例外遺失

如果 `try` 裡丟出例外，而 `finally` 裡又丟出另一個例外，原本的例外可能被蓋掉。

```java
try {
    throw new RuntimeException("important error");
} finally {
    throw new RuntimeException("cleanup error");
}
```

最後看到的可能只剩：

```text
cleanup error
```

所以：

1. `finally` 裡的清理動作也要小心處理例外。
2. 優先使用 `try-with-resources`。
3. 不要在 `finally` 裡 `return`。

---

## 20. 繼承與例外限制

覆寫方法時，子類別方法不能比父類別方法丟出更寬的受檢例外。

```java
class Parent {
    void run() throws IOException {}
}

class Child extends Parent {
    @Override
    void run() throws FileNotFoundException {} // OK，較具體
}
```

但下面不行：

```java
class Child extends Parent {
    @Override
    void run() throws Exception {} // 不行，Exception 比 IOException 更寬
}
```

原因是：多型必須維持可替換性。  
如果原本使用 `Parent` 的程式只準備處理 `IOException`，子類別卻突然丟出更大的 `Exception`，呼叫端就會被破壞。

---

## 21. 建構子與清理問題

建構子（Constructor / 构造函数）若開啟外部資源，必須特別小心。

例如：

```java
public class InputFile {
    private BufferedReader reader;

    public InputFile(String path) throws IOException {
        reader = new BufferedReader(new FileReader(path));
    }

    public String readLine() throws IOException {
        return reader.readLine();
    }

    public void close() throws IOException {
        reader.close();
    }
}
```

問題：如果建構中途失敗，物件可能沒有完整建立，清理邏輯就很麻煩。

因此現代 Java 更推薦：

1. 使用 `try-with-resources`
2. 讓資源生命週期集中在較小範圍
3. 避免建構子做太複雜、容易失敗的事

---

## 22. try-with-resources

Java 7 後，管理外部資源最推薦的方式是 `try-with-resources`。

```java
try (BufferedReader reader = Files.newBufferedReader(Path.of("data.txt"))) {
    String line = reader.readLine();
    System.out.println(line);
} catch (IOException e) {
    System.out.println("讀檔失敗：" + e.getMessage());
}
```

只要資源實作 `AutoCloseable`，離開 `try` 區塊時 Java 會自動呼叫 `close()`。

### 22.1 多個資源

```java
try (
    BufferedReader reader = Files.newBufferedReader(inputPath);
    PrintWriter writer = new PrintWriter(outputFile)
) {
    writer.println(reader.readLine());
}
```

關閉順序會和建立順序相反：

```text
先建立 reader，再建立 writer
離開時先關 writer，再關 reader
```

這符合常見依賴關係：後建立的資源可能依賴先建立的資源。

---

## 23. AutoCloseable

`try-with-resources` 的核心是 `AutoCloseable`：

```java
class Resource implements AutoCloseable {
    @Override
    public void close() {
        System.out.println("closed");
    }
}
```

使用：

```java
try (Resource resource = new Resource()) {
    System.out.println("using resource");
}
```

輸出：

```text
using resource
closed
```

如果資源沒有實作 `AutoCloseable`，就不能放進 `try (...)` 裡。

---

## 24. 例外匹配規則

例外匹配會接受子類別匹配父類別。

```java
class Annoyance extends Exception {}
class Sneeze extends Annoyance {}
```

```java
try {
    throw new Sneeze();
} catch (Annoyance e) {
    System.out.println("Caught Annoyance");
}
```

`Sneeze` 是 `Annoyance` 的子類，所以可以被 `catch (Annoyance e)` 捕捉。

但如果你同時要捕捉子類與父類，子類要放前面：

```java
try {
    throw new Sneeze();
} catch (Sneeze e) {
    System.out.println("Caught Sneeze");
} catch (Annoyance e) {
    System.out.println("Caught Annoyance");
}
```

---

## 25. 對 Checked Exception 的現代看法

原文後半討論了 Checked Exception 的爭議。保留重點即可：

### 優點

- 編譯器強迫呼叫者知道可能失敗。
- 對檔案、I/O、資料庫這類外部風險有提醒作用。

### 缺點

- 大型專案中容易讓 `throws` 到處擴散。
- 很多人不知道怎麼處理，只好空 `catch` 或亂包。
- 反而造成例外被吞掉。

### 實務建議

1. 知道怎麼處理才 `catch`。
2. 不知道怎麼處理，不要空 `catch`。
3. 可以在邊界層轉成業務語意的 RuntimeException。
4. 保留 cause，避免 debug 時失去原始線索。

---

## 26. 把 Checked Exception 包成 RuntimeException

有時候你不想讓底層 checked exception 汙染整個方法鏈，可以包成 RuntimeException。

```java
try {
    Files.readString(Path.of("data.txt"));
} catch (IOException e) {
    throw new UncheckedIOException(e);
}
```

或自訂：

```java
try {
    externalClient.call();
} catch (IOException e) {
    throw new ExternalServiceException("外部服務呼叫失敗", e);
}
```

這不是「逃避錯誤」，前提是你有保留 `cause`，並且讓上層有統一處理策略。

---

## 27. Spring Boot 後端中的例外處理位置

在 Spring Boot 專案中，常見做法是：

```text
Controller
→ Service
→ Repository
```

例外策略通常是：

1. Repository 發生資料錯誤。
2. Service 轉成業務語意例外。
3. Global Exception Handler 統一轉 HTTP Response。

簡化示意：

```java
@Service
public class UserService {
    public UserDto getUser(Long id) {
        return userRepository.findById(id)
            .map(UserDto::from)
            .orElseThrow(() -> new UserNotFoundException(id));
    }
}
```

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse("USER_NOT_FOUND", e.getMessage()));
    }
}
```

這就是本章在 backend 實務上的最重要延伸。

---

## 28. 本章實務規則整理

### 28.1 該做

- 用例外表示真正無法在目前層級繼續的錯誤。
- 在知道怎麼處理時才 `catch`。
- 保留原始 cause。
- 外部資源優先用 `try-with-resources`。
- 用清楚的自訂例外名稱表達業務錯誤。
- 在後端專案中建立全域例外處理器。

### 28.2 不該做

- 空的 `catch`。
- 到處 `catch (Exception e)` 然後吞掉。
- 在 `finally` 裡 `return`。
- 用例外控制正常流程。
- 把底層技術例外直接暴露給 API 使用者。

---

## 29. 面試高頻重點

1. `throw` 和 `throws` 差在哪？
2. Checked Exception 和 RuntimeException 差在哪？
3. 為什麼 `catch` 要把子類別放在父類別前面？
4. `finally` 一定會執行嗎？什麼情況可能不執行？
5. `try-with-resources` 解決什麼問題？
6. 什麼是 exception chaining？
7. 為什麼不要空 `catch`？
8. Spring Boot 如何做全域例外處理？
9. 自訂例外應該繼承 `Exception` 還是 `RuntimeException`？
10. `Error` 和 `Exception` 差在哪？

---

## 30. 本章小結

例外是 Java 後端工程不可跳過的核心能力。它的重點不是語法，而是錯誤責任的分層：

```text
發生錯誤的地方
→ 能處理的地方
→ 能對使用者回應的地方
```

在純 Java 中，你要理解 `try / catch / finally / throw / throws / RuntimeException / checked exception / try-with-resources`。  
在 Spring Boot 中，你要進一步理解：

```text
Service 丟業務例外
Global Exception Handler 統一處理
API 回傳穩定錯誤格式
log 保留 debug 線索
```

這樣才算真的把 Java 例外處理學成 backend 可用能力。

# 重新批改總覽

你的答案拆開後是：

```text
Q1  C
Q2  C
Q3  C
Q4  C
Q5  C
Q6  A
Q7  B
Q8  D
Q9  B
Q10 C

Q11 T
Q12 T
Q13 T
Q14 T
Q15 F
```

## 修正後成績

| 題型    |    得分 |
| ------- | ------: |
| Q1~Q10  |  8 / 10 |
| Q11~Q15 |   5 / 5 |
| Q16~Q18 |   1 / 3 |
| Q19~Q20 |   2 / 2 |
| 總分    | 16 / 20 |

真正錯的是：

```text
Q5、Q6、Q16、Q18
```

---

# Q1

## 完整問題

下列哪個關於 Exception 的敘述正確？

A. 所有 Exception 都必須使用 try-catch
B. RuntimeException 屬於 Checked Exception
C. Exception 是 Throwable 的子類別
D. Error 是 Exception 的子類別

## 你的回答

C

## 正解

C ✅

## 完整詳解

Java 例外繼承結構：

```text
Throwable
├── Exception
│   └── RuntimeException
└── Error
```

所以 `Exception` 確實是 `Throwable` 的子類別。

A 錯，因為 `RuntimeException` 不強制 try-catch。
B 錯，`RuntimeException` 是 unchecked exception。
D 錯，`Error` 不是 `Exception` 子類別，而是 `Throwable` 的另一個分支。

## 本題觀念

`Throwable / Exception / RuntimeException / Error` 的繼承關係。

## 面試高頻

★★★★★

---

# Q2

## 完整問題

以下哪個屬於 Checked Exception？

A. NullPointerException
B. ArrayIndexOutOfBoundsException
C. IOException
D. ArithmeticException

## 你的回答

C

## 正解

C ✅

## 完整詳解

`IOException` 是 checked exception，編譯器會強制你處理：

```java
void readFile() throws IOException
```

或：

```java
try {
    // read file
} catch (IOException e) {
    // handle
}
```

其他三個都是 `RuntimeException` 的子類別。

## 本題觀念

Checked exception 會被編譯器強制處理；unchecked exception 不會。

## 面試高頻

★★★★★

---

# Q3

## 完整問題

```java
public static void main(String[] args) {
    int x = 10 / 0;
    System.out.println("End");
}
```

執行結果？

A. 輸出 End
B. 編譯失敗
C. 拋出 ArithmeticException
D. 拋出 IOException

## 你的回答

C

## 正解

C ✅

## 完整詳解

整數除以 0 會在執行時拋出：

```java
ArithmeticException
```

所以：

```java
System.out.println("End");
```

不會執行。

## 本題觀念

`ArithmeticException` 是 runtime exception。

## 面試高頻

★★★★☆

---

# Q4

## 完整問題

關於 throw 與 throws，下列何者正確？

A. throw 用來宣告方法可能拋出的例外
B. throws 用來實際拋出例外物件
C. throw 用來拋出例外物件
D. 兩者功能完全相同

## 你的回答

C

## 正解

C ✅

## 完整詳解

`throw` 是實際拋出例外物件：

```java
throw new RuntimeException("error");
```

`throws` 是寫在方法宣告上，表示這個方法可能拋出某些例外：

```java
void readFile() throws IOException
```

## 本題觀念

`throw` 是動作；`throws` 是宣告。

## 面試高頻

★★★★★

---

# Q5

## 完整問題

```java
public static void test() {
    throw new RuntimeException("Error");
}
```

是否能編譯？

A. 不能
B. 可以
C. 必須加 throws RuntimeException
D. 必須 try-catch

## 你的回答

C

## 正解

B ❌

## 完整詳解

`RuntimeException` 是 unchecked exception。

所以這樣可以編譯：

```java
public static void test() {
    throw new RuntimeException("Error");
}
```

不需要：

```java
throws RuntimeException
```

也不需要：

```java
try-catch
```

當然，你可以加 `throws RuntimeException`，但那不是「必須」。

## 本題觀念

`RuntimeException` 不會被編譯器強制處理。

## 面試高頻

★★★★★

---

# Q6

## 完整問題

下列哪個 catch 會先被執行？

```java
try {
    ...
}
catch (Exception e) {
    ...
}
catch (IOException e) {
    ...
}
```

A. Exception
B. IOException
C. 都會
D. 編譯失敗

## 你的回答

A

## 正解

D ❌

## 完整詳解

這段程式會編譯失敗。

原因是：

```java
catch (Exception e)
```

已經可以捕捉所有 `Exception` 子類別，包含：

```java
IOException
```

所以後面的：

```java
catch (IOException e)
```

永遠到不了。

正確順序應該是：

```java
catch (IOException e) {
    ...
} catch (Exception e) {
    ...
}
```

子類別要放前面，父類別放後面。

## 本題觀念

多個 `catch` 時，必須從「具體」到「抽象」。

## 面試高頻

★★★★★

---

# Q7

## 完整問題

try-with-resources 主要用途是？

A. 提高執行速度
B. 自動關閉資源
C. 避免 RuntimeException
D. 避免 JVM GC

## 你的回答

B

## 正解

B ✅

## 完整詳解

try-with-resources 會在離開 try 區塊時自動呼叫資源的 `close()`。

例如：

```java
try (FileInputStream fis = new FileInputStream("a.txt")) {
    int data = fis.read();
}
```

離開 try 後，`fis.close()` 會自動執行。

## 本題觀念

try-with-resources 用來管理需要關閉的資源，例如檔案、連線、stream。

## 面試高頻

★★★★☆

---

# Q8

## 完整問題

哪些類別最常搭配 try-with-resources？

A. Scanner
B. FileInputStream
C. BufferedReader
D. 以上皆是

## 你的回答

D

## 正解

D ✅

## 完整詳解

這些都可以搭配 try-with-resources，因為它們都屬於需要關閉的資源類型，且支援 `AutoCloseable` / `Closeable`。

例如：

```java
try (BufferedReader br = new BufferedReader(new FileReader("a.txt"))) {
    System.out.println(br.readLine());
}
```

## 本題觀念

只要物件實作 `AutoCloseable`，就能放進 try-with-resources。

## 面試高頻

★★★☆☆

---

# Q9

## 完整問題

關於 finally，下列何者正確？

A. 只有發生 Exception 才執行
B. 一定會執行（一般情況）
C. 只能寫在 catch 前面
D. 只能有一個 try 沒有 catch

## 你的回答

B

## 正解

B ✅

## 完整詳解

一般情況下，`finally` 一定會執行，不管 try 裡面有沒有發生例外。

```java
try {
    System.out.println("A");
} finally {
    System.out.println("B");
}
```

輸出：

```text
A
B
```

但「一般情況」要注意：如果 JVM 直接被關掉，例如 `System.exit(0)`，finally 可能不會執行。

## 本題觀念

`finally` 用來放一定要執行的清理動作。

## 面試高頻

★★★★☆

---

# Q10

## 完整問題

Backend 開發中，下列哪種做法較合理？

A. 所有 Exception 都吃掉不處理
B. 所有 Exception 都 printStackTrace()
C. 建立自訂 Exception 表達業務錯誤
D. 全部直接 throws Exception

## 你的回答

C

## 正解

C ✅

## 完整詳解

Backend 專案通常會定義明確的業務例外，例如：

```java
BusinessException
NotFoundException
UnauthorizedException
```

這樣比單純丟：

```java
throw new RuntimeException("error");
```

更清楚。

在 Spring Boot 裡，也方便搭配：

```java
@RestControllerAdvice
```

做全域錯誤處理，把不同例外轉成不同 HTTP response。

## 本題觀念

例外不只是錯誤訊息，也是業務語意。

## 面試高頻

★★★★★

---

# Q11

## 完整問題

RuntimeException 屬於 Exception 家族。

( T / F )

## 你的回答

T

## 正解

T ✅

## 完整詳解

繼承關係：

```text
Exception
└── RuntimeException
```

所以 `RuntimeException` 是 `Exception` 的子類別。

## 本題觀念

Exception 繼承樹。

## 面試高頻

★★★★☆

---

# Q12

## 完整問題

IOException 屬於 Checked Exception。

( T / F )

## 你的回答

T

## 正解

T ✅

## 完整詳解

`IOException` 是 checked exception。
只要方法可能拋出它，編譯器就會要求：

```java
try-catch
```

或：

```java
throws IOException
```

## 本題觀念

常見 checked exception：`IOException`、`SQLException`、`ClassNotFoundException`。

## 面試高頻

★★★★★

---

# Q13

## 完整問題

finally 可以沒有 catch。

( T / F )

## 你的回答

T

## 正解

T ✅

## 完整詳解

可以這樣寫：

```java
try {
    System.out.println("A");
} finally {
    System.out.println("B");
}
```

`try-finally` 合法。

不一定要有 `catch`。

## 本題觀念

合法結構：

```text
try-catch
try-finally
try-catch-finally
```

## 面試高頻

★★★★☆

---

# Q14

## 完整問題

可以自訂 Exception 類別。

( T / F )

## 你的回答

T

## 正解

T ✅

## 完整詳解

可以自訂 checked exception：

```java
class MyException extends Exception {
}
```

也可以自訂 unchecked exception：

```java
class BusinessException extends RuntimeException {
}
```

Backend 專案比較常見的是自訂 `RuntimeException` 子類別。

## 本題觀念

自訂例外可以用來表達更精準的錯誤語意。

## 面試高頻

★★★★☆

---

# Q15

## 完整問題

Java 只能拋出系統內建 Exception。

( T / F )

## 你的回答

F

## 正解

F ✅

## 完整詳解

Java 可以拋出自訂例外。

例如：

```java
throw new UserNotFoundException();
```

只要你的類別繼承自 `Throwable` 相關類型即可。實務上通常繼承：

```java
Exception
```

或：

```java
RuntimeException
```

## 本題觀念

Java 例外系統可擴充。

## 面試高頻

★★★☆☆

---

# Q16

## 完整問題

```java
public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("A");
            throw new RuntimeException();
        } catch (Exception e) {
            System.out.println("B");
        }
        System.out.println("C");
    }
}
```

輸出？

## 你的回答

```text
A
C
```

## 正解

```text
A
B
C
```

❌

## 完整詳解

執行流程：

```text
1. 進入 try
2. 印出 A
3. throw new RuntimeException()
4. RuntimeException 被 catch(Exception e) 捕捉
5. 印出 B
6. catch 結束後，程式繼續往下
7. 印出 C
```

重點是：

```text
例外被 catch 處理掉之後，程式會繼續執行 catch 後面的程式碼。
```

除非你在 catch 裡面再次 throw，或 return，或程式終止。

## 本題觀念

`catch` 不是「程式結束」，而是「例外處理點」。

## 面試高頻

★★★★★

---

# Q17

## 完整問題

```java
public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("A");
        } finally {
            System.out.println("B");
        }
    }
}
```

輸出？

## 你的回答

```text
A
B
```

## 正解

```text
A
B
```

✅

## 完整詳解

try 正常執行：

```text
A
```

離開 try 前會執行 finally：

```text
B
```

所以輸出：

```text
A
B
```

## 本題觀念

`finally` 不需要發生例外也會執行。

## 面試高頻

★★★★☆

---

# Q18

## 完整問題

```java
public class Main {
    public static void main(String[] args) {
        try {
            int x = 10 / 0;
            System.out.println("A");
        } catch (ArithmeticException e) {
            System.out.println("B");
        }
        System.out.println("C");
    }
}
```

輸出？

## 你的回答

```text
B
```

## 正解

```text
B
C
```

❌

## 完整詳解

執行流程：

```text
1. 進入 try
2. 執行 int x = 10 / 0
3. 發生 ArithmeticException
4. try 裡後面的 System.out.println("A") 不會執行
5. 進入 catch
6. 印出 B
7. catch 結束
8. 繼續執行 catch 後面的 System.out.println("C")
9. 印出 C
```

所以輸出：

```text
B
C
```

你漏掉的是 catch 後面的正常流程。

## 本題觀念

try 裡出錯後，try 剩下的程式碼跳過；catch 處理完後，外層流程繼續。

## 面試高頻

★★★★★

---

# Q19

## 完整問題

請說明：

```text
Checked Exception
與
RuntimeException
最大的差異是什麼？
```

## 你的回答

```text
Checked Exception >> 編譯時就會檢查
RuntimeException >> 執行時錯誤才會拋
```

## 評分

✅ 給分，但要修正語意。

## 完整詳解

你前半句是對的：

```text
Checked Exception：編譯器會檢查，強制你處理。
```

但後半句要修正：

```text
RuntimeException 不是「執行時才會拋」。
```

更精準說法是：

```text
Checked Exception 和 RuntimeException 都是在執行時真的被拋出。
差別是：編譯器會不會強制你處理。
```

正確版：

```text
Checked Exception：
編譯器強制要求 try-catch 或 throws。

RuntimeException：
編譯器不強制處理，通常代表程式設計錯誤、非法參數、非法狀態，或業務流程中刻意設計的 unchecked exception。
```

範例：

```java
void readFile() throws IOException {
    // IOException 是 checked exception
}
```

```java
void validate(User user) {
    if (user == null) {
        throw new IllegalArgumentException("user is null");
    }
}
```

## 本題觀念

重點不是「什麼時候拋」，而是「編譯器會不會要求你處理」。

## 面試高頻

★★★★★

---

# Q20

## 完整問題

為什麼 Backend 專案通常會建立：

```java
BusinessException
NotFoundException
UnauthorizedException
```

而不是全部都直接使用：

```java
RuntimeException
```

## 你的回答

```text
因為 RuntimeException 不能表達所有商業邏輯的錯誤。
而且像 BusinessException、NotFoundException、UnauthorizedException
這種自定義錯誤，也能夠一眼看出來是哪裡出現錯誤。
```

## 評分

✅ 正確，而且是實務方向。

## 完整詳解

你的答案核心是對的。

自訂例外的好處：

### 1. 語意清楚

```java
throw new NotFoundException("User not found");
```

比：

```java
throw new RuntimeException("User not found");
```

更清楚。

---

### 2. 可以分類處理

例如 Spring Boot 可以用全域例外處理：

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(NotFoundException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorized(UnauthorizedException e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }
}
```

---

### 3. 可以對應 HTTP 狀態碼

```text
NotFoundException       → 404
UnauthorizedException   → 401
BusinessException       → 400 或 409
```

---

### 4. 更好維護

看到例外類別名稱，就知道問題類型。

```java
throw new PaymentFailedException();
throw new DuplicateEmailException();
throw new PermissionDeniedException();
```

比全部丟 `RuntimeException` 更適合中大型專案。

## 本題觀念

Backend exception architecture：例外類別本身就是業務錯誤模型的一部分。

## 面試高頻

★★★★★

---

# 最終修正版結論

## 你的真正錯題

| 題號 | 題目重點                      | 你的答案 |  正解 |
| ---- | ----------------------------- | -------: | ----: |
| Q5   | RuntimeException 是否必須處理 |        C |     B |
| Q6   | catch 父子類別順序            |        A |     D |
| Q16  | catch 後流程                  |      A C | A B C |
| Q18  | catch 後流程                  |        B |   B C |

## 你已經穩的部分

```text
Exception 繼承關係
Checked Exception 基本概念
throw vs throws 基本差異
try-with-resources
finally 基本執行
自訂 Exception 設計原因
```

## 你要補強的部分

```text
RuntimeException 不強制處理
catch 順序：子類別先，父類別後
catch 處理完後，流程會繼續
try 裡發生例外後，try 剩下程式碼會跳過
```

## 修正後學習評價

```text
Exception 基礎觀念：高
Exception 實務設計：高
Exception 執行流程：需要補強
```

```

```
