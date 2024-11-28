package src

class RuntimeError : RuntimeException {
    constructor(token: Token, message: String) : super(message) {
        this.token = token
    }

    constructor(message: String) : super(message) {
        this.token = null
    }

    val token: Token?
}