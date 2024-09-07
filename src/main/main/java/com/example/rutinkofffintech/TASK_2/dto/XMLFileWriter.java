package com.example.rutinkofffintech.TASK_2.dto;

import java.io.IOException;
import java.io.PrintWriter;

public class XMLFileWriter {

    public static void saveToXML(String filePath, String xmlContent) {
        try (PrintWriter out = new PrintWriter(filePath)) {
            out.println(xmlContent);
        } catch (IOException e) {
            System.err.println("Error saving XML file: " + e.getMessage());
        }
    }
}
