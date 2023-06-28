package org.example;

import org.beanio.BeanReader;
import org.beanio.StreamFactory;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        // Create a StreamFactory
        StreamFactory factory = StreamFactory.newInstance();
        // Load the mapping file
        factory.load("C:\\Users\\a840014\\Downloads\\untitled2\\src\\main\\resources\\mapping.xml");


        BeanReader reader = factory.createReader("MyXmlData", new File("C:\\Users\\a840014\\OneDrive - Atos\\Desktop\\genesis_file.xml"));

        // Read the XML file record by record
        Object record;
        while ((record = reader.read()) != null) {
            if ("Record".equals(reader.getRecordName())) {
                // Process the Record object
                Record recordObj = (Record) record;
                System.out.println("Field 1: " + recordObj.getField_1());
            }
        }

        // Close the reader
        reader.close();
    }
}
