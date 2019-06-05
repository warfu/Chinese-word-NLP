/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/11/6 16:02</create-date>
 *
 * <copyright file="SingleString2PinyinConverter.java">
 * This source is subject to the Apache License 2.0. see http://www.apache.org/licenses/LICENSE-2.0
 * </copyright>
 */
package org.pwstudio.nlp.dictionary.py;

import java.util.*;

import org.pwstudio.nlp.algorithm.ahocorasick.trie.Token;
import org.pwstudio.nlp.algorithm.ahocorasick.trie.Trie;

/**
 * 将类似āiyā的词语转为拼音的转换器
 * @author hankcs
 */
public class TonePinyinString2PinyinConverter
{
    /**
     * 带音调的字母到Pinyin的map
     */
    static Map<String, Pinyin> mapKey;
    /**
     * 带数字音调的字幕到Pinyin的map
     */
    static Map<String, Pinyin> mapNumberKey;
    static Trie trie;
    static
    {
        mapNumberKey = new TreeMap<String, Pinyin>();
        mapKey = new TreeMap<String, Pinyin>();
        for (Pinyin pinyin : Integer2PinyinConverter.pinyins)
        {
            mapNumberKey.put(pinyin.toString(), pinyin);
            String pinyinWithToneMark = pinyin.getPinyinWithToneMark();
            String pinyinWithoutTone = pinyin.getPinyinWithoutTone();
            Pinyin tone5 = String2PinyinConverter.convert2Tone5(pinyin);
            mapKey.put(pinyinWithToneMark, pinyin);
            mapKey.put(pinyinWithoutTone, tone5);
        }
        trie = new Trie().remainLongest();
        trie.addAllKeyword(mapKey.keySet());
    }

    /**
     * 这个拼音是否合格
     * @param singlePinyin
     * @return
     */
    public static boolean valid(String singlePinyin)
    {
        if (mapNumberKey.containsKey(singlePinyin)) return true;

        return false;
    }

    public static Pinyin convertFromToneNumber(String singlePinyin)
    {
        return mapNumberKey.get(singlePinyin);
    }

    public static List<Pinyin> convert(String[] pinyinArray)
    {
        List<Pinyin> pinyinList = new ArrayList<Pinyin>(pinyinArray.length);
        for (int i = 0; i < pinyinArray.length; i++)
        {
            pinyinList.add(mapKey.get(pinyinArray[i]));
        }

        return pinyinList;
    }

    public static Pinyin convert(String singlePinyin)
    {
        return mapKey.get(singlePinyin);
    }

    /**
     *
     * @param tonePinyinText
     * @return
     */
    public static List<Pinyin> convert(String tonePinyinText, boolean removeNull)
    {
        List<Pinyin> pinyinList = new LinkedList<Pinyin>();
        Collection<Token> tokenize = trie.tokenize(tonePinyinText);
        for (Token token : tokenize)
        {
            Pinyin pinyin = mapKey.get(token.getFragment());
            if (removeNull && pinyin == null) continue;
            pinyinList.add(pinyin);
        }

        return pinyinList;
    }

    /**
     * 这些拼音是否全部合格
     * @param pinyinStringArray
     * @return
     */
    public static boolean valid(String[] pinyinStringArray)
    {
        for (String p : pinyinStringArray)
        {
            if (!valid(p)) return false;
        }

        return true;
    }

    public static List<Pinyin> convertFromToneNumber(String[] pinyinArray)
    {
        List<Pinyin> pinyinList = new ArrayList<Pinyin>(pinyinArray.length);
        for (String py : pinyinArray)
        {
            pinyinList.add(convertFromToneNumber(py));
        }
        return pinyinList;
    }
}
