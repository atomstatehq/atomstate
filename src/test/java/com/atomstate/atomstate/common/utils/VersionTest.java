/*
 * Copyright 2024- Atomstate Technologies Private Limited.
 *
 * Licensed as a Atomstate Enterprise file under the Atomstate Enterprise
 * License (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * https://github.com/atomstatehq/atomstate/blob/main/license/ael.md
 */

package com.atomstate.atomstate.common.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VersionTest {
    @Test
    public void testConstructor() {
        Version version = new Version(1, 0, 0);
        assertEquals(1, version.major);
        assertEquals(0, version.minor);
        assertEquals(0, version.patch);
    }

    @Test
    public void testParseValidVersionString() {
        Version version = Version.parse("1.0.0");
        assertEquals(1, version.major);
        assertEquals(0, version.minor);
        assertEquals(0, version.patch);

        version = Version.parse("2.1.3");
        assertEquals(2, version.major);
        assertEquals(1, version.minor);
        assertEquals(3, version.patch);
    }

    @Test
    public void testParseInvalidVersionString() {
        assertThrows(IllegalArgumentException.class, () -> Version.parse("1.0"));
        assertThrows(IllegalArgumentException.class, () -> Version.parse("1.0.0.0"));
        assertThrows(IllegalArgumentException.class, () -> Version.parse("invalid.version"));
    }

    @Test
    public void testToString() {
        Version version = new Version(1, 2, 3);
        assertEquals("1.2.3", version.toString());

        version = new Version(10, 20, 30);
        assertEquals("10.20.30", version.toString());
    }

    @Test
    public void testCompareTo() {
        Version v1 = new Version(1, 0, 0);
        Version v2 = new Version(1, 0, 1);
        Version v3 = new Version(1, 1, 0);
        Version v4 = new Version(2, 0, 0);

        assertTrue(v1.compareTo(v2) < 0); // v1 < v2
        assertTrue(v2.compareTo(v1) > 0); // v2 > v1
        assertTrue(v1.compareTo(v3) < 0); // v1 < v3
        assertTrue(v3.compareTo(v4) < 0); // v3 < v4
        assertEquals(0, v1.compareTo(new Version(1, 0, 0))); // v1 == v1
    }

    @Test
    public void testEquals() {
        Version v1 = new Version(1, 0, 0);
        Version v2 = new Version(1, 0, 0);
        Version v3 = new Version(1, 0, 1);
        Version v4 = new Version(1, 1, 0);

        assertEquals(v1, v2); // Equal versions
        assertNotEquals(v1, v3); // Different patch
        assertNotEquals(v1, v4); // Different minor
        assertNotEquals(null, v1); // Not equal to null
        assertNotEquals("string", v1); // Not equal to a different type
    }

    @Test
    public void testHashCode() {
        Version v1 = new Version(1, 0, 0);
        Version v2 = new Version(1, 0, 0);
        Version v3 = new Version(1, 0, 1);

        assertEquals(v1.hashCode(), v2.hashCode()); // Same version
        assertNotEquals(v1.hashCode(), v3.hashCode()); // Different version
    }
}