/*
 * @(#) JSONExceptionTest.kt
 *
 * kjson-exception  Exception class for JSON projects
 * Copyright (c) 2024 Peter Wall
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

import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.expect

class JSONExceptionTest {

    @Test fun `should create JSONException with simple message`() {
        val e = JSONException("simple message")
        expect("simple message") { e.message }
        expect("simple message") { e.text }
        assertNull(e.key)
        assertNull(e.cause)
    }

    @Test fun `should create JSONException with message including key`() {
        val e = JSONException("Something went wrong", "end of file")
        expect("Something went wrong, at end of file") { e.message }
        expect("Something went wrong") { e.text }
        expect("end of file") { e.key }
        assertNull(e.cause)
    }

    @Test fun `should add cause to JSONException`() {
        val e = JSONException("Something went wrong", "end of file").withCause(RuntimeException("nested"))
        expect("Something went wrong, at end of file") { e.message }
        expect("Something went wrong") { e.text }
        expect("end of file") { e.key }
        assertNotNull(e.cause) {
            assertIs<RuntimeException>(it)
            expect("nested") { it.message }
        }
    }

    @Test fun `should create extended message`() {
        expect("Something went wrong") { JSONException.extendMessage("Something went wrong") }
        expect("Something went wrong, at startup") { JSONException.extendMessage("Something went wrong", "startup") }
    }

}
