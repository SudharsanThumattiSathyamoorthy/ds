package com.sudhar.examples;

public class CoinsChangeDP {

    public static void main(final String[] args) {
        System.out.println(coinsChange(5, new int[] {1, 2, 5}));
    }

    private static int coinsChange(int amount, int[] coins) {

        int result[] = new int[amount+1];
        result[0] = 1;

        for (int coin: coins) {
            for (int i = 1; i <= amount; i++) {
               if (i >= coin) {
                    result[i] += result[i - coin];
               }
            }
        }
        return result[amount];
    }
}
