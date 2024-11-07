#include <string.h>
#include <stdio.h>
#include "register.h"

void register_init(Register* reg, int value, const char* address) {
    reg->value = value;
    strncpy(reg->address, address, 2);
    reg->address[2] = '\0';
}

char* to_16bit_value(int value, char* buffer) {
    int i;
    unsigned int mask = 1 << 15;
    
    for (i = 0; i < 16; i++) {
        buffer[i] = (value & mask) ? '1' : '0';
        mask >>= 1;
    }
    buffer[16] = '\0';
    return buffer;
}

char* to_3bit_address(const char* address) {
    if (strcmp(address, "r0") == 0) return "000";
    if (strcmp(address, "r1") == 0) return "001";
    if (strcmp(address, "r2") == 0) return "010";
    if (strcmp(address, "r3") == 0) return "011";
    if (strcmp(address, "r4") == 0) return "100";
    if (strcmp(address, "r5") == 0) return "101";
    if (strcmp(address, "r6") == 0) return "110";
    if (strcmp(address, "r7") == 0) return "111";
    return "";
} 