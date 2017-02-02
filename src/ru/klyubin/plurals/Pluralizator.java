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
import ru.klyubin.plurals.util.UTF8Control;

import java.util.*;

/**
 * Created by Timofey Klyubin on 01.10.2016.
 * t.klyubin@gmail.com
 */
public class Pluralizator {

    private static final Logger LOG = Logger.getLogger(Pluralizator.class);

    private static final String CONFIG_PROPERTIES = "application";
    private static final String PLURALS_PROPERTIES = "plurals";

    private static final String AUTOLOAD_KEY = "ru.klyubin.plurals.autoload";
    private static final boolean DEFAULT_AUTOLOAD = true;

    private static final String PREFIX_KEY = "ru.klyubin.plurals.prefix";
    private static final String DEFAULT_PREFIX = "java.pluralization";

    private static final String RULE_PROVIDER_KEY = "ru.klyubin.plurals.rule.provider";
    private static final Class<? extends PluralizationRuleProvider> DEFAULT_RULE_PROVIDER = DefaultPluralizationRuleProvider.class;

    private final static Map<String, Pluralize> PLURALIZE_MAP;

    private static ResourceBundle internalBundle = null;
    private static Class<? extends PluralizationRuleProvider> pluralizationRuleProviderClass = DefaultPluralizationRuleProvider.class;
    static {
        PLURALIZE_MAP = new HashMap<>();
        LOG.debug("Start initializing pluralizator...");

        boolean autoload = DEFAULT_AUTOLOAD;
        String prefix = DEFAULT_PREFIX;
        try {
            internalBundle = ResourceBundle.getBundle(CONFIG_PROPERTIES, UTF8Control.getInstance());
            autoload = internalBundle.containsKey(AUTOLOAD_KEY)
                    ? Boolean.parseBoolean(internalBundle.getString(AUTOLOAD_KEY))
                    : DEFAULT_AUTOLOAD;

            prefix = internalBundle.containsKey(PREFIX_KEY)
                    ? internalBundle.getString(PREFIX_KEY)
                    : DEFAULT_PREFIX;

        } catch (MissingResourceException e) {
            LOG.warn(e.getMessage(), e);
            LOG.warn("Falling back to default values...");
        }

        if (autoload) {
            initializeInternal(prefix);
        }
    }

    private static synchronized void initializeInternal(String prefix) {
        try {
            if (internalBundle.containsKey(RULE_PROVIDER_KEY)) {
                Class<?> loaded = Pluralizator.class.getClassLoader().loadClass(internalBundle.getString(RULE_PROVIDER_KEY));
                if (loaded.isAssignableFrom(PluralizationRuleProvider.class)) {
                    pluralizationRuleProviderClass = loaded.asSubclass(PluralizationRuleProvider.class);
                }
            }
        } catch (ClassNotFoundException e) {
            LOG.error("Couldn't find rule provider class [" + internalBundle.getString(RULE_PROVIDER_KEY) +
                    "], falling back to default one.", e);
            pluralizationRuleProviderClass = DEFAULT_RULE_PROVIDER;
        }

        try {
            Map<String, StringBundle> stringBundleMap = new HashMap<>();
            for (Locale locale : Locale.getAvailableLocales()) {
                try {
                    ResourceBundle pluralsBundle = ResourceBundle.getBundle(PLURALS_PROPERTIES, locale, UTF8Control.getInstance());
                    for (String key : pluralsBundle.keySet()) {
                        if (key.startsWith(prefix + ".")) {
                            String mapKey = key.replace(prefix + ".", "");
                            mapKey = mapKey.substring(0, mapKey.lastIndexOf("."));
                            if (stringBundleMap.get(mapKey) == null) {
                                stringBundleMap.put(mapKey, new StringBundle());
                            }
                            StringBundle currentStringBundle = stringBundleMap.get(mapKey);
                            if (currentStringBundle.getInternalMap().get(locale) == null) {
                                currentStringBundle.getInternalMap().put(locale, new HashMap<Pluralization, String>());
                            }
                            Map<Pluralization, String> currentPluralization = currentStringBundle.getInternalMap().get(locale);
                            currentPluralization.put(Pluralization.ZERO, pluralsBundle.getString(prefix + "." + mapKey + "." + Pluralization.ZERO));
                            currentPluralization.put(Pluralization.ONE, pluralsBundle.getString(prefix + "." + mapKey + "." + Pluralization.ONE));
                            currentPluralization.put(Pluralization.FEW, pluralsBundle.getString(prefix + "." + mapKey + "." + Pluralization.FEW));
                            currentPluralization.put(Pluralization.TWO, pluralsBundle.getString(prefix + "." + mapKey + "." + Pluralization.TWO));
                            currentPluralization.put(Pluralization.MANY, pluralsBundle.getString(prefix + "." + mapKey + "." + Pluralization.MANY));
                        }
                    }
                } catch (MissingResourceException e) {
                    LOG.debug("Didn't find " + PLURALS_PROPERTIES + ".properties for locale [" + locale.getLanguage() + "]");
                }
            }

            for (Map.Entry<String, StringBundle> stringBundleEntry : stringBundleMap.entrySet()) {
                registerPluralString(stringBundleEntry.getKey(), stringBundleEntry.getValue());
            }
        } catch (MissingResourceException e) {
            LOG.info("Couldn't find " + PLURALS_PROPERTIES + ".properties, plurals data is empty. [" + e.getMessage() + "]");
        }
    }

    public static void initialize() {
        initializeInternal(internalBundle.containsKey(PREFIX_KEY)
                ? internalBundle.getString(PREFIX_KEY)
                : DEFAULT_PREFIX);
    }

    private static synchronized void registerPluralString(String key, StringBundle stringBundle) {
        final Pluralize newInstance = Pluralize.getInstance(stringBundle);
        try {
            newInstance.setPluralizationRuleProvider(pluralizationRuleProviderClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            LOG.error("Couldn't register pluralization for key [" + key + "] with custom rule provider.", e);
            throw new RuntimeException(e);
        }

        PLURALIZE_MAP.put(key, newInstance);
    }

    public static String pluralize(String key, int quantity, Locale locale) {
        if (!PLURALIZE_MAP.containsKey(key)) {
            throw new PluralizationNotFoundException(key);
        }

        return PLURALIZE_MAP.get(key).pluralize(quantity, locale);
    }

    public static String pluralize(String key, int quantity) {
        if (!PLURALIZE_MAP.containsKey(key)) {
            throw new PluralizationNotFoundException(key);
        }

        LOG.debug("Default locale is:" + Locale.getDefault());
        return PLURALIZE_MAP.get(key).pluralize(quantity, Locale.getDefault());
    }



    public static class PluralizationNotFoundException extends RuntimeException {
        public PluralizationNotFoundException(String message) {
            super(message);
        }

        public PluralizationNotFoundException() {
        }
    }
}
