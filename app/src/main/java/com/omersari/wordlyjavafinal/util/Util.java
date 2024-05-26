package com.omersari.wordlyjavafinal.util;

import java.util.List;

public class Util {
    public int calculateSuccessRate(List<Boolean> boolList) {
        int totalCount = boolList.size();
        if(totalCount == 0) return 0;
        int successCount = 0;

        // true değerlerini say
        for (Boolean value : boolList) {
            if (value) {
                successCount++;
            }
        }

        // Başarı yüzdesini hesapla
        double successRate = (double) successCount / totalCount * 100;
        return Integer.valueOf((int) successRate);
    }
}
