package src

enum class TokenType {
    // Single-character tokens.
    LEFT_BRACE,
    RIGHT_BRACE,
    MINUS,
    PLUS,
    STAR,
    SLASH,
    EQUAL,

    // One or two character tokens.
    BANG,
    BANG_EQUAL,

    // Literals.
    IDENTIFIER,
    NUMBER,

    // Keywords.
    PRINT,
    SCOPE,

    // Special
    EOF
}