# Note
For a real-world Compose Multiplatform app implementing video player,
see https://github.com/mahozad/cutcon.

## Alternative approaches
https://www.linkedin.com/pulse/java-media-framework-vs-javafx-api-randula-koralage/
  - Vlcj: https://github.com/caprica/vlcj
    + Recommended by Compose Multiplatform
    + Requires VLC to have been installed on the system
    + See the vlcj branch for vlcj implementation
  - JavaFx Media player
    + Supported formats: https://openjfx.io/javadoc/19/javafx.media/javafx/scene/media/package-summary.html
  - Java Media Framework (JMF): https://www.oracle.com/java/technologies/javase/java-media-framework.html
    + Supported formats: https://www.oracle.com/java/technologies/javase/jmf-211-formats.html
  - Swing player: https://stackoverflow.com/q/52038982

## GitHub issues, PRs and discussions about video player
  - https://github.com/JetBrains/compose-multiplatform/pull/2906
  - https://github.com/JetBrains/compose-multiplatform/pull/3336
  - https://github.com/JetBrains/compose-multiplatform/tree/master/experimental/components/VideoPlayer
  - How the GIF player was implemented in Compose Multiplatform:  
    https://github.com/JetBrains/compose-multiplatform/issues/153
  - https://github.com/bytedeco/javacv/discussions/1956
  - https://github.com/caprica/vlcj/issues/1235
  - https://github.com/JetBrains/compose-multiplatform/issues/1354
  - https://github.com/JetBrains/compose-multiplatform/issues/2100
  - https://github.com/JetBrains/compose-multiplatform/issues/1087
  - https://github.com/JetBrains/compose-multiplatform/issues/1089
  - https://github.com/JetBrains/compose-multiplatform/issues/4446
  - https://github.com/JetBrains/compose-multiplatform/issues/3393
  - https://github.com/JetBrains/compose-multiplatform/issues/4389
  - https://github.com/JetBrains/compose-multiplatform/issues/4962
  - https://github.com/JetBrains/compose-multiplatform/issues/1164
  - https://github.com/JetBrains/compose-multiplatform/issues/2499
  - https://github.com/JetBrains/compose-multiplatform/issues/1951
  - https://github.com/JetBrains/compose-multiplatform/issues/1249
  - https://github.com/JetBrains/compose-multiplatform/issues/1595
  - https://github.com/JetBrains/compose-multiplatform/issues/711
  - https://github.com/JetBrains/compose-multiplatform/pull/421
  - https://github.com/open-ani/ani/blob/master/app/shared/video-player/desktop/ui/VideoPlayer.desktop.kt

## Similar libraries
  - https://github.com/databrary/datavyu-ffmpegplugin
  - https://github.com/rockcarry/fanplayer
  - https://github.com/bilibili/ijkplayer
  - https://github.com/arthenica/ffmpeg-kit
  - We can use vlcj library which needs VLC to have been installed on the system.
    See https://github.com/caprica/vlcj
    To run without VLC see:
    + https://github.com/caprica/vlcj/issues/1072
    + https://github.com/caprica/vlcj-natives/issues/1
    + https://stackoverflow.com/q/20836359
    + https://stackoverflow.com/q/45881054
    + https://stackoverflow.com/q/15080165
  - We can use VLC official libVLC binding for Java/Kotlin to play video: http://www.videolan.org/vlc/libvlc.html
  - We can use [ffplay](https://ffmpeg.org/ffplay.html) included in **BtbN** Windows build:
    https://ffmpeg.org/download.html#build-windows
    -> https://github.com/BtbN/FFmpeg-Builds/releases
    -> Expand the assets section to see all build variants
    -> Download an ffmpeg (which includes ffprobe and ffplay)
  - On Android, We can use ExoPlayer:
    https://itnext.io/playing-a-video-with-jetpack-compose-10a453ff956
    https://www.youtube.com/watch?v=JX1fwti2LI4&ab_channel=PhilippLackner

## FFmpeg
About: https://ffmpeg.org/about.html

FFmpeg is the leading multimedia framework, able to
decode, encode, transcode, mux, demux, stream, filter and play
pretty much anything that humans and machines have created.
It supports the most obscure ancient formats up to the cutting edge.
No matter if they were designed by some standards committee, the community or a corporation.

[FFmpeg in 100 seconds](https://www.youtube.com/watch?v=26Mayv5JPz0)

[Building FFmpeg for Windows](https://trac.ffmpeg.org/wiki/CompilationGuide/MinGW)

The best way to use FFmpeg in Java/Kotlin seems to be through the [javacv library](https://github.com/bytedeco/javacv).
It uses [JNI to directly call C libraries](https://stackoverflow.com/a/5963294) of FFmpeg in Java
(*.dll* files on Windows and *.so* files on Linux) so it is quite performant.
JavaCV brings FFmpeg binaries wrapped in JAR files for various platforms. For example, for Windows x86-64,
it brings a JAR that contains a small ffmpeg.exe and its various libraries like *avformat.dll* and so on.
The JAR is also quite small (about 20 MB for Windows x86-64).
The ffmpeg.exe itself contained in the JAR too uses those libraries.
We can also run the FFmpeg executable itself instead of using
its libraries by using javacv `Loader.load(...)` which extracts the ffmpeg.exe and its libraries in a temp folder and
returns the path of copied ffmpeg.exe, so we can use it with Java `ProcessBuilder` or `Runtime` to execute it.
Running a simple command and delegating the logic to ffmpeg itself may be easier
but may have a lower performance compared to using the libraries directly because we are creating another process.

See
- https://stackoverflow.com/q/13973035/8583692
- https://www.youtube.com/watch?v=d7KHAVaX_Rs&ab_channel=Computerphile
- https://softwareengineering.stackexchange.com/q/210278
- https://stackoverflow.com/q/9809213/8583692

See the sections below for more information about excluding JavaCV unneeded JARs and so on.

MSYS2 provides up-to-date native builds for FFmpeg, GCC, etc. just to name a few.
See https://www.msys2.org/

YouTube probably uses FFmpeg to encode videos. See:
  - https://streaminglearningcenter.com/blogs/youtube-uses-ffmpeg-for-encoding.html
  - https://multimedia.cx/eggs/googles-youtube-uses-ffmpeg/
  - https://www.quora.com/What-does-YouTube-use-for-encoding-video
  - https://security.googleblog.com/2014/01/ffmpeg-and-thousand-fixes.html

### Alternative approaches and ways for using FFmpeg
- Manually building a custom build of FFmpeg (and/or FFprobe, etc.) (which seems rather easy)
  to reduce its size significantly and include that in our app like what these tools have done:
    + [ExoPlayer library](https://github.com/google/ExoPlayer) which is used to play media files in Android
      (the library seems to have been merged into androidx.media3 library)
      which seems to [use FFmpeg](https://github.com/google/ExoPlayer/blob/release-v2/extensions/ffmpeg/README.md?plain=1)
      for [extended support of media formats](https://github.com/google/ExoPlayer/blob/release-v2/docs/supported-formats.md?plain=1)
    + https://github.com/rom1v/scrcpy-deps/ builds a custom build of FFmpeg for [Scrcpy](https://github.com/Genymobile/scrcpy)
    + Chromium (uses FFmpeg to play video etc.)
    + Electron (uses Chromium and thus FFmpeg).
      The custom FFmpeg is available in each of its GitHub releases page
        * https://github.com/electron/electron/issues/9534
        * https://github.com/electron/electron/issues/2588
        * https://github.com/electron/electron/pull/14729/files
        * https://github.com/electron/electron/pull/4548
    + https://www.npmjs.com/package/ffbinaries
    + https://www.npmjs.com/package/@ffprobe-installer/ffprobe
    + Telegram uses ffmpeg:
      + https://github.com/telegramdesktop/dependencies_windows
      + https://github.com/search?q=repo%3Atelegramdesktop%2Ftdesktop%20ffmpeg&type=code
- See and check out the Git tag `ffmpeg-aggregator-dependency` in this repository
  for the previous (aggregator FFmpeg dependency) implementation.
- Using external FFmpeg.exe and Java `ProcessBuilder` to run a regular executable file.  
  See and check out the Git tag `ffmpeg-external-version` for that implementation
  and read the [ffmpeg/README.md](ffmpeg/README.md) file for more information.
- (???) Using [Kotlin Native](https://kotlinlang.org/docs/native-c-interop.html) to work with [FFmpeg C library](https://github.com/FFmpeg/FFmpeg)
- Using [Java project Panama](https://openjdk.org/projects/panama/)  
  Also see:
    + https://jdk.java.net/panama/
    + https://github.com/openjdk/panama-foreign
    + https://stackoverflow.com/a/65326358
- Using **ffmpeg-kit** library or the way it uses FFmpeg: https://github.com/arthenica/ffmpeg-kit
- Using libVLC in our app how it is used for [vlc-android](https://github.com/videolan/vlc-android)
- Using [JNA](https://github.com/java-native-access/jna) to include
  [libVLC](https://www.videolan.org/vlc/libvlc.html)
  (see https://github.com/java-native-access/jna/issues/243#issuecomment-34768684) or
  using the [FFmpeg C library](https://github.com/FFmpeg/FFmpeg) directly
- Using [JNR-FFI](https://github.com/jnr/jnr-ffi/blob/master/docs/ComparisonToSimilarProjects.md) to include
  [libVLC](https://www.videolan.org/vlc/libvlc.html) or
  using the [FFmpeg C library](https://github.com/FFmpeg/FFmpeg) directly
- Using [JNI](https://en.wikipedia.org/wiki/Java_Native_Interface) to include
  [libVLC](https://www.videolan.org/vlc/libvlc.html) or
  using the [FFmpeg C library](https://github.com/FFmpeg/FFmpeg) directly

For an example Kotlin library that uses a C library see [Skiko](https://github.com/JetBrains/skiko)

### The `org.bytedeco:ffmpeg[-platform][-gpl]` dependency
The `org.bytedeco:ffmpeg-platform[-gpl]` brings many FFmpeg JAR artifacts for various OSs and architectures
which are about 250 MB in total. This is so we can run FFmpeg in our Java app in a cross-platform manner
(we can simply call `Loader.load(ffmpeg::class.java)` and it loads the appropriate FFmpeg native executable for the
OS/architecture our program is running on). But because we are creating a GUI application and there is currently no way
a GUI executable on Windows, for example, can be run on Linux or Android, we don't need the `load` to be cross-platform
(i.e. we don't need all the various JAR native artifacts of the FFmpeg for a Windows exe etc. to be available for app).
So, we want to exclude other FFmpeg JARs to dramatically reduce the app size
(like what `implementation(compose.desktop.currentOs)` or JavaFx plugin do).

Shrinking/optimizing the app with proguard (pro.rules file) doesn't seem to help.

The `org.bytedeco:ffmpeg-platform[-gpl]` is just an aggregator dependency which automatically
brings some **artifacts** of `org.bytedeco:ffmpeg` dependency
(like JUnit 5 [org.junit.jupiter:junit-jupiter](https://github.com/junit-team/junit5/tree/main/junit-jupiter)
aggregator except that *junit-jupiter* aggregates separate **dependencies** while
*ffmpeg-platform[-gpl]* aggregates some **artifacts of a single dependency**)
(if we use `org.bytedeco:ffmpeg-platform-gpl` it brings `-gpl` artifacts of `org.bytedewco:ffmpeg`
(which have more features), otherwise it brings the regular artifacts).

Also see [this org.bytedeco guide](https://github.com/bytedeco/javacpp-presets/wiki/Reducing-the-Number-of-Dependencies).

So, instead of the aggregator `org.bytedeco:ffmpeg-platform[-gpl]`, we have used the `org.bytedeco:ffmpeg` dependency
directly. In this case, Gradle will only download the default artifact of the dependency (like it does for other libraries)
and none of its other artifacts (i.e. the JARs wrapping FFmpeg native executables) are downloaded
(see [all artifacts of one of its versions in MVN Repository](https://repo1.maven.org/maven2/org/bytedeco/ffmpeg/5.1.2-1.5.8/)).
To bring those artifacts as well, we included the specific artifact for our current OS/architecture the Gradle is
running on using classifiers in the dependency notation (like `group:module:version:classifier`).
See [Gradle docs: resolving specific artifacts of a dependency](https://docs.gradle.org/current/userguide/declaring_dependencies.html#sec:resolve_specific_artifacts_from_dependency).

Another way that I wrote is to use the `org.bytedeco:ffmpeg-platform[-gpl]` aggregator
and using either of the following approaches to exclude unwanted JAR files:
- **Not tested**: https://medium.com/nerd-for-tech/gradle-managing-scope-and-platform-specific-dependencies-5384d6d8ac52
- **Not tested; just a draft**: Using something like this:

```kotlin
// See https://stackoverflow.com/q/28181462
// See https://dev.to/autonomousapps/a-crash-course-in-classpaths-build-l08
tasks.withType<KotlinCompile /*OR*/ /*KotlinCompileCommon*/> {
    doLast {
        libraries.map{it.name}.forEach(::println)
        // libraries.removeAll {
        //     "ffmpeg" in it.name
        // }
    }
    exclude { "ffmpeg" in it.name }
}
```

- Register a Gradle `TransformAction` in the build script like this:

```kotlin
/**
 * Note that The code to detect OS and architecture has been adopted and adapted from
 * Compose Gradle plugin -> org/jetbrains/compose/desktop/application/internal/osUtils.kt
 *
 * See https://docs.gradle.org/current/userguide/artifact_transforms.html
 * and https://diarium.usal.es/pmgallardo/2020/10/12/possible-values-of-os-dependent-java-system-properties/
 * and https://stackoverflow.com/q/74389033
 */
abstract class FFmpegTransformer : TransformAction<TransformParameters.None> {

    @get:InputArtifact
    abstract val inputArtifact: Provider<FileSystemLocation>

    // TODO: Exclude irrelevant javacpp JARs as well
    override fun transform(outputs: TransformOutputs) {
        val artifactFileName = inputArtifact.get().asFile.name
        if (shouldTransform(artifactFileName)) {
            val dir = outputs.dir(inputArtifact.get().asFile.nameWithoutExtension) // Request an output location
            dir.resolve(artifactFileName).createNewFile() // Create an empty JAR file
            // OR Files.write(dir.toPath().resolve(fileName), "Generated text")
            println("Transforming $artifactFileName to $dir")
        } else {
            outputs.file(inputArtifact)
        }
    }

    private fun shouldTransform(artifactFileName: String) =
        artifactFileName.contains("ffmpeg") && (
                // If it's Windows, just include the x86 version to be able to run it on both 32- and 64-bit architectures
                // NOTE: Does not seem to work because org.bytedeco:ffmpeg tries to load
                //  64-bit variant on 64-bit Windows which we have excluded
                (currentTarget.os == OS.Windows && artifactFileName.isWin32Artifact()) ||
                (currentTarget.os != OS.Windows && artifactFileName.isForCurrentOS() && artifactFileName.isForCurrentArch()) ||
                (artifactFileName.isMainArtifact())
        ).not()

    private fun String.isWin32Artifact() = endsWith("windows-x86.jar") || endsWith("windows-x86-gpl.jar")

    private fun String.isMainArtifact() = contains("platform") || contains("ffmpeg-5.1.2-1.5.8.jar")

    private fun String.isForCurrentOS() = contains(currentTarget.os.id, ignoreCase = true)

    private fun String.isForCurrentArch() = contains(currentTarget.arch.id, ignoreCase = true)

    private enum class OS(val id: String) {
        Linux("linux"),
        MacOSX("macosx"),
        Windows("windows"),
        Android("android")
    }

    private enum class Arch(val id: String) {
        X86_64("x86_64"),
        X86("x86"),
        Arm("arm"),
        Arm64("arm64"),
    }

    private data class Target(val os: OS, val arch: Arch) {
        val id: String get() = "${os.id}-${arch.id}"
    }

    private val currentTarget by lazy {
        Target(currentOS, currentArch)
    }

    private val currentArch by lazy {
        val osArch = System.getProperty("os.arch")
        when (osArch) {
            "x86" -> Arch.X86
            "x86_64", "amd64" -> Arch.X86_64
            "arm" -> Arch.Arm
            "aarch64" -> Arch.Arm64
            else -> error("Unsupported OS architecture: $osArch")
        }
    }

    private val currentOS: OS by lazy {
        // NOTE: On Android the OS is Linux as well, so we check something else
        //  See https://developer.android.com/reference/java/lang/System#getProperties()
        val vendor = System.getProperty("java.vendor")
        val os = System.getProperty("os.name")
        when {
            vendor.contains("Android", ignoreCase = true) -> OS.Android
            os.startsWith("Linux", ignoreCase = true) -> OS.Linux
            os.equals("Mac OS X", ignoreCase = true) -> OS.MacOSX
            os.contains("Win", ignoreCase = true) -> OS.Windows
            else -> error("Unknown OS name: $os")
        }
    }
}

val artifactType = Attribute.of("artifactType", String::class.java)
val isProcessed = Attribute.of("isProcessed", Boolean::class.javaObjectType) // isProcessed is an arbitrary name
configurations
    .filter(Configuration::isCanBeResolved)
    .map(Configuration::getAttributes)
    .forEach {
        afterEvaluate {
            // Request isProcessed=true on all resolvable configurations
            it.attribute(isProcessed, true)
        }
    }

dependencies {
    attributesSchema {
        attribute(isProcessed)
    }
    artifactTypes.getByName("jar") {
        attributes.attribute(isProcessed, false)
    }
    registerTransform(FFmpegTransformer::class) {
        from.attribute(isProcessed, false).attribute(artifactType, "jar")
        to.attribute(isProcessed, true).attribute(artifactType, "jar")
    }
}
```

Side note 1: The `org.bytedeco:ffmpeg-platform[-gpl]` uses
[POM classifier property](https://maven.apache.org/pom.html#dependencies) in Maven build system to
depend on a specific artifact of a dependency (also see https://stackoverflow.com/a/20909695)
(Gradle also supports this.
See [Gradle docs: resolving specific artifacts of a dependency](https://docs.gradle.org/current/userguide/declaring_dependencies.html#sec:resolve_specific_artifacts_from_dependency)).

Side note 2: The `-gpl` version of FFmpeg contains more features (like libx264 encoder)
and therefore has larger native JARs (about 4 MB larger than the regular version).

Also see:
- https://github.com/gradle/gradle/issues/22704

## Creating a video player
  - FFmpeg tutorials and how-tos: https://trac.ffmpeg.org/wiki
  - https://stackoverflow.com/q/19463550
  - http://dranger.com/ffmpeg/
  - https://www.cocos.com/en/post/building-an-internal-video-player-based-on-ffmpeg-for-cocos-creator
  - [An ffmpeg and SDL Tutorial or How to Write a Video Player in Less Than 1000 Lines](http://dranger.com/ffmpeg/)
  - [bytedeco:javacv JavaFxVideoAndAudio player](https://github.com/bytedeco/javacv/blob/master/samples/JavaFxPlayVideoAndAudio.java)
  - [SDL library](https://github.com/libsdl-org/SDL)
  - Related (for cutting/splitting/slicing/cropping video): https://github.com/mifi/lossless-cut

We can use the [javacv library](https://github.com/bytedeco/javacv) (org.bytedeco:javacv-platform:1.5.8)
and its FrameGrabber and Recorder and draw each frame in a Compose canvas.
Note that the library brings JARs for all platforms and architectures (about 250 MB)
which may not be needed on the current OS/architecture.

See:
- https://github.com/bytedeco/javacv/blob/master/samples/FFmpegStreamingTimeout.java
- https://stackoverflow.com/q/13770376
- https://stackoverflow.com/q/67986576
- https://stackoverflow.com/q/59185079
- https://stackoverflow.com/q/17401852

## Enable hardware acceleration
  - https://github.com/bytedeco/javacv/issues/1701
  - https://www.quora.com/Whats-the-difference-between-an-H-W-decoder-and-an-S-W-decoder-in-video-players

## Playing audio in Java
  - https://www.baeldung.com/java-play-sound
  - We could probably use https://github.com/mahozad/jlayer to play audio/sound

## Sync the video and audio
  - https://stackoverflow.com/q/68874455
  - https://forum.processing.org/two/discussion/19233/getting-audio-and-video-in-sync.html
  - https://community.oracle.com/tech/developers/discussion/2383959/how-can-i-sync-with-audio-framerate
  - https://youtu.be/jzvC-0WCjKU

## Speed up or slow down video and audio

https://github.com/waywardgeek/sonic

Example of speeding up by a factor of `3.0`:

```shell
ffmpeg -i input.mp4 -vf "setpts=PTS/3.0" -af "atempo=3.0" output.mp4
```

See:
  - https://trac.ffmpeg.org/wiki/How%20to%20speed%20up%20/%20slow%20down%20a%20video
  - https://superuser.com/q/1324525
  - https://stackoverflow.com/q/63228235

## Test videos
Sample videos for testing can be downloaded from:
  - Tv test patterns
    - https://en.wikipedia.org/wiki/Philips_circle_pattern
    - https://www.reddit.com/r/VIDEOENGINEERING/comments/125iwkf/new_downloadable_test_pattern/
    - https://www.pierrehenrypauly.com/database
    - https://www.youtube.com/watch?v=dlzc-jbhRpI&ab_channel=Pierre-HenryPAULY
    - https://www.youtube.com/watch?v=IIqtuupvdWg&ab_channel=XponentialdesignTutorials%26VideoMarketplace
    - http://www.gvgdevelopers.com/concrete/products/k2/test_clips/
    - https://stock.adobe.com/ee/search?k=tv+test+pattern&asset_id=202297164
  - https://samples.mplayerhq.hu/
  - https://test-videos.co.uk/
  - https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4

## Some technical stuff

.ts (MPEG-2 transport stream-A container format for broadcasting MPEG-2 video via terrestrial and satellite networks)
and .mp3 file formats are a type of MPEG-2 container format.

### Muxing
Is abbreviation of multiplexing

### PTS (presentation timestamp)
  - https://en.wikipedia.org/wiki/Presentation_timestamp

### Transcoding


### Other
Add audio spectrum below or over the video like Aimp.
See https://github.com/goxr3plus/XR3Player#java-audio-tutorials-and-apis-by-goxr3plus-studio
