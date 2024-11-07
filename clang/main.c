#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "register.h"
#include "instruction.h"

#define MAX_INSTRUCTIONS 100
#define INPUT_SIZE 50

int main() {
    char input[INPUT_SIZE];
    Register registers[8];
    Instruction instructions[MAX_INSTRUCTIONS];
    int step = 0;
    
    // Initialize registers
    for (int i = 0; i < 8; i++) {
        char addr[3];
        sprintf(addr, "r%d", i);
        register_init(&registers[i], 0, addr);
    }
    
    // Main input loop
    while (1) {
        printf("Input instructions: ");
        if (fgets(input, INPUT_SIZE, stdin) == NULL) break;
        
        // Remove newline
        input[strcspn(input, "\n")] = 0;
        
        if (strcmp(input, "end 0 0") == 0) break;
        
        process_instruction(input, registers, instructions, &step);
    }
    
    // Print results
    printf("%-19s%-24s%s\n", "Decoded Form", "Encoded Form", "Clock Cycles");
    
    char buffer[100];
    for (int i = 0; i < step; i++) {
        instruction_to_string(&instructions[i], buffer);
        printf("%s\n", buffer);
    }
    
    // Rest of the output formatting...
    // (Similar to the Java version but using C printf formatting)
    
    // Add this after printing the register values
    printf("-----------------------------------------------\n");
    int totalClockCycles = 0;
    for (int i = 0; i < step; i++) {
        totalClockCycles += instructions[i].clock_cycles;
    }
    double cpi = (double)totalClockCycles / step;
    printf("the program's CPI: %.2f\n", cpi);
    printf("-----------------------------------------------\n");
    
    // Add pipeline visualization
    int clockCyclesWithPipeline = step + 3;
    printf("Pipelined execution of the program:\n");
    printf("%17s", "");
    for (int i = 1; i <= clockCyclesWithPipeline; i++) {
        printf("%5d", i);
    }
    
    // Print pipeline stages for each instruction
    for (int i = 0; i < step; i++) {
        printf("\n%d. %-5s%-4s%-2s :", i + 1, 
               instructions[i].opcode, 
               instructions[i].register_ptr->address, 
               instructions[i].operand);
        
        // Print spaces before pipeline stages
        for (int j = 0; j < i + 1; j++) {
            printf("%5s", "");
        }
        
        // Print pipeline stages
        printf("IF | ID | EX | WB");
    }
    
    printf("\n\nPipelined ICP ex %d clock cycles to complete the program execution\n", 
           clockCyclesWithPipeline);
    
    // Add before pipeline visualization
    printf("\nValues in registers after execution:\n");
    for (int i = 0; i < 8; i++) {
        printf("r%d   %d\n", i, registers[i].value);
    }
    
    return 0;
} 