-keepattributes RuntimeVisibleAnnotations,AnnotationDefault

# kotlinx-serialization-json specific. Add this if you have java.lang.NoClassDefFoundError kotlinx.serialization.json.JsonObjectSerializer
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Application rules

-keepclassmembers @kotlinx.serialization.Serializable class com.lr_soft.mywordcards.model.** {
    # lookup for plugin generated serializable classes
    *** Companion;
    # lookup for serializable objects
    *** INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}
# lookup for plugin generated serializable classes
-if @kotlinx.serialization.Serializable class com.lr_soft.mywordcards.model.**
-keepclassmembers class com.lr_soft.mywordcards.model.<1>$Companion {
    kotlinx.serialization.KSerializer serializer(...);
}

