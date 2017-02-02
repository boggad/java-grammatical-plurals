/*
 * MIT License
 *
 * Copyright (c) 2016 Timofey Klyubin
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

package ru.klyubin.plurals.internal;

import ru.klyubin.plurals.Pluralization;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Timofey Klyubin on 03.02.17.
 * t.klyubin@gmail.com
 */
public final class StringBundle {

    private final Map<Locale, Map<Pluralization, String>> internalMap;

    public StringBundle() {
        this.internalMap = new HashMap<>();
    }

    public Map<Locale, Map<Pluralization, String>> getInternalMap() {
        return internalMap;
    }

    public String getString(Locale locale, Pluralization pluralization) {
        if (internalMap.containsKey(locale)) {
            return internalMap.get(locale).get(pluralization);
        }
        return null;
    }
}
