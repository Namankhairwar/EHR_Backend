package com.clinic.patient.applicationCommonFeature.ehrConversion;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.time.Year;

public class EhrConversion implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) {
        // Get next value from a database sequence (e.g., "ehr_seq")
        Long seq = ((Number) session.createNativeQuery("SELECT nextval('ehr_seq')").uniqueResult()).longValue();
        String year = String.valueOf(Year.now().getValue());
        return "Ehr" + year + seq; // e.g., Ehr20261
    }
}