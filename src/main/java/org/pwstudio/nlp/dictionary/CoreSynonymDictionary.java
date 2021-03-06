/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/9/13 13:12</create-date>
 *
 * <copyright file="CoreSynonymDictionary.java">
 * This source is subject to the Apache License 2.0. see http://www.apache.org/licenses/LICENSE-2.0
 * </copyright>
 */
package org.pwstudio.nlp.dictionary;

import java.util.ArrayList;
import java.util.List;

import org.pwstudio.nlp.PwNLP;
import org.pwstudio.nlp.algorithm.EditDistance;
import org.pwstudio.nlp.corpus.io.IOUtil;
import org.pwstudio.nlp.dictionary.common.CommonSynonymDictionary;
import org.pwstudio.nlp.seg.common.Term;

import static org.pwstudio.nlp.utility.Predefine.logger;
/**
 * 核心同义词词典
 *
 * @author hankcs
 */
public class CoreSynonymDictionary
{
    static CommonSynonymDictionary dictionary;

    static
    {
        try
        {
            long start = System.currentTimeMillis();
            dictionary = CommonSynonymDictionary.create(IOUtil.newInputStream(PwNLP.Config.CoreSynonymDictionaryDictionaryPath));
            logger.info("载入核心同义词词典成功，耗时 " + (System.currentTimeMillis() - start) + " ms");
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("载入核心同义词词典失败" + e);
        }
    }

    /**
     * 获取一个词的同义词（意义完全相同的，即{@link org.pwstudio.nlp.dictionary.common.CommonSynonymDictionary.SynonymItem#type}
     * == {@link org.pwstudio.nlp.corpus.synonym.Synonym.Type#EQUAL}的）列表
     * @param key
     * @return
     */
    public static CommonSynonymDictionary.SynonymItem get(String key)
    {
        return dictionary.get(key);
    }

    /**
     * 不分词直接转换
     * @param text
     * @return
     */
    public static String rewriteQuickly(String text)
    {
        return dictionary.rewriteQuickly(text);
    }

    public static String rewrite(String text)
    {
        return dictionary.rewrite(text);
    }

    /**
     * 语义距离
     * @param itemA
     * @param itemB
     * @return
     */
    public static long distance(CommonSynonymDictionary.SynonymItem itemA, CommonSynonymDictionary.SynonymItem itemB)
    {
        return itemA.distance(itemB);
    }

    /**
     * 判断两个单词之间的语义距离
     * @param A
     * @param B
     * @return
     */
    public static long distance(String A, String B)
    {
        CommonSynonymDictionary.SynonymItem itemA = get(A);
        CommonSynonymDictionary.SynonymItem itemB = get(B);
        if (itemA == null || itemB == null) return Long.MAX_VALUE;

        return distance(itemA, itemB);
    }

    /**
     * 计算两个单词之间的相似度，0表示不相似，1表示完全相似
     * @param A
     * @param B
     * @return
     */
    public static double similarity(String A, String B)
    {
        long distance = distance(A, B);
        if (distance > dictionary.getMaxSynonymItemIdDistance()) return 0.0;

        return (dictionary.getMaxSynonymItemIdDistance() - distance) / (double) dictionary.getMaxSynonymItemIdDistance();
    }

    /**
     * 将分词结果转换为同义词列表
     * @param sentence 句子
     * @param withUndefinedItem 是否保留词典中没有的词语
     * @return
     */
    public static List<CommonSynonymDictionary.SynonymItem> convert(List<Term> sentence, boolean withUndefinedItem)
    {
        List<CommonSynonymDictionary.SynonymItem> synonymItemList = new ArrayList<CommonSynonymDictionary.SynonymItem>(sentence.size());
        for (Term term : sentence)
        {
            CommonSynonymDictionary.SynonymItem item = get(term.word);
            if (item == null)
            {
                if (withUndefinedItem)
                {
                    item = CommonSynonymDictionary.SynonymItem.createUndefined(term.word);
                    synonymItemList.add(item);
                }

            }
            else
            {
                synonymItemList.add(item);
            }
        }

        return synonymItemList;
    }

    /**
     * 获取语义标签
     * @return
     */
    public static long[] getLexemeArray(List<CommonSynonymDictionary.SynonymItem> synonymItemList)
    {
        long[] array = new long[synonymItemList.size()];
        int i = 0;
        for (CommonSynonymDictionary.SynonymItem item : synonymItemList)
        {
            array[i++] = item.entry.id;
        }
        return array;
    }


    public long distance(List<CommonSynonymDictionary.SynonymItem> synonymItemListA, List<CommonSynonymDictionary.SynonymItem> synonymItemListB)
    {
        return EditDistance.compute(synonymItemListA, synonymItemListB);
    }

    public long distance(long[] arrayA, long[] arrayB)
    {
        return EditDistance.compute(arrayA, arrayB);
    }
}
