package com.example.calculatorapp.presentation

interface MainCommunication : Communication<String> {
    class Base : Communication.Abstract<String>(), MainCommunication
}