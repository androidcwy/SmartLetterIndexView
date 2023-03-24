package com.smart.example.letterindexview

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * @author JoeYe
 * @date 2023/3/10 16:47
 */
class JsonUtils {

    /**
     * fromJson2List
     */
    inline fun <reified T> fromJson2List(json: String) = fromJson<List<T>>(json)

    /**
     * fromJson
     */
    inline fun <reified T> fromJson(json: String): T? {
        return try {
            val type = object : TypeToken<T>() {}.type
            return Gson().fromJson(json, type)
        } catch (e: Exception) {
            println("try exception,${e.message}")
            null
        }
    }

}