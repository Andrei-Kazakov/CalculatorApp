package com.example.calculatorapp.domain

import com.example.calculatorapp.data.CalculationState.*
import com.example.calculatorapp.data.MainRepository
import com.example.calculatorapp.data.Operation
import com.example.calculatorapp.presentation.Communication

interface MainInteractor {

    fun plus(): Result

    fun minus(): Result

    fun devide(): Result

    fun multiply(): Result

    fun calculate(): Result

    fun handle(value: String): Result

    fun clear(): Result

    class Base(private val repository: MainRepository) : MainInteractor {

        private val handleOperation: HandleOperation = HandleOperation.Base(repository)

        override fun plus(): Result {
            return handleOperation.handle(Operation.Plus)
        }

        override fun minus(): Result {
            return handleOperation.handle(Operation.Minus)
        }

        override fun devide(): Result {
            return handleOperation.handle(Operation.Divide)
        }

        override fun multiply(): Result {
            return handleOperation.handle(Operation.Multiply)
        }

        override fun calculate(): Result {
            if (repository.isLeftPartEmpty() || repository.isRightPartEmpty())
                return Result.Nothing
            return try {
                val left = repository.leftPart()
                val operation = repository.operation()
                val right = repository.rightPart()
                val value = repository.calculate().toString()
                val text = "$left\n$operation\n$right\n\n = $value"
                Result.Success(text)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

        override fun handle(value: String): Result {
            return if (repository.compareState(INITIAL) || repository.compareState(LEFT_PART_PRESENT)) {
                repository.updateLeftPart(value)
                Result.Success(repository.leftPart())
            } else {
                repository.updateRightPart(value)
                Result.Success(repository.leftPart() + repository.operation() + repository.rightPart())
            }
        }

        override fun clear(): Result {
            if (repository.compareState(INITIAL)){
                return Result.Nothing
                }
            if (repository.compareState(LEFT_PART_PRESENT)) {
                val value = repository.leftPart()
                val new = value.dropLast(1)
                return if (new.isEmpty()){
                    repository.clearLeft()
                    Result.Success("0")
                } else {
                    repository.updateLeftPart(new, false)
                    Result.Success(repository.leftPart())
                }
            }
            if (repository.compareState(OPERATION_PRESENT)) {
                repository.clearOperation()
                return Result.Success(repository.leftPart())
            }

            val value = repository.rightPart()
            val new = value.dropLast(1)
            return if (new.isEmpty()) {
                repository.clearRight()
                Result.Success(repository.leftPart() + repository.operation())
            } else {
                repository.updateRightPart(new, false)
                Result.Success(repository.leftPart() + repository.operation() + repository.rightPart())
            }
        }

        private interface HandleOperation {

            fun handle(operation: Operation): Result
            class Base(private val repository: MainRepository) : HandleOperation {

                override fun handle(operation: Operation): Result {

                    if (repository.compareState(LEFT_PART_PRESENT)) {
                        repository.changeOperation(operation)
                        return Result.Success(repository.leftPart() + repository.operation())
                    }

                    if (repository.compareState(OPERATION_PRESENT)) {
                        repository.changeOperation(operation)
                        return Result.Success(repository.leftPart() + repository.operation())
                    }

                    if (repository.compareState(RIGHT_PART_PRESENT)) {
                        return Result.Nothing
                    }
                    return Result.Nothing
                }
            }
        }
    }
}

sealed class Result {

    abstract fun map(
        success: Communication<String>,
        fail: Communication<String>
    )

    data class Success(private val value: String) : Result() {
        override fun map(success: Communication<String>, fail: Communication<String>) =
            success.map(value)
    }

    data class Error(private val e: Exception) : Result() {
        override fun map(success: Communication<String>, fail: Communication<String>) =
            fail.map(e.message ?: "error")

    }

    object Nothing : Result() {
        override fun map(success: Communication<String>, fail: Communication<String>) = Unit
    }
}