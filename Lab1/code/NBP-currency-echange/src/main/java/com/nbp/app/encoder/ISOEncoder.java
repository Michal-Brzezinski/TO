package com.nbp.app.encoder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ISOEncoder implements IEncoder {
    private static final Charset FALLBACK_CHARSET = Charset.forName("ISO-8859-2");
    private static final Pattern ENCODING_PATTERN = Pattern.compile("encoding=\"([^\"]+)\"");

    @Override
    public Charset detect(byte[] xmlData) {
        int length = Math.min(xmlData.length, 200);
        String header = new String(xmlData, 0, length, StandardCharsets.US_ASCII);

        Matcher matcher = ENCODING_PATTERN.matcher(header);
        if (matcher.find()) {
            try {
                return Charset.forName(matcher.group(1));
            } catch (Exception e) {
                System.err.println("Warning: unknown coding, using fallback");
            }
        }
        return FALLBACK_CHARSET;
    }
}
