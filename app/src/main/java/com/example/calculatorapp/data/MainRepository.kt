package com.example.calculatorapp.data

import java.math.BigInteger

interface MainRepository : Validation {

    fun updateLeftPart(value: String, adding: Boolean = true)

    fun updateRightPart(value: String, adding: Boolean = true)

    fun compareState(other: CalculationState): Boolean

    fun changeOperation(operation: Operation)

    fun leftPart(): String

    fun rightPart(): String

    fun operation(): String

    fun calculate(): BigInteger

    fun clearLeft()

    fun clearRight()

    fun clearOperation()

    class Base(private val left: Part, private val right: Part) : MainRepository {

        private var operation : Operation = Operation.None
        private var state: CalculationState = CalculationState.INITIAL

        override fun updateLeftPart(value: String, adding: Boolean) {
            left.update(value, adding)
            state = CalculationState.LEFT_PART_PRESENT
        }

        override fun updateRightPart(value: String, adding: Boolean) {
            right.update(value, adding)
            state = CalculationState.RIGHT_PART_PRESENT
        }

        override fun compareState(other: CalculationState): Boolean {
            return this.state == other
        }

        override fun changeOperation(operation: Operation) {
            this.operation = operation
            this.state = CalculationState.OPERATION_PRESENT
        }

        override fun leftPart(): String {
            return left.value().toString()
        }

        override fun rightPart(): String {
            return right.value().toString()
        }

        override fun operation(): String {
            return operation.toString()
        }

        override fun calculate(): BigInteger {
            val calculate = operation.calculate(left, right)
            this.state = CalculationState.INITIAL
            left.clear()
            right.clear()
            return calculate
        }

        override fun clearLeft() {
            state = CalculationState.INITIAL
            left.clear()
        }

        override fun clearRight() {
            state = CalculationState.OPERATION_PRESENT
            right.clear()
        }

        override fun clearOperation() {
            state = CalculationState.LEFT_PART_PRESENT
            operation = Operation.None
        }

        override fun isLeftPartEmpty(): Boolean {
            return left.isEmpty()
        }

        override fun isRightPartEmpty(): Boolean {
            return right.isEmpty()
        }

    }
}

interface Validation {
    fun isLeftPartEmpty(): Boolean

    fun isRightPartEmpty(): Boolean
}