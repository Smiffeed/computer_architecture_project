class Register:
    def __init__(self, value, address):
        self.value = value
        self.address = address
    
    def to_16bit_value(self):
        binary = bin(self.value & 0xFFFF)[2:]  # Convert to binary and handle negative numbers
        return binary.zfill(16)  # Pad with zeros to make it 16 bits
    
    def to_3bit_address(self):
        address_map = {
            'r0': '000', 'r1': '001', 'r2': '010',
            'r3': '011', 'r4': '100', 'r5': '101',
            'r6': '110', 'r7': '111'
        }
        return address_map.get(self.address, '') 