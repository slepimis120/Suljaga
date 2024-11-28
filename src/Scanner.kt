package src

class Scanner(private var source: String) {
    private val tokens: MutableList<Token> = mutableListOf()
    private var start: Int = 0
    private var current: Int = 0
    private var line: Int = 1

    companion object {
        val keywords = mapOf(
            "print" to TokenType.PRINT,
            "scope" to TokenType.SCOPE
        )
    }

    fun scanTokens(): List<Token> {
        while (!isAtEnd()) {
            start = current
            scanToken()
        }
        tokens.add(Token(TokenType.EOF, "", null, line))
        return tokens
    }

    private fun isAtEnd(): Boolean {
        return current >= source.length
    }

    private fun scanToken() {
        val c = advance()
        when (c) {
            '{' -> addToken(TokenType.LEFT_BRACE)
            '}' -> addToken(TokenType.RIGHT_BRACE)
            '-' -> addToken(TokenType.MINUS)
            '+' -> addToken(TokenType.PLUS)
            '*' -> addToken(TokenType.STAR)
            '/' -> addToken(TokenType.SLASH)
            '=' -> addToken(TokenType.EQUAL)
            '\n' -> addToken(TokenType.NEW_LINE).also { line++ }

            ' ', '\r', '\t' -> {
            }

            else -> {
                if (c.isDigit()) {
                    number()
                } else if (c.isAlpha()) {
                    identifier()
                } else {
                    Suljaga.error(line, "Unexpected character.")
                }
            }
        }
    }

    private fun advance(): Char {
        current++
        return source[current - 1]
    }

    private fun addToken(type: TokenType) {
        addToken(type, null)
    }

    private fun addToken(type: TokenType, literal: Any? = null) {
        val text = source.substring(start, current)
        tokens.add(Token(type, text, literal, line))
    }

    private fun number() {
        while (peek().isDigit()) advance()
        addToken(TokenType.NUMBER, source.substring(start, current).toDouble())
    }

    private fun identifier() {
        while (peek().isLetterOrDigit()) advance()
        val text = source.substring(start, current)
        val type = keywords[text] ?: TokenType.IDENTIFIER
        addToken(type)
    }

    private fun peek(): Char {
        if (isAtEnd()) return '\u0000'
        return source[current]
    }

    private fun Char.isAlpha(): Boolean {
        return this in 'a'..'z' || this in 'A'..'Z' || this == '_'
    }
}