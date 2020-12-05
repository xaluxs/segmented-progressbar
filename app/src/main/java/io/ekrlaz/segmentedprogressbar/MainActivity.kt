package io.ekrlaz.segmentedprogressbar

import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import io.ekrlaz.segmentedprogressbar.databinding.ActivityMainBinding
import java.io.File
import java.text.DecimalFormat


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val phoneStorageDir = Environment.getExternalStorageDirectory()
        val downloadDir = File(phoneStorageDir, "Download")
        val picturesDir = File(phoneStorageDir, "DCIM")


        val totalSpace = cacheDir.totalSpace
        val freeSpace = cacheDir.freeSpace
        val usedSpace = totalSpace-freeSpace
        val downloadsSize = getFolderContentSize(downloadDir)
        val picturesSize = getFolderContentSize(picturesDir)


        binding.progress.setMax(totalSpace.toFloat())
        binding.progress.setPrimaryProgress(usedSpace.toFloat())
        binding.progress.setSecondaryProgress(picturesSize.toFloat())
        binding.progress.setThirdProgress(downloadsSize.toFloat())

        binding.usedStorage.text = getString(R.string.used_storage).format(readableFileSize(usedSpace))
        binding.picturesStorage.text = getString(R.string.pictures).format(readableFileSize(picturesSize))
        binding.downloadStorage.text = getString(R.string.downloads).format(readableFileSize(downloadsSize))
        binding.freeStorage.text = getString(R.string.free_storage).format(readableFileSize(freeSpace))


    }


    private fun getFolderContentSize(dir: File): Long {

        var downloadsSize = 0L

        if (dir.isDirectory) {

            val files = dir.listFiles()
            if (files != null) {
                for (file in files) {
                    downloadsSize += getFolderContentSize(file)
                }
            }
        } else {
            downloadsSize += dir.length()
        }


        return downloadsSize
    }

    private fun readableFileSize(size: Long): String? {
        if (size <= 0) return "0"
        val units = arrayOf(
            "B",
            "kB",
            "MB",
            "GB",
            "TB"
        )
        val digitGroups =
            (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(
            size / Math.pow(
                1024.0,
                digitGroups.toDouble()
            )
        ).toString() + " " + units[digitGroups]
    }


}