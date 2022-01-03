package AddressParser;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static AddressParser.UtilClass.*;


public class AddressParserApplication {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        String inputFilePath = "";
        ArrayList<Address> addresses = null;
        while (!inputFilePath.equals("exit")) {
            inputFilePath = readFilePath();
            if (checkFilePath(inputFilePath)) {
                if (checkFileXML(inputFilePath)) {
                    addresses = new ArrayList<>(parsingXML(inputFilePath)); //     c:\\address.xml
                } else {                                                     //    c:\\address.txt
                    if (checkFileCSV(inputFilePath)) {                       //    c:\\address.csv.xls
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
