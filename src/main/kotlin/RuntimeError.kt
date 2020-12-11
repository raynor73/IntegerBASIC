import java.lang.RuntimeException

/**
 * @author igorlapin on 11/12/2020.
 */
class RuntimeError(val token: Token, message: String) : RuntimeException(message)