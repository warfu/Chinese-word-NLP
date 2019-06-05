/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/9/9 20:19</create-date>
 *
 * <copyright file="NGramDictionaryMaker.java">
 * This source is subject to the Apache License 2.0. see http://www.apache.org/licenses/LICENSE-2.0
 * </copyright>
 */
package org.pwstudio.nlp.corpus.dictionary;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

import org.pwstudio.nlp.collection.trie.bintrie.BinTrie;
import org.pwstudio.nlp.corpus.document.sentence.word.IWord;
import org.pwstudio.nlp.corpus.io.IOUtil;

import static org.pwstudio.nlp.utility.Predefine.logger;

/**
 * 2-gram词典制作工具
 *
 * @author hankcs
 */
public class NGramDictionaryMaker
{
    BinTrie<Integer> trie;
    /**
     * 转移矩阵
     */
    TMDictionaryMaker tmDictionaryMaker;

    public NGramDictionaryMaker()
    {
        trie = new BinTrie<Integer>();
        tmDictionaryMaker = new TMDictionaryMaker();
    }

    public void addPair(IWord first, IWord second)
    {
        String combine = first.getValue() + "@" + second.getValue();
        Integer frequency = trie.get(combine);
        if (frequency == null) frequency = 0;
        trie.put(combine, frequency + 1);
        // 同时还要统计标签的转移情况
        tmDictionaryMaker.addPair(first.getLabel(), second.getLabel());
    }

    /**
     * 保存NGram词典和转移矩阵
     *
     * @param path
     * @return
     */
    public boolean saveTxtTo(String path)
    {
        saveTransformMatrixToTxt(path + ".tr.txt");
        saveNGramToTxt(path + ".ngram.txt");
        return true;
    }

    /**
     * 保存NGram词典
     *
     * @param path
     * @return
     */
    public boolean saveNGramToTxt(String path)
    {
        try
        {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(IOUtil.newOutputStream(path)));
            for (Map.Entry<String, Integer> entry : trie.entrySet())
            {
                bw.write(entry.getKey() + " " + entry.getValue());
                bw.newLine();
            }
            bw.close();
        }
        catch (Exception e)
        {
            logger.warning("在保存NGram词典到" + path + "时发生异常" + e);
            return false;
        }

        return true;
    }

    /**
     * 保存转移矩阵
     *
     * @param path
     * @return
     */
    public boolean saveTransformMatrixToTxt(String path)
    {
        return tmDictionaryMaker.saveTxtTo(path);
    }
}