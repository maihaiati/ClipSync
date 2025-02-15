package com.mtechdwork.clipsync

import android.accessibilityservice.AccessibilityService
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class ClipboardAccessibilityService : AccessibilityService() {
    private lateinit var clipboardManager: ClipboardManager

    override fun onServiceConnected() {
        super.onServiceConnected()
        clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        listenClipboard()
    }

    private fun listenClipboard() {
        clipboardManager.addPrimaryClipChangedListener {
            val clipData: ClipData? = clipboardManager.primaryClip
            val clipText = clipData?.getItemAt(0)?.text?.toString()
            Log.d("ClipboardService", "Clipboard updated: $clipText")
            // TODO: Gửi clipboard đến thiết bị khác qua mạng LAN/P2P
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}

    override fun onInterrupt() {}
}