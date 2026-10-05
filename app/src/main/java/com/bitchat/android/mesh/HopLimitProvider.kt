package com.bitchat.android.mesh

import android.util.Log
import com.bitchat.android.ui.debug.DebugSettingsManager

/**
 * Provides the current hop limit (TTL) for mesh packets.
 * 
 * Checks the debug override first, falls back to the default constant.
 * This allows users to dynamically adjust the mesh reach without recompilation.
 */
object HopLimitProvider {
    private const val TAG = "HopLimitProvider"

    /**
     * Get the current message TTL hops value as a UByte.
     * 
     * @return The hop limit (0-255), with debug override taking precedence
     */
    fun getMessageTtlHops(): UByte {
        return try {
            val debugValue = DebugSettingsManager.getInstance().messageTtlHops.value
            val clamped = debugValue.coerceIn(0, 255)
            if (debugValue != 200) {  // 200 is the default
                Log.d(TAG, "Using debug-override hop limit: $clamped")
            }
            clamped.toUByte()
        } catch (e: Exception) {
            Log.w(TAG, "Failed to read debug hop limit, using default: ${e.message}")
            com.bitchat.android.util.AppConstants.MESSAGE_TTL_HOPS
        }
    }

    /**
     * Get the current sync TTL hops value (always 0 for neighbor-only).
     * 
     * @return 0u (neighbor-only, non-overridable)
     */
    fun getSyncTtlHops(): UByte {
        return com.bitchat.android.util.AppConstants.SYNC_TTL_HOPS
    }
}
