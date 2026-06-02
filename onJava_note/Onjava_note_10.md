# 第十章 介面（Interface / 接口）

> 本章重點：理解 Java 如何用 **抽象類別（Abstract Class / 抽象类）** 和 **介面（Interface / 接口）** 把「規格」和「實作」分開。

在 Java 裡，抽象類別和介面都是用來處理同一件事：

```text
我先定義一組大家都要遵守的規則，
但具體怎麼做，交給子類別或實作類別決定。
```

這種設計可以讓程式比較容易擴充，也比較不會讓某一段程式綁死在某一個具體類別上。

---

# 1. 抽象類別與抽象方法

## 1.1 為什麼需要抽象類別？

前一章多型（Polymorphism / 多态）裡的 `Instrument` 樂器範例，有一個問題：

`Instrument` 本身其實不是一個真正可以被使用的樂器。

你可以有：

- `Wind` 管樂器
- `Percussion` 打擊樂器
- `Stringed` 弦樂器

但單純一個 `Instrument` 物件沒有實際意義。

所以比較合理的設計是：

```text
Instrument 只負責定義「所有樂器都應該會做什麼」；
真正怎麼演奏，交給 Wind、Percussion、Stringed 自己實作。
```

這時候就可以使用 **抽象類別（Abstract Class / 抽象类）**。

---

## 1.2 抽象方法是什麼？

**抽象方法（Abstract Method / 抽象方法）** 是只有方法宣告、沒有方法內容的方法。

```java
abstract void play();
```

這行的意思是：

```text
我規定子類別一定要有 play() 這個方法，
但我現在不寫 play() 裡面要做什麼。
```

抽象方法沒有 `{}` 方法主體。

錯誤寫法：

```java
abstract void play() {
    System.out.println("play");
}
```

正確寫法：

```java
abstract void play();
```

---

## 1.3 有抽象方法的類別，也必須是抽象類別

只要一個類別裡面有抽象方法，這個類別本身就必須加上 `abstract`。

```java
abstract class Instrument {
    abstract void play();
}
```

如果你沒有加 `abstract`，編譯器會報錯。

原因很簡單：

```text
這個 class 還沒有完整實作，
Java 不允許你把它當成完整類別使用。
```

---

## 1.4 抽象類別不能被 new

抽象類別不能直接建立物件。

```java
abstract class Instrument {
    abstract void play();
}

public class Main {
    public static void main(String[] args) {
        Instrument i = new Instrument(); // 編譯失敗
    }
}
```

會失敗的原因是：

```text
Instrument 還沒有定義 play() 要怎麼做，
所以 Java 不知道這個物件真正能做什麼。
```

這是編譯期（Compile Time / 编译期）就會擋下來的錯誤，比等到程式執行後才爆炸安全得多。

---

## 1.5 子類別必須實作所有抽象方法

如果一個普通類別繼承抽象類別，它必須把所有抽象方法補完。

```java
abstract class Instrument {
    abstract void play();
}

class Wind extends Instrument {
    @Override
    void play() {
        System.out.println("Wind.play()");
    }
}
```

這裡的 `@Override` 表示：

```text
我正在覆寫父類別的方法。
```

如果方法名稱或參數寫錯，編譯器會提醒你。

例如：

```java
class Wind extends Instrument {
    @Override
    void plaay() { // 拼錯，編譯器會抓到
        System.out.println("Wind.play()");
    }
}
```

建議：只要你是在覆寫父類別方法，或實作介面方法，就加 `@Override`。

---

## 1.6 子類別沒有補完，也必須是 abstract

如果子類別沒有把抽象方法全部實作完，那子類別也還是不完整的，所以也必須是抽象類別。

```java
abstract class Instrument {
    abstract void play();
    abstract void adjust();
}

abstract class Wind extends Instrument {
    @Override
    void play() {
        System.out.println("Wind.play()");
    }

    // adjust() 還沒有實作，所以 Wind 也必須是 abstract
}
```

---

## 1.7 沒有抽象方法，也可以宣告成 abstract

一個類別就算沒有抽象方法，也可以被宣告成抽象類別。

```java
abstract class BaseService {
    void logStart() {
        System.out.println("start");
    }
}
```

這樣做的目的通常是：

```text
我想提供一些共用方法，
但不希望別人直接 new 這個基底類別。
```

在 backend 專案裡，常見情境像是：

```java
abstract class BaseController {
    protected void logRequest() {
        System.out.println("request received");
    }
}
```

不過要注意：現代 Spring Boot 專案不一定鼓勵大量使用繼承式的 Base 類別。很多時候會改用組合（Composition / 组合）或共用工具類別處理。

---

# 2. 抽象類別的存取權限

抽象類別裡面可以有不同存取權限的方法。

常見存取修飾子（Access Modifier / 访问修饰符）：

| Java        | 臺灣用語                     | 簡中       | 意思                    |
| ----------- | ---------------------------- | ---------- | ----------------------- |
| `public`    | 公開                         | 公开       | 外部都可以用            |
| `protected` | 受保護                       | 保护       | 同 package 或子類別可用 |
| 無修飾子    | package-private / 套件內可見 | 包访问权限 | 同 package 可用         |
| `private`   | 私有                         | 私有       | 只有自己類別內可用      |

範例：

```java
abstract class AbstractAccess {
    private void helper() {}

    protected abstract void validate();

    abstract void execute(); // package-private

    public abstract void run();
}
```

但是下面這種寫法不合法：

```java
private abstract void test(); // 編譯失敗
```

原因：

```text
private 方法不能被子類別看見，
但 abstract 方法又要求子類別去實作，
兩者互相矛盾。
```

---

# 3. 用抽象類別重寫 Instrument 範例

```java
abstract class Instrument {
    public abstract void play();

    public String what() {
        return "Instrument";
    }

    public abstract void adjust();
}

class Wind extends Instrument {
    @Override
    public void play() {
        System.out.println("Wind.play()");
    }

    @Override
    public String what() {
        return "Wind";
    }

    @Override
    public void adjust() {
        System.out.println("Adjusting Wind");
    }
}

class Percussion extends Instrument {
    @Override
    public void play() {
        System.out.println("Percussion.play()");
    }

    @Override
    public void adjust() {
        System.out.println("Adjusting Percussion");
    }
}
```

使用時：

```java
public class Main {
    static void tune(Instrument instrument) {
        instrument.play();
    }

    public static void main(String[] args) {
        Instrument wind = new Wind();
        Instrument drum = new Percussion();

        tune(wind);
        tune(drum);
    }
}
```

輸出：

```text
Wind.play()
Percussion.play()
```

重點不是樂器本身，而是這個結構：

```text
tune() 不需要知道實際傳進來的是 Wind 還是 Percussion，
它只需要知道：這東西是 Instrument，而且一定有 play()。
```

這就是抽象類別和多型搭配時的核心價值。

---

# 4. 介面 interface

## 4.1 interface 是什麼？

**介面（Interface / 接口）** 可以理解成一份「契約」。

它規定：

```text
只要某個 class 說自己 implements 這個 interface，
就必須提供 interface 規定的方法。
```

最基本的 interface 長這樣：

```java
interface PaymentProcessor {
    void pay(int amount);
}
```

這代表：

```text
任何 PaymentProcessor 都必須有 pay(int amount) 方法。
```

但 `PaymentProcessor` 不管你是：

- 信用卡付款
- LINE Pay
- Apple Pay
- 轉帳付款

具體付款細節交給實作類別處理。

---

## 4.2 implements：實作介面

```java
interface PaymentProcessor {
    void pay(int amount);
}

class CreditCardPaymentProcessor implements PaymentProcessor {
    @Override
    public void pay(int amount) {
        System.out.println("Pay by credit card: " + amount);
    }
}

class LinePayPaymentProcessor implements PaymentProcessor {
    @Override
    public void pay(int amount) {
        System.out.println("Pay by LINE Pay: " + amount);
    }
}
```

注意：實作 interface 的方法必須是 `public`。

原因是 interface 裡的方法預設就是 `public`，你不能在實作時把可見範圍縮小。

錯誤寫法：

```java
class CreditCardPaymentProcessor implements PaymentProcessor {
    @Override
    void pay(int amount) { // 編譯失敗，少了 public
        System.out.println(amount);
    }
}
```

正確寫法：

```java
class CreditCardPaymentProcessor implements PaymentProcessor {
    @Override
    public void pay(int amount) {
        System.out.println(amount);
    }
}
```

---

## 4.3 interface 的方法預設是 public abstract

在 interface 裡：

```java
interface Service {
    void execute();
}
```

等同於：

```java
interface Service {
    public abstract void execute();
}
```

通常不需要特別寫 `public abstract`，因為 Java 預設就是這樣。

比較常見、也比較乾淨的寫法是：

```java
interface Service {
    void execute();
}
```

---

## 4.4 interface 的欄位預設是 public static final

interface 可以放欄位，但它們預設都是：

```text
public static final
```

也就是常數。

```java
interface HttpStatusCode {
    int OK = 200;
    int BAD_REQUEST = 400;
    int INTERNAL_SERVER_ERROR = 500;
}
```

等同於：

```java
interface HttpStatusCode {
    public static final int OK = 200;
}
```

但在現代 Java 裡，這種「用 interface 放一堆常數」的做法要少用。

更常見的選擇是：

```java
enum OrderStatus {
    PENDING,
    PAID,
    CANCELLED
}
```

或是：

```java
final class ErrorCodes {
    private ErrorCodes() {}

    public static final String ACCOUNT_NOT_FOUND = "ACCOUNT_NOT_FOUND";
}
```

結論：

```text
interface 主要拿來定義行為契約，不是拿來當常數包。
```

---

# 5. Java 8 之後的 default method

## 5.1 default method 是什麼？

Java 8 以前，interface 幾乎只能放抽象方法。

Java 8 之後，interface 可以有 **預設方法（Default Method / 默认方法）**。

```java
interface Notifier {
    void send(String message);

    default void sendWelcomeMessage() {
        send("Welcome");
    }
}
```

這裡的 `sendWelcomeMessage()` 有方法內容，所以實作類別可以不用自己寫。

```java
class EmailNotifier implements Notifier {
    @Override
    public void send(String message) {
        System.out.println("Email: " + message);
    }
}

public class Main {
    public static void main(String[] args) {
        Notifier notifier = new EmailNotifier();
        notifier.sendWelcomeMessage();
    }
}
```

輸出：

```text
Email: Welcome
```

---

## 5.2 default method 的用途

最重要用途：

```text
在不破壞舊實作類別的情況下，
幫 interface 增加新方法。
```

假設原本有：

```java
interface Notifier {
    void send(String message);
}
```

很多類別已經實作它：

```java
class EmailNotifier implements Notifier { ... }
class SmsNotifier implements Notifier { ... }
class LineNotifier implements Notifier { ... }
```

如果你直接新增抽象方法：

```java
interface Notifier {
    void send(String message);
    void sendWelcomeMessage();
}
```

所有實作類別都會編譯失敗，因為它們都少了 `sendWelcomeMessage()`。

如果改成 default method：

```java
interface Notifier {
    void send(String message);

    default void sendWelcomeMessage() {
        send("Welcome");
    }
}
```

舊類別不需要全部改掉。

---

## 5.3 default method 不代表 interface 應該塞滿邏輯

default method 很方便，但不要濫用。

interface 的主要責任仍然是：

```text
定義能力、定義契約。
```

如果你在 interface 裡塞太多業務邏輯，設計會變得難維護。

在 backend 專案裡，通常商業邏輯應該放在：

- service class
- domain class
- strategy implementation

而不是大量塞進 interface default method。

---

# 6. interface 裡的 static method

Java 8 之後，interface 可以有 **靜態方法（Static Method / 静态方法）**。

```java
interface Validators {
    static boolean isEmail(String value) {
        return value != null && value.contains("@");
    }
}
```

使用時：

```java
public class Main {
    public static void main(String[] args) {
        boolean result = Validators.isEmail("test@example.com");
        System.out.println(result);
    }
}
```

輸出：

```text
true
```

這種寫法可以把跟 interface 概念相關的工具方法放在一起。

但在實務上，如果只是一般工具方法，也常會用：

```java
final class EmailValidator {
    private EmailValidator() {}

    static boolean isValid(String value) {
        return value != null && value.contains("@");
    }
}
```

結論：

```text
interface static method 可以用，
但不要把所有工具方法都硬塞進 interface。
```

---

# 7. 多重介面實作

Java 不支援多重類別繼承。

也就是一個 class 只能 `extends` 一個 class。

```java
class A {}
class B {}

class C extends A, B {} // 編譯失敗
```

但是 Java 可以實作多個 interface。

```java
interface Flyable {
    void fly();
}

interface Swimmable {
    void swim();
}

class Duck implements Flyable, Swimmable {
    @Override
    public void fly() {
        System.out.println("Duck flies");
    }

    @Override
    public void swim() {
        System.out.println("Duck swims");
    }
}
```

這代表 `Duck` 同時具備兩種能力：

```text
Duck is Flyable
Duck is Swimmable
```

在 Java 裡，這是 interface 很重要的用途：

```text
一個 class 可以同時被看成多種型別。
```

---

## 7.1 class + 多個 interface 的語法順序

如果同時有 `extends` 和 `implements`，順序一定是：

```java
class 子類別 extends 父類別 implements 介面1, 介面2 {
}
```

範例：

```java
class Character {
    public void move() {
        System.out.println("move");
    }
}

interface CanFight {
    void fight();
}

interface CanFly {
    void fly();
}

class Hero extends Character implements CanFight, CanFly {
    @Override
    public void fight() {
        System.out.println("fight");
    }

    @Override
    public void fly() {
        System.out.println("fly");
    }
}
```

不能寫成：

```java
class Hero implements CanFight, CanFly extends Character { // 錯
}
```

---

## 7.2 為什麼多個 interface 有用？

假設有方法：

```java
static void startFight(CanFight fighter) {
    fighter.fight();
}

static void startFlight(CanFly flyer) {
    flyer.fly();
}
```

`Hero` 可以傳進兩個方法：

```java
Hero hero = new Hero();
startFight(hero);
startFlight(hero);
```

因為 `Hero` 同時是：

```text
CanFight
CanFly
Character
```

這就是 interface 帶來的彈性。

---

# 8. default method 的衝突

如果一個 class 實作兩個 interface，而兩個 interface 有同名同參數的 default method，就會衝突。

```java
interface A {
    default void hello() {
        System.out.println("A.hello");
    }
}

interface B {
    default void hello() {
        System.out.println("B.hello");
    }
}

class C implements A, B {
    // 編譯失敗，因為 Java 不知道要用 A 還是 B 的 hello()
}
```

解法：在 class 裡自己覆寫。

```java
class C implements A, B {
    @Override
    public void hello() {
        A.super.hello();
    }
}
```

或自己寫新的邏輯：

```java
class C implements A, B {
    @Override
    public void hello() {
        System.out.println("C.hello");
    }
}
```

注意：

```text
回傳型別不是方法簽名的一部分。
```

所以不能只靠回傳型別不同來區分方法。

```java
interface A {
    int value();
}

interface B {
    String value();
}

class C implements A, B { // 編譯失敗
}
```

---

# 9. 介面繼承介面

interface 可以繼承 interface。

```java
interface Repository {
    void save();
}

interface UserRepository extends Repository {
    void findByEmail(String email);
}
```

這代表：

```text
UserRepository 擁有 Repository 的 save()，
同時又新增 findByEmail()。
```

如果 class 實作 `UserRepository`，兩個方法都要實作。

```java
class JdbcUserRepository implements UserRepository {
    @Override
    public void save() {
        System.out.println("save user");
    }

    @Override
    public void findByEmail(String email) {
        System.out.println("find user by email: " + email);
    }
}
```

---

## 9.1 interface 可以同時繼承多個 interface

```java
interface ReadableData {
    String read();
}

interface WritableData {
    void write(String value);
}

interface DataStore extends ReadableData, WritableData {
}
```

`DataStore` 同時具備讀與寫的契約。

這個語法只適用於 interface。

class 不能 `extends` 多個 class，但 interface 可以 `extends` 多個 interface。

---

# 10. 抽象類別 vs 介面

Java 8 之後，interface 有 default method，所以初學時會比較容易混淆：

```text
既然 interface 也可以有方法內容，
那什麼時候用 abstract class？
什麼時候用 interface？
```

可以先用這張表判斷。

| 比較項目 | Interface（介面 / 接口）                | Abstract Class（抽象類別 / 抽象类） |
| -------- | --------------------------------------- | ----------------------------------- |
| 主要用途 | 定義能力、規格、契約                    | 定義共同基底與共用狀態              |
| 多重組合 | 一個 class 可 implements 多個 interface | 一個 class 只能 extends 一個 class  |
| 物件狀態 | 不能放一般 instance field               | 可以放 instance field               |
| 建構子   | 沒有 constructor                        | 可以有 constructor                  |
| 方法實作 | 可有 default/static/private method      | 可有一般方法與抽象方法              |
| 適合語意 | 某種能力，例如 `Runnable`、`Comparable` | 某種共同父類，例如 `BaseEntity`     |

簡化判斷：

```text
如果你要表達「這個類別具備某種能力」：用 interface。
如果你要表達「這些類別共享狀態與部分實作」：考慮 abstract class。
```

---

## 10.1 什麼時候用 interface？

適合使用 interface 的情境：

```text
1. 不同類別可以有同一種能力。
2. 你想讓程式依賴規格，而不是依賴具體實作。
3. 你需要一個 class 同時具備多種型別。
4. 你要做策略模式、adapter、mock 測試。
```

backend 常見例子：

```java
interface PasswordEncoder {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
```

不同實作：

```java
class BcryptPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(String rawPassword) {
        return "bcrypt:" + rawPassword;
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return encodedPassword.equals(encode(rawPassword));
    }
}
```

使用端只依賴 interface：

```java
class AuthService {
    private final PasswordEncoder passwordEncoder;

    AuthService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    void register(String password) {
        String encoded = passwordEncoder.encode(password);
        System.out.println(encoded);
    }
}
```

這樣 `AuthService` 不需要知道實際使用哪一種加密方式。

---

## 10.2 什麼時候用 abstract class？

適合使用 abstract class 的情境：

```text
1. 多個子類別真的共享欄位。
2. 多個子類別真的共享部分流程。
3. 你想禁止直接 new 基底類別。
4. 你需要 constructor 初始化共同狀態。
```

範例：

```java
abstract class BaseEntity {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

class User extends BaseEntity {
    private String email;
}

class Product extends BaseEntity {
    private String name;
}
```

這裡 `BaseEntity` 放共同欄位 `id`，所以 abstract class 比 interface 合理。

但要注意：在 JPA / Hibernate 裡，entity 繼承有額外規則，實務上會再搭配 `@MappedSuperclass` 等機制。這裡只看 Java 語法概念。

---

# 11. 完全解耦：讓方法依賴 interface，而不是具體 class

## 11.1 問題：方法綁死具體類別

假設有一個付款服務：

```java
class CreditCardPaymentProcessor {
    void pay(int amount) {
        System.out.println("Credit card pay: " + amount);
    }
}

class CheckoutService {
    void checkout(CreditCardPaymentProcessor processor) {
        processor.pay(1000);
    }
}
```

這樣的問題是：

```text
CheckoutService 只能用 CreditCardPaymentProcessor。
如果未來要改 LINE Pay，就要改 CheckoutService。
```

這就是耦合（Coupling / 耦合）太高。

---

## 11.2 解法：依賴 interface

先定義 interface：

```java
interface PaymentProcessor {
    void pay(int amount);
}
```

不同付款方式實作它：

```java
class CreditCardPaymentProcessor implements PaymentProcessor {
    @Override
    public void pay(int amount) {
        System.out.println("Credit card pay: " + amount);
    }
}

class LinePayPaymentProcessor implements PaymentProcessor {
    @Override
    public void pay(int amount) {
        System.out.println("LINE Pay: " + amount);
    }
}
```

使用端改依賴 interface：

```java
class CheckoutService {
    void checkout(PaymentProcessor processor) {
        processor.pay(1000);
    }
}
```

使用：

```java
public class Main {
    public static void main(String[] args) {
        CheckoutService service = new CheckoutService();

        service.checkout(new CreditCardPaymentProcessor());
        service.checkout(new LinePayPaymentProcessor());
    }
}
```

這樣 `CheckoutService` 不需要知道具體付款方式。

它只需要知道：

```text
傳進來的東西只要是 PaymentProcessor，
就一定有 pay() 可以呼叫。
```

這就是 interface 的核心價值。

---

# 12. 策略模式：把變動的行為交給物件

上面的 `PaymentProcessor` 其實就是 **策略模式（Strategy Pattern / 策略模式）** 的基本概念。

策略模式的結構：

```text
固定流程：CheckoutService.checkout()
變動部分：PaymentProcessor.pay()
```

也就是：

```text
流程本身不變，
但某一步要怎麼做，可以換不同策略。
```

backend 常見情境：

- 不同登入方式：Email / Phone / Google / LINE
- 不同通知方式：Email / SMS / Push
- 不同付款方式：Credit Card / LINE Pay / ATM
- 不同檔案儲存：Local / S3 / GCS

範例：

```java
interface NotificationSender {
    void send(String message);
}

class EmailSender implements NotificationSender {
    @Override
    public void send(String message) {
        System.out.println("Email: " + message);
    }
}

class SmsSender implements NotificationSender {
    @Override
    public void send(String message) {
        System.out.println("SMS: " + message);
    }
}

class NotificationService {
    private final NotificationSender sender;

    NotificationService(NotificationSender sender) {
        this.sender = sender;
    }

    void notifyUser(String message) {
        sender.send(message);
    }
}
```

---

# 13. Adapter Pattern：讓舊類別符合新 interface

## 13.1 問題：現有類別不能改

假設你有一個舊系統類別：

```java
class LegacySmsClient {
    void sendSms(String phone, String text) {
        System.out.println("Send SMS to " + phone + ": " + text);
    }
}
```

但你的新系統希望所有通知都符合這個 interface：

```java
interface NotificationSender {
    void send(String message);
}
```

`LegacySmsClient` 不是你寫的，或你不想直接改它。

這時候可以寫一個 **轉接器（Adapter / 适配器）**。

---

## 13.2 Adapter 範例

```java
class SmsNotificationAdapter implements NotificationSender {
    private final LegacySmsClient smsClient;
    private final String phone;

    SmsNotificationAdapter(LegacySmsClient smsClient, String phone) {
        this.smsClient = smsClient;
        this.phone = phone;
    }

    @Override
    public void send(String message) {
        smsClient.sendSms(phone, message);
    }
}
```

使用：

```java
public class Main {
    public static void main(String[] args) {
        LegacySmsClient legacyClient = new LegacySmsClient();

        NotificationSender sender =
                new SmsNotificationAdapter(legacyClient, "0912345678");

        sender.send("Your code is 123456");
    }
}
```

重點：

```text
Adapter 把舊類別包起來，
對外提供新系統需要的 interface。
```

這在接第三方 API、舊系統、外部 SDK 時很常見。

---

# 14. interface 欄位與初始化

interface 裡的欄位是 `public static final`，所以它們屬於 interface 本身，不屬於某個物件。

```java
interface RandomValues {
    int VALUE = 10;
}
```

使用：

```java
System.out.println(RandomValues.VALUE);
```

不需要 new。

但實務上，interface 不適合放太多狀態或常數。

尤其像這種舊寫法：

```java
interface Months {
    int JANUARY = 1;
    int FEBRUARY = 2;
    int MARCH = 3;
}
```

現在通常會改用 enum：

```java
enum Month {
    JANUARY,
    FEBRUARY,
    MARCH
}
```

所以這一段只需要知道：

```text
interface 欄位預設是 public static final。
但現代 Java 不建議把 interface 當常數集合濫用。
```

---

# 15. 巢狀介面 Nested Interface

**巢狀介面（Nested Interface / 嵌套接口）** 是定義在 class 或 interface 裡面的 interface。

```java
class Button {
    interface OnClickListener {
        void onClick();
    }
}
```

使用：

```java
class SaveButtonListener implements Button.OnClickListener {
    @Override
    public void onClick() {
        System.out.println("save");
    }
}
```

這種寫法通常表示：

```text
這個 interface 和外層 class 有強關聯。
```

但在一般 backend 初學階段，巢狀介面不是主線。

你先知道即可，不需要現在深挖。

---

# 16. Factory Method Pattern：用工廠建立符合 interface 的物件

## 16.1 為什麼需要工廠？

有時候使用端不想直接 `new` 具體類別。

例如：

```java
PaymentProcessor processor = new CreditCardPaymentProcessor();
```

這樣使用端就知道太多細節。

可以改成由工廠負責建立物件。

---

## 16.2 基本工廠範例

```java
interface PaymentProcessor {
    void pay(int amount);
}

class CreditCardPaymentProcessor implements PaymentProcessor {
    @Override
    public void pay(int amount) {
        System.out.println("Credit card pay: " + amount);
    }
}

interface PaymentProcessorFactory {
    PaymentProcessor create();
}

class CreditCardPaymentProcessorFactory implements PaymentProcessorFactory {
    @Override
    public PaymentProcessor create() {
        return new CreditCardPaymentProcessor();
    }
}
```

使用：

```java
class CheckoutService {
    void checkout(PaymentProcessorFactory factory) {
        PaymentProcessor processor = factory.create();
        processor.pay(1000);
    }
}
```

重點：

```text
使用端依賴 factory interface，
factory 再決定要建立哪個實作類別。
```

---

## 16.3 實務補充：Spring 會幫你做很多工廠工作

在 Spring Boot 裡，很多時候你不會自己手寫一堆 factory class。

Spring 的 Bean Container（Bean 容器 / Bean 容器）會幫你建立物件、管理依賴、注入需要的實作。

例如：

```java
interface NotificationSender {
    void send(String message);
}

class EmailNotificationSender implements NotificationSender {
    @Override
    public void send(String message) {
        System.out.println(message);
    }
}
```

未來在 Spring 裡，你會用 DI（Dependency Injection / 依賴注入 / 依赖注入）讓 `NotificationService` 拿到 `NotificationSender`。

所以 factory method 本章先理解概念即可：

```text
不要讓使用端到處直接 new 具體實作。
```

---

# 17. 本章常見誤解

## 誤解 1：interface 比 abstract class 一定更好

不對。

interface 和 abstract class 是不同工具。

```text
interface 適合定義能力與契約。
abstract class 適合共享狀態與部分實作。
```

不要因為 interface 看起來比較抽象，就到處使用 interface。

---

## 誤解 2：有 interface 就比較工程化

不一定。

如果系統只有一個實作，而且沒有替換需求，硬切 interface 可能只是增加複雜度。

例如：

```text
UserService interface
UserServiceImpl class
```

這種寫法在企業專案常見，但不代表永遠必要。

在小型專案或早期階段，可以先用具體 class。

等到真的出現：

- 多種實作
- 測試需要 mock
- 需要隔離第三方服務
- service 之間需要穩定契約

再抽 interface 也可以。

---

## 誤解 3：default method 可以取代 abstract class

不完全可以。

default method 不能放一般 instance field，也沒有 constructor。

所以如果你需要共享物件狀態，abstract class 仍然有它的用途。

---

## 誤解 4：interface 裡的方法不用寫 public，所以實作時也不用

錯。

interface 裡的方法預設是 public。

實作類別的方法也必須是 public。

```java
interface Task {
    void run();
}

class EmailTask implements Task {
    @Override
    public void run() { // 必須 public
        System.out.println("run");
    }
}
```

---

# 18. 本章核心觀念整理

## 18.1 抽象類別

```text
abstract class 可以有：
- instance field
- constructor
- 一般方法
- abstract method
```

用途：

```text
共享共同狀態與部分實作，
同時禁止直接建立基底類別物件。
```

---

## 18.2 介面

```text
interface 用來定義一組行為契約。
```

它告訴使用端：

```text
只要某個物件是這個 interface 型別，
我就可以呼叫這些方法。
```

---

## 18.3 解耦

interface 最大價值是降低耦合。

```text
高耦合：CheckoutService 依賴 CreditCardPaymentProcessor
低耦合：CheckoutService 依賴 PaymentProcessor interface
```

---

## 18.4 多型搭配 interface

```java
PaymentProcessor p1 = new CreditCardPaymentProcessor();
PaymentProcessor p2 = new LinePayPaymentProcessor();
```

變數型別是 interface，實際物件是不同實作。

執行時會呼叫實際物件自己的方法。

這就是多型在 interface 上的應用。

---

# 19. 面試高頻

## 19.1 interface 和 abstract class 差在哪？

回答方向：

```text
interface 偏向定義行為契約，可以多重實作；
abstract class 偏向共同父類，可以共享狀態與部分實作，但只能單一繼承。
```

補充：

```text
Java 8 之後 interface 可以有 default/static method，
但它仍然不能像 abstract class 一樣持有一般 instance state。
```

---

## 19.2 interface 裡的方法預設是什麼？

```text
public abstract
```

所以實作時必須是 `public`。

---

## 19.3 interface 裡的欄位預設是什麼？

```text
public static final
```

所以通常是常數。

---

## 19.4 Java 可以多重繼承嗎？

精準回答：

```text
Java 不支援多重 class 繼承。
一個 class 只能 extends 一個 class。
但 Java 可以 implements 多個 interface。
Java 8 之後，interface 的 default method 讓 Java 具備部分多重行為組合能力，但不是多重狀態繼承。
```

---

## 19.5 default method 衝突怎麼處理？

如果兩個 interface 有相同 default method，實作類別必須自己覆寫。

```java
class C implements A, B {
    @Override
    public void hello() {
        A.super.hello();
    }
}
```

---

## 19.6 為什麼 backend 常用 interface？

回答方向：

```text
因為 interface 可以讓 service 依賴抽象契約，而不是依賴具體實作。
這有助於替換實作、測試 mock、整合第三方服務，以及降低模組之間的耦合。
```

例如：

```text
NotificationSender
PaymentProcessor
PasswordEncoder
StorageClient
TokenProvider
```

---

# 20. 本章學習結論

本章真正要學會的不是語法本身，而是這個設計判斷：

```text
什麼時候我只是需要一個普通 class？
什麼時候我需要 abstract class？
什麼時候我需要 interface？
```

實務判斷可以先用這個版本：

```text
1. 預設先用普通 class。
2. 如果需要多種可替換行為，再抽 interface。
3. 如果多個類別共享狀態與部分實作，再考慮 abstract class。
4. 不要為了「看起來比較設計模式」而過早抽象。
```

這點很重要。

過早抽象（Premature Abstraction / 过早抽象）會讓程式變複雜。

好的設計不是 interface 越多越好，而是：

```text
抽象剛好服務真正的變動點。
```

---

# 21. 建議練習

## 練習 1：抽象類別

設計一個 `Animal` 抽象類別：

要求：

```text
- 有 abstract void makeSound()
- 有一般方法 sleep()
- Dog / Cat 繼承 Animal
- main() 裡用 Animal 變數接 Dog / Cat
```

---

## 練習 2：interface

設計一個 `NotificationSender` interface：

要求：

```text
- void send(String message)
- EmailSender 實作它
- SmsSender 實作它
- NotificationService 依賴 NotificationSender
```

---

## 練習 3：default method

在 `NotificationSender` 裡加入：

```java
default void sendWelcomeMessage() {
    send("Welcome");
}
```

確認 `EmailSender` 和 `SmsSender` 沒有自己寫 `sendWelcomeMessage()`，仍然可以呼叫。

---

## 練習 4：Adapter

假設有舊類別：

```java
class LegacyEmailClient {
    void deliver(String title, String body) {
        System.out.println(title + ": " + body);
    }
}
```

請寫一個 `EmailNotificationAdapter implements NotificationSender`，讓舊類別可以接上新 interface。

---

# 22. 本章最小通關標準

你完成本章後，至少要能做到：

```text
1. 說出 abstract class 和 interface 的差異。
2. 手寫一個 interface 並讓 class implements 它。
3. 知道 interface 方法實作時必須 public。
4. 知道 default method 的用途與風險。
5. 知道 Java 不能 extends 多個 class，但可以 implements 多個 interface。
6. 能用 interface 降低 service 對具體 class 的依賴。
7. 能看懂 adapter / strategy / factory method 的基本結構。
```

# 批改總覽

| 區塊             |                             結果 |
| ---------------- | -------------------------------: |
| Q1–Q10 選擇題    |                          10 / 10 |
| Q11–Q15 是非題   |                            4 / 5 |
| Q16–Q20 程式判斷 |                          4.5 / 5 |
| Q21–Q24 簡答     |                       約 3.3 / 4 |
| Q25 程式碼       |             概念對，但語法不完整 |
| Q26–Q28 加分題   | Q26 概念對、Q27 對、Q28 大方向對 |

整體判斷：
你對 **interface / abstract class 的核心差異、多重實作、default method 衝突、低耦合設計** 已經有抓到。主要要補的是：

```text
1. abstract class 可以有一般方法。
2. interface default method 的輸出要追到實際呼叫的 send()。
3. interface 欄位不是拿來放使用者資料。
4. 寫 class implements interface 時，Java 語法要完整。
5. 依賴 interface 不是永遠必要，但在 PasswordEncoder 這種可替換策略上很合理。
```

---

# Part A：選擇題

---

## Q1

### 完整問題

關於抽象方法（Abstract Method / 抽象方法），哪個敘述正確？

### 完整選項

A. 抽象方法可以有 `{}` 方法主體
B. 抽象方法只能出現在 interface 裡
C. 抽象方法只有宣告，沒有方法主體
D. 抽象方法一定是 `private`

### 你的回答

C

### 正確答案

C

### 詳解

抽象方法只有方法宣告，沒有方法主體。

```java
abstract void f();
```

不能寫成：

```java
abstract void f() {
    System.out.println("f");
}
```

因為 `abstract` 的意思就是：

```text
這個方法現在不實作，交給子類別實作。
```

### 本題觀念

- 抽象方法（Abstract Method / 抽象方法）
- 方法宣告（Method Declaration / 方法声明）
- 方法主體（Method Body / 方法体）

### 面試高頻

高。

這是 abstract class 基本題。

---

## Q2

### 完整問題

下列哪個 class 宣告是合法的？

### 完整選項

A.

```java
class Animal {
    abstract void makeSound();
}
```

B.

```java
abstract class Animal {
    abstract void makeSound();
}
```

C.

```java
class Animal {
    private abstract void makeSound();
}
```

D.

```java
abstract class Animal {
    abstract void makeSound() {}
}
```

### 你的回答

B

### 正確答案

B

### 詳解

只要 class 裡面有 abstract method，class 本身也必須宣告成 `abstract`。

A 錯，因為有 abstract method，但 class 沒有加 `abstract`。

C 錯，因為 `private abstract` 矛盾。
`private` 代表子類別看不到；`abstract` 代表子類別必須實作。兩者不能同時成立。

D 錯，因為 abstract method 不能有 `{}` 方法主體。

### 本題觀念

- 有 abstract method 的 class 必須是 abstract class。
- `private abstract` 不合法。
- abstract method 不能有方法內容。

### 面試高頻

高。

---

## Q3

### 完整問題

抽象類別（Abstract Class / 抽象类）能不能直接 `new`？

### 完整選項

A. 可以，只要它沒有抽象方法
B. 可以，只要 constructor 是 public
C. 不可以，只要 class 被宣告成 `abstract` 就不能直接 new
D. 可以，但只能在同一個 package 裡 new

### 你的回答

C

### 正確答案

C

### 詳解

只要 class 被宣告成 `abstract`，就不能直接建立物件。

```java
abstract class Animal {}

Animal a = new Animal(); // 編譯失敗
```

即使它裡面沒有 abstract method，只要 class 是 `abstract`，就不能 `new`。

### 本題觀念

- 抽象類別不能被實例化（instantiate / 实例化）。
- constructor 存在不代表可以 new。

### 面試高頻

高。

---

## Q4

### 完整問題

interface 裡面的方法，預設是什麼？

### 完整選項

A. `private static`
B. `public abstract`
C. `protected abstract`
D. `public final`

### 你的回答

B

### 正確答案

B

### 詳解

interface 裡的方法預設是：

```java
public abstract
```

所以：

```java
interface Task {
    void run();
}
```

等同於：

```java
interface Task {
    public abstract void run();
}
```

### 本題觀念

- interface method 預設 `public abstract`
- 實作時不能降低存取權限

### 面試高頻

高。

---

## Q5

### 完整問題

interface 裡面的欄位，預設是什麼？

### 完整選項

A. `private static`
B. `public static final`
C. `protected final`
D. `public transient`

### 你的回答

B

### 正確答案

B

### 詳解

interface 裡的欄位預設是：

```java
public static final
```

所以：

```java
interface Status {
    int ACTIVE = 1;
}
```

等同於：

```java
interface Status {
    public static final int ACTIVE = 1;
}
```

它是常數，不是每個物件自己的欄位。

### 本題觀念

- interface field 預設是常數。
- interface 不適合存放物件狀態。

### 面試高頻

中高。

---

## Q6

### 完整問題

下列哪個實作 interface 的方法寫法正確？

```java
interface Task {
    void run();
}
```

### 完整選項

A.

```java
class EmailTask implements Task {
    void run() {}
}
```

B.

```java
class EmailTask implements Task {
    public void run() {}
}
```

C.

```java
class EmailTask implements Task {
    private void run() {}
}
```

D.

```java
class EmailTask implements Task {
    protected void run() {}
}
```

### 你的回答

B

### 正確答案

B

### 詳解

`Task.run()` 在 interface 裡預設是 `public abstract`。

所以實作時必須是 `public`：

```java
class EmailTask implements Task {
    @Override
    public void run() {}
}
```

A 的 `void run()` 沒有修飾子，是 package-private（套件私有 / 包访问权限），比 `public` 小，所以不合法。

C、D 也都比 `public` 小。

### 本題觀念

- 實作 interface method 時，不能降低可見性。
- package-private 不是 public。

### 面試高頻

高。

---

## Q7

### 完整問題

Java 是否支援多重類別繼承？

### 完整選項

A. 支援，一個 class 可以 `extends` 多個 class
B. 不支援，一個 class 只能 `extends` 一個 class
C. 支援，但只能繼承 abstract class
D. 支援，但只有 Java 8 之後可以

### 你的回答

B

### 正確答案

B

### 詳解

Java 不支援多重 class 繼承。

錯誤：

```java
class C extends A, B {}
```

正確：

```java
class C extends A implements X, Y {}
```

Java 可以：

```text
extends 一個 class
implements 多個 interface
```

### 本題觀念

- 單一類別繼承（Single Inheritance / 单继承）
- 多重介面實作（Multiple Interface Implementation / 多接口实现）

### 面試高頻

高。

---

## Q8

### 完整問題

關於 default method（預設方法 / 默认方法），哪個敘述正確？

### 完整選項

A. default method 只能寫在 abstract class 裡
B. default method 讓 interface 可以提供方法實作
C. default method 一定要由每個實作類別覆寫
D. default method 不能呼叫 interface 裡的其他方法

### 你的回答

B

### 正確答案

B

### 詳解

Java 8 之後，interface 可以有 default method。

```java
interface Notifier {
    void send(String message);

    default void sendWelcomeMessage() {
        send("Welcome");
    }
}
```

default method 有方法主體，實作類別可以不覆寫它。

### 本題觀念

- default method
- interface 可以提供部分預設行為
- 但不要把大量商業邏輯塞進 interface

### 面試高頻

中高。

---

## Q9

### 完整問題

下列哪個情境最適合使用 interface？

### 完整選項

A. 多個類別共享相同 instance field
B. 需要 constructor 初始化共同狀態
C. 想定義一種能力或契約，讓不同類別各自實作
D. 想讓 class 不能被 new

### 你的回答

C

### 正確答案

C

### 詳解

interface 最適合用來定義：

```text
能力
契約
規格
```

例如：

```java
interface PaymentProcessor {
    void pay(int amount);
}
```

不同付款方式各自實作：

```java
class CreditCardPaymentProcessor implements PaymentProcessor {}
class LinePayPaymentProcessor implements PaymentProcessor {}
```

A、B 比較偏 abstract class。
D 可以用 abstract class，但不是 interface 的主要用途。

### 本題觀念

- interface = 行為契約
- abstract class = 共同基底與 shared state

### 面試高頻

高。

---

## Q10

### 完整問題

下列哪個情境比較適合使用 abstract class？

### 完整選項

A. 只想定義一組沒有狀態的能力
B. 一個類別需要同時具備多種能力
C. 多個子類別共享共同欄位與部分實作
D. 想讓一個 class implements 多個規格

### 你的回答

C

### 正確答案

C

### 詳解

abstract class 適合：

```text
多個子類別共享欄位
多個子類別共享部分方法
需要 constructor 初始化共同狀態
```

例如：

```java
abstract class BaseEntity {
    private Long id;

    public Long getId() {
        return id;
    }
}
```

A、B、D 都比較偏 interface。

### 本題觀念

- shared state
- constructor
- common implementation
- abstract class 的使用時機

### 面試高頻

高。

---

# Part B：是非題

---

## Q11

### 完整問題

interface 的主要用途是定義行為契約，而不是拿來儲存物件狀態。

### 你的回答

T

### 正確答案

T

### 詳解

正確。

interface 主要是定義行為規格，例如：

```java
interface Sender {
    void send(String message);
}
```

interface 裡的欄位是 `public static final`，不是 instance field，所以不適合儲存物件狀態。

### 本題觀念

- interface = contract
- interface 不存物件狀態

### 面試高頻

高。

---

## Q12

### 完整問題

一個 class 可以同時 `implements` 多個 interface。

### 你的回答

T

### 正確答案

T

### 詳解

正確。

```java
class Duck implements Flyable, Swimmable {
}
```

Java 不支援多重 class 繼承，但支援多重 interface 實作。

### 本題觀念

- implements 多個 interface
- interface 可作為多型型別

### 面試高頻

高。

---

## Q13

### 完整問題

abstract class 裡面只能有 abstract method，不能有一般方法。

### 你的回答

T

### 正確答案

F

### 你的答案判斷

錯誤。

### 詳解

abstract class 可以同時有：

```text
abstract method
一般方法
field
constructor
```

例如：

```java
abstract class Animal {
    abstract void makeSound();

    void sleep() {
        System.out.println("Animal.sleep()");
    }
}
```

這完全合法。

所以 abstract class 不是「全部方法都必須抽象」。

它的意思是：

```text
這個 class 不能直接 new，
而且它可以包含尚未完成的方法。
```

### 本題觀念

- abstract class 可以有一般方法。
- abstract class 可以有欄位。
- abstract class 可以有 constructor。

### 面試高頻

高。

這題很常拿來區分你是不是真的理解 abstract class。

---

## Q14

### 完整問題

如果 class 實作兩個 interface，而兩個 interface 有同名同參數的 default method，class 必須自己覆寫該方法解決衝突。

### 你的回答

T

### 正確答案

T

### 詳解

正確。

```java
interface A {
    default void hello() {}
}

interface B {
    default void hello() {}
}

class C implements A, B {
    @Override
    public void hello() {
        A.super.hello();
    }
}
```

如果 `C` 不覆寫，Java 不知道該使用 `A.hello()` 還是 `B.hello()`，所以會編譯失敗。

### 本題觀念

- default method 衝突
- `A.super.method()` 語法

### 面試高頻

中高。

---

## Q15

### 完整問題

回傳型別是 Java 方法簽名（method signature / 方法签名）的一部分，所以可以只靠回傳型別不同來區分兩個同名同參數方法。

### 你的回答

F

### 正確答案

F

### 詳解

正確。

Java 的方法簽名主要包含：

```text
方法名稱
參數型別與順序
```

不包含回傳型別。

所以這樣不合法：

```java
int value() {
    return 1;
}

String value() {
    return "A";
}
```

因為兩個方法的名稱和參數完全一樣，不能只靠回傳型別區分。

### 本題觀念

- method signature
- overloading 不能只靠 return type

### 面試高頻

高。

---

# Part C：程式碼判斷與輸出題

---

## Q16

### 完整問題

請問這段程式會不會編譯成功？如果成功，輸出什麼？

```java
abstract class Animal {
    abstract void makeSound();

    void sleep() {
        System.out.println("Animal.sleep()");
    }
}

class Dog extends Animal {
    @Override
    void makeSound() {
        System.out.println("Dog.bark()");
    }
}

public class Main {
    public static void main(String[] args) {
        Animal animal = new Dog();
        animal.makeSound();
        animal.sleep();
    }
}
```

### 你的回答

```text
會編譯成功
Dog.bark()
Animal.sleep()
```

### 正確答案

```text
會編譯成功
Dog.bark()
Animal.sleep()
```

### 詳解

```java
Animal animal = new Dog();
```

這是向上轉型（Upcasting / 向上转型）。

變數型別是 `Animal`，實際物件是 `Dog`。

呼叫：

```java
animal.makeSound();
```

`makeSound()` 是被 `Dog` 覆寫的方法，所以執行期會呼叫 `Dog.makeSound()`。

呼叫：

```java
animal.sleep();
```

`Dog` 沒有覆寫 `sleep()`，所以使用父類別 `Animal` 的一般方法。

### 本題觀念

- abstract class 可以有一般方法。
- 多型方法呼叫看實際物件。
- 沒覆寫的方法會沿用父類別版本。

### 面試高頻

高。

---

## Q17

### 完整問題

請問這段程式會不會編譯成功？原因是什麼？

```java
interface Sender {
    void send(String message);
}

class EmailSender implements Sender {
    @Override
    void send(String message) {
        System.out.println("Email: " + message);
    }
}
```

### 你的回答

```text
不會編譯成功
原因：因為 void send(String message); 是 interface 的 method 預設是 public abstract。
void send 沒有修飾詞，會是 package-private，會衝突。
```

### 正確答案

不會編譯成功。

原因：`Sender.send()` 預設是 `public abstract`，但 `EmailSender.send()` 沒有寫 `public`，變成 package-private，降低了可見性。

### 詳解

interface 方法：

```java
void send(String message);
```

等同於：

```java
public abstract void send(String message);
```

但你實作時寫：

```java
void send(String message)
```

這是 package-private（套件私有 / 包访问权限）。

Java 不允許覆寫或實作方法時，把存取範圍縮小。

正確寫法：

```java
class EmailSender implements Sender {
    @Override
    public void send(String message) {
        System.out.println("Email: " + message);
    }
}
```

### 本題觀念

- interface method 預設 public
- 實作時必須 public
- 不能降低可見性

### 面試高頻

高。

你的觀念正確。用詞小修：不是 `packaged private`，是 `package-private`。

---

## Q18

### 完整問題

請問這段程式會不會編譯成功？如果成功，輸出什麼？

```java
interface Notifier {
    void send(String message);

    default void sendWelcomeMessage() {
        send("Welcome");
    }
}

class SmsNotifier implements Notifier {
    @Override
    public void send(String message) {
        System.out.println("SMS: " + message);
    }
}

public class Main {
    public static void main(String[] args) {
        Notifier notifier = new SmsNotifier();
        notifier.sendWelcomeMessage();
    }
}
```

### 你的回答

```text
會，Welcome
SmsNotifier implements Notifier 他有 override send 且 public。
當 notifier 呼叫 sendWelcomeMessage 方法，方法看 object，但是 SmsNotifier 沒有這個方法，但是他實作的 interface 有，所以呼叫 default void sendWelcomeMessage() {
    send("Welcome");
}
```

### 正確答案

會編譯成功。

輸出：

```text
SMS: Welcome
```

### 你的答案判斷

部分正確。

你判斷「會編譯成功」正確。
你也知道會走 interface 的 default method。
但輸出少了 `SMS: `。

### 詳解

呼叫：

```java
notifier.sendWelcomeMessage();
```

`SmsNotifier` 沒有覆寫 `sendWelcomeMessage()`，所以使用 `Notifier` 裡的 default method。

default method 裡面：

```java
send("Welcome");
```

這裡的 `send()` 是 interface 規定的方法，但實際物件是：

```java
new SmsNotifier()
```

所以呼叫的是：

```java
SmsNotifier.send("Welcome")
```

也就是：

```java
System.out.println("SMS: " + message);
```

因此輸出：

```text
SMS: Welcome
```

### 本題觀念

- default method 可以呼叫 interface 裡的抽象方法。
- default method 裡呼叫的抽象方法，仍然會走多型。
- 輸出題要追到最後一層實際方法。

### 面試高頻

中高。

---

## Q19

### 完整問題

請問這段程式會不會編譯成功？如果不會，原因是什麼？

```java
interface A {
    default void hello() {
        System.out.println("A.hello");
    }
}

interface B {
    default void hello() {
        System.out.println("B.hello");
    }
}

class C implements A, B {
}
```

### 你的回答

```text
不會編譯成功
方法名重複，需要明確複寫
```

### 正確答案

不會編譯成功。

原因：`A` 和 `B` 都提供了同名、同參數的 default method，`C` 必須自己覆寫 `hello()` 解決衝突。

### 詳解

Java 不知道 `C` 應該繼承哪一個 default method：

```text
A.hello()
B.hello()
```

所以必須改成：

```java
class C implements A, B {
    @Override
    public void hello() {
        A.super.hello();
    }
}
```

或：

```java
class C implements A, B {
    @Override
    public void hello() {
        System.out.println("C.hello");
    }
}
```

### 本題觀念

- default method 衝突
- class 必須覆寫解決

### 面試高頻

中高。

---

## Q20

### 完整問題

請問這段程式會不會編譯成功？如果成功，輸出什麼？

```java
interface A {
    default void hello() {
        System.out.println("A.hello");
    }
}

interface B {
    default void hello() {
        System.out.println("B.hello");
    }
}

class C implements A, B {
    @Override
    public void hello() {
        B.super.hello();
    }
}

public class Main {
    public static void main(String[] args) {
        C c = new C();
        c.hello();
    }
}
```

### 你的回答

```text
會，B.hello
明確複寫
```

### 正確答案

```text
會編譯成功
B.hello
```

### 詳解

`C` 自己覆寫了 `hello()`：

```java
@Override
public void hello() {
    B.super.hello();
}
```

這代表它明確選擇呼叫 `B` 介面裡的 default method。

所以輸出：

```text
B.hello
```

### 本題觀念

- `InterfaceName.super.method()`
- 明確指定呼叫哪個 interface 的 default method

### 面試高頻

中。

---

# Part D：簡答題

---

## Q21

### 完整問題

請用自己的話說明：

```text
interface 和 abstract class 最大差異是什麼？
```

至少回答到：

- 多重實作 / 單一繼承
- 狀態 shared state
- constructor
- 使用時機

### 你的回答

```text
interface 多重實作 / 想定義一種能力或契約，讓不同類別各自實作
abstract class 單一繼承 / 狀態 shared state / constructor / 多個子類別共享共同欄位與部分實作
```

### 正確答案方向

你的回答正確。

更完整版本：

```text
interface 主要用來定義行為契約或能力。一個 class 可以 implements 多個 interface。
interface 不能持有一般 instance state，也沒有 constructor。

abstract class 是 class，所以一個 class 只能 extends 一個 abstract class。
它可以有 instance field、constructor、一般方法與 abstract method。
適合用在多個子類別共享共同狀態或部分實作的情境。
```

### 詳解

核心差異是：

| 項目        | interface                 | abstract class               |
| ----------- | ------------------------- | ---------------------------- |
| 多重能力    | 可以 implements 多個      | 只能 extends 一個            |
| 狀態        | 不能有一般 instance field | 可以有 instance field        |
| constructor | 沒有                      | 有                           |
| 用途        | 定義能力、契約            | 共享共同基底、狀態、部分實作 |

### 本題觀念

- interface = contract
- abstract class = shared base
- Java 單一類別繼承

### 面試高頻

非常高。

---

## Q22

### 完整問題

為什麼實作 interface 的方法通常必須寫 `public`？

請解釋 Java 的存取權限規則。

### 你的回答

```text
因為 interface 的方法預設是 abstract public。
而一般類的方法是 package-private，沒有明確複寫的話會衝突編譯失敗。
```

### 正確答案方向

你的回答正確。

小修正：

```text
abstract public
```

慣用順序通常寫：

```text
public abstract
```

### 詳解

interface 裡的方法：

```java
void run();
```

其實是：

```java
public abstract void run();
```

實作類別如果寫：

```java
void run() {}
```

這是 package-private，可見性比 public 小。

Java 規定覆寫或實作方法時，不能降低存取權限。

所以必須寫：

```java
@Override
public void run() {}
```

### 本題觀念

- public abstract
- package-private
- overriding visibility rule

### 面試高頻

高。

---

## Q23

### 完整問題

請說明這段程式的設計重點：

```java
interface PaymentProcessor {
    void pay(int amount);
}

class CreditCardPaymentProcessor implements PaymentProcessor {
    @Override
    public void pay(int amount) {
        System.out.println("Credit card pay: " + amount);
    }
}

class LinePayPaymentProcessor implements PaymentProcessor {
    @Override
    public void pay(int amount) {
        System.out.println("LINE Pay: " + amount);
    }
}

class CheckoutService {
    void checkout(PaymentProcessor processor) {
        processor.pay(1000);
    }
}
```

問題：

```text
為什麼 CheckoutService 不直接依賴 CreditCardPaymentProcessor？
這樣設計有什麼好處？
```

### 你的回答

```text
因為這樣你就不需要去動 CheckoutService。
如果之後有新的方法新增，或者舊的方法刪除，這樣做就不需要再多寫一個 CheckoutService。
這樣做可以降低耦合。
```

### 你的答案判斷

大方向正確，但有一點要修。

正確的是：

```text
新增新的付款實作時，不需要改 CheckoutService。
```

例如新增：

```java
class ApplePayPaymentProcessor implements PaymentProcessor {}
```

`CheckoutService` 不用改。

但如果你改的是 `PaymentProcessor` 這個 interface 本身，例如新增抽象方法或刪除方法，那所有實作類別都可能要改。這點不能說完全不需要動。

### 正確答案方向

`CheckoutService` 不直接依賴 `CreditCardPaymentProcessor`，而是依賴 `PaymentProcessor`，好處是：

```text
1. 降低耦合。
2. 可以替換付款方式。
3. 可以新增付款方式而不修改 CheckoutService。
4. 測試時可以傳入假的 PaymentProcessor。
5. CheckoutService 只關心「能 pay」，不關心具體怎麼 pay。
```

### 詳解

高耦合版本：

```java
class CheckoutService {
    void checkout(CreditCardPaymentProcessor processor) {
        processor.pay(1000);
    }
}
```

這樣 `CheckoutService` 被信用卡付款綁死。

低耦合版本：

```java
class CheckoutService {
    void checkout(PaymentProcessor processor) {
        processor.pay(1000);
    }
}
```

只要是 `PaymentProcessor`，都可以傳入。

### 本題觀念

- 依賴抽象，不依賴具體實作。
- 低耦合（Low Coupling / 低耦合）
- 策略模式（Strategy Pattern / 策略模式）

### 面試高頻

非常高。

這是 Spring DI、Service 設計、可測試性設計的基礎。

---

## Q24

### 完整問題

請判斷下面這個設計適不適合 interface，並說明原因。

```java
interface UserData {
    String email = "test@example.com";
    String name = "Zane";
}
```

問題：

```text
這樣寫有什麼問題？
現代 Java 比較好的做法可能是什麼？
```

### 你的回答

```text
不適合 interface
如果有需要共用 field 應該考慮 enum 或者 abstract class
```

### 你的答案判斷

部分正確。

你判斷「不適合 interface」是正確的。

但「enum 或 abstract class」要更精準：

- `enum` 適合固定有限選項，例如 `UserRole.ADMIN`、`OrderStatus.PAID`
- `abstract class` 適合共享共同欄位與部分實作
- 但 `email`、`name` 這種使用者資料，最適合的是普通 class 或 record，不是 enum，也通常不是 abstract class

### 正確答案方向

這樣寫的問題：

```text
1. interface 欄位預設是 public static final。
2. email 和 name 會變成全域常數，不是每個使用者自己的資料。
3. interface 的目的不是存使用者資料。
4. 使用者資料應該放在 class / record / entity / DTO 裡。
```

比較好的做法：

```java
class UserData {
    private final String email;
    private final String name;

    UserData(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
```

或 Java record：

```java
record UserData(String email, String name) {
}
```

Spring Boot / backend 情境中，可能是：

```java
class UserResponse {
    private String email;
    private String name;
}
```

### 本題觀念

- interface field 是常數，不是物件資料。
- 使用者資料應該用 class、record、DTO、entity。
- enum 用於固定選項，不適合一般使用者資料。

### 面試高頻

中。

但在 code review 裡很常被指出。

---

## Q25

### 完整問題

請用簡短程式碼寫出：

一個 `NotificationSender` interface，並有兩個實作類別：

```text
EmailSender
SmsSender
```

再寫一個 `NotificationService`，它不要依賴具體的 `EmailSender` 或 `SmsSender`，而是依賴 `NotificationSender`。

條件：

- `NotificationSender` 有 `void send(String message)`
- `EmailSender` 輸出 `Email: message`
- `SmsSender` 輸出 `SMS: message`
- `NotificationService` 裡面有 `notifyUser(String message)` 方法

### 你的回答

```java
interface NotificationSender {
 void send(String message);
}

public EmailSender implements NotificationSender {
@Override
 public  void send(String message){
  System.out.println("Email:" + message);
}

public SmsSender implements NotificationSender {
@Override
 public  void send(String message){
  System.out.println("SMS:" + message);
}

class NotificationService{
    void notifyUser(NotificationSender sender) {
        sender.send(message);
    }
}
```

### 你的答案判斷

概念方向正確，但 Java 語法不會編譯。

主要問題：

```text
1. 少了 class 關鍵字。
2. public EmailSender implements ... 是錯的。
3. 大括號沒有關完。
4. notifyUser(String message) 條件沒有做到。
5. NotificationService 裡用了 message，但方法參數沒有 message。
6. NotificationService 應該依賴 NotificationSender 作為欄位或建構子參數，而不是每次 notifyUser 才傳 sender。
```

### 正確寫法

```java
interface NotificationSender {
    void send(String message);
}

class EmailSender implements NotificationSender {
    @Override
    public void send(String message) {
        System.out.println("Email: " + message);
    }
}

class SmsSender implements NotificationSender {
    @Override
    public void send(String message) {
        System.out.println("SMS: " + message);
    }
}

class NotificationService {
    private final NotificationSender sender;

    NotificationService(NotificationSender sender) {
        this.sender = sender;
    }

    void notifyUser(String message) {
        sender.send(message);
    }
}
```

使用：

```java
public class Main {
    public static void main(String[] args) {
        NotificationService emailService =
                new NotificationService(new EmailSender());

        emailService.notifyUser("Hello");

        NotificationService smsService =
                new NotificationService(new SmsSender());

        smsService.notifyUser("Hello");
    }
}
```

輸出：

```text
Email: Hello
SMS: Hello
```

### 為什麼這樣寫比較符合題目？

題目說：

```text
NotificationService 不依賴 EmailSender 或 SmsSender，
而是依賴 NotificationSender。
```

所以 `NotificationService` 應該長這樣：

```java
private final NotificationSender sender;
```

這樣 service 本身只知道「sender 可以 send」，不關心實際是 Email 還是 SMS。

### 本題觀念

- interface 作為依賴型別
- constructor injection baseline
- service 依賴抽象
- Java class 語法完整性

### 面試高頻

高。

尤其 Spring Boot 會大量用這種依賴注入思維。

---

# Part E：加分題

---

## Q26

### 完整問題

Adapter Pattern 題

假設你有一個舊系統類別：

```java
class LegacyEmailClient {
    void deliver(String title, String body) {
        System.out.println(title + ": " + body);
    }
}
```

但你的新系統只認這個 interface：

```java
interface NotificationSender {
    void send(String message);
}
```

請寫一個：

```java
class EmailNotificationAdapter implements NotificationSender
```

讓 `LegacyEmailClient` 可以被當成 `NotificationSender` 使用。

### 你的回答

```text
詳細不記得，但大概就是寫一個 adapter 把 LegacyEmailClient 注入到這個 adapter 裡，再實作這個 interface。
```

### 你的答案判斷

概念正確。

你抓到 adapter 的核心：

```text
把舊類別包起來，對外提供新 interface。
```

### 正確程式碼

```java
class LegacyEmailClient {
    void deliver(String title, String body) {
        System.out.println(title + ": " + body);
    }
}

interface NotificationSender {
    void send(String message);
}

class EmailNotificationAdapter implements NotificationSender {
    private final LegacyEmailClient legacyEmailClient;

    EmailNotificationAdapter(LegacyEmailClient legacyEmailClient) {
        this.legacyEmailClient = legacyEmailClient;
    }

    @Override
    public void send(String message) {
        legacyEmailClient.deliver("Notification", message);
    }
}
```

使用：

```java
public class Main {
    public static void main(String[] args) {
        LegacyEmailClient legacyClient = new LegacyEmailClient();

        NotificationSender sender =
                new EmailNotificationAdapter(legacyClient);

        sender.send("Your code is 123456");
    }
}
```

輸出：

```text
Notification: Your code is 123456
```

### 詳解

`LegacyEmailClient` 原本的方法是：

```java
deliver(String title, String body)
```

新系統要的是：

```java
send(String message)
```

兩邊方法長得不一樣，所以不能直接把 `LegacyEmailClient` 當作 `NotificationSender`。

Adapter 的責任就是轉換：

```text
send(message)
→ legacyEmailClient.deliver("Notification", message)
```

### 本題觀念

- Adapter Pattern（轉接器模式 / 适配器模式）
- 包裝舊系統
- 對外提供新介面

### 面試高頻

中高。

實務高頻，尤其接第三方 SDK、舊系統、外部服務時很常見。

---

## Q27

### 完整問題

面試題：

```text
Java 8 之後 interface 有 default method，
那 interface 是否可以完全取代 abstract class？
為什麼？
```

### 你的回答

```text
不行，主要還是看用途。
interface 不是萬能的，都寫成 interface 有時候還可能讓程式碼更複雜更高耦合。

interface 多重實作 / 想定義一種能力或契約，讓不同類別各自實作
abstract class 單一繼承 / 狀態 shared state / constructor / 多個子類別共享共同欄位與部分實作
```

### 你的答案判斷

正確。

小修正：

```text
都寫成 interface 不一定會更高耦合，
但可能造成過早抽象、類別數變多、閱讀成本變高。
```

### 更完整回答

不能完全取代。

Java 8 的 default method 讓 interface 可以提供部分預設行為，但 interface 仍然不能完全取代 abstract class，因為：

```text
1. interface 不能持有一般 instance field。
2. interface 沒有 constructor。
3. abstract class 可以共享狀態與部分實作。
4. interface 適合定義能力或契約。
5. abstract class 適合表達共同基底與共用狀態。
```

### 範例

interface 適合：

```java
interface PaymentProcessor {
    void pay(int amount);
}
```

abstract class 適合：

```java
abstract class BaseEntity {
    private Long id;

    public Long getId() {
        return id;
    }
}
```

### 本題觀念

- default method 的限制
- interface vs abstract class
- shared state
- constructor

### 面試高頻

非常高。

---

## Q28

### 完整問題

設計判斷題

你現在正在寫會員系統，裡面有一個 `PasswordEncoder`。

請判斷以下兩種設計哪個比較好，並說明原因。

設計 A：

```java
class AuthService {
    private final BcryptPasswordEncoder passwordEncoder;
}
```

設計 B：

```java
class AuthService {
    private final PasswordEncoder passwordEncoder;
}
```

其中：

```java
interface PasswordEncoder {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
```

問題：

```text
哪個比較低耦合？
哪個比較容易測試？
哪個比較容易替換實作？
```

### 你的回答

```text
設計 B
哪個比較低耦合？設計 B
哪個比較容易測試？如果你結構簡單 A，複雜的話理論上是 B
哪個比較容易替換實作？B
```

### 你的答案判斷

大方向正確。

但「哪個比較容易測試」這題，在這個情境下更精準答案是：B。

A 在小程式裡看起來比較直接，但不是比較容易測試。
B 可以注入假的 `PasswordEncoder`，所以單元測試更容易。

### 正確答案

| 問題                   | 答案 |
| ---------------------- | ---- |
| 哪個比較低耦合？       | B    |
| 哪個比較容易測試？     | B    |
| 哪個比較容易替換實作？ | B    |

### 詳解

設計 A：

```java
class AuthService {
    private final BcryptPasswordEncoder passwordEncoder;
}
```

問題是 `AuthService` 綁死 `BcryptPasswordEncoder`。

如果未來要換成：

```text
Argon2PasswordEncoder
Pbkdf2PasswordEncoder
FakePasswordEncoder for test
```

`AuthService` 可能就要改。

設計 B：

```java
class AuthService {
    private final PasswordEncoder passwordEncoder;
}
```

`AuthService` 只依賴介面。

它只知道：

```text
passwordEncoder 可以 encode()
passwordEncoder 可以 matches()
```

不關心底層是 bcrypt、argon2、測試假的 encoder，還是其他實作。

### 測試例子

```java
class FakePasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(String rawPassword) {
        return "encoded-" + rawPassword;
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return encodedPassword.equals("encoded-" + rawPassword);
    }
}
```

測試時可以注入：

```java
AuthService authService =
        new AuthService(new FakePasswordEncoder());
```

這比直接綁死 `BcryptPasswordEncoder` 更容易測試。

### 但要補一個實務判斷

不是所有 class 都一定要先抽 interface。

如果某個東西：

```text
只有一個實作
不會替換
不需要 mock
沒有外部依賴
```

那可以先用 concrete class。

但 `PasswordEncoder` 這個例子適合 interface，因為它天然就是可替換策略：

```text
bcrypt
argon2
pbkdf2
fake encoder for test
```

### 本題觀念

- 依賴反轉（Dependency Inversion / 依赖倒置）
- 依賴抽象，不依賴具體實作
- 可測試性
- 可替換性
- 策略模式

### 面試高頻

非常高。

Spring Boot / backend 面試常會用這種題目看你是否理解 DI 與 interface 的價值。

---

# 錯題與需要修正的重點

## 1. Q13：abstract class 可以有一般方法

你答錯。

請記住：

```java
abstract class Animal {
    abstract void makeSound();

    void sleep() {
        System.out.println("sleep");
    }
}
```

這是合法的。

abstract class 不是「所有方法都要 abstract」。

---

## 2. Q18：輸出要追到實際覆寫方法

你答：

```text
Welcome
```

正確是：

```text
SMS: Welcome
```

因為 default method 裡呼叫：

```java
send("Welcome");
```

最後會動態綁定到：

```java
SmsNotifier.send("Welcome")
```

---

## 3. Q24：UserData 不該用 interface，也不一定是 enum / abstract class

`email`、`name` 是每個 user 自己的資料。

比較合理：

```java
record UserData(String email, String name) {}
```

或：

```java
class UserData {
    private String email;
    private String name;
}
```

enum 適合固定狀態，例如：

```java
enum UserRole {
    USER,
    ADMIN
}
```

---

## 4. Q25：觀念對，但語法要修

你目前寫法不能編譯。

最小正確版要長這樣：

```java
interface NotificationSender {
    void send(String message);
}

class EmailSender implements NotificationSender {
    @Override
    public void send(String message) {
        System.out.println("Email: " + message);
    }
}

class SmsSender implements NotificationSender {
    @Override
    public void send(String message) {
        System.out.println("SMS: " + message);
    }
}

class NotificationService {
    private final NotificationSender sender;

    NotificationService(NotificationSender sender) {
        this.sender = sender;
    }

    void notifyUser(String message) {
        sender.send(message);
    }
}
```
