/**
 * @author igorlapin on 10/12/2020.
 */
class Scanner(private val source: String) {

    private val tokens = ArrayList<Token>()

    private var start = 0
    private var current = 0
    private var line = 1

    fun scanTokens(): List<Token> {
        while (!isAtEnd()) {
            start = current
            scanToken()
        }

        tokens.add(Token(TokenType.EOF, "", null, line))

        return tokens
    }

    private fun scanToken() {
        when (val c = advance()) {
            '(' -> addToken(TokenType.LEFT_PAREN)
            ')' -> addToken(TokenType.RIGHT_PAREN)
            ',' -> addToken(TokenType.COMMA)
            '-' -> addToken(TokenType.MINUS)
            '+' -> addToken(TokenType.PLUS)
            ':' -> addToken(TokenType.COLON)
            ';' -> addToken(TokenType.SEMICOLON)
            '*' -> addToken(TokenType.STAR)
            '#' -> addToken(TokenType.HASH)
            '=' -> addToken(TokenType.EQUAL)
            '/' -> addToken(TokenType.SLASH)
            '<' -> addToken(
                when {
                    match('=') -> TokenType.LESS_EQUAL
                    match('>') -> TokenType.LESS_GREATER
                    else -> TokenType.LESS
                }
            )
            '>' -> addToken(if (match('=')) TokenType.GREATER_EQUAL else TokenType.GREATER)

            ' ', '\r', '\t' -> {}

            '\n' -> line++

            '"' -> string()

            else -> {
                when {
                    isDigit(c) -> number()
                    isAlpha(c) -> identifier()
                    else -> error(line, "Unexpected character.")
                }
            }
        }
    }

    private fun identifier() {
        val c = peek()
        when {
            c == '$' -> {
                advance()
                addToken(TokenType.STRING_IDENTIFIER)
            }
            isDigit(c) -> {
                advance()
                addToken(TokenType.IDENTIFIER)
            }
            else -> {
                if (!isAlpha(c)) {
                    error(line, "Unexpected character in identifier.")
                } else {
                    while (isAlpha(peek())) {
                        advance()
                    }

                    val text = source.substring(start, current)
                    val type = KEYWORDS[text]
                    if (type == null) {
                        error(line, "Unexpected keyword")
                    } else {
                        addToken(type)
                    }
                }
            }
        }
    }

    private fun isAlpha(c: Char): Boolean {
        return c in 'A'..'Z'
    }

    private fun number() {
        while (isDigit(peek())) {
            advance()
        }

        addToken(TokenType.NUMBER, source.substring(start, current).toInt())
    }

    private fun isDigit(c: Char): Boolean {
        return c in '0'..'9'
    }

    private fun  string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') {
                error(line, "Unexpected new line in string.")
            }
            advance()
        }

        if (isAtEnd()) {
            error(line, "Unterminated string.")
        }

        advance()

        addToken(TokenType.STRING, source.substring(start + 1, current - 1))
    }

    private fun peek(): Char {
        return if (isAtEnd()) {
            0.toChar()
        } else {
            source[current]
        }
    }

    private fun match(expected: Char): Boolean {
        return when {
            isAtEnd() -> false
            source[current] != expected -> false
            else -> {
                current++
                true
            }
        }
    }

    private fun advance(): Char {
        current++
        return source[current - 1]
    }

    private fun addToken(type: TokenType) {
        addToken(type, null)
    }

    private fun addToken(type: TokenType, literal: Any?) {
        val text = source.substring(start, current)
        tokens.add(Token(type, text, literal, line))
    }

    private fun isAtEnd(): Boolean {
        return current >= source.length
    }

    companion object {

        private val KEYWORDS = mapOf(
            "PRINT" to TokenType.PRINT
        )
    }
}