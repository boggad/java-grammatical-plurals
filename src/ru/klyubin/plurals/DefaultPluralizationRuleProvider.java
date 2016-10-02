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

package ru.klyubin.plurals;

import ru.klyubin.plurals.rules.EnglishPluralizationRule;
import ru.klyubin.plurals.rules.RussianPluralizationRule;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Timofey Klyubin on 01.10.2016.
 * t.klyubin@gmail.com
 */
public class DefaultPluralizationRuleProvider implements PluralizationRuleProvider {

    private final static Map<Locale, PluralizationRule> DEFAULT_RULES;
    static {
        DEFAULT_RULES = new HashMap<>();
        DEFAULT_RULES.put(Locale.ENGLISH, new EnglishPluralizationRule());
        DEFAULT_RULES.put(new Locale.Builder().setLanguage("ru").setRegion("RU").build(), new RussianPluralizationRule());
    }

    @Override
    public PluralizationRule getRuleForLocale(Locale locale) {
        if (!DEFAULT_RULES.containsKey(locale)) {
            throw new IllegalArgumentException("There's no PluralizationRule for specified locale: " + locale.getDisplayName());
        }
        return DEFAULT_RULES.get(locale);
    }

}
