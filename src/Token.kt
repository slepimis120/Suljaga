class Token {
    var type: TokenType
    var suljagaeme: String
    var literal: Any?
    var line: Int

    constructor(type: TokenType, suljagaeme: String, literal: Any?, line: Int){
        this.type = type
        this.suljagaeme = suljagaeme
        this.literal = literal
        this.line = line
    }

    override fun toString(): String {
        return "$type $suljagaeme $literal"
    }
}