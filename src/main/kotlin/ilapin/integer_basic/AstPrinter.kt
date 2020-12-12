package ilapin.integer_basic

import java.lang.StringBuilder

/**
 * @author igorlapin on 11/12/2020.
 */

fun main() {
    val expression = Expression.Binary(
            Expression.Unary(
                    Token(TokenType.MINUS, "-", null, 1),
                    Expression.Literal(123)
            ),
            Token(TokenType.STAR, "*", null, 1),
            Expression.Grouping(Expression.Literal(45))
    )

    println(AstPrinter().print(expression))
}

class AstPrinter : Expression.Visitor<String> {

    fun print(expression: Expression): String {
        return expression.accept(this)
    }

    override fun visitBinaryExpression(expression: Expression.Binary): String {
        return parenthesize(expression.operator.lexeme, expression.left, expression.right)
    }

    override fun visitGroupingExpression(expression: Expression.Grouping): String {
        return parenthesize("group", expression.expression)
    }

    override fun visitLiteralExpression(expression: Expression.Literal): String {
        return expression.value?.toString() ?: "null"
    }

    override fun visitUnaryExpression(expression: Expression.Unary): String {
        return parenthesize(expression.operator.lexeme, expression.right)
    }

    override fun visitVariableExpression(expression: Expression.Variable): String {
        return expression.name.lexeme
    }

    private fun parenthesize(name: String, vararg expressions: Expression): String {
        val sb = StringBuilder()

        sb.append("(").append(name)
        expressions.forEach {
            sb.append(" ")
            sb.append(it.accept(this))
        }
        sb.append(")")

        return sb.toString()
    }
}