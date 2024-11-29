package com.github.slepimis120.suljaga

class RuntimeError(val token: Token, message: String) : RuntimeException(message) {

}