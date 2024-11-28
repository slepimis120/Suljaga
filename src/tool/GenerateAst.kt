package src.tool

import java.io.PrintWriter
import java.util.*
import kotlin.system.exitProcess

class GenerateAst {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            if (args.size != 1) {
                System.err.println("Usage: generate_ast <output directory>")
                exitProcess(64)
            }
            val outputDir = args[0]
            defineAst(outputDir, "Expr", listOf(
                "Assign : Token name, Expr value",
                "Binary : Expr left, Token operator, Expr right",
                "Grouping : Expr expression",
                "Literal : Any value",
                "Unary : Token operator, Expr right",
                "Variable : Token name"
            ))

            defineAst(outputDir, "Stmt", listOf(
                "Block : List<Stmt> statements",
                "Expression : Expr expression",
                "Print : Expr expression",
                "Var : Token name, Expr initializer"
            ))
        }

        private fun defineAst(outputDir: String, baseName: String, types: List<String>){
            val path = "$outputDir/$baseName.kt"
            val writer = PrintWriter(path, "UTF-8")
            writer.println("package src")
            writer.println()
            writer.println("abstract class $baseName {")

            defineVisitor(writer, baseName, types)

            for (type in types) {
                val className = type.split(":")[0].trim()
                val fields = type.split(":")[1].trim()
                defineType(writer, baseName, className, fields)
            }
            writer.println()
            writer.println("    abstract fun <R> accept(visitor: Visitor<R>): R")

            writer.println("}")
            writer.close()
        }

        private fun defineType(writer: PrintWriter, baseName: String, className: String, fieldList: String) {
            writer.println("    class $className(")

            val fields = fieldList.split(", ")
            fields.forEachIndexed { index, field ->
                val (type, name) = field.split(" ")
                val separator = if (index == fields.lastIndex) "" else ","
                writer.println("        val $name: $type$separator")
            }

            writer.println("    ) : $baseName() {")

            writer.println()
            writer.println("    override fun <R> accept(visitor: Visitor<R>): R {")
            writer.println("        return visitor.visit$className$baseName(this)")
            writer.println("    }")

            writer.println("    }")
        }

        private fun defineVisitor(writer: PrintWriter, baseName: String, types: List<String>) {
            writer.println("    interface Visitor<R> {")
            types.forEach { type ->
                val typeName = type.split(":")[0].trim()
                writer.println("        fun visit$typeName$baseName(${baseName.lowercase(Locale.getDefault())}: $typeName): R")
            }
            writer.println("    }")
        }

    }
}