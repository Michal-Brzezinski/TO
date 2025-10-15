package com.nbp.app.encoder;

import java.nio.charset.Charset;

public interface IEncoder {

    Charset detect(byte[] xmlData);
}
