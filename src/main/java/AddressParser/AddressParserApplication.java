package AddressParser;

import java.util.ArrayList;
import java.util.Map;
import static AddressParser.UtilClass.*;


public class AddressParserApplication {

    public static void main(String[] args) {
        String inputFilePath = "";
        ArrayList<Address> addresses = null;
        while (!inputFilePath.equals("exit")) {
            inputFilePath = readFilePath();
            if (checkFilePath(inputFilePath)) {
                if (checkFileXML(inputFilePath)) {
                    addresses = new ArrayList<>(parsingXML(inputFilePath));
                } else {
                    if (checkFileCSV(inputFilePath)) {
                        addresses = new ArrayList<>(parsingCSV(inputFilePath));
                    }
                }
                if (addresses != null) {
                    Map<Integer, Address> duplicates = searchDuplicates(addresses);
                    printDuplicates(duplicates);
                    searchCountHous(addresses);
                }
            }
        }
    }
}
