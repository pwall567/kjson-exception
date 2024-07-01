/*
 * @(#) JSONException.kt
 *
 * kjson-exception  Exception class for JSON projects
 * Copyright (c) 2021, 2023, 2024 Peter Wall
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.kjson

/**
 * A base exception class for JSON errors.
 *
 * @author  Peter Wall
 */
open class JSONException(
    /** The base text of the message. */
    val text: String,
    /** An optional key, indicating where the error occurred. */
    val key: Any? = null,
): RuntimeException(extendMessage(text, key)) {

    /** The extended message */
    override val message: String
        get() = super.message!! // message will always be non-null

    /**
     * Add a "cause" [Throwable].
     */
    fun withCause(throwable: Throwable): JSONException = apply {
        initCause(throwable)
    }

    companion object {

        /**
         * Create extended message from base message text and optional key.
         */
        fun extendMessage(text: String, key: Any? = null): String =
            key?.toString()?.takeIf { it.isNotEmpty() }?.let {
                "$text, at $it"
            } ?: text

    }

}
