package com.nbp.app.parser;

import com.nbp.app.model.ExchangeTable;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;

import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlDocumentParser implements IDocumentParser {
    // Używane jako fallback, gdyby deklaracja kodowania w XML była nieobecna
    private static final Charset FALLBACK_CHARSET = Charset.forName("ISO-8859-2");
    private static final Pattern ENCODING_PATTERN = Pattern.compile("encoding=\"([^\"]+)\"");

    @Override
    public ExchangeTable parse(byte[] content) {
        try {
            Charset charset = detectCharset(content);
            String xmlContent = new String(content, charset);

            JAXBContext jaxbContext = JAXBContext.newInstance(ExchangeTable.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(xmlContent);
            return (ExchangeTable) unmarshaller.unmarshal(reader);
        } catch (Exception e) {
            throw new RuntimeException("Could not parse XML content", e);
        }
    }

    /**
     * Detects charset from the XML declaration (e.g., <?xml ... encoding="ISO-8859-2"?>).
     * Falls back to a default charset if the declaration is not found.
     */
    private Charset detectCharset(byte[] xmlData) {
        // We only need the first few bytes to find the XML declaration.
        int length = Math.min(xmlData.length, 200);
        String header = new String(xmlData, 0, length, StandardCharsets.US_ASCII);

        Matcher matcher = ENCODING_PATTERN.matcher(header);
        if (matcher.find()) {
            try {
                String encodingName = matcher.group(1);
                return Charset.forName(encodingName);
            } catch (Exception e) {
                System.err.println("Warning: Could not determine charset from XML. Using fallback.");
                return FALLBACK_CHARSET;
            }
        }

        return FALLBACK_CHARSET;
    }
}