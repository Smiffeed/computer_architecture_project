#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include "instruction.h"

void instruction_init(Instruction* inst, int step, const char* opcode, 
                     Register* reg, int clock_cycles, const char* operand) {
    inst->step = step;
    strncpy(inst->opcode, opcode, 3);
    inst->opcode[3] = '\0';
    inst->register_ptr = reg;
    inst->clock_cycles = clock_cycles;
    inst->value = reg->value;
    strncpy(inst->operand, operand, 9);
    inst->operand[9] = '\0';
}

char* five_bit_opcode(const char* opcode) {
    if (strcmp(opcode, "mov") == 0) return "00001";
    if (strcmp(opcode, "add") == 0) return "00010";
    if (strcmp(opcode, "mul") == 0) return "00100";
    if (strcmp(opcode, "sub") == 0) return "00011";
    if (strcmp(opcode, "div") == 0) return "00101";
    return "00000";
}

void instruction_to_string(Instruction* inst, char* buffer) {
    char value_buffer[17];
    to_16bit_value(inst->value, value_buffer);
    
    sprintf(buffer, "[%d] %-5s%-4s %-5s%-22s%d",
            inst->step, inst->opcode, inst->register_ptr->address, 
            inst->operand, value_buffer, inst->clock_cycles);
}

void process_instruction(char* input, Register* registers, 
                        Instruction* instructions, int* step) {
    char opcode[10], operand1[10], operand2[10];
    
    // Parse input
    sscanf(input, "%s %s %s", opcode, operand1, operand2);
    
    // Find destination register
    Register* dest_reg = NULL;
    for (int i = 0; i < 8; i++) {
        if (strcmp(registers[i].address, operand1) == 0) {
            dest_reg = &registers[i];
            break;
        }
    }
    
    if (dest_reg == NULL) return;
    
    // Convert operand2 to integer
    int value = atoi(operand2);
    
    // Process based on opcode
    if (strcmp(opcode, "mov") == 0) {
        dest_reg->value = value;
    } else if (strcmp(opcode, "add") == 0) {
        dest_reg->value += value;
    } else if (strcmp(opcode, "sub") == 0) {
        dest_reg->value -= value;
    } else if (strcmp(opcode, "mul") == 0) {
        dest_reg->value *= value;
    } else if (strcmp(opcode, "div") == 0) {
        if (value != 0) {
            dest_reg->value /= value;
        }
    }
    
    // Create instruction record
    instruction_init(&instructions[*step], *step, opcode, 
                    dest_reg, 1, operand2);  // Using 1 as default clock cycles
    (*step)++;
} 