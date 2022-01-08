package com.boots.helpers;

import com.boots.entity.ETransactionTypes;

public class TransactionHelper {
    public static Double getNewAmount(
            ETransactionTypes oldTransactionType,
            ETransactionTypes newTransactionType,
            Double oldTransactionAmount,
            Double newTransactionAmount,
            Double oldAmount
    ) {
        Double result = oldAmount;

        if (newTransactionType == ETransactionTypes.income) {
            result += newTransactionAmount +
                    oldTransactionAmount * (oldTransactionType == ETransactionTypes.income ? -1 : 1);
        } else {
            result -= (newTransactionAmount + oldTransactionAmount * (
                    oldTransactionType == ETransactionTypes.income ? 1 : -1
            ));
        }

        return result;
    }
}
