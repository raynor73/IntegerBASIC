package ilapin.integer_basic

/**
 * @author igorlapin on 11/12/2020.
 */
sealed class Expression {

    class Binary(val left: Expression, val operator: Token, val right: Expression) : Expression() {

        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitBinaryExpression(this)
    }

    class Grouping(val expression: Expression) : Expression() {

        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitGroupingExpression(this)
    }

    class Literal(val value: Any?) : Expression() {

        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitLiteralExpression(this)
    }

    class Unary(val operator: Token, val right: Expression) : Expression() {

        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitUnaryExpression(this)
    }

    class Variable(val name: Token) : Expression() {

        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitVariableExpression(this)
    }

    abstract fun <R> accept(visitor: Visitor<R>): R

    interface Visitor<R> {

        fun visitBinaryExpression(expression: Binary): R

        fun visitGroupingExpression(expression: Grouping): R

        fun visitLiteralExpression(expression: Literal): R

        fun visitUnaryExpression(expression: Unary): R

        fun visitVariableExpression(expression: Variable): R
    }
}