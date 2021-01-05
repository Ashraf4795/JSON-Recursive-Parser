import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class JSONRecursiveParser {

    companion object {

        @Throws(JSONException::class)
        fun parseJSON(jsonString: String, dest: HashMap<String, Any>){
            if(jsonString.length == 0 ) {
                throw JSONException("Emptry Json Data")
            }
            when (jsonString.first()) {
                '{' -> {
                    val jsonObject = JSONObject(jsonString)
                    parseJSONObject(dest, jsonObject)
                }
                '[' -> {
                    val jsonArray = JSONArray(jsonString)
                    parseJSONArray(dest, jsonArray)
                }else -> {
                    throw JSONException("JSON start with ${jsonString.first()}, please validate.")
                }
            }
        }

        private fun parseJSONObject(dest: HashMap<String, Any>, jsonObject: JSONObject) {
            val jsonKeys = jsonObject.keys()

            for (key in jsonKeys) {
                if (key == null) {
                    dest.put("null", "null")
                    continue //to next key
                }
                try {
                    val jsonElement = jsonObject.get(key)
                    when (jsonElement) {
                        is JSONObject -> {
                            parseJSONObject(dest, jsonElement)
                        }
                        is JSONArray -> {
                            parseJSONArray(dest, jsonElement)
                        }
                        else -> {
                            dest.put(key, jsonElement)
                        }
                    }
                } catch (jsonException: JSONException) {
                    //jsonObject.get(key) may throw JSONException for two reasons
                    // 1- if key is null
                    // 2- if the retrived object is null
                    dest.put(key, "null")
                }


            }
        }

        private fun parseJSONArray(dest: HashMap<String, Any>, jsonArray: JSONArray) {
            for (index in 0 until jsonArray.length()) {
                try {
                    val jsonElement = jsonArray.get(index)

                    when (jsonElement) {
                        is JSONObject -> {
                            parseJSONObject(dest, jsonElement)
                        }
                        else -> {
                            dest.put("${index + 1} array_element", jsonElement)
                        }
                    }
                } catch (jsonException: JSONException) {
                    //jsonObject.get(key) may throw JSONException for two reasons
                    // 1- if key is null
                    // 2- if the retrived object is null
                    dest.put("arr-element ${index + 1}", "null")
                }
            }
        }
    }
}