package org.unibl.etf.mybudgetbackend.models.xml_wrappers;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Data;

@Data
public class Amount {
    @JacksonXmlProperty(isAttribute = true)
    private String currency;

    @JacksonXmlText
    private double value;
}