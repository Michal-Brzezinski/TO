package com.nbp.app.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Optional;

@XmlRootElement(name = "tabela_kursow")
public class ExchangeTable {
    private String tableNumber;
    private String publicationDate;
    private List<ExchangeRate> rates;

    @XmlElement(name = "numer_tabeli")
    public String getTableNumber() { return tableNumber; }
    public void setTableNumber(String tableNumber) { this.tableNumber = tableNumber; }

    @XmlElement(name = "data_publikacji")
    public String getPublicationDate() { return publicationDate; }
    public void setPublicationDate(String publicationDate) { this.publicationDate = publicationDate; }

    @XmlElement(name = "pozycja")
    public List<ExchangeRate> getRates() { return rates; }
    public void setRates(List<ExchangeRate> rates) { this.rates = rates; }

    public Optional<ExchangeRate> getRate(String currencyCode) {
        return rates.stream()
                .filter(rate -> rate.getCurrencyCode().equalsIgnoreCase(currencyCode))
                .findFirst();
    }
}