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

package com.deathmotion.foliascheduler.internal.checks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * The {@code RelocateCheck} class ensures that the package of the class has been relocated
 * from its original package.
 * This is essential to prevent conflicts with other plugins that may use this library as well.
 * <p>
 * When the class is loaded, a static block invokes the {@link #check()} method to perform
 * this validation.
 * The package name is compared against an expected original package name
 * stored in a resource file.
 * <p>
 * If the package has not been relocated, an {@link IllegalStateException} is thrown with
 * instructions to relocate the package.
 * </p>
 * <p>
 * The original package name is read from the file {@code /original_package_name.txt}, which
 * should be present in the resources directory.
 * </p>
 */
public class RelocateCheck {

    static {
        check();
    }

    /**
     * Checks if the current package of the class matches the original package name.
     * If they are the same, it throws an {@link IllegalStateException} to enforce
     * package relocation.
     *
     * @throws IllegalStateException if the current package has not been relocated
     */
    private static void check() {
        String currentPackage = RelocateCheck.class.getPackage().getName();
        String originalPackage = getOriginalPackageName();

        if (currentPackage.equals(originalPackage)) {
            throw new IllegalStateException("In order to use FoliaScheduler, you must relocate the package 'com.deathmotion.foliascheduler' to your own package. Check the readme for more information.");
        }
    }

    /**
     * Retrieves the original package name from the {@code /original_package_name.txt} file
     * in the classpath.
     *
     * @return the original package name as a {@code String}
     * @throws RuntimeException if the file cannot be read or is empty
     */
    private static String getOriginalPackageName() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(RelocateCheck.class.getResourceAsStream("/original_package_name.txt"))))) {
            String packageName = reader.readLine();
            if (packageName != null) {
                return packageName.trim();
            } else {
                throw new IOException("The file '/original_package_name.txt' is empty.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the original package name.", e);
        }
    }
}

