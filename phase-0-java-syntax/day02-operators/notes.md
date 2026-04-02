# Day 02 — Operators & Expression Evaluation

## Learned

arithmetic operators:

+
-
*
/
%

example:

3 + 5
10 - 2
4 * 6
10 / 3
10 % 3

difference:

int / int → int
example:

10 / 3 = 3

---

## Assignment Operators

assignment:

=

compound assignment:

+=
-=
*=
/=

example:

a = 5
a += 3
a -= 2

equivalent:

a += 3 → a = a + 3

---

## Comparison Operators

comparison operators return:

boolean

operators:

==
!=
>
<
>=
<=

example:

a > 5
score == 100
age != 18

---

## Logical Operators

logical operators:

&&
||
!

example:

age > 18 && age < 65

meaning:

greater than 18 AND less than 65

---

## Short-Circuit Evaluation

rule:

false && expression → expression not executed

true || expression → expression not executed

example:

user != null && user.isActive()

prevents:

NullPointerException

---

## Operator Precedence

evaluation order:

*
/
%

+
-

>
<
>=
<=

==
!=

&&

||

=

example:

3 + 5 * 2

evaluates as:

3 + (5 * 2)

---

## Expression Model

expression:

produces a value

example:

3 + 5
a * 2
x > 10
isReady && isValid

statement:

does not produce value

example:

int a = 5;

---

## Program Output

14
true

---

## Status

DONE

---

## Interview Notes

comparison operators return boolean

logical operators use short-circuit evaluation

int / int returns int

operator precedence affects evaluation order

&& commonly used for null-safety checks