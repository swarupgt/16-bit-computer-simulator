# 16 Bit Computer Simulator
### Overview
This project was written as a term project for CSCI 6461 Computer Architecture, Spring 2024. It's a simulator written in Java, with a GUI to execute instructions and observe register and cache values. It works with a custom but simple RISC instruction set.

As of April 2024, it runs all the instructions(`load`, `store`, `jump` and such) , has functioning I/O and caching.

The heart of the project can be found in `simulator/src/main/java`.

### How to run
1.  The simulator (`simulator/target/simulator-1.0.0-jar-with-dependencies.jar`) can be easily run using the `java` command. For example on Linux - 
```
    $ java -jar file.jar
```
2. Press the `INIT` button to initialise the CPU.

3. If you want to run the provided `input.txt` file, paste its **full filepath** into the `Program File` text box and hit Enter. If the text box turns red, the file was not found.

4. Once the file is loaded, you will see the PC point to the first instruction's location. By using the `Step` button, you can iterate through the instructions, and see the values of the various registers update, as well as the console output with each action.

5. If you want to run through all of the instructions in one shot, after the file has been loaded, hit the `RUN` button. 

    - Note that in this case, the console output only shows all at once after a couple of seconds, when the program has finished execution. We are looking into making it so that it happens per instruction.

6. To reset the CPU values, simply hit `INIT` again.

### Operating the Console
1. Since user input cannot be put directly into registers, type into the Octal/Binary boxes. You can then click any of the `-` buttons beside each register to load in the binary text field (only if it's valid binary text). 

2. The console output prints every instruction being executed (register and address values are in decimal). It also prints whenever a halt occurs, or when the CPU has been reset.

3. *Buttons*
    - `INIT`: This button resets and initializes all the CPU values.
    - `Step`: This button single steps through each instruction loaded from the file.
    - `Run`: This is used to run through all instructions loaded from the file until halt is reached.
    - `-`: Present next to each register, this button is used to load the text from the Binary field into the corresponding register.
