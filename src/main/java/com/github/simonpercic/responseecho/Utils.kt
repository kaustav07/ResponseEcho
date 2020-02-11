package com.github.simonpercic.responseecho

import com.github.simonpercic.oklog.shared.data.HeaderData
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.omg.CORBA.NameValuePair
import java.net.URI
import java.net.URL
import java.net.URLDecoder
import java.net.URLEncoder


fun List<HeaderData>.authorization() : String? = this.associate { Pair(it.name,it.value) }["Authorization"]

fun String?.getUrlQueryParam() : List<HeaderData> {
    val queryList = ArrayList<HeaderData>()
    this?.let {
        val url = URL(URLDecoder.decode(it)).query
       url.split("&").forEach {
           if(it.startsWith('{').not()){
               val (name,value) = it.split("=")
               queryList.add(HeaderData(name, URLDecoder.decode(value)))
           }else{
               val obj = Gson().fromJson<JsonObject>(it,JsonObject::class.java)
               obj.entrySet().forEach {
                   queryList.add(HeaderData(it.key, it.value.toString()))
               }
           }

       }
    }

    return queryList
}

fun String?.getPlainUrlQueryParam() : HashMap<String,String?> {
    val queryList = HashMap<String,String?>()
    this?.let {
        val url = URL(it).query
        url.split("&").forEach {
            val (name,value) = it.split("=")
            queryList.put(name, URLDecoder.decode(value))
        }
    }

    return queryList
}