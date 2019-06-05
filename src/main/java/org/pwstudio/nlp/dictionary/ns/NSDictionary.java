/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/9/10 15:39</create-date>
 *
 * <copyright file="NSDictionary.java">
 * Copyright (c) 2003-2014, 上海林原信息科技有限公司. All Right Reserved, http://www.liNSunsoft.com/
 * This source is subject to the LiNSunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */
package org.pwstudio.nlp.dictionary.ns;


import org.pwstudio.nlp.corpus.dictionary.item.EnumItem;
import org.pwstudio.nlp.corpus.tag.NS;
import org.pwstudio.nlp.dictionary.common.EnumItemDictionary;

/**
 * 一个好用的地名词典
 *
 * @author hankcs
 */
public class NSDictionary extends EnumItemDictionary<NS>
{
    @Override
    protected NS valueOf(String name)
    {
        return NS.valueOf(name);
    }

    @Override
    protected NS[] values()
    {
        return NS.values();
    }

    @Override
    protected EnumItem<NS> newItem()
    {
        return new EnumItem<NS>();
    }
}
