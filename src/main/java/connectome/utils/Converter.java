package connectome.utils;

public class Converter {

    public static long convertIP2Long(String ip) {
        String[] ipArray = ip.split("\\.");
        long result = 0;
        for (int index = 0; index < ipArray.length; index++) {
            result += Integer.parseInt(ipArray[index]) * Math.pow(256, 3 - index);
        }
        return result;
    }
}



