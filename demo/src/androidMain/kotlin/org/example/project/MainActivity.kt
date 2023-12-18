package org.example.project

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ir.mahozad.R
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.util.VLCVideoLayout

class MainActivity : ComponentActivity() {
    private var libVLC: LibVLC? = null
    private var videoLayout: VLCVideoLayout? = null
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val args = mutableListOf("-vvv")
        libVLC = LibVLC(this, args)
        mediaPlayer = MediaPlayer(libVLC)
        videoLayout = findViewById(R.id.video_layout)


//        setContent {
//            App()
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        libVLC?.release()
    }

    override fun onStart() {
        super.onStart()
        mediaPlayer?.attachViews(videoLayout!!, null, false, false)
        val media = Media(libVLC, assets.openFd("video.mp4"))
        mediaPlayer?.media = media
        media.release()
        mediaPlayer?.play()
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer?.stop()
        mediaPlayer?.detachViews()
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
