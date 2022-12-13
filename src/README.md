See how the GIF player was implemented in Compose Multiplatform:
https://github.com/JetBrains/compose-jb/issues/153

## FFmpeg
- About: https://ffmpeg.org/about.html
- Tutorials and how-tos: https://trac.ffmpeg.org/wiki

FFmpeg is the leading multimedia framework, able to
decode, encode, transcode, mux, demux, stream, filter and play
pretty much anything that humans and machines have created.
It supports the most obscure ancient formats up to the cutting edge.
No matter if they were designed by some standards committee, the community or a corporation.

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
Sample videos for testing can be downloaded from https://test-videos.co.uk/

## Some technical stuff

### Muxing
Is abbreviation of multiplexing

### Transcoding
