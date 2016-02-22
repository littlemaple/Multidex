package imagination.ga.marsh;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 44260 on 2016/2/18.
 */
public class SerializerXML implements ISerializer<HashMap<String, Object>> {
    @Override
    public String serialize(HashMap<String, Object> stringObjectHashMap) {
        return stringObjectHashMap == null ? null : createXml(stringObjectHashMap);
    }

    @Override
    public HashMap<String, Object> deserialize(String s) {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            HashMap<String, Object> hashMap = new HashMap<>();
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new ByteArrayInputStream(s.getBytes())));
            NodeList nodeList = document.getElementsByTagName("xml");
            int length = nodeList.getLength();
            if (length == 1) {
                Element element = (Element) nodeList.item(0);
                NodeList childList = element.getElementsByTagName("*");
                for (int i = 0; i < childList.getLength(); i++) {
                    Node childNode = childList.item(i);
                    hashMap.put(childNode.getNodeName(), childNode.getTextContent());
                }
            }
            return hashMap;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String createXml(HashMap<String, Object> map) {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            Document document = documentBuilderFactory.newDocumentBuilder().newDocument();
            Element rootElement = document.createElement("xml");
            document.appendChild(rootElement);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Element child = document.createElement(entry.getKey());
                child.setNodeValue(String.valueOf(entry.getValue()));
                child.setTextContent(String.valueOf(entry.getValue()));
                rootElement.appendChild(child);
            }
            return toString(document);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String toString(Document doc) {
        try {
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.CDATA_SECTION_ELEMENTS, "yes");
            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            return sw.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error converting to String", ex);
        }
    }

}
