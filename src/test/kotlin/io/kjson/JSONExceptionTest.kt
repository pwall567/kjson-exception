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

import java.net.URI

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

    @Test fun `should create extended message without creating exception`() {
        expect("Something went wrong") { JSONException.extendMessage("Something went wrong") }
        expect("Something went wrong, at startup") { JSONException.extendMessage("Something went wrong", "startup") }
    }

    @Test fun `should allow simple derived exception class`() {
        val baseMessage = "Something's on fire"
        val derivedException = SimpleDerivedException(baseMessage, "home")
        expect("Something's on fire, at home") { derivedException.message }
        expect(baseMessage) { derivedException.text }
        expect("home") { derivedException.key}
    }

    @Test fun `should allow strongly-typed key in derived exception class`() {
        val uri = URI("http://example.com/dummy.page")
        val baseMessage = "Something else went wrong"
        val derivedException = URIDerivedException(baseMessage, uri)
        expect("$baseMessage, at $uri") { derivedException.message }
        expect(baseMessage) { derivedException.text }
        val exceptionURI: URI = derivedException.key
        expect(uri) { exceptionURI }
    }

    @Test fun `should add cause to derived exception class`() {
        val baseMessage = "Something's on fire"
        val derivedException = SimpleDerivedException(baseMessage, "home").withCause(RuntimeException("nested 2"))
        expect("Something's on fire, at home") { derivedException.message }
        expect(baseMessage) { derivedException.text }
        expect("home") { derivedException.key}
        assertNotNull(derivedException.cause) {
            assertIs<RuntimeException>(it)
            expect("nested 2") { it.message }
        }
    }

    class SimpleDerivedException(
        text: String,
        key: Any?,
    ) : JSONException(text, key)

    class URIDerivedException(
        text: String,
        override val key: URI
    ) : JSONException(text, key)

}
