package com.example.olympinav.Utils;

import androidx.annotation.ColorRes;

import com.example.olympinav.R;

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


  @ColorRes
  public static int getProgressBarColor(int progress) {
    return progress > 33 ? progress > 66 ? R.color.full : R.color.medium : R.color.quiet;
  }
}
