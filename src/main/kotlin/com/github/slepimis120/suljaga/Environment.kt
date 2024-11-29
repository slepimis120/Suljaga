package com.github.slepimis120.suljaga

class Environment {
    private val values = HashMap<String, Any?>()
    private var enclosing: Environment? = null

    constructor(enclosing: Environment?) {
        this.enclosing = enclosing
    }

    constructor() {
        enclosing = null
    }

    fun define(name: String, value: Any?) {
        values[name] = value
    }

    fun get(name: Token): Any? {
        if (values.containsKey(name.lexeme)) {
            return values[name.lexeme]
        }
        if (enclosing != null) {
            return enclosing?.get(name)
        }
        return null
    }

    fun assign(name: Token, value: Any?) {
        if (values.containsKey(name.lexeme)) {
            values[name.lexeme] = value
        } else {
            define(name.lexeme, value)
        }
    }

    fun getAt(distance: Int, name: String): Any? {
        return ancestor(distance).values[name]
    }

    private fun ancestor(distance: Int): Environment {
        var environment = this
        for (i in 0 until distance) {
            environment = environment.enclosing!!
        }
        return environment
    }

    fun assignAt(distance: Int, name: Token, value: Any?) {
        ancestor(distance).values[name.lexeme] = value
    }
}