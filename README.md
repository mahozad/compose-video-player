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
  - How the GIF player was implemented in Compose Multiplatform:  
    https://github.com/JetBrains/compose-jb/issues/153
  - https://github.com/bytedeco/javacv/discussions/1956#discussioncomment-4373707
  - https://github.com/JetBrains/compose-jb/issues/1164
  - https://github.com/JetBrains/compose-jb/issues/2499
  - https://github.com/JetBrains/compose-jb/issues/1951
  - https://github.com/JetBrains/compose-jb/issues/1249
  - https://github.com/JetBrains/compose-jb/issues/1595
  - https://github.com/JetBrains/compose-jb/issues/711
  - https://github.com/JetBrains/compose-jb/pull/421

## Similar libraries
  - https://github.com/databrary/datavyu-ffmpegplugin
  - https://github.com/rockcarry/fanplayer

## FFmpeg
About: https://ffmpeg.org/about.html

FFmpeg is the leading multimedia framework, able to
decode, encode, transcode, mux, demux, stream, filter and play
pretty much anything that humans and machines have created.
It supports the most obscure ancient formats up to the cutting edge.
No matter if they were designed by some standards committee, the community or a corporation.

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

## Enable hardware acceleration
  - https://github.com/bytedeco/javacv/issues/1701
  - https://www.quora.com/Whats-the-difference-between-an-H-W-decoder-and-an-S-W-decoder-in-video-players

## Playing audio in Java
  - https://www.baeldung.com/java-play-sound

## Sync the video and audio
  - https://stackoverflow.com/q/68874455
  - https://forum.processing.org/two/discussion/19233/getting-audio-and-video-in-sync.html
  - https://community.oracle.com/tech/developers/discussion/2383959/how-can-i-sync-with-audio-framerate

## Speed up or slow down video and audio
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
