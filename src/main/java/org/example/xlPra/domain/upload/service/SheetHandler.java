package org.example.xlPra.domain.upload.service;

import org.apache.poi.xssf.model.SharedStringsTable;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class SheetHandler extends DefaultHandler {
	private final SharedStringsTable sst;
    private String lastContents = "";
    private boolean isString = false;

    public SheetHandler(SharedStringsTable sst) {
        this.sst = sst;
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) {
        if ("c".equals(name)) {
            String type = attributes.getValue("t");
            isString = "s".equals(type);
        }
        lastContents = "";
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        lastContents += new String(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String name) {
        if ("v".equals(name)) {
            if (isString) {
                int idx = Integer.parseInt(lastContents);
                lastContents = sst.getItemAt(idx).getString();
            }
            System.out.print(lastContents + "\t");
        }

        if ("row".equals(name)) {
            System.out.println();
        }
    }
}
