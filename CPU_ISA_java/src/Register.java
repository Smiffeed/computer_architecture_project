public class Register {
    private String address;
    private int value;

    public Register(int value, String address) {
        this.value = value;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String to16BitValue() {
        String binaryValue = Integer.toBinaryString(value);
        if (binaryValue.length() > 16) {
            return binaryValue.substring(binaryValue.length() - 16);
        } else {
            return String.format("%16s", binaryValue).replace(' ', '0');
        }
    }

    public String to3BitAddress() {
        switch (address) {
            case "r0":
                return "000";
            case "r1":
                return "001";
            case "r2":
                return "010";
            case "r3":
                return "011";
            case "r4":
                return "100";
            case "r5":
                return "101";
            case "r6":
                return "110";
            case "r7":
                return "111";
            default:
                return "";
        }
    }
}

