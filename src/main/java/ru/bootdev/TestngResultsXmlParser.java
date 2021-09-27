package ru.bootdev;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.bootdev.model.Suite;
import ru.bootdev.model.Test;
import ru.bootdev.model.TestMethod;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class TestngResultsXmlParser {

    private final Map<Suite, Map<Test, List<TestMethod>>> data = new HashMap<>();
    private final Document document;

    public TestngResultsXmlParser(File testResultsXml) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        this.document = xmlDocument(testResultsXml);
    }

    public Map<Suite, Map<Test, List<TestMethod>>> getData() {
        return data;
    }

    private DocumentBuilderFactory documentBuilderFactory() {
        DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
        df.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        df.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        return df;
    }

    private Document xmlDocument(File xmlFile) throws ParserConfigurationException, IOException, SAXException {
        return documentBuilderFactory().newDocumentBuilder().parse(xmlFile);
    }

    private XPath xPath() {
        return XPathFactory.newInstance().newXPath();
    }

    private NodeList findXpath(String format, Object... args) throws XPathExpressionException {
        return (NodeList) xPath().compile(String.format(format, args)).evaluate(document, XPathConstants.NODESET);
    }

    public void parse() throws XPathExpressionException {
        NodeList suites = findXpath("//suite");
        collectSuites(suites);
    }

    private void collectSuites(NodeList suites) throws XPathExpressionException {
        forEach(suites, node -> {
            Suite suite = new Suite(node.getAttributes());
            data.put(suite, new HashMap<>());
            collectTests(suite);
        });
    }

    private void collectTests(Suite suite) throws XPathExpressionException {
        NodeList tests = findXpath("//suite[@name='%s']//test", suite.getName());
        forEach(tests, node -> {
            Test test = new Test(node.getAttributes());
            data.get(suite).put(test, new ArrayList<>());
            collectMethods(suite, test);
        });
    }

    private void collectMethods(Suite suite, Test test) throws XPathExpressionException {
        NodeList methods = findXpath("//suite[@name='%s']//test[@name='%s']//test-method", suite.getName(), test.getName());
        forEach(methods, node -> {
            NamedNodeMap methodAttributes = node.getAttributes();

            AtomicReference<Node> exceptionClass = new AtomicReference<>();
            AtomicReference<Node> exceptionMessage = new AtomicReference<>();
            AtomicReference<Node> exceptionStackTrace = new AtomicReference<>();

            NodeList childNodes = node.getChildNodes();
            forEach(childNodes, c -> {
                if (c.getNodeName().equals("exception")) exceptionClass.set(c);
            });

            if (exceptionClass.get() != null) forEach(exceptionClass.get().getChildNodes(), c -> {
                if (c.getNodeName().equals("message")) exceptionMessage.set(c);
                if (c.getNodeName().equals("full-stacktrace")) exceptionStackTrace.set(c);
            });

            TestMethod testMethod = new TestMethod(methodAttributes, exceptionClass.get(), exceptionMessage.get(), exceptionStackTrace.get());
            data.get(suite).get(test).add(testMethod);
        });
    }

    private void forEach(NodeList nodes, ForEach<Node> lambda) throws XPathExpressionException {
        for (int i = 0; i < nodes.getLength(); i++) {
            lambda.apply(nodes.item(i));
        }
    }

    interface ForEach<T> {

        void apply(T t) throws XPathExpressionException;
    }
}
