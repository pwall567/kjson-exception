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

import java.net.URI

import io.kstuff.test.shouldBe
import io.kstuff.test.shouldBeNonNull
import io.kstuff.test.shouldBeType

class JSONExceptionTest {

    @Test fun `should create JSONException with simple message`() {
        val e = JSONException("simple message")
        e.message shouldBe "simple message"
        e.text shouldBe "simple message"
        e.key shouldBe null
        e.cause shouldBe null
    }

    @Test fun `should create JSONException with message including key`() {
        val e = JSONException("Something went wrong", "end of file")
        e.message shouldBe "Something went wrong, at end of file"
        e.text shouldBe "Something went wrong"
        e.key shouldBe "end of file"
        e.cause shouldBe null
    }

    @Test fun `should add cause to JSONException`() {
        val e = JSONException("Something went wrong", "end of file").withCause(RuntimeException("nested"))
        e.message shouldBe "Something went wrong, at end of file"
        e.text shouldBe "Something went wrong"
        e.key shouldBe "end of file"
        e.cause.shouldBeNonNull().let {
            it.shouldBeType<RuntimeException>()
            it.message shouldBe "nested"
        }
    }

    @Test fun `should create extended message without creating exception`() {
        JSONException.extendMessage("Something went wrong") shouldBe "Something went wrong"
        JSONException.extendMessage("Something went wrong", "startup") shouldBe "Something went wrong, at startup"
    }

    @Test fun `should allow simple derived exception class`() {
        val baseMessage = "Something's on fire"
        val derivedException = SimpleDerivedException(baseMessage, "home")
        derivedException.message shouldBe "Something's on fire, at home"
        derivedException.text shouldBe baseMessage
        derivedException.key shouldBe "home"
    }

    @Test fun `should allow strongly-typed key in derived exception class`() {
        val uri = URI("http://example.com/dummy.page")
        val baseMessage = "Something else went wrong"
        val derivedException = URIDerivedException(baseMessage, uri)
        derivedException.message shouldBe "$baseMessage, at $uri"
        derivedException.text shouldBe baseMessage
        val exceptionURI: URI = derivedException.key
        exceptionURI shouldBe uri
    }

    @Test fun `should add cause to derived exception class`() {
        val baseMessage = "Something's on fire"
        val derivedException = SimpleDerivedException(baseMessage, "home").withCause(RuntimeException("nested 2"))
        derivedException.message shouldBe "Something's on fire, at home"
        derivedException.text shouldBe baseMessage
        derivedException.key shouldBe "home"
        derivedException.cause.shouldBeNonNull().let {
            it.shouldBeType<RuntimeException>()
            it.message shouldBe "nested 2"
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
