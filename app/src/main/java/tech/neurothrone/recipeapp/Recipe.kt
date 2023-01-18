package tech.neurothrone.recipeapp

class Recipe {
  var id: String? = null
  var title = ""
  var description = ""
  
  fun toMap(): MutableMap<String, Any> {
    val recipeMap = mutableMapOf<String, Any>()
    recipeMap["title"] = this.title
    recipeMap["description"] = this.description
    return recipeMap
  }
}

/*

+ Recipe
Title
Description
Image, many?
Recipe rows, many

+ Recipe row
Title
Amount
Unit dl/st/osv

 */