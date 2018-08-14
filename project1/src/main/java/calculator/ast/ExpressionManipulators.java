package calculator.ast;

import calculator.errors.EvaluationError;
import calculator.gui.ImageDrawer;
import calculator.interpreter.Environment;
import datastructures.concrete.DoubleLinkedList;
//import datastructures.concrete.dictionaries.ArrayDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;

/**
 * All of the public static methods in this class are given the exact same
 * parameters for consistency. You can often ignore some of these parameters
 * when implementing your methods. Some of these methods should be recursive.
 * You may want to consider using public-private pairs in some cases.
 */
public class ExpressionManipulators {
    /**
     * Checks to make sure that the given node is an operation AstNode with the
     * expected name and number of children. Throws an EvaluationError otherwise.
     */
    private static void assertNodeMatches(AstNode node, String expectedName, int expectedNumChildren) {
        if (!node.isOperation() && !node.getName().equals(expectedName)
                && node.getChildren().size() != expectedNumChildren) {
            throw new EvaluationError("Node is not valid " + expectedName + " node.");
        }
    }

    /**
     * Accepts an 'toDouble(inner)' AstNode and returns a new node containing the
     * simplified version of the 'inner' AstNode.
     *
     * Preconditions:
     *
     * - The 'node' parameter is an operation AstNode with the name 'toDouble'. -
     * The 'node' parameter has exactly one child: the AstNode to convert into a
     * double.
     *
     * Postconditions:
     *
     * - Returns a number AstNode containing the computed double.
     *
     * For example, if this method receives the AstNode corresponding to 'toDouble(3
     * + 4)', this method should return the AstNode corresponding to '7'.
     * 
     * This method is required to handle the following binary operations +, -, *, /,
     * ^ (addition, subtraction, multiplication, division, and exponentiation,
     * respectively) and the following unary operations negate, sin, cos
     *
     * @throws EvaluationError
     *             if any of the expressions contains an undefined variable.
     * @throws EvaluationError
     *             if any of the expressions uses an unknown operation.
     */
    public static AstNode handleToDouble(Environment env, AstNode node) {
        // To help you get started, we've implemented this method for you.
        // You should fill in the locations specified by "your code here"
        // in the 'toDoubleHelper' method.
        //
        // If you're not sure why we have a public method calling a private
        // recursive helper method, review your notes from CSE 143 (or the
        // equivalent class you took) about the 'public-private pair' pattern.

        assertNodeMatches(node, "toDouble", 1);
        AstNode exprToConvert = node.getChildren().get(0);
        return new AstNode(toDoubleHelper(env.getVariables(), exprToConvert));
    }

    private static double toDoubleHelper(IDictionary<String, AstNode> variables, AstNode node) {
        // There are three types of nodes, so we have three cases.
        if (node.isNumber()) {
            return node.getNumericValue();
        } else if (node.isVariable()) {
            if (variables.containsKey(node.getName())) {

                // call the function again, three variables to be used
                // to replace return variables.get(node.getName()).getNumericValue();
                return toDoubleHelper(variables, variables.get(node.getName()));
            } else {
                throw new EvaluationError(String.format("variable '%s' is not defined.", node.getName()));

            }
        } else {

            // node.isOperation() == true
            // You may assume the expression node has the correct number of children.
            // If you wish to make your code more robust, you can also use the provided
            // "assertNodeMatches" method to verify the input is valid.
            String operator = node.getName();

            // +, -, *, /, ^
            if (operator.equalsIgnoreCase("+") || operator.equalsIgnoreCase("-") || operator.equalsIgnoreCase("*")
                    || operator.equalsIgnoreCase("/") || operator.equalsIgnoreCase("^")) {

                AstNode leftExprToEvaluate = node.getChildren().get(0);
                AstNode rightExprToEvaluate = node.getChildren().get(1);

                double leftValue = toDoubleHelper(variables, leftExprToEvaluate);
                double rightValue = toDoubleHelper(variables, rightExprToEvaluate);

                if (operator.equalsIgnoreCase("+")) {
                    return leftValue + rightValue;

                } else if (operator.equalsIgnoreCase("-")) {
                    return leftValue - rightValue;

                } else if (operator.equalsIgnoreCase("*")) {
                    return leftValue * rightValue;

                } else if (operator.equalsIgnoreCase("/")) {
                    return leftValue / rightValue;

                } else if (operator.equalsIgnoreCase("^")) {
                    return Math.pow(leftValue, rightValue);

                }

                throw new EvaluationError(String.format("operation '%s' is not defined.", operator));

            } else if (operator.equalsIgnoreCase("negate") || operator.equalsIgnoreCase("sin")
                    || operator.equalsIgnoreCase("cos")) {

                AstNode exprToEvaluate = node.getChildren().get(0);
                double val = toDoubleHelper(variables, exprToEvaluate);

                if (operator.equalsIgnoreCase("negate")) {
                    return -1 * val;

                } else if (operator.equalsIgnoreCase("sin")) {
                    return Math.sin(val);

                } else if (operator.equalsIgnoreCase("cos")) {
                    return Math.cos(val);
                }
                throw new EvaluationError(String.format("operation '%s' is not defined.", operator));
            }

            throw new EvaluationError(String.format("operation '%s' is not defined.", operator));
        }
    }

    /**
     * Accepts a 'simplify(inner)' AstNode and returns a new node containing the
     * simplified version of the 'inner' AstNode.
     *
     * Preconditions:
     *
     * - The 'node' parameter is an operation AstNode with the name 'simplify'. -
     * The 'node' parameter has exactly one child: the AstNode to simplify
     *
     * Postconditions:
     *
     * - Returns an AstNode containing the simplified inner parameter.
     *
     * For example, if we received the AstNode corresponding to the expression
     * "simplify(3 + 4)", you would return the AstNode corresponding to the number
     * "7".
     *
     * Note: there are many possible simplifications we could implement here, but
     * you are only required to implement a single one: constant folding.
     *
     * That is, whenever you see expressions of the form "NUM + NUM", or "NUM -
     * NUM", or "NUM * NUM", simplify them.
     */
    public static AstNode handleSimplify(Environment env, AstNode node) {
        // Try writing this one on your own!
        // Hint 1: Your code will likely be structured roughly similarly
        // to your "handleToDouble" method
        // Hint 2: When you're implementing constant folding, you may want
        // to call your "handleToDouble" method in some way
        // Hint 3: When implementing your private pair, think carefully about
        // when you should recurse. Do you recurse after simplifying
        // the current level? Or before?

        assertNodeMatches(node, "simplify", 1);
        AstNode exprToSimplify = node.getChildren().get(0);
        return simplifyHelper(env.getVariables(), exprToSimplify);
    }

    private static AstNode simplifyHelper(IDictionary<String, AstNode> variables, AstNode node) {
        // There are three types of nodes, so we have three cases.
        if (node.isVariable()) {
            if (variables.containsKey(node.getName())) {
                if (variables.get(node.getName()).isNumber()) {
                    return new AstNode(variables.get(node.getName()).getNumericValue());
                } else {

                    AstNode n = simplifyHelper(variables, variables.get(node.getName()));
                    if (n.isNumber()) {
                        return new AstNode(n.getNumericValue());
                    }
                    return n;
                }
            }
            return node;
        } else if (node.isOperation()) {
            // node.isOperation() == true
            // You may assume the expression node has the correct number of children.
            // If you wish to make your code more robust, you can also use the provided
            // "assertNodeMatches" method to verify the input is valid.

            String operator = node.getName();
            // +, -, *, /, ^
            if (operator.equalsIgnoreCase("+") || operator.equalsIgnoreCase("-") || operator.equalsIgnoreCase("*")
                    || operator.equalsIgnoreCase("/") || operator.equalsIgnoreCase("^")) {
                // call the method itself to generate children

                return mathOperation(variables, node, operator);

            } else if (operator.equalsIgnoreCase("negate") || operator.equalsIgnoreCase("sin")
                    || operator.equalsIgnoreCase("cos")) {

                AstNode expr = simplifyHelper(variables, node.getChildren().get(0));

                node.getChildren().set(0, expr);
                return node;
            }
            return node;

        } else {
            return node;
        }
    }

    private static AstNode mathOperation(IDictionary<String, AstNode> variables, AstNode node, String operator) {
        AstNode leftExpr = simplifyHelper(variables, node.getChildren().get(0));
        AstNode rightExpr = simplifyHelper(variables, node.getChildren().get(1));

        if (leftExpr.isNumber() && rightExpr.isNumber()) {

            double leftValue;
            double rightValue;
            leftValue = leftExpr.getNumericValue();
            rightValue = rightExpr.getNumericValue();

            if (operator.equalsIgnoreCase("+")) {
                return new AstNode(leftValue + rightValue);
            } else if (operator.equalsIgnoreCase("-")) {
                return new AstNode(leftValue - rightValue);
            } else if (operator.equalsIgnoreCase("*")) {
                return new AstNode(leftValue * rightValue);
            } else if (operator.equalsIgnoreCase("/")) {
                node.getChildren().set(0, leftExpr);
                node.getChildren().set(1, rightExpr);
                return node;

            } else if (operator.equalsIgnoreCase("^")) {
                return new AstNode(Math.pow(leftValue, rightValue));
            }
        } else {
            node.getChildren().set(0, leftExpr);
            node.getChildren().set(1, rightExpr);
            return node;
        }

        return node;
    }

    /**
     * Accepts an Environment variable and a 'plot(exprToPlot, var, varMin, varMax,
     * step)' AstNode and generates the corresponding plot on the ImageDrawer
     * attached to the environment. Returns some arbitrary AstNode.
     *
     * Example 1:
     *
     * >>> plot(3 * x, x, 2, 5, 0.5)
     *
     * This method will receive the AstNode corresponding to 'plot(3 * x, x, 2, 5,
     * 0.5)'. Your 'handlePlot' method is then responsible for plotting the equation
     * "3 * x", varying "x" from 2 to 5 in increments of 0.5.
     *
     * In this case, this means you'll be plotting the following points:
     *
     * [(2, 6), (2.5, 7.5), (3, 9), (3.5, 10.5), (4, 12), (4.5, 13.5), (5, 15)]
     *
     * ---
     *
     * Another example: now, we're plotting the quadratic equation "a^2 + 4a + 4"
     * from -10 to 10 in 0.01 increments. In this case, "a" is our "x" variable.
     *
     * >>> c := 4 4 >>> step := 0.01 0.01 >>> plot(a^2 + c*a + a, a, -10, 10, step)
     *
     * ---
     *
     * @throws EvaluationError
     *             if any of the expressions contains an undefined variable.
     * @throws EvaluationError
     *             if varMin > varMax
     * @throws EvaluationError
     *             if 'var' was already defined
     * @throws EvaluationError
     *             if 'step' is zero or negative
     */

    public static AstNode plot(Environment env, AstNode node) {

        assertNodeMatches(node, "plot", 5);

        IDictionary<String, AstNode> vars = env.getVariables();

        // IDictionary<String, AstNode> tempVar = new ArrayDictionary();

        AstNode exprToPlot = node.getChildren().get(0);
        AstNode var = node.getChildren().get(1);
        AstNode varMin = node.getChildren().get(2);
        AstNode varMax = node.getChildren().get(3);
        AstNode step = node.getChildren().get(4);

        if (vars.containsKey(var.getName())) {
            throw new EvaluationError("'var' was already defined.");
        }

        if (toDoubleHelper(vars, varMin) > toDoubleHelper(vars, varMax)) {
            throw new EvaluationError("varMin > varMax");
        }

        if (toDoubleHelper(vars, step) <= 0) {
            throw new EvaluationError("'step' is zero or negative");
        }

        IList<Double> xvalues = new DoubleLinkedList<Double>();
        IList<Double> yvalues = new DoubleLinkedList<Double>();

        for (double i = toDoubleHelper(vars, varMin); i <= toDoubleHelper(vars, varMax); i += toDoubleHelper(vars,
                step)) {

            AstNode temp = new AstNode(i);
            vars.put(var.getName(), temp);
            double plot = toDoubleHelper(vars, exprToPlot);
            xvalues.add(i);
            yvalues.add(plot);
        }

        // Clean up the temp var
        vars.remove(var.getName());

        ImageDrawer drawer = env.getImageDrawer();
        drawer.drawScatterPlot("plot", "xAxis", "yAxis", xvalues, yvalues);

        // Note: every single function we add MUS T return an
        // AST node that your "simplify" function is capable of handling.
        // However, your "simplify" function doesn't really know what to do
        // with "plot" functions (and what is the "plot" function supposed to
        // evaluate to anyways?) so we'll settle for just returning an
        // arbitrary number.
        //
        // When working on this method, you should uncomment the following line:

        return new AstNode(1);
    }

    // extra credit: solve(equal(object))
    /**
     * 
     * Solve the extra credit problem
     * 
     * @param env
     * @param node
     * @return
     */
    public static AstNode solve(Environment env, AstNode node) {

        assertNodeMatches(node, "solve", 2);
        AstNode expNode = node.getChildren().get(0);
        AstNode var = node.getChildren().get(1);

        if (!expNode.isOperation() || !expNode.getName().equalsIgnoreCase("equal")) {
            throw new EvaluationError(String.format("expect solve(equal(...), ...)  "));
        }

        if (env.getVariables().containsKey(var.getName())) {
            throw new EvaluationError("'var' was already defined.");
        }

        AstNode leftNode = expNode.getChildren().get(0);
        AstNode rightNode = expNode.getChildren().get(1);

        // make the range for running
        int minRange = -1000;
        int maxRange = 1000;
        for (int i = minRange; i <= maxRange; i++) {

            AstNode temp = new AstNode(i);
            env.getVariables().put(var.getName(), temp);

            if (toDoubleHelper(env.getVariables(), leftNode) == toDoubleHelper(env.getVariables(), rightNode)) {
                env.getVariables().remove(var.getName());
                return temp;
            }
        }
        throw new EvaluationError(String.format("Unable to solve. Range is %d to %d", minRange, maxRange));
    }
}
