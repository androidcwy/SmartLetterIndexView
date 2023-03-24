package com.smart.example.letterindexview.model

import com.smart.liv.model.LetterSortModel

/**
 * @author JoeYe
 * @date 2023/3/10 17:17
 */
data class CitySortModel(
    val children: List<CitySortModel>?,
    val sortModel: LetterSortModel
)

