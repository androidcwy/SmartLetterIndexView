package com.smart.liv.comparator

import com.smart.liv.model.LetterSortModel
import com.smart.liv.utils.PinYinStringHelper
import com.smart.liv.utils.PinYinStringHelper.isHanzi
import com.smart.liv.utils.PinYinStringHelper.isLetter
import com.smart.liv.utils.PinYinStringHelper.isNumber

/**
 * @author JoeYe
 * @date 2023/3/9 14:50
 */
open class SortComparator : Comparator<LetterSortModel> {
    private var compareCallNum = 0

    override fun compare(o1: LetterSortModel, o2: LetterSortModel): Int {
        compareCallNum = 0
        return compareString((o1).name, (o2).name)
    }

    private fun compareString(o1: String, o2: String): Int {
        compareCallNum++
        //若存在长度为0的情况：
        if (o1.isEmpty() && o2.isEmpty()) {
            return 0
        } else if (o1.isEmpty()) {
            return -1
        } else if (o2.isEmpty()) {
            return 1
        }
        val firstStrA = o1.substring(0, 1)
        val firstStrB = o2.substring(0, 1)
        var typeA = getFirstCharType(o1)
        var typeB = getFirstCharType(o2)
        if (typeA > typeB) {
            return -1 //返回负值，则往前排
        } else if (typeA < typeB) {
            return 1
        }

        //类型相同，需要进行进一步的比较
        var compareResult: Int
        if (typeA < 9 && typeB < 9) {
            compareResult = firstStrA.compareTo(firstStrB)
            return if (compareResult != 0) {
                //若不同，立即出来比较结果
                compareResult
            } else {
                //若相同，则递归调用
                compareString(o1.substring(1), o2.substring(1))
            }
        }

        //是字母或汉字

        //若是首字母，先用第一个字母或拼音进行比较
        //否则，先判断字符类型
        val firstPinyinA: String = PinYinStringHelper.getFirstPingYin(o1)!!.substring(0, 1)
        val firstPinyinB: String = PinYinStringHelper.getFirstPingYin(o2)!!.substring(0, 1)

        if (compareCallNum == 1) {
            compareResult = firstPinyinA.compareTo(firstPinyinB)
            if (compareResult != 0) {
                return compareResult
            }
        }
        //若首字的第一个字母相同，或不是首字，判断原字符是汉字还是字母，汉字排在前面
        typeA = getFirstCharType2(o1)
        typeB = getFirstCharType2(o2)
        if (typeA > typeB) {
            return -1
        } else if (typeA < typeB) {
            return 1
        }

        //不是首字母，在字符类型之后判断，第一个字母或拼音进行比较
        if (compareCallNum != 1) {
            compareResult = firstPinyinA.compareTo(firstPinyinB)
            if (compareResult != 0) {
                return compareResult
            }
        }
        if (isLetter(o1) && isLetter(o2)) {
            //若是同一个字母，还要比较下大小写
            compareResult = firstStrA.compareTo(firstStrB)
            if (compareResult != 0) {
                return compareResult
            }
        }
        if (isHanzi(o1) && isHanzi(o2)) {
            //使用姓的拼音进行比较
//            compareResult = firstPinyinA.compareTo(firstPinyinB);
            compareResult = PinYinStringHelper.getFirstPingYin(o1)!!
                .compareTo(PinYinStringHelper.getFirstPingYin(o2)!!)
            if (compareResult != 0) {
                return compareResult
            }

            //若姓的拼音相同，比较汉字是否相同
            compareResult = firstStrA.compareTo(firstStrB)
            if (compareResult != 0) {
                return compareResult
            }
        }
        //若相同，则进行下一个字符的比较（递归调用）
        return compareString(o1.substring(1), o2.substring(1))
    }

    private fun getFirstCharType2(str: String): Int {
        return if (isHanzi(str)) {
            10
        } else if (isLetter(str)) {
            9
        } else if (isNumber(str)) {
            8
        } else {
            0
        }
    }

    private fun getFirstCharType(str: String): Int {
        return if (isHanzi(str)) {
            10
        } else if (isLetter(str)) {
            10
        } else if (isNumber(str)) {
            8
        } else {
            0
        }
    }

}