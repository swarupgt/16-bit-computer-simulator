# Spring 2024 - CSCI 6461 Team 5 Project
# Part 1 Design Notes

- Project by Swarup Totloor, Yashita Pobbareddy, Ravi Gavade

## Overview
- We approached  Part 1 of the project by building the `CPU` as a class. By making `CPU` its own class, we abstract its inner working from the user or GUI program. All the registers are instances of the `Register` class, since the only thing that differentiates between them is the bitsize. these registers, along with the memory, belong to the CPU class. 

- The `InstructionRegister` class inherits from `Register`, as it has the same functionalities, on top of which helper functions to fetch different parts of the instruction word are built.

- `Memory` is also its own class. We use an Integer array to store, as it is a lot more efficient to store such values compared to bitsize length char arrays.

- For the GUI, we made use of the JavaFX Library as it allows for a lot of customisablity and ease of use. The `Controller` class controls the GUI, and has an instance of `CPU` as its attribute. This class sets all the register values to be seen on the screen. It checks for the correctness of the octal and binary inputs from the user, as well as the existence of the input loading file in the system.

- In addition to the register fields and input fields, we thought adding a **console output** would be useful to see the various operations taking place behind the curtains. A lot of getters and setters were used to make the back and forth of values from UI to CPU objects very easy.

- This structure allows for easy integration of any future instructions and components.

- We make use of the Maven tool to build our Java program, as it allows for automating our builds, making development easier.

#### Some of the key functions that play a major role in the execution are listed below - 

#### `CPU.Initialise()`
 - This function is ued to reset of initialise all the register and other values part of the CPU.
 - It also refreshes the console output.

#### `CPU.LoadFromROM()`
 - This function reads the given input loading file and loads it into the memory.
 - It also sets the PC based on the first valid instruction it comes across.

#### `CPU.RunOneStep()`
 - As the name suggests, it runs a single step. It fetches the instruction to be executed into the IR, and increments PC.
 - It also handles the halts that occur in the machine. 
 - This function can be called in a loop to run through all the instructions.

#### `CPU.ExecuteInstruction()`
 - This function is called by `RunOneStep()`. Based on the op code values from the IR, it calls all the load/store functions like `LDR()` or `STX()` etc.