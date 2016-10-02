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
import ru.klyubin.plurals.Pluralize;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Created by Timofey Klyubin on 01.10.2016.
 * t.klyubin@gmail.com
 */
public class TestPluralization {

    private Pluralize pluralize;
    private Locale russianLocale;

    @Before
    public void setUp() {
        pluralize = new Pluralize();
        russianLocale = new Locale.Builder().setLanguage("ru").setRegion("RU").build();
    }

    @Test
    public void testZero() {
        assertEquals("0 штук", pluralize.pluralize(0, russianLocale));
        assertEquals("0 items", pluralize.pluralize(0, Locale.ENGLISH));
    }

    @Test
    public void testOne() {
        assertEquals("1 штука", pluralize.pluralize(1, russianLocale));
        assertEquals("1 item", pluralize.pluralize(1, Locale.ENGLISH));
    }

    @Test
    public void testFew() {
        assertEquals("2 штуки", pluralize.pluralize(2, russianLocale));
        assertEquals("2 items", pluralize.pluralize(2, Locale.ENGLISH));

        assertEquals("3 штуки", pluralize.pluralize(3, russianLocale));
        assertEquals("3 items", pluralize.pluralize(3, Locale.ENGLISH));

        assertEquals("4 штуки", pluralize.pluralize(4, russianLocale));
        assertEquals("4 items", pluralize.pluralize(4, Locale.ENGLISH));
    }

    @Test
    public void testMany() {
        assertEquals("5 штук", pluralize.pluralize(5, russianLocale));
        assertEquals("5 items", pluralize.pluralize(5, Locale.ENGLISH));

        assertEquals("7 штук", pluralize.pluralize(7, russianLocale));
        assertEquals("7 items", pluralize.pluralize(7, Locale.ENGLISH));

        for (int i = 10; i < 21; i++) {
            assertEquals(String.valueOf(i) + " штук", pluralize.pluralize(i, russianLocale));
        }
    }
}
