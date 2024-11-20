package src

class Token {
    var type: TokenType
    var lexeme: String
    var literal: Any?
    var line: Int

    constructor(type: TokenType, lexeme: String, literal: Any?, line: Int){
        this.type = type
        this.lexeme = lexeme
        this.literal = literal
        this.line = line
    }

    override fun toString(): String {
        return "$type $lexeme $literal"
    }
}