package com.sudhar.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WordAbbrevation {

//    Given an array of n distinct non-empty strings, you need to generate minimal possible abbreviations for every word following rules below.
//
//    Begin with the first character and then the number of characters abbreviated, which followed by the last character.
//    If there are any conflict, that is more than one words share the same abbreviation, a longer prefix is used instead of only the first character until making the map from word to abbreviation become unique. In other words, a final abbreviation cannot map to more than one original words.
//    If the abbreviation doesn't make the word shorter, then keep it as original.
//    Example:
//    Input: ["like", "god", "internal", "me", "internet", "interval", "intension", "face", "intrusion"]
//    Output: ["l2e","god","internal","me","i6t","interval","inte4n","f2e","intr4n"]
//    Note:
//    Both n and the length of each word will not exceed 400.
//    The length of each word is greater than 1.
//    The words consist of lowercase English letters only.
//    The return answers should be in the same order as the original array.

    class Solution1 {
        public List<String> wordsAbbreviation(List<String> words) {
            int N = words.size();
            String[] ans = new String[N];
            int[] prefix = new int[N];

            for (int i = 0; i < N; ++i)
                ans[i] = abbrev(words.get(i), 0);

            for (int i = 0; i < N; ++i) {
                while (true) {
                    Set<Integer> dupes = new HashSet();
                    for (int j = i+1; j < N; ++j)
                        if (ans[i].equals(ans[j]))
                            dupes.add(j);

                    if (dupes.isEmpty()) break;
                    dupes.add(i);
                    for (int k: dupes)
                        ans[k] = abbrev(words.get(k), ++prefix[k]);
                }
            }

            return Arrays.asList(ans);
        }

        public String abbrev(String word, int i) {
            int N = word.length();
            if (N - i <= 3) return word;
            return word.substring(0, i+1) + (N - i - 2) + word.charAt(N-1);
        }
    }

    class Solution2 {
        public List<String> wordsAbbreviation(List<String> words) {
            Map<String, List<IndexedWord>> groups = new HashMap();
            String[] ans = new String[words.size()];

            int index = 0;
            for (String word: words) {
                String ab = abbrev(word, 0);
                if (!groups.containsKey(ab))
                    groups.put(ab, new ArrayList());
                groups.get(ab).add(new IndexedWord(word, index));
                index++;
            }

            for (List<IndexedWord> group: groups.values()) {
                Collections.sort(group, (a, b) -> a.word.compareTo(b.word));

                int[] lcp = new int[group.size()];
                for (int i = 1; i < group.size(); ++i) {
                    int p = longestCommonPrefix(group.get(i-1).word, group.get(i).word);
                    lcp[i] = p;
                    lcp[i-1] = Math.max(lcp[i-1], p);
                }

                for (int i = 0; i < group.size(); ++i)
                    ans[group.get(i).index] = abbrev(group.get(i).word, lcp[i]);
            }

            return Arrays.asList(ans);
        }

        public String abbrev(String word, int i) {
            int N = word.length();
            if (N - i <= 3) return word;
            return word.substring(0, i+1) + (N - i - 2) + word.charAt(N-1);
        }

        public int longestCommonPrefix(String word1, String word2) {
            int i = 0;
            while (i < word1.length() && i < word2.length()
                   && word1.charAt(i) == word2.charAt(i))
                i++;
            return i;
        }
    }

    class IndexedWord {
        String word;
        int index;
        IndexedWord(String w, int i) {
            word = w;
            index = i;
        }
    }


    class Solution {
        public List<String> wordsAbbreviation(List<String> words) {
            Map<String, List<IndexedWord>> groups = new HashMap();
            String[] ans = new String[words.size()];

            int index = 0;
            for (String word: words) {
                String ab = abbrev(word, 0);
                if (!groups.containsKey(ab))
                    groups.put(ab, new ArrayList());
                groups.get(ab).add(new IndexedWord(word, index));
                index++;
            }

            for (List<IndexedWord> group: groups.values()) {
                TrieNode trie = new TrieNode();
                for (IndexedWord iw: group) {
                    TrieNode cur = trie;
                    for (char letter: iw.word.substring(1).toCharArray()) {
                        if (cur.children[letter - 'a'] == null)
                            cur.children[letter - 'a'] = new TrieNode();
                        cur.count++;
                        cur = cur.children[letter - 'a'];
                    }
                }

                for (IndexedWord iw: group) {
                    TrieNode cur = trie;
                    int i = 1;
                    for (char letter: iw.word.substring(1).toCharArray()) {
                        if (cur.count == 1) break;
                        cur = cur.children[letter - 'a'];
                        i++;
                    }
                    ans[iw.index] = abbrev(iw.word, i-1);
                }
            }

            return Arrays.asList(ans);
        }

        public String abbrev(String word, int i) {
            int N = word.length();
            if (N - i <= 3) return word;
            return word.substring(0, i+1) + (N - i - 2) + word.charAt(N-1);
        }

    }

    class TrieNode {
        TrieNode[] children;
        int count;
        TrieNode() {
            children = new TrieNode[26];
            count = 0;
        }
    }


}
