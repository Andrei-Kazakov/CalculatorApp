package com.example.calculatorapp.data

import java.math.BigInteger

interface Part {

    fun isEmpty(): Boolean

    fun clear()

    fun update(value: String, adding: Boolean = true)

    fun value(): BigInteger

    class Base() : Part {

        private var value: BigInteger = BigInteger.ZERO

        override fun isEmpty(): Boolean {
            return value == BigInteger.ZERO
        }

        override fun clear() {
            value = BigInteger.ZERO
        }

        override fun update(value: String, adding: Boolean) {
            if (adding) {
                if (isEmpty())
                    this.value = BigInteger(value)
                else {
                    val old = this.value.toString()
                    val new = old + value
                    update(new, false)
                }
            } else {
                this.value = BigInteger(value)
            }
        }

        override fun value(): BigInteger {
            return value
        }
    }
}