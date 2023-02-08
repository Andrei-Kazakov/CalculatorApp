package com.example.calculatorapp.data

import java.math.BigInteger

sealed class Operation {

    abstract fun calculate(left: Part, right: Part): BigInteger

    object Plus : Operation() {
        override fun calculate(left: Part, right: Part): BigInteger {
            return left.value().add(right.value())
        }

        override fun toString(): String {
            return " + "
        }
    }

    object Minus : Operation() {
        override fun calculate(left: Part, right: Part): BigInteger {
            return left.value().subtract(right.value())
        }

        override fun toString(): String {
            return " - "
        }
    }

    object Divide : Operation() {
        override fun calculate(left: Part, right: Part): BigInteger {
            return left.value().divide(right.value())
        }

        override fun toString(): String {
            return " / "
        }
    }

    object Multiply : Operation() {
        override fun calculate(left: Part, right: Part): BigInteger {
            return left.value().multiply(right.value())
        }

        override fun toString(): String {
            return " * "
        }
    }

    object None : Operation() {
        override fun calculate(left: Part, right: Part): BigInteger {
            return BigInteger.ZERO
        }
    }
}