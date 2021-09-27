package ru.bootdev.model;

import org.w3c.dom.NamedNodeMap;

public class Test {

    private String name;
    private Integer durationMs;
    private String startedAt;
    private String finishedAt;

    public Test() {
    }

    public Test(NamedNodeMap attributes) {
        this.name = attributes.getNamedItem("name").getNodeValue();
        this.durationMs = Integer.parseInt(attributes.getNamedItem("duration-ms").getNodeValue());
        this.startedAt = attributes.getNamedItem("started-at").getNodeValue();
        this.finishedAt = attributes.getNamedItem("finished-at").getNodeValue();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(Integer durationMs) {
        this.durationMs = durationMs;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(String finishedAt) {
        this.finishedAt = finishedAt;
    }
}
