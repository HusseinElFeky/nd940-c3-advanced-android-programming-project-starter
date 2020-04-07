package com.udacity.loadapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.loadapp.R
import com.udacity.loadapp.models.DownloadStatus
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
    }

    companion object {
        const val EXTRA_FILE_NAME = "file_name"
        const val EXTRA_DOWNLOAD_STATUS = "download_status"

        fun withExtras(fileName: String, downloadStatus: DownloadStatus): Bundle {
            return Bundle().apply {
                putString(EXTRA_FILE_NAME, fileName)
                putInt(EXTRA_DOWNLOAD_STATUS, downloadStatus.ordinal)
            }
        }
    }
}
