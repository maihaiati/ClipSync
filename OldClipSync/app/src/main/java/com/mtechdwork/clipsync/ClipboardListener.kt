package com.mtechdwork.clipsync

import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast

class ClipboardListener(private val context: Context) {
    private val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    private val onPrimaryClipChangedListener = ClipboardManager.OnPrimaryClipChangedListener {
        if (clipboardManager.hasPrimaryClip() && clipboardManager.primaryClip!!.itemCount > 0) {
            val clipText = clipboardManager.primaryClip!!.getItemAt(0).text.toString()
            Toast.makeText(context, "Clipboard changed: $clipText", Toast.LENGTH_SHORT).show()
        }
    }

    fun startListening() {
        clipboardManager.addPrimaryClipChangedListener(onPrimaryClipChangedListener)
    }

    fun stopListening() {
        clipboardManager.removePrimaryClipChangedListener(onPrimaryClipChangedListener)
    }
}