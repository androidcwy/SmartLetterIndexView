package com.smart.liv.model

import com.smart.liv.const.Constants
import com.smart.liv.utils.PinYinStringHelper

/**
 * @author JoeYe
 * @date 2023/3/8 15:02
 */
data class LetterSortModel(val name: String, val type: Int = Constants.TYPE_NORMAL){
    //全拼
    var pinyin: String? = null
        get() {
            if (field == null) {
                field = PinYinStringHelper.getPingYin(name)
            }
            return field
        }

    //大写首字母或特殊字符
    var word: String = ""
        get() {
            field = PinYinStringHelper.getAlpha(name)
            return field
        }

    //简拼
    var jianpin: String? = null
        get() {
            if (field == null) {
                field = PinYinStringHelper.getPinYinHeadChar(name)
            }
            return field
        }
}
