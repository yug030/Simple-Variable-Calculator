# Simple-Variable-Calculator
A simple calculator that can take an equation as a String, separate different variables and perform calculations based on different values provided.

Currently it is only in primitive type:
1. Can calculate int-only expressions. For example, given input "5(1+3)(2\*3+1)+6*5" will give the correct output 170.
2. Can separate most of the variables correctly. For example, given input "5a(b+1)+x-y/3" will prompt the user to enter value for a, b, x and y.
(currently an exception will be thrown here, and I'm trying to fix it)
