/**
 * @author igorlapin on 10/12/2020.
 */
import java.io.File
import kotlin.system.exitProcess

var hadError = false

fun main(args: Array<String>) {
    when {
        args.size > 1 -> {
            println("Usage: basic [path]")
            exitProcess(64)
        }

        args.size == 1 -> runFile(args[0])

        else -> runPrompt()
    }
}

fun runFile(path: String) {
    run(File(path).readText())

    if (hadError) {
        exitProcess(65)
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

fun run(source: String) {
    val scanner = Scanner(source)
    val tokens = scanner.scanTokens()

    tokens.forEach { println(it) }
}

fun error(line: Int, message: String) {
    report(line, "", message)
}

fun report(line: Int, where: String, message: String) {
    System.err.println("[line $line] Error$where: $message")
    hadError = true
}