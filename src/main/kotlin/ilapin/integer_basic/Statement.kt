package ilapin.integer_basic

/**
 * @author igorlapin on 12/12/2020.
 */
sealed class Statement {

    class Print(val expression: ilapin.integer_basic.Expression) : Statement() {

        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitPrintStatement(this)
    }

    class Expression(val expression: ilapin.integer_basic.Expression) : Statement() {

        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitExpressionStatement(this)
    }

    class Variable(val name: Token, val initializer: ilapin.integer_basic.Expression) : Statement() {

        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitVariableStatement(this)
    }

    abstract fun <R> accept(visitor: Visitor<R>): R

    interface Visitor<R> {

        fun visitPrintStatement(statement: Print): R

        fun visitExpressionStatement(statement: Expression): R

        fun visitVariableStatement(statement: Variable): R
    }
}