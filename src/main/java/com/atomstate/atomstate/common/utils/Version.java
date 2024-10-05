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

/**
 * Represents a version number consisting of major, minor, and patch components.
 * This class implements the {@link Comparable} interface to allow natural ordering
 * of version numbers based on their semantic versioning.
 * <p>
 * A version is represented in the format "major.minor.patch", where:
 * <ul>
 *   <li><strong>major</strong>: Indicates significant changes or backward-incompatible changes.</li>
 *   <li><strong>minor</strong>: Indicates backward-compatible feature additions.</li>
 *   <li><strong>patch</strong>: Indicates backward-compatible bug fixes.</li>
 * </ul>
 * </p>
 *
 * @author Atomstate
 * @version 1.0.0
 * @since 1.0.0
 */
public class Version implements Comparable<Version> {
    /**
     * The major version number.
     * Indicates significant changes or backward-incompatible changes.
     *
     * @since 1.0.0
     */
    protected final int major;

    /**
     * The minor version number.
     * Indicates backward-compatible feature additions.
     *
     * @since 1.0.0
     */
    protected final int minor;

    /**
     * The patch version number.
     * Indicates backward-compatible bug fixes.
     *
     * @since 1.0.0
     */
    protected final int patch;

    /**
     * Constructs a new Version object with the specified major, minor, and patch numbers.
     *
     * @param major the major version number
     * @param minor the minor version number
     * @param patch the patch version number
     * @author Atomstate
     * @since 1.0.0
     */
    public Version(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    /**
     * Parses a version string and creates a Version object.
     * The version string must be in the format "major.minor.patch".
     *
     * @param versionString the version string to parse
     * @return a Version object representing the parsed version
     * @throws IllegalArgumentException if the version string is invalid or does not contain exactly three parts
     * @author Atomstate
     * @since 1.0.0
     */
    public static Version parse(String versionString) {
        String[] parts = versionString.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid version string: " + versionString);
        }
        int major = Integer.parseInt(parts[0]);
        int minor = Integer.parseInt(parts[1]);
        int patch = Integer.parseInt(parts[2]);
        return new Version(major, minor, patch);
    }

    /**
     * Returns a string representation of this Version object in the format "major.minor.patch".
     *
     * @return a string representation of the version
     * @author Atomstate
     * @since 1.0.0
     */
    @Override
    public String toString() {
        return major + "." + minor + "." + patch;
    }

    /**
     * Compares this Version object with another Version object for order.
     *
     * @param other the Version object to be compared
     * @return a negative integer, zero, or a positive integer as this version is less than,
     * equal to, or greater than the specified object
     * @author Atomstate
     * @since 1.0.0
     */
    @Override
    public int compareTo(Version other) {
        if (this.major != other.major) {
            return Integer.compare(this.major, other.major);
        }
        if (this.minor != other.minor) {
            return Integer.compare(this.minor, other.minor);
        }
        return Integer.compare(this.patch, other.patch);
    }

    /**
     * Indicates whether some other object is "equal to" this Version.
     *
     * @param obj the reference object with which to compare
     * @return true if this Version is the same as the obj argument; false otherwise
     * @author Atomstate
     * @since 1.0.0
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Version other)) return false;

        return this.major == other.major && this.minor == other.minor && this.patch == other.patch;
    }

    /**
     * Returns a hash code value for this Version.
     *
     * @return a hash code value for this Version
     * @author Atomstate
     * @since 1.0.0
     */
    @Override
    public int hashCode() {
        int result = Integer.hashCode(major); // Hash code for major
        result = 31 * result + Integer.hashCode(minor); // Hash code for minor
        result = 31 * result + Integer.hashCode(patch); // Hash code for patch
        return result; // Return combined hash code
    }
}