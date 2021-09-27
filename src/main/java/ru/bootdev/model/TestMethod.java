package ru.bootdev.model;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class TestMethod {

    private String name;
    private String status;
    private String signature;
    private String description;
    private String exceptionClass;
    private String exceptionMessage;
    private String exceptionStackTrace;

    public TestMethod() {
    }

    public TestMethod(NamedNodeMap methodAttributes, Node exception, Node exceptionMessage, Node exceptionStackTrace) {
        this.name = attribute(methodAttributes, "name");
        this.status = attribute(methodAttributes, "status");
        this.signature = attribute(methodAttributes, "signature");
        this.description = attribute(methodAttributes, "description");
        this.exceptionClass = attribute(exception, "class");
        this.exceptionMessage = nodeValue(exceptionMessage);
        this.exceptionStackTrace = nodeValue(exceptionStackTrace);
    }

    private String attribute(NamedNodeMap attributes, String attributeName) {
        Node attributeNode = attributes.getNamedItem(attributeName);
        if (attributeNode != null) return attributeNode.getNodeValue();
        return "";
    }

    private String attribute(Node node, String attributeName) {
        if (node != null)return attribute(node.getAttributes(), attributeName);
        return "";
    }

    private String nodeValue(Node node) {
        if (node != null) return node.getTextContent().trim();
        return "";
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExceptionClass(String exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public void setExceptionStackTrace(String exceptionStackTrace) {
        this.exceptionStackTrace = exceptionStackTrace;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignature() {
        return signature;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public String getExceptionClass() {
        return exceptionClass;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public String getExceptionStackTrace() {
        return exceptionStackTrace;
    }

    public String getName() {
        return name;
    }
}
