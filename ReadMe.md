# TestNG's testng-results.xml to *.XLSX file converter Maven Plugin

## Usage

Add the following code to your **pom.xml** file

```xml
<build>
    <plugins>
        <plugin>
            <groupId>ru.bootdev</groupId>
            <artifactId>testng-xls-results-maven-plugin</artifactId>
            <version>1.0.0</version>
            <configuration>
                <sourceXml>
                    test-output/testng-results.xml
                </sourceXml>
                <excludedMethods>
                    (?i)^after|before|setup$
                </excludedMethods>
            </configuration>
            <executions>
                <execution>
                    <phase>test</phase>
                    <goals>
                        <goal>xlsReport</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

## Configuration

The plugin has two available parameters `sourceXml` and `excludedMethods`.

The `sourceXml` represents TestNG's testng-results.xml relative path from the project root (the example is above).  
The `excludedMethods` is a regexp string that represents which test method names will be excluded from the xls report.

ALl parameters above can be assigned by system parameters through the maven -D flag. The parameters names are
`xlsReport.sourceXml` and `xlsReport.excludedMethods`.

Also, you can specify an execution phase. By default, it is the test phase.

## XLSX reports

All generated *.xlsx reports will be placed in the project root folder.

