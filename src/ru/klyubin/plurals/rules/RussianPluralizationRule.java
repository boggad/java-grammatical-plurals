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

package ru.klyubin.plurals.rules;

import ru.klyubin.plurals.Pluralization;
import ru.klyubin.plurals.PluralizationRule;

/**
 * Created by Timofey Klyubin on 01.10.2016.
 * t.klyubin@gmail.com
 */
public final class RussianPluralizationRule implements PluralizationRule {
    @Override
    public Pluralization select(int quantity) {
        if (quantity % 10 == 0) {
            return Pluralization.ZERO;
        }
        final int absQuantity = Math.abs(quantity);
        if (absQuantity % 10 == 1 && absQuantity % 100 != 11) {
            return Pluralization.ONE;
        }
        if (absQuantity % 10 < 5 && absQuantity % 100 != 11) {
            return Pluralization.FEW;
        }
        return Pluralization.MANY;
    }
}
