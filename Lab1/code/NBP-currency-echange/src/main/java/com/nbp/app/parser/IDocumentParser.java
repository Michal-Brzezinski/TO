package com.nbp.app.parser;

import com.nbp.app.model.ExchangeTable;

public interface IDocumentParser {
    ExchangeTable parse(byte[] content);
}