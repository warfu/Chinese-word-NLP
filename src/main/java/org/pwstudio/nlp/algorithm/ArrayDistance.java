/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/9/17 9:47</create-date>
 *
 * <copyright file="BinSearch.java">
 * This source is subject to the Apache License 2.0. see http://www.apache.org/licenses/LICENSE-2.0
 * </copyright>
 */
package org.pwstudio.nlp.algorithm;

import java.util.TreeSet;

/**
 * 求两个集合中最相近的两个数
 *
 * @author hankcs
 */
public class ArrayDistance
{
    public static Long computeMinimumDistance(TreeSet<Long> setA, TreeSet<Long> setB)
    {
        Long[] arrayA = setA.toArray(new Long[0]);
        Long[] arrayB = setB.toArray(new Long[0]);
       return computeMinimumDistance(arrayA, arrayB);
    }

    public static Long computeMinimumDistance(Long[] arrayA, Long[] arrayB)
    {
        int aIndex = 0;
        int bIndex = 0;
        long min = Math.abs(arrayA[0] - arrayB[0]);
        while (true)
        {
            if (arrayA[aIndex] > arrayB[bIndex])
            {
                bIndex++;
            }
            else
            {
                aIndex++;
            }
            if (aIndex >= arrayA.length || bIndex >= arrayB.length)
            {
                break;
            }
            if (Math.abs(arrayA[aIndex] - arrayB[bIndex]) < min)
            {
                min = Math.abs(arrayA[aIndex] - arrayB[bIndex]);
            }
        }

        return min;
    }

    public static Long computeAverageDistance(Long[] arrayA, Long[] arrayB)
    {
        Long totalA = 0L;
        Long totalB = 0L;
        for (Long a : arrayA) totalA += a;
        for (Long b : arrayB) totalB += b;

        return Math.abs(totalA / arrayA.length - totalB / arrayB.length);
    }
}
