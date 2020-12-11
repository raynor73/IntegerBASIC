/**
 * @author igorlapin on 10/12/2020.
 */
import java.io.File
import kotlin.system.exitProcess

object IntegerBASIC {

    private var hadError = false
    private var hadRuntimeError = false

    private val interpreter = Interpreter()

    fun runFile(path: String) {
        run(File(path).readText())

        if (hadError) {
            exitProcess(65)
        }

        if (hadRuntimeError) {
            exitProcess(70)
        }
    }

    fun runPrompt() {
        while(true) {
            print("> ")
            val line = readLine() ?: break
            run(line)
            hadError = false
        }
    }

    fun error(line: Int, message: String) {
        report(line, "", message)
    }

    fun error(token: Token, message: String) {
        if (token.type == TokenType.EOF) {
            report(token.line, " at end", message)
        } else {
            report(token.line, " at '${token.lexeme}'", message)
        }
    }

    fun runtimeError(error: RuntimeError) {
        System.err.println("${error.message}\n[line ${error.token.line}]")
        hadRuntimeError = true
    }

    private fun run(source: String) {
        val scanner = Scanner(source)
        val tokens = scanner.scanTokens()

        val parser = Parser(tokens)
        val expression = parser.parse()
        if (hadError || expression == null) {
            return
        }

        interpreter.interpret(expression)

        //println(AstPrinter().print(expression))

        //tokens.forEach { println(it) }
    }

    private fun report(line: Int, where: String, message: String) {
        System.err.println("[line $line] Error$where: $message")
        hadError = true
    }
}

fun main(args: Array<String>) {
    when {
        args.size > 1 -> {
            println("Usage: basic [path]")
            exitProcess(64)
        }

        args.size == 1 -> IntegerBASIC.runFile(args[0])

        else -> IntegerBASIC.runPrompt()
    }
}
