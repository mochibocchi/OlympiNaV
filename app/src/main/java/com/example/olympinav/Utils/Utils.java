package com.example.olympinav.Utils;

public class Utils {


  public static String calculateWordForQuantity(String wordSingular, int count) {
    if (count == 1)
      return wordSingular;

    if (wordSingular.endsWith("y")) {
      return wordSingular.substring(0, wordSingular.length() - 1) + "ies";
    } else if (wordSingular.endsWith("s")) {
      return wordSingular + "es";
    } else {
      return wordSingular + "s";
    }
  }

}
