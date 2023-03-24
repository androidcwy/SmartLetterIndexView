package com.smart.liv.utils

import net.sourceforge.pinyin4j.PinyinHelper
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination
import java.util.*
import java.util.regex.Pattern


/**
 * @author JoeYe
 * @date 2023/3/9 14:57
 */
object PinYinStringHelper {

    private val pinyinFormat = HanyuPinyinOutputFormat().let {
        it.caseType = HanyuPinyinCaseType.LOWERCASE
        it.toneType = HanyuPinyinToneType.WITHOUT_TONE
        it.vCharType = HanyuPinyinVCharType.WITH_V
        it
    }

    /**
     * 得到 全拼
     */
    fun getPingYin(src: String): String? {
        if (src.trim { it <= ' ' }.isEmpty()) {
            return null
        }
        val result = StringBuilder()
        try {
            val chinese = extractChinese(src)
            if (chinese.isNotEmpty()) {
                val chineseArray = toHanyuMutiPinyinStringArray(chinese)
                chineseArray.forEach {
                    result.append(it)
                }
            } else {
                result.append(src)
            }
        } catch (ignored: BadHanyuPinyinOutputFormatCombination) {
        }
        return result.toString().uppercase(Locale.getDefault())
    }

    fun getFirstPingYin(src: String): String? {
        if (src.trim { it <= ' ' }.isEmpty()) {
            return null
        }
        var result = StringBuilder()
        try {
            val chinese = extractChinese(src)
            if (chinese.isNotEmpty()) {
                val chineseArray = toHanyuMutiPinyinStringArray(chinese)
                chineseArray.forEach {
                    result.append(it[0])
                }
            } else {
                result.append(src)
            }
        } catch (ignored: BadHanyuPinyinOutputFormatCombination) {
        }
        return result.toString().uppercase(Locale.getDefault())
    }

    /**
     * 得到中文首字母缩写
     * 简拼
     */
    fun getPinYinHeadChar(str: String): String? {
        if (str.trim { it <= ' ' }.isEmpty()) {
            return null
        }
        val chinese = extractChinese(str)
        val result = if(chinese.isNotEmpty()){
            val pinyinArray = toHanyuMutiPinyinStringArray(chinese)
            pinyinArray[0][0].toString()
        } else {
            str[0].toString()
        }
        return result.uppercase(Locale.getDefault())
    }

    /**
     * 获得汉语拼音首字母
     */
    fun getAlpha(str: String?): String {
        if (str == null || str.trim { it <= ' ' }.isEmpty() || (!isHanzi(str) && !isLetter(str))) {
            return "#"
        }
        val c = str.trim { it <= ' ' }.substring(0, 1)[0]
        // 正则表达式，判断首字母是否是英文字母
        return if (isLetter(c.toString())) {
            (c.toString()).uppercase(Locale.getDefault())
        } else {
            //汉字转拼音，获取拼音首字母
            val headChar = getHeadChar(str)
            if (headChar?.isNotEmpty() == true) {
                headChar.substring(0, 1)
            } else {
                "#"
            }
        }
    }

    /**
     * 得到首字母
     */
    fun getHeadChar(str: String): String? {
        if (str.trim { it <= ' ' }.isEmpty()) {
            return null
        }
        val chinese = extractChinese(str)
        val result = if(chinese.isNotEmpty()){
            val pinyinArray = toHanyuMutiPinyinStringArray(chinese)
            pinyinArray[0][0].toString()
        } else {
            str[0].toString()
        }
        return result.uppercase(Locale.getDefault())
    }

    fun isHanzi(str: String?): Boolean {
        val c = str!![0]
        // 正则表达式，判断首字母是否是英文字母
        val pattern: Pattern = Pattern.compile("[\\u4E00-\\u9FA5]+")
        return pattern.matcher(c.toString()).matches()
    }

    fun isLetter(str: String): Boolean {
        val c = str[0]
        // 正则表达式，判断首字母是否是英文字母
        val pattern: Pattern = Pattern.compile("^[A-Za-z]+$")
        return pattern.matcher(c.toString()).matches()
    }

    fun isNumber(str: String): Boolean {
        val c = str[0]
        // 正则表达式，判断首字母是否是英文字母
        val pattern: Pattern = Pattern.compile("^[1-9]+$")
        return pattern.matcher(c.toString() + "").matches()
    }

    private fun extractChinese(input: String): String {
        val regex = "[\\u4E00-\\u9FA5]+"
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(input)
        var result = ""
        while (matcher.find()) {
            result += matcher.group()
        }
        return result
    }


    private fun toHanyuMutiPinyinStringArray(data: String): Array<String> {
        return PinyinHelper.toHanYuPinyinString(data, pinyinFormat, "_ARR_", false).split("_ARR_")
            .toTypedArray()
    }
}