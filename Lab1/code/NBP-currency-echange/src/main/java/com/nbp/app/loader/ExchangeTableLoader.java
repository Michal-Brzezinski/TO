package com.nbp.app.loader;

import com.nbp.app.encoder.ISOEncoder;
import com.nbp.app.model.ExchangeTable;
import com.nbp.app.parser.IDocumentParser;
import com.nbp.app.parser.XmlDocumentParser;
import com.nbp.app.repository.IRemoteRepository;
import com.nbp.app.repository.NbpApiRepository;

import java.util.concurrent.CompletableFuture;

public class ExchangeTableLoader {
    private static final String NBP_DATA_URL =
            "http://static.nbp.pl/dane/kursy/xml/LastA.xml";

    private final IRemoteRepository repository;
    private final IDocumentParser parser;

    public ExchangeTableLoader(IRemoteRepository repository,
                               IDocumentParser parser) {
        this.repository = repository;
        this.parser     = parser;
    }

    public ExchangeTableLoader() {
        this(
                new NbpApiRepository(),
                new XmlDocumentParser(new ISOEncoder())
        );
    }

    public CompletableFuture<ExchangeTable> loadDataAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                byte[] data = repository.get(NBP_DATA_URL);
                return parser.parse(data);
            } catch (Exception e) {
                throw new RuntimeException(
                        "FATAL: Could not initialize application data", e);
            }
        });
    }
}
