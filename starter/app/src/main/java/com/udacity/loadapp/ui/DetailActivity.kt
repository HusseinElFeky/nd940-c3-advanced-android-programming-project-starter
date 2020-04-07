package com.udacity.loadapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.loadapp.R
import com.udacity.loadapp.models.DownloadStatus
import com.udacity.loadapp.utils.NotificationUtils
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var fileName: String
    private lateinit var downloadStatus: DownloadStatus
    private var downloadId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        loadExtras()
        clearNotification()
        initViews()
    }

    private fun loadExtras() {
        val extras = intent.extras
        extras?.let {
            fileName = it.getString(EXTRA_FILE_NAME)!!
            downloadStatus = DownloadStatus.values()[it.getInt(EXTRA_DOWNLOAD_STATUS)]
            downloadId = it.getInt(EXTRA_DOWNLOAD_ID)
        }
    }

    private fun clearNotification() {
        NotificationUtils.clearNotification(this, downloadId)
    }

    private fun initViews() {
        tv_file_name.text = fileName

        tv_download_status.text = if (downloadStatus == DownloadStatus.SUCCESS) {
            getString(R.string.success)
        } else {
            getString(R.string.fail)
        }
    }

    companion object {
        private const val EXTRA_FILE_NAME = "file_name"
        private const val EXTRA_DOWNLOAD_STATUS = "download_status"
        private const val EXTRA_DOWNLOAD_ID = "download_id"

        fun withExtras(fileName: String, downloadStatus: DownloadStatus, downloadId: Int): Bundle {
            return Bundle().apply {
                putString(EXTRA_FILE_NAME, fileName)
                putInt(EXTRA_DOWNLOAD_STATUS, downloadStatus.ordinal)
                putInt(EXTRA_DOWNLOAD_ID, downloadId)
            }
        }
    }
}
