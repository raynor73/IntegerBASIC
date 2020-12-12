package ilapin.integer_basic

/**
 * @author igorlapin on 10/12/2020.
 */
class Token(
        val type: TokenType,
        val lexeme: String,
        val literal: Any?,
        val line: Int
) {

    override fun toString(): String {
        return "$type $lexeme $literal"
    }
}