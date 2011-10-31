package com.trivago.mail.pigeon.web.data.process;


import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class LinkParser {

    private static Set<String> links;

    public static Set<String> parse(final String content) {

        links = new HashSet<String>();
        DOMParser parser = new DOMParser();
        try {
            parser.parse(content);
            links = new HashSet<String>();
            org.w3c.dom.Document document = parser.getDocument();
            Node root = document.getFirstChild();
            process(root);
            return links;
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void process(final Node node) {

        String name = node.getNodeName();

        if (name.equalsIgnoreCase("a")) {

            if (node.hasAttributes()) {
                Node item = node.getAttributes().getNamedItem("href");
                if (item != null) {
                    links.add(item.getNodeValue());
                }
            }
        }

        Node sibling = node.getNextSibling();
        if (sibling != null) {
            process(sibling);
        }

        Node child = node.getFirstChild();
        if (child != null) {
            process(child);
        }
    }
}
