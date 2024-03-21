# CSCI 6461 Computer Architecture Team Project
# Team 5 User Guide

- Project by Swarup Totloor, Yashita Pobbareddy, Ravi Gavade

## Part 1 (Load/Store Instructions)

### How to run
1.  The simulator can be easily run using the `java` command. For example on Linux - 
```
    $ java -jar SimulatorTeam5.jar
```
2. Press the `INIT` button to initialise the CPU.

3. If you want to run the provided `input.txt` file, paste its **full filepath** into the `Program File` text box and hit Enter. If the text box turns red, the file was not found. Note that it currently only supports the load and store intructions, so any other instruction will cause an error.

4. Once the file is loaded, you will see the PC point to the first instruction's location. By using the `Step` button, you can iterate through the instructions, and see the values of the various registers update, as well as the console output with each action.

5. If you want to run through all of the instructions in one shot, after the file has been loaded, hit the `RUN` button. 

    - Note that in this case, the console output only shows all at once after a couple of seconds, when the program has finished execution. We are looking into making it so that it happens per instruction.

6. To reset the CPU values, simply hit `INIT` again.

### Operating the Console
1. Since user input cannot be put directly into registers, type into the Octal/Binary boxes. You can then click any of the `-` buttons beside each register to load in the binary text field (only if it's valid binary text). 

2. The console output is useful to keep an eye on,it prints every instruction being executed (register and address values are in decimal). It also prints whenever a halt occurs, or when the CPU has been reset.

3. *Buttons*
    - `Load`: The button loads the value of `MBR` into the address pointed to by the value of `MAR`.
    - `Store`: This button stores the `MBR` value into the address pointed to by the value of `MAR`.
    - `INIT`: This button resets and initializes all the CPU values.
    - `Step`: This button single steps through each instruction loaded from the file.
    - `Run`: This is used to run through all instructions loaded from the file until halt is reached.
    - `-`: Present next to each register, this button is used to load the text from the Binary field into the corresponding register.