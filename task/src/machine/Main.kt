package machine

import java.lang.IllegalArgumentException


enum class CoffeeState{
    READY,
    MAKING_COFFEE,
    FILLING;
}

enum class FillStates {
    STANDBY,
    WATER,
    MILK,
    COFFEE,
    CUPS ;
}

enum class MachineActions(val action: String){
    BUY("buy"),
    FILL("fill"),
    TAKE("take"),
    REMAINING("remaining"),
    EXIT("EXIT");
}

enum class CoffeeSorts(val coffee: String){
    ESPRESSO("1"),
    LATTE("2"),
    CAPPUCINO("3"),
    BACK("back");
}

class Espresso(){
    val requiredWater = 250
    val requiredCoffee = 16
    val price = 4
}
class Latte(){
    val requiredWater = 350
    val requiredMilk = 75
    val requiredCoffee = 20
    val price = 7
}
class Cappucino(){
    val requiredWater = 200
    val requiredMilk = 100
    val requiredCoffee = 12
    val price = 6
}

class CoffeeMachine(){
    var currentAmountOfMilk = 540
    var currentAmountOfWater = 400
    var currentAmountOfCoffee = 120
    var currentAmountOfCups = 9
    var currentAmountOfMoney = 550

    var currentState = CoffeeState.READY
    var currentCommand = ""
    var currentFillState = FillStates.STANDBY

    fun proceedCommand(command: String) {
        currentCommand = command
        if(checkMainAction() && currentState == CoffeeState.READY){
            processMainAction()
        }else if(checkCoffeeAction() && currentState == CoffeeState.MAKING_COFFEE){
            processCoffeeSelection()
        }else if(currentState == CoffeeState.FILLING) {
            fillMachine()
        }
    }

    private fun checkMainAction(): Boolean {
        try {
            MachineActions.values().firstOrNull { it.action == currentCommand }
            return true
        }catch (e: IllegalArgumentException){
            return false
        }
    }

    private fun checkCoffeeAction(): Boolean {
        return try {
            CoffeeSorts.values().firstOrNull { it.coffee == currentCommand }
            true
        }catch (e: IllegalArgumentException){
            false
        }
    }

    private fun processMainAction() {
        when (currentCommand) {
            MachineActions.BUY.action -> buyCoffee()
            MachineActions.FILL.action -> {
                fillMachine()
            }
            MachineActions.TAKE.action -> {
                takeMoney()
                printMainActionChoice()
            }
            MachineActions.REMAINING.action -> {
                printCurrentStatus()
                printMainActionChoice()
            }
            MachineActions.EXIT.action -> currentCommand= "exit"
        }
    }

    private fun processCoffeeSelection(){
        when(currentCommand) {
            CoffeeSorts.ESPRESSO.coffee -> makeEspresso()
            CoffeeSorts.LATTE.coffee-> makeLatte()
            CoffeeSorts.CAPPUCINO.coffee -> makeCappucino()
            CoffeeSorts.BACK.coffee -> currentState = CoffeeState.READY
            else -> "Unknown coffee"
        }
        printMainActionChoice()
    }

    private fun printMainActionChoice(){
        println()
        println("Write action (buy, fill, take, remaining, exit):")
    }


    private fun printCurrentStatus() {
        println()
        println("The coffee machine has:")
        println("$currentAmountOfWater ml of water")
        println("$currentAmountOfMilk ml of milk")
        println("$currentAmountOfCoffee g of coffee beans")
        println("$currentAmountOfCups disposable cups")
        println("\$$currentAmountOfMoney of money")
    }

    private fun buyCoffee () {
        currentState = CoffeeState.MAKING_COFFEE
        println()
        println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:")
    }

    private fun makeEspresso(){
        val espresso = Espresso()
        if(checkEspressoResource(espresso)){
            currentAmountOfWater -= espresso.requiredWater
            currentAmountOfCoffee -= espresso.requiredCoffee
            currentAmountOfCups -= 1
            currentAmountOfMoney += espresso.price
        }
        currentState = CoffeeState.READY
    }

    private fun makeLatte(){
        val latte = Latte()
        if (checkLatteResource(latte)){
            currentAmountOfWater -= latte.requiredWater
            currentAmountOfMilk -= latte.requiredMilk
            currentAmountOfCoffee -= latte.requiredCoffee
            currentAmountOfCups -= 1
            currentAmountOfMoney += latte.price
        }
        currentState = CoffeeState.READY
    }

    private fun makeCappucino(){
        val cappucino = Cappucino()
        if (checkCappucinoResource(cappucino)){
            currentAmountOfWater -= cappucino.requiredWater
            currentAmountOfMilk -= cappucino.requiredMilk
            currentAmountOfCoffee -= cappucino.requiredCoffee
            currentAmountOfCups -= 1
            currentAmountOfMoney += cappucino.price
        }
        currentState = CoffeeState.READY
    }


    private fun checkCappucinoResource(_cappucino: Cappucino): Boolean {
        val cappucino = _cappucino
        if ((currentAmountOfWater - cappucino.requiredWater) < 0){
            println("Sorry, not enough water!")
            return false
        }else if ((currentAmountOfMilk - cappucino.requiredMilk) < 0) {
            println("Sorry, not enough coffee milk!")
            return false
        }
        else if (currentAmountOfCoffee - cappucino.requiredCoffee < 0){
            println("Sorry, not enough coffee beans!")
            return false
        }else if (currentAmountOfCups < 0) {
            println("Sorry, not enough cups!")
            return false
        }
        else {
            println("I have enough resources, making you a coffee!")
            return true
        }
    }

    private fun checkLatteResource(_latte: Latte): Boolean{
        if ((currentAmountOfWater - _latte.requiredWater) < 0){
            println("Sorry, not enough water!")
            return false
        }else if ((currentAmountOfMilk - _latte.requiredMilk) < 0) {
            println("Sorry, not enough coffee milk!")
            return false
        }
        else if (currentAmountOfCoffee - _latte.requiredCoffee < 0){
            println("Sorry, not enough coffee beans!")
            return false
        }else if (currentAmountOfCups < 0) {
            println("Sorry, not enough cups!")
            return false
        }
        else {
            println("I have enough resources, making you a coffee!")
            return true
        }
    }

    fun checkEspressoResource(_espresso: Espresso): Boolean{
        if ((currentAmountOfWater- _espresso.requiredWater) < 0){
            println("Sorry, not enough water!")
            return false
        }else if ((currentAmountOfCoffee - _espresso.requiredCoffee) < 0){
            println("Sorry, not enough coffee beans!")
            return false
        }else if (currentAmountOfCups < 0) {
            println("Sorry, not enough cups!")
            return false
        }
        else {
            println("I have enough resources, making you a coffee!")
            return true
        }
    }

    private fun fillMachine() {
        currentState = CoffeeState.FILLING
        when(currentFillState){
            FillStates.STANDBY -> {
                println("Write how many ml of water you want to add:")
                currentFillState = FillStates.WATER
            }
            FillStates.WATER -> {
                currentAmountOfWater += currentCommand.toInt()
                currentFillState = FillStates.MILK
                println("Write how many ml of milk you want to add:")
            }
            FillStates.MILK -> {
                currentAmountOfMilk += currentCommand.toInt()
                currentFillState = FillStates.COFFEE
                println("Write how many grams of coffee beans you want to add:")
            }
            FillStates.COFFEE -> {
                currentAmountOfCoffee += currentCommand.toInt()
                currentFillState = FillStates.CUPS
                println("Write how many disposable cups you want to add:")
            }
            FillStates.CUPS -> {
                currentAmountOfCups += currentCommand.toInt()
                currentFillState = FillStates.STANDBY
                currentState = CoffeeState.READY
            }
        }
    }

    private fun takeMoney(){
        println("I gave you \$$currentAmountOfMoney}")
        currentAmountOfMoney = 0
    }

}

fun main() {
    val coffeeMachine = CoffeeMachine()
    println()
    println("Write action (buy, fill, take, remaining, exit):")
    while ( coffeeMachine.currentCommand != "exit" ){
        val action = chooseAction()
        coffeeMachine.proceedCommand(action)
    }
}

fun chooseAction():String {
    return readln()
}
