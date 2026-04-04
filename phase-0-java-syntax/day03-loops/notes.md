Day 03 — Loops
Learned

loop types:

for
while
do-while

purpose:

repeat execution
iterate data
control flow automation

for Loop

structure:

for (initialization; condition; update) {
statement
}

example:

for (int i = 0; i < 5; i++) {
System.out.println(i);
}

execution order:

1 initialization
2 condition check
3 execute block
4 update
5 repeat step 2

while Loop

structure:

while (condition) {
statement
}

example:

int i = 0;

while (i < 5) {
System.out.println(i);
i++;
}

feature:

condition checked before execution

may execute zero times

do-while Loop

structure:

do {
statement
} while (condition);

example:

int i = 0;

do {
System.out.println(i);
i++;
} while (i < 5);

feature:

executes at least once

condition checked after execution

Loop Control Keywords

break:

exit loop immediately

example:

for (int i = 0; i < 10; i++) {
if (i == 5) {
break;
}
}

continue:

skip current iteration

example:

for (int i = 0; i < 5; i++) {
if (i == 2) {
continue;
}
}
Typical Backend Usage Patterns

counter iteration:

for (int i = 0; i < n; i++)

collection traversal (preview):

for (int i = 0; i < list.size(); i++)

input validation retry:

while (invalid)

menu loop:

while (running)
Stack Model (baseline)

loop variable:

stored in stack frame

example:

int i = 0;

lifecycle:

created at loop start
destroyed after loop ends

Common Mistakes

missing update expression:

while (i < 5)

causes:

infinite loop

wrong condition direction:

i > 5

loop never executes

semicolon misuse:

while (i < 5);

creates empty loop body

Program Output

example:

for (int i = 0; i < 3; i++) {
System.out.println(i);
}

output:

0
1
2
Status

DONE

## Interview Notes

for loop preferred when iteration count known

while loop preferred when condition-driven loop

do-while guarantees at least one execution

break exits loop immediately

continue skips current iteration

infinite loop caused by missing update condition