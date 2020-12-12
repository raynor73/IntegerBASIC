package ilapin.integer_basic

/**
 * @author igorlapin on 10/12/2020.
 */
enum class TokenType {
    // Single-character tokens.
    LEFT_PAREN, RIGHT_PAREN, COMMA, MINUS, PLUS, COLON, SEMICOLON, SLASH, STAR, HASH, EQUAL,

    // One or two character tokens.
    GREATER, GREATER_EQUAL, LESS, LESS_EQUAL, LESS_GREATER,

    // Literals.
    STRING_IDENTIFIER, IDENTIFIER, STRING, NUMBER,

    // Keywords.
    AND, OR, MOD, NOT,

    // Statements
    LET, INPUT, PRINT, TAB, FOR, TO, STEP, NEXT, IF, THEN, GOTO, GOSUB, RETURN, DIM, REM, END, POKE, CALL,

    // Built-in functions.
    ABS, SGN, PEEK, RND, LEN,

    // Special markers
    LINE_NUMBER, EOL, EOF
}