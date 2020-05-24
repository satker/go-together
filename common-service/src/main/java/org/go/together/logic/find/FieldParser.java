package org.go.together.logic.find;

public class FieldParser {
    private static String[] getParsedString(String string) {
        return string.split("&");
    }

    public static String getLocalEntityField(String string) {
        return getParsedString(string)[0];
    }

    public static String getRemoteFieldToSearch(String string) {
        return getParsedString(string)[1];
    }

    public static String getRemoteGetField(String string) {
        return getParsedString(string)[2];
    }

    public static String getServiceName(String string) {
        return getRemoteFieldToSearch(string).split("\\.", 2)[0];
    }

    public static String getFieldSearch(String string) {
        return getRemoteFieldToSearch(string).split("\\.", 2)[1];
    }

    public static boolean isFieldParsed(String string) {
        return getParsedString(string).length == 3;
    }
}
