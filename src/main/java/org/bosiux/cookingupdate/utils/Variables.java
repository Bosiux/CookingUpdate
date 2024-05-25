package org.bosiux.cookingupdate.utils;

public class Variables {

    public static String getTitle(){
        return  "                                                                                     \n" +
                "     ____            _    _             _   _           _       _                    \n" +
                "    / ___|___   ___ | | _(_)_ __   __ _| | | |_ __   __| | __ _| |_ ___              \n" +
                "   | |   / _ \\ / _ \\| |/ / | '_ \\ / _` | | | | '_ \\ / _` |/ _` | __/ _ \\        \n" +
                "   | |__| (_) | (_) |   <| | | | | (_| | |_| | |_) | (_| | (_| | ||  __/             \n" +
                "    \\____\\___/ \\___/|_|\\_\\_|_| |_|\\__, |\\___/| .__/ \\__,_|\\__,_|\\__\\___|  \n" +
                "                                  |___/      |_|                                     \n" +
                getVersion() +
                getCredits() ;

    }

    private static String getVersion(){
        return  "                                      _   ___                                        \n"+
                "                                     / | / _ \\                                      \n" +
                "                                     | || | | |                                      \n" +
                "                                     | || | | |                                      \n" +
                "                                     |_(_)___/                                        ";
    }

    public static String getCredits(){
        return  "                                                                           \n" +
                "   +----+-----------+-----------------------------------------------------+\n" +
                "   | ID |  Profile  |                        Link                         |\n" +
                "   +----+-----------+-----------------------------------------------------+\n" +
                "   |  1 |  GitHub   | https://github.com/Bosiux                           |\n" +
                "   |  2 |  LinkedIn | https://www.linkedin.com/in/davide-bosio-47872930b/ |\n" +
                "   |  3 |  Twitter  | ----                                                |\n" +
                "   |  4 |  Website  | ----                                                |\n" +
                "   +----+-----------+-----------------------------------------------------+\n" +
                "                                                ";

    }
}
