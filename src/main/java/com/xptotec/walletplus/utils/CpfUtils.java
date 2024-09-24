package com.xptotec.walletplus.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;

public class CpfUtils {


    public static String cleanCpf(String cpf) {
        if (cpf == null) {
            return null;
        }
        String cleanedCpf = cpf.replaceAll("[^\\d]", "");
        return String.format("%011d", new BigInteger(cleanedCpf));
    }

    public static boolean isValidCpf(String cpf) {
        String cleanedCpf = cleanCpf(cpf);
        return StringUtils.isNoneEmpty(cleanedCpf) && cleanedCpf.matches("^[0-9]{11}$");
    }
}