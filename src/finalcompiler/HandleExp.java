package finalcompiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class HandleExp {
    ArrayList<String> exp;
    Object result;
    HandleExp(ArrayList<String> exp) {
        this.exp = exp;
        this.result = evaluateExpression(exp);
    }

    HandleExp(String[] exp) {
        this(new ArrayList<>(Arrays.asList(exp)));
    }

    HandleExp(String exp) {
        this( Compiler.extractWords(exp));
    }



    // Method to evaluate the expression
    public Object evaluateExpression(ArrayList<String> exp) {
        // Handle variables and replace them with their values
        for (int i = 0; i < exp.size(); i++) {
            if (isVariable(exp.get(i))) {
                DataTypes var = getVariable(exp.get(i));
                if (var != null) {
                    exp.set(i, var.getValue());
                } else {
                    throw new RuntimeException("Variable " + exp.get(i) + " is not defined.");
                }
            }
        }

        // Convert infix expression to postfix for easier evaluation
        ArrayList<String> postfixExp = infixToPostfix(exp);
        return evaluatePostfix(postfixExp);
    }

    // Check if the token is a variable
    private boolean isVariable(String token) {
        return !isOperator(token) && !isNumber(token) && !isBoolean(token) && !token.equals("(") && !token.equals(")");
    }

    // Get the variable from the static list of defined variables
    private DataTypes getVariable(String name) {
        for (DataTypes dt : Compiler.dataTypes) {
            if (dt.getName().equals(name)) {
                return dt;
            }
        }
        return null;
    }

    // Convert infix expression to postfix
    private ArrayList<String> infixToPostfix(ArrayList<String> infix) {
        ArrayList<String> postfix = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        for (String token : infix) {
            if (isNumber(token) || isBoolean(token)) {
                postfix.add(token);
            } else if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    postfix.add(stack.pop());
                }
                stack.pop();
            } else if (isOperator(token)) {
                while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(token)) {
                    postfix.add(stack.pop());
                }
                stack.push(token);
            }
        }
        while (!stack.isEmpty()) {
            postfix.add(stack.pop());
        }
        return postfix;
    }

    // Evaluate postfix expression
    private Object evaluatePostfix(ArrayList<String> postfix) {
        Stack<Object> stack = new Stack<>();
        for (String token : postfix) {
            if (isNumber(token)) {
                if (token.contains(".")) {
                    stack.push(Double.parseDouble(token));
                } else {
                    stack.push(Integer.parseInt(token));
                }
            } else if (isBoolean(token)) {
                stack.push(Boolean.parseBoolean(token));
            } else if (isOperator(token)) {
                if (token.equals("not")) {
                    Object a = stack.pop();
                    stack.push(applyOperator(token, a, null));
                } else {
                    Object b = stack.pop();
                    Object a = stack.pop();
                    stack.push(applyOperator(token, a, b));
                }
            }
        }
        return stack.pop();
    }

    // Apply operator to operands
    private Object applyOperator(String operator, Object a, Object b) {
        switch (operator) {
            case "+":
                if (a instanceof String || b instanceof String) {
                    return a.toString() + b.toString();
                } else if (a instanceof Double || b instanceof Double) {
                    return ((Number) a).doubleValue() + ((Number) b).doubleValue();
                } else {
                    return ((Number) a).intValue() + ((Number) b).intValue();
                }
            case "-":
                if (a instanceof Double || b instanceof Double) {
                    return ((Number) a).doubleValue() - ((Number) b).doubleValue();
                } else {
                    return ((Number) a).intValue() - ((Number) b).intValue();
                }
            case "*":
                if (a instanceof Double || b instanceof Double) {
                    return ((Number) a).doubleValue() * ((Number) b).doubleValue();
                } else {
                    return ((Number) a).intValue() * ((Number) b).intValue();
                }
            case "/":
                if (a instanceof Double || b instanceof Double) {
                    return ((Number) a).doubleValue() / ((Number) b).doubleValue();
                } else {
                    return ((Number) a).intValue() / ((Number) b).intValue();
                }
            case "div":
                return ((Number) a).intValue() / ((Number) b).intValue();
            case "mod":
                return ((Number) a).intValue() % ((Number) b).intValue();
            case "=":
                return a.equals(b);
            case "<>":
                return !a.equals(b);
            case "<":
                if (a instanceof Double || b instanceof Double) {
                    return ((Number) a).doubleValue() < ((Number) b).doubleValue();
                } else {
                    return ((Number) a).intValue() < ((Number) b).intValue();
                }
            case ">":
                if (a instanceof Double || b instanceof Double) {
                    return ((Number) a).doubleValue() > ((Number) b).doubleValue();
                } else {
                    return ((Number) a).intValue() > ((Number) b).intValue();
                }
            case "<=":
                if (a instanceof Double || b instanceof Double) {
                    return ((Number) a).doubleValue() <= ((Number) b).doubleValue();
                } else {
                    return ((Number) a).intValue() <= ((Number) b).intValue();
                }
            case ">=":
                if (a instanceof Double || b instanceof Double) {
                    return ((Number) a).doubleValue() >= ((Number) b).doubleValue();
                } else {
                    return ((Number) a).intValue() >= ((Number) b).intValue();
                }
            case "and":
                return (Boolean) a && (Boolean) b;
            case "or":
                return (Boolean) a || (Boolean) b;
            case "xor":
                return (Boolean) a ^ (Boolean) b;
            case "not":
                return !(Boolean) a;
            default:
                throw new UnsupportedOperationException("Unknown operator: " + operator);
        }
    }

    // Check if the token is an operator
    private boolean isOperator(String token) {
        return token.matches("[+\\-*/=<>!&|^]|div|mod|and|or|xor|not|<=|>=|<>");
    }

    // Check if the token is a number
    private boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Check if the token is a boolean
    private boolean isBoolean(String token) {
        return token.equals("true") || token.equals("false");
    }

    // Get the precedence of the operator
    private int precedence(String operator) {
        switch (operator) {
            case "not":
                return 4;
            case "*":
            case "/":
            case "div":
            case "mod":
                return 3;
            case "+":
            case "-":
                return 2;
            case "=":
            case "<>":
            case "<":
            case ">":
            case "<=":
            case ">=":
                return 1;
            case "and":
                return 0;
            case "or":
            case "xor":
                return -1;
            default:
                return -1;
        }
    }


}
