package src

import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.file.Paths
import kotlin.system.exitProcess

class Suljaga {
    companion object {
        private var hadError = false
        private var hadRuntimeError = false
        private val interpreter = Interpreter()

        @JvmStatic
        fun main(args: Array<String>){
            if (args.size > 1){
                println("Usage: jsuljaga [script]")
                exitProcess(64)
            } else if(args.size == 1){
                runFile(args[0])
            } else{
                runPrompt()
            }
        }

        private fun runFile(path: String) {
            val source = Paths.get(path).toFile().readText(Charset.defaultCharset())
            run(source)
            if(hadError) exitProcess(65)
            if(hadRuntimeError) exitProcess(70)
        }

        private fun runPrompt() {
            val input = InputStreamReader(System.`in`)
            val reader = BufferedReader(input)

            while(true){
                print("> ")
                val line = reader.readLine() ?: break
                if (line.trim().isNotEmpty()) {
                    run(line + "\n")
                }
                hadError = false
            }
        }

        private fun run(source: String) {
            val scanner = Scanner(source)
            val tokens = scanner.scanTokens()
            val parser = Parser(tokens)
            val statements = parser.parse()

            if (hadError) return

            val resolver = Resolver(interpreter)
            resolver.resolve(statements)

            interpreter.interpret(statements)
        }

        fun error(line: Int, message: String){
            report(line, "", message)
        }

        private fun report(line: Int, where: String, message: String){
            System.err.println("[line $line] Error$where: $message")

            hadError = true
        }

        fun error(token: Token, message: String){
            if(token.type == TokenType.EOF){
                report(token.line, " at end", message)
            } else {
                report(token.line, " at '${token.lexeme}'", message)
            }
        }

        fun runtimeError(error: RuntimeError){
            System.err.println("${error.message}\n[line ${error.token?.line}]")
            hadRuntimeError = true
        }
    }
}