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
  - https://github.com/JetBrains/compose-multiplatform/tree/master/experimental/components/VideoPlayer
  - How the GIF player was implemented in Compose Multiplatform:  
    https://github.com/JetBrains/compose-multiplatform/issues/153
  - https://github.com/bytedeco/javacv/discussions/1956#discussioncomment-4373707
  - https://github.com/JetBrains/compose-multiplatform/issues/1164
  - https://github.com/JetBrains/compose-multiplatform/issues/2499
  - https://github.com/JetBrains/compose-multiplatform/issues/1951
  - https://github.com/JetBrains/compose-multiplatform/issues/1249
  - https://github.com/JetBrains/compose-multiplatform/issues/1595
  - https://github.com/JetBrains/compose-multiplatform/issues/711
  - https://github.com/JetBrains/compose-multiplatform/pull/421

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

MSYS2 provides up-to-date native builds for FFmpeg, GCC, etc. just to name a few.
See https://www.msys2.org/

YouTube probably uses FFmpeg to encode videos. See:
  - https://streaminglearningcenter.com/blogs/youtube-uses-ffmpeg-for-encoding.html
  - https://multimedia.cx/eggs/googles-youtube-uses-ffmpeg/
  - https://www.quora.com/What-does-YouTube-use-for-encoding-video
  - https://security.googleblog.com/2014/01/ffmpeg-and-thousand-fixes.html

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
