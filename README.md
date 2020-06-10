# Cleaner Buddy #

## Development Tools ##

The following tools are used.
* Java 8 JDK or better - For Java development
* Android Studio - For Android development
   * On the CSE Linux machines you may need to install binaries for running 32-bit tools by following command:
   ```
   sudo yum install glibc.i686 zlib.i686 libstdc++.i686 ncurses-libs.i686 libgcc.i686
   ```
* Genymotion - Not required, but it is a better, faster emulator for Android
* VirtualBox - Genymotion's virtual machine


## Importing the Android project into Android Studio ##

1. Open Android Studio
2. If prompted with the welcome screen, select "Import project (Eclipse ADT, Gradle, etc.)"
3. If not, go to File -> Import Project
4. Find the folder where the repository is and select the folder.
5. After successful import, you will be able to see project structure and gradle sync will began.
6. If the build fails, check for the errors in build tab.


## Android Studio Plugins ##

### Android Size Analyzer ###
We used this tool to analyze the apk size of our application. It will remove the unnecessary code statements in the project reducing its size about 40%.  
To use, 
* Install Java 8 on your machine, go to File -> Project Structure -> SDK Location and set JDK location to the location of your Java 8 JDK. 
* Go to settings -> plugins and install Android Size Analyzer.
* In the build tab, go to Analyze Code.


### Lint ###
The lint tool checks your Android project source files for potential bugs and optimization improvements for correctness, security, performance, usability, accessibility, and internationalization.  
To add lint in project, create a script-lint.gradle file. 
```
android{
    lintOptions{
        lintConfig file("$project.rootDir/tools/rules-lint.xml")
        htmlOutput file("$project.buildDir/outputs/lint/lint.html")
        warningsAsErrors true
        xmlReport false
    }
}
```
Import script-lint.gradle to your build.gradle file.



## Directory Structure ##

* app/src/main -Folder for main source code.
   * AndroidManifest.xml - Manifest file.
   * java/com/example/garbagecleaner - Folder containing main java files.
* app/release - Signed apk file for project.
* app/build/outputs/apk/debug - Build apk file for project.


## How to build ##

Prerequisites: Install the following from the SDK Manager
* Android SDK Platform-tools
* Android SDK Build-tools
* SDK Platform Android 5.1
* SDK Platform Android 5.0
* Android Support Repository
* Android Support Library
These may be installed as:
* In Android Studio select and download from the SDK Manager by going to Tools -> Android -> SDK Manager
In General
* In the Android Studio top menu bar, select Build -> Make Project.
