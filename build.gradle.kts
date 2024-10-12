buildscript {
    val API_URL by extra("http://momocoffe-lb-1774679013.us-east-1.elb.amazonaws.com")
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.3" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
}
val API_URL by extra("http://momocoffe-lb-1774679013.us-east-1.elb.amazonaws.com")



