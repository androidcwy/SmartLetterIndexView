package com.smart.example.letterindexview

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.smart.example.letterindexview.model.CitySortModel
import com.smart.liv.LetterSortLayout
import com.smart.liv.comparator.SortComparator
import com.smart.liv.const.Constants
import com.smart.liv.model.LetterSortModel
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * @author JoeYe
 * @date 2023/3/8 17:04
 */
class MainActivity: AppCompatActivity() {

    private val sortComparator: SortComparator = SortComparator()

    val lsl: LetterSortLayout by lazy {
        findViewById(R.id.lsl)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var data = mutableListOf<LetterSortModel>(
            LetterSortModel("广西省", Constants.TYPE_NORMAL),
            LetterSortModel("广东省", Constants.TYPE_NORMAL),
            LetterSortModel("澳门", Constants.TYPE_NORMAL),
            LetterSortModel("广安省", Constants.TYPE_NORMAL),
            LetterSortModel("广安省", Constants.TYPE_NORMAL),
            LetterSortModel("广安省", Constants.TYPE_NORMAL),
            LetterSortModel("广安省", Constants.TYPE_NORMAL),
            LetterSortModel("广安省", Constants.TYPE_NORMAL),
            LetterSortModel("广安省", Constants.TYPE_NORMAL),
            LetterSortModel("广安省", Constants.TYPE_NORMAL),
            LetterSortModel("广安省", Constants.TYPE_NORMAL),
            LetterSortModel("广安省", Constants.TYPE_NORMAL),
            LetterSortModel("广安省", Constants.TYPE_NORMAL),
            LetterSortModel("广安省", Constants.TYPE_NORMAL),
            LetterSortModel("广安省", Constants.TYPE_NORMAL),
            LetterSortModel("广安省", Constants.TYPE_NORMAL),
            LetterSortModel("广安省", Constants.TYPE_NORMAL),
            LetterSortModel("广安省", Constants.TYPE_NORMAL),
            LetterSortModel("广安省", Constants.TYPE_NORMAL),
            LetterSortModel("四川省", Constants.TYPE_NORMAL),
            LetterSortModel("四川省", Constants.TYPE_NORMAL),
            LetterSortModel("四川省", Constants.TYPE_NORMAL),
            LetterSortModel("上海市", Constants.TYPE_NORMAL),
            LetterSortModel("云南省", Constants.TYPE_NORMAL),
            LetterSortModel("西藏自治区", Constants.TYPE_NORMAL),
            LetterSortModel("广安省", Constants.TYPE_NORMAL),
            LetterSortModel("重庆市", Constants.TYPE_NORMAL),
            LetterSortModel("北京市", Constants.TYPE_NORMAL),
            LetterSortModel("海南省", Constants.TYPE_NORMAL),
            LetterSortModel("浙江省", Constants.TYPE_NORMAL),
        )
        data.sortWith(sortComparator)
//
        val newList = mutableListOf<LetterSortModel>()

        for (i in 0 until data.size) {
            if(i == 0){
                newList.add(LetterSortModel(data[i].word!!, Constants.TYPE_LETTER))
            } else {
                val last = data[i - 1]
                val current = data[i]
                if(last.word != current.word){
                    newList.add(LetterSortModel(data[i].word!!, Constants.TYPE_LETTER))
                }
            }
            newList.add(data[i])
        }

        lsl.dataList = newList


    }

}