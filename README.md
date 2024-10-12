# Momo Coffee Android KDS v1

## Configuration
file: build.gradle.kts
```bash
   debug {
            buildConfigField("String", "API_BASE_URL", "\"http://205.251.136.173:3002\"") // Api Backend Desarrollo
            buildConfigField("String", "API_URL_CURRENCY", "\"https://open.er-api.com\"") // Api Generador de conversor de moneda a dolares
        }
```
```bash
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "API_BASE_URL", "\"http://205.251.136.173:3002\"") // Api Backend Producion
            buildConfigField("String", "API_URL_CURRENCY", "\"https://open.er-api.com\"") // Api Generador de conversor de moneda a dolares
            signingConfig = signingConfigs.getByName("release")
        }

```