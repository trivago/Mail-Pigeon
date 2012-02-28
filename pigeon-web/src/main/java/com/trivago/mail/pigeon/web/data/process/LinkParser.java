/**
 * Copyright (C) 2011-2012 trivago GmbH <mario.mueller@trivago.com>, <christian.krause@trivago.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

        links = new HashSet<>();
        DOMParser parser = new DOMParser();
        try {
            parser.parse(content);
            links = new HashSet<>();
            org.w3c.dom.Document document = parser.getDocument();
            Node root = document.getFirstChild();
            process(root);
            return links;
        } catch (SAXException | IOException e) {
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
