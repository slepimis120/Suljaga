package src

class Resolver(private var interpreter: Interpreter) : Expr.Visitor<Unit>, Stmt.Visitor<Unit> {
    private val scopes = mutableListOf<HashMap<String, Boolean>>()

    override fun visitAssignExpr(expr: Expr.Assign): Unit {
        resolve(expr.value)
        resolveLocal(expr, expr.name)
    }

    override fun visitBinaryExpr(expr: Expr.Binary): Unit {
        resolve(expr.left)
        resolve(expr.right)
    }

    override fun visitGroupingExpr(expr: Expr.Grouping): Unit {
        resolve(expr.expression)
    }

    override fun visitLiteralExpr(expr: Expr.Literal): Unit {
        return
    }

    override fun visitUnaryExpr(expr: Expr.Unary): Unit {
        resolve(expr.right)
    }

    override fun visitVariableExpr(expr: Expr.Variable): Unit {
        if(scopes.isNotEmpty()){
            if(scopes.last()[expr.name.lexeme] == false){
                Suljaga.error(expr.name, "Cannot read local variable in its own initializer.")
            }
        }

        resolveLocal(expr, expr.name)
    }

    override fun visitBlockStmt(stmt: Stmt.Block) : Unit {
        beginScope()
        resolve(stmt.statements)
        endScope()
    }

    override fun visitExpressionStmt(stmt: Stmt.Expression): Unit {
        resolve(stmt.expression)
    }

    override fun visitPrintStmt(stmt: Stmt.Print): Unit {
        resolve(stmt.expression)
    }

    override fun visitVarStmt(stmt: Stmt.Var): Unit {
        declare(stmt.name)
        resolve(stmt.initializer)
        define(stmt.name)
    }

    fun resolve(statements: List<Stmt>) {
        for (statement in statements) {
            resolve(statement)
        }
    }

    private fun resolve(statement: Stmt) {
        statement.accept(this)
    }

    private fun resolve(expr: Expr) {
        expr.accept(this)
    }

    private fun beginScope() {
        scopes.add(HashMap())
    }

    private fun endScope() {
        scopes.removeAt(scopes.size - 1)
    }

    private fun declare(name : Token){
        if(scopes.isEmpty()) return

        var scope = scopes.last()
        scope[name.lexeme] = false
    }

    private fun define(name : Token){
        if(scopes.isEmpty()) return

        var scope = scopes.last()
        scope[name.lexeme] = true
    }

    private fun resolveLocal(expr: Expr, name: Token){
        for(i in scopes.size - 1 downTo 0){
            if(scopes[i].containsKey(name.lexeme)){
                interpreter.resolve(expr, scopes.size - 1 - i)
                return
            }
        }
    }
}
