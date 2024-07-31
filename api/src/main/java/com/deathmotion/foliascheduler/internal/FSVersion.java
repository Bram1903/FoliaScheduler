/*
 * MIT License
 *
 * Copyright (c) 2024 Bram
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.deathmotion.foliascheduler.internal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents a PacketEvents version using Semantic Versioning.
 * Supports version comparison, cloning, and provides a string representation.
 * Snapshot versioning is also supported.
 * Generally a snapshot version is published before the release version,
 * and thus, is considered "older" than the release version.
 */
public class FSVersion implements Comparable<FSVersion> {

    private final int major;
    private final int minor;
    private final int patch;
    private final boolean snapshot;
    private final @Nullable String snapshotCommit;

    /**
     * Constructs a {@link FSVersion} instance.
     *
     * @param major          the major version number.
     * @param minor          the minor version number.
     * @param patch          the patch version number.
     * @param snapshot       whether the version is a snapshot.
     * @param snapshotCommit the snapshot commit hash, if available.
     */
    public FSVersion(
            final int major, final int minor, final int patch,
            final boolean snapshot, final @Nullable String snapshotCommit
    ) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.snapshot = snapshot;
        this.snapshotCommit = snapshotCommit;
    }

    /**
     * Constructs a {@link FSVersion} instance.
     *
     * @param major          the major version number.
     * @param minor          the minor version number.
     * @param patch          the patch version number.
     * @param snapshotCommit the snapshot commit hash, if available.
     */
    public FSVersion(
            final int major, final int minor, final int patch,
            final @Nullable String snapshotCommit
    ) {
        this(major, minor, patch, snapshotCommit != null, snapshotCommit);
    }

    /**
     * Constructs a {@link FSVersion} instance.
     *
     * @param major    the major version number.
     * @param minor    the minor version number.
     * @param patch    the patch version number.
     * @param snapshot whether the version is a snapshot.
     */
    public FSVersion(final int major, final int minor, final int patch, final boolean snapshot) {
        this(major, minor, patch, snapshot, null);
    }

    /**
     * Constructs a {@link FSVersion} instance with snapshot defaulted to false.
     *
     * @param major the major version number.
     * @param minor the minor version number.
     * @param patch the patch version number.
     */
    public FSVersion(final int major, final int minor, final int patch) {
        this(major, minor, patch, false);
    }

    /**
     * Gets the major version number.
     *
     * @return the major version number.
     */
    public int major() {
        return major;
    }

    /**
     * Gets the minor version number.
     *
     * @return the minor version number.
     */
    public int minor() {
        return minor;
    }

    /**
     * Gets the patch version number.
     *
     * @return the patch version number.
     */
    public int patch() {
        return patch;
    }

    /**
     * Checks if the version is a snapshot.
     *
     * @return true if snapshot, false otherwise.
     */
    public boolean snapshot() {
        return snapshot;
    }

    /**
     * Gets the snapshot commit hash of the PacketEvents snapshot version. May be of any length.
     * Availability is not guaranteed since it is contingent on how the program was built.
     * Generally speaking, the commit hash can only be available if the PacketEvents version is a snapshot version.
     *
     * @return the snapshot commit hash, if available.
     */
    public @Nullable String snapshotCommit() {
        return snapshotCommit;
    }

    /**
     * Compares this {@link FSVersion} with another {@link FSVersion}.
     *
     * @param other the other {@link FSVersion}.
     * @return a negative integer, zero, or a positive integer as this version can be less than,
     * equal to, or greater than the specified version.
     */
    @Override
    public int compareTo(@NotNull final FSVersion other) {
        int majorCompare = Integer.compare(this.major, other.major);
        if (majorCompare != 0) return majorCompare;

        int minorCompare = Integer.compare(this.minor, other.minor);
        if (minorCompare != 0) return minorCompare;

        int patchCompare = Integer.compare(this.patch, other.patch);
        if (patchCompare != 0) return patchCompare;

        return Boolean.compare(other.snapshot, this.snapshot);
    }

    /**
     * Checks if the provided object is equal to this {@link FSVersion}.
     *
     * @param obj the object to compare.
     * @return true if the provided object is equal to this {@link FSVersion}, false otherwise.
     */
    @Override
    public boolean equals(@NotNull final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof FSVersion)) return false;
        FSVersion other = (FSVersion) obj;

        return this.major == other.major &&
                this.minor == other.minor &&
                this.patch == other.patch &&
                this.snapshot == other.snapshot &&
                Objects.equals(this.snapshotCommit, other.snapshotCommit);
    }

    /**
     * Checks if this version is newer than the provided version.
     *
     * @param otherVersion the other {@link FSVersion}.
     * @return true if this version is newer, false otherwise.
     */
    public boolean isNewerThan(@NotNull final FSVersion otherVersion) {
        return this.compareTo(otherVersion) > 0;
    }

    /**
     * Checks if this version is older than the provided version.
     *
     * @param otherVersion the other {@link FSVersion}.
     * @return true if this version is older, false otherwise.
     */
    public boolean isOlderThan(@NotNull final FSVersion otherVersion) {
        return this.compareTo(otherVersion) < 0;
    }

    /**
     * Returns a hash code value for this {@link FSVersion}.
     *
     * @return a hash code value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(major, minor, patch, snapshot, snapshotCommit);
    }

    /**
     * Creates and returns a copy of this {@link FSVersion}.
     *
     * @return a clone of this instance.
     */
    @Override
    public FSVersion clone() {
        return new FSVersion(major, minor, patch, snapshot, snapshotCommit);
    }

    /**
     * Converts the {@link FSVersion} to a string representation.
     *
     * @return a string representation of the version.
     */
    @Override
    public String toString() {
        return major + "." + minor + "." + patch
                + (snapshotCommit != null ? "+" + snapshotCommit : "")
                + (snapshot ? "-SNAPSHOT" : "");
    }
}
