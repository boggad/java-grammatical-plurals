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

import org.junit.Before;
import org.junit.Test;
import ru.klyubin.plurals.Pluralization;
import ru.klyubin.plurals.Pluralizator;
import ru.klyubin.plurals.Pluralize;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Created by Timofey Klyubin on 01.10.2016.
 * t.klyubin@gmail.com
 */
public class TestPluralization {

    private Locale russianLocale;
    private static final String TEST_KEY = "test";

    @Before
    public void setUp() {
        russianLocale = new Locale.Builder().setLanguage("ru").setRegion("RU").build();
    }

    @Test
    public void testZero() {
        assertEquals("0 штук", Pluralizator.pluralize(TEST_KEY, 0, russianLocale));
        assertEquals("0 items", Pluralizator.pluralize(TEST_KEY, 0, Locale.ENGLISH));
    }

    @Test
    public void testOne() {
        assertEquals("1 штука", Pluralizator.pluralize(TEST_KEY, 1, russianLocale));
        assertEquals("1 item", Pluralizator.pluralize(TEST_KEY, 1, Locale.ENGLISH));
    }

    @Test
    public void testFew() {
        assertEquals("2 штуки", Pluralizator.pluralize(TEST_KEY, 2, russianLocale));
        assertEquals("2 items", Pluralizator.pluralize(TEST_KEY, 2, Locale.ENGLISH));

        assertEquals("3 штуки", Pluralizator.pluralize(TEST_KEY, 3, russianLocale));
        assertEquals("3 items", Pluralizator.pluralize(TEST_KEY, 3, Locale.ENGLISH));

        assertEquals("4 штуки", Pluralizator.pluralize(TEST_KEY, 4, russianLocale));
        assertEquals("4 items", Pluralizator.pluralize(TEST_KEY, 4, Locale.ENGLISH));
    }

    @Test
    public void testMany() {
        assertEquals("5 штук", Pluralizator.pluralize(TEST_KEY, 5, russianLocale));
        assertEquals("5 items", Pluralizator.pluralize(TEST_KEY, 5, Locale.ENGLISH));

        assertEquals("7 штук", Pluralizator.pluralize(TEST_KEY, 7, russianLocale));
        assertEquals("7 items", Pluralizator.pluralize(TEST_KEY, 7, Locale.ENGLISH));

        for (int i = 10; i < 21; i++) {
            assertEquals(String.valueOf(i) + " штук", Pluralizator.pluralize(TEST_KEY, i, russianLocale));
        }
    }

    @Test
    public void testDifferentKey() {
        assertEquals("Осталось всего 2 дня отпуска :(", Pluralizator.pluralize("vacation", 2, russianLocale));
        assertEquals("Осталось всего 5 дней отпуска :(", Pluralizator.pluralize("vacation", 5, russianLocale));
        assertEquals("Остался всего 21 день отпуска :(", Pluralizator.pluralize("vacation", 21, russianLocale));
        assertEquals("Осталось всего 150 дней отпуска :(", Pluralizator.pluralize("vacation", 150, russianLocale));
        assertEquals("It's only 2 days of vacation left :(", Pluralizator.pluralize("vacation", 2, Locale.ENGLISH));
    }
}
