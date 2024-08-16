package org.unibl.etf.mybudgetbackend.models.xml_wrappers;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class Transaction {
    @JacksonXmlProperty(localName = "Description")
    private String description;

    @JacksonXmlProperty(localName = "Amount")
    private Amount amount;
}