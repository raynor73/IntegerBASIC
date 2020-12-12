package ilapin.integer_basic

import java.lang.RuntimeException

/**
 * @author igorlapin on 11/12/2020.
 */
class Parser(private val tokens: List<Token>) {

    private var current = 0

    /*fun parse(): Expression? {
        return try {
            expression()
        } catch (e: ParseError) {
            null
        }
    }*/
    fun parse(): List<Statement> {
        val statements = ArrayList<Statement>()

        while (!isAtEnd()) {
            statements.add(statement())
        }

        return statements
    }

    private fun statement(): Statement {
        val statement = when {
            match(TokenType.PRINT) -> printStatement()
            else -> error("Unhandled error #2")//expressionStatement()
        }

        if (!match(TokenType.EOL, TokenType.COLON)) {
            error("Unhandled error #1")
        }

        return statement
    }

    private fun printStatement(): Statement {
        return Statement.Print(expression())
    }

    private fun expression(): Expression {
        return equality()
    }

    private fun equality(): Expression {
        var expression = comparison()

        while (match(TokenType.LESS_GREATER, TokenType.EQUAL)) {
            val operator = previous()
            val right = comparison()
            expression = Expression.Binary(expression, operator, right)
        }

        return expression
    }

    private fun consume(type: TokenType, message: String): Token {
        if (check(type)) {
            return advance()
        }

        throw error(peek(), message)
    }

    private fun error(token: Token, message: String): ParseError {
        IntegerBASIC.error(token, message)
        return ParseError()
    }

    private fun synchronize() {
        advance()

        while (!isAtEnd()) {
            when (peek().type) {
                TokenType.FOR, TokenType.IF, TokenType.PRINT, TokenType.RETURN -> return
                else -> {}
            }

            advance()
        }
    }

    private fun primary(): Expression {
        return when {
            match(TokenType.NUMBER, TokenType.STRING) -> Expression.Literal(previous().literal)
            match(TokenType.LEFT_PAREN) -> {
                val expression = expression()
                consume(TokenType.RIGHT_PAREN, "Expect ')' after expression.")
                Expression.Grouping(expression)
            }
            else -> throw error(peek(), "Expect expression.")
        }
    }

    private fun unary(): Expression {
        return if (match(TokenType.NOT, TokenType.MINUS)) {
            val operator = previous()
            val right = unary()
            Expression.Unary(operator, right)
        } else {
            primary()
        }
    }

    private fun factor(): Expression {
        var expression = unary()

        while (match(TokenType.SLASH, TokenType.STAR)) {
            val operator = previous()
            val right = unary()
            expression = Expression.Binary(expression, operator, right)
        }

        return expression
    }

    private fun term(): Expression {
        var expression = factor()

        while (match(TokenType.MINUS, TokenType.PLUS)) {
            val operator = previous()
            val right = factor()
            expression = Expression.Binary(expression, operator, right)
        }

        return expression
    }

    private fun comparison(): Expression {
        var expression = term()

        while (match(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
            val operator = previous()
            val right = term()
            expression = Expression.Binary(expression, operator, right)
        }

        return expression
    }

    private fun match(vararg types: TokenType): Boolean {
        types.forEach { type ->
            if (check(type)) {
                advance()
                return true
            }
        }

        return false
    }

    private fun check(type: TokenType): Boolean {
        if (isAtEnd()) {
            return false
        }

        return peek().type == type
    }

    private fun advance(): Token {
        if (!isAtEnd()) {
            current++
        }

        return previous()
    }

    private fun isAtEnd(): Boolean {
        return peek().type == TokenType.EOF
    }

    private fun peek(): Token {
        return tokens[current]
    }

    private fun previous(): Token {
        return tokens[current - 1]
    }

    class ParseError : RuntimeException()
}