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
import org.pwstudio.nlp.dictionary.common.CommonSynonymDictionaryEx;
import org.pwstudio.nlp.dictionary.stopword.CoreStopWordDictionary;
import org.pwstudio.nlp.seg.common.Term;
import org.pwstudio.nlp.utility.TextUtility;

import static org.pwstudio.nlp.utility.Predefine.logger;
/**
 * 核心同义词词典(使用语义id作为value）
 *
 * @author hankcs
 */
public class CoreSynonymDictionaryEx
{
    static CommonSynonymDictionaryEx dictionary;

    static
    {
        try
        {
            dictionary = CommonSynonymDictionaryEx.create(IOUtil.newInputStream(PwNLP.Config.CoreSynonymDictionaryDictionaryPath));
        }
        catch (Exception e)
        {
            logger.severe("载入核心同义词词典失败");
            throw new IllegalArgumentException(e);
        }
    }

    public static Long[] get(String key)
    {
        return dictionary.get(key);
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
     * 将分词结果转换为同义词列表
     * @param sentence 句子
     * @param withUndefinedItem 是否保留词典中没有的词语
     * @return
     */
    public static List<Long[]> convert(List<Term> sentence, boolean withUndefinedItem)
    {
        List<Long[]> synonymItemList = new ArrayList<Long[]>(sentence.size());
        for (Term term : sentence)
        {
            // 除掉停用词
            if (term.nature == null) continue;
            String nature = term.nature.toString();
            char firstChar = nature.charAt(0);
            switch (firstChar)
            {
                case 'm':
                {
                    if (!TextUtility.isAllChinese(term.word)) continue;
                }break;
                case 'w':
                {
                    continue;
                }
            }
            // 停用词
            if (CoreStopWordDictionary.contains(term.word)) continue;
            Long[] item = get(term.word);
//            logger.trace("{} {}", wordResult.word, Arrays.toString(item));
            if (item == null)
            {
                if (withUndefinedItem)
                {
                    item = new Long[]{Long.MAX_VALUE / 3};
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