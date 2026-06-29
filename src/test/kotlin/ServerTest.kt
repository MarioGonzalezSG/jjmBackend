package com.jjm

import com.jjm.jjmbackend.module
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.*

class ServerTest {

    @Test
    fun `test health endpoint`() = testApplication {
        application {
            module()
        }
        // verify health endpoint returns 200
        assertEquals(HttpStatusCode.OK, client.get("/health").status)
    }

}
