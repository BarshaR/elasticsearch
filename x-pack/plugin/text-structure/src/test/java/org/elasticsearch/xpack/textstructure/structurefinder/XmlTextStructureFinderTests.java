/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.textstructure.structurefinder;

import org.elasticsearch.xpack.core.textstructure.structurefinder.TextStructure;

import java.util.Collections;

public class XmlTextStructureFinderTests extends TextStructureTestCase {

    private final TextStructureFinderFactory factory = new XmlTextStructureFinderFactory();

    public void testCreateConfigsGivenGoodXml() throws Exception {
        assertTrue(factory.canCreateFromSample(explanation, XML_SAMPLE, 0.0));

        String charset = randomFrom(POSSIBLE_CHARSETS);
        Boolean hasByteOrderMarker = randomHasByteOrderMarker(charset);
        TextStructureFinder structureFinder = factory.createFromSample(
            explanation,
            XML_SAMPLE,
            charset,
            hasByteOrderMarker,
            TextStructureFinderManager.DEFAULT_LINE_MERGE_SIZE_LIMIT,
            TextStructureOverrides.EMPTY_OVERRIDES,
            NOOP_TIMEOUT_CHECKER
        );

        TextStructure structure = structureFinder.getStructure();

        assertEquals(TextStructure.Format.XML, structure.getFormat());
        assertEquals(charset, structure.getCharset());
        if (hasByteOrderMarker == null) {
            assertNull(structure.getHasByteOrderMarker());
        } else {
            assertEquals(hasByteOrderMarker, structure.getHasByteOrderMarker());
        }
        assertNull(structure.getExcludeLinesPattern());
        assertEquals("^\\s*<log4j:event", structure.getMultilineStartPattern());
        assertNull(structure.getDelimiter());
        assertNull(structure.getQuote());
        assertNull(structure.getHasHeaderRow());
        assertNull(structure.getShouldTrimFields());
        assertNull(structure.getGrokPattern());
        assertEquals("timestamp", structure.getTimestampField());
        assertEquals(Collections.singletonList("UNIX_MS"), structure.getJodaTimestampFormats());
        assertEquals(Collections.singleton("properties"), structure.getMappings().keySet());
    }
}
