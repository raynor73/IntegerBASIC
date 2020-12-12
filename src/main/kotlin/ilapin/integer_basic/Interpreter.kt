package ilapin.integer_basic

/**
 * @author igorlapin on 11/12/2020.
 */
class Interpreter : Expression.Visitor<Any?>, Statement.Visitor<Any?> {

    /*fun interpret(expression: Expression) {
        try {
            val value = evaluate(expression)
            println(stringify(value))
        } catch (e: RuntimeError) {
            IntegerBASIC.runtimeError(e)
        }
    }*/

    fun interpret(statements: List<Statement>) {
        try {
            statements.forEach { execute(it) }
        } catch (e: RuntimeError) {
            IntegerBASIC.runtimeError(e)
        }
    }

    private fun execute(statement: Statement) {
        statement.accept(this)
    }

    private fun stringify(value: Any?): String {
        return when (value) {
            null -> "null"
            is Int -> value.toString()
            else -> value.toString()
        }
    }

    override fun visitBinaryExpression(expression: Expression.Binary): Any? {
        val left = evaluate(expression.left)
        val right = evaluate(expression.right)

        return when (expression.operator.type) {
            TokenType.PLUS -> {
                checkNumberOperands(expression.operator, left, right)
                (left as Int) + (right as Int)
            }
            TokenType.MINUS -> {
                checkNumberOperands(expression.operator, left, right)
                (left as Int) - (right as Int)
            }
            TokenType.SLASH -> {
                checkNumberOperands(expression.operator, left, right)
                (left as Int) / (right as Int)
            }
            TokenType.STAR -> {
                checkNumberOperands(expression.operator, left, right)
                (left as Int) * (right as Int)
            }

            TokenType.GREATER -> {
                checkNumberOperands(expression.operator, left, right)
                if ((left as Int) > (right as Int)) 1 else 0
            }
            TokenType.GREATER_EQUAL -> {
                checkNumberOperands(expression.operator, left, right)
                if ((left as Int) >= (right as Int)) 1 else 0
            }
            TokenType.LESS -> {
                checkNumberOperands(expression.operator, left, right)
                if ((left as Int) < (right as Int)) 1 else 0
            }
            TokenType.LESS_EQUAL -> {
                checkNumberOperands(expression.operator, left, right)
                if ((left as Int) <= (right as Int)) 1 else 0
            }
            TokenType.LESS_GREATER -> {
                checkNumberOperands(expression.operator, left, right)
                if ((left as Int) != (right as Int)) 1 else 0
            }
            TokenType.EQUAL -> { // implement assignment
                checkNumberOperands(expression.operator, left, right)
                if ((left as Int) == (right as Int)) 1 else 0
            }

            else -> error("Some weird stuff happened during evaluating binary operator.")
        }
    }

    override fun visitGroupingExpression(expression: Expression.Grouping): Any? {
        return evaluate(expression.expression)
    }

    override fun visitLiteralExpression(expression: Expression.Literal): Any? {
        return expression.value
    }

    override fun visitUnaryExpression(expression: Expression.Unary): Any? {
        val right = evaluate(expression.right)

        return when (expression.operator.type) {
            TokenType.MINUS -> {
                checkNumberOperand(expression.operator, right)
                -(right as Int)
            }
            TokenType.NOT -> {
                checkNumberOperand(expression.operator, right)
                if ((right as Int) == 0) 1 else 0
            }

            else -> error("Some weird stuff happened during evaluating unary operator.")
        }
    }

    override fun visitVariableExpression(expression: Expression.Variable): Any? {
        TODO("Not yet implemented")
    }

    override fun visitPrintStatement(statement: Statement.Print): Any? {
        println(stringify(evaluate(statement.expression)))
        return null
    }

    override fun visitVariableStatement(statement: Statement.Variable): Any? {
        TODO("Not yet implemented")
    }
    private fun checkNumberOperand(operator: Token, operand: Any?) {
        if (operand is Int) {
            return
        } else {
            throw RuntimeError(operator, "Operand must be a number.")
        }
    }

    private fun checkNumberOperands(operator: Token, left: Any?, right: Any?) {
        if (left is Int && right is Int) {
            return
        } else {
            throw RuntimeError(operator, "Operands must be numbers.")
        }
    }

    private fun evaluate(expression: Expression): Any? {
        return expression.accept(this)
    }
}