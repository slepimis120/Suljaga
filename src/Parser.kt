package src

class Parser(private var tokens: List<Token>) {
    companion object {
        class ParseError : RuntimeException()
    }

    private var current: Int = 0

    fun parse(): List<Stmt> {
        val statements = ArrayList<Stmt>()
        while(!isAtEnd()) {
            declaration()?.let { statements.add(it) }
        }
        return statements
    }

    private fun expression(): Expr {
        return assignment()
    }

    private fun declaration(): Stmt? {
        try {
            return statement()
        } catch (error: ParseError) {
            synchronize()
            return null
        }
    }

    private fun assignment(): Expr {
        val expr = term()

        if (match(TokenType.EQUAL)) {
            val equals = previous()
            val value = assignment()

            if (expr is Expr.Variable) {
                val name = expr.name
                return Expr.Assign(name, value)
            }

            throw error(equals, "Invalid assignment target.")
        }

        return expr
    }

    private fun statement(): Stmt {
        return when {
            match(TokenType.PRINT) -> printStatement()
            match(TokenType.SCOPE) -> {
                consume(TokenType.LEFT_BRACE, "Expect '{' after 'scope'.")
                Stmt.Block(block())
            }
            else -> expressionStatement()
        }
    }

    private fun printStatement(): Stmt {
        val value = expression()
        consume(TokenType.NEW_LINE, "Expect new line after value.")
        return Stmt.Print(value)
    }

    private fun expressionStatement(): Stmt {
        val expr = assignmentOrExpression()
        consume(TokenType.NEW_LINE, "Expect new line after expression.")
        return Stmt.Expression(expr)
    }

    private fun assignmentOrExpression(): Expr {
        val expr = expression()

        if (match(TokenType.EQUAL)) {
            val equals = previous()
            val value = assignmentOrExpression()

            if (expr is Expr.Variable) {
                return Expr.Assign(expr.name, value)
            }

            throw error(equals, "Invalid assignment target.")
        }

        return expr
    }

    private fun block(): List<Stmt> {
        val statements = ArrayList<Stmt>()

        while (!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
            ignoreNewLines()
            declaration()?.let { statements.add(it) }
        }

        consume(TokenType.RIGHT_BRACE, "Expect '}' after block.")
        consume(TokenType.NEW_LINE, "Expect new line after block.")
        return statements
    }

    private fun term(): Expr {
        var expr = factor()

        while (match(TokenType.PLUS, TokenType.MINUS)) {
            val operator = previous()
            val right = factor()
            expr = Expr.Binary(expr, operator, right)
        }

        return expr
    }

    private fun factor(): Expr {
        var expr = unary()

        while (match(TokenType.STAR, TokenType.SLASH)) {
            val operator = previous()
            val right = unary()
            expr = Expr.Binary(expr, operator, right)
        }

        return expr
    }

    private fun unary(): Expr {
        if (match(TokenType.MINUS)) {
            val operator = previous()
            val right = unary()
            return Expr.Unary(operator, right)
        }

        return primary()
    }

    private fun primary(): Expr {
        if (match(TokenType.NUMBER)) {
            return Expr.Literal(previous().literal!!)
        }

        if (match(TokenType.LEFT_BRACE)) {
            val expr = expression()
            consume(TokenType.RIGHT_BRACE, "Expect ')' after expression.")
            return Expr.Grouping(expr)
        }

        if (match(TokenType.IDENTIFIER)) {
            return Expr.Variable(previous())
        }

        throw error(peek(), "Expect expression.")
    }

    private fun match(vararg types: TokenType): Boolean {
        for (type in types) {
            if (check(type)) {
                advance()
                return true
            }
        }
        return false
    }

    private fun consume(type: TokenType, message: String): Token {
        if (check(type)) return advance()
        throw error(peek(), message)
    }

    private fun check(type: TokenType): Boolean {
        if (isAtEnd()) return false
        return peek().type == type
    }

    private fun advance(): Token {
        if (!isAtEnd()) current++
        return previous()
    }

    private fun isAtEnd(): Boolean {
        return peek().type == TokenType.EOF
    }

    private fun peek(): Token {
        return tokens[current]
    }

    private fun previous(): Token {
        return tokens[current - 1]
    }

    private fun error(token: Token, message: String): ParseError {
        Suljaga.error(token, message)
        return ParseError()
    }

    private fun synchronize() {
        advance()

        while (!isAtEnd()) {
            when (peek().type) {
                TokenType.PRINT, TokenType.SCOPE -> return
                else -> advance()
            }
        }
    }

    private fun ignoreNewLines() {
        while (match(TokenType.NEW_LINE)) {
            // Ignore newlines in "scope", because scope doesn't end after a newline
        }
    }
}