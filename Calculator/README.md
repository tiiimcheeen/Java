# Calculator

A simple calculator. Can add, substract, multiply, divide, and negate. Will display in decimal forms, not fraction form. <br>
Can only do one operation before '='. For example, if pressing '1' '+' '2' '=', then will display '3'. However, if pressing '1' '+' '2' '+' '3' '=', 
then will display '5' instead of '6'.

# Calculator 2.0

Can handle multiple operations with the correct operator precedence. For example, if pressing '1' '+' '2' '*' '3' '=', then will display '7' correctly.<br>
Can also show an error message when encountering problems. For example, if pressing '1' '+' '+' '2' '=', then will display 'Error!'.

# Calculator 3.0

Can show more detailed error messages.<br>
If '=' is pressed without any values, then will display _Error: Input Required_.<br>
If the expression is incomplete, e.g. '1' '+' '2' '+' '=', then will display _Error: No Operand After -_.<br>
If an operator is wrongly placed, e.g. '1' '+' '/' '2' '=', then will display _Error: Invalid Char: -_.<br>
If dividing by 0, then will display _Error: Division by Zero_.<br>
If an operand has more than 1 decimals, e.g. '1.2.3' '+' '2' '=', then will display _Error: Extra Decimals_.
