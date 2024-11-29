package src

class Interpreter : Expr.Visitor<Any?>, Stmt.Visitor<Any?> {
    private var environment = Environment()
    private val locals = HashMap<Expr, Int>()

    fun interpret(statements: List<Stmt>) {
        try{
            for(statement in statements){
                execute(statement)
            }
        } catch (e: RuntimeError){
            Suljaga.runtimeError(e)
        }
    }

    private fun execute(stmt: Stmt) {
        stmt.accept(this)
    }

    private fun executeBlock(statements: List<Stmt>, environment: Environment) {
        val previous = this.environment
        try {
            this.environment = environment
            for (statement in statements) {
                execute(statement)
            }
        } finally {
            this.environment = previous
        }
    }

    override fun visitAssignExpr(expr: Expr.Assign): Any? {
        val value = evaluate(expr.value)
        val distance = locals[expr]

        if(distance != null){
            environment.assignAt(distance, expr.name, value)
        } else {
            environment.assign(expr.name, value)
        }

        return value
    }

    override fun visitBinaryExpr(expr: Expr.Binary): Any? {
        val left = evaluate(expr.left)
        val right = evaluate(expr.right)

        return when(expr.operator.type){
            TokenType.PLUS -> {
                checkNumberedOperand(expr.operator, left, right)
                left as Double + right as Double
            }

            TokenType.MINUS -> {
                checkNumberedOperand(expr.operator, left, right)
                left as Double - right as Double
            }

            TokenType.STAR -> {
                checkNumberedOperand(expr.operator, left, right)
                left as Double * right as Double
            }

            TokenType.SLASH -> {
                checkNumberedOperand(expr.operator, left, right)
                left as Double / right as Double
            }

            else -> null
        }
    }

    override fun visitGroupingExpr(expr: Expr.Grouping): Any? {
        return evaluate(expr.expression)
    }

    override fun visitLiteralExpr(expr: Expr.Literal): Any {
        return expr.value
    }

    override fun visitUnaryExpr(expr: Expr.Unary): Any? {
        val right = evaluate(expr.right)

        return when(expr.operator.type){
            TokenType.MINUS -> {
                checkNumberedOperand(expr.operator, right)
                -(right as Double)
            }

            else -> null
        }
    }

    override fun visitVariableExpr(expr: Expr.Variable): Any? {
        return lookUpVariable(expr.name, expr)
    }

    private fun checkNumberedOperand(operator: Token, operand: Any?, operand2: Any? = null) {
        if(operand is Double && operand2 is Double){
            return
        } else {
            throw RuntimeError(operator, "Operands must be a number.")
        }
    }

    private fun checkNumberedOperand(operator: Token, operand: Any?) {
        if(operand is Double){
            return
        } else {
            throw RuntimeError(operator, "Operand must be a number.")
        }
    }

    private fun evaluate(expr: Expr): Any? {
        return expr.accept(this)
    }

    private fun stringify(obj: Any?): String {
        if(obj == null) return "null"

        if(obj is Double){
            var text = obj.toString()
            if(text.endsWith(".0")){
                text = text.substring(0, text.length - 2)
            }
            return text
        }

        return obj.toString()
    }

    override fun visitBlockStmt(stmt: Stmt.Block) {
        executeBlock(stmt.statements, Environment(environment))
    }

    override fun visitExpressionStmt(stmt: Stmt.Expression): Any? {
        evaluate(stmt.expression)
        return null
    }

    override fun visitPrintStmt(stmt: Stmt.Print): Any? {
        val value = evaluate(stmt.expression)
        println(stringify(value))
        return null
    }

    override fun visitVarStmt(stmt: Stmt.Var): Any? {
        val value = evaluate(stmt.initializer)
        environment.define(stmt.name.lexeme, value)
        return null
    }

    fun resolve(expr: Expr, depth: Int) {
        locals[expr] = depth
    }

    private fun lookUpVariable(name: Token, expr: Expr): Any? {
        val distance = locals[expr]
        return if(distance != null){
            environment.getAt(distance, name.lexeme)
        } else {
            environment.get(name)
        }
    }
}