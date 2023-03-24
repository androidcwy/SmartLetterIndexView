package com.smart.example.letterindexview

/**
 * @author JoeYe
 * @date 2023/3/10 16:37
 */
class CityBean : ArrayList<CityBeanItem>()

data class CityBeanItem(
    val children: List<Children>,
    val code: String,
    val name: String
)

data class Children(
    val children: List<ChildrenX>,
    val code: String,
    val name: String
)

data class ChildrenX(
    val code: String,
    val name: String
)