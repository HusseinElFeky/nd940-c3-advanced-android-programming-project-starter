package com.udacity.loadapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.loadapp.R
import com.udacity.loadapp.models.DownloadStatus
import com.udacity.loadapp.utils.NotificationUtils
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    private var downloadId = -1
    private lateinit var downloadStatus: DownloadStatus
    private lateinit var fileName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        loadExtras()
        clearNotification()
        initViews()
        initListeners()
    }

    private fun loadExtras() {
        val extras = intent.extras
        extras?.let {
            downloadId = it.getInt(EXTRA_DOWNLOAD_ID)
            downloadStatus = DownloadStatus.values()[it.getInt(EXTRA_DOWNLOAD_STATUS)]
            fileName = it.getString(EXTRA_FILE_NAME)!!
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

        ml_details.transitionToEnd()
    }

    private fun initListeners() {
        btn_ok.setOnClickListener {
            finish()
        }
    }

    companion object {
        private const val EXTRA_DOWNLOAD_ID = "download_id"
        private const val EXTRA_DOWNLOAD_STATUS = "download_status"
        private const val EXTRA_FILE_NAME = "file_name"

        fun withExtras(
            downloadId: Int,
            downloadStatus: DownloadStatus,
            fileName: String
        ): Bundle {
            return Bundle().apply {
                putInt(EXTRA_DOWNLOAD_ID, downloadId)
                putInt(EXTRA_DOWNLOAD_STATUS, downloadStatus.ordinal)
                putString(EXTRA_FILE_NAME, fileName)
            }
        }
    }
}
