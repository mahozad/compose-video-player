See how the GIF player was implemented in Compose Multiplatform:
https://github.com/JetBrains/compose-jb/issues/153

## Links about video player
  - https://github.com/bytedeco/javacv/discussions/1956#discussioncomment-4373707
  - https://github.com/JetBrains/compose-jb/issues/2499
  - https://github.com/JetBrains/compose-jb/issues/1951
  - https://github.com/JetBrains/compose-jb/issues/1249
  - https://github.com/JetBrains/compose-jb/issues/1595
  - https://github.com/JetBrains/compose-jb/issues/711
  - https://github.com/JetBrains/compose-jb/pull/421

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

## Creating video player
  - FFmpeg tutorials and how-tos: https://trac.ffmpeg.org/wiki
  - https://stackoverflow.com/q/19463550
  - http://dranger.com/ffmpeg/

## Enable hardware acceleration
See https://github.com/bytedeco/javacv/issues/1701

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

### Muxing
Is abbreviation of multiplexing

### Transcoding
