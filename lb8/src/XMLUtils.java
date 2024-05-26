import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLUtils {

    public static void saveParticipantsToXML(List<Participant> participants, File file) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("RegisteredConferees");
        doc.appendChild(rootElement);

        for (Participant participant : participants) {
            Element conferee = doc.createElement("Conferee");
            rootElement.appendChild(conferee);

            Element name = doc.createElement("name");
            name.appendChild(doc.createTextNode(participant.getName()));
            conferee.appendChild(name);

            Element lastName = doc.createElement("familyName");
            lastName.appendChild(doc.createTextNode(participant.getLastName()));
            conferee.appendChild(lastName);

            Element workplace = doc.createElement("placeOfWork");
            workplace.appendChild(doc.createTextNode(participant.getWorkplace()));
            conferee.appendChild(workplace);

            Element reportTitle = doc.createElement("reportTitle");
            reportTitle.appendChild(doc.createTextNode(participant.getReportTitle()));
            conferee.appendChild(reportTitle);

            Element email = doc.createElement("email");
            email.appendChild(doc.createTextNode(participant.getEmail()));
            conferee.appendChild(email);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }

    public static List<Participant> loadParticipantsFromXML(File file) throws ParserConfigurationException, SAXException, IOException {
        List<Participant> participants = new ArrayList<>();

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);

        Node root = doc.getDocumentElement();
        NodeList confereeNodes = root.getChildNodes();

        for (int i = 0; i < confereeNodes.getLength(); i++) {
            Node confereeNode = confereeNodes.item(i);
            if (confereeNode.getNodeType() == Node.ELEMENT_NODE) {
                Element confereeElement = (Element) confereeNode;
                String name = confereeElement.getElementsByTagName("name").item(0).getTextContent();
                String lastName = confereeElement.getElementsByTagName("familyName").item(0).getTextContent();
                String workplace = confereeElement.getElementsByTagName("placeOfWork").item(0).getTextContent();
                String reportTitle = confereeElement.getElementsByTagName("reportTitle").item(0).getTextContent();
                String email = confereeElement.getElementsByTagName("email").item(0).getTextContent();

                Participant participant = new Participant(name, lastName, workplace, reportTitle, email);
                participants.add(participant);
            }
        }

        return participants;
    }
}