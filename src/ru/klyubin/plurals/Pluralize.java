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

import org.apache.log4j.Logger;
import ru.klyubin.plurals.internal.StringBundle;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Timofey Klyubin on 01.10.2016.
 * t.klyubin@gmail.com
 *
 * !!!WARNING!!!
 * THIS CLASS IS FOR INTERNAL USE, PLEASE USE Pluralizator IN YOUR PROJECT
 */
public class Pluralize {

    private static final Logger LOG = Logger.getLogger(Pluralize.class);

    private PluralizationRuleProvider pluralizationRuleProvider;

    private StringBundle bundle;

    public Pluralize(StringBundle bundle) {
        this(bundle, new DefaultPluralizationRuleProvider());
    }

    public Pluralize(StringBundle bundle, PluralizationRuleProvider pluralizationRuleProvider) {
        this.pluralizationRuleProvider = pluralizationRuleProvider;
        this.bundle = bundle;
    }

    public static Pluralize getInstance(StringBundle bundle) {
        return new Pluralize(bundle);
    }

    public String pluralize(int quantity, Locale locale) {
        PluralizationRule rule = pluralizationRuleProvider.getRuleForLocale(locale);
        Pluralization pluralization = rule.select(quantity);
        final String rawString = bundle.getString(locale, pluralization);
        if (rawString == null) {
            throw new IllegalArgumentException("Pluralization strings for locale [".concat(locale.getDisplayLanguage()).concat("] does not exist!"));
        }
        return MessageFormat.format(rawString, quantity);
    }

    protected PluralizationRule getRuleForLocale(Locale locale) {
        return pluralizationRuleProvider.getRuleForLocale(locale);
    }

    public void setPluralizationRuleProvider(PluralizationRuleProvider pluralizationRuleProvider) {
        this.pluralizationRuleProvider = pluralizationRuleProvider;
    }
}
