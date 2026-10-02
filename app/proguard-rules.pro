
# ProGuard and R8 rules for FlatSplit

# Keep Firestore data models.
# Firestore uses reflection for object deserialization.
-keep class com.techhub.flatsplit.domain.model.** { *; }

# Keep AndroidX @Keep annotation and anything annotated with it.
-keep @interface androidx.annotation.Keep

-keep @androidx.annotation.Keep class * { *; }

-keepclassmembers class * {
    @androidx.annotation.Keep *;
}

# Keep source file and line number information
# for readable crash reports.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile