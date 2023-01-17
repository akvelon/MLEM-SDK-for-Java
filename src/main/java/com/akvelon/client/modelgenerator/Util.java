package com.akvelon.client.modelgenerator;

import com.akvelon.client.util.Logger;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * A util class.
 */
class Util {
    /**
     * Generate a java classes for given class name and content.
     *
     * @param className java-class name.
     * @param content   a content for java-class.
     */
    public static void generateFile(String className, String content) {
        Logger.getInstance().log(System.Logger.Level.INFO, "GENERATING " + className);
        try {
            Path path = Paths.get(className);
            Files.deleteIfExists(path);
            if (!Files.exists(path.getParent())) {
                Files.createDirectory(path.getParent());
            }
            Files.write(path, content.getBytes(), StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            Logger.getInstance().log(System.Logger.Level.ERROR, "Error generating file", e);
        }
    }

    /**
     * Convert context data into String representation.
     *
     * @param context  a Java class description.
     * @param template mustache pattern file name
     * @return string representation of the java class.
     */
    public static String mustacheContent(Context context, String template) {
        try {
            StringWriter writer = new StringWriter();
            MustacheFactory mf = new DefaultMustacheFactory();
            Mustache mustache = mf.compile(template);
            mustache.execute(writer, context).flush();
            return writer.toString();
        } catch (IOException ex) {
            Logger.getInstance().log(System.Logger.Level.ERROR, "mustache error", ex);
        }
        return "";
    }

    /**
     * Makes the first letter capital.
     *
     * @param str the string to capitalize.
     * @return capitalized string.
     */
    public static String capitalize(String str) {
        if (!hasLength(str)) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * @param str the string to check.
     * @return true if a given str is not null or empty.
     */
    private static boolean hasLength(String str) {
        return str != null && !str.isEmpty();
    }

    /**
     * Convert the given string to correct class name.
     *
     * @param className the given string.
     * @return a java-class name.
     */
    public static String formatToJavaClass(String className) {
        StringBuilder formattedClassName = new StringBuilder(className.replaceAll("\\W", " ").replaceAll("_", " "));
        String[] strings = formattedClassName.toString().split(" ");
        formattedClassName = new StringBuilder();
        for (String string : strings) {
            formattedClassName.append(Util.capitalize(string));
        }

        return formattedClassName.toString();
    }
}
