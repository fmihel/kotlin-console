# kotlin-console
kotlin, console

### install
```bash
git clone git@github.com:fmihel/kotlin-console.git
```

### use
```kotlin
include fmihel.console.*

fun my(){
    console("Hi")
}
```

### include to project (as file)
add to  ```build.gradle.kts```
```kotlin
kotlin {
    sourceSets {
        main {
            kotlin.srcDir("../kotlin-console/src/main/kotlin/console") 
        }
    }
}
``` 



### include to project (as project)
add to  ```build.gradle.kts```
```kotlin
dependencies {
    implementation(project(":shared-console"))
}
``` 

add to  ```settings.gradle.kts```
```kotlin
include(":shared-console")
project(":shared-console").projectDir = file("../kotlin-console/src/main/kotlin/console")
```
