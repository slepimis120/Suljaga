package com.github.slepimis120.suljaga

enum class TokenType {
    // Single-character tokens.
    LEFT_BRACE,
    RIGHT_BRACE,
    MINUS,
    PLUS,
    STAR,
    SLASH,
    EQUAL,

    // Literals.
    IDENTIFIER,
    NUMBER,

    // Keywords.
    PRINT,
    SCOPE,

    // Special
    NEW_LINE,
    EOF
}