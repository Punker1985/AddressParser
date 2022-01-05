package AddressParser;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
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
                    addresses = new ArrayList<>(parsingXML(inputFilePath)); //     c:\\test\\address.xml
                } else {                                                     //    c:\\test\\address.txt
                    if (checkFileCSV(inputFilePath)) {                       //    c:\\test\\address.csv.xls
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
