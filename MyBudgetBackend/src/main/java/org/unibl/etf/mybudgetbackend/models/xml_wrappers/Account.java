package org.unibl.etf.mybudgetbackend.models.xml_wrappers;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.List;

@Data
public class Account {
    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(isAttribute = true)
    private String currency;

    @JacksonXmlProperty(localName = "Balance")
    private double balance;

    @JacksonXmlElementWrapper(localName = "Transactions")
    @JacksonXmlProperty(localName = "Transaction")
    private List<Transaction> transactions;
}
