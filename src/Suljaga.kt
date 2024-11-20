import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.file.Paths
import kotlin.system.exitProcess

class Suljaga {
    companion object {
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
            try {
                val source = Paths.get(path).toFile().readText(Charset.defaultCharset())
                run(source)
            } catch (e: Exception) {
                println("Error reading file: ${e.message}")
                exitProcess(65)
            }
        }

        private fun runPrompt() {
            val input = InputStreamReader(System.`in`)
            val reader = BufferedReader(input)

            while(true){
                print("> ")
                val line = reader.readLine() ?: break
                run(line)
            }
        }

        private fun run(source: String){
            val scanner = Scanner(source)
            val tokens = scanner.scanTokens()

            for(token in tokens){
                print(token)
            }
        }

        fun error(line: Int, message: String){
            report(line, "", message)
        }

        private fun report(line: Int, where: String, message: String){
            System.err.println("[line $line] Error$where: $message")
        }
    }
}