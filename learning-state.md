# Java Backend Learning State Snapshot

Last Update:
2026-04-02

---

# CURRENT POSITION

CURRENT_PHASE = Phase 0 — Java Syntax Foundation
CURRENT_MODULE = if / switch (next)

---

# Phase 0 Progress

primitive types DONE
variables DONE
stack model DONE
naming style DONE
char vs String DONE

operators DONE
if / switch TODO
loops TODO
methods TODO
arrays TODO
class basics TODO

---

# Checkpoints

declare_variable PASS
int_vs_double_difference PASS
char_literal_rule PASS
program_execution_success PASS
understand_stack_baseline PASS

arithmetic_evaluation PASS
comparison_evaluation PASS
logical_expression_evaluation PASS
operator_precedence_awareness PASS

write_if_statement TODO
write_for_loop TODO
write_method TODO
define_class TODO

---

# Knowledge Notes

char:
single Unicode character (16-bit)
primitive type
stored directly on stack (when local variable)

String:
object type (not primitive)
stored on heap
variable stores reference on stack
immutable

stack stores:
primitive values
method frames
object references

heap stores:
objects
String instances
arrays

expression evaluation model:
operator precedence determines evaluation order
comparison operators return boolean
logical operators use short-circuit evaluation
int / int → int

logical short-circuit rule:
false && expression → expression not executed
true || expression → expression not executed

---

# Next Session Target

if statement syntax
boolean decision flow
branch execution model
nested if
comparison + logical combination patterns
backend validation-style conditions