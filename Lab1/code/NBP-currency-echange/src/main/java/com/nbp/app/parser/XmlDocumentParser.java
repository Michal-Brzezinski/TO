package com.nbp.app.parser;

import com.nbp.app.encoder.IEncoder;
import com.nbp.app.model.ExchangeTable;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;

import java.io.StringReader;
import java.nio.charset.Charset;

public class XmlDocumentParser implements IDocumentParser {
    private final IEncoder encoder;

    public XmlDocumentParser(IEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public ExchangeTable parse(byte[] content) {
        try {
            Charset charset = encoder.detect(content);
            String xml = new String(content, charset);

            JAXBContext ctx = JAXBContext.newInstance(ExchangeTable.class);
            Unmarshaller um = ctx.createUnmarshaller();
            return (ExchangeTable) um.unmarshal(new StringReader(xml));
        } catch (Exception e) {
            throw new RuntimeException("Could not parse XML content", e);
        }
    }
}
