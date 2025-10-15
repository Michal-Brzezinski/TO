package com.nbp.app.loader;

import com.nbp.app.model.ExchangeTable;
import com.nbp.app.parser.IDocumentParser;
import com.nbp.app.parser.XmlDocumentParser;
import com.nbp.app.repository.IRemoteRepository;
import com.nbp.app.repository.NbpApiRepository;

import java.util.concurrent.CompletableFuture;

public class ExchangeTableLoader {
    private static final String NBP_DATA_URL = "http://static.nbp.pl/dane/kursy/xml/LastA.xml";
    private final IRemoteRepository repository;
    private final IDocumentParser parser;

    public ExchangeTableLoader() {
        // Wstrzykujemy domyślne implementacje. W bardziej złożonym systemie
        // użylibyśmy frameworka do wstrzykiwania zależności (np. Spring, Guice).
        this.repository = new NbpApiRepository();
        this.parser = new XmlDocumentParser();
    }

    /**
     * Asynchronously loads and parses the exchange table data.
     * @return A CompletableFuture which will complete with the ExchangeTable.
     */
    public CompletableFuture<ExchangeTable> loadDataAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("Downloading exchange rate data in the background...");
                byte[] data = repository.get(NBP_DATA_URL);
                return parser.parse(data);
            } catch (Exception e) {
                // Owijamy wyjątek w RuntimeException, aby mógł być propagowany przez CompletableFuture
                throw new RuntimeException("FATAL: Could not initialize application data", e);
            }
        });
    }
}