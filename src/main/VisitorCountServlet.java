import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class VisitorCountServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public void init() {
        // Initialize visitor count from XML file
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse("visitorCount.xml");
            NodeList nodeList = doc.getElementsByTagName("visitorCount");
            Element element = (Element) nodeList.item(0);
            String visitorCount = element.getTextContent();
            System.out.println("Initial Visitor Count: " + visitorCount);
        } catch (Exception e) {
            System.out.println("Error initializing visitor count: " + e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handle GET request to retrieve visitor count
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse("visitorCount.xml");
            NodeList nodeList = doc.getElementsByTagName("visitorCount");
            Element element = (Element) nodeList.item(0);
            String visitorCount = element.getTextContent();
            response.setContentType("text/xml");
            PrintWriter out = response.getWriter();
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?><visitorCount>" + visitorCount + "</visitorCount>");
        } catch (Exception e) {
            System.out.println("Error retrieving visitor count: " + e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handle POST request to update visitor count
        try {
            String newVisitorCount = request.getParameter("count");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse("visitorCount.xml");
            NodeList nodeList = doc.getElementsByTagName("visitorCount");
            Element element = (Element) nodeList.item(0);
            element.setTextContent(newVisitorCount);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult("visitorCount.xml");
            transformer.transform(source, result);
            response.setContentType("text/xml");
            PrintWriter out = response.getWriter();
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?><visitorCount>" + newVisitorCount + "</visitorCount>");
        } catch (Exception e) {
            System.out.println("Error updating visitor count: " + e.getMessage());
        }
    }
}