/*
 * <summary></summary>
 * <author>hankcs</author>
 * <email>me@hankcs.com</email>
 * <create-date>2015/5/7 11:04</create-date>
 *
 * <copyright file="HMMSegment.java">
 * Copyright (c) 2003-2015, hankcs. All Right Reserved, http://www.hankcs.com/
 * </copyright>
 */
package org.pwstudio.nlp.seg.HMM;

import java.util.LinkedList;
import java.util.List;

import org.pwstudio.nlp.PwNLP;
import org.pwstudio.nlp.corpus.io.ByteArray;
import org.pwstudio.nlp.model.trigram.CharacterBasedGenerativeModel;
import org.pwstudio.nlp.seg.CharacterBasedSegment;
import org.pwstudio.nlp.seg.common.Term;
import org.pwstudio.nlp.utility.GlobalObjectPool;
import org.pwstudio.nlp.utility.TextUtility;

import static org.pwstudio.nlp.utility.Predefine.logger;

/**
 * 基于2阶HMM（A Second-Order Hidden Markov Model, TriGram3阶文法模型）+ BMES序列标注的分词器
 *
 * @author hankcs
 */
public class HMMSegment extends CharacterBasedSegment
{
    CharacterBasedGenerativeModel model;

    public HMMSegment()
    {
        this(PwNLP.Config.HMMSegmentModelPath);
    }

    public HMMSegment(String modelPath)
    {
        model = GlobalObjectPool.get(modelPath);
        if (model != null) return;
        model = new CharacterBasedGenerativeModel();
        long start = System.currentTimeMillis();
        logger.info("开始从[ " + modelPath + " ]加载2阶HMM模型");
        try
        {
            ByteArray byteArray = ByteArray.createByteArray(modelPath);
            if (byteArray == null)
            {
                throw new IllegalArgumentException("HMM分词模型[ " + modelPath + " ]不存在");
            }
            model.load(byteArray);
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("发生了异常：" + TextUtility.exceptionToString(e));
        }
        logger.info("加载成功，耗时：" + (System.currentTimeMillis() - start) + " ms");
        GlobalObjectPool.put(modelPath, model);
    }

    @Override
    protected List<Term> roughSegSentence(char[] sentence)
    {
        char[] tag = model.tag(sentence);
        List<Term> termList = new LinkedList<Term>();
        int offset = 0;
        for (int i = 0; i < tag.length; offset += 1, ++i)
        {
            switch (tag[i])
            {
                case 'b':
                {
                    int begin = offset;
                    while (tag[i] != 'e')
                    {
                        offset += 1;
                        ++i;
                        if (i == tag.length)
                        {
                            break;
                        }
                    }
                    if (i == tag.length)
                    {
                        termList.add(new Term(new String(sentence, begin, offset - begin), null));
                    }
                    else
                        termList.add(new Term(new String(sentence, begin, offset - begin + 1), null));
                }
                break;
                default:
                {
                    termList.add(new Term(new String(sentence, offset, 1), null));
                }
                break;
            }
        }

        return termList;
    }
}
