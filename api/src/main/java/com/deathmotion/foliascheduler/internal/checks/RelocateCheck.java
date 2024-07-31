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

public class RelocateCheck {

    static {
        check();
    }

    private static void check() {
        String currentPackage = RelocateCheck.class.getPackage().getName();
        String originalPackage = getOriginalPackageName();

        if (currentPackage.equals(originalPackage)) {
            throw new IllegalStateException("In order to use FoliaScheduler, you must relocate the package 'com.deathmotion.foliascheduler' to your own package. Check the readme for more information.");
        }
    }

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

